
package genj.edit.actions;

import java.awt.event.ActionEvent;

import spin.Spin;

import genj.edit.Images;
import genj.gedcom.Entity;
import genj.gedcom.Gedcom;
import genj.gedcom.GedcomMetaListener;
import genj.gedcom.Property;
import genj.util.swing.Action2;

  
public class Redo extends Action2 implements GedcomMetaListener {
  
  
  private Gedcom gedcom;
  
  
  public Redo() {
    this(null, false);
  }
  
  
  public Redo(Gedcom gedcom) {
    this(gedcom, gedcom.canRedo());
  }
  
  
  public Redo(Gedcom gedcom, boolean enabled) {
    
    
    setImage(Images.imgRedo);
    setText(AbstractChange.resources.getString("redo"));    
    setTip(getText());
    setEnabled(enabled);
    
    
    this.gedcom = gedcom;
    
  }
  
  
  public void follow(Gedcom newGedcom) {
    
    if (gedcom==newGedcom)
      return;
    
    if (gedcom!=null)
      gedcom.removeGedcomListener((GedcomMetaListener)Spin.over(this));
    
    gedcom = newGedcom;
    
    if (gedcom!=null)
      gedcom.addGedcomListener((GedcomMetaListener)Spin.over(this));
    
    
    setEnabled(gedcom!=null && gedcom.canRedo());
  }
  
  
  public void actionPerformed(ActionEvent event) {
    if (gedcom!=null&&gedcom.canRedo())
      gedcom.redoUnitOfWork();
  }
  
  public void gedcomEntityAdded(Gedcom gedcom, Entity entity) {
    
  }

  public void gedcomEntityDeleted(Gedcom gedcom, Entity entity) {
    
  }

  public void gedcomPropertyAdded(Gedcom gedcom, Property property, int pos, Property added) {
    
  }

  public void gedcomPropertyChanged(Gedcom gedcom, Property prop) {
    
  }

  public void gedcomPropertyDeleted(Gedcom gedcom, Property property, int pos, Property removed) {
    
  }

  public void gedcomHeaderChanged(Gedcom gedcom) {
    
  }

  public void gedcomBeforeUnitOfWork(Gedcom gedcom) {
    
  }
  
  public void gedcomAfterUnitOfWork(Gedcom gedcom) {
    
  }

  public void gedcomWriteLockAcquired(Gedcom gedcom) {
    
  }

  public void gedcomWriteLockReleased(Gedcom gedcom) {
    if (this.gedcom==gedcom)
      setEnabled(gedcom.canRedo());
  }
  
} 

