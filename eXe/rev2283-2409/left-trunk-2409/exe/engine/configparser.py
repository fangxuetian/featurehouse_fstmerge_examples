"""A more user friendly configParser
Copyright 2005-2006 Matthew Sherborne. All rights reserved.
Released under the GPL2 license found at
http://www.fsf.org/licensing/licenses/gpl.txt
"""
import re, os
exSection = re.compile('\[(?P<sectionname>(\w|\s)+)\]\s*')
exOption = re.compile("""\s*                # Ignore white space at the beginning
                         (?P<optionname>
                          (\s*(\w|[\_\-])+)+) # This matches multiple words
                         (?P<operator>\s*[:=]\s*)       # = or : with optional white space around it
                         (?P<value>.*?)              # Multiple words
                         (\s*)(?=$)         # White space at end ignored
                      """, re.VERBOSE)
UseDefault = object()
RaiseValueError = object()
class ConfigParser(object):
    """For parsing and writing config files"""
    optionMiddle = ' = '
    defaultValue = RaiseValueError 
    autoWrite = False 
    def __init__(self, onWrite=None):
        """
        set 'onWrite' to a method that will be called passing the parser as the
        only parameter, just before we write the file.
        """
        self._sections = {}
        self._originalFile = None
        self._onWrite = onWrite
    def __getattr__(self, attr):
        """
        Allows access to section objects
        through attributes
        """
        if '.' in attr:
            section, option = attr.split('.', 1)
            try:
                return self.get(section, option)
            except ValueError:
                raise AttributeError('%s instance has no attribute %s' %
                                     (self.__class__.__name__, attr))
        else:
            raise AttributeError('%s instance has no attribute %s' %
                                 (self.__class__.__name__, attr))
    def __setattr__(self, attr, value):
        """
        Allows creation of sections by attributes
        """
        if '.' in attr:
            section, option = attr.split('.', 1)
            section = self.set(section, option, value)
        else:
            if attr.startswith('_'):
                self.__dict__[attr] = value
            elif attr in self.__dict__ or attr in self.__class__.__dict__:
                if attr in self._sections:
                    assert isinstance(value, Section)
                self.__dict__[attr] = value
            else:
                Section(attr, self)
    def __delattr__(self, attr):
        """
        Allows deletion of a section
        by deleting its attribute
        """
        if self.has_section(attr):
            self.delete(attr)
        elif attr in self.__dict__:
            del self.__dict__[attr]
        else:
            raise AttributeError('%s instance has no attribute %s' %
                                 (self.__class__.__name__, attr))
    def __contains__(self, name):
        """
        Allows checking for existence of sections
        """
        if '.' in name:
            section, option = name.split('.', 1)
            return self.has_option(section, option)
        else:
            return self.has_section(name)
    def addSection(self, name):
        """
        Adds and returns a section object
        """
        if not self.has_section(name):
            Section(name, self)
        return self.__dict__[name]
    def read(self, file_):
        """Reads in a config file. 'file_' can be a file object
        or a string"""
        self._originalFile = file_
        if isinstance(file_, basestring):
            file_ = open(file_)
        lines = file_.readlines()
        for i, line in enumerate(lines):
            if not isinstance(line, unicode):
                lines[i] = unicode(line, 'utf8')
        self._sections = {}
        section = None
        sectionName = None
        for line in lines:
            if not line:
                continue
            match = exSection.match(line)
            if match:
                sectionName = match.group('sectionname')
                section = Section(sectionName, self)
            elif section is not None:
                match = exOption.match(line)
                if match:
                    opName = match.group('optionname')
                    opValue = match.group('value')
                    section[opName] = opValue
    def write(self, file_=None):
        """Writes the options to the file_"""
        if self._onWrite:
            self._onWrite(self)
        if not file_:
            file_ = self._originalFile
        else:
            self._originalFile = file_
        if isinstance(file_, basestring):
            if os.path.exists(file_):
                file_ = open(file_, 'r+')
            else:
                file_ = open(file_, 'w+')
        try:
            lines = file_.readlines()
        except IOError:
            lines = []
        section = None
        sectionName = None
        sectionOffsets = {}
        for i in range(len(lines)):
            line = lines[i]
            match = exSection.match(line)
            if match:
                sectionName = match.group('sectionname')
                optionOffsets = {}
                sectionOffsets[sectionName] = (line, optionOffsets, i)
            elif sectionName:
                match = exOption.match(line)
                if match:
                    opName = match.group('optionname')
                    if self.has_option(sectionName, opName):
                        opNewVal = self.get(sectionName, opName)
                        lines[i] = exOption.sub(r'\1\4', line) + opNewVal
                        optionOffsets[opName] = i
                    else: lines[i] = None
        linesToAdd, lastSectionLines = \
            self.addNewOptions(lines, section, sectionOffsets)
        self._writeFile(file_, lines, linesToAdd, lastSectionLines)
    def addNewOptions(self, lines, section, sectionOffsets):
        """
        Adds the new options to the file
        """
        linesToAdd = {}
        lastSectionLines = []
        linesToAdd[len(lines)] = lastSectionLines
        existingSections = sectionOffsets.keys()
        for section in self._sections:
            if section not in existingSections:
                linesToInsert = lastSectionLines
                if lines:
                    linesToInsert.append('')
                linesToInsert.append('[%s]' % section)
                newOpts = [(name, val)
                           for name, (val) 
                           in self._sections[section].items()]
            else:
                offsets = sectionOffsets[section][1]
                existingOptions = offsets.keys()
                newOpts = [(name, val) 
                           for name, val 
                           in self._sections[section].items() 
                           if name not in existingOptions]
                if offsets:
                    lineToAppendAt = max(offsets.values())
                else:
                    lineToAppendAt = sectionOffsets[section][2]
                linesToInsert = []
                linesToAdd[lineToAppendAt] = linesToInsert
            newOpts.sort()  # Put them in order
            for name, val in newOpts:
                linesToInsert.append('%s%s%s' % (name, self.optionMiddle, val))
        return linesToAdd, lastSectionLines
    def _writeFile(self, file_, lines, linesToAdd, lastSectionLines):
        """
        Actually writes the new file
        """
        newLines = []
        for i in range(len(lines)):
            line = lines[i]
            if line is not None:
                newLines.append(line.replace('\n', ''))
            toAdd = linesToAdd.get(i, None)
            if toAdd:
                newLines += toAdd
        newLines += lastSectionLines
        file_.seek(0)
        data = '\n'.join(newLines)
        file_.write(data.encode('utf8'))
        file_.truncate()
    def has_option(self, sectionName, optionName):
        """Returns 1 if we know about this setting"""
        if self.has_section(sectionName):
            return optionName in self._sections[sectionName].keys()
        else: return 0
    def has_section(self, sectionName):
        """Returns 1 if this section has been defined"""
        return sectionName in self._sections.keys()
    def get(self, sectionName, optionName, default=UseDefault):
        """Returns the option or 'default' if it doesn't exist"""
        if self.has_option(sectionName, optionName):
            return self._sections[sectionName][optionName]
        elif default is not UseDefault:
            return default
        elif self.defaultValue is RaiseValueError:
            raise ValueError("Option %s.%s doesn't exist" %
                             (sectionName, optionName))
        elif callable(self.defaultValue):
            return self.defaultValue(sectionName, optionName)
        else:
            return self.defaultValue
    def set(self, sectionName, optionName, value):
        """Set's an option in a section to value,
        can be used for new options, new sections and pre-existing ones"""
        sec = Section(sectionName, self) # This creates or gets a section
        if not isinstance(value, unicode):
            if not isinstance(value, str):
                value = str(value)
            value = unicode(value, 'utf8')
        if sec.get(optionName, None) != value:
            sec[optionName] = value
            if self.autoWrite and self._originalFile is not None:
                if hasattr(self._originalFile, 'seek') and \
                    callable(self._originalFile.seek):
                    self._originalFile.seek(0)
                if hasattr(self._originalFile, 'mode'):
                    if '+' not in self._originalFile.mode:
                        return
                self.write()
    def setdefault(self, sectionName, optionName, value):
        """
        If 'sectionName' and 'optionName' exists, returns its value,
        otherwise, sets it then returns the new value set.
        it's like setdefault in 'dict' instanaces
        """
        if self.has_section(sectionName) and \
           self.has_option(sectionName, optionName):
            return self.get(sectionName, optionName)
        else:
            self.set(sectionName, optionName, value)
            return value
    def delete(self, sectionName, optionName=None):
        """Remove a section or optionName. Set optionName to None
        to remove the whole section"""
        if self._sections.has_key(sectionName):
            if optionName is None:
                del self._sections[sectionName]
                delattr(self, sectionName)
                if self.autoWrite:
                    self.write()
            else:
                sec = self._sections[sectionName]
                if sec.has_key(optionName):
                    del sec[optionName]
                    if self.autoWrite:
                        self.write()
