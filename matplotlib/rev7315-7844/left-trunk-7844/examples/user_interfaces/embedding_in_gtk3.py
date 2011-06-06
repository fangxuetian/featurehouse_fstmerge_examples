"""
demonstrate adding a FigureCanvasGTK/GTKAgg widget to a gtk.ScrolledWindow
"""
import gtk
from matplotlib.figure import Figure
from numpy import arange, sin, pi
from matplotlib.backends.backend_gtkagg import FigureCanvasGTKAgg as FigureCanvas
win = gtk.Window()
win.connect("destroy", lambda x: gtk.main_quit())
win.set_default_size(400,300)
win.set_title("Embedding in GTK")
f = Figure(figsize=(5,4), dpi=100)
a = f.add_subplot(111)
t = arange(0.0,3.0,0.01)
s = sin(2*pi*t)
a.plot(t,s)
sw = gtk.ScrolledWindow()
win.add (sw)
sw.set_border_width (10)
sw.set_policy (hscrollbar_policy=gtk.POLICY_AUTOMATIC,
               vscrollbar_policy=gtk.POLICY_ALWAYS)
canvas = FigureCanvas(f)  # a gtk.DrawingArea
canvas.set_size_request(800,600)
sw.add_with_viewport (canvas)
win.show_all()
gtk.main()
