package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public abstract non-sealed class AbstractXmlParserErrorHandlerTest extends AbstractParserErrorHandlerTest {

	private static String PATIENT_DUPLICATE_CHOICE = 
			"""
			<Patient xmlns="http://hl7.org/fhir">
					<deceasedBoolean value="true"></deceasedBoolean>
					<deceasedDateTime value="2022-02-07T13:28:17-05:00"></deceasedDateTime>
			</Patient>""";

	protected abstract FhirContext getFhirContext();

	@Override
	protected IParser createParser() {
		return getFhirContext().newXmlParser();
	}

	@Override
	protected String createResourceWithRepeatingChoice() {
		return PATIENT_DUPLICATE_CHOICE;
	}

}
