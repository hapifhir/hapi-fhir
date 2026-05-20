package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseTerminologyImportParameters implements IModelJson {

	@JsonProperty("versionId")
	private String myVersionId;

	@JsonProperty("dontMakeCurrent")
	private Boolean myDontMakeCurrent;

	public String getVersionId() {
		return myVersionId;
	}

	public void setVersionId(String theVersionId) {
		myVersionId = theVersionId;
	}

	public Boolean getDontMakeCurrent() {
		return myDontMakeCurrent;
	}

	public void setDontMakeCurrent(Boolean theDontMakeCurrent) {
		myDontMakeCurrent = theDontMakeCurrent;
	}
}
