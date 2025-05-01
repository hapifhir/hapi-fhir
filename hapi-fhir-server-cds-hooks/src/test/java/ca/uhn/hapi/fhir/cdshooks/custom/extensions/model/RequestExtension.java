package ca.uhn.hapi.fhir.cdshooks.custom.extensions.model;

import ca.uhn.fhir.rest.api.server.cdshooks.CdsHooksExtension;
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
