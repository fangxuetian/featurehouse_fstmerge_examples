USE_TRAITED_CONFIG = True
from rcparams import rc
from cutils import get_config_file
if USE_TRAITED_CONFIG:
    from mplconfig import rcParams, mplConfig, save_config, rcdefaults
else:
    from rcparams import rcParams, rcdefaults
