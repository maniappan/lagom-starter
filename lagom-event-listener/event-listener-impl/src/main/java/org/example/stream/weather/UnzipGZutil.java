package org.example.stream.weather;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnzipGZutil {
	private static final Logger LOGGER = LoggerFactory.getLogger(UnzipGZutil.class);

	private static final String INPUT_GZIP_FILE = "C:\\Users\\sundaramoorthim\\Desktop\\junk\\test.gz";
	private static final String OUTPUT_FILE = "C:\\\\Users\\\\sundaramoorthim\\\\Desktop\\\\junk\\\\test.txt";

	public static void main(String[] args) {
		UnzipGZutil.gunzipIt(INPUT_GZIP_FILE, OUTPUT_FILE);
	}

	/**
	 * GunZip it
	 */
	public static String gunzipIt(String inputFile, String outputFile) {

		LOGGER.info("inputFile :: {}", inputFile);
		LOGGER.info("outputFile :: {}", outputFile);

		byte[] buffer = new byte[1024];
		LOGGER.info("UNZIP started ");

		try {

			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(inputFile));

			FileOutputStream out = new FileOutputStream(outputFile);

			int len;
			while ((len = gzis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}

			gzis.close();
			out.close();

			LOGGER.info("UNZIP done ");

			File file = new File(inputFile);

			if (file.delete()) {
				LOGGER.info(file.getName() + " is deleted!");
			} else {
				LOGGER.info("Delete operation is failed.");
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return "UNZIP completed";
	}
}