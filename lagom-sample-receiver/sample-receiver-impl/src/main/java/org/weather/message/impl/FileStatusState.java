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
public final class FileStatusState implements CompressedJsonable {

  public final String filestatus;
  public final String timestamp;

  @JsonCreator
  public FileStatusState(String filestatus, String timestamp) {
    this.filestatus = Preconditions.checkNotNull(filestatus, "filestatus");
    this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp");
  }
}
