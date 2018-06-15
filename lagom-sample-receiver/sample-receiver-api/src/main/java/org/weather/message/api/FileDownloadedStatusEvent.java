package org.weather.message.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Preconditions;
import lombok.Value;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = FileDownloadedStatusEvent.FileDownloadedStatusChanged.class, name = "file-status") })
public interface FileDownloadedStatusEvent {

	  String getFileStatus();

	  @Value
	  final class FileDownloadedStatusChanged implements FileDownloadedStatusEvent {
	    public final String filename;
	    public final String filestatus;

	    @JsonCreator
	    public FileDownloadedStatusChanged(String filename, String filestatus) {
	        this.filename = Preconditions.checkNotNull(filename, "filename");
	        this.filestatus = Preconditions.checkNotNull(filestatus, "filestatus");
	    }

		@Override
		public String getFileStatus() {
			return filestatus;
		}
	  }
}
