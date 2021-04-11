package ca.uhn.fhir.jpa.bulk.imp.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkImportJobFileJson implements IModelJson {

	@JsonProperty("contents")
	private String myContents;

	public String getContents() {
		return myContents;
	}

	public BulkImportJobFileJson setContents(String theContents) {
		myContents = theContents;
		return this;
	}

}
