"""
A fuzzy checksum program based on a message posted to the spambayes list a
long time ago from Justin Mason of the SpamAssassin gang.  The basic idea is
that you dump stuff that can be obviously variable (email addresses and
such), compute several partial checksums over what remains, then compare
pieces against previous partial checksums to make a decision about a match.
Note that this concept can break down for small messages.  I only use it
downstream from Spambayes - after it's scored the message as spam:
:0
* ^X-Spambayes-Classification: (.*-)?spam
{
    :0 W: cksum.lock
    | pycksum.py -v $HOME/tmp/cksum.cache 2>> $HOME/tmp/cksum.log
    ... further spam processing here
}
That reduces the risk of tossing out mail I'm actually interested in. ;-) I
run it in verbose mode and save the log message.  It catches a fair fraction
of duplicate spams, probably 3 out of every 4.  (Mail for several email
addresses funnels into skip@mojam.com.)
"""
import getopt
import sys
import email.Parser
import md5
import anydbm
import re
import time
import binascii
def flatten(body):
    if isinstance(body, str):
        return body
    if hasattr(body, "get_payload"):
        payload = body.get_payload()
        if payload is None:
            return ""
        return flatten(payload)
    if isinstance(body, list):
        return "\n".join([flatten(b) for b in body])
    raise TypeError, ("unrecognized body type: %s" % type(body))
def clean(data):
    """Clean the obviously variable stuff from a chunk of data.
    The first (and perhaps only) use of this is to try and eliminate bits
    of data that keep multiple spam email messages from looking the same.
    """
    data = re.sub(r"<[^>]*>", "", data).lower()
    return " ".join([w for w in data.split()
                     if ('@' not in w and
                         (':' not in w or
                          w[:4] != "ftp:" and
                          w[:7] != "mailto:" and
                          w[:5] != "http:" and
                          w[:7] != "gopher:" and
                          w[:8] != "pmguid:"))])
def generate_checksum(msg):
    body = flatten(msg)
    lines = clean(body)
    chunksize = len(lines)//4+1
    sum = []
    for i in range(4):
        chunk = "\n".join(lines[i*chunksize:(i+1)*chunksize])
        sum.append(binascii.b2a_hex(md5.new(chunk).digest()))
    return ".".join(sum)
def save_checksum(cksum, f):
    pieces = cksum.split('.')
    result = 1
    db = anydbm.open(f, "c")
    maxdblen = 2**14
    for subsum in (".".join(pieces[:-1]),
                   ".".join(pieces[1:-1]),
                   ".".join(pieces[1:])):
        if not db.has_key(subsum):
            db[subsum] = str(time.time())
            if len(db) > maxdblen:
                items = [(float(db[k]),k) for k in db.keys()]
                items.sort()
                items = items[:-(maxdblen-20)]
                for v,k in items:
                    del db[k]
        else:
            result = 0
            break
    db.close()
    return result
def main(args):
    opts, args = getopt.getopt(args, "v")
    verbose = 0
    for opt, arg in opts:
        if opt == "-v":
            verbose = 1
    if not args:
        dbf = None
    else:
        dbf = args[0]
    msg = email.Parser.Parser().parse(sys.stdin)
    cksum = generate_checksum(msg)
    if dbf is None:
        print cksum
        result = 1
        disp = 'nodb'
    else:
        result = save_checksum(cksum, dbf)
        disp = result and 'old' or 'new'
    if verbose:
        t = time.strftime("%Y-%m-%d:%H:%M:%S", time.localtime(time.time()))
        logmsg = "%s/%s/%s/%s\n" % (t, cksum, disp, msg['message-id'])
        sys.stderr.write(logmsg)
    return result
if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
