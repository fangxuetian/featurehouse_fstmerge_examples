from backend import BackendManager
from logfile import log
log( 'Initalizing backend manager...')
bm = BackendManager()
log( 'Loading all available backends...')
bm.load_all_available_backends()
log( 'Listing loaded backends...')
bends = bm.get_loaded_backends()
for type, backend in bends.iteritems():
    log( type, backend.find_available_interfaces())
