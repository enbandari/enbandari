package com.ben.util;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFilter implements FileFilter {

	public boolean accept(File arg0) {

		return arg0.isDirectory()&&!arg0.isHidden();
	}

}
