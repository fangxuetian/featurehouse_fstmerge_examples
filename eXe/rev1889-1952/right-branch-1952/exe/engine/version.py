"""
Version Information
2006-01-11
"""
project        = "exe"
release        = "0.14"
revision       = "$Revision: 1890 $"[11:-2]
version        = release + "." + revision
if __name__ == '__main__':
    print project, version
