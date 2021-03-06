import matplotlib.mlab as mlab
from matplotlib.pyplot import figure, show
import numpy as np
x = np.arange(0.0, 2, 0.01)
y1 = np.sin(2*np.pi*x)
y2 = 1.2*np.sin(4*np.pi*x)
fig = figure()
ax1 = fig.add_subplot(311)
ax2 = fig.add_subplot(312, sharex=ax1)
ax3 = fig.add_subplot(313, sharex=ax1)
ax1.fill_between(x, 0, y1)
ax1.set_ylabel('between y1 and 0')
ax2.fill_between(x, y1, 1)
ax2.set_ylabel('between y1 and 1')
ax3.fill_between(x, y1, y2)
ax3.set_ylabel('between y1 and y2')
ax3.set_xlabel('x')
fig = figure()
ax = fig.add_subplot(211)
ax.plot(x, y1, x, y2, color='black')
ax.fill_between(x, y1, y2, where=y2>=y1, facecolor='green')
ax.fill_between(x, y1, y2, where=y2<=y1, facecolor='red')
ax.set_title('fill between where')
y2 = np.ma.masked_greater(y2, 1.0)
ax1 = fig.add_subplot(212, sharex=ax)
ax1.plot(x, y1, x, y2, color='black')
ax1.fill_between(x, y1, y2, where=y2>=y1, facecolor='green')
ax1.fill_between(x, y1, y2, where=y2<=y1, facecolor='red')
ax1.set_title('Now regions with y2>1 are masked')
fig = figure()
ax = fig.add_subplot(111)
y = np.sin(4*np.pi*x)
ax.plot(x, y, color='black')
import matplotlib.transforms as mtransforms
trans = mtransforms.blended_transform_factory(ax.transData, ax.transAxes)
theta = 0.9
ax.axhline(theta, color='green', lw=2, alpha=0.5)
ax.axhline(-theta, color='red', lw=2, alpha=0.5)
ax.fill_between(x, 0, 1, where=y>theta, facecolor='green', alpha=0.5, transform=trans)
ax.fill_between(x, 0, 1, where=y<-theta, facecolor='red', alpha=0.5, transform=trans)
show()
