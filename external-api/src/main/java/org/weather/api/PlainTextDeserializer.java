package org.weather.api;

import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import com.lightbend.lagom.javadsl.api.deser.MessageSerializer;

import akka.util.ByteString;

public class PlainTextDeserializer implements MessageSerializer.NegotiatedDeserializer<ByteString, ByteString> {
	  private final String charset;

	  public PlainTextDeserializer(String charset) {
	    this.charset = charset;
	  }

	  @Override
	  public ByteString deserialize(ByteString bytes) throws DeserializationException {
	    return bytes;
	  }
	}