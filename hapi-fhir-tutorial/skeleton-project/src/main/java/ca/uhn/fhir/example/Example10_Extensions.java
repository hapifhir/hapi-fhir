package ca.uhn.fhir.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.parser.IParser;

public class Example10_Extensions {

	public static void main(String[] args) {
		Patient pat = new Patient();
		pat.addName().addFamily("Simpson").addGiven("Homer");
		
		String url = "http://acme.org#eyeColour";
		boolean isModifier = false;
		pat.addUndeclaredExtension(isModifier, url).setValue(new CodeDt("blue"));;
		
		IParser p = new FhirContext().newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(pat);
		
		System.out.println(encoded);
	}
	
}
