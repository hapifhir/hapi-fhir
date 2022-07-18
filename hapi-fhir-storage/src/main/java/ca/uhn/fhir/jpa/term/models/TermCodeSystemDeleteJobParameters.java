package ca.uhn.fhir.jpa.term.models;

import ca.uhn.fhir.model.api.IModelJson;

public class TermCodeSystemDeleteJobParameters implements IModelJson {

	/**
	 * Term code system PID
	 */
	private long myTermPid;

	public long getTermPid() {
		return myTermPid;
	}

	public void setTermPid(long theTermPid) {
		myTermPid = theTermPid;
	}
}
