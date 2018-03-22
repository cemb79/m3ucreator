package com.seat.sound.mp3;

import java.io.File;

public class SongMetadata {

	private String artist;
	private String title;
	private int duration;
	private File song;
	
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public File getSong() {
		return song;
	}
	public void setSong(File song) {
		this.song = song;
	}
}
