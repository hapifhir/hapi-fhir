package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class R4BXmlParserErrorHandlerTest extends AbstractXmlParserErrorHandlerTest {

  private static FhirContext ourCtx = FhirContext.forR4B();

  @Override
  protected FhirContext getFhirContext() {
    return ourCtx;
  }
}
