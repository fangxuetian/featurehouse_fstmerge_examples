"""
Simple fields which can be used to build up a generic iDevice.
"""
import logging
from exe.engine.persist   import Persistable
from exe.engine.path      import Path, toUnicode
from exe.engine.resource  import Resource
from exe.engine.translate import lateTranslate
from exe.engine.mimetex   import compile
from HTMLParser           import HTMLParser
from exe.engine.flvreader import FLVReader
from htmlentitydefs       import name2codepoint
from exe.engine.htmlToText import HtmlToText
from twisted.persisted.styles import Versioned
from exe.webui                import common
from exe                  import globals as G
from exe.engine.node      import Node
import os
import re
import urllib
import shutil
log = logging.getLogger(__name__)
class Field(Persistable):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    persistenceVersion = 3
    nextId = 1
    def __init__(self, name, instruc=""):
        """
        Initialize 
        """
        self._name     = name
        self._instruc  = instruc
        self._id       = Field.nextId
        Field.nextId  += 1
        self.idevice   = None
    name    = lateTranslate('name')
    instruc = lateTranslate('instruc')
    def getId(self):
        """
        Returns our id which is a combination of our iDevice's id
        and our own number.
        """
        if self.idevice:
            fieldId = self.idevice.id + "_"
        else:
            fieldId = ""
        fieldId += unicode(self._id)
        return fieldId
    id = property(getId)
    def setIDevice(self, idevice):
        """
        Gives ourselves a new ID unique to the new idevice.
        """
        if hasattr(idevice, 'getUniqueFieldId'):
            self._id = idevice.getUniqueFieldId()
        self._idevice = idevice
    def getIDevice(self):
        if hasattr(self, '_idevice'):
            return self._idevice
        else:
            return None
    idevice = property(getIDevice, setIDevice)
    def __getstate__(self): 
        """
        Override Persistable's getstate, to recognize when this is an actual
        file save (in which case, do not save the nonpersistant attributes),
        or a copy (as used in in file insert, to merge other files).
        Currently, only node's copyToPackage will indicate that this is 
        for a copy, by setting G.application.persistNonPersistants
        Return which variables we should persist
        """
        if G.application.persistNonPersistants:
            toPersist = self.__dict__
        else: 
            toPersist = dict([(key, value) 
                    for key, value in self.__dict__.items() 
                    if key not in self.nonpersistant])
        return Versioned.__getstate__(self, toPersist)
    def upgradeToVersion1(self):
        """
        Upgrades to exe v0.10
        """
        self._name = self.__dict__['name']
        del self.__dict__['name']
        if self.__dict__.has_key('instruc'):
            self._instruc = self.__dict__['instruc']
        else:
            self._instruc = self.__dict__['instruction']
    def upgradeToVersion2(self):
        """
        Upgrades to 0.21
        """
        if 'idevice' in self.__dict__:
            self._idevice = self.__dict__['idevice']
            del self.__dict__['idevice']
    def _upgradeFieldToVersion2(self):
        """
        Called from Idevices to upgrade fields to exe v0.12
        """
        pass
    def _upgradeFieldToVersion3(self):
        """
        Called from Idevices to upgrade fields to exe v0.24
        """
        pass
