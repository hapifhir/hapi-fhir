package ca.uhn.fhir.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirContextDstu2Hl7OrgTest {

  @SuppressWarnings("deprecation")
  @Test
  public void testAutoDetectVersion() {
    FhirContext ctx = new FhirContext();
    assertEquals(FhirVersionEnum.DSTU2_HL7ORG, ctx.getVersion().getVersion());
  }

}
