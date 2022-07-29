package ca.uhn.fhir.jpa.term.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TermCodeSystemDeleteVersionJobParameters extends DeleteCodeSystemBaseParameters {

	/**
	 * Code system version pid
	 */
	@JsonProperty("versionPID")
	private long myCodeSystemVersionPid;

	public long getCodeSystemVersionPid() {
		return myCodeSystemVersionPid;
	}

	public void setCodeSystemVersionPid(long theCodeSystemVersionPid) {
		myCodeSystemVersionPid = theCodeSystemVersionPid;
	}
}
