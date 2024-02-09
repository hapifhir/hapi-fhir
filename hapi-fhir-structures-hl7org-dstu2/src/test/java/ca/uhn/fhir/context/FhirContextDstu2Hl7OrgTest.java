package ca.uhn.fhir.context;

import org.junit.jupiter.api.Test;

public class FhirContextDstu2Hl7OrgTest {

  @SuppressWarnings("deprecation")
  @Test
  public void testAutoDetectVersion() {
    FhirContext ctx = new FhirContext();
		assertThat(ctx.getVersion().getVersion()).isEqualTo(FhirVersionEnum.DSTU2_HL7ORG);
  }

}
