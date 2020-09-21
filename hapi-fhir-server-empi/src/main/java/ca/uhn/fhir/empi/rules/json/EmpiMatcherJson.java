package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.matcher.EmpiMatcherEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBase;

public class EmpiMatcherJson  implements IModelJson {
	@JsonProperty(value = "algorithm", required = true)
	EmpiMatcherEnum myAlgorithm;

	@JsonProperty(value = "identifierSystem", required = false)
	String myIdentifierSystem;

	/**
	 * For String value types, should the values be normalized (case, accents) before they are compared
	 */
	@JsonProperty(value = "exact")
	boolean myExact;

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

	public boolean getExact() {
		return myExact;
	}

	public EmpiMatcherJson setExact(boolean theExact) {
		myExact = theExact;
		return this;
	}

	public boolean match(FhirContext theFhirContext, IBase theLeftValue, IBase theRightValue) {
		return myAlgorithm.match(theFhirContext, theLeftValue, theRightValue, myExact, myIdentifierSystem);
	}
}
