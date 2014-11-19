package ca.uhn.fhir.example;

import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;

@SuppressWarnings("unused")
public class Example01_CreateAPatient {

	public static void main(String[] theArgs) {
		
		Patient pat = new Patient();
		
		IdentifierDt identifier = pat.addIdentifier();
		identifier.setSystem("http://acme.org/MRNs").setValue("7000135");
		
		HumanNameDt name = pat.addName();
		name.addFamily("Simpson").addGiven("Homer").addGiven("J");
		
		pat.getBirthDate().set
		
	}
	
}
