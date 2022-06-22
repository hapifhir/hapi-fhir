package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BulkExportJobResults implements IModelJson {

	@JsonProperty("binaryIds")
	private List<String> myBinaryIds;

	// the resource type of the resources written into the binary
	@JsonProperty("resourceType")
	private String myResourceType;

	public List<String> getBinaryIds() {
		return myBinaryIds;
	}

	public void setBinaryIds(List<String> theBinaryIds) {
		myBinaryIds = theBinaryIds;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}
}
