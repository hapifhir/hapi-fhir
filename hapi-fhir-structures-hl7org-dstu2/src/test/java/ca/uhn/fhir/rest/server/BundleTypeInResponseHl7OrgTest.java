package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.hl7.fhir.dstu2.model.Bundle.BundleType;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();

  @Test
  public void testSearch() throws Exception {
    HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());

    ourLog.info(responseContent);

		org.hl7.fhir.dstu2.model.Bundle bundle = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());
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
