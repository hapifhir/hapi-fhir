package ca.uhn.hapi.fhir.cdshooks.custom.extensions.model;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleExtension extends CdsHooksExtension {
	@JsonProperty("example-property")
	String myExampleProperty;

	public String getExampleProperty() {
		return myExampleProperty;
	}
}
