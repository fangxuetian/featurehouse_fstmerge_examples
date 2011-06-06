import os, sys
import matplotlib
matplotlib.use('QtAgg') # qt3 example
from qt import *
QApplication.setColorSpec(QApplication.NormalColor)
TRUE  = 1
FALSE = 0
ITERS = 1000
import pylab as p
import matplotlib.pyplot as plt
import numpy as np
import time
import pipong 
from numpy.random import randn, randint
class BlitQT(QObject):
    def __init__(self):
        QObject.__init__(self, None, "app")
        self.ax = plt.subplot(111)
        self.animation = pipong.Game(self.ax)
    def timerEvent(self, evt):
       self.animation.draw(evt)
plt.grid() # to ensure proper background restore
app = BlitQT()
app.tstart = time.time()
app.startTimer(0)
plt.show()
print 'FPS:' , app.animation.cnt/(time.time()-app.tstart)