class TextField(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    def __init__(self, name, instruc="", content=""):
        """
        Initialize 
        """
        Field.__init__(self, name, instruc)
        self.content = content
class FieldWithResources(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element.
    Used by TextAreaField, FeedbackField, and ClozeField to encapsulate 
    all the multi-resource handling which can now be included 
    via the tinyMCE RichTextArea.
    """
    persistenceVersion = 2
    nonpersistant      = ['content', 'content_wo_resourcePaths']
    def __init__(self, name, instruc="", content=""):
        """
        Initialize 
        """
        Field.__init__(self, name, instruc)
        self.content = content
        self.content_w_resourcePaths = content
        self.content_wo_resourcePaths = content
        from exe.engine.galleryidevice  import GalleryImages
        self.images = GalleryImages(self)
        self.nextImageId       = 0
        self.parentNode = None
        if hasattr(self.idevice, 'parentNode'): 
            self.parentNode = self.idevice.parentNode
    def TwistedRePersist(self): 
        """ 
        to be called by twisted after any upgrades to this class, 
        but before any of its subclass upgrades occur:
        """
        if hasattr(self, "content_w_resourcePaths"):
            self.content = self.content_w_resourcePaths 
            self.content_wo_resourcePaths = \
                self.MassageContentForRenderView(self.content_w_resourcePaths)
        if not hasattr(self, 'anchor_names'):
            if hasattr(self, 'content'): 
                self.ProcessInternalAnchors(self.content)
            else:
                self.anchor_names = []
        if not hasattr(self, 'anchors_linked_from_fields'): 
            self.anchors_linked_from_fields = {}
        if not hasattr(self, 'intlinks_to_anchors'): 
            self.intlinks_to_anchors = {}
    def genImageId(self): 
        """
        Generate a unique id for an image. 
        Called by 'GalleryImage'
        """ 
        self.nextImageId += 1 
        return '%s.%s' % (self.id, self.nextImageId - 1)
    def setParentNode(self):
        """
        Mechanism by which the idevice's parentNode is triggered
        into being recognized by this field.  Normally not needed
        until the addition of resources using GalleryImage, this
        is because the field appears to be intially constructed 
        on a cloneable idevice, which doesn't yet have a parentNode
        even defined!
        NOTE: a property might be a MUCH better approach to this.
        """
        self.parentNode = None
        if hasattr(self.idevice, 'parentNode'): 
            self.parentNode = self.idevice.parentNode
    def RemoveZombieResources(self, resources_in_use):
        """
        Given the list of resources still in use, compare to this
        still listed in the images[] resource list, and remove
        any zombies, resources in images[] that are longer in use.
        """
        num_images=len(self.images)  
        for image_num in range(num_images-1, -1, -1): 
            embedded_resource = self.images[image_num]
            embedded_res_name = embedded_resource._imageResource.storageName
            if embedded_res_name in resources_in_use or  \
               embedded_res_name.replace(" ", "%20") in resources_in_use:
                log.debug("confirmed resource is still active in this field: "\
                        + embedded_res_name) 
            else:
                log.debug("resource no longer used in this field, REMOVING: "\
                        + embedded_res_name) 
                del self.images[image_num]
    def ListActiveResources(self, content):
        """
        Find and return the name of all active image resources,
        to help find those no longer in use and in need of purging.
        This finds images and media as well, since they both use src="...".
        It now also finds any math-images' paired exe_math_latex sources, too!
        And NOW also finds ANY other resource file embedded as an href via
        the advlink text-link plugin.
        And the embedded mp3s that use xspf_player.swf;
        AND the embedded FLVs that use flowPlayer.swf, 
        even though they still use src="resources/" in their embed tag,
        their src param has been changed to flv_src="resources/" to avoid
        problems with IE upon export.  Luckily, the src="resources" will still
        find BOTH such occurrences :-)
        """
        resources_in_use =  []
        search_strings = ["src=\"resources/", "exe_math_latex=\"resources/", \
                "href=\"resources/", \
                "src=\"../templates/xspf_player.swf?song_url=resources/"]
        for search_num in range(len(search_strings)): 
            search_str = search_strings[search_num] 
            embedded_mp3 = False
            if search_str == \
                "src=\"../templates/xspf_player.swf?song_url=resources/":
                embedded_mp3 = True
            found_pos = content.find(search_str) 
            while found_pos >= 0: 
                if not embedded_mp3:
                    end_pos = content.find('\"', found_pos+len(search_str)) 
                else:
                    end_pos = content.find('&song_title=', 
                            found_pos+len(search_str)) 
                    if end_pos <= 0:
                        end_pos = content.find('&amp;song_title=', 
                            found_pos+len(search_str)) 
                if end_pos > 0: 
                    resource_str = content[found_pos+len(search_str):end_pos] 
                    resources_in_use.append(resource_str)
                found_pos = content.find(search_str, found_pos+1) 
        return resources_in_use
    def RemoveTemporaryAnchors(self, content): 
        """
        """
        new_content = content
        next_anchor_pos = new_content.find('<exe_tmp_anchor ')
        closing_tag = '</exe_tmp_anchor>'
        while next_anchor_pos >= 0: 
            next_end_pos = new_content.find(closing_tag, next_anchor_pos)
            if next_end_pos >= 0: 
                next_end_pos += len(closing_tag)
                this_tmp_anchor = new_content[next_anchor_pos : next_end_pos ] 
                new_content = new_content.replace(this_tmp_anchor, '')
            else:
                next_anchor_pos += 1
            next_anchor_pos = new_content.find('<exe_tmp_anchor ', 
                    next_anchor_pos)
        return new_content
    def RemoveInternalLinkToRemovedAnchor(self, dst_field, full_anchor_name):
        """
        Called by a linked destination field when its anchor is removed,
        this will remove references to the anchor from this link source field.
        """ 
        if hasattr(self, 'intlinks_to_anchors'): 
            if full_anchor_name in self.intlinks_to_anchors.keys(): 
                if dst_field != self.intlinks_to_anchors[full_anchor_name]: 
                    log.warn('RemoveInternalLinkToRemovedAnchor found a '
                        + 'different link-destination field than expected; '
                        + 'removing anyway.')
                del self.intlinks_to_anchors[full_anchor_name]
            else:
                log.warn('RemoveInternalLinkToRemovedAnchor did not find the '
                        + 'link-destination anchor as expected; '
                        + 'removing anyway.')
        self.content = common.removeInternalLinks(
                self.content, full_anchor_name)
        self.content_w_resourcePaths = common.removeInternalLinks(
                self.content_w_resourcePaths, full_anchor_name)
        self.content_wo_resourcePaths = common.removeInternalLinks(
                self.content_wo_resourcePaths, full_anchor_name)
        this_node_path = ""
        if self.idevice is not None and self.idevice.parentNode is not None:
            this_node_path = self.idevice.parentNode.GetFullNodePath()
        else:
            this_node_path = "<disconnected>"
        log.warn('Removed internal link to removed-anchor: ' + full_anchor_name
                + ' from node: ' + this_node_path)
    def ReplaceAllInternalAnchorsLinks(self, oldNode=None, newNode=None):
        """
        An ensemble wrapper around RemoveInternalLinkToRemovedAnchor(), 
        or RenameInternalLinkToAnchor(), depending on its usage to 
        remove or replace ALL internal links to ALL anchors within this field.
        To be called by multi-object idevice's delete object action
        (for example, when a Multi-Choice removes an option or a question), or 
        when the idevice itself is deleted or moved, via its ChangedParentNode
        which could have already changed the node with a move.
        As such, allow the old_node to be passed in, as the fields
        anchors data structures may still be stored there, and not yet on
        its new parentNode.
        Likewise new_node allows this to also be used for idevice moves,
        where the internal anchors are not actually being removed but replaced.
        """
        old_node_path = ""
        old_package = None
        if oldNode is None and newNode is None:
            if self.idevice is not None:
                oldNode = self.idevice.parentNode
        if oldNode: 
            old_node_path = oldNode.last_full_node_path 
            old_package = oldNode.package
        new_node_path = ""
        new_package = None
        if newNode: 
            new_node_path = newNode.GetFullNodePath()
            new_package = newNode.package
        if hasattr(self, 'anchor_names') \
        and hasattr(self, 'anchors_linked_from_fields'): 
            for this_anchor_name in self.anchor_names: 
                old_full_link_name = old_node_path + "#" \
                        + this_anchor_name 
                new_full_link_name = new_node_path + "#" + \
                        this_anchor_name 
                for that_field in \
                self.anchors_linked_from_fields[this_anchor_name]:
                    if newNode:
                        that_field.RenameInternalLinkToAnchor( 
                            self, old_full_link_name, 
                            new_full_link_name) 
                    else:
                        that_field.RemoveInternalLinkToRemovedAnchor( 
                            self, old_full_link_name)
        if oldNode is not None \
        and hasattr(oldNode, 'anchor_fields') \
        and self in oldNode.anchor_fields:
            if old_package and hasattr(old_package, 'anchor_fields') \
            and self in old_package.anchor_fields:
                old_package.anchor_fields.remove(self)
            oldNode.anchor_fields.remove(self)
            if len(oldNode.anchor_fields) == 0:
                if old_package and hasattr(old_package, 'anchor_nodes') \
                and oldNode in old_package.anchor_nodes:
                    old_package.anchor_nodes.remove(oldNode)
    def RenameInternalLinkToAnchor(self, dst_field, old_full_anchor_name,
                                    new_full_anchor_name):
        """
        Called by a linked destination field when its anchor is removed,
        this will remove references to the anchor from this link source field.
        """
        if hasattr(self, 'intlinks_to_anchors'): 
            if old_full_anchor_name in self.intlinks_to_anchors.keys(): 
                if dst_field != self.intlinks_to_anchors[old_full_anchor_name]: 
                    log.warn('RenameInternalLinkToAnchor found a different '
                        + 'link-destination field than expected; '
                        + 'renaming anyway.')
                self.intlinks_to_anchors[new_full_anchor_name] = dst_field
                del self.intlinks_to_anchors[old_full_anchor_name]
            else:
                log.warn('RenameInternalLinkToAnchor did not find the'
                        + 'link-destination anchor as expected; '
                        + 'renaming anyway.')
                self.intlinks_to_anchors[new_full_anchor_name] = dst_field
        old_intlink = 'href="' + old_full_anchor_name + '"'
        new_intlink = 'href="' + new_full_anchor_name + '"'
        self.content = self.content.replace(old_intlink, new_intlink)
        self.content_w_resourcePaths = self.content_w_resourcePaths.replace(
                old_intlink, new_intlink)
        self.content_wo_resourcePaths = self.content_wo_resourcePaths.replace(
                old_intlink, new_intlink)
    def ListActiveAnchors(self, content):
        """
        to build up the list of all anchors currently within this field's 
        new content to process.
        assuming TinyMCE anchor tag conventions of:
               <a title="TITLE" name="NAME"></a>
        and that TITLE==NAME
        """
        anchor_names = []
        starting_tag_bits = '<a title="'
        next_anchor_pos = content.find(starting_tag_bits)
        middle_tag_bits = '" name="'
        closing_tag_bits = '"></a>'
        while next_anchor_pos >= 0:
            title_start_pos = next_anchor_pos + len(starting_tag_bits)
            title_end_pos = content.find(middle_tag_bits, next_anchor_pos)
            this_anchor_title = content[title_start_pos : title_end_pos ]
            name_start_pos = title_end_pos + len(middle_tag_bits)
            name_end_pos = content.find(closing_tag_bits, next_anchor_pos)
            this_anchor_name = content[name_start_pos : name_end_pos ]
            if this_anchor_name: 
                anchor_names.append(this_anchor_name)
            next_end_pos = name_end_pos + len(closing_tag_bits)
            next_anchor_pos = content.find(starting_tag_bits, next_end_pos)
        return anchor_names
    def ListActiveInternalLinks(self, content):
        """
        to build up the list of all internal links currently within this 
        field's new content to process, merely looking for href="exe-node:..."
        into the fields var: intlinks_to_anchors, 
        which is created here as: intlinks_names_n_fields
        """
        intlinks_names_n_fields = {}
        this_package = None
        if self.idevice is not None and self.idevice.parentNode is not None:
            this_package = self.idevice.parentNode.package
        intlink_start = 'href="exe-node:'
        intlink_pre   = 'href="'
        next_link_pos = content.find(intlink_start)
        while next_link_pos >= 0: 
            link_name_start_pos = next_link_pos + len(intlink_pre)
            link_name_end_pos = content.find('"', link_name_start_pos)
            if link_name_end_pos >= 0: 
                link_name = content[link_name_start_pos : link_name_end_pos]
                node_name_end_pos = link_name.find('#') 
                if node_name_end_pos < 0: 
                    node_name_end_pos = len(link_name) - 1 
                    link_anchor_name = ""
                else:
                    link_anchor_name = link_name[node_name_end_pos + 1 : ]
                link_node_name = link_name[0 : node_name_end_pos]
                if link_node_name: 
                    link_field = common.findLinkedField(this_package, 
                            link_node_name, link_anchor_name)
                    if link_field is None and link_anchor_name == u"auto_top":
                        link_field = common.findLinkedNode(this_package, 
                            link_node_name, link_anchor_name, 
                            check_fields=False)
                    intlinks_names_n_fields[link_name] = link_field
            next_link_pos = content.find(intlink_start, next_link_pos+1)
        return intlinks_names_n_fields
    def GetFullNodePath(self): 
        """
        Really a general purpose single-line node-naming convention,
        but for the moment, it is only used for the actual anchors, to
        provide a path to its specific node.
        """
        full_path = ""
        if self.idevice is not None and self.idevice.parentNode is not None:
            full_path = self.idevice.parentNode.GetFullNodePath()
        return full_path
    def ProcessPreviewed(self, content): 
        """
        to build up the corresponding resources from any images (etc.) added
        in by the tinyMCE image-browser plug-in,
        which will have put them into src="../previews/"
        This is a wrapper around the specific types of previews,
        images, media, etc..
        """
        if not hasattr(self.idevice, 'parentNode') \
        or self.idevice.parentNode is None: 
            log.debug('ProcessPreviewed called, but without a '
                       + 'idevice.parentNode; probably from a new object '
                       + 'that was immediately deleted; bailing.')
            return content
        new_content = self.RemoveTemporaryAnchors(content)
        resources_in_use = self.ListActiveResources(new_content)
        self.RemoveZombieResources(resources_in_use)
        self.ProcessInternalAnchors(new_content)
        self.ProcessInternalLinks(new_content)
        new_content = self.ProcessPreviewedImages(new_content)
        new_content = self.ProcessPreviewedMedia(new_content)
        new_content = self.ProcessPreviewedLinkResources(new_content)
        resources_in_use = self.ListActiveResources(new_content)
        self.RemoveZombieResources(resources_in_use)
        return new_content
    def RemoveInternalAnchor(self, this_anchor_name):
        """
        clear out the internal data structures for this anchor,
        once it has been found to have been removed from the content.
        """ 
        if hasattr(self, 'anchors_linked_from_fields') \
        and this_anchor_name in self.anchors_linked_from_fields.keys(): 
            full_anchor_name = self.GetFullNodePath() \
                + "#" + this_anchor_name 
            for that_field in self.anchors_linked_from_fields[\
                this_anchor_name]: 
                that_field.RemoveInternalLinkToRemovedAnchor(\
                    self, full_anchor_name)
            del self.anchors_linked_from_fields[this_anchor_name]
        if this_anchor_name in self.anchor_names:
            self.anchor_names.remove(this_anchor_name)
        if len(self.anchor_names) == 0:
            if self.idevice is not None and self.idevice.parentNode is not None\
            and self.idevice.parentNode.package is not None: 
                this_parentNode = self.idevice.parentNode
                this_package = this_parentNode.package
                if hasattr(this_parentNode, 'anchor_fields') \
                and self in this_parentNode.anchor_fields:
                    this_parentNode.anchor_fields.remove(self)
                if (not hasattr(this_parentNode, 'anchor_fields') \
                or len(this_parentNode.anchor_fields) == 0) \
                and (not hasattr(this_parentNode, 
                    'top_anchors_linked_from_fields') \
                or len(this_parentNode.top_anchors_linked_from_fields) == 0) \
                and hasattr(this_package, 'anchor_nodes') \
                and this_parentNode in this_package.anchor_nodes:
                    this_package.anchor_nodes.remove(this_parentNode)
                if hasattr(this_package, 'anchor_fields') \
                and self in this_package.anchor_fields:
                    this_package.anchor_fields.remove(self)
        return
    def AddInternalAnchor(self, this_anchor_name):
        """
        setup the internal data structures for this anchor,
        once it has been found to exist in the content.
        """ 
        if not hasattr(self, 'anchor_names'):
            self.anchor_names = []
        if this_anchor_name not in self.anchor_names:
            self.anchor_names.append(this_anchor_name)
        if len(self.anchor_names) > 0:
            if self.idevice is not None and self.idevice.parentNode is not None\
            and self.idevice.parentNode.package is not None: 
                this_parentNode = self.idevice.parentNode
                this_package = this_parentNode.package
                if not hasattr(this_parentNode, 'anchor_fields'):
                    this_parentNode.anchor_fields = []
                if self not in this_parentNode.anchor_fields:
                    this_parentNode.anchor_fields.append(self)
                if not hasattr(this_package, 'anchor_nodes'):
                    this_package.anchor_nodes = []
                if this_parentNode not in this_package.anchor_nodes:
                    this_package.anchor_nodes.append(this_parentNode)
                if not hasattr(this_package, 'anchor_fields'):
                    this_package.anchor_fields = []
                if self not in this_package.anchor_fields:
                    this_package.anchor_fields.append(self)
        if not hasattr(self, 'anchors_linked_from_fields'):
            self.anchors_linked_from_fields = {}
        if this_anchor_name not in self.anchors_linked_from_fields.values(): 
            self.anchors_linked_from_fields.setdefault(this_anchor_name, [])
        return
    def ProcessInternalAnchors(self, html_content):
        """
        Perform all of the data structure linking for internal anchors,
        using the self.anchor_names which will be updated
        via ListActiveAnchors(), and connecting (or disconnecting) to/from
        the package's anchor_field and anchor_nodes lists, as well as the
        parentNode's anchor_fields list.  While many of these lists are
        indeed rather redundant, they will save lots of processing time
        when looking for all the places an anchor is referenced, etc.
        """
        if hasattr(self, 'anchor_names'): 
            old_anchor_names = self.anchor_names
        else:
            old_anchor_names = []
        new_anchor_names = self.ListActiveAnchors(html_content)
        for this_anchor in new_anchor_names:
            if this_anchor not in old_anchor_names:
                self.AddInternalAnchor(this_anchor)
        for this_anchor in old_anchor_names:
            if this_anchor not in new_anchor_names:
                self.RemoveInternalAnchor(this_anchor)
        self.anchor_names = new_anchor_names
        return
    def RemoveAllInternalLinks(self):
        """
        Ensemble method wrapper around RemoveInternalLink(),
        to remove ALL internal links of this field from
        the corresponding data structures, for when
        nodes or idevices are deleted, etc.
        """
        if not hasattr(self, 'intlinks_to_anchors'):
            self.intlinks_to_anchors = {}
        for this_link_name in self.intlinks_to_anchors.keys():
            this_anchor_name = common.getAnchorNameFromLinkName(
                    this_link_name)
            self.RemoveInternalLink(this_link_name, this_anchor_name,
                    self.intlinks_to_anchors[this_link_name])
        return
    def RemoveInternalLink(self, full_link_name, link_anchor_name, link_field):
        """
        clear out the internal data structures for this internal link,
        once it has been found to no longer exist in the content.
        """ 
        link_node = None
        this_package = None
        if self.idevice and self.idevice.parentNode:
            this_package = self.idevice.parentNode.package
        if isinstance(link_field, Node):
            link_node = link_field
        if link_field is None:
            link_node_name = full_link_name[ 0 : -(len(link_anchor_name)+1)]
            found_node = common.findLinkedNode(this_package, 
                            link_node_name, link_anchor_name)
            if found_node:
                link_node = found_node
            else: 
                log.warn('Did not find link_field; unable to remove '
                    + 'internal link data structures for:' + link_anchor_name)
                return
        if link_node:
            if not hasattr(link_node, 'top_anchors_linked_from_fields'):
                link_node.top_anchors_linked_from_fields = []
            if self in link_node.top_anchors_linked_from_fields:
                link_node.top_anchors_linked_from_fields.remove(self)
            if len(link_node.top_anchors_linked_from_fields)==0 \
            and (not hasattr(link_node, 'anchor_fields') \
            or len(link_node.anchor_fields) == 0):
                if this_package and not hasattr(this_package, 'anchor_nodes'):
                    this_package.anchor_nodes = []
                if this_package and link_node in this_package.anchor_nodes:
                    this_package.anchor_nodes.remove(link_node)
            log.debug('Removed internal link to auto_top: ' + full_link_name)
            return
        if hasattr(link_field, 'anchors_linked_from_fields') \
        and link_anchor_name in link_field.anchors_linked_from_fields.keys() \
        and self in link_field.anchors_linked_from_fields[link_anchor_name]:
            link_field.anchors_linked_from_fields[link_anchor_name].remove(self)
        log.debug('Removed internal link to anchor: ' + full_link_name)
        return
    def AddInternalLink(self, full_link_name, link_anchor_name, link_field):
        """
        setup the internal data structures for this internal link,
        once it has been found to exist in the content.
        """ 
        link_node = None
        this_package = None
        if self.idevice and self.idevice.parentNode:
            this_package = self.idevice.parentNode.package
        if isinstance(link_field, Node):
            link_node = link_field
        if link_field is None:
            link_node_name = full_link_name[ 0 : -(len(link_anchor_name)+1)]
            found_node = common.findLinkedNode(this_package, 
                            link_node_name, link_anchor_name)
            if found_node:
                link_node = found_node
            else:
                log.warn('Did not find link_field; unable to add internal link '
                    + 'data structures for:' + link_anchor_name)
                return
        if link_node:
            if not hasattr(link_node, 'top_anchors_linked_from_fields'):
                link_node.top_anchors_linked_from_fields = []
            if self not in link_node.top_anchors_linked_from_fields:
                link_node.top_anchors_linked_from_fields.append(self)
            if this_package and not hasattr(this_package, 'anchor_nodes'):
                this_package.anchor_nodes = []
            if this_package and link_node not in this_package.anchor_nodes:
                this_package.anchor_nodes.append(link_node)
            return
        if not hasattr(link_field, 'anchors_linked_from_fields'):
            link_field.anchors_linked_from_fields = {}
        if link_anchor_name not in \
            link_field.anchors_linked_from_fields.values(): 
            link_field.anchors_linked_from_fields.setdefault(
                    link_anchor_name, [])
        if self not in link_field.anchors_linked_from_fields[link_anchor_name]:
            link_field.anchors_linked_from_fields[link_anchor_name].append(self)
        return
    def ProcessInternalLinks(self, html_content):
        """
        Perform all of the data structure linking for internal links.
        Source side of an internal link to a destination anchor already
        taken care of by ProcessInternalAnchors().
        """
        if hasattr(self, 'intlinks_to_anchors'):
            old_intlinks = self.intlinks_to_anchors
        else:
            old_intlinks = {}
        new_intlinks_to_anchors = self.ListActiveInternalLinks(html_content)
        for this_link_name in new_intlinks_to_anchors.keys():
            if this_link_name not in old_intlinks.keys():
                this_anchor_name = common.getAnchorNameFromLinkName(
                        this_link_name)
                self.AddInternalLink(this_link_name, this_anchor_name,
                        new_intlinks_to_anchors[this_link_name])
        for this_link_name in old_intlinks.keys():
            if this_link_name not in new_intlinks_to_anchors.keys():
                this_anchor_name = common.getAnchorNameFromLinkName(
                        this_link_name)
                self.RemoveInternalLink(this_link_name, this_anchor_name,
                        old_intlinks[this_link_name])
        self.intlinks_to_anchors = new_intlinks_to_anchors
        return
    def ProcessPreviewedMedia(self, content):
        """
        STOLEN from ProcessPreviewedImages(), the functionality here is
        EXTREMELY close, yet just different enough to make it worth
        processing in a separate pass.
        To build up the corresponding resources from any media (etc.) added
        in by the tinyMCE media-browser plug-in,
        which will have put them into src="/previews/"
        NOTE: subtle difference from images that go into src="../previews/"
        AND: rather than a simple <img src="../previews/IMAGEFILE"> tag,
        the media will be embedded in a more complex multi-line multi-tag,
        as shown above before this routine.
        """
        new_content = content
        search_str = "<param name=\"src\" value=\"/previews/" 
        found_pos = new_content.find(search_str) 
        while found_pos >= 0: 
            end_pos = new_content.find('\"', found_pos+len(search_str)) 
            if end_pos == -1: 
                end_pos = new_content.find('&quot', found_pos+1) 
            else: 
                end_pos2 = new_content.find('&quot', found_pos+1) 
                if end_pos2 > 0 and end_pos2 < end_pos:
                    end_pos = end_pos2
            if end_pos >= found_pos:
               file_url_str = new_content[found_pos:end_pos] 
               pre_input_file_name_str = file_url_str[len(search_str):]
               log.debug("ProcessPreviewedMedia: found escaped file = " \
                           + pre_input_file_name_str)
               converter = HtmlToText(pre_input_file_name_str)
               input_file_name_str = converter.convertToText()
               log.debug("ProcessPreviewedMedia: unescaped filename = " \
                           + input_file_name_str)
               webDir     = Path(G.application.tempWebDir)
               previewDir  = webDir.joinpath('previews')
               server_filename = previewDir.joinpath(input_file_name_str);
               file_name_str = server_filename.abspath().encode('utf-8');
               if os.path.exists(file_name_str) \
               and os.path.isfile(file_name_str): 
                   embed_mp3_player = False
                   exe_mp3_parmline = "<param name=\"exe_mp3\" " \
                           + "value=\"/previews/" \
                           + pre_input_file_name_str
                   if new_content.find(exe_mp3_parmline) >= 0:
                       embed_mp3_player = True
                       log.debug('ProcessPreviewedMedia: this is an eXe mp3.')
                   embed_flv_player = False
                   exe_flv_parmline = "<param name=\"exe_flv\" " \
                           + "value=\"/previews/" \
                           + pre_input_file_name_str
                   if new_content.find(exe_flv_parmline) >= 0:
                       embed_flv_player = True
                       log.debug('ProcessPreviewedMedia: this is an eXe flv.')
                       if embed_mp3_player:
                           log.warn('ProcessPreviewedMedia: using FLV rather than mp3!')
                           embed_mp3_player = False
                   basename_value = ""
                   descrip_file_path = Path(server_filename + ".exe_info")
                   if os.path.exists(descrip_file_path) \
                   and os.path.isfile(descrip_file_path): 
                       descrip_file = open(descrip_file_path, 'rb')
                       basename_info = descrip_file.read().decode('utf-8')
                       log.debug("ProcessPreviewedMedia: decoded basename = " \
                           + basename_info)
                       basename_key_str = "basename="
                       basename_found_pos = basename_info.find(basename_key_str) 
                       if basename_found_pos == 0: 
                           basename_value = \
                                   basename_info[len(basename_key_str):] 
                           bases_dir = previewDir.joinpath('allyourbase')
                           if not bases_dir.exists():
                               bases_dir.makedirs()
                           base_file_name = bases_dir.joinpath( \
                                   toUnicode(basename_value))
                           base_file_str = base_file_name.abspath()
                           log.debug("ProcessPreviewedMedia: copied to "
                                  + "basefile = " + base_file_str)
                           shutil.copyfile(file_name_str, base_file_str)
                           file_name_str = base_file_str
                   self.setParentNode()
                   from exe.engine.galleryidevice  import GalleryImage
                   new_GalleryImage = GalleryImage(self, \
                                                    '', file_name_str, \
                                                    mkThumbnail=False)
                   new_GalleryImageResource = new_GalleryImage._imageResource
                   resource_path = new_GalleryImageResource._storageName
                   resource_url = new_GalleryImage.resourcesUrl+resource_path 
                   if embed_mp3_player:
                       resource_url = "../templates/xspf_player.swf" \
                               + "?song_url=" + resource_url \
                               + "&song_title=" + resource_path
                       self.idevice.systemResources += ['xspf_player.swf']
                   if embed_flv_player:
                       self.idevice.systemResources += ['flowPlayer.swf']
                   if not embed_flv_player:
                       new_src_string = "<param name=\"src\" value=\""\
                               +resource_url
                   else:
                       new_src_string = "<param name=\"flv_src\" value=\""\
                               +resource_url
                   new_content = new_content.replace(file_url_str, 
                                                     new_src_string)
                   log.debug("ProcessPreviewedMedia: built resource: " \
                           + resource_url)
                   embed_search_str = "src=\"/previews/"+pre_input_file_name_str
                   if not embed_flv_player:
                       embed_replace_str = "src=\"" + resource_url
                   else:
                       embed_replace_str = "flv_src=\"" + resource_url
                   new_content = new_content.replace(embed_search_str,
                                                     embed_replace_str)
                   embed_search_str = "x-ms-wmv\" data=\"/previews/"\
                           + pre_input_file_name_str
                   embed_replace_str = "x-ms-wmv\" data=\"" + resource_url
                   new_content = new_content.replace(embed_search_str,
                                                     embed_replace_str)
                   if embed_mp3_player:
                       embed_search_str = "<param name=\"exe_mp3\" " \
                           + "value=\"/previews/" \
                           + pre_input_file_name_str
                       embed_replace_str = "<param name=\"exe_mp3\" " \
                           + "value=\"" + resource_url
                       new_content = new_content.replace(embed_search_str,
                                                     embed_replace_str)
                   if embed_flv_player:
                       embed_search_str = "<param name=\"exe_flv\" " \
                           + "value=\"/previews/" \
                           + pre_input_file_name_str
                       embed_replace_str = "<param name=\"exe_flv\" " \
                           + "value=\"" + resource_url
                       new_content = new_content.replace(embed_search_str,
                                                     embed_replace_str)
                       embed_search_str = "playList: [ { url: '/previews/" \
                           + pre_input_file_name_str
                       embed_replace_str = "playList: [ { url: '" + resource_url
                       new_content = new_content.replace(embed_search_str,
                                                     embed_replace_str)
               else:
                   log.warn("file '"+file_name_str+"' does not exist; " \
                           + "unable to include it as a possible media " \
                           + "resource for this TextAreaElement.")
            else:
               log.warn("ProcessPreviewedMedia: file URL string appears " \
                        + "to NOT have a terminating quote.")
            found_pos = new_content.find(search_str, found_pos+1) 
        return new_content
    def ProcessPairedMathSource(self, content, preview_math_src, \
            math_image_resource_filename, math_image_resource_url):
        """
        to build up the corresponding LaTeX math-source file resources 
        from any math images added.  called from ProcessPreviewedImages().
        """
        new_content = content
        log.debug('ProcessPairedMathSource: processing ' \
                + 'exe_math_latex='+preview_math_src)
        quoteless_math_src  = preview_math_src.replace("\"","")
        preview_math_file = quoteless_math_src.replace("src=","")
        math_file = preview_math_file.replace("../previews/","")
        webDir     = Path(G.application.tempWebDir)
        previewDir  = webDir.joinpath('previews')
        full_math_filename = previewDir.joinpath(math_file);
        math_file_name_str = full_math_filename.abspath().encode('utf-8');
        if os.path.exists(math_file_name_str) \
        and os.path.isfile(math_file_name_str): 
            expected_mathsrc_resource_filename = \
                    math_image_resource_filename+".tex"
            if (math_file != expected_mathsrc_resource_filename):
                log.debug('Note: it no longer syncs to the image file, '\
                        + 'which is now named: ' \
                        + math_image_resource_filename)
                bases_dir = previewDir.joinpath('allyourbase') 
                if not bases_dir.exists(): 
                    bases_dir.makedirs() 
                base_file_name = bases_dir.joinpath(\
                        expected_mathsrc_resource_filename) 
                base_file_str = \
                    base_file_name.abspath().encode('utf-8') 
                log.debug('To keep sync with the math image resource, ' \
                        + 'copying math source to: ' + base_file_str \
                        + ' (before resource-ifying).')
                shutil.copyfile(math_file_name_str, base_file_str)
                full_math_filename = base_file_name
                math_file_name_str = base_file_str
            else:
                log.debug('And this exe_math_latex file still syncs with the '
                        + 'image file.')
            from exe.engine.galleryidevice  import GalleryImage
            new_GalleryImage = GalleryImage(self, \
                    '', math_file_name_str, \
                    mkThumbnail=False) 
            new_GalleryImageResource = new_GalleryImage._imageResource 
            mathsrc_resource_path = new_GalleryImageResource._storageName 
            mathsrc_resource_url = new_GalleryImage.resourcesUrl \
                    + mathsrc_resource_path
            if (mathsrc_resource_url != math_image_resource_url+".tex"):
                log.warn('The math source was resource-ified differently ' \
                        + 'than expected, to: ' + mathsrc_resource_url \
                        + '; using it anyhow')
            else:
                log.debug('math source was resource-ified properly to: ' \
                        + mathsrc_resource_url)
            from_str = "exe_math_latex=\""+preview_math_file+"\""
            to_str =   "exe_math_latex=\""+mathsrc_resource_url+"\""
            new_content = new_content.replace(from_str, to_str)
            return new_content
        else:
            log.warn('ProcessPairedMathSource did not find math source at: '\
                    + full_math_filename + '; original LaTeX will be absent.')
            return content
    def ProcessPreviewedLinkResources(self, content):
        """
        NOTE: now that we have 3 versions of ProcessPreviewed*(),
        it might be time to begin exploring a much better, consolidated, 
        approach!  But for now, quickly throw in yet another variation,
        designed to look for <a href="../previews/....">text</a>
        As per ProcessPreviewedMedia(), this was:
        STOLEN from ProcessPreviewedImages(), the functionality here is
        EXTREMELY close, yet just different enough to make it worth
        processing in a separate pass.
        To build up the corresponding resources from any resource added
        in by the tinyMCE advlink-browser plug-in.
        """
        new_content = content
        empty_image_str1 = "<a href=\"/\">"
        empty_image_str2 = "<a href=\"../\">"
        if new_content.find(empty_image_str1) >= 0 \
        or new_content.find(empty_image_str2) >= 0: 
            default_href = "<a href=\"resources/.missingURL\">"
            new_content = new_content.replace(empty_image_str1, default_href);
            new_content = new_content.replace(empty_image_str2, default_href);
            log.warn("Empty href tag(s) pointed to resources/.missingURL");
        search_str = "href=\"../previews/" 
        found_pos = new_content.find(search_str) 
        while found_pos >= 0: 
            end_pos = new_content.find('\"', found_pos+len(search_str)) 
            if end_pos == -1: 
                end_pos = new_content.find('&quot', found_pos+1) 
            else: 
                end_pos2 = new_content.find('&quot', found_pos+1) 
                if end_pos2 > 0 and end_pos2 < end_pos:
                    end_pos = end_pos2
            if end_pos >= found_pos:
               file_url_str = new_content[found_pos:end_pos] 
               pre_input_file_name_str = file_url_str[len(search_str):]
               log.debug("ProcessPreviewedLinkResources: found escaped file = "\
                           + pre_input_file_name_str)
               converter = HtmlToText(pre_input_file_name_str)
               input_file_name_str = converter.convertToText()
               log.debug("ProcessPreviewedLinkResources: unescaped filename = "\
                           + input_file_name_str)
               webDir     = Path(G.application.tempWebDir)
               previewDir  = webDir.joinpath('previews')
               server_filename = previewDir.joinpath(input_file_name_str);
               file_name_str = server_filename.abspath().encode('utf-8');
               if os.path.exists(file_name_str) \
               and os.path.isfile(file_name_str): 
                   basename_value = ""
                   descrip_file_path = Path(server_filename + ".exe_info")
                   if os.path.exists(descrip_file_path) \
                   and os.path.isfile(descrip_file_path): 
                       descrip_file = open(descrip_file_path, 'rb')
                       basename_info = descrip_file.read().decode('utf-8')
                       log.debug("ProcessPreviewedLinkResources: decoded "
                           + "basename = " + basename_info)
                       basename_key_str = "basename="
                       basename_found_pos = basename_info.find(basename_key_str) 
                       if basename_found_pos == 0: 
                           basename_value = \
                                   basename_info[len(basename_key_str):] 
                           bases_dir = previewDir.joinpath('allyourbase')
                           if not bases_dir.exists():
                               bases_dir.makedirs()
                           base_file_name = bases_dir.joinpath( \
                                   toUnicode(basename_value))
                           base_file_str =  base_file_name.abspath()
                           log.debug("ProcessPreviewedLinkResources: copied "
                                  + " to basefile = " + base_file_str)
                           shutil.copyfile(file_name_str, base_file_str)
                           file_name_str = base_file_str
                   self.setParentNode()
                   from exe.engine.galleryidevice  import GalleryImage
                   new_GalleryImage = GalleryImage(self, \
                                                    '', file_name_str, \
                                                    mkThumbnail=False)
                   new_GalleryImageResource = new_GalleryImage._imageResource
                   resource_path = new_GalleryImageResource._storageName
                   resource_url = new_GalleryImage.resourcesUrl+resource_path
                   new_src_string = "href=\""+resource_url
                   new_content = new_content.replace(file_url_str, 
                                                     new_src_string)
                   log.debug("ProcessPreviewedLinkResources: built resource: "\
                           + resource_url)
               else:
                   log.warn("file '"+file_name_str+"' does not exist; " \
                           + "unable to include it as a possible file " \
                           + "resource for this TextAreaElement.")
                   filename_warning = "href=\"resources/.missingURL"
                   new_content = new_content.replace(file_url_str, 
                                                     filename_warning)
            else:
               log.warn("ProcessPreviewedLinkResources: file URL string " \
                        + "appears to NOT have a terminating quote.")
            found_pos = new_content.find(search_str, found_pos+1) 
        return new_content
    def ProcessPreviewedImages(self, content):
        """
        to build up the corresponding resources from any images (etc.) added
        in by the tinyMCE image-browser plug-in,
        which will have put them into src="../previews/"
        Now updated to include special math images as well, as generated
        by our custom exemath plugin to TinyMCE.  These are to follow the
        naming convention of "eXe_LaTeX_math_#.gif" (where the # is only
        guaranteed to be unique per Preview session, and can therefore end
        up being resource-ified into "eXe_LaTeX_math_#.#.gif"). Furthermore,
        they are to be paired with a source LateX file which is to be of
        the same name, followed by .tex, e.g., "eXe_LaTeX_math_#.gif.tex"
        (and to maintain this pairing, as a resource will need to be named
        "eXe_LaTeX_math_#.#.gif.tex" if applicable, where this does differ
        slightly from what could be its automatic unique-ified 
        resource-ification of: "eXe_LaTeX_math_#.gif.#.tex"!!!)
        """
        new_content = content
        empty_image_str = "<img src=\"/\" />"
        if new_content.find(empty_image_str)  >= 0: 
            new_content = new_content.replace(empty_image_str, "");
            log.warn("Empty image tag(s) removed from content");
        search_str = "src=\"../previews/" 
        found_pos = new_content.find(search_str) 
        while found_pos >= 0: 
            end_pos = new_content.find('\"', found_pos+len(search_str)) 
            if end_pos == -1: 
                end_pos = new_content.find('&quot', found_pos+1) 
            else: 
                end_pos2 = new_content.find('&quot', found_pos+1) 
                if end_pos2 > 0 and end_pos2 < end_pos:
                    end_pos = end_pos2
            if end_pos >= found_pos:
               file_url_str = new_content[found_pos:end_pos] 
               pre_input_file_name_str = file_url_str[len(search_str):]
               log.debug("ProcessPreviewedImages: found escaped file = " \
                           + pre_input_file_name_str)
               converter = HtmlToText(pre_input_file_name_str)
               input_file_name_str = converter.convertToText()
               log.debug("ProcessPreviewedImages: unescaped filename = " \
                           + input_file_name_str)
               webDir     = Path(G.application.tempWebDir)
               previewDir  = webDir.joinpath('previews')
               server_filename = previewDir.joinpath(input_file_name_str);
               file_name_str = server_filename.abspath().encode('utf-8');
               if os.path.exists(file_name_str) \
               and os.path.isfile(file_name_str): 
                   basename_value = ""
                   descrip_file_path = Path(server_filename + ".exe_info")
                   if os.path.exists(descrip_file_path) \
                   and os.path.isfile(descrip_file_path): 
                       descrip_file = open(descrip_file_path, 'rb')
                       basename_info = descrip_file.read().decode('utf-8')
                       log.debug("ProcessPreviewedImages: decoded basename = " \
                           + basename_info)
                       basename_key_str = "basename="
                       basename_found_pos = basename_info.find(basename_key_str) 
                       if basename_found_pos == 0: 
                           basename_value = \
                                   basename_info[len(basename_key_str):] 
                           bases_dir = previewDir.joinpath('allyourbase')
                           if not bases_dir.exists():
                               bases_dir.makedirs()
                           base_file_name = bases_dir.joinpath( \
                                   toUnicode(basename_value))
                           base_file_str =  base_file_name.abspath()
                           log.debug("ProcessPreviewedImages: copied to "
                                  + "basefile = " + base_file_str)
                           shutil.copyfile(file_name_str, base_file_str)
                           file_name_str = base_file_str
                   self.setParentNode()
                   from exe.engine.galleryidevice  import GalleryImage
                   new_GalleryImage = GalleryImage(self, \
                                                    '', file_name_str, \
                                                    mkThumbnail=False)
                   new_GalleryImageResource = new_GalleryImage._imageResource
                   resource_path = new_GalleryImageResource._storageName
                   resource_url = new_GalleryImage.resourcesUrl+resource_path
                   new_src_string = "src=\""+resource_url
                   new_content = new_content.replace(file_url_str, 
                                                     new_src_string)
                   log.debug("ProcessPreviewedImages: built resource: " \
                           + resource_url)
                   if resource_path.find("eXe_LaTeX_math_") >= 0:
                       preview_math_src = file_url_str + ".tex\""
                       new_content = self.ProcessPairedMathSource(new_content,\
                               preview_math_src, resource_path, resource_url)
               else:
                   log.warn("file '"+file_name_str+"' does not exist; " \
                           + "unable to include it as a possible image " \
                           + "resource for this TextAreaElement.")
                   filename_warning = "src=\"WARNING_FILE="+file_name_str \
                           +"=DOES_NOT_EXIST"
                   new_content = new_content.replace(file_url_str, 
                                                     filename_warning)
            else:
               log.warn("ProcessPreviewedImages: file URL string appears " \
                        + "to NOT have a terminating quote.")
            found_pos = new_content.find(search_str, found_pos+1) 
        return new_content
    def MassageContentForRenderView(self, content): 
        """
        Returns an XHTML string for viewing this resource-laden element 
        upon export, since the resources will be flattened no longer exist 
        in the system resources directory....
        This is a wrapper around the specific types of previews,
        images, media, etc..
        """
        new_content = self.MassageImageContentForRenderView(content)
        new_content = self.MassageMediaContentForRenderView(new_content)
        new_content = self.MassageLinkResourceContentForRenderView(new_content)
        return new_content 
    def MassageMediaContentForRenderView(self, content):
        """
        Stolen and Modified straight from MassageImageContentForRenderView()
        Returns an XHTML string for viewing this resource-laden element 
        upon export, since the resources will be flattened no longer exist 
        in the system resources directory....
        """
        resources_url_src = "src=\"resources/"
        exported_src = "src=\""
        export_content = content.replace(resources_url_src,exported_src)
        resources_url_src = "<param name=\"src\" value=\"resources/"
        exported_src = "<param name=\"src\" value=\""
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = "x-ms-wmv\" data=\"resources/" 
        exported_src = "x-ms-wmv\" data=\"" 
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = \
                "src=\"../templates/xspf_player.swf?song_url=resources/"
        exported_src =  "src=\"xspf_player.swf?song_url="
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = \
                "exe_mp3=\"../templates/xspf_player.swf?song_url=resources/"
        exported_src =  "exe_mp3=\"xspf_player.swf?song_url="
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = "<param name=\"src\" "\
                + "value=\"../templates/xspf_player.swf?song_url=resources/"
        exported_src = "<param name=\"src\" value=\"xspf_player.swf?song_url="
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = "<param name=\"exe_mp3\" "\
                + "value=\"../templates/xspf_player.swf?song_url=resources/"
        exported_src = "<param name=\"exe_mp3\" "\
                + "value=\"xspf_player.swf?song_url="
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = \
                "data=\"../templates/flowPlayer.swf"
        exported_src =  "data=\"flowPlayer.swf"
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = "<param name=\"data\" "\
                + "value=\"../templates/flowPlayer.swf"
        exported_src = "<param name=\"data\" value=\"flowPlayer.swf"
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = "<param name=\"movie\" "\
                + "value=\"../templates/flowPlayer.swf"
        exported_src = "<param name=\"movie\" value=\"flowPlayer.swf"
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = "<param name=\"flv_src\" "\
                + "value=\"resources/"
        exported_src = "<param name=\"flv_src\" "\
                + "value=\""
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = \
                "exe_flv=\"resources/"
        exported_src =  "exe_flv=\""
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = "<param name=\"exe_flv\" "\
                + "value=\"resources/"
        exported_src = "<param name=\"exe_flv\" "\
                + "value=\""
        export_content = export_content.replace(resources_url_src,exported_src)
        resources_url_src = "playList: [ { url: 'resources/" 
        exported_src = "playList: [ { url: '" 
        export_content = export_content.replace(resources_url_src,exported_src)
        return export_content
    def MassageLinkResourceContentForRenderView(self, content):
        """
        Stolen and Modified straight from MassageImageContentForRenderView()
        Returns an XHTML string for viewing this resource-laden element 
        upon export, since the resources will be flattened no longer exist 
        in the system resources directory....
        """
        resources_url_src = "href=\"resources/"
        exported_src = "href=\""
        export_content = content.replace(resources_url_src,exported_src)
        return export_content
    def MassageImageContentForRenderView(self, content):
        """
        Returns an XHTML string for viewing this resource-laden element 
        upon export, since the resources will be flattened no longer exist 
        in the system resources directory....
        """
        resources_url_src = "src=\"resources/"
        exported_src = "src=\""
        export_content = content.replace(resources_url_src,exported_src)
        resources_url_src = "exe_math_latex=\"resources/"
        exported_src = "exe_math_latex=\""
        export_content = export_content.replace(resources_url_src,exported_src)
        return export_content
    def upgradeToVersion1(self):
        """
        Upgrade to allow the images embedded via tinyMCE to
        persist with this field
        """
        from exe.engine.galleryidevice  import GalleryImages
        self.images = GalleryImages(self)
        self.nextImageId       = 0
        self.parentNode = None
        if hasattr(self.idevice, 'parentNode'): 
            self.parentNode = self.idevice.parentNode
    def upgradeToVersion2(self):
        """ 
        remove any extraneous thumbnails which were created with some of the 
        earlier embedded resources, in v0.95 - v0.98, due to the earlier use
        of GalleryImages, which, be default, would create such a thumbail.
        """
        for image in self.images:
            if (not hasattr(image, 'makeThumbnail') or image.makeThumbnail) \
            and image._thumbnailResource:
                log.debug('FieldWithResource: removing unused thumbnail: '\
                        + repr(image._thumbnailResource.storageName))
                image._thumbnailResource.delete()
                image._thumbnailResource = None
                image.makeThumbnail = False
class TextAreaField(FieldWithResources):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    Note that TextAreaFields can now hold any number of image resources,
    which will typically be inserted by way of tinyMCE.
    """
    persistenceVersion = 1
    nonpersistant      = ['content', 'content_wo_resourcePaths']
    def __init__(self, name, instruc="", content=""):
        """
        Initialize 
        """
        FieldWithResources.__init__(self, name, instruc, content)
    def upgradeToVersion1(self):
        """
        Upgrades to somewhere before version 0.25 (post-v0.24) 
        to reflect that TextAreaField now inherits from FieldWithResources,
        and will need its corresponding fields populated from content.
        """ 
        self.content_w_resourcePaths = self.content 
        self.content_wo_resourcePaths = self.content
class FeedbackField(FieldWithResources):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    persistenceVersion = 2
    nonpersistant      = ['content', 'content_wo_resourcePaths']
    def __init__(self, name, instruc=""):
        """
        Initialize 
        """
        FieldWithResources.__init__(self, name, instruc)
        self._buttonCaption = x_(u"Click Here")
        self.feedback      = ""
        self.content      = ""
    buttonCaption = lateTranslate('buttonCaption')
    def upgradeToVersion1(self):
        """
        Upgrades to version 0.14
        """
        self.buttonCaption = self.__dict__['buttonCaption']
    def upgradeToVersion2(self):
        """
        Upgrades to somewhere before version 0.25 (post-v0.24) 
        to reflect that FeedbackField now inherits from FieldWithResources,
        and will need its corresponding fields populated from content.
        [see also the related (and likely redundant) upgrades to FeedbackField 
         in: idevicestore.py's  __upgradeGeneric() for readingActivity, 
         and: genericidevice.py's upgradeToVersion9() for the same]
        """ 
        self.content = self.feedback 
        self.content_w_resourcePaths = self.feedback 
        self.content_wo_resourcePaths = self.feedback
class ImageField(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    persistenceVersion = 3
    isDefaultImage = True
    def __init__(self, name, instruc=""):
        """
        """
        Field.__init__(self, name, instruc)
        self.width         = ""
        self.height        = ""
        self.imageResource = None
        self.defaultImage  = ""
        self.isDefaultImage = True
        self.isFeedback    = False
    def setImage(self, imagePath):
        """
        Store the image in the package
        Needs to be in a package to work.
        """
        log.debug(u"setImage "+unicode(imagePath))
        resourceFile = Path(imagePath)
        assert(self.idevice.parentNode,
               'Image '+self.idevice.id+' has no parentNode')
        assert(self.idevice.parentNode.package,
               'iDevice '+self.idevice.parentNode.id+' has no package')
        if resourceFile.isfile():
            if self.imageResource:
                self.imageResource.delete()
            self.imageResource = Resource(self.idevice, resourceFile)
            self.isDefaultImage  = False
        else:
            log.error('File %s is not a file' % resourceFile)
    def setDefaultImage(self):
        """
        Set a default image to display until the user picks one
        """
        if self.defaultImage:
            self.setImage(self.defaultImage)
            self.isDefaultImage = True
    def _upgradeFieldToVersion2(self):
        """
        Upgrades to exe v0.12
        """
        log.debug("ImageField upgrade field to version 2")
        idevice = self.idevice or self.__dict__.get('idevice')
        package = idevice.parentNode.package
        if not hasattr(package, 'resources'):
            package.resources = {}
        imgPath = package.resourceDir/self.imageName
        if self.imageName and idevice.parentNode:
            self.imageResource = Resource(idevice, imgPath)
        else:
            self.imageResource = None
        del self.imageName
    def _upgradeFieldToVersion3(self):
        """
        Upgrades to exe v0.24
        """
        self.isFeedback    = False
class MagnifierField(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    persistenceVersion = 2
    def __init__(self, name, instruc=""):
        """
        """
        Field.__init__(self, name, instruc)
        self.width         = "100"
        self.height        = "100"
        self.imageResource = None
        self.defaultImage  = ""
        self.glassSize     = "2"
        self.initialZSize  = "100"
        self.maxZSize      = "150"
        self.message       = ""
        self.isDefaultImage= True
    def setImage(self, imagePath):
        """
        Store the image in the package
        Needs to be in a package to work.
        """
        log.debug(u"setImage "+unicode(imagePath))
        resourceFile = Path(imagePath)
        assert(self.idevice.parentNode,
               'Image '+self.idevice.id+' has no parentNode')
        assert(self.idevice.parentNode.package,
               'iDevice '+self.idevice.parentNode.id+' has no package')
        if resourceFile.isfile():
            if self.imageResource:
                self.imageResource.delete()
            self.imageResource = Resource(self.idevice, resourceFile)
            self.isDefaultImage = False
        else:
            log.error('File %s is not a file' % resourceFile)
    def setDefaultImage(self):
        """
        Set a default image to display until the user picks one
        """
        if self.defaultImage:
            self.setImage(self.defaultImage)
            self.isDefaultImage = True
    def _upgradeFieldToVersion2(self):
        """
        Upgrades to exe v0.24
        """
        self.message   = ""
        self.isDefaultImage = False
class MultimediaField(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    persistenceVersion = 2
    def __init__(self, name, instruc=""):
        """
        """
        Field.__init__(self, name, instruc)
        self.width         = "320"
        self.height        = "100"
        self.mediaResource = None
        self.caption       = ""
        self._captionInstruc = x_(u"""Provide a caption for the 
MP3 file. This will appear in the players title bar as well.""")
    captionInstruc    = lateTranslate('captionInstruc')
    def setMedia(self, mediaPath):
        """
        Store the media file in the package
        Needs to be in a package to work.
        """
        log.debug(u"setMedia "+unicode(mediaPath))
        resourceFile = Path(mediaPath)
        assert(self.idevice.parentNode,
               'Media '+self.idevice.id+' has no parentNode')
        assert(self.idevice.parentNode.package,
               'iDevice '+self.idevice.parentNode.id+' has no package')
        if resourceFile.isfile():
            if self.mediaResource:
                self.mediaResource.delete()
            self.mediaResource = Resource(self.idevice, resourceFile)
            if '+' in self.mediaResource.storageName:
                path = self.mediaResource.path
                newPath = path.replace('+','')
                Path(path).rename(newPath)
                self.mediaResource._storageName = \
                    self.mediaResource.storageName.replace('+','')
                self.mediaResource._path = newPath
        else:
            log.error('File %s is not a file' % resourceFile)
    def upgradeToVersion2(self):
        """
        Upgrades to exe v0.20
        """
        Field.upgradeToVersion2(self)
        if hasattr(Field, 'updateToVersion2'):
            Field.upgradeToVersion2(self)
        if hasattr(self.idevice, 'caption'):
            self.caption = self.idevice.caption
        elif self.mediaResource:
            self.caption = self.mediaResource.storageName 
        else:
            self.caption   = ""
        self._captionInstruc = x_(u"""Provide a caption for the 
MP3 file. This will appear in the players title bar as well.""")
class ClozeHTMLParser(HTMLParser):
    """
    Separates out gaps from our raw cloze data
    """
    result = None
    inGap = False
    lastGap = ''
    lastText = ''
    whiteSpaceRe = re.compile(r'\s+')
    paragraphRe = re.compile(r'(\r\n\r\n)([^\r]*)(\1)')
    def reset(self):
        """
        Make our data ready
        """
        HTMLParser.reset(self)
        self.result = []
        self.inGap = False
        self.lastGap = ''
        self.lastText = ''
    def handle_starttag(self, tag, attrs):
        """
        Turn on inGap if necessary
        """
        if not self.inGap:
            if tag.lower() == 'u':
                self.inGap = True
            elif tag.lower() == 'span':
                style = dict(attrs).get('style', '')
                if 'underline' in style:
                    self.inGap = True
                else:
                    self.writeTag(tag, attrs)
            elif tag.lower() == 'br':
                self.lastText += '<br/>' 
            else:
                self.writeTag(tag, attrs)
    def writeTag(self, tag, attrs=None):
        """
        Outputs a tag "as is"
        """
        if attrs is None:
            self.lastText += '</%s>' % tag
        else:
            attrs = ['%s="%s"' % (name, val) for name, val in attrs]
            if attrs:
                self.lastText += '<%s %s>' % (tag, ' '.join(attrs))
            else:
                self.lastText += '<%s>' % tag
    def handle_endtag(self, tag):
        """
        Turns off inGap
        """
        if self.inGap:
            if tag.lower() == 'u':
                self.inGap = False
                self._endGap()
            elif tag.lower() == 'span':
                self.inGap = False
                self._endGap()
        elif tag.lower() != 'br':
            self.writeTag(tag)
    def _endGap(self):
        """
        Handles finding the end of gap
        """
        gapString = self.lastGap.strip()
        gapWords = self.whiteSpaceRe.split(gapString)
        gapSpacers = self.whiteSpaceRe.findall(gapString)
        if len(gapWords) > len(gapSpacers):
            gapSpacers.append(None)
        gaps = zip(gapWords, gapSpacers)
        lastText = self.lastText
        for gap, text in gaps:
            if gap == '<br/>':
                self.result.append((lastText, None))
            else:
                self.result.append((lastText, gap))
            lastText = text
        self.lastGap = ''
        self.lastText = ''
    def handle_data(self, data):
        """
        Adds the data to either lastGap or lastText
        """
        if self.inGap:
            self.lastGap += data
        else:
            self.lastText += data
    def close(self):
        """
        Fills in the last bit of result
        """
        if self.lastText:
            self._endGap()
        HTMLParser.close(self)
class ClozeField(FieldWithResources):
    """
    This field handles a passage with words that the student must fill in
    And can now support multiple images (and any other resources) via tinyMCE
    """
    regex = re.compile('(%u)((\d|[A-F]){4})', re.UNICODE)
    persistenceVersion = 3
    nonpersistant      = ['content', 'content_wo_resourcePaths']
    def __init__(self, name, instruc):
        """
        Initialise
        """
        FieldWithResources.__init__(self, name, instruc)
        self.parts = []
        self._encodedContent = ''
        self.rawContent = ''
        self._setVersion2Attributes()
    def _setVersion2Attributes(self):
        """
        Sets the attributes that were added in persistenceVersion 2
        """
        self.strictMarking = False
        self._strictMarkingInstruc = \
            x_(u"<p>If left unchecked a small number of spelling and "
                "capitalization errors will be accepted. If checked only "
                "an exact match in spelling and capitalization will be accepted."
                "</p>"
                "<p><strong>For example:</strong> If the correct answer is "
                "<code>Elephant</code> then both <code>elephant</code> and "
                "<code>Eliphant</code> will be judged "
                "<em>\"close enough\"</em> by the algorithm as it only has "
                "one letter wrong, even if \"Check Capitilization\" is on."
                "</p>"
                "<p>If capitalization checking is off in the above example, "
                "the lowercase <code>e</code> will not be considered a "
                "mistake and <code>eliphant</code> will also be accepted."
                "</p>"
                "<p>If both \"Strict Marking\" and \"Check Capitalization\" "
                "are set, the only correct answer is \"Elephant\". If only "
                "\"Strict Marking\" is checked and \"Check Capitalization\" "
                "is not, \"elephant\" will also be accepted."
                "</p>")
        self.checkCaps = False
        self._checkCapsInstruc = \
            x_(u"<p>If this option is checked, submitted answers with "
                "different capitalization will be marked as incorrect."
                "</p>")
        self.instantMarking = False
        self._instantMarkingInstruc = \
            x_(u"""<p>If this option is set, each word will be marked as the 
learner types it rather than all the words being marked the end of the 
exercise.</p>""")
    def set_encodedContent(self, value):
        """
        Cleans out the encoded content as it is passed in. Makes clean XHTML.
        """
        for key, val in name2codepoint.items():
            value = value.replace('&%s;' % key, unichr(val))
        value = re.sub(r'font-family:\s*"([^"]+)"', r'font-family: \1', value)
        parser = ClozeHTMLParser()
        parser.feed(value)
        parser.close()
        self.parts = parser.result
        encodedContent = ''
        for shown, hidden in parser.result:
            encodedContent += shown
            if hidden:
                encodedContent += ' <u>'
                encodedContent += hidden
                encodedContent += '</u> ' 
        self._encodedContent = encodedContent
    encodedContent        = property(lambda self: self._encodedContent, 
                                     set_encodedContent)
    strictMarkingInstruc  = lateTranslate('strictMarkingInstruc')
    checkCapsInstruc      = lateTranslate('checkCapsInstruc')
    instantMarkingInstruc = lateTranslate('instantMarkingInstruc')
    def upgradeToVersion1(self):
        """
        Upgrades to exe v0.11
        """
        self.autoCompletion = True
        self.autoCompletionInstruc = _(u"""Allow auto completion when 
                                       user filling the gaps.""")
    def upgradeToVersion2(self):
        """
        Upgrades to exe v0.12
        """
        Field.upgradeToVersion2(self)
        strictMarking = not self.autoCompletion
        del self.autoCompletion
        del self.autoCompletionInstruc
        self._setVersion2Attributes()
        self.strictMarking = strictMarking
    def upgradeToVersion3(self):
        """
        Upgrades to somewhere before version 0.25 (post-v0.24) 
        to reflect that ClozeField now inherits from FieldWithResources,
        and will need its corresponding fields populated from content.
        """ 
        self.content = self.encodedContent
        self.content_w_resourcePaths = self.encodedContent
        self.content_wo_resourcePaths = self.encodedContent
class FlashField(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    def __init__(self, name, instruc=""):
        """
        Set default elps.
        """
        Field.__init__(self, name, instruc)
        self.width         = 300
        self.height        = 250
        self.flashResource = None
        self._fileInstruc   = x_("""Only select .swf (Flash Objects) for 
this iDevice.""")
    fileInstruc = lateTranslate('fileInstruc')
    def setFlash(self, flashPath):
        """
        Store the image in the package
        Needs to be in a package to work.
        """
        log.debug(u"setFlash "+unicode(flashPath))
        resourceFile = Path(flashPath)
        assert(self.idevice.parentNode,
               'Flash '+self.idevice.id+' has no parentNode')
        assert(self.idevice.parentNode.package,
               'iDevice '+self.idevice.parentNode.id+' has no package')
        if resourceFile.isfile():
            if self.flashResource:
                self.flashResource.delete()
            self.flashResource = Resource(self.idevice, resourceFile)
        else:
            log.error('File %s is not a file' % resourceFile)
    def _upgradeFieldToVersion2(self):
        """
        Upgrades to exe v0.12
        """
        if hasattr(self, 'flashName'): 
            if self.flashName and self.idevice.parentNode:
                self.flashResource = Resource(self.idevice, Path(self.flashName))
            else:
                self.flashResource = None
            del self.flashName
    def _upgradeFieldToVersion3(self):
        """
        Upgrades to exe v0.13
        """
        self._fileInstruc   = x_("""Only select .swf (Flash Objects) for 
this iDevice.""")
class FlashMovieField(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    persistenceVersion = 4
    def __init__(self, name, instruc=""):
        """
        """
        Field.__init__(self, name, instruc)
        self.width         = 320
        self.height        = 240
        self.flashResource = None
        self.message       = ""
        self._fileInstruc   = x_("""Only select .flv (Flash Video Files) for 
this iDevice.""")
    fileInstruc = lateTranslate('fileInstruc')
    def setFlash(self, flashPath):
        """
        Store the image in the package
        Needs to be in a package to work.
        """
        log.debug(u"setFlash "+unicode(flashPath))
        resourceFile = Path(flashPath)
        assert(self.idevice.parentNode,
               'Flash '+self.idevice.id+' has no parentNode')
        assert(self.idevice.parentNode.package,
               'iDevice '+self.idevice.parentNode.id+' has no package')
        if resourceFile.isfile():
            if self.flashResource:
                self.flashResource.delete()
            try:
                flvDic = FLVReader(resourceFile)
                self.height = flvDic.get("height", 240)+30
                self.width = flvDic.get("width", 320)
                self.flashResource = Resource(self.idevice, resourceFile)
            except AssertionError: 
                log.error('File %s is not a flash movie' % resourceFile)
        else:
            log.error('File %s is not a file' % resourceFile)
    def _upgradeFieldToVersion2(self):
        """
        Upgrades to exe v0.12
        """
        if hasattr(self, 'flashName'):
            if self.flashName and self.idevice.parentNode:
                self.flashResource = Resource(self.idevice, Path(self.flashName))
            else:
                self.flashResource = None
            del self.flashName
    def _upgradeFieldToVersion3(self):
        """
        Upgrades to exe v0.14
        """
        self._fileInstruc   = x_("""Only select .flv (Flash Video Files) for 
this iDevice.""")
    def _upgradeFieldToVersion4(self):
        """
        Upgrades to exe v0.20.3
        """
        self.message   = ""
class DiscussionField(Field):
    def __init__(self, name, instruc=x_("Type a discussion topic here."), content="" ):
        """
        Initialize 
        """
        Field.__init__(self, name, instruc)
        self.content = content
class MathField(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    persistenceVersion = 1
    def __init__(self, name, instruc="", latex=""):
        """
        Initialize 
        'self._latex' is a string of latex
        'self.gifResource' is a resouce that points to a cached gif
        rendered from the latex
        """
        Field.__init__(self, name, instruc)
        self._latex      = latex # The latex entered by the user
        self.gifResource = None
        self.fontsize    = 4
        self._instruc    = x_(u""
            "<p>" 
            "Select symbols from the text editor below or enter LATEX manually"
            " to create mathematical formula."
            " To preview your LATEX as it will display use the &lt;Preview&gt;"
            " button below."
            "</p>"
            )
        self._previewInstruc = x_("""Click on Preview button to convert 
                                  the latex into an image.""")
    def get_latex(self):
        """
        Returns latex string
        """
        return self._latex
    def set_latex(self, latex):
        """
        Replaces current gifResource
        """
        if self.gifResource is not None:
            self.gifResource.delete()
            self.gifResource = None
        if latex <> "":
            tempFileName = compile(latex, self.fontsize)
            self.gifResource = Resource(self.idevice, tempFileName)
            Path(tempFileName).remove()
        self._latex = latex
    def get_gifURL(self):
        """
        Returns the url to our gif for putting inside
        <img src=""/> tag attributes
        """
        if self.gifResource is None:
            return ''
        else:
            return self.gifResource.path
    def _upgradeFieldToVersion1(self):
        """
        Upgrades to exe v0.19
        """
        self.fontsize = "4"
    latex = property(get_latex, set_latex)
    gifURL = property(get_gifURL)
    instruc = lateTranslate('instruc')
    previewInstruc = lateTranslate('previewInstruc')
class QuizOptionField(Field):
    """
    A Question is built up of question and options.  Each
    option can be rendered as an XHTML element
    Used by the QuizQuestionField, as part of the Multi-Choice iDevice.
    """
    persistenceVersion = 1
    def __init__(self, question, idevice, name="", instruc=""):
        """
        Initialize 
        """
        Field.__init__(self, name, instruc)
        self.isCorrect = False
        self.question  = question
        self.idevice = idevice
        self.answerTextArea = TextAreaField(x_(u'Option'), 
                                  idevice._answerInstruc, u'')
        self.answerTextArea.idevice = idevice
        self.feedbackTextArea = TextAreaField(x_(u'Feedback'), 
                                    idevice._feedbackInstruc, u'')
        self.feedbackTextArea.idevice = idevice
    def getResourcesField(self, this_resource):
        """
        implement the specific resource finding mechanism for this iDevice:
        """
        if hasattr(self, 'answerTextArea')\
        and hasattr(self.answerTextArea, 'images'):
            for this_image in self.answerTextArea.images:
                if hasattr(this_image, '_imageResource') \
                and this_resource == this_image._imageResource:
                    return self.answerTextArea
        if hasattr(self, 'feedbackTextArea')\
        and hasattr(self.feedbackTextArea, 'images'):
            for this_image in self.feedbackTextArea.images:
                if hasattr(this_image, '_imageResource') \
                and this_resource == this_image._imageResource:
                    return self.feedbackTextArea
        return None
    def getRichTextFields(self):
        """
        Like getResourcesField(), a general helper to allow nodes to search 
        through all of their fields without having to know the specifics of each
        iDevice type.  
        """
        fields_list = []
        if hasattr(self, 'answerTextArea'):
            fields_list.append(self.answerTextArea)
        if hasattr(self, 'feedbackTextArea'):
            fields_list.append(self.feedbackTextArea)
        return fields_list
    def upgradeToVersion1(self):
        """
        Upgrades to somewhere before version 0.25 (post-v0.24) 
        to reflect the new TextAreaFields now in use for images.
        """ 
        self.answerTextArea = TextAreaField(x_(u'Option'), 
                                  self.idevice._answerInstruc, self.answer)
        self.answerTextArea.idevice = self.idevice
        self.feedbackTextArea = TextAreaField(x_(u'Feedback'), 
                                    self.idevice._feedbackInstruc, 
                                    self.feedback)
        self.feedbackTextArea.idevice = self.idevice
class QuizQuestionField(Field):
    """
    A Question is built up of question and Options.
    Used as part of the Multi-Choice iDevice.
    """
    persistenceVersion = 1
    def __init__(self, idevice, name, instruc=""):
        """
        Initialize 
        """
        Field.__init__(self, name, instruc)
        self.options              = []
        self.idevice              = idevice
        self.questionTextArea     = TextAreaField(x_(u'Question'), 
                                        idevice._questionInstruc, u'')
        self.questionTextArea.idevice     = idevice
        self.hintTextArea         = TextAreaField(x_(u'Hint'), 
                                        idevice._hintInstruc, u'')
        self.hintTextArea.idevice         = idevice
    def addOption(self):
        """
        Add a new option to this question. 
        """
        option = QuizOptionField(self, self.idevice)
        self.options.append(option)
    def getResourcesField(self, this_resource):
        """
        implement the specific resource finding mechanism for this iDevice:
        """
        if hasattr(self, 'questionTextArea')\
        and hasattr(self.questionTextArea, 'images'):
            for this_image in self.questionTextArea.images:
                if hasattr(this_image, '_imageResource') \
                and this_resource == this_image._imageResource:
                    return self.questionTextArea
        if hasattr(self, 'hintTextArea')\
        and hasattr(self.hintTextArea, 'images'):
            for this_image in self.hintTextArea.images:
                if hasattr(this_image, '_imageResource') \
                and this_resource == this_image._imageResource:
                    return self.hintTextArea
        for this_option in self.options:
            this_field = this_option.getResourcesField(this_resource)
            if this_field is not None:
                return this_field
        return None
    def getRichTextFields(self):
        """
        Like getResourcesField(), a general helper to allow nodes to search 
        through all of their fields without having to know the specifics of each
        iDevice type.  
        """
        fields_list = []
        if hasattr(self, 'questionTextArea'):
            fields_list.append(self.questionTextArea)
        if hasattr(self, 'hintTextArea'):
            fields_list.append(self.hintTextArea)
        for this_option in self.options:
            fields_list.extend(this_option.getRichTextFields())
        return fields_list
    def upgradeToVersion1(self):
        """
        Upgrades to somewhere before version 0.25 (post-v0.24) 
        to reflect the new TextAreaFields now in use for images.
        """ 
        self.questionTextArea     = TextAreaField(x_(u'Question'), 
                                        self.idevice._questionInstruc, 
                                        self.question)
        self.questionTextArea.idevice = self.idevice
        self.hintTextArea         = TextAreaField(x_(u'Hint'), 
                                        self.idevice._hintInstruc, self.hint)
        self.hintTextArea.idevice  = self.idevice
class SelectOptionField(Field):
    """
    A Question is built up of question and options.  Each
    option can be rendered as an XHTML element
    Used by the SelectQuestionField, as part of the Multi-Select iDevice.
    """
    persistenceVersion = 1
    def __init__(self, question, idevice, name="", instruc=""):
        """
        Initialize 
        """
        Field.__init__(self, name, instruc)
        self.isCorrect = False
        self.question  = question
        self.idevice = idevice
        self.answerTextArea    = TextAreaField(x_(u'Options'), 
                                     question._optionInstruc, u'')
        self.answerTextArea.idevice = idevice
    def getResourcesField(self, this_resource):
        """
        implement the specific resource finding mechanism for this iDevice:
        """
        if hasattr(self, 'answerTextArea')\
        and hasattr(self.answerTextArea, 'images'):
            for this_image in self.answerTextArea.images:
                if hasattr(this_image, '_imageResource') \
                and this_resource == this_image._imageResource:
                    return self.answerTextArea
        return None
    def getRichTextFields(self):
        """
        Like getResourcesField(), a general helper to allow nodes to search 
        through all of their fields without having to know the specifics of each
        iDevice type.  
        """
        fields_list = []
        if hasattr(self, 'answerTextArea'):
            fields_list.append(self.answerTextArea)
        return fields_list
    def upgradeToVersion1(self):
        """
        Upgrades to somewhere before version 0.25 (post-v0.24) 
        to reflect the new TextAreaFields now in use for images.
        """ 
        self.answerTextArea    = TextAreaField(x_(u'Options'), 
                                     self.question._optionInstruc, 
                                     self.answer)
        self.answerTextArea.idevice = self.idevice
class SelectQuestionField(Field):
    """
    A Question is built up of question and Options.
    Used as part of the Multi-Select iDevice.
    """
    persistenceVersion = 1
    def __init__(self, idevice, name, instruc=""):
        """
        Initialize 
        """
        Field.__init__(self, name, instruc)
        self.idevice              = idevice
        self._questionInstruc      = x_(u"""Enter the question stem. 
The question should be clear and unambiguous. Avoid negative premises as these 
can tend to confuse learners.""")
        self.questionTextArea = TextAreaField(x_(u'Question:'), 
                                    self.questionInstruc, u'')
        self.questionTextArea.idevice = idevice
        self.options              = []
        self._optionInstruc        = x_(u"""Enter the available choices here. 
You can add options by clicking the "Add another option" button. Delete options by 
clicking the red X next to the option.""")
        self._correctAnswerInstruc = x_(u"""Select as many correct answer 
options as required by clicking the check box beside the option.""")
        self.feedbackInstruc       = x_(u"""Type in the feedback you want 
to provide the learner with.""")
        self.feedbackTextArea = TextAreaField(x_(u'Feedback:'), 
                                    self.feedbackInstruc, u'')
        self.feedbackTextArea.idevice = idevice
    questionInstruc      = lateTranslate('questionInstruc')
    optionInstruc        = lateTranslate('optionInstruc')
    correctAnswerInstruc = lateTranslate('correctAnswerInstruc')
    def addOption(self):
        """
        Add a new option to this question. 
        """
        option = SelectOptionField(self, self.idevice)
        self.options.append(option)
    def getResourcesField(self, this_resource):
        """
        implement the specific resource finding mechanism for this iDevice:
        """
        if hasattr(self, 'questionTextArea')\
        and hasattr(self.questionTextArea, 'images'):
            for this_image in self.questionTextArea.images:
                if hasattr(this_image, '_imageResource') \
                and this_resource == this_image._imageResource:
                    return self.questionTextArea
        if hasattr(self, 'feedbackTextArea')\
        and hasattr(self.feedbackTextArea, 'images'):
            for this_image in self.feedbackTextArea.images:
                if hasattr(this_image, '_imageResource') \
                and this_resource == this_image._imageResource:
                    return self.feedbackTextArea
        for this_option in self.options:
            this_field = this_option.getResourcesField(this_resource)
            if this_field is not None:
                return this_field
        return None
    def getRichTextFields(self):
        """
        Like getResourcesField(), a general helper to allow nodes to search 
        through all of their fields without having to know the specifics of each
        iDevice type.  
        """
        fields_list = []
        if hasattr(self, 'questionTextArea'):
            fields_list.append(self.questionTextArea)
        if hasattr(self, 'feedbackTextArea'):
            fields_list.append(self.feedbackTextArea)
        for this_option in self.options:
            fields_list.extend(this_option.getRichTextFields())
        return fields_list
    def upgradeToVersion1(self):
        """
        Upgrades to somewhere before version 0.25 (post-v0.24) 
        to reflect the new TextAreaFields now in use for images.
        """ 
        self.questionTextArea = TextAreaField(x_(u'Question:'), 
                                    self.questionInstruc, self.question)
        self.questionTextArea.idevice = self.idevice
        self.feedbackTextArea = TextAreaField(x_(u'Feedback:'), 
                                    self.feedbackInstruc, self.feedback)
        self.feedbackTextArea.idevice = self.idevice
class AttachmentField(Field):
    """
    A Generic iDevice is built up of these fields.  Each field can be
    rendered as an XHTML element
    """
    def __init__(self, name, instruc=""):
        """
        """
        Field.__init__(self, name, instruc)
        self.attachResource = None
    def setAttachment(self, attachPath):
        """
        Store the attachment file in the package
        Needs to be in a package to work.
        """
        log.debug(u"setAttachment "+unicode(attachPath))
        resourceFile = Path(attachPath)
        assert(self.idevice.parentNode,
               'Attach '+self.idevice.id+' has no parentNode')
        assert(self.idevice.parentNode.package,
               'iDevice '+self.idevice.parentNode.id+' has no package')
        if resourceFile.isfile():
            if self.attachResource:
                self.attachResource.delete()
            self.attachResource = Resource(self.idevice, resourceFile)
        else:
            log.error('File %s is not a file' % resourceFile)
