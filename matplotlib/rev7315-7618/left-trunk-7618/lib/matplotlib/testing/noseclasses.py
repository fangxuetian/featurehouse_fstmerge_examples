import os
from nose.plugins.errorclass import ErrorClass, ErrorClassPlugin
class KnownFailureTest(Exception):
    '''Raise this exception to mark a test as a known failing test.'''
    pass
class KnownFailure(ErrorClassPlugin):
    '''Plugin that installs a KNOWNFAIL error class for the
    KnownFailureClass exception.  When KnownFailureTest is raised,
    the exception will be logged in the knownfail attribute of the
    result, 'K' or 'KNOWNFAIL' (verbose) will be output, and the
    exception will not be counted as an error or failure.
    This is based on numpy.testing.noseclasses.KnownFailure.
    '''
    enabled = True
    knownfail = ErrorClass(KnownFailureTest,
                           label='KNOWNFAIL',
                           isfailure=False)
    def options(self, parser, env=os.environ):
        env_opt = 'NOSE_WITHOUT_KNOWNFAIL'
        parser.add_option('--no-knownfail', action='store_true',
                          dest='noKnownFail', default=env.get(env_opt, False),
                          help='Disable special handling of KnownFailureTest '
                               'exceptions')
    def configure(self, options, conf):
        if not self.can_configure:
            return
        self.conf = conf
        disable = getattr(options, 'noKnownFail', False)
        if disable:
            self.enabled = False
    def addError( self, test, err ):
        pass
