from coursemanager import CourseManager
from xmlreader import readConfig
from os.path import exists
from  shutil import rmtree, copyfile
from os import system
debug = 0
class Course(CourseManager):
	def showallcourse( self ):
		"""Read the courses.xml and display their link to the content resource homepage"""
		print "<p><a href=start.pyg?cmd=showaddcourse>Add a new content resource</a><br>\n"
		if exists( self.coursexmlfile):
			doc = readConfig( self.coursexmlfile )
			try:		
				if self.preview_dir=="":
					for node in doc["courses"]:
						print """<br><a href="start.pyg?cmd=view_course&courseidentifier=%s">%s</a>\
						<a href="start.pyg?cmd=edit_course&courseidentifier=%s">edit</a>&nbsp;\
						<!--a href="start.pyg?cmd=delete_course&courseidentifier=%s">delete</a-->&nbsp;\
						<br>\n""" % (  node["courseidentifier"] ,  node["title"],\
									 node["courseidentifier"],\
									 node["courseidentifier"]  )
				else:
					for node in doc["courses"]:
						print '<br><a href="start.pyg?cmd=view_course&courseidentifier=%s%s">%s Preview</a><br>\n'\
							 % (  node["courseidentifier"] , self.preview_cgi_sting, node["title"]  )
			except:
				pass
		else:
			print "No content resource right now!"
	def outline_course( self, form ):
		try:
			course_identifier = form["courseidentifier"].value
			self.get_course_detail( course_identifier )
		except:
			print "Can't get the resource detail \n"
			return
			
		content = self.show_course_topics( ret_str=1, for_topic_tree=1 )
		heading = self.dict["title"]
		crumb = ""
		preview = self.previewcgi + "?courseidentifier=%s" % self.dict["courseidentifier"]
		outline = self.startcgi + "?cmd=outline_course&courseidentifier=%s" % self.dict["courseidentifier"]
		self.showexe( self.theme_template, heading, content, crumb, preview, outline )
		
	def showaddcourse( self ):
		"""Displays the content resource posting form."""
		
		heading = "Add a new content recourse"
		
		content = self.xml_string( self.courseForm, self.dict )
		crumb = ""
		
		self.showexe( self.theme_template, heading, content, crumb )
		
	def course_read_form( self, form ):
		for item in form:
			if item[-7:]<>"graphic" and item[-5:]<>"_file":
				try:
					self.dict[item]= form[item].value
				except:
					pass
	
	def save_new_course( self, form ):
		"""Accept actual posted form data, creates identifier and update the courses.xml"""
		self.course_read_form(form)
		
		maxidentifier = self.max_identifier( self.coursexmlfile, "courses", "courseidentifier" ) + 1
				
		tmpidentifier = self.doc_root  + str( maxidentifier )
		while exists( tmpidentifier ):
			maxidentifier = maxidentifier + 1
			tmpidentifier = self.doc_root  + str( maxidentifier )
			
		self.dict["courseidentifier"] = str( maxidentifier )
		target_dir = self.doc_root  + self.dict["courseidentifier"] + "/images/"
		file_dir   = self.doc_root  + self.dict["courseidentifier"] + "/files/"
		if self.create_dir( tmpidentifier ):
			self.dict["graphic"] = self.process_graphic( form, target_dir, "graphic", "graphic" )
			from string import strip
			if self.dict["graphic"].strip<>"course.gif":
				cp_cmd =  "/usr/bin/convert -pointsize 24 -font Candice label:'%s'  -append -geometry 700x90  -mattecolor blue -frame 10x10+0+10 %scourse.gif" %( self.dict["title"], target_dir )
				if debug: print "course:system(%s) <br>\n" %cp_cmd
				system( cp_cmd )
			for item in self.dict:
				if item[-5:]=="_file":
					self.dict[item] = self.process_file( form, file_dir, item, item )
			
			
			if not self.dict.has_key( "parse_h3" ):
				self.dict["parse_h3"] = ""
			
			self.save_course_file( "add"  )
		self.showallcourse()
	
	def save_course_file( self, action ):
		
		if self.dict["parse_h3"]=="yes":
			self.savexmlfile( action, "courses", self.dict, "courseidentifier", self.coursexmlfile, self.course_xml_template, parse_h3=1 )
		else:
			self.savexmlfile( action, "courses", self.dict, "courseidentifier", self.coursexmlfile, self.course_xml_template )
		
	def update_course( self, form ):
		self.course_read_form( form )
		
		target_dir = self.doc_root  + self.dict["courseidentifier"] + "/images/"
		if form.has_key("new_graphic"):
			self.dict["graphic"] = self.process_graphic( form, target_dir, "graphic", "new_graphic" )
		else:
			self.dict["graphic"] = self.process_graphic( form, target_dir, "graphic", "graphic" )	
		if not self.dict.has_key( "parse_h3" ):
			self.dict["parse_h3"] = ""
		
		if self.dict["graphic"].strip<>"course.gif":
			target_dir = self.doc_root  + self.dict["courseidentifier"] + "/images/"
			cp_cmd =  "/usr/bin/convert -pointsize 24 -font Candice label:'%s'  -append -geometry 700x90  -mattecolor blue -frame 10x10+0+10 %scourse.gif" %( self.dict["title"], target_dir )
			if debug: print "course:system(%s) <br>\n" %cp_cmd
			system( cp_cmd )
		self.save_course_file("update")
		self.view_course( form )
				
	def edit_course( self, form ):
		course_identifier = form["courseidentifier"].value
		self.get_course_detail( course_identifier )
		heading = ""
		content = self.xml_string( self.courseForm, self.dict,1 )
		crumb = "<p><H3><a href=%s?cmd=view_course&courseidentifier=%s>%s</a> -> Edit</H3><p>\n" % ( self.startcgi, self.dict["courseidentifier"], self.dict["title"])
		preview = self.previewcgi + "?courseidentifier=%s" % self.dict["courseidentifier"]
		outline = self.startcgi + "?cmd=outline_course&courseidentifier=%s" % self.dict["courseidentifier"]
		self.showexe( self.theme_template, heading, content, crumb, preview, outline )
		
	def delete_course( self, form ):
		"""delete the content resource info from courses.xml and delete the resource identifier
		"""
		course_identifier = form["courseidentifier"].value
		self.get_course_detail( course_identifier )
		
		self.save_course_file( "delete" )
		
		if course_identifier:
			rm_identifier = self.doc_root + course_identifier
			rm_identifier_dest = self.doc_root +'.' + course_identifier
			rmtree( rm_identifier )
		self.showallcourse()
		
	def up_course( self, form ):
		"""move up the content resource info from courses.xml
		"""
		course_identifier = form["courseidentifier"].value
		self.dict["courseidentifier"] = course_identifier
		self.save_course_file( "up")
		self.showallcourse()
		
	def down_course( self, form ):
		"""mode down the content resource infor from coruses.xml
		"""
		course_identifier = form["courseidentifier"].value
		self.dict["courseidentifier"] = course_identifier
		self.save_course_file( "down")
		self.showallcourse()
