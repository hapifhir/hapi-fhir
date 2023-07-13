package ca.uhn.fhir.mdm.blocklist.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockedFieldJson implements IModelJson {

	/**
	 * The fhir path to the field on the resource that is being
	 * processed.
	 * This path must lead to a single primitive value,
	 * otherwise no blocking can be detected.
	 */
	@JsonProperty(value = "fhirPath", required = true)
	private String myFhirPath;

	/**
	 * The value to block on.
	 * If the value of the field at `fhirPath` matches this
	 * value, it will be blocked.
	 */
	@JsonProperty(value = "value", required = true)
	private String myBlockedValue;

	public String getFhirPath() {
		return myFhirPath;
	}

	public BlockedFieldJson setFhirPath(String theFhirPath) {
		myFhirPath = theFhirPath;
		return this;
	}

	public String getBlockedValue() {
		return myBlockedValue;
	}

	public BlockedFieldJson setBlockedValue(String theBlockedValue) {
		myBlockedValue = theBlockedValue;
		return this;
	}
}
