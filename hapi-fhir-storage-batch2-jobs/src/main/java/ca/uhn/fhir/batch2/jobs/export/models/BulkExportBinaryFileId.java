package ca.uhn.fhir.batch2.jobs.export.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkExportBinaryFileId extends BulkExportJobBase {

	@JsonProperty("binaryId")
	private String myBinaryId;

	@JsonProperty("resourceType")
	private String myResourceType;

	public BulkExportBinaryFileId() {
	}

	public String getBinaryId() {
		return myBinaryId;
	}

	public void setBinaryId(String theBinaryId) {
		myBinaryId = theBinaryId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}
}
