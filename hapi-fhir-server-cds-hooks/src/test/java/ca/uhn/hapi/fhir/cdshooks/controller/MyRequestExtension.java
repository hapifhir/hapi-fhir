package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import com.fasterxml.jackson.annotation.JsonProperty;


class MyRequestExtension extends CdsHooksExtension {
	@JsonProperty("example-config-item")
	String myConfigItem;

	MyRequestExtension(){}

	public String getConfigItem() {
		return myConfigItem;
	}

	public void setConfigItem(String theConfigItem) {
		myConfigItem = theConfigItem;
	}
}
