package net.sf.jabref.imports;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.sf.jabref.BasePanel;
import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.GUIGlobals;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefFrame;
import net.sf.jabref.MnemonicAwareAction;
import net.sf.jabref.Util;
import net.sf.jabref.external.FileLinksUpgradeWarning;



public class OpenDatabaseAction extends MnemonicAwareAction {

    boolean showDialog;
    private JabRefFrame frame;

    
    
    private static ArrayList<PostOpenAction> postOpenActions =
            new ArrayList<PostOpenAction>();

    static {
        
        
        postOpenActions.add(new CheckForNewEntryTypesAction());
        
        postOpenActions.add(new FileLinksUpgradeWarning());
    }

    public OpenDatabaseAction(JabRefFrame frame, boolean showDialog) {
        super(GUIGlobals.getImage("open"));
        this.frame = frame;
        this.showDialog = showDialog;
        putValue(NAME, "Open database");
        putValue(ACCELERATOR_KEY, Globals.prefs.getKey("Open database"));
        putValue(SHORT_DESCRIPTION, Globals.lang("Open BibTeX database"));
    }

    public void actionPerformed(ActionEvent e) {
        List<File> filesToOpen = new ArrayList<File>();
        

        if (showDialog) {

            String[] chosen = Globals.getMultipleFiles(frame, new File(Globals.prefs.get("workingDirectory")), ".bib",
                    true);
            if (chosen != null) for (int i=0; i<chosen.length; i++) {
                if (chosen[i] != null)
                    filesToOpen.add(new File(chosen[i]));
            }

            
        } else {
            Util.pr(NAME);
            Util.pr(e.getActionCommand());
            filesToOpen.add(new File(Util.checkName(e.getActionCommand())));
        }

        BasePanel toRaise = null;
        int initialCount = filesToOpen.size(), removed = 0;
        
        
        for (Iterator<File> iterator = filesToOpen.iterator(); iterator.hasNext();) {
            File file = iterator.next();
            for (int i=0; i<frame.getTabbedPane().getTabCount(); i++) {
                BasePanel bp = frame.baseAt(i);
                if ((bp.getFile() != null) && bp.getFile().equals(file)) {
                    iterator.remove();
                    removed++;
                    
                    
                    if (removed == initialCount) {
                        toRaise = bp;
                    }
                    break;
                }
            }
        }


        
        
        if (filesToOpen.size() > 0) {
            final List<File> theFiles = Collections.unmodifiableList(filesToOpen);
            (new Thread() {
                public void run() {
                    for (Iterator<File> i=theFiles.iterator(); i.hasNext();)
                        openIt(i.next(), true);

                }
            }).start();
            for (Iterator<File> i=theFiles.iterator(); i.hasNext();)
                frame.getFileHistory().newFile(i.next().getPath());
        }
        
        
        else if (toRaise != null) {
            frame.output(Globals.lang("File '%0' is already open.", toRaise.getFile().getPath()));
            frame.getTabbedPane().setSelectedComponent(toRaise);
        }
    }

    class OpenItSwingHelper implements Runnable {
        BasePanel bp;
        boolean raisePanel;
        File file;

        OpenItSwingHelper(BasePanel bp, File file, boolean raisePanel) {
            this.bp = bp;
            this.raisePanel = raisePanel;
            this.file = file;
        }

        public void run() {
            frame.addTab(bp, file, raisePanel);

        }
    }

