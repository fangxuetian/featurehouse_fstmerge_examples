"""
Support module for making a port forwarder with mktap.
"""
from twisted.protocols import portforward
from twisted.python import usage
from twisted.application import strports
class Options(usage.Options):
    synopsis = "Usage: mktap portforward [options]"
    longdesc = 'Port Forwarder.'
    optParameters = [
          ["port", "p", "6666","Set the port number."],
          ["host", "h", "localhost","Set the host."],
          ["dest_port", "d", 6665,"Set the destination port."],
    ]
    zsh_actions = {"host" : "_hosts"}
def makeService(config):
    f = portforward.ProxyFactory(config['host'], int(config['dest_port']))
    return strports.service(config['port'], f)
