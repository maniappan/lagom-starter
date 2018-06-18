package org.weather.api;

import static com.lightbend.lagom.javadsl.api.Service.named;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import akka.NotUsed;
import akka.util.ByteString;

public interface ExternalService extends Service {

	ServiceCall<NotUsed, ByteString> getWeather(String filename);
	@Override
	default Descriptor descriptor() {
		return named("weatherapi").withCalls(Service.restCall(Method.GET,
					"/sample/:filename",
				this::getWeather).withResponseSerializer( new TextMessageSerializer())).withAutoAcl(true);
	}
}
