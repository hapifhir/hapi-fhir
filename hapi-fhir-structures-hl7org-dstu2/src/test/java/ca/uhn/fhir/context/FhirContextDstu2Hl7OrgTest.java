package ca.uhn.fhir.context;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FhirContextDstu2Hl7OrgTest {

  @SuppressWarnings("deprecation")
  @Test
  public void testAutoDetectVersion() {
    FhirContext ctx = new FhirContext();
    assertEquals(FhirVersionEnum.DSTU2_HL7ORG, ctx.getVersion().getVersion());
  }

}
