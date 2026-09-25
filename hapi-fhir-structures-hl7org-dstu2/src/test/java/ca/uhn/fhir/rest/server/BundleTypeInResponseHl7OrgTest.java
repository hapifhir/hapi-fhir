package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu2.model.Bundle.BundleType;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BundleTypeInResponseHl7OrgTest {

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BundleTypeInResponseHl7OrgTest.class);
  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new DummyPatientResourceProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.XML)
      .setDefaultPrettyPrint(false);

  @Test
  public void testSearch() throws Exception {
    String responseContent = ourServer.fhirRequest("/Patient").get().assertStatus(200).getBody();

    ourLog.info(responseContent);

		org.hl7.fhir.dstu2.model.Bundle bundle = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Bundle.class, responseContent);
		assertThat(bundle.getEntry()).hasSize(1);
		assertEquals(BundleType.SEARCHSET, bundle.getType());
  }

  public static class DummyPatientResourceProvider implements IResourceProvider {

    @Search
    public List<Patient> findPatient() {
      ArrayList<Patient> retVal = new ArrayList<>();

      Patient patient = new Patient();
      patient.setId("1");
      patient.addIdentifier().setSystem("system").setValue("identifier123");
      retVal.add(patient);
      return retVal;
    }

    @Override
    public Class<Patient> getResourceType() {
      return Patient.class;
    }

  }

  @AfterAll
  public static void afterClass() throws Exception {
    TestUtil.randomizeLocaleAndTimezone();
  }

}
