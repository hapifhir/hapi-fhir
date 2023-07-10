package ca.uhn.hapi.fhir.cdshooks.api.json;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Coding using within CdsService responses
 */
public class CdsServiceResponseCodingJson implements IModelJson {
	@JsonProperty(value = "code", required = true)
	String myCode;

	@JsonProperty("system")
	String mySystem;

	@JsonProperty("display")
	String myDisplay;

	public String getCode() {
		return myCode;
	}

	public CdsServiceResponseCodingJson setCode(String theCode) {
		myCode = theCode;
		return this;
	}

	public String getSystem() {
		return mySystem;
	}

	public CdsServiceResponseCodingJson setSystem(String theSystem) {
		mySystem = theSystem;
		return this;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public CdsServiceResponseCodingJson setDisplay(String theDisplay) {
		myDisplay = theDisplay;
		return this;
	}
}
