"""
Place a legend on the axes at location loc.  Labels are a
sequence of strings and loc can be a string or an integer
specifying the legend location
The location codes are
  'best'         : 0, (only implemented for axis legends)
  'upper right'  : 1,
  'upper left'   : 2,
  'lower left'   : 3,
  'lower right'  : 4,
  'right'        : 5,
  'center left'  : 6,
  'center right' : 7,
  'lower center' : 8,
  'upper center' : 9,
  'center'       : 10,
Return value is a sequence of text, line instances that make
up the legend
"""
from __future__ import division
import warnings
import numpy as np
from matplotlib import rcParams
from matplotlib.artist import Artist, allow_rasterization
from matplotlib.cbook import is_string_like, iterable, silent_list, safezip
from matplotlib.font_manager import FontProperties
from matplotlib.lines import Line2D
from matplotlib.patches import Patch, Rectangle, Shadow, FancyBboxPatch
from matplotlib.collections import LineCollection, RegularPolyCollection, \
     CircleCollection
from matplotlib.transforms import Bbox, BboxBase, TransformedBbox, BboxTransformTo
from matplotlib.offsetbox import HPacker, VPacker, TextArea, DrawingArea
class Legend(Artist):
    """
    Place a legend on the axes at location loc.  Labels are a
    sequence of strings and loc can be a string or an integer
    specifying the legend location
    The location codes are::
      'best'         : 0, (only implemented for axis legends)
      'upper right'  : 1,
      'upper left'   : 2,
      'lower left'   : 3,
      'lower right'  : 4,
      'right'        : 5,
      'center left'  : 6,
      'center right' : 7,
      'lower center' : 8,
      'upper center' : 9,
      'center'       : 10,
    loc can be a tuple of the noramilzed coordinate values with
    respect its parent.
    Return value is a sequence of text, line instances that make
    up the legend
    """
    codes = {'best'         : 0, # only implemented for axis legends
             'upper right'  : 1,
             'upper left'   : 2,
             'lower left'   : 3,
             'lower right'  : 4,
             'right'        : 5,
             'center left'  : 6,
             'center right' : 7,
             'lower center' : 8,
             'upper center' : 9,
             'center'       : 10,
             }
    zorder = 5
    def __str__(self):
        return "Legend"
    def __init__(self, parent, handles, labels,
                 loc = None,
                 numpoints = None,     # the number of points in the legend line
                 markerscale = None,   # the relative size of legend markers vs. original
                 scatterpoints = 3,    # TODO: may be an rcParam
                 scatteryoffsets=None,
                 prop = None,          # properties for the legend texts
                 pad = None,           # deprecated; use borderpad
                 labelsep = None,      # deprecated; use labelspacing
                 handlelen = None,     # deprecated; use handlelength
                 handletextsep = None, # deprecated; use handletextpad
                 axespad = None,       # deprecated; use borderaxespad
                 borderpad = None,     # the whitespace inside the legend border
                 labelspacing=None, #the vertical space between the legend entries
                 handlelength=None, # the length of the legend handles
                 handletextpad=None, # the pad between the legend handle and text
                 borderaxespad=None, # the pad between the axes and legend border
                 columnspacing=None, # spacing between columns
                 ncol=1, # number of columns
                 mode=None, # mode for horizontal distribution of columns. None, "expand"
                 fancybox=None, # True use a fancy box, false use a rounded box, none use rc
                 shadow = None,
                 title = None, # set a title for the legend
                 bbox_to_anchor = None, # bbox that the legend will be anchored.
                 bbox_transform = None, # transform for the bbox
                 ):
        """
        - *parent* : the artist that contains the legend
        - *handles* : a list of artists (lines, patches) to add to the legend
        - *labels* : a list of strings to label the legend
        Optional keyword arguments:
        ================   ==================================================================
        Keyword            Description
        ================   ==================================================================
        loc                a location code
        prop               the font property
        markerscale        the relative size of legend markers vs. original
        numpoints          the number of points in the legend for line
        scatterpoints      the number of points in the legend for scatter plot
        scatteryoffsets    a list of yoffsets for scatter symbols in legend
        fancybox           if True, draw a frame with a round fancybox.  If None, use rc
        shadow             if True, draw a shadow behind legend
        ncol               number of columns
        borderpad          the fractional whitespace inside the legend border
        labelspacing       the vertical space between the legend entries
        handlelength       the length of the legend handles
        handletextpad      the pad between the legend handle and text
        borderaxespad      the pad between the axes and legend border
        columnspacing      the spacing between columns
        title              the legend title
        bbox_to_anchor     the bbox that the legend will be anchored.
        bbox_transform     the transform for the bbox. transAxes if None.
        ================   ==================================================================
The dimensions of pad and spacing are given as a fraction of the
_fontsize. Values from rcParams will be used if None.
Users can specify any arbitrary location for the legend using the
*bbox_to_anchor* keyword argument. bbox_to_anchor can be an instance
of BboxBase(or its derivatives) or a tuple of 2 or 4 floats.
See :meth:`set_bbox_to_anchor` for more detail.
The legend location can be specified by setting *loc* with a tuple of
2 floats, which is interpreted as the lower-left corner of the legend
in the normalized axes coordinate.
        """
        from matplotlib.axes import Axes     # local import only to avoid circularity
        from matplotlib.figure import Figure # local import only to avoid circularity
        Artist.__init__(self)
        if prop is None:
            self.prop=FontProperties(size=rcParams["legend.fontsize"])
        elif isinstance(prop, dict):
            self.prop=FontProperties(**prop)
            if "size" not in prop:
                self.prop.set_size(rcParams["legend.fontsize"])
        else:
            self.prop=prop
        self._fontsize = self.prop.get_size_in_points()
        propnames=['numpoints', 'markerscale', 'shadow', "columnspacing",
                   "scatterpoints"]
        localdict = locals()
        for name in propnames:
            if localdict[name] is None:
                value = rcParams["legend."+name]
            else:
                value = localdict[name]
            setattr(self, name, value)
        deprecated_kwds = {"pad":"borderpad",
                           "labelsep":"labelspacing",
                           "handlelen":"handlelength",
                           "handletextsep":"handletextpad",
                           "axespad":"borderaxespad"}
        bbox = parent.bbox
        axessize_fontsize = min(bbox.width, bbox.height)/self._fontsize
        for k, v in deprecated_kwds.items():
            if localdict[k] is not None and localdict[v] is None:
                warnings.warn("Use '%s' instead of '%s'." % (v, k),
                              DeprecationWarning)
                setattr(self, v, localdict[k]*axessize_fontsize)
                continue
            if localdict[v] is None:
                setattr(self, v, rcParams["legend."+v])
            else:
                setattr(self, v, localdict[v])
        del localdict
        handles = list(handles)
        if len(handles)<2:
            ncol = 1
        self._ncol = ncol
        if self.numpoints <= 0:
            raise ValueError("numpoints must be >= 0; it was %d"% numpoints)
        if scatteryoffsets is None:
            self._scatteryoffsets = np.array([3./8., 4./8., 2.5/8.])
        else:
            self._scatteryoffsets = np.asarray(scatteryoffsets)
        reps =  int(self.numpoints / len(self._scatteryoffsets)) + 1
        self._scatteryoffsets = np.tile(self._scatteryoffsets, reps)[:self.scatterpoints]
        self._legend_box = None
        if isinstance(parent,Axes):
            self.isaxes = True
            self.set_figure(parent.figure)
        elif isinstance(parent,Figure):
            self.isaxes = False
            self.set_figure(parent)
        else:
            raise TypeError("Legend needs either Axes or Figure as parent")
        self.parent = parent
        if loc is None:
            loc = rcParams["legend.loc"]
            if not self.isaxes and loc in [0,'best']:
                loc = 'upper right'
        if is_string_like(loc):
            if loc not in self.codes:
                if self.isaxes:
                    warnings.warn('Unrecognized location "%s". Falling back on "best"; '
                                  'valid locations are\n\t%s\n'
                                  % (loc, '\n\t'.join(self.codes.keys())))
                    loc = 0
                else:
                    warnings.warn('Unrecognized location "%s". Falling back on "upper right"; '
                                  'valid locations are\n\t%s\n'
                                   % (loc, '\n\t'.join(self.codes.keys())))
                    loc = 1
            else:
                loc = self.codes[loc]
        if not self.isaxes and loc == 0:
            warnings.warn('Automatic legend placement (loc="best") not implemented for figure legend. '
                          'Falling back on "upper right".')
            loc = 1
        self._loc = loc
        self._mode = mode
        self.set_bbox_to_anchor(bbox_to_anchor, bbox_transform)
        self.legendPatch = FancyBboxPatch(
            xy=(0.0, 0.0), width=1., height=1.,
            facecolor='w', edgecolor='k',
            mutation_scale=self._fontsize,
            snap=True
            )
        if fancybox is None:
            fancybox = rcParams["legend.fancybox"]
        if fancybox == True:
            self.legendPatch.set_boxstyle("round",pad=0,
                                          rounding_size=0.2)
        else:
            self.legendPatch.set_boxstyle("square",pad=0)
        self._set_artist_props(self.legendPatch)
        self._drawFrame = True
        self._init_legend_box(handles, labels)
        self.set_title(title)
        self._last_fontsize_points = self._fontsize
    def _set_artist_props(self, a):
        """
        set the boilerplate props for artists added to axes
        """
        a.set_figure(self.figure)
        for c in self.get_children():
            c.set_figure(self.figure)
        a.set_transform(self.get_transform())
    def _findoffset_best(self, width, height, xdescent, ydescent, renderer):
        "Helper function to locate the legend at its best position"
        ox, oy = self._find_best_position(width, height, renderer)
        return ox+xdescent, oy+ydescent
    def _findoffset_loc(self, width, height, xdescent, ydescent, renderer):
        "Heper function to locate the legend using the location code"
        if iterable(self._loc) and len(self._loc)==2:
            fx, fy = self._loc
            bbox = self.get_bbox_to_anchor()
            x, y = bbox.x0 + bbox.width * fx, bbox.y0 + bbox.height * fy
        else:
            bbox = Bbox.from_bounds(0, 0, width, height)
            x, y = self._get_anchored_bbox(self._loc, bbox, self.get_bbox_to_anchor(), renderer)
        return x+xdescent, y+ydescent
    @allow_rasterization
    def draw(self, renderer):
        "Draw everything that belongs to the legend"
        if not self.get_visible(): return
        renderer.open_group('legend')
        if self._loc == 0:
            _findoffset = self._findoffset_best
        else:
            _findoffset = self._findoffset_loc
        def findoffset(width, height, xdescent, ydescent):
            return _findoffset(width, height, xdescent, ydescent, renderer)
        self._legend_box.set_offset(findoffset)
        fontsize = renderer.points_to_pixels(self._fontsize)
        if self._mode in ["expand"]:
            pad = 2*(self.borderaxespad+self.borderpad)*fontsize
            self._legend_box.set_width(self.get_bbox_to_anchor().width-pad)
        if self._drawFrame:
            bbox = self._legend_box.get_window_extent(renderer)
            self.legendPatch.set_bounds(bbox.x0, bbox.y0,
                                        bbox.width, bbox.height)
            self.legendPatch.set_mutation_scale(fontsize)
            if self.shadow:
                shadow = Shadow(self.legendPatch, 2, -2)
                shadow.draw(renderer)
            self.legendPatch.draw(renderer)
        self._legend_box.draw(renderer)
        renderer.close_group('legend')
    def _approx_text_height(self, renderer=None):
        """
        Return the approximate height of the text. This is used to place
        the legend handle.
        """
        if renderer is None:
            return self._fontsize
        else:
            return renderer.points_to_pixels(self._fontsize)
    def _init_legend_box(self, handles, labels):
        """
        Initiallize the legend_box. The legend_box is an instance of
        the OffsetBox, which is packed with legend handles and
        texts. Once packed, their location is calculated during the
        drawing time.
        """
        fontsize = self._fontsize
        text_list = []  # the list of text instances
        handle_list = []  # the list of text instances
        label_prop = dict(verticalalignment='baseline',
                          horizontalalignment='left',
                          fontproperties=self.prop,
                          )
        labelboxes = []
        for l in labels:
            textbox = TextArea(l, textprops=label_prop,
                               multilinebaseline=True, minimumdescent=True)
            text_list.append(textbox._text)
            labelboxes.append(textbox)
        handleboxes = []
        height = self._approx_text_height() * 0.7
        descent = 0.
        for handle in handles:
            if isinstance(handle, RegularPolyCollection) or \
                   isinstance(handle, CircleCollection):
                npoints = self.scatterpoints
            else:
                npoints = self.numpoints
            if npoints > 1:
                xdata = np.linspace(0.3*fontsize,
                                    (self.handlelength-0.3)*fontsize,
                                    npoints)
                xdata_marker = xdata
            elif npoints == 1:
                xdata = np.linspace(0, self.handlelength*fontsize, 2)
                xdata_marker = [0.5*self.handlelength*fontsize]
            if isinstance(handle, Line2D):
                ydata = ((height-descent)/2.)*np.ones(xdata.shape, float)
                legline = Line2D(xdata, ydata)
                legline.update_from(handle)
                self._set_artist_props(legline) # after update
                legline.set_clip_box(None)
                legline.set_clip_path(None)
                legline.set_drawstyle('default')
                legline.set_marker('None')
                handle_list.append(legline)
                legline_marker = Line2D(xdata_marker, ydata[:len(xdata_marker)])
                legline_marker.update_from(handle)
                self._set_artist_props(legline_marker)
                legline_marker.set_clip_box(None)
                legline_marker.set_clip_path(None)
                legline_marker.set_linestyle('None')
                legline._legmarker = legline_marker
            elif isinstance(handle, Patch):
                p = Rectangle(xy=(0., 0.),
                              width = self.handlelength*fontsize,
                              height=(height-descent),
                              )
                p.update_from(handle)
                self._set_artist_props(p)
                p.set_clip_box(None)
                p.set_clip_path(None)
                handle_list.append(p)
            elif isinstance(handle, LineCollection):
                ydata = ((height-descent)/2.)*np.ones(xdata.shape, float)
                legline = Line2D(xdata, ydata)
                self._set_artist_props(legline)
                legline.set_clip_box(None)
                legline.set_clip_path(None)
                lw = handle.get_linewidth()[0]
                dashes = handle.get_dashes()[0]
                color = handle.get_colors()[0]
                legline.set_color(color)
                legline.set_linewidth(lw)
                if dashes[0] is not None: # dashed line
                    legline.set_dashes(dashes[1])
                handle_list.append(legline)
            elif isinstance(handle, RegularPolyCollection):
                ydata = height*self._scatteryoffsets
                size_max, size_min = max(handle.get_sizes()),\
                                     min(handle.get_sizes())
                if self.scatterpoints < 4:
                    sizes = [.5*(size_max+size_min), size_max,
                             size_min]
                else:
                    sizes = (size_max-size_min)*np.linspace(0,1,self.scatterpoints)+size_min
                p = type(handle)(handle.get_numsides(),
                                 rotation=handle.get_rotation(),
                                 sizes=sizes,
                                 offsets=zip(xdata_marker,ydata),
                                 transOffset=self.get_transform(),
                                 )
                p.update_from(handle)
                p.set_figure(self.figure)
                p.set_clip_box(None)
                p.set_clip_path(None)
                handle_list.append(p)
            elif isinstance(handle, CircleCollection):
                ydata = height*self._scatteryoffsets
                size_max, size_min = max(handle.get_sizes()),\
                                     min(handle.get_sizes())
                if self.scatterpoints < 4:
                    sizes = [.5*(size_max+size_min), size_max,
                             size_min]
                else:
                    sizes = (size_max-size_min)*np.linspace(0,1,self.scatterpoints)+size_min
                p = type(handle)(sizes,
                                 offsets=zip(xdata_marker,ydata),
                                 transOffset=self.get_transform(),
                                 )
                p.update_from(handle)
                p.set_figure(self.figure)
                p.set_clip_box(None)
                p.set_clip_path(None)
                handle_list.append(p)
            else:
                handle_list.append(None)
            handlebox = DrawingArea(width=self.handlelength*fontsize,
                                    height=height,
                                    xdescent=0., ydescent=descent)
            handle = handle_list[-1]
            handlebox.add_artist(handle)
            if hasattr(handle, "_legmarker"):
                handlebox.add_artist(handle._legmarker)
            handleboxes.append(handlebox)
        ncol = min(self._ncol, len(handleboxes))
        nrows, num_largecol = divmod(len(handleboxes), ncol)
        num_smallcol = ncol-num_largecol
        largecol = safezip(range(0, num_largecol*(nrows+1), (nrows+1)),
                           [nrows+1] * num_largecol)
        smallcol = safezip(range(num_largecol*(nrows+1), len(handleboxes), nrows),
                           [nrows] * num_smallcol)
        handle_label = safezip(handleboxes, labelboxes)
        columnbox = []
        for i0, di in largecol+smallcol:
            itemBoxes = [HPacker(pad=0,
                                 sep=self.handletextpad*fontsize,
                                 children=[h, t], align="baseline")
                         for h, t in handle_label[i0:i0+di]]
            itemBoxes[-1].get_children()[1].set_minimumdescent(False)
            columnbox.append(VPacker(pad=0,
                                        sep=self.labelspacing*fontsize,
                                        align="baseline",
                                        children=itemBoxes))
        if self._mode == "expand":
            mode = "expand"
        else:
            mode = "fixed"
        sep = self.columnspacing*fontsize
        self._legend_handle_box = HPacker(pad=0,
                                          sep=sep, align="baseline",
                                          mode=mode,
                                          children=columnbox)
        self._legend_title_box = TextArea("")
        self._legend_box = VPacker(pad=self.borderpad*fontsize,
                                   sep=self.labelspacing*fontsize,
                                   align="center",
                                   children=[self._legend_title_box,
                                             self._legend_handle_box])
        self._legend_box.set_figure(self.figure)
        self.texts = text_list
        self.legendHandles = handle_list
    def _auto_legend_data(self):
        """
        Returns list of vertices and extents covered by the plot.
        Returns a two long list.
        First element is a list of (x, y) vertices (in
        display-coordinates) covered by all the lines and line
        collections, in the legend's handles.
        Second element is a list of bounding boxes for all the patches in
        the legend's handles.
        """
        assert self.isaxes # should always hold because function is only called internally
        ax = self.parent
        vertices = []
        bboxes = []
        lines = []
        for handle in ax.lines:
            assert isinstance(handle, Line2D)
            path = handle.get_path()
            trans = handle.get_transform()
            tpath = trans.transform_path(path)
            lines.append(tpath)
        for handle in ax.patches:
            assert isinstance(handle, Patch)
            if isinstance(handle, Rectangle):
                transform = handle.get_data_transform()
                bboxes.append(handle.get_bbox().transformed(transform))
            else:
                transform = handle.get_transform()
                bboxes.append(handle.get_path().get_extents(transform))
        return [vertices, bboxes, lines]
    def draw_frame(self, b):
        'b is a boolean.  Set draw frame to b'
        self._drawFrame = b
    def get_children(self):
        'return a list of child artists'
        children = []
        if self._legend_box:
            children.append(self._legend_box)
        return children
    def get_frame(self):
        'return the Rectangle instance used to frame the legend'
        return self.legendPatch
    def get_lines(self):
        'return a list of lines.Line2D instances in the legend'
        return [h for h in self.legendHandles if isinstance(h, Line2D)]
    def get_patches(self):
        'return a list of patch instances in the legend'
        return silent_list('Patch', [h for h in self.legendHandles if isinstance(h, Patch)])
    def get_texts(self):
        'return a list of text.Text instance in the legend'
        return silent_list('Text', self.texts)
    def set_title(self, title):
        'set the legend title'
        self._legend_title_box._text.set_text(title)
        if title:
            self._legend_title_box.set_visible(True)
        else:
            self._legend_title_box.set_visible(False)
    def get_title(self):
        'return Text instance for the legend title'
        return self._legend_title_box._text
    def get_window_extent(self):
        'return a extent of the the legend'
        return self.legendPatch.get_window_extent()
    def get_bbox_to_anchor(self):
        """
        return the bbox that the legend will be anchored
        """
        if self._bbox_to_anchor is None:
            return self.parent.bbox
        else:
            return self._bbox_to_anchor
    def set_bbox_to_anchor(self, bbox, transform=None):
        """
        set the bbox that the legend will be anchored.
        *bbox* can be a BboxBase instance, a tuple of [left, bottom,
        width, height] in the given transform (normalized axes
        coordinate if None), or a tuple of [left, bottom] where the
        width and height will be assumed to be zero.
        """
        if bbox is None:
            self._bbox_to_anchor = None
            return
        elif isinstance(bbox, BboxBase):
            self._bbox_to_anchor = bbox
        else:
            try:
                l = len(bbox)
            except TypeError:
                raise ValueError("Invalid argument for bbox : %s" % str(bbox))
            if l == 2:
                bbox = [bbox[0], bbox[1], 0, 0]
            self._bbox_to_anchor = Bbox.from_bounds(*bbox)
        if transform is None:
            transform = BboxTransformTo(self.parent.bbox)
        self._bbox_to_anchor = TransformedBbox(self._bbox_to_anchor,
                                               transform)
    def _get_anchored_bbox(self, loc, bbox, parentbbox, renderer):
        """
        Place the *bbox* inside the *parentbbox* according to a given
        location code. Return the (x,y) coordinate of the bbox.
        - loc: a location code in range(1, 11).
          This corresponds to the possible values for self._loc, excluding "best".
        - bbox: bbox to be placed, display coodinate units.
        - parentbbox: a parent box which will contain the bbox. In
            display coordinates.
        """
        assert loc in range(1,11) # called only internally
        BEST, UR, UL, LL, LR, R, CL, CR, LC, UC, C = range(11)
        anchor_coefs={UR:"NE",
                      UL:"NW",
                      LL:"SW",
                      LR:"SE",
                      R:"E",
                      CL:"W",
                      CR:"E",
                      LC:"S",
                      UC:"N",
                      C:"C"}
        c = anchor_coefs[loc]
        fontsize = renderer.points_to_pixels(self._fontsize)
        container = parentbbox.padded(-(self.borderaxespad) * fontsize)
        anchored_box = bbox.anchored(c, container=container)
        return anchored_box.x0, anchored_box.y0
    def _find_best_position(self, width, height, renderer, consider=None):
        """
        Determine the best location to place the legend.
        `consider` is a list of (x, y) pairs to consider as a potential
        lower-left corner of the legend. All are display coords.
        """
        assert self.isaxes # should always hold because function is only called internally
        verts, bboxes, lines = self._auto_legend_data()
        bbox = Bbox.from_bounds(0, 0, width, height)
        consider = [self._get_anchored_bbox(x, bbox, self.get_bbox_to_anchor(),
                                            renderer) for x in range(1, len(self.codes))]
        candidates = []
        for l, b in consider:
            legendBox = Bbox.from_bounds(l, b, width, height)
            badness = 0
            badness = legendBox.count_contains(verts)
            badness += legendBox.count_overlaps(bboxes)
            for line in lines:
                if line.intersects_bbox(legendBox):
                    badness += 1
            ox, oy = l, b
            if badness == 0:
                return ox, oy
            candidates.append((badness, (l, b)))
        minCandidate = candidates[0]
        for candidate in candidates:
            if candidate[0] < minCandidate[0]:
                minCandidate = candidate
        ox, oy = minCandidate[1]
        return ox, oy
