

package net.sf.jabref;


import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

public abstract class MnemonicAwareAction extends AbstractAction {

    public MnemonicAwareAction() {
	
    }

    public MnemonicAwareAction(ImageIcon icon) {
	
        
        putValue(SMALL_ICON, icon);
    }

    public void putValue(String key, Object value) {
	if (key.equals(Action.NAME)) {
	    String name = Globals.menuTitle(value.toString());
	    int i = name.indexOf('&');
	    if (i >= 0) {
		char mnemonic = Character.toUpperCase(name.charAt(i+1));
		putValue(Action.MNEMONIC_KEY, new Integer((int) mnemonic));
		value = name.substring(0, i) + name.substring(i+1);
	    } else
                value = name;
	}
	super.putValue(key, value);
    }
}
