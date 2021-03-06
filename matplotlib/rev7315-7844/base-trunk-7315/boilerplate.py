import inspect
import random
import re
import sys
import types
sys.path.insert(0, './lib')
from matplotlib.axes import Axes
from matplotlib.cbook import dedent
_fmtplot = """\
def %(func)s(%(argspec)s):
    %(docstring)s
    %(ax)s = gca()
    %(washold)s = %(ax)s.ishold()
    %(sethold)s
    if hold is not None:
        %(ax)s.hold(hold)
    try:
        %(ret)s = %(ax)s.%(func)s(%(call)s)
        draw_if_interactive()
    finally:
        %(ax)s.hold(%(washold)s)
    %(mappable)s
    return %(ret)s
"""
_fmtmisc = """\
def %(func)s(%(argspec)s):
    %(docstring)s
    %(ret)s =  gca().%(func)s(%(call)s)
    draw_if_interactive()
    return %(ret)s
"""
_plotcommands = (
    'acorr',
    'arrow',
    'axhline',
    'axhspan',
    'axvline',
    'axvspan',
    'bar',
    'barh',
    'broken_barh',
    'boxplot',
    'cohere',
    'clabel',
    'contour',
    'contourf',
    'csd',
    'errorbar',
    'fill',
    'fill_between',
    'fill_betweenx',
    'hexbin',
    'hist',
    'hlines',
    'imshow',
    'loglog',
    'pcolor',
    'pcolormesh',
    'pie',
    'plot',
    'plot_date',
    'psd',
    'quiver',
    'quiverkey',
    'scatter',
    'semilogx',
    'semilogy',
    'specgram',
    'spy',
    'stem',
    'step',
    'vlines',
    'xcorr',
    'barbs',
    )
_misccommands = (
    'cla',
    'grid',
    'legend',
    'table',
    'text',
    'annotate',
    )
cmappable = {
    'contour' : 'if %(ret)s._A is not None: gci._current = %(ret)s',
    'contourf': 'if %(ret)s._A is not None: gci._current = %(ret)s',
    'hexbin' : 'gci._current = %(ret)s',
    'scatter' : 'gci._current = %(ret)s',
    'pcolor'  : 'gci._current = %(ret)s',
    'pcolormesh'  : 'gci._current = %(ret)s',
    'imshow'  : 'gci._current = %(ret)s',
    'spy'    : 'gci._current = %(ret)s',
    'quiver' : 'gci._current = %(ret)s',
    'specgram'  : 'gci._current = %(ret)s[-1]',
}
def format_value(value):
    """
    Format function default values as needed for inspect.formatargspec.
    The interesting part is a hard-coded list of functions used
    as defaults in pyplot methods.
    """
    if isinstance(value, types.FunctionType):
        if value.func_name in ('detrend_none', 'window_hanning'):
            return '=mlab.' + value.func_name
        if value.func_name == 'mean':
            return '=np.' + value.func_name
        raise ValueError, ('default value %s unknown to boilerplate.formatvalue'
                           % value)
    return '='+repr(value)
def remove_final_whitespace(string):
    """
    Return a copy of *string* with final whitespace removed from each line.
    """
    return '\n'.join(map(lambda x: x.rstrip(), string.split('\n')))
def make_docstring(cmd, mention_hold):
    func = getattr(Axes, cmd)
    docstring = inspect.getdoc(func)
    if docstring is None:
        return ""
    escaped = re.sub(r'\\', r'\\\\', docstring)
    if mention_hold:
        escaped += '''
Additional kwargs: hold = [True|False] overrides default hold state
'''
    return '"""'+escaped+'"""'
for fmt,cmdlist in (_fmtplot,_plotcommands),(_fmtmisc,_misccommands):
    for func in cmdlist:
        if func in cmappable:
            mappable = cmappable[func] % locals()
        else:
            mappable = ''
        docstring = make_docstring(func, fmt is _fmtplot)
        args, varargs, varkw, defaults = inspect.getargspec(getattr(Axes, func))
        args.pop(0) # remove 'self' argument
        if defaults is None:
            defaults = ()
        call = map(str, args)
        if varargs is not None:
            call.append('*'+varargs)
        if varkw is not None:
            call.append('**'+varkw)
        call = ', '.join(call)
        if varargs:
            sethold = "hold = %(varkw)s.pop('hold', None)" % locals()
        elif fmt is _fmtplot:
            args.append('hold')
            defaults = defaults + (None,)
            sethold = ''
        argspec = inspect.formatargspec(args, varargs, varkw, defaults,
                                        formatvalue=format_value)
        argspec = argspec[1:-1] # remove parens
        washold,ret,ax = 'washold', 'ret', 'ax'
        bad = set(args) | set((varargs, varkw))
        while washold in bad or ret in bad or ax in bad:
            washold = 'washold' + str(random.randrange(10**12))
            ret = 'ret' + str(random.randrange(10**12))
            ax = 'ax' + str(random.randrange(10**12))
        for reserved in ('gca', 'gci', 'draw_if_interactive'):
            if reserved in bad:
                raise ValueError, \
                    'Axes method %s has kwarg named %s' % (func, reserved)
        print remove_final_whitespace(fmt%locals())
_fmtcmap = """\
def %(name)s():
    '''
    set the default colormap to %(name)s and apply to current image if any.
    See help(colormaps) for more information
    '''
    rc('image', cmap='%(name)s')
    im = gci()
    if im is not None:
        im.set_cmap(cm.%(name)s)
    draw_if_interactive()
"""
cmaps = (
    'autumn',
    'bone',
    'cool',
    'copper',
    'flag',
    'gray' ,
    'hot',
    'hsv',
    'jet' ,
    'pink',
    'prism',
    'spring',
    'summer',
    'winter',
    'spectral'
)
for name in cmaps:
    print _fmtcmap%locals()
