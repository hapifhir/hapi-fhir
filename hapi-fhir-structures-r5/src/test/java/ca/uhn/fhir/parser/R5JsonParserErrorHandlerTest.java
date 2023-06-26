package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class R5JsonParserErrorHandlerTest extends AbstractJsonParserErrorHandlerTest {

  private static FhirContext ourCtx = FhirContext.forR5();

  @Override
  protected FhirContext getFhirContext() {
    return ourCtx;
  }
}
