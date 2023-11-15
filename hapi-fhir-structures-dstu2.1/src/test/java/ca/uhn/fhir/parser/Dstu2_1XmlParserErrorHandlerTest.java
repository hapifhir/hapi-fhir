package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class Dstu2_1XmlParserErrorHandlerTest extends AbstractXmlParserErrorHandlerTest {

  private static FhirContext ourCtx = FhirContext.forDstu2_1();

	@Override
	protected FhirContext getFhirContext() {
		return ourCtx;
	}
}
