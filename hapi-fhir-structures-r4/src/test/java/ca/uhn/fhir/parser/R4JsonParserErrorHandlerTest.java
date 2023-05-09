package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class R4JsonParserErrorHandlerTest extends AbstractJsonParserErrorHandlerTest {

  private static FhirContext ourCtx = FhirContext.forR4();

  @Override
  protected FhirContext getFhirContext() {
    return ourCtx;
  }
}
