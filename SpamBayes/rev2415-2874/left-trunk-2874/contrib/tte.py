"""
Train to exhaustion: train repeatedly on a pile of ham and spam until
everything scores properly.
usage %(prog)s [ -h ] -g file -s file [ -d file | -p file ] \
               [ -m N ] [ -r N ] [ -c ext ] [ -o sect:opt:val ] [ -v ]
-h      - Print this usage message and exit.
-g file - Take ham from file.
-s file - Take spam from file.
-d file - Use a database-based classifier named file.
-p file - Use a pickle-based classifier named file.
-m N    - Train on at most N messages (nham == N/2 and nspam == N/2).
-r N    - Run at most N rounds (default %(MAXROUNDS)s), even if not
          all messages score correctly.
-c ext  - Cull all messages which aren't used as training input during any
          run and write to new ham and spam files with ext as an extra file
          extension.  All messages which are never considered (because one
          training set is longer than the other or the -m flag was used to
          reduce the amount of input) are retained.
-o sect:opt:val -
          Set [sect, opt] in the options database to val.
-v        Be very verbose, spewing all sorts of stuff out to stderr.
-R        Walk backwards through the mailbox.
--ratio=n Define the ratio of spam:ham messages to be trained at once.
          The default is 1:1, but given the sorry state of the Net's email
          infrastructure these days you'll probably want to raise it (3:2 or
          2:1, etc).  Keep it as close to 1 as you can...
Note: The -c command line argument isn't quite as benign as it might first
appear.  Since the tte protocol trains on the same number of ham and spam
messages, if you use the output of one run as input into a later run you
will almost certainly train on fewer messages than before since the two
files will probably not have the same number of messages.  The extra
messages in the longer file will be ignored in future runs until you add
more messages to the shorter file.
Note: Adding messages which train correctly won't affect anything other than
adding more ham or spam to the respective training pile.  To force such
messages to have an effect you should set your ham_cutoff and spam_cutoff
values closer to 0.0 and 1.0 than your normal settings during scoring.  For
example, if your normal ham_cutoff and spam_cutoff values are 0.2 and 0.8,
you might run %(prog)s like
    %(prog)s -o Categorization:ham_cutoff:0.05 \
        -o Categorization:spam_cutoff:0.95 \
        [ other args ]
For more detail on the notion of training to exhaustion see Gary Robinson's
blog:
    http://www.garyrobinson.net/2004/02/spam_filtering_.html
"""
from __future__ import division
import sys
import getopt
import os
import datetime
from spambayes import storage
from spambayes import Options
from spambayes import mboxutils
from spambayes.tokenizer import tokenize
prog = os.path.basename(sys.argv[0])
MAXROUNDS = 10
def usage(msg=None):
    if msg is not None:
        print >> sys.stderr, msg
    print >> sys.stderr, __doc__.strip() % globals()
try:
    reversed
except NameError:
    def reversed(seq):
        seq = seq[:]
        seq.reverse()
        return iter(seq)
