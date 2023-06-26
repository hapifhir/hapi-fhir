package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class Dstu3JsonParserErrorHandlerTest extends AbstractJsonParserErrorHandlerTest {

  private static FhirContext ourCtx = FhirContext.forDstu3();

  @Override
  protected FhirContext getFhirContext() {
    return ourCtx;
  }
}
