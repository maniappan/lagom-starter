package org.weather.api;

import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import com.lightbend.lagom.javadsl.api.deser.MessageSerializer;

import akka.util.ByteString;
import play.Logger;

public class GzDeserializer implements MessageSerializer.NegotiatedDeserializer<ByteString, ByteString> {
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public ByteString deserialize(ByteString bytes) throws DeserializationException {
		try {

			Logger.info("Started Downloading");
			


		/*	final Path file = (Path) Paths.get("target/weather.gz");

			
			Logger.info("Downloading PATH= {}",file.toString());


			// Files.write(Paths.get("C:\\Users\\sundaramoorthim\\Desktop\\junkfiles.gz"),
			// bytes.iterator().getByte());
			// bytes.copyToBuffer(dest);
			ByteChannel byteChannel = Files.newByteChannel(file, StandardOpenOption.CREATE_NEW,
					StandardOpenOption.WRITE);
			byteChannel.write(bytes.toByteBuffer());
			byteChannel.close();
*/			Logger.info("Completed Downloading");

			return bytes;
			// return mapper.readValue(bytes.iterator().asInputStream(), String.class);
		} catch (Exception e) {
			throw new DeserializationException(e);
		}
	}
}
