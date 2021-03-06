package net.sf.jabref.external;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import net.sf.jabref.BasePanel;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.GUIGlobals;
import net.sf.jabref.Globals;


public class PushToVim implements PushToApplication {

    private boolean couldNotConnect=false, couldNotRunClient=false;

    public String getName() {
        return Globals.menuTitle("Insert selected citations into Vim") ;
    }

    public String getApplicationName() {
        return "Vim";
    }

    public String getTooltip() {
        return Globals.lang("Push selection to Vim");
    }

    public Icon getIcon() {
        return GUIGlobals.getImage("vim");
    }

    public String getKeyStrokeName() {
        return null;
    }

    public void pushEntries(BibtexEntry[] entries, String keys) {

        couldNotConnect=false;
        couldNotRunClient=false;
        try {
                String[] com = new String[] {Globals.prefs.get("vim"), "--servername", Globals.prefs.get("vimServer"), "--remote-send",
                "<C-\\><C-N>a\\" + Globals.prefs.get("citeCommand") +
                       "{" + keys + "}"};

            final Process p = Runtime.getRuntime().exec(com);

            Runnable errorListener = new Runnable() {
                public void run() {
                    InputStream out = p.getErrorStream();
                    int c;
                    StringBuffer sb = new StringBuffer();
                    try {
                        while ((c = out.read()) != -1)
                            sb.append((char) c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    if (sb.toString().trim().length() > 0) {
			System.out.println(sb.toString());
                        couldNotConnect = true;
                        return;
                    }
                }
            };
            Thread t = new Thread(errorListener);
            t.start();
            t.join();
        }
        catch (IOException excep) {
            couldNotRunClient = true;
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void operationCompleted(BasePanel panel) {
        if (couldNotConnect)
            JOptionPane.showMessageDialog(
                panel.frame(),
                "<HTML>"+
                Globals.lang("Could not connect to Vim server. Make sure that "
                +"Vim is running<BR>with correct server name."
                +"</HTML>"),
                Globals.lang("Error"), JOptionPane.ERROR_MESSAGE);
        else if (couldNotRunClient)
            JOptionPane.showMessageDialog(
                panel.frame(),
                Globals.lang("Could not run the 'vim' program."),
                Globals.lang("Error"), JOptionPane.ERROR_MESSAGE);
        else {
            panel.output(Globals.lang("Pushed citations to Vim"));
        }
    }

    public boolean requiresBibtexKeys() {
        return true;
    }
}
