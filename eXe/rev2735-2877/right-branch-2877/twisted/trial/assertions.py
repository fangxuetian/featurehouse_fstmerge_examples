"""
DEPRECATED.  Use the assertion methods on TestCase.
Assertion functions useful for unit tests.
"""
import warnings
from twisted.trial import unittest
warnings.warn("twisted.trial.assertions is deprecated.  Instead use the "
              "assertion methods on unittest.TestCase", stacklevel=2,
              category=DeprecationWarning)
__all__ = unittest._assertions + ['SkipTest', 'FailTest']
for name in __all__:
    globals()[name] = getattr(unittest, name)
