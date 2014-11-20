package ca.uhn.fhir.example;

import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class Example01_CreateAPatient {

	public static void main(String[] theArgs) {
		
		// Create a resource instance
		Patient pat = new Patient();
		
		// Add a "name" element
		HumanNameDt name = pat.addName();
		name.addFamily("Simpson").addGiven("Homer").addGiven("J");
		
		// Add an "identifier" element
		IdentifierDt identifier = pat.addIdentifier();
		identifier.setSystem("http://acme.org/MRNs").setValue("7000135");

		// Model is designed to be chained
		pat.addIdentifier().setLabel("Library Card 12345").setValue("12345");
		
	}
	
}
