package ca.uhn.fhir.jpa.term.models;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CodeSystemVersionPIDResult implements IModelJson {

	@JsonProperty("codeSystemVersionPID")
	private long myCodeSystemVersionPID;

	public long getCodeSystemVersionPID() {
		return myCodeSystemVersionPID;
	}

	public void setCodeSystemVersionPID(long theCodeSystemVersionPID) {
		myCodeSystemVersionPID = theCodeSystemVersionPID;
	}
}
