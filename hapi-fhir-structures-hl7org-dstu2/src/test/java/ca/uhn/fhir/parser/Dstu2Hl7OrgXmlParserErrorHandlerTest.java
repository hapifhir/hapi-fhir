package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;

public class Dstu2Hl7OrgXmlParserErrorHandlerTest extends AbstractXmlParserErrorHandlerTest {

  private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();

  @Override
  protected FhirContext getFhirContext() {
    return ourCtx;
  }
}
