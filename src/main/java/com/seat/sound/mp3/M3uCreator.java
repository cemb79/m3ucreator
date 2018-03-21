package com.seat.sound.mp3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.apache.tika.parser.mp3.Mp3Parser;

public class M3uCreator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public File[] getSongsByGenere(File directoryOrg, String[] generes) {
		FilenameFilter filter = new Mp3FileFilter();
		File[] files = directoryOrg.listFiles(filter);
		
		for(File file : files) {
			if(file.isDirectory()) {
				//TODO add recursion
			} else {
				isSongInGenere(file, generes);
			}
		}
		
		return null;
	}

	private boolean isSongInGenere(File file, String[] genres) {
		boolean result = false;
		Metadata metadata = getMetadata(file);
		String songGenre = metadata.get("genre");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return metadata;
	}

}
