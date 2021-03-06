"""
I am a support module for making SOCKSv4 servers with mktap.
"""
from twisted.protocols import socks
from twisted.python import usage
from twisted.application import internet
import sys
class Options(usage.Options):
    synopsis = "Usage: mktap socks [-i <interface>] [-p <port>] [-l <file>]"
    optParameters = [["interface", "i", "127.0.0.1", "local interface to which we listen"],
                  ["port", "p", 1080, "Port on which to listen"],
                  ["log", "l", None, "file to log connection data to"]]
    zsh_actions = {"log" : "_files -g '*.log'"}
    longdesc = "Makes a SOCKSv4 server."
def makeService(config):
    if config["interface"] != "127.0.0.1":
        print
        print "WARNING:"
        print "  You have chosen to listen on a non-local interface."
        print "  This may allow intruders to access your local network"
        print "  if you run this on a firewall."
        print
    t = socks.SOCKSv4Factory(config['log'])
    portno = int(config['port'])
    return internet.TCPServer(portno, t, interface=config['interface'])