class Section(dict):
    """Represents a single section"""
    def __new__(cls, name, parent):
        """
        Utility func that will either make a new
        or return an existing Section instance
        """
        if parent.has_section(name):
            return parent._sections[name]
        else:
            return dict.__new__(cls, name, parent)
    def __init__(self, name, parent):
        """Setup"""
        dict.__init__(self, {})
        self.__name = name
        self.__parent = parent
        self.__parent._sections[name] = self
        dct = self.__parent.__dict__ 
        if name not in dct:
            dct[name] = self
    def has_option(self, optionName):
        """Returns 1 if we know about this setting"""
        return self.__parent.has_option(self.__name, optionName)
    def get(self, optionName, default=UseDefault):
        """Returns the option name"""
        return self.__parent.get(self.__name, optionName, default)
    def set(self, optionName, value):
        """Sets an option"""
        self.__parent.set(self.__name, optionName, value)
    def setdefault(self, optionName, value):
        """
        If 'optionName' exists, returns its value,
        otherwise, sets it then returns the new value set.
        it's like setdefault in 'dict' instanaces
        """
        return self.__parent.setdefault(self.__name, optionName, value)
    def __getattr__(self, attr):
        try:
            return self.__parent.get(self.__name, attr)
        except ValueError:
            raise AttributeError('%s instance has no attribute %s' %
                                 (self.__class__.__name__, attr))
    def __setattr__(self, attr, value):
        if attr.startswith('_'):
            self.__dict__[attr] = value
        else:
            self.__parent.set(self.__name, attr, value)
    def __delattr__(self, attr):
        self.__parent.delete(self.__name, attr)
    def __contains__(self, name):
        return self.__parent.has_option(self.__name, name)
