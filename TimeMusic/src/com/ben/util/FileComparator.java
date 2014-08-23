package com.ben.util;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {

	public int compare(File arg0, File arg1) {
		String name0=arg0.getName();
		String name1=arg1.getName();
		return name0.compareTo(name1);
	}
}
