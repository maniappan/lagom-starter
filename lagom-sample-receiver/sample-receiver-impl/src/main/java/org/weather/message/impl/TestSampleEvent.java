package org.weather.message.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

import lombok.Value;

/**
 * This interface defines all the events that the Workflow entity supports.
 * <p>
 * By convention, the events should be inner classes of the interface, which
 * makes it simple to get a complete picture of what events an entity has.
 */
public interface TestSampleEvent extends Jsonable, AggregateEvent<TestSampleEvent> {

  /**
   * Tags are used for getting and publishing streams of events. Each event
   * will have this tag, and in this case, we are partitioning the tags into
   * 4 shards, which means we can have 4 concurrent processors/publishers of
   * events.
   */
  AggregateEventShards<TestSampleEvent> TAG = AggregateEventTag.sharded(TestSampleEvent.class, 4);

  /**
   * An event that represents a change in greeting message.
   */
  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  public final class TestSampleMessageChanged implements TestSampleEvent {

	  public final String city;
		public final String state;

		@JsonCreator
		public TestSampleMessageChanged(@JsonProperty("city") String city, @JsonProperty("state") String state) {
			this.city = city;
			this.state = state;

		}

  }

  @Override
  default AggregateEventTagger<TestSampleEvent> aggregateTag() {
    return TAG;
  }

}
