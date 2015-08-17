package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.Identifier;
import org.hl7.fhir.instance.model.Patient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ETagServerHl7OrgTest {

  private static CloseableHttpClient ourClient;
  private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
  private static Date ourLastModifiedDate;
  private static int ourPort;
  private static Server ourServer;
  private static PoolingHttpClientConnectionManager ourConnectionManager;

  @Test
  public void testETagHeader() throws Exception {
    ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2/_history/3");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Identifier dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifier().get(0);
    assertEquals("2", dt.getSystemElement().getValueAsString());
    assertEquals("3", dt.getValue());

    Header cl = status.getFirstHeader(Constants.HEADER_ETAG_LC);
    assertNotNull(cl);
    assertEquals("W/\"222\"", cl.getValue());
  }

  @Test
  public void testAutomaticNotModified() throws Exception {
    ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
    httpGet.addHeader(Constants.HEADER_IF_NONE_MATCH, "\"222\"");
    HttpResponse status = ourClient.execute(httpGet);
    assertEquals(Constants.STATUS_HTTP_304_NOT_MODIFIED, status.getStatusLine().getStatusCode());
  }

  @Test
  public void testLastModifiedHeader() throws Exception {
    ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2/_history/3");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Identifier dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifier().get(0);
    assertEquals("2", dt.getSystemElement().getValueAsString());
    assertEquals("3", dt.getValue());

    Header cl = status.getFirstHeader(Constants.HEADER_LAST_MODIFIED_LOWERCASE);
    assertNotNull(cl);
    assertEquals("Sun, 25 Nov 2012 02:34:47 GMT", cl.getValue());
  }

  @Before
  public void before() throws IOException {
    ourLastId = null;
  }

  @Test
  public void testUpdateWithNoVersion() throws Exception {
    Patient p = new Patient();
    p.addIdentifier().setSystem("urn:system").setValue("001");
    String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpPut http;
    http = new HttpPut("http://localhost:" + ourPort + "/Patient/2");
    http.setEntity(new StringEntity(resBody, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    HttpResponse status = ourClient.execute(http);
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());

  }

  @Test
  public void testUpdateWithIfMatch() throws Exception {
    Patient p = new Patient();
    p.addIdentifier().setSystem("urn:system").setValue("001");
    String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpPut http;
    http = new HttpPut("http://localhost:" + ourPort + "/Patient/2");
    http.setEntity(new StringEntity(resBody, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    http.addHeader(Constants.HEADER_IF_MATCH, "\"221\"");
    CloseableHttpResponse status = ourClient.execute(http);
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    assertEquals("Patient/2/_history/221", ourLastId.toUnqualified().getValue());

  }

  @Test
  public void testUpdateWithIfMatchPreconditionFailed() throws Exception {
    Patient p = new Patient();
    p.addIdentifier().setSystem("urn:system").setValue("001");
    String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

    HttpPut http;
    http = new HttpPut("http://localhost:" + ourPort + "/Patient/2");
    http.setEntity(new StringEntity(resBody, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
    http.addHeader(Constants.HEADER_IF_MATCH, "\"222\"");
    CloseableHttpResponse status = ourClient.execute(http);
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(Constants.STATUS_HTTP_412_PRECONDITION_FAILED, status.getStatusLine().getStatusCode());
    assertEquals("Patient/2/_history/222", ourLastId.toUnqualified().getValue());
  }

  @AfterClass
  public static void afterClass() throws Exception {
    ourServer.stop();
  }

  @BeforeClass
  public static void beforeClass() throws Exception {
    ourPort = PortUtil.findFreePort();
    ourServer = new Server(ourPort);

    PatientProvider patientProvider = new PatientProvider();

    ServletHandler proxyHandler = new ServletHandler();
    RestfulServer servlet = new RestfulServer(ourCtx);
    servlet.setResourceProviders(patientProvider);
    ServletHolder servletHolder = new ServletHolder(servlet);
    proxyHandler.addServletWithMapping(servletHolder, "/*");
    ourServer.setHandler(proxyHandler);
    ourServer.start();

    ourConnectionManager = new PoolingHttpClientConnectionManager(50000, TimeUnit.MILLISECONDS);
    HttpClientBuilder builder = HttpClientBuilder.create();
    builder.setConnectionManager(ourConnectionManager);
    ourClient = builder.build();

  }

  private static IdDt ourLastId;

  public static class PatientProvider implements IResourceProvider {

    @Read(version = true)
    public Patient findPatient(@IdParam IdDt theId) {
      Patient patient = new Patient();
      patient.getMeta().setLastUpdated(ourLastModifiedDate);
      patient.addIdentifier().setSystem(theId.getIdPart()).setValue(theId.getVersionIdPart());
      patient.setId(theId.withVersion("222"));
      return patient;
    }

    @Update
    public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient theResource) {
      ourLastId = theId;

      if ("222".equals(theId.getVersionIdPart())) {
        throw new PreconditionFailedException("Bad version");
      }

      return new MethodOutcome(theId.withVersion(theId.getVersionIdPart() + "0"));
    }

    @Override
    public Class<Patient> getResourceType() {
      return Patient.class;
    }

  }

}
