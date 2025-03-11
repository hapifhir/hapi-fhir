package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class Dstu2JsonParserErrorHandlerTest extends AbstractJsonParserErrorHandlerTest {

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@Override
	protected FhirContext getFhirContext() {
		return ourCtx;
	}
}
