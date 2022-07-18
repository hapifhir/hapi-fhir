package ca.uhn.fhir.jpa.term.models;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TermCodeSystemVersionPidResult implements IModelJson {

	@JsonProperty("termVersionPID")
	private long myTermVersionPID;

	public long getTermVersionPID() {
		return myTermVersionPID;
	}

	public void setTermVersionPID(long theTermVersionPID) {
		myTermVersionPID = theTermVersionPID;
	}
}
