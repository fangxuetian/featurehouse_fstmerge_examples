
package net.sourceforge.squirrel_sql.client.update.gui.installer;

import java.io.FileNotFoundException;

import net.sourceforge.squirrel_sql.client.update.gui.installer.event.InstallStatusListener;
import net.sourceforge.squirrel_sql.client.update.xmlbeans.ChangeListXmlBean;
import net.sourceforge.squirrel_sql.fw.util.FileWrapper;

public interface ArtifactInstallerFactory {

	ArtifactInstaller create(ChangeListXmlBean changeList, InstallStatusListener listener) throws FileNotFoundException;
	
	ArtifactInstaller create(FileWrapper changeList, InstallStatusListener listener) throws FileNotFoundException;
}
