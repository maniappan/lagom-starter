package org.weather.message.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Value;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = TestSampleEvent.TestSampleEventMessage.class, name = "weather-received") })
public interface TestSampleEvent {

	String getcity();

	@Value
	final class TestSampleEventMessage implements TestSampleEvent {

		public final String city;
		public final String state;

		@JsonCreator
		public TestSampleEventMessage(@JsonProperty("city") String city, @JsonProperty("state") String state) {
			this.city = city;
			this.state = state;

		}

		@Override
		public String getcity() {
			return city;
		}

	}
}
