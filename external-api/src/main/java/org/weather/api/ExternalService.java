package org.weather.api;

import static com.lightbend.lagom.javadsl.api.Service.named;


import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import akka.NotUsed;

public interface ExternalService extends Service {

	ServiceCall<NotUsed, String> getWeather(String city,String state);
	@Override
	default Descriptor descriptor() {
		return named("weatherapi").withCalls(Service.restCall(Method.GET,
					"/api/:city/:state",
				this::getWeather)).withAutoAcl(true);
	}
}
