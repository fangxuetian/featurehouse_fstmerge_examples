"""
This now uses the imshow command instead of pcolor which *is much
faster*
"""
from __future__ import division
import numpy as np
from matplotlib.pyplot import *
from matplotlib.collections import LineCollection
import matplotlib.cbook as cbook
if 1:   # load the data
    dfile = cbook.get_sample_data('s1045.ima', asfileobj=False)
    print 'loading image', dfile
    im = np.fromstring(file(dfile, 'rb').read(), np.uint16).astype(float)
    im.shape = 256, 256
if 1: # plot the MRI in pcolor
    subplot(221)
    imshow(im, cmap=cm.jet)
    axis('off')
if 1:  # plot the histogram of MRI intensity
    subplot(222)
    im = np.ravel(im)
    im = im[np.nonzero(im)] # ignore the background
    im = im/(2.0**15) # normalize
    hist(im, 100)
    xticks([-1, -.5, 0, .5, 1])
    yticks([])
    xlabel('intensity')
    ylabel('MRI density')
if 1:   # plot the EEG
    numSamples, numRows = 800,4
    eegfile = cbook.get_sample_data('eeg.dat', asfileobj=False)
    print 'loading eeg', eegfile
    data = np.fromstring(file(eegfile, 'rb').read(), float)
    data.shape = numSamples, numRows
    t = 10.0 * np.arange(numSamples, dtype=float)/numSamples
    ticklocs = []
    ax = subplot(212)
    xlim(0,10)
    xticks(np.arange(10))
    dmin = data.min()
    dmax = data.max()
    dr = (dmax - dmin)*0.7 # Crowd them a bit.
    y0 = dmin
    y1 = (numRows-1) * dr + dmax
    ylim(y0, y1)
    segs = []
    for i in range(numRows):
        segs.append(np.hstack((t[:,np.newaxis], data[:,i,np.newaxis])))
        ticklocs.append(i*dr)
    offsets = np.zeros((numRows,2), dtype=float)
    offsets[:,1] = ticklocs
    lines = LineCollection(segs, offsets=offsets,
                           transOffset=None,
                           )
    ax.add_collection(lines)
    ax.set_yticks(ticklocs)
    ax.set_yticklabels(['PG3', 'PG5', 'PG7', 'PG9'])
    xlabel('time (s)')
show()
