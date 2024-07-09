package ca.uhn.hapi.fhir.cdshooks.custom.extensions.model;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import com.fasterxml.jackson.annotation.JsonProperty;


public class RequestExtension extends CdsHooksExtension {
	@JsonProperty("example-config-item")
	String myConfigItem;

	public String getConfigItem() {
		return myConfigItem;
	}

	public void setConfigItem(String theConfigItem) {
		myConfigItem = theConfigItem;
	}
}
