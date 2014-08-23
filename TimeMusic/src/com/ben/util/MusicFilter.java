package com.ben.util;

import java.io.File;
import java.io.FilenameFilter;

public class MusicFilter implements FilenameFilter {

	public boolean accept(File dir, String filename) {
		filename=filename.toLowerCase();
		return filename.endsWith(".mp3")||filename.endsWith(".ogg")||filename.endsWith(".m4a");
	}

}
