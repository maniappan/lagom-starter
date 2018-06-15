package org.weather.api;

import java.util.Arrays;
import java.util.List;

import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import com.lightbend.lagom.javadsl.api.deser.StrictMessageSerializer;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;
import com.lightbend.lagom.javadsl.api.transport.NotAcceptable;
import com.lightbend.lagom.javadsl.api.transport.UnsupportedMediaType;

import akka.util.ByteString;

public class TextMessageSerializer implements StrictMessageSerializer<String> {


	@Override
	public NegotiatedDeserializer<String, ByteString> deserializer(MessageProtocol protocol) throws UnsupportedMediaType {
	  if (protocol.contentType().isPresent()) {
	    if (protocol.contentType().get().equals("text/plain")) {
	      return new PlainTextDeserializer(protocol.charset().orElse("utf-8"));
	    } else if (protocol.contentType().get().equals("application/json")) {
	      return new JsonTextDeserializer();
	    } else {
	      throw new UnsupportedMediaType(protocol, new MessageProtocol().withContentType("text/plain"));
	    }
	  } else {
	    return new PlainTextDeserializer("utf-8");
	  }
	}

	@Override
	public NegotiatedSerializer<String, ByteString> serializerForRequest() {
	  return new PlainTextSerializer("utf-8");
	}

	@Override
	public NegotiatedSerializer<String, ByteString> serializerForResponse(List<MessageProtocol> acceptedMessageProtocols) throws NotAcceptable {
	  if (acceptedMessageProtocols.isEmpty()) {
	    return new PlainTextSerializer("utf-8");
	  } else {
	    for (MessageProtocol protocol: acceptedMessageProtocols) {
	      if (protocol.contentType().isPresent()) {
	        String contentType = protocol.contentType().get();
	        if (contentType.equals("text/plain") || contentType.equals("text/*") || contentType.equals("*/*")) {
	          return new PlainTextSerializer(protocol.charset().orElse("utf-8"));
	        } else if (protocol.contentType().get().equals("application/json")) {
	          return new JsonTextSerializer();
	        }
	      } else {
	        return new PlainTextSerializer(protocol.charset().orElse("utf-8"));
	      }
	    }
	    throw new NotAcceptable(acceptedMessageProtocols, new MessageProtocol().withContentType("text/plain"));
	  }
	}

}
