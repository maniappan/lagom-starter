package org.example.stream.weather;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnzipGZutil {
	private static final Logger LOGGER = LoggerFactory.getLogger(UnzipGZutil.class);

	private static final String INPUT_GZIP_FILE = "C:\\Users\\sundaramoorthim\\Desktop\\junk\\test.gz";
	private static final String OUTPUT_FILE = "C:\\Users\\sundaramoorthim\\Desktop\\\\junk\\test.txt";

	public static void main(String[] args) {
		UnzipGZutil.gunzipIt(INPUT_GZIP_FILE, OUTPUT_FILE);
	}

	/**
	 * GunZip it
	 */
	public static String gunzipIt(String inputFile, String outputFile) {

		LOGGER.info("inputFile :: {}", inputFile);
		LOGGER.info("outputFile :: {}", outputFile);

		LOGGER.info("UNZIP started ");

		try {

			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputFile));
			FileOutputStream out = new FileOutputStream(outputFile);

			ReadableByteChannel rbc = Channels.newChannel(gzis);
			WritableByteChannel wbc = Channels.newChannel(out);
			ByteBuffer buffer = ByteBuffer.allocate(65536);

			while (rbc.read(buffer) != -1) {
				buffer.flip();
				wbc.write(buffer);
				buffer.clear();
			}

			rbc.close();
			wbc.close();

			gzis.close();
			out.close();

			LOGGER.info("UNZIP done ");

			File file = new File(inputFile);

			if (file.delete()) {
				LOGGER.info(file.getName() + " is deleted!");
			} else {
				LOGGER.info("Delete operation is failed.");
			}

		} catch (IOException e) {

			LOGGER.error("Exception : " + ExceptionUtils.getStackTrace(e));
		}
		return "UNZIP completed";
	}
}