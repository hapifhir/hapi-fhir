package ca.uhn.fhir.android;

import ca.uhn.fhir.context.FhirContext;

public class AndroidLoader {

	public static void main(String[] theArgs) {
		FhirContext ctx = FhirContext.forDstu2();
		ctx.newJsonParser();
		ctx.newXmlParser();
		ctx.newRestfulGenericClient("");
	}
	
}
