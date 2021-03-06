"""Main entry point to Bitpim
It invokes BitPim in gui or commandline mode as appropriate
@Note: Only gui mode is supported at the moment
"""
import sys
if __debug__:
    def profile(filename, command):
        import hotshot, hotshot.stats, os
        file=os.path.abspath(filename)
        profile=hotshot.Profile(file)
        profile.run(command)
        profile.close()
        del profile
	howmany=100
        stats=hotshot.stats.load(file)
        stats.strip_dirs()
        stats.sort_stats('time', 'calls')
        stats.print_stats(100)
        stats.sort_stats('cum', 'calls')
        stats.print_stats(100)
        stats.sort_stats('calls', 'time')
        stats.print_stats(100)
        sys.exit(0)
if __name__ == '__main__':
    import sys  
    import encodings.utf_8
    import encodings.ascii
    import encodings.iso8859_1
    import getopt
    import os.path
    if sys.platform=="darwin" and len(sys.argv)>1 and sys.argv[1].startswith("-psn_"):
	sys.argv=sys.argv[:1]+sys.argv[2:]
    _options, _args=getopt.getopt(sys.argv[1:], 'c:d:')
    _kwargs={}
    _debug=__debug__ or bool(_args and 'debug' in _args)
    if not _debug:
        import warnings
        def ignorer(*args, **kwargs): pass
        warnings.showwarning=ignorer
        class _donowt:
            def __getattr__(self, _):
                return self
            def __call__(self, *args, **kwargs):
                pass
        sys.stdout=_donowt()
        sys.stderr=_donowt()
    for _k,_v in _options:
        if _k=='-d':
            _kwargs['config_filename']=os.path.join(_v, '.bitpim')
        elif _k=='-c':
            _kwargs['config_filename']=_v
    if _args and 'bitfling' in _args:
        import bitfling.bitfling
        bitfling.bitfling.run(sys.argv)
    else:
        import gui
        gui.run(sys.argv, _kwargs)
