package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReindexJobParameters implements IModelJson {

	@JsonProperty("resourceType")
	private String myResourceType;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

}
