"""
This unit provides a base class for something that is rendered, this can be
a page, a pane, a block, even part of XUL like the outlinePane but not really
going down to the element level. We'll call them rendering components.
It provides a way to get at your parent rendering component, the top bieng the
mainpage, who has no parent. It also it provides you with a config instance and
a package instance.  Finally it makes you a LivePage and a Resource descendant,
but you don't have to use that functionality. It means you can use a rendering
template to do your rendering, even if you're part of a bigger block.
"""
from nevow import loaders
from nevow.livepage import LivePage
from twisted.web.resource import Resource
import re
Unset = object()
DontHave = object()
labelRe = re.compile(r'(label\s*=\s*")([^"]*)(")', re.I)
accesskeyRe = re.compile(r'(accesskey\s*=\s*")([^"]*)(")', re.I)
class Renderable(object):
    """
    A base class for all things rendered
    """
    _templateFileName = ''
    name = None # Must provide a name in dervied classes, or pass one to
    docFactory = None
    def __init__(self, parent, package=None, webServer=None, name=None):
        """
        Pass me a 'parent' rendering component,
        a 'package' that I'm rendering for
        and a 'webServer' instance reference (from webServer.py)
        If you don't pass 'webServer' and 'package' I'll
        get them from 'parent'
        'name' is a identifier to distuniguish us from the other children of our
        parent
        """
        self.parent = parent # This is the same for both blocks and pages
        if name:
            self.name = name
        elif not self.name:
            raise AssertionError('Element of class "%s" created with no name.' %
                                 self.__class__.__name__)
        self.config = Unset
        self.ideviceStore = Unset
        self.packageStore = Unset
        if parent:
            parent.renderChildren[self.name] = self
        self.renderChildren = {}
        if package:
            self.package = package
        elif parent:
            self.package = parent.package
        else:
            self.package = None
        if webServer:
            self.webServer = webServer
        elif parent:
            self.webServer = parent.webServer
        else:
            self.webServer = None
        if self._templateFileName:
            if hasattr(self, 'config') and self.config:
                pth = self.config.webDir/'templates'/self._templateFileName
                self.docFactory = loaders.xmlfile(pth)
            else:
                self.docFactory = loaders.xmlfile(self._templateFileName)
    def getRoot(self):
        """
        Returns the highest renderable in the tree
        that doesn't have a parent.
        Basically 'PackageRedirector'
        """
        renderable = self
        while renderable.parent:
            renderable = renderable.parent
        return renderable
    root = property(getRoot)
    def delete(self):
        """
        Removes our self from our parents tree
        """
        del self.parent.renderChildren[self.name]
    def __getattribute__(self, attr):
        """
        Sets unset attributes.
        """
        def baseget(name):
            """
            Gets values the old proper way
            but instead of raising AttributeErro
            returns the constant 'DontHave'
            """
            try:
                return object.__getattribute__(self, name)
            except AttributeError:
                return DontHave
        res = baseget(attr)
        if res is Unset or res is DontHave:
            if attr.startswith('render_'):
                name = attr.split('_', 1)[-1]
                res = baseget('renderChildren')[name].render
            elif baseget('webServer'):
                res = getattr(baseget('webServer').application, attr)
            setattr(self, attr, res)
        return res
    def process(self, request):
        """
        Called when a request comes in.
        This implementation automatically
        passes on the request to all our
        child renderables
        """
        for rc in self.renderChildren.values():
            if not isinstance(rc, _RenderablePage):
                rc.process(request)
class _RenderablePageMetaClass(type):
    """
    This is the metaclass which creates all descendants of renderable.
    FWe use this to make sure that all xul elements that pass though a render
    function and have a label attribute get translated
    """
    def __new__(meta, className, ancestors, dct):
        """Used to wrap the renderable methods in the translating renderable
        methods"""
        def wrapFunc(oldFunc):
            """
            Takes a render_x func and returns a wrapped version that will also
            translate label tags in the appropriate xul element.
            """
            def newFunc(self, ctx, data=None):
                """
                Wraps any render function and if the associated XUL attribute
                has a label tag, translates the label and then calls the
                original render func.
                """
                _RenderablePageMetaClass.translateCtx(ctx)
                return oldFunc(self, ctx, data)
            return newFunc
        for name, func in dct.items():
            if name.startswith('render_') and callable(func) and \
               name not in ('render_GET', 'render_POST'):
                dct[name] = wrapFunc(func)
        return type.__new__(meta, className, ancestors, dct)
    @staticmethod
    def translateCtx(ctx):
        """
        Translates a ctx according to the rules in exe/tools/mki18n.py
        """
        attributes = ctx.tag.attributes
        if 'label' in attributes:
            if 'accesskey' in attributes:
                toTranslate = 'label="%s" accesskey="%s"' % (
                    attributes.get('label'),
                    attributes.get('accesskey'))
                translated = _(toTranslate)
                labelMatch = labelRe.search(translated)
                if labelMatch:
                    label = labelMatch.group(2)
                else:
                    label = ''
                accesskeyMatch = accesskeyRe.search(translated)
                if accesskeyMatch:
                    accesskey = accesskeyMatch.group(2)
                else:
                    accesskey = ''
                ctx.tag.attributes['label'] = label
                ctx.tag.attributes['accesskey'] = accesskey
            else:
                ctx.tag.attributes['label'] = _(attributes['label'])
        elif ctx.tag.tagName == 'label':
            value = attributes.get('value')
            if value is not None:
                ctx.tag.attributes['value'] = _(value)
        elif ctx.tag.tagName == 'key':
            if 'key' in attributes:
                ctx.tag.attributes['key'] = _(attributes['key'])
            elif 'keycode' in attributes:
                ctx.tag.attributes['keycode'] = _(attributes['keycode'])
        elif ctx.tag.tagName == 'window':
            if 'title' in attributes:
                ctx.tag.attributes['title'] = _(attributes['title'])
        return ctx.tag
class _RenderablePage(Renderable):
    """
    For internal use only
    """
    __metaclass__ = _RenderablePageMetaClass
    def __init__(self, parent, package=None, config=None):
        """
        Same as Renderable.__init__ but uses putChild to put ourselves
        in our parents sub-page tree
        """
        Renderable.__init__(self, parent, package, config)
        if parent:
            self.parent.putChild(self.name, self)
    def render_translate(self, ctx, data):
        """
        Called for XUL elements that have no other rendering functions.
        Translates the label tag of the element
        This is automatically wrapped by translateCtx so we don't need to do
        anything.
        """
        return ctx
class RenderableResource(_RenderablePage, Resource):
    """
    It is a page and renderable, but not live
    """
    def __init__(self, parent, package=None, config=None):
        """
        See Renderable.__init__ docstring
        """
        Resource.__init__(self)
        _RenderablePage.__init__(self, parent, package, config)
class RenderableLivePage(_RenderablePage, LivePage):
    """
    This class is both a renderable and a LivePage/Resource
    """
    def __init__(self, parent, package=None, config=None):
        """
        Same as Renderable.__init__ but
        """
        LivePage.__init__(self)
        _RenderablePage.__init__(self, parent, package, config)
