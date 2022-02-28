package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NdJsonFileJson implements IModelJson {

	@JsonProperty("ndJsonText")
	private String myNdJsonText;
	@JsonProperty("sourceName")
	private String mySourceName;

	public String getNdJsonText() {
		return myNdJsonText;
	}

	public NdJsonFileJson setNdJsonText(String theNdJsonText) {
		myNdJsonText = theNdJsonText;
		return this;
	}

	public String getSourceName() {
		return mySourceName;
	}

	public void setSourceName(String theSourceName) {
		mySourceName = theSourceName;
	}
}
