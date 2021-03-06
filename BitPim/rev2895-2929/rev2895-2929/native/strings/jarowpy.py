def jarow(s1, s2, winkleradjust=0):

    if len(s1)==0 or len(s2)==0: return 0

    if s1==s2: return 1

    halflen=min(len(s1)/2+1, len(s2)/2+1)

    s1pos=0

    s1seenins2=[0]*len(s2)

    s2pos=0

    s2seenins1=[0]*len(s1)

    transpositions=0

    commonlen=0

    while s1pos<len(s1) or s2pos<len(s2):

        s1char=None

        while s1pos<len(s1):

            c=s1[s1pos]

            for i in xrange(max(0,s1pos-halflen), min(len(s2),s1pos+halflen)):

                if s1seenins2[i]: continue

                if c==s2[i]:

                    s1char=c

                    s1seenins2[i]=1

                    break

            s1pos+=1

            if s1char is not None:

                break

        s2char=None

        while s2pos<len(s2):

            c=s2[s2pos]

            for i in xrange(max(0,s2pos-halflen), min(len(s1),s2pos+halflen)):

                if s2seenins1[i]: continue

                if c==s1[i]:

                    s2char=c

                    s2seenins1[i]=1

                    break

            s2pos+=1

            if s2char is not None:

                break

        if s1char==None and s2char==None:

            break

        if s1char!=None and s2char==None:

            return 0

        if s1char==None and s2char!=None:

            return 0

        commonlen+=1

        if s1char!=s2char:

            transpositions+=1

    if commonlen==0: return 0

    transpositions/=2

    dist=commonlen/float(len(s1)) + commonlen/float(len(s2)) + (commonlen-transpositions)/float(commonlen)

    dist/=3.0

    if winkleradjust:

        for common in range(min(len(s1)+1, len(s2)+1, winkleradjust)):

            if common>=len(s1) or common>=len(s2): break

            if s1[common]!=s2[common]:

                break

        dist = dist + common * 0.1 * (1-dist)

    return dist

