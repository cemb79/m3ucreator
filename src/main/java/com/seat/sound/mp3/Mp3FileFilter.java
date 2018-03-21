package com.seat.sound.mp3;

import java.io.File;
import java.io.FileFilter;

public class Mp3FileFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		boolean result = false;
		if(file.isDirectory()) {
			result = true;
		} else if(file.getName().endsWith("mp3")) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
}
