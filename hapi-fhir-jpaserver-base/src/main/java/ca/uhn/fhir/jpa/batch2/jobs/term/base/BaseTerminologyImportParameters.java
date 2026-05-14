package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseTerminologyImportParameters implements IModelJson {

	// FIXME: implement - should this be "dontMakeCurrent"?
	@JsonProperty("makeCurrent")
	private Boolean myMakeCurrent;

	public Boolean getMakeCurrent() {
		return myMakeCurrent;
	}

	public void setMakeCurrent(Boolean theMakeCurrent) {
		myMakeCurrent = theMakeCurrent;
	}

}
