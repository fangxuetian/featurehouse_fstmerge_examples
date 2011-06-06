"""A special directive for including a matplotlib plot.
The source code for the plot may be included in one of two ways:
  1. A path to a source file as the argument to the directive::
       .. plot:: path/to/plot.py
     When a path to a source file is given, the content of the
     directive may optionally contain a caption for the plot::
       .. plot:: path/to/plot.py
          This is the caption for the plot
     Additionally, one my specify the name of a function to call (with
     no arguments) immediately after importing the module::
       .. plot:: path/to/plot.py plot_function1
  2. Included as inline content to the directive::
     .. plot::
        import matplotlib.pyplot as plt
        import matplotlib.image as mpimg
        import numpy as np
        img = mpimg.imread('_static/stinkbug.png')
        imgplot = plt.imshow(img)
In HTML output, `plot` will include a .png file with a link to a high-res
.png and .pdf.  In LaTeX output, it will include a .pdf.
To customize the size of the plot, this directive supports all of the
options of the `image` directive, except for `target` (since plot will
add its own target).  These include `alt`, `height`, `width`, `scale`,
`align` and `class`.
Additionally, if the `:include-source:` option is provided, the
literal source will be displayed inline in the text, (as well as a
link to the source in HTML).
The set of file formats to generate can be specified with the
`plot_formats` configuration variable.
"""
import sys, os, shutil, imp, warnings, cStringIO, re
try:
    from hashlib import md5
except ImportError:
    from md5 import md5
from docutils.parsers.rst import directives
try:
    from docutils.parsers.rst.directives.images import align
except ImportError:
    from docutils.parsers.rst.directives.images import Image
    align = Image.align
import sphinx
sphinx_version = sphinx.__version__.split(".")
sphinx_version = tuple([int(re.split('[a-z]', x)[0])
                        for x in sphinx_version[:2]])
import matplotlib
import matplotlib.cbook as cbook
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.image as image
from matplotlib import _pylab_helpers
from matplotlib.sphinxext import only_directives
if hasattr(os.path, 'relpath'):
    relpath = os.path.relpath
else:
    def relpath(target, base=os.curdir):
        """
        Return a relative path to the target from either the current dir or an optional base dir.
        Base can be a directory specified either as absolute or relative to current dir.
        """
        if not os.path.exists(target):
            raise OSError, 'Target does not exist: '+target
        if not os.path.isdir(base):
            raise OSError, 'Base is not a directory or does not exist: '+base
        base_list = (os.path.abspath(base)).split(os.sep)
        target_list = (os.path.abspath(target)).split(os.sep)
        if os.name in ['nt','dos','os2'] and base_list[0] <> target_list[0]:
            raise OSError, 'Target is on a different drive to base. Target: '+target_list[0].upper()+', base: '+base_list[0].upper()
        for i in range(min(len(base_list), len(target_list))):
            if base_list[i] <> target_list[i]: break
        else:
            i+=1
        rel_list = [os.pardir] * (len(base_list)-i) + target_list[i:]
        if rel_list:
            return os.path.join(*rel_list)
        else:
            return ""
template = """
.. htmlonly::
   %(links)s
   .. figure:: %(prefix)s%(tmpdir)s/%(outname)s.png
%(options)s
%(caption)s
.. latexonly::
   .. figure:: %(prefix)s%(tmpdir)s/%(outname)s.pdf
%(options)s
%(caption)s
"""
exception_template = """
.. htmlonly::
   [`source code <%(linkdir)s/%(basename)s.py>`__]
Exception occurred rendering plot.
"""
template_content_indent = '      '
def out_of_date(original, derived):
    """
    Returns True if derivative is out-of-date wrt original,
    both of which are full file paths.
    """
    return (not os.path.exists(derived) or
            (os.path.exists(original) and
             os.stat(derived).st_mtime < os.stat(original).st_mtime))
def run_code(plot_path, function_name, plot_code):
    """
    Import a Python module from a path, and run the function given by
    name, if function_name is not None.
    """
    if plot_code is not None:
        exec(plot_code)
    else:
        pwd = os.getcwd()
        path, fname = os.path.split(plot_path)
        sys.path.insert(0, os.path.abspath(path))
        stdout = sys.stdout
        sys.stdout = cStringIO.StringIO()
        os.chdir(path)
        fd = None
        try:
            fd = open(fname)
            module = imp.load_module(
                "__plot__", fd, fname, ('py', 'r', imp.PY_SOURCE))
        finally:
            del sys.path[0]
            os.chdir(pwd)
            sys.stdout = stdout
            if fd is not None:
                fd.close()
        if function_name is not None:
            getattr(module, function_name)()
def run_savefig(plot_path, basename, tmpdir, destdir, formats):
    """
    Once a plot script has been imported, this function runs savefig
    on all of the figures in all of the desired formats.
    """
    fig_managers = _pylab_helpers.Gcf.get_all_fig_managers()
    for i, figman in enumerate(fig_managers):
        for j, (format, dpi) in enumerate(formats):
            if len(fig_managers) == 1:
                outname = basename
            else:
                outname = "%s_%02d" % (basename, i)
            outname = outname + "." + format
            outpath = os.path.join(tmpdir, outname)
            try:
                figman.canvas.figure.savefig(outpath, dpi=dpi)
            except:
                s = cbook.exception_to_str("Exception saving plot %s" % plot_path)
                warnings.warn(s)
                return 0
            if j > 0:
                shutil.copyfile(outpath, os.path.join(destdir, outname))
    return len(fig_managers)
