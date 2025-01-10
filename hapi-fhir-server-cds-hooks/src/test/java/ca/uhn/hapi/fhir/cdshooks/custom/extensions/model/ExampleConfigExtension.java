package ca.uhn.hapi.fhir.cdshooks.custom.extensions.model;

import ca.uhn.fhir.rest.api.server.cdshooks.CdsHooksExtension;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExampleConfigExtension extends CdsHooksExtension {
	@JsonProperty("example-client-conformance")
	String myExampleClientConformance;

	public String getExampleClientConformance() {
		return myExampleClientConformance;
	}
}
