import math
import copy
from matplotlib import lines as mlines, axis as maxis, \
        patches as mpatches
import art3d
import proj3d
import numpy as np
def get_flip_min_max(coord, index, mins, maxs):
    if coord[index] == mins[index]:
        return maxs[index]
    else:
        return mins[index]
def move_from_center(coord, centers, deltas, axmask=(True, True, True)):
    '''Return a coordinate that is moved by "deltas" away from the center.'''
    coord = copy.copy(coord)
    for i in range(3):
        if not axmask[i]:
            continue
        if coord[i] < centers[i]:
            coord[i] -= deltas[i]
        else:
            coord[i] += deltas[i]
    return coord
def tick_update_position(tick, tickxs, tickys, labelpos):
    '''Update tick line and label position and style.'''
    for (label, on) in ((tick.label1, tick.label1On), \
            (tick.label2, tick.label2On)):
        if on:
            label.set_position(labelpos)
    tick.tick1On, tick.tick2On = True, False
    tick.tick1line.set_linestyle('-')
    tick.tick1line.set_marker('')
    tick.tick1line.set_data(tickxs, tickys)
    tick.gridline.set_data(0, 0)
class Axis(maxis.XAxis):
    _PLANES = (
        (0, 3, 7, 4), (1, 2, 6, 5),     # yz planes
        (0, 1, 5, 4), (3, 2, 6, 7),     # xz planes
        (0, 1, 2, 3), (4, 5, 6, 7),     # xy planes
    )
    _AXINFO = {
        'x': {'i': 0, 'tickdir': 1,
            'color': (0.95, 0.95, 0.95, 0.5)},
        'y': {'i': 1, 'tickdir': 0,
            'color': (0.90, 0.90, 0.90, 0.5)},
        'z': {'i': 2, 'tickdir': 0,
            'color': (0.925, 0.925, 0.925, 0.5)},
    }
    def __init__(self, adir, v_intervalx, d_intervalx, axes, *args, **kwargs):
        self.adir = adir
        self.d_interval = d_intervalx
        self.v_interval = v_intervalx
        maxis.XAxis.__init__(self, axes, *args, **kwargs)
        self.line = mlines.Line2D(xdata=(0, 0), ydata=(0, 0),
                                 linewidth=0.75,
                                 color=(0,0, 0,0),
                                 antialiased=True,
                           )
        self.has_pane = True
        self.pane = mpatches.Polygon(np.array([[0,0], [0,1], [1,0], [0,0]]),
                                    alpha=0.8,
                                    facecolor=(1,1,1,0),
                                    edgecolor=(1,1,1,0))
        self.axes._set_artist_props(self.line)
        self.axes._set_artist_props(self.pane)
        self.gridlines = art3d.Line3DCollection([], )
        self.axes._set_artist_props(self.gridlines)
        self.axes._set_artist_props(self.label)
        self.label._transform = self.axes.transData
        self.set_rotate_label(kwargs.get('rotate_label', None))
    def get_tick_positions(self):
        majorLocs = self.major.locator()
        self.major.formatter.set_locs(majorLocs)
        majorLabels = [self.major.formatter(val, i) for i, val in enumerate(majorLocs)]
        return majorLabels, majorLocs
    def get_major_ticks(self):
        ticks = maxis.XAxis.get_major_ticks(self)
        for t in ticks:
            def update_coords(renderer, self=t.label1):
                return text_update_coords(self, renderer)
            t.tick1line.set_transform(self.axes.transData)
            t.tick2line.set_transform(self.axes.transData)
            t.gridline.set_transform(self.axes.transData)
            t.label1.set_transform(self.axes.transData)
            t.label2.set_transform(self.axes.transData)
        return ticks
    def set_pane(self, xys, color):
        if self.has_pane:
            xys = np.asarray(xys)
            xys = xys[:,:2]
            self.pane.xy = xys
            self.pane.set_edgecolor(color)
            self.pane.set_facecolor(color)
            self.pane.set_alpha(color[-1])
    def set_rotate_label(self, val):
        '''
        Whether to rotate the axis label: True, False or None.
        If set to None the label will be rotated if longer than 4 chars.
        '''
        self._rotate_label = val
    def get_rotate_label(self, text):
        if self._rotate_label is not None:
            return self._rotate_label
        else:
            return len(text) > 4
    def draw(self, renderer):
        self.label._transform = self.axes.transData
        renderer.open_group('axis3d')
        majorTicks = self.get_major_ticks()
        majorLocs = self.major.locator()
        interval = self.get_view_interval()
        majorLocs = [loc for loc in majorLocs if \
                interval[0] < loc < interval[1]]
        self.major.formatter.set_locs(majorLocs)
        majorLabels = [self.major.formatter(val, i)
                       for i, val in enumerate(majorLocs)]
        minx, maxx, miny, maxy, minz, maxz = self.axes.get_w_lims()
        mins = (minx, miny, minz)
        maxs = (maxx, maxy, maxz)
        centers = [(maxv + minv) / 2 for minv, maxv in zip(mins, maxs)]
        deltas = [(maxv - minv) / 12 for minv, maxv in zip(mins, maxs)]
        mins = [minv - delta / 4 for minv, delta in zip(mins, deltas)]
        maxs = [maxv + delta / 4 for maxv, delta in zip(maxs, deltas)]
        vals = mins[0], maxs[0], mins[1], maxs[1], mins[2], maxs[2]
        tc = self.axes.tunit_cube(vals, renderer.M)
        avgz = [tc[p1][2] + tc[p2][2] + tc[p3][2] + tc[p4][2] for \
                p1, p2, p3, p4 in self._PLANES]
        highs = [avgz[2*i] < avgz[2*i+1] for i in range(3)]
        info = self._AXINFO[self.adir]
        index = info['i']
        if not highs[index]:
            plane = self._PLANES[2 * index]
        else:
            plane = self._PLANES[2 * index + 1]
        xys = [tc[p] for p in plane]
        self.set_pane(xys, info['color'])
        self.pane.draw(renderer)
        minmax = []
        for i, val in enumerate(highs):
            if val:
                minmax.append(maxs[i])
            else:
                minmax.append(mins[i])
        juggled = art3d.juggle_axes(0, 2, 1, self.adir)
        edgep1 = copy.copy(minmax)
        edgep1[juggled[0]] = get_flip_min_max(edgep1, juggled[0], mins, maxs)
        edgep2 = copy.copy(edgep1)
        edgep2[juggled[1]] = get_flip_min_max(edgep2, juggled[1], mins, maxs)
        pep = proj3d.proj_trans_points([edgep1, edgep2], renderer.M)
        self.line.set_data((pep[0][0], pep[0][1]), (pep[1][0], pep[1][1]))
        self.line.draw(renderer)
        xyz0 = []
        for val in majorLocs:
            coord = copy.copy(minmax)
            coord[index] = val
            xyz0.append(coord)
        dy = pep[1][1] - pep[1][0]
        dx = pep[0][1] - pep[0][0]
        lxyz = [(v1 + v2) / 2 for v1, v2 in zip(edgep1, edgep2)]
        labeldeltas = [1.3 * x for x in deltas]
        lxyz = move_from_center(lxyz, centers, labeldeltas)
        tlx, tly, tlz = proj3d.proj_transform(lxyz[0], lxyz[1], lxyz[2], \
                renderer.M)
        self.label.set_position((tlx, tly))
        if self.get_rotate_label(self.label.get_text()):
            angle = art3d.norm_text_angle(math.degrees(math.atan2(dy, dx)))
            self.label.set_rotation(angle)
        self.label.set_va('center')
        self.label.draw(renderer)
        xyz1 = copy.deepcopy(xyz0)
        newindex = (index + 1) % 3
        newval = get_flip_min_max(xyz1[0], newindex, mins, maxs)
        for i in range(len(majorLocs)):
            xyz1[i][newindex] = newval
        xyz2 = copy.deepcopy(xyz0)
        newindex = (index + 2) %  3
        newval = get_flip_min_max(xyz2[0], newindex, mins, maxs)
        for i in range(len(majorLocs)):
            xyz2[i][newindex] = newval
        lines = zip(xyz1, xyz0, xyz2)
        if self.axes._draw_grid:
            self.gridlines.set_segments(lines)
            self.gridlines.set_color([(0.9,0.9,0.9,1)] * len(lines))
            self.gridlines.draw(renderer, project=True)
        tickdir = info['tickdir']
        tickdelta = deltas[tickdir]
        if highs[tickdir]:
            ticksign = 1
        else:
            ticksign = -1
        for tick, loc, label in zip(majorTicks, majorLocs, majorLabels):
            if tick is None:
                continue
            pos = copy.copy(edgep1)
            pos[index] = loc
            pos[tickdir] = edgep1[tickdir] + 0.1 * ticksign * tickdelta
            x1, y1, z1 = proj3d.proj_transform(pos[0], pos[1], pos[2], \
                    renderer.M)
            pos[tickdir] = edgep1[tickdir] - 0.2 * ticksign * tickdelta
            x2, y2, z2 = proj3d.proj_transform(pos[0], pos[1], pos[2], \
                    renderer.M)
            labeldeltas = [0.6 * x for x in deltas]
            axmask = [True, True, True]
            axmask[index] = False
            pos[tickdir] = edgep1[tickdir]
            pos = move_from_center(pos, centers, labeldeltas, axmask)
            lx, ly, lz = proj3d.proj_transform(pos[0], pos[1], pos[2], \
                    renderer.M)
            tick_update_position(tick, (x1, x2), (y1, y2), (lx, ly))
            tick.set_label1(label)
            tick.set_label2(label)
            tick.draw(renderer)
        renderer.close_group('axis3d')
    def get_view_interval(self):
        """return the Interval instance for this axis view limits"""
        return self.v_interval
class XAxis(Axis):
    def get_data_interval(self):
        'return the Interval instance for this axis data limits'
        return self.axes.xy_dataLim.intervalx
class YAxis(Axis):
    def get_data_interval(self):
        'return the Interval instance for this axis data limits'
        return self.axes.xy_dataLim.intervaly
class ZAxis(Axis):
    def get_data_interval(self):
        'return the Interval instance for this axis data limits'
        return self.axes.zz_dataLim.intervalx
