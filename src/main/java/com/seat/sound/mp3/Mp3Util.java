package com.seat.sound.mp3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class Mp3Util {
	
	private static final Logger logger = Logger.getLogger(Mp3Util.class.getName());

	public static Metadata getMetadata(File file) {
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
}
