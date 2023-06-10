package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.context.FhirContext;

/**
 */
public class FqlParser {

	private final FhirContext myFhirContext;

	public FqlParser(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}


	public FqlStatement parse(String theInput) {
		return new Parser(myFhirContext, theInput).parse();
	}


}
