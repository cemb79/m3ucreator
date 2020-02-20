package com.seat.sound.song;

import java.io.File;

import org.apache.tika.metadata.Metadata;

import com.seat.sound.mp3.Mp3FileFilter;
import com.seat.sound.mp3.Mp3Util;

public class AlbumOrganizer {

	public static void main(String[] args) {
		int i = 0;
		String directory = null;
		
		if(args == null || args.length == 0) {
			System.out.println("Invalid arguments");
			System.out.println("Usage: AlbumOrganizer [arguments...]");
			System.out.print(" -d <directory>\tLocation of songs files");
			System.exit(1);
		}
		
		while(i < args.length) {
			String argument = args[i];
			if(argument.equals("-d")) {
				directory = args[++i];
			}
			i++;
		}
		
		AlbumOrganizer org = new AlbumOrganizer();
		System.out.println("Searching for songs in " + directory);
		org.organizeAlbumList(directory);
	}

	private void organizeAlbumList(String path) {
		File[] songs = getSongsInDirectory(path);
		int songsRenamed = 0;
		for(File song : songs) {
			if(song.isDirectory()) {
				System.out.println("Entering directory: " + song.getAbsolutePath());
				organizeAlbumList(song.getAbsolutePath());
				continue;
			}
			if(!hasSongTrackNumberInName(song)) {
				String track = getTrackNumber(song);
				boolean isRenamed = renameSong(song, track);
				if(isRenamed) {
					songsRenamed++;
				}
			}
		}
		if(songsRenamed > 0) {
			System.out.println("#### Songs renamed: " + songsRenamed);
		} else {
			System.out.println("No files renamed");
		}
	}

	private File[] getSongsInDirectory(String directory) {
		File dirFile = new File(directory);
		File[] songs = null;
		if(dirFile.isDirectory()) {
			songs = dirFile.listFiles(new Mp3FileFilter());
		}
		return songs;
	}
	
	private String getTrackNumber(File song) {
		Metadata metadata = Mp3Util.getMetadata(song);
		String track = metadata.get("xmpDM:trackNumber");
		int num = parseTrack(track);
		if(num < 10) {
			if(!track.startsWith("0")) {
				track = "0" + num;
			}
		} else {
			track = String.valueOf(num);
		}
		return track;
	}
	
	private int parseTrack(String track) {
		int num = 0;
		try {
			num = Integer.parseInt(track);
		} catch(NumberFormatException e) {
			String[] parts = track.split("/");
			if(parts != null && parts.length > 0) {
				num = Integer.parseInt(parts[0].trim());
			}
		}
		return num;
	}
	
	private boolean hasSongTrackNumberInName(File song) {
		String name = song.getName();
		return name.matches("^\\d{1,2}.*$");
	}
	
	private boolean renameSong(File song, String track) {
		String name = song.getName();
		String renamed = track + " - " + name;
		boolean result = renameFile(song, new File(song.getParent(), renamed));
		if(result) {
			System.out.println(renamed);
		}
		return result;
	}
	
	private boolean renameFile(File origin, File dest) {
		return origin.renameTo(dest);
	}
}
