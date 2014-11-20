package ca.uhn.fhir.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.parser.IParser;

public class Example03_EncodeResource {
	public static void main(String[] theArgs) {
		
		// Create a Patient
		Patient pat = new Patient();
		pat.addName().addFamily("Simpson").addGiven("Homer").addGiven("J");
		pat.addIdentifier().setSystem("http://acme.org/MRNs").setValue("7000135");
		pat.addIdentifier().setLabel("Library Card 12345").setValue("12345");
		pat.addTelecom().setUse(ContactUseEnum.HOME).setSystem(ContactSystemEnum.PHONE).setValue("1 (416) 340-4800");
		pat.setGender(AdministrativeGenderCodesEnum.M);
		
		// Create a context
		FhirContext ctx = new FhirContext();
		
		// Create a XML parser
		IParser parser = ctx.newXmlParser();
		parser.setPrettyPrint(true);
		
		String encode = parser.encodeResourceToString(pat);
		System.out.println(encode);
		
	}
}
