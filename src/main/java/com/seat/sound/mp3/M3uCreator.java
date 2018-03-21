package com.seat.sound.mp3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class M3uCreator {

	public static void main(String[] args) {
		M3uCreator creator = new M3uCreator();
		String[] generes = {"Soundtrack"};
		File directoryOrg = new File("/Users/cmercado/Music/iTunes/iTunes Media/Music");
		List<File> songList = creator.getSongsByGenere(directoryOrg, generes);
		System.out.println("Song list obtained: " + songList.size());
		creator.createM3uList(songList, "soundtrack");
	}
	
	public List<File> getSongsByGenere(File directoryOrg, String[] generes) {
		File[] files = directoryOrg.listFiles(new Mp3FileFilter());
		ArrayList<File> songList = new ArrayList<>();
		
		for(File file : files) {
			if(file.isDirectory()) {
				songList.addAll(getSongsByGenere(file, generes));
			} else {
				if(isSongInGenere(file, generes)) {
					songList.add(file);
				}
			}
		}
		
		return songList;
	}

	private boolean isSongInGenere(File file, String[] genres) {
		boolean result = false;
		Metadata metadata = getMetadata(file);
		String songGenre = metadata.get("xmpDM:genre");
		for(String genre : genres) {
			if(genre.equalsIgnoreCase(songGenre)) {
				result = true;
				break;
			}
		}
		return result;
	}

	private Metadata getMetadata(File file) {
		InputStream input = null;
		Metadata metadata = null;
		try {
			input = new FileInputStream(file);
			ContentHandler handler = new DefaultHandler();
			metadata = new Metadata();
			Parser parser = new Mp3Parser();
			ParseContext parseCtx = new ParseContext();
			parser.parse(input, handler, metadata, parseCtx);
		} catch (IOException | SAXException | TikaException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return metadata;
	}

	public void createM3uList(List<File> songList, String listName) {
		String m3uFile = listName + ".m3u";
		System.out.println("Creating m3u file: " + m3uFile);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(m3uFile), "utf-8"))) {
			writer.write("#EXTM3U\n");
			for(File song : songList) {
				writer.write(song.getAbsolutePath());
				writer.write("\n");
			}
			System.out.println("File list created");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
