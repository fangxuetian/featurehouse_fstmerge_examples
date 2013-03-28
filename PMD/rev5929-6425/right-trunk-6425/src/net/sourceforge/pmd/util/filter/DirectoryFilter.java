package net.sourceforge.pmd.util.filter;

import java.io.File;


public final class DirectoryFilter implements Filter<File> {
	public static final DirectoryFilter INSTANCE = new DirectoryFilter();

	private DirectoryFilter() {
	}

	public boolean filter(File file) {
		return file.isDirectory();
	}
}
