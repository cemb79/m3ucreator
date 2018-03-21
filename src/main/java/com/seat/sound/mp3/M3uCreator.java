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
import java.util.logging.Logger;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class M3uCreator {
	
	private static final Logger logger = Logger.getLogger(M3uCreator.class.getName());

	public static void main(String[] args) {
		M3uCreator creator = new M3uCreator();
		String dirOrigin = "";
		String[] genres = null;
		String listName = "";
		int i = 0;
		
		while(i < args.length) {
			String argument = args[i];
			if(argument.equals("-o")) {
				dirOrigin = args[++i];
			} else if(argument.equals("-g")) {
				String gen = args[++i];
				genres = gen.split(",");
			} else if(argument.equals("-l")) {
				listName = args[++i];
			}
			i++;
		}
		
		creator.init(dirOrigin, genres, listName);
	}
	
	public void init(String dirOrigin, String[] genres, String listName) {
		logger.info("Searching songs for genres");
		for(String genre : genres) {
			logger.info(genre);
		}
		File directoryOrg = new File(dirOrigin);
		List<File> songList = getSongsByGenere(directoryOrg, genres);
		logger.info(() -> "Song list obtained: " + songList.size());
		createM3uList(songList, listName);
	}
	
	public List<File> getSongsByGenere(File directoryOrg, String[] generes) {
		File[] files = directoryOrg.listFiles(new Mp3FileFilter());
		ArrayList<File> songList = new ArrayList<>();
		
		if(files != null) {
			for(File file : files) {
				if(file.isDirectory()) {
					songList.addAll(getSongsByGenere(file, generes));
				} else {
					if(isSongInGenere(file, generes)) {
						songList.add(file);
					}
				}
			}
		} else {
			logger.info("No songs found");
		}
		
		return songList;
	}

	private boolean isSongInGenere(File file, String[] genres) {
		boolean result = false;
		Metadata metadata = getMetadata(file);
		String songGenre = metadata.get("xmpDM:genre");
		if(songGenre != null) {
			for(String genre : genres) {
				if(songGenre.toLowerCase().contains(genre.toLowerCase())) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	private Metadata getMetadata(File file) {
		Metadata metadata = new Metadata();
		try (InputStream input = new FileInputStream(file)){
			ContentHandler handler = new DefaultHandler();
			Parser parser = new Mp3Parser();
			ParseContext parseCtx = new ParseContext();
			parser.parse(input, handler, metadata, parseCtx);
		} catch (IOException | SAXException | TikaException e) {
			logger.warning(e.getMessage());
		} 
		return metadata;
	}

	public void createM3uList(List<File> songList, String listName) {
		String m3uFile = listName + ".m3u";
		if(songList.isEmpty()) {
			logger.info("No songs to export");
		} else {
			logger.info(() -> "Creating m3u file: " + m3uFile);
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(m3uFile), "utf-8"))) {
				writer.write("#EXTM3U\n");
				for(File song : songList) {
					writer.write(song.getAbsolutePath());
					writer.write("\n");
				}
				logger.info("File list created");
			} catch(IOException e) {
				logger.warning(e.getMessage());
			}
		}
	}
}
