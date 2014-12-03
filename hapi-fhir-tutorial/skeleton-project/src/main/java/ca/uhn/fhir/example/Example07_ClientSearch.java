package ca.uhn.fhir.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class Example07_ClientSearch {
	public static void main(String[] theArgs) {

		// Create a client
		String serverBaseUrl = "http://fhirtest.uhn.ca/base";
		FhirContext ctx = new FhirContext();
		IGenericClient client = ctx.newRestfulGenericClient(serverBaseUrl);

		// Use the client to read back the new instance using the
		// ID we retrieved from the read
		Patient patient = client.read(Patient.class, "4529");
	
		// Print the ID of the newly created resource
		System.out.println("Found ID:    " + patient.getId());
		
		// Change the gender and send an update to the server
		patient.setGender(AdministrativeGenderCodesEnum.F);
		MethodOutcome outcome = client.update().resource(patient).execute();
		
		System.out.println("Now have ID: " + outcome.getId());
		
	}
}
