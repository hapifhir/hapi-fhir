package ca.uhn.fhir.jpa.term.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TermCodeSystemDeleteJobParameters extends DeleteCodeSystemBaseParameters {

	/**
	 * Term code system PID
	 */
	@JsonProperty("codeSystemPID")
	private long myTermPid;

	public long getTermPid() {
		return myTermPid;
	}

	public void setTermPid(long theTermPid) {
		myTermPid = theTermPid;
	}
}
