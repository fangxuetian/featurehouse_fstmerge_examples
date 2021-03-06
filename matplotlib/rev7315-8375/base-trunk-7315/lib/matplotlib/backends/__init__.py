import matplotlib
import inspect
import warnings
from matplotlib.rcsetup import interactive_bk
__all__ = ['backend','show','draw_if_interactive',
           'new_figure_manager', 'backend_version']
backend = matplotlib.get_backend() # validates, to match all_backends
def pylab_setup():
    'return new_figure_manager, draw_if_interactive and show for pylab'
    if backend.startswith('module://'):
        backend_name = backend[9:]
    else:
        backend_name = 'backend_'+backend
        backend_name = backend_name.lower() # until we banish mixed case
        backend_name = 'matplotlib.backends.%s'%backend_name.lower()
    backend_mod = __import__(backend_name,
                             globals(),locals(),[backend_name])
    new_figure_manager = backend_mod.new_figure_manager
    def do_nothing_show(*args, **kwargs):
        frame = inspect.currentframe()
        fname = frame.f_back.f_code.co_filename
        if fname in ('<stdin>', '<ipython console>'):
            warnings.warn("""
Your currently selected backend, '%s' does not support show().
Please select a GUI backend in your matplotlibrc file ('%s')
or with matplotlib.use()""" %
                          (backend, matplotlib.matplotlib_fname()))
    def do_nothing(*args, **kwargs): pass
    backend_version = getattr(backend_mod,'backend_version', 'unknown')
    show = getattr(backend_mod, 'show', do_nothing_show)
    draw_if_interactive = getattr(backend_mod, 'draw_if_interactive', do_nothing)
    if backend.lower() in ['wx','wxagg']:
        Toolbar = backend_mod.Toolbar
        __all__.append('Toolbar')
    matplotlib.verbose.report('backend %s version %s' % (backend,backend_version))
    return new_figure_manager, draw_if_interactive, show
