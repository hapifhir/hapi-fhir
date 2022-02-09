package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobInstanceParameter implements IModelJson {

	@JsonProperty("name")
	private String myName;
	@JsonProperty("value")
	private String myValue;

	public String getName() {
		return myName;
	}

	public JobInstanceParameter setName(String theName) {
		myName = theName;
		return this;
	}

	public String getValue() {
		return myValue;
	}

	public JobInstanceParameter setValue(String theValue) {
		myValue = theValue;
		return this;
	}

}
