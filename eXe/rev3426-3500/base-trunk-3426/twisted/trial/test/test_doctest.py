"""
test twisted's doctest support
"""
import exceptions, sys, doctest
from twisted import trial
from twisted.trial import runner, unittest, reporter
from twisted.trial import itrial
from twisted.python import failure
from twisted.trial.test import mockdoctest
from pprint import pprint
import zope.interface as zi
class TestRunners(unittest.TestCase):
    def test_correctCount(self):
        suite = runner.DocTestSuite(mockdoctest)
        self.assertEqual(7, suite.countTestCases())
    def test_basicTrialIntegration(self):
        loader = runner.TestLoader()
        suite = loader.loadDoctests(mockdoctest)
        self.assertEqual(7, suite.countTestCases())
    def test_expectedResults(self):
        suite = runner.DocTestSuite(mockdoctest)
        result = reporter.TestResult()
        suite.run(result)
        self.assertEqual(5, len(result.successes))
        self.assertEqual(2, len(result.errors) + len(result.failures))
