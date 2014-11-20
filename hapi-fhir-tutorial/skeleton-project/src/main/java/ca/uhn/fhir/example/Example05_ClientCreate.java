package ca.uhn.fhir.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class Example05_ClientCreate {
	public static void main(String[] theArgs) {

		Patient pat = new Patient();
		pat.addName().addFamily("Simpson").addGiven("Homer").addGiven("J");
		pat.addIdentifier().setSystem("http://acme.org/MRNs").setValue("7000135");
		pat.setGender(AdministrativeGenderCodesEnum.M);
		
		// Create a context
		FhirContext ctx = new FhirContext();
		
		// Create a client
		String serverBaseUrl = "http://fhirtest.uhn.ca/base";
		IGenericClient client = ctx.newRestfulGenericClient(serverBaseUrl);

		// Use the client to store a new resource instance 
		MethodOutcome outcome = client.create().resource(pat).execute();
	
		// Print the ID of the newly created resource
		System.out.println(outcome.getId());
		
	}
}