def train(store, hambox, spambox, maxmsgs, maxrounds, tdict, reverse, verbose,
          ratio):
    smisses = hmisses = round = 0
    ham_cutoff = Options.options["Categorization", "ham_cutoff"]
    spam_cutoff = Options.options["Categorization", "spam_cutoff"]
    nspam, nham = ratio
    while round < maxrounds and (hmisses or smisses or round == 0):
        hambone = mboxutils.getmbox(hambox)
        spamcan = mboxutils.getmbox(spambox)
        if reverse:
            hambone = reversed(list(hambone))
            spamcan = reversed(list(spamcan))
        round += 1
        if verbose:
            print >> sys.stderr, "*** round", round, "***"
        hmisses = smisses = nmsgs = 0
        start = datetime.datetime.now()
        try:
            while not maxmsgs or nmsgs < maxmsgs:
                hams = []
                for i in range(nham):
                    try:
                        hams.append(hambone.next())
                    except StopIteration:
                        if not hams:
                            raise
                        break
                spams = []
                for i in range(nspam):
                    try:
                        spams.append(spamcan.next())
                    except StopIteration:
                        if not spams:
                            raise
                        break
                nmsgs += len(hams) + len(spams)
                sys.stdout.write("\r%5d" % nmsgs)
                sys.stdout.flush()
                for (ham, spam) in map(None, hams, spams):
                    if ham is not None:
                        score = store.spamprob(tokenize(ham))
                        selector = ham["message-id"] or ham["subject"]
                        if score > ham_cutoff and selector is not None:
                            if verbose:
                                print >> sys.stderr, "miss ham: %.6f %s" % (
                                    score, selector)
                            hmisses += 1
                            tdict[ham["message-id"]] = True
                            store.learn(tokenize(ham), False)
                    if spam is not None:
                        score = store.spamprob(tokenize(spam))
                        selector = (spam["message-id"] or
                                    spam["subject"])
                        if score < spam_cutoff and selector is not None:
                            if verbose:
                                print >> sys.stderr, "miss spam: %.6f %s" % (
                                    score, selector)
                            smisses += 1
                            tdict[spam["message-id"]] = True
                            store.learn(tokenize(spam), True)
        except StopIteration:
            pass
        delta = datetime.datetime.now()-start
        seconds = delta.seconds + delta.microseconds/1000000
        print "\rround: %2d, msgs: %4d, ham misses: %3d, spam misses: %3d, %.1fs" % \
              (round, nmsgs, hmisses, smisses, seconds)
    nhamleft = 0
    try:
        while True:
            msg = hambone.next()
            score = store.spamprob(tokenize(msg))
            if score > ham_cutoff:
                tdict[msg["message-id"]] = True
                nhamleft += 1
    except StopIteration:
        if nhamleft: print nhamleft, "untrained hams"
    nspamleft = 0
    try:
        while True:
            msg = spamcan.next()
            score = store.spamprob(tokenize(msg))
            if score < spam_cutoff:
                tdict[msg["message-id"]] = True
                nspamleft += 1
    except StopIteration:
        if nspamleft: print nspamleft, "untrained spams"
def main(args):
    try:
        opts, args = getopt.getopt(args, "hg:s:d:p:o:m:r:c:vR",
                                   ["help", "good=", "spam=",
                                    "database=", "pickle=", "verbose",
                                    "option=", "max=", "maxrounds=",
                                    "cullext=", "reverse", "ratio="])
    except getopt.GetoptError, msg:
        usage(msg)
        return 1
    ham = spam = dbname = usedb = cullext = None
    maxmsgs = 0
    maxrounds = MAXROUNDS
    verbose = False
    reverse = False
    sh_ratio = (1, 1)
    for opt, arg in opts:
        if opt in ("-h", "--help"):
            usage()
            return 0
        elif opt in ("-v", "--verbose"):
            verbose = True
        elif opt in ("-g", "--good"):
            ham = arg
        elif opt in ("-s", "--spam"):
            spam = arg
        elif opt in ("-c", "--cullext"):
            cullext = arg
        elif opt in ("-m", "--max"):
            maxmsgs = int(arg)
        elif opt in ("-r", "--maxrounds"):
            maxrounds = int(arg)
        elif opt in ("-R", "--reverse"):
            reverse = True
        elif opt in ('-o', '--option'):
            Options.options.set_from_cmdline(arg, sys.stderr)
        elif opt == '--ratio':
            arg = arg.split(":")
            sh_ratio = (int(arg[0]), int(arg[1]))
    if ham is None or spam is None:
        usage("require both ham and spam piles")
        return 1
    dbname, usedb = storage.database_type(opts)
    try:
        os.unlink(dbname)
    except OSError:
        pass
    store = storage.open_storage(dbname, usedb)
    tdict = {}
    train(store, ham, spam, maxmsgs, maxrounds, tdict, reverse, verbose,
          sh_ratio)
    store.store()
    if cullext is not None:
        print "writing new ham mbox..."
        n = m = 0
        newham = file(ham + cullext, "w")
        for msg in mboxutils.getmbox(ham):
            m += 1
            if msg["message-id"] in tdict:
                newham.write(str(msg))
                n += 1
            sys.stdout.write("\r%5d of %5d" % (n, m))
            sys.stdout.flush()
        sys.stdout.write("\n")
        newham.close()
        print "writing new spam mbox..."
        n = m = 0
        newspam = file(spam + cullext, "w")
        for msg in mboxutils.getmbox(spam):
            m += 1
            if msg["message-id"] in tdict:
                newspam.write(str(msg))
                n += 1
            sys.stdout.write("\r%5d of %5d" % (n, m))
            sys.stdout.flush()
        sys.stdout.write("\n")
        newspam.close()
    return 0
if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
