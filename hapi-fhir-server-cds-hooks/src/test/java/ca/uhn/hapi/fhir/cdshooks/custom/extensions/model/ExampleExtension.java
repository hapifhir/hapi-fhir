package ca.uhn.hapi.fhir.cdshooks.custom.extensions.model;

import ca.uhn.fhir.rest.api.server.cdshooks.CdsHooksExtension;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleExtension extends CdsHooksExtension {
	@JsonProperty("example-property")
	String myExampleProperty;

	public String getExampleProperty() {
		return myExampleProperty;
	}
}
