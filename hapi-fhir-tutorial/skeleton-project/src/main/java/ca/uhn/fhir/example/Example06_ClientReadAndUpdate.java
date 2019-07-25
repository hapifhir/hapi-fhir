package ca.uhn.fhir.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

//@formatter:off
public class Example06_ClientReadAndUpdate {
	public static void main(String[] theArgs) {

		// Create a client
		FhirContext ctx = new FhirContext();
		String serverBaseUrl = "http://fhirtest.uhn.ca/base";
		IGenericClient client = ctx.newRestfulGenericClient(serverBaseUrl);

		// Log requests and responses
		client.registerInterceptor(new LoggingInterceptor(true));
		
		// Build a search and execute it
		Bundle response = client.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("Test"))
			.and(Patient.BIRTHDATE.before().day("2014-01-01"))
			.limitTo(100)
			.execute();
		
		// How many resources did we find?
		System.out.println("Responses: " + response.size());
		
		// Print the ID of the first one
		IdDt firstResponseId = response.getEntries().get(0).getResource().getId();
		System.out.println(firstResponseId);
		
	}
}
//@formatter:on
