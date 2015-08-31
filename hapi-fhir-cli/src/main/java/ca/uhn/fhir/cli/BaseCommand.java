package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.IGenericClient;

public class BaseCommand {

	public BaseCommand() {
		super();
	}

	protected IGenericClient newClient(FhirContext ctx) {
		IGenericClient fhirClient = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
		return fhirClient;
	}

}