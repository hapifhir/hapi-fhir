package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseTerminologyImportParameters implements IModelJson {

	@JsonProperty
	private String myVersionId;
	// FIXME: implement - should this be "dontMakeCurrent"?
	@JsonProperty("makeCurrent")
	private Boolean myMakeCurrent;

	public String getVersionId() {
		return myVersionId;
	}

	public void setVersionId(String theVersionId) {
		myVersionId = theVersionId;
	}

	public Boolean getMakeCurrent() {
		return myMakeCurrent;
	}

	public void setMakeCurrent(Boolean theMakeCurrent) {
		myMakeCurrent = theMakeCurrent;
	}

}
