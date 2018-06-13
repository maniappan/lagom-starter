package org.weather.message.api;

import lombok.Value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value
@JsonDeserialize
public class TestSampleMessage {

	public final String city;
	public final String state;

	@JsonCreator
	public TestSampleMessage(@JsonProperty("city") String city, @JsonProperty("state") String state) {
		this.city = city;
		this.state = state;

	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

}
