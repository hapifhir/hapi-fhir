package ca.uhn.fhir.example;

import ca.uhn.fhir.model.dstu.composite.ContactDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;

public class Example02_CreateAPatient {
	public static void main(String[] theArgs) {
		
		Patient pat = new Patient();
		
		pat.addName().addFamily("Simpson").addGiven("Homer").addGiven("J");
		pat.addIdentifier().setSystem("http://acme.org/MRNs").setValue("7000135");
		pat.addIdentifier().setLabel("Library Card 12345").setValue("12345");

		// Enumerated types are provided for many coded elements
		ContactDt contact = pat.addTelecom();
		contact.setUse(ContactUseEnum.HOME);
		contact.setSystem(ContactSystemEnum.PHONE);
		contact.setValue("1 (416) 340-4800");
		
		pat.setGender(AdministrativeGenderCodesEnum.M);
		
	}
}
