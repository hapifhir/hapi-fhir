package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

abstract public non-sealed class AbstractJsonParserErrorHandlerTest extends AbstractParserErrorHandlerTest {

	private static String PATIENT_DUPLICATE_CHOICE = 
			""" 
			{
				"resourceType": "Patient",
				"deceasedBoolean": "true",
				"deceasedDateTime": "2022-02-07T13:28:17-05:00"
			}
			""";

	protected abstract FhirContext getFhirContext();
	
	@Override
	protected IParser createParser() {
		return getFhirContext().newJsonParser();
	}

	@Override
	protected String createResourceWithRepeatingChoice() {
		return PATIENT_DUPLICATE_CHOICE;
	}

}
