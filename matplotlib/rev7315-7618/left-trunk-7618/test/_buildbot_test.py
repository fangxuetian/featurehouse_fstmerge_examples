"""This script will install matplotlib to a virtual environment to
faciltate testing."""
import shutil, os, sys
from subprocess import Popen, PIPE, STDOUT
from _buildbot_util import check_call
TARGET=os.path.abspath('PYmpl')
if not os.path.exists(TARGET):
    raise RuntimeError('the virtualenv target directory was not found')
TARGET_py = os.path.join(TARGET,'bin','python')
check_call('%s -c "import shutil,matplotlib; x=matplotlib.get_configdir(); shutil.rmtree(x)"'%TARGET_py)
check_call('%s run-mpl-test.py --verbose --all --keep-failed'%TARGET_py,
           cwd='test')
