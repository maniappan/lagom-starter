package org.weather.message.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;

import akka.Done;
import lombok.Value;

/**
 * This interface defines all the commands that the Workflow entity supports.
 * 
 * By convention, the commands should be inner classes of the interface, which
 * makes it simple to get a complete picture of what commands an entity
 * supports.
 */
public interface TestSampleCommand extends Jsonable {

	/**
	 * A command to switch the greeting message.
	 * <p>
	 * It has a reply type of {@link akka.Done}, which is sent back to the caller
	 * when all the events emitted by this command are successfully persisted.
	 */
	@SuppressWarnings("serial")
	@Value
	@JsonDeserialize
	final class TestSampleMessage implements TestSampleCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
		public final String city;
		public final String state;

		@JsonCreator
		public TestSampleMessage(@JsonProperty("city") String city, @JsonProperty("state") String state) {
			this.city = city;
			this.state = state;

		}

	}

	/**
	 * A command to say hello to someone using the current greeting message.
	 * <p>
	 * The reply type is String, and will contain the message to say to that person.
	 */
	@SuppressWarnings("serial")
	@Value
	@JsonDeserialize
	final class TestSample implements TestSampleCommand, PersistentEntity.ReplyType<String> {

		public final String city;

		@JsonCreator
		public TestSample(String city) {
			this.city = Preconditions.checkNotNull(city, "city");
		}
	}

}
