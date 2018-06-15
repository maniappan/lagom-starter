package org.weather.api;

import java.util.List;

import com.lightbend.lagom.javadsl.api.deser.StrictMessageSerializer;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;
import com.lightbend.lagom.javadsl.api.transport.NotAcceptable;
import com.lightbend.lagom.javadsl.api.transport.UnsupportedMediaType;

import akka.util.ByteString;

public class TextMessageSerializer implements StrictMessageSerializer<ByteString> {


	@Override
	public NegotiatedDeserializer<ByteString, ByteString> deserializer(MessageProtocol protocol) throws UnsupportedMediaType {
	  if (protocol.contentType().isPresent()) {
	    if (protocol.contentType().get().equals("text/plain")) {
	      return new PlainTextDeserializer(protocol.charset().orElse("utf-8"));
	    } else if (protocol.contentType().get().equals("application/json")) {
	      return new JsonTextDeserializer();
	    } 
	    else if (protocol.contentType().get().equals("application/octet-stream")) {
		      return new GzDeserializer();
		    } 
	    else {
	      throw new UnsupportedMediaType(protocol, new MessageProtocol().withContentType("text/plain"));
	    }
	  } else {
	    return new PlainTextDeserializer("utf-8");
	  }
	}

	@Override
	public NegotiatedSerializer<ByteString, ByteString> serializerForRequest() {
		return null;
	}

	@Override
	public NegotiatedSerializer<ByteString, ByteString> serializerForResponse(List<MessageProtocol> acceptedMessageProtocols) throws NotAcceptable {
	return null;
	}

}
