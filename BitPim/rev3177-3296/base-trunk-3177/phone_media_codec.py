"""
Codec for converting phone media file names to local file names.
"""
import codecs
import wx
escape_char='%'
bad_chars={
    '__WXMSW__': (escape_char, '/', '\\', '[', ']', '?', '*', ':',
                  '"', '<', '>', '|', '=', ';'),
    '__WXMAC__': (escape_char, '/', ':'),
    '__WXGTK__': (escape_char, '/') }
def phone_media_encode(input, errors='ignore'):
    """ Encodes the phone media file name into local storage file name
    """
    assert errors=='ignore'
    l=[]
    for c in input:
        ord_c=ord(c)
        if ord_c<32 or ord_c>127 or \
           c in bad_chars.get(wx.Platform, ()):
            l+=hex(ord_c).replace('0x', escape_char)
        else:
            l+=c
    return (str(''.join(l)), len(input))
def phone_media_decode(input, errors='ignore'):
    """ Decodes local system file name to phone media file name
    """
    assert errors=='ignore'
    l=[]
    esc_str=''
    for c in input:
        if c==escape_char:
            if len(esc_str):
                l+=esc_str
            esc_str=c
        elif len(esc_str):
            esc_str+=c
            if len(esc_str)==3:
                try:
                    h=int(esc_str[1:], 16)
                    l+=chr(h)
                except:
                    l+=esc_str
                esc_str=''
        else:
            l+=c
    return (''.join(l), len(input))
class Codec(codecs.Codec):
    def encode(self, input,errors='strict'):
        return phone_media_encode(input,errors)
    def decode(self, input,errors='strict'):
        return phone_media_decode(input,errors)
class StreamWriter(Codec,codecs.StreamWriter):
    pass
class StreamReader(Codec,codecs.StreamReader):
    pass
codec_name='phone_media'
def search_func(name):
    if name==codec_name:
        return (phone_media_encode, phone_media_decode,
                StreamReader, StreamWriter)
