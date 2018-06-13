package org.weather.message.impl;

import lombok.Value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;

/**
 * The state for the {@link Workflow} entity.
 */
@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class TestSampleState implements CompressedJsonable {

  public final String city;
  public final String timestamp;

  @JsonCreator
  public TestSampleState(String city, String timestamp) {
    this.city = Preconditions.checkNotNull(city, "message");
    this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp");
  }
}
