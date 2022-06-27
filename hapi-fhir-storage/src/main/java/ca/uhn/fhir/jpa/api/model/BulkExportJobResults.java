package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkExportJobResults implements IModelJson {

//	@JsonProperty("binaryIds")
//	private List<String> myBinaryIds;
//
//	// the resource type of the resources written into the binary
//	@JsonProperty("resourceType")
//	private String myResourceType;

	@JsonProperty("resourceType2BinaryIds")
	private Map<String, List<String>> myResourceTypeToBinaryIds;

	public BulkExportJobResults() {
	}

	public Map<String, List<String>> getResourceTypeToBinaryIds() {
		if (myResourceTypeToBinaryIds == null) {
			myResourceTypeToBinaryIds = new HashMap<>();
		}
		return myResourceTypeToBinaryIds;
	}

	public void setResourceTypeToBinaryIds(Map<String, List<String>> theResourceTypeToBinaryIds) {
		myResourceTypeToBinaryIds = theResourceTypeToBinaryIds;
	}

}
