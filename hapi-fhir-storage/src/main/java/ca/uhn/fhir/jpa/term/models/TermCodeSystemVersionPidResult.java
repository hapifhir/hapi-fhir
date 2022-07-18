package ca.uhn.fhir.jpa.term.models;

import ca.uhn.fhir.model.api.IModelJson;

public class TermCodeSystemVersionPidResult implements IModelJson {

	private long myTermVersionPID;

	public long getTermVersionPID() {
		return myTermVersionPID;
	}

	public void setTermVersionPID(long theTermVersionPID) {
		myTermVersionPID = theTermVersionPID;
	}
}
