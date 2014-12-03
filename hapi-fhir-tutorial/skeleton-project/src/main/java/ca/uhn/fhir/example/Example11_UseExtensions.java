package ca.uhn.fhir.example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.parser.IParser;

public class Example11_UseExtensions {

	public static void main(String[] args) {

		Example11_ExtendedPatient pat = new Example11_ExtendedPatient();
		pat.addName().addFamily("Simpson").addGiven("Homer");
		pat.setEyeColour(new CodeDt("blue"));
		
		IParser p = new FhirContext().newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(pat);
		
		System.out.println(encoded);
		
	}

}
