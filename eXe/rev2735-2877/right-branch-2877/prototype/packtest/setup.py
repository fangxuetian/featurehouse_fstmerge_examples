import glob
from distutils.core import setup
import py2exe
setup(console=["packtest.py"],
      version="1.1",
      py_modules=["page"],
      data_files=[('web', glob.glob("web/*")), ('.', ["README",])],
     )
