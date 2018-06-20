package org.weather.api;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightbend.lagom.javadsl.api.deser.MessageSerializer;
import com.lightbend.lagom.javadsl.api.deser.SerializationException;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;

import akka.util.ByteString;

public class JsonTextSerializer implements MessageSerializer.NegotiatedSerializer<String, ByteString> {
	  private final ObjectMapper mapper = new ObjectMapper();

	  @Override
	  public MessageProtocol protocol() {
	    return new MessageProtocol(Optional.of("application/json"), Optional.empty(), Optional.empty());
	  }

	  @Override
	  public ByteString serialize(String s) throws SerializationException {
	    try {
	      return ByteString.fromArray(mapper.writeValueAsBytes(s));
	    } catch (JsonProcessingException e) {
	      throw new SerializationException(e);
	    }
	  }
	}