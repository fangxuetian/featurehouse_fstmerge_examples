

package net.sf.freecol.common.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import net.sf.freecol.FreeCol;
import net.sf.freecol.common.io.FreeColModFile.ModInfo;


public class Mods {

    
    public static List<FreeColModFile> getMods() {
        final File[] fmods = FreeCol.getModsDirectory().listFiles(new FileFilter() {
            public boolean accept(File f) {
                final String name = f.getName();
                if (f.isDirectory()) {
                    return true;
                }
                for (String ending : FreeColModFile.FILE_ENDINGS) {
                    if (name.endsWith(ending)) {
                        return true;
                    }
                }
                return false;
            }
        });
        final List<FreeColModFile> list = new ArrayList<FreeColModFile>(fmods.length);
        for (File f : fmods) {
            list.add(new FreeColModFile(f.getName()));
        }
        return list;
    }
    
    
    public static List<ModInfo> getModInfos() {
        final List<FreeColModFile> mods = getMods();
        final List<ModInfo> modInfos = new ArrayList<ModInfo>(mods.size());
        for (FreeColModFile mod : mods) {
            modInfos.add(mod.getModInfo());
        }
        return modInfos;
    }
}
