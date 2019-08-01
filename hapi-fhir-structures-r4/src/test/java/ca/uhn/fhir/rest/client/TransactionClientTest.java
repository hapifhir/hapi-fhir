package ca.uhn.fhir.rest.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.util.TestUtil;

public class TransactionClientTest {

  private FhirContext ctx;
  private HttpClient httpClient;
  private HttpResponse httpResponse;

  // atom-document-large.xml

  @Before
  public void before() {
    ctx = FhirContext.forR4();

    httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
    ctx.getRestfulClientFactory().setHttpClient(httpClient);
    ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

    httpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
  }

  @Test
  public void testSimpleTransaction() throws Exception {
    Patient patient = new Patient();
    patient.setId(new IdType("Patient/testPersistWithSimpleLinkP01"));
    patient.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP01");
    patient.addName().setFamily("Tester").addGiven("Joe");

    Observation obs = new Observation();
    obs.getCode().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
    obs.setSubject(new Reference("Patient/testPersistWithSimpleLinkP01"));

    List<IBaseResource> resources = Arrays.asList((IBaseResource) patient, obs);

    IClient client = ctx.newRestfulClient(IClient.class, "http://foo");

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
    when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
    when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), Charset.forName("UTF-8")));

    client.transaction(resources);

    assertEquals(HttpPost.class, capt.getValue().getClass());
    HttpPost post = (HttpPost) capt.getValue();
    assertEquals("http://foo", post.getURI().toString());

    Bundle bundle = ctx.newJsonParser().parseResource(Bundle.class, new InputStreamReader(post.getEntity().getContent()));
    ourLog.info(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));

    assertEquals(2, bundle.getEntry().size());
    assertEquals("Patient/testPersistWithSimpleLinkP01", bundle.getEntry().get(0).getResource().getIdElement().getValue());

    assertTrue(bundle.getEntry().get(1).getResource().getIdElement().isEmpty());

  }

  @Test
  public void testSimpleTransactionWithBundleParam() throws Exception {
    Patient patient = new Patient();
    patient.setId(new IdType("Patient/testPersistWithSimpleLinkP01"));
    patient.addIdentifier().setSystem("urn:system").setValue("testPersistWithSimpleLinkP01");
    patient.addName().setFamily("Tester").addGiven("Joe");

    Observation obs = new Observation();
    obs.getCode().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
    obs.setSubject(new Reference("Patient/testPersistWithSimpleLinkP01"));

    Bundle transactionBundle = new Bundle();
    transactionBundle.addEntry().setResource(patient).setFullUrl("http://foo/Patient/testPersistWithSimpleLinkP01");
    transactionBundle.addEntry().setResource(obs);

    IBundleClient client = ctx.newRestfulClient(IBundleClient.class, "http://foo");

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
    when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
    when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), Charset.forName("UTF-8")));

    client.transaction(transactionBundle);

    assertEquals(HttpPost.class, capt.getValue().getClass());
    HttpPost post = (HttpPost) capt.getValue();
    assertEquals("http://foo", post.getURI().toString());

    Bundle bundle = ctx.newJsonParser().parseResource(Bundle.class, new InputStreamReader(post.getEntity().getContent()));
    ourLog.info(ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle));

    assertEquals(2, bundle.getEntry().size());
    assertEquals("http://foo/Patient/testPersistWithSimpleLinkP01", bundle.getEntry().get(0).getResource().getIdElement().getValue());

    assertTrue(bundle.getEntry().get(1).getResource().getIdElement().isEmpty());

  }

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionClientTest.class);

  private String createBundle() {
    return ctx.newXmlParser().encodeResourceToString(new Bundle());
  }

  private interface IClient extends IBasicClient {

    @Transaction
    public List<IBaseResource> transaction(@TransactionParam List<IBaseResource> theResources);

  }

  private interface IBundleClient extends IBasicClient {

    @Transaction
    public List<IBaseResource> transaction(@TransactionParam Bundle theResources);

  }

  @AfterClass
  public static void afterClassClearContext() {
    TestUtil.clearAllStaticFieldsForUnitTest();
  }

}
