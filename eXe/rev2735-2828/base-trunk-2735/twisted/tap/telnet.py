"""
Support module for making a telnet server with mktap.
"""
from twisted.manhole import telnet
from twisted.python import usage
from twisted.application import strports
class Options(usage.Options):
    synopsis = "Usage: mktap telnet [options]"
    longdesc = "Makes a telnet server to a Python shell."
    optParameters = [
         ["username", "u", "admin","set the login username"],
         ["password", "w", "changeme","set the password"],
         ["port", "p", "4040", "port to listen on"],
    ]
    zsh_actions = {"username":"_users"}
def makeService(config):
    t = telnet.ShellFactory()
    t.username, t.password = config['username'], config['password']
    s = strports.service(config['port'], t)
    t.setService(s)
    return s
