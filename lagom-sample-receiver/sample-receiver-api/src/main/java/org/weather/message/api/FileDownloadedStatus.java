package org.weather.message.api;

import lombok.Value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value
@JsonDeserialize
public class FileDownloadedStatus {

	public final String filestatus;

	@JsonCreator
	public FileDownloadedStatus(@JsonProperty("filestatus") String filestatus) {
		this.filestatus = filestatus;

	}


}
