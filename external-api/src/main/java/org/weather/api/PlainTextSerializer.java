package org.weather.api;

import java.util.Optional;

import com.lightbend.lagom.javadsl.api.deser.MessageSerializer;
import com.lightbend.lagom.javadsl.api.deser.SerializationException;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;

import akka.util.ByteString;

public class PlainTextSerializer implements MessageSerializer.NegotiatedSerializer<String, ByteString> {
	  private final String charset;

	  public PlainTextSerializer(String charset) {
	    this.charset = charset;
	  }

	  @Override
	  public MessageProtocol protocol() {
	    return new MessageProtocol(Optional.of("text/plain"), Optional.of(charset), Optional.empty());
	  }

	  @Override
	  public ByteString serialize(String s) throws SerializationException {
	    return ByteString.fromString(s, charset);
	  }
	}