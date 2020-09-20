package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.empi.rules.metric.EmpiMatcherEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmpiMatcherJson  implements IModelJson {
	@JsonProperty(value = "algorithm", required = true)
	EmpiMatcherEnum myAlgorithm;

	@JsonProperty(value = "identifierSystem", required = false)
	String myIdentifierSystem;

	public EmpiMatcherEnum getAlgorithm() {
		return myAlgorithm;
	}

	public EmpiMatcherJson setAlgorithm(EmpiMatcherEnum theAlgorithm) {
		myAlgorithm = theAlgorithm;
		return this;
	}

	public String getIdentifierSystem() {
		return myIdentifierSystem;
	}

	public EmpiMatcherJson setIdentifierSystem(String theIdentifierSystem) {
		myIdentifierSystem = theIdentifierSystem;
		return this;
	}
}
