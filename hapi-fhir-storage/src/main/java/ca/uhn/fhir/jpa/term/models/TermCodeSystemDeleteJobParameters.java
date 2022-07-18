package ca.uhn.fhir.jpa.term.models;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TermCodeSystemDeleteJobParameters implements IModelJson {

	/**
	 * Term code system PID
	 */
	@JsonProperty("termPid")
	private long myTermPid;

	public long getTermPid() {
		return myTermPid;
	}

	public void setTermPid(long theTermPid) {
		myTermPid = theTermPid;
	}
}
