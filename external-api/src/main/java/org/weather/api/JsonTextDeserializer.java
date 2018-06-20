package org.weather.api;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import com.lightbend.lagom.javadsl.api.deser.MessageSerializer;

import akka.util.ByteString;

public class JsonTextDeserializer implements MessageSerializer.NegotiatedDeserializer<ByteString, ByteString> {
	  private final ObjectMapper mapper = new ObjectMapper();

	  @Override
	  public ByteString deserialize(ByteString bytes) throws DeserializationException {
	    try {
	    	//System.out.println("test output");
	    	//System.out.println("test output---"+ bytes.decodeString("utf-8"));
	    	return bytes;
	      //return mapper.readValue(bytes.iterator().asInputStream(), String.class);
	    } catch (Exception e) {
	      throw new DeserializationException(e);
	    }
	  }
	}