def clear_state():
    plt.close('all')
    matplotlib.rcdefaults()
    matplotlib.rcParams['figure.figsize'] = (5.5, 4.5)
def render_figures(plot_path, function_name, plot_code, tmpdir, destdir,
                   formats):
    """
    Run a pyplot script and save the low and high res PNGs and a PDF
    in outdir.
    """
    plot_path = str(plot_path)  # todo, why is unicode breaking this
    basedir, fname = os.path.split(plot_path)
    basename, ext = os.path.splitext(fname)
    all_exists = True
    for format, dpi in formats:
        outname = os.path.join(tmpdir, '%s.%s' % (basename, format))
        if out_of_date(plot_path, outname):
            all_exists = False
            break
    if all_exists:
        return 1
    i = 0
    while True:
        all_exists = True
        for format, dpi in formats:
            outname = os.path.join(
                tmpdir, '%s_%02d.%s' % (basename, i, format))
            if out_of_date(plot_path, outname):
                all_exists = False
                break
        if all_exists:
            i += 1
        else:
            break
    if i != 0:
        return i
    clear_state()
    try:
        run_code(plot_path, function_name, plot_code)
    except:
        s = cbook.exception_to_str("Exception running plot %s" % plot_path)
        warnings.warn(s)
        return 0
    num_figs = run_savefig(plot_path, basename, tmpdir, destdir, formats)
    if '__plot__' in sys.modules:
        del sys.modules['__plot__']
    return num_figs
def _plot_directive(plot_path, basedir, function_name, plot_code, caption,
                    options, state_machine):
    formats = setup.config.plot_formats
    if type(formats) == str:
        formats = eval(formats)
    fname = os.path.basename(plot_path)
    basename, ext = os.path.splitext(fname)
    rstdir, rstfile = os.path.split(state_machine.document.attributes['source'])
    outdir = os.path.join('plot_directive', basedir)
    reldir = relpath(setup.confdir, rstdir)
    linkdir = os.path.join(reldir, outdir)
    tmpdir = os.path.join('build', outdir)
    if sphinx_version < (0, 6):
        tmpdir = os.path.abspath(tmpdir)
        prefix = ''
    else:
        prefix = '/'
    if not os.path.exists(tmpdir):
        cbook.mkdirs(tmpdir)
    destdir = os.path.abspath(os.path.join(setup.app.builder.outdir, outdir))
    if not os.path.exists(destdir):
        cbook.mkdirs(destdir)
    caption = '\n'.join(map(lambda line: template_content_indent + line.strip(), caption.split('\n')))
    num_figs = render_figures(plot_path, function_name, plot_code, tmpdir,
                              destdir, formats)
    lines = []
    if options.has_key('include-source'):
        if plot_code is None:
            fd = open(plot_path, 'r')
            plot_code = fd.read()
            fd.close()
        lines.extend(['::', ''])
        lines.extend(['    %s' % row.rstrip()
                      for row in plot_code.split('\n')])
        lines.append('')
        del options['include-source']
    else:
        lines = []
    if num_figs > 0:
        options = ['%s:%s: %s' % (template_content_indent, key, val)
                   for key, val in options.items()]
        options = "\n".join(options)
        if plot_code is None:
            shutil.copyfile(plot_path, os.path.join(destdir, fname))
        for i in range(num_figs):
            if num_figs == 1:
                outname = basename
            else:
                outname = "%s_%02d" % (basename, i)
            links = []
            if plot_code is None:
                links.append('`source code <%(linkdir)s/%(basename)s.py>`__')
            for format, dpi in formats[1:]:
                links.append('`%s <%s/%s.%s>`__' % (format, linkdir, outname, format))
            if len(links):
                links = '[%s]' % (', '.join(links) % locals())
            else:
                links = ''
            lines.extend((template % locals()).split('\n'))
    else:
        lines.extend((exception_template % locals()).split('\n'))
    if len(lines):
        state_machine.insert_input(
            lines, state_machine.input_lines.source(0))
    return []
def plot_directive(name, arguments, options, content, lineno,
                   content_offset, block_text, state, state_machine):
    """
    Handle the arguments to the plot directive.  The real work happens
    in _plot_directive.
    """
    if len(arguments):
        plot_path = directives.uri(arguments[0])
        basedir = relpath(os.path.dirname(plot_path), setup.app.builder.srcdir)
        caption = '\n'.join(content)
        if len(arguments) == 2:
            function_name = arguments[1]
        else:
            function_name = None
        return _plot_directive(plot_path, basedir, function_name, None, caption,
                               options, state_machine)
    else:
        plot_code = '\n'.join(content)
        plot_path = md5(plot_code).hexdigest()[-10:]
        return _plot_directive(plot_path, 'inline', None, plot_code, '', options,
                               state_machine)
def setup(app):
    setup.app = app
    setup.config = app.config
    setup.confdir = app.confdir
    options = {'alt': directives.unchanged,
               'height': directives.length_or_unitless,
               'width': directives.length_or_percentage_or_unitless,
               'scale': directives.nonnegative_int,
               'align': align,
               'class': directives.class_option,
               'include-source': directives.flag }
    app.add_directive('plot', plot_directive, True, (0, 2, 0), **options)
    app.add_config_value(
        'plot_formats',
        [('png', 80), ('hires.png', 200), ('pdf', 50)],
        True)
