package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class R4XmlParserErrorHandlerTest extends AbstractXmlParserErrorHandlerTest {

  private static FhirContext ourCtx = FhirContext.forR4();

  @Override
  protected FhirContext getFhirContext() {
    return ourCtx;
  }
}
