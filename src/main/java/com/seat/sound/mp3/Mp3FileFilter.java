package com.seat.sound.mp3;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class Mp3FileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith("mp3");
	}

}
