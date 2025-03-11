package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class R4BJsonParserErrorHandlerTest extends AbstractJsonParserErrorHandlerTest {

  private static FhirContext ourCtx = FhirContext.forR4B();

  @Override
  protected FhirContext getFhirContext() {
    return ourCtx;
  }
}
