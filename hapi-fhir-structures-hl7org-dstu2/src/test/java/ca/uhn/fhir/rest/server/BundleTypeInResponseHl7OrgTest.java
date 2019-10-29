package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu2.model.Bundle.BundleType;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class BundleTypeInResponseHl7OrgTest {

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BundleTypeInResponseHl7OrgTest.class);
  private static CloseableHttpClient ourClient;
  private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
  private static int ourPort;
  private static Server ourServer;

  @Test
  public void testSearch() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
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

  @AfterClass
  public static void afterClass() throws Exception {
    JettyUtil.closeServer(ourServer);
  }

  @BeforeClass
  public static void beforeClass() throws Exception {
    ourServer = new Server(0);

    DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

    ServletHandler proxyHandler = new ServletHandler();
    RestfulServer servlet = new RestfulServer(ourCtx);
    servlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
    servlet.setResourceProviders(patientProvider);
    servlet.setDefaultResponseEncoding(EncodingEnum.XML);

    ServletHolder servletHolder = new ServletHolder(servlet);
    proxyHandler.addServletWithMapping(servletHolder, "/*");
    ourServer.setHandler(proxyHandler);
    JettyUtil.startServer(ourServer);
    ourPort = JettyUtil.getPortForStartedServer(ourServer);

    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
    HttpClientBuilder builder = HttpClientBuilder.create();
    builder.setConnectionManager(connectionManager);
    ourClient = builder.build();

  }

}