    public void openIt(File file, boolean raisePanel) {
        if ((file != null) && (file.exists())) {
            frame.output(Globals.lang("Opening") + ": '" + file.getPath() + "'");
            try {
                String fileName = file.getPath();
                Globals.prefs.put("workingDirectory", file.getPath());
                
                String encoding = Globals.prefs.get("defaultEncoding");
                final ParserResult pr = loadDatabase(file, encoding);

                if ((pr == null) || (pr == ParserResult.INVALID_FORMAT)) {
                    JOptionPane.showMessageDialog(null, Globals.lang("Error opening file" + " '" + fileName + "'"),
                            Globals.lang("Error"),
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                final BasePanel panel = addNewDatabase(pr, file, raisePanel);
                
                
                
                
                
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        performPostOpenActions(panel, pr, true);
                    }
                });


            } catch (Exception ex) {
                
                Util.showQuickErrorDialog(frame, Globals.lang("Open database"), ex);
            }
        }
    }

    
    public static void performPostOpenActions(BasePanel panel, ParserResult pr,
                                              boolean mustRaisePanel) {
        for (Iterator<PostOpenAction> iterator = postOpenActions.iterator(); iterator.hasNext();) {
            PostOpenAction action = iterator.next();
            if (action.isActionNecessary(pr)) {
                if (mustRaisePanel)
                    panel.frame().getTabbedPane().setSelectedComponent(panel);
                action.performAction(panel, pr);
            }
        }
    }

    public BasePanel addNewDatabase(ParserResult pr, File file,
                               boolean raisePanel) {

        String fileName = file.getPath();
        BibtexDatabase db = pr.getDatabase();
        HashMap<String, String> meta = pr.getMetaData();

        if (pr.hasWarnings()) {
            final String[] wrns = pr.warnings();
            (new Thread() {
                public void run() {
                    StringBuffer wrn = new StringBuffer();
                    for (int i = 0; i < wrns.length; i++)
                        wrn.append(i + 1).append(". ").append(wrns[i]).append("\n");

                    if (wrn.length() > 0)
                        wrn.deleteCharAt(wrn.length() - 1);
                    
                    
                    
                    
                    
                    JOptionPane.showMessageDialog(frame, wrn.toString(),
                            Globals.lang("Warnings"),
                            JOptionPane.WARNING_MESSAGE);
                }
            }).start();
        }
        BasePanel bp = new BasePanel(frame, db, file, meta, pr.getEncoding());

        
        SwingUtilities.invokeLater(new OpenItSwingHelper(bp, file, raisePanel));

        frame.output(Globals.lang("Opened database") + " '" + fileName +
                "' " + Globals.lang("with") + " " +
                db.getEntryCount() + " " + Globals.lang("entries") + ".");

        return bp;
    }

    public static ParserResult loadDatabase(File fileToOpen, String encoding)
            throws IOException {

        
        Reader reader;
        
        

        
        

        
        
        
        
        
        Reader utf8Reader = ImportFormatReader.getReader(fileToOpen, "UTF8");
        String suppliedEncoding = checkForEncoding(utf8Reader);
        utf8Reader.close();
        
        if (suppliedEncoding == null) {
            Reader utf16Reader = ImportFormatReader.getReader(fileToOpen, "UTF-16");
            suppliedEncoding = checkForEncoding(utf16Reader);
            utf16Reader.close();
            
        }

        

        if ((suppliedEncoding != null)) {
           try {
               reader = ImportFormatReader.getReader(fileToOpen, suppliedEncoding);
               encoding = suppliedEncoding; 
           } catch (Exception ex) {
               ex.printStackTrace();
               reader = ImportFormatReader.getReader(fileToOpen, encoding); 
           }
        } else {
            
            reader = ImportFormatReader.getReader(fileToOpen, encoding);
        }

        BibtexParser bp = new BibtexParser(reader);

        ParserResult pr = bp.parse();
        pr.setEncoding(encoding);

        return pr;
    }

    private static String checkForEncoding(Reader reader) {
        String suppliedEncoding = null;
        StringBuffer headerText = new StringBuffer();
        try {
            boolean keepon = true;
            int piv = 0, offset = 0;
            int c;

            while (keepon) {
                c = reader.read();
                if ((piv == 0) && ((c == '%') || (Character.isWhitespace((char)c))))
                    offset++;
                else {
                    headerText.append((char) c);
                    if (c == GUIGlobals.SIGNATURE.charAt(piv))
                        piv++;
                    else 
                        keepon = false;
                }
                
                found:
                if (piv == GUIGlobals.SIGNATURE.length()) {
                    keepon = false;

                    
                    
                    
                    
                    while (reader.read() != '\n'){
                        
                    }
                    
                    while (((c =reader.read()) == '%') || (Character.isWhitespace((char)c))){
                        
                    }
                    
                    
                    if ((char)c != GUIGlobals.encPrefix.charAt(0))
                        break found;

                    for (int i = 1; i < GUIGlobals.encPrefix.length(); i++) {
                        if (reader.read() != GUIGlobals.encPrefix.charAt(i))
                            break found; 
                        
                        
                        
                        
                        
                    }
                    
                    
                    
                    
                    StringBuffer sb = new StringBuffer();

                    while ((c = reader.read()) != '\n') {
                        sb.append((char) c);
                    }


                    suppliedEncoding = sb.toString();
                }
            }
        } catch (IOException ex) {
        }
        return suppliedEncoding != null ? suppliedEncoding.trim() : null;
    }
}
