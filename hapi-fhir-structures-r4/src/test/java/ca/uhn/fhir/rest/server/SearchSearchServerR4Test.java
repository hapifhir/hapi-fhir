package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.BaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.util.UrlUtil.escapeUrlParam;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchSearchServerR4Test {

  private static CloseableHttpClient ourClient;
  private static FhirContext ourCtx = FhirContext.forR4();
  private static IServerAddressStrategy ourDefaultAddressStrategy;
  private static StringAndListParam ourLastAndList;

  private static Set<Include> ourLastIncludes;
  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchSearchServerR4Test.class);
  private static int ourPort;
  private static Server ourServer;
  private static RestfulServer ourServlet;

  @BeforeEach
  public void before() {
    ourServlet.setServerAddressStrategy(ourDefaultAddressStrategy);
    ourLastIncludes = null;
    ourLastAndList = null;
  }

  @Test
  public void testEncodeConvertsReferencesToRelative() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithRef");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    ourLog.info(responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());
    Patient patient = (Patient) ourCtx.newXmlParser().parseResource(Bundle.class, responseContent).getEntry().get(0).getResource();
    String ref = patient.getManagingOrganization().getReference();
    assertEquals("Organization/555", ref);
  }

  /**
   * Try loading the page as a POST just to make sure we get the right error
   */
  @Test
  public void testGetPagesWithPost() throws Exception {

    HttpPost httpPost = new HttpPost("http://localhost:" + ourPort);
    List<? extends NameValuePair> parameters = Collections.singletonList(new BasicNameValuePair("_getpages", "AAA"));
    httpPost.setEntity(new UrlEncodedFormEntity(parameters));

    CloseableHttpResponse status = ourClient.execute(httpPost);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    ourLog.info(responseContent);
    assertEquals(400, status.getStatusLine().getStatusCode());
    // assertThat(responseContent, containsString("Requests for _getpages must use HTTP GET"));
  }

  @Test
  public void testOmitEmptyOptionalParam() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals(null, p.getNameFirstRep().getFamily());
  }

  @Test
  public void testParseEscapedValues() throws Exception {

    StringBuilder b = new StringBuilder();
    b.append("http://localhost:");
    b.append(ourPort);
    b.append("/Patient?");
    b.append(escapeUrlParam("findPatientWithAndList")).append('=').append(escapeUrlParam("NE\\,NE,NE\\,NE")).append('&');
    b.append(escapeUrlParam("findPatientWithAndList")).append('=').append(escapeUrlParam("NE\\\\NE")).append('&');
    b.append(escapeUrlParam("findPatientWithAndList:exact")).append('=').append(escapeUrlParam("E\\$E")).append('&');
    b.append(escapeUrlParam("findPatientWithAndList:exact")).append('=').append(escapeUrlParam("E\\|E")).append('&');

    HttpGet httpGet = new HttpGet(b.toString());

    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    ourLog.info(responseContent);

    assertEquals(200, status.getStatusLine().getStatusCode());

    assertEquals(4, ourLastAndList.getValuesAsQueryTokens().size());
    assertEquals(2, ourLastAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().size());
    assertFalse(ourLastAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).isExact());
    assertEquals("NE,NE", ourLastAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(0).getValue());
    assertEquals("NE,NE", ourLastAndList.getValuesAsQueryTokens().get(0).getValuesAsQueryTokens().get(1).getValue());
    assertEquals("NE\\NE", ourLastAndList.getValuesAsQueryTokens().get(1).getValuesAsQueryTokens().get(0).getValue());
    assertTrue(ourLastAndList.getValuesAsQueryTokens().get(2).getValuesAsQueryTokens().get(0).isExact());
    assertEquals("E$E", ourLastAndList.getValuesAsQueryTokens().get(2).getValuesAsQueryTokens().get(0).getValue());
    assertEquals("E|E", ourLastAndList.getValuesAsQueryTokens().get(3).getValuesAsQueryTokens().get(0).getValue());
  }

  @Test
  public void testReturnLinks() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=findWithLinks");

    CloseableHttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(10, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("AAANamed", p.getIdentifierFirstRep().getValue());

  }

  /**
   * #149
   */
  @Test
  public void testReturnLinksWithAddressStrategy() throws Exception {
    ourServlet.setServerAddressStrategy(new HardcodedServerAddressStrategy("https://blah.com/base"));

    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=findWithLinks");

    CloseableHttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

    ourLog.info(responseContent);

    assertEquals(10, bundle.getEntry().size());
    assertEquals("https://blah.com/base/Patient?_query=findWithLinks", bundle.getLink("self").getUrl());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("AAANamed", p.getIdentifierFirstRep().getValue());

    String linkNext = bundle.getLink("next").getUrl();
    ourLog.info(linkNext);
    assertThat(linkNext, startsWith("https://blah.com/base?_getpages="));

    /*
     * Load the second page
     */
    String urlPart = linkNext.substring(linkNext.indexOf('?'));
    String link = "http://localhost:" + ourPort + urlPart;
    httpGet = new HttpGet(link);

    status = ourClient.execute(httpGet);
    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

    ourLog.info(responseContent);

    assertEquals(10, bundle.getEntry().size());
    assertEquals(linkNext, bundle.getLink("self").getUrl());

    p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("AAANamed", p.getIdentifierFirstRep().getValue());

  }

  @Test
  public void testSearchById() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=aaa");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("idaaa", p.getNameFirstRep().getFamily());
  }

  @Test
  public void testSearchByIdUsingClient() throws Exception {
    IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);

    Bundle bundle = client
        .search()
        .forResource("Patient")
        .where(BaseResource.RES_ID.exactly().code("aaa"))
        .returnBundle(Bundle.class)
        .execute();
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("idaaa", p.getNameFirstRep().getFamily());
  }

  @Test
  public void testSearchByPost() throws Exception {
    HttpPost filePost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search");

    // add parameters to the post method
    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("_id", "aaa"));

    UrlEncodedFormEntity sendentity = new UrlEncodedFormEntity(parameters, "UTF-8");
    filePost.setEntity(sendentity);

    HttpResponse status = ourClient.execute(filePost);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("idaaa", p.getNameFirstRep().getFamily());
  }

  /**
   * See #164
   */
  @Test
  public void testSearchByPostWithInvalidPostUrl() throws Exception {
    HttpPost filePost = new HttpPost("http://localhost:" + ourPort + "/Patient?name=Central"); // should end with
                                                                                               // _search

    // add parameters to the post method
    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("_id", "aaa"));

    UrlEncodedFormEntity sendentity = new UrlEncodedFormEntity(parameters, "UTF-8");
    filePost.setEntity(sendentity);

    HttpResponse status = ourClient.execute(filePost);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    ourLog.info(responseContent);
    assertEquals(400, status.getStatusLine().getStatusCode());
    assertThat(responseContent, containsString(
        "<diagnostics value=\""+ Msg.code(446) + "Incorrect Content-Type header value of &quot;application/x-www-form-urlencoded; charset=UTF-8&quot; was provided in the request. A FHIR Content-Type is required for &quot;CREATE&quot; operation\"/>"));
  }

  /**
   * See #164
   */
  @Test
  public void testSearchByPostWithMissingContentType() throws Exception {
    HttpPost filePost = new HttpPost("http://localhost:" + ourPort + "/Patient?name=Central"); // should end with
                                                                                               // _search

    HttpEntity sendentity = new ByteArrayEntity(new byte[] { 1, 2, 3, 4 });
    filePost.setEntity(sendentity);

    HttpResponse status = ourClient.execute(filePost);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    ourLog.info(responseContent);
    assertEquals(400, status.getStatusLine().getStatusCode());
    assertThat(responseContent, containsString("<diagnostics value=\""+ Msg.code(448) + "No Content-Type header was provided in the request. This is required for &quot;CREATE&quot; operation\"/>"));
  }

  /**
   * See #164
   */
  @Test
  public void testSearchByPostWithParamsInBodyAndUrl() throws Exception {
    HttpPost filePost = new HttpPost("http://localhost:" + ourPort + "/Patient/_search?name=Central");

    // add parameters to the post method
    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("_id", "aaa"));

    UrlEncodedFormEntity sendentity = new UrlEncodedFormEntity(parameters, "UTF-8");
    filePost.setEntity(sendentity);

    HttpResponse status = ourClient.execute(filePost);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    ourLog.info(responseContent);
    assertEquals(200, status.getStatusLine().getStatusCode());

    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("idaaa", p.getName().get(0).getFamily());
    assertEquals("nameCentral", p.getName().get(1).getGiven().get(0).getValue());

  }

  @Test
  public void testSearchCompartment() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/fooCompartment");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    ourLog.info(responseContent);
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("fooCompartment", p.getIdentifierFirstRep().getValue());
    assertThat(bundle.getEntry().get(0).getResource().getIdElement().getValue(), containsString("Patient/123"));
  }

  @Test
  public void testSearchGetWithUnderscoreSearch() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/_search?subject%3APatient=100&name=3141-9%2C8302-2%2C8287-5%2C39156-5");

    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());

    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Observation p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Observation.class).get(0);
    assertEquals("Patient/100", p.getSubject().getReference().toString());
    assertEquals(4, p.getCode().getCoding().size());
    assertEquals("3141-9", p.getCode().getCoding().get(0).getCode());
    assertEquals("8302-2", p.getCode().getCoding().get(1).getCode());

  }

  @Test
  public void testSearchIncludesParametersIncludes() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchIncludes&_include=foo&_include:recurse=bar");

    CloseableHttpResponse status = ourClient.execute(httpGet);
    IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());

    assertEquals(2, ourLastIncludes.size());
    assertThat(ourLastIncludes, containsInAnyOrder(new Include("foo", false), new Include("bar", true)));
  }

  @Test
  public void testSearchIncludesParametersIncludesList() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchIncludesList&_include=foo&_include:recurse=bar");

    CloseableHttpResponse status = ourClient.execute(httpGet);
    IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());

    assertEquals(2, ourLastIncludes.size());
    assertThat(ourLastIncludes, containsInAnyOrder(new Include("foo", false), new Include("bar", true)));
  }

  @Test
  public void testSearchIncludesParametersNone() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchIncludes");

    CloseableHttpResponse status = ourClient.execute(httpGet);
    IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());

    assertThat(ourLastIncludes, empty());
  }

  @Test
  public void testSearchWithOrList() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?findPatientWithOrList=aaa,bbb");
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("aaa", p.getIdentifier().get(0).getValue());
    assertEquals("bbb", p.getIdentifier().get(1).getValue());
  }

  @Test
  public void testSearchWithTokenParameter() throws Exception {
    String token = UrlUtil.escapeUrlParam("http://www.dmix.gov/vista/2957|301");
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?tokenParam=" + token);
    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("http://www.dmix.gov/vista/2957", p.getNameFirstRep().getFamily());
    assertEquals("301", p.getNameFirstRep().getGivenAsSingleString());
  }

  @Test
  public void testSpecificallyNamedQueryGetsPrecedence() throws Exception {
    HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?AAA=123");

    HttpResponse status = ourClient.execute(httpGet);
    String responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    Patient p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("AAA", p.getIdentifierFirstRep().getValue());

    // Now the named query

    httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=findPatientByAAA&AAA=123");

    status = ourClient.execute(httpGet);
    responseContent = IOUtils.toString(status.getEntity().getContent());
    IOUtils.closeQuietly(status.getEntity().getContent());
    assertEquals(200, status.getStatusLine().getStatusCode());
    bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
    assertEquals(1, bundle.getEntry().size());

    p = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class).get(0);
    assertEquals("AAANamed", p.getIdentifierFirstRep().getValue());
  }

  @AfterAll
  public static void afterClassClearContext() throws Exception {
    JettyUtil.closeServer(ourServer);
    TestUtil.randomizeLocaleAndTimezone();
  }

  @BeforeAll
  public static void beforeClass() throws Exception {
    ourServer = new Server(0);

    DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

    ServletHandler proxyHandler = new ServletHandler();
    ourServlet = new RestfulServer(ourCtx);
    ourServlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
    ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);
    ourServlet.setPagingProvider(new FifoMemoryPagingProvider(10).setDefaultPageSize(10));

    ourServlet.setResourceProviders(patientProvider, new DummyObservationResourceProvider());
    ServletHolder servletHolder = new ServletHolder(ourServlet);
    proxyHandler.addServletWithMapping(servletHolder, "/*");
    ourServer.setHandler(proxyHandler);
    JettyUtil.startServer(ourServer);
    ourPort = JettyUtil.getPortForStartedServer(ourServer);

    PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
    HttpClientBuilder builder = HttpClientBuilder.create();
    builder.setConnectionManager(connectionManager);
    ourClient = builder.build();

    ourDefaultAddressStrategy = ourServlet.getServerAddressStrategy();
  }

  public static class DummyObservationResourceProvider implements IResourceProvider {

    @Override
    public Class<Observation> getResourceType() {
      return Observation.class;
    }

    @Search
    public Observation search(@RequiredParam(name = "subject") ReferenceParam theSubject, @RequiredParam(name = "name") TokenOrListParam theName) {
      Observation o = new Observation();
      o.setId("1");

      o.getSubject().setReference(theSubject.getResourceType() + "/" + theSubject.getIdPart());
      for (TokenParam next : theName.getValuesAsQueryTokens()) {
        o.getCode().addCoding().setSystem(next.getSystem()).setCode(next.getValue());
      }

      return o;
    }

  }

  public static class DummyPatientResourceProvider implements IResourceProvider {

    @Search(compartmentName = "fooCompartment")
    public List<Patient> compartment(@IdParam IdType theId) {
      ArrayList<Patient> retVal = new ArrayList<>();

      Patient patient = new Patient();
      patient.setId(theId);
      patient.addIdentifier().setSystem("system").setValue("fooCompartment");
      retVal.add(patient);
      return retVal;
    }

    /**
     * Only needed for #164
     */
    @Create
    public MethodOutcome create(@ResourceParam Patient thePatient) {
      throw new IllegalArgumentException();
    }

    @Search
    public List<Patient> findPatient(@RequiredParam(name = "_id") StringParam theParam, @OptionalParam(name = "name") StringParam theName) {
      ArrayList<Patient> retVal = new ArrayList<Patient>();

      Patient patient = new Patient();
      patient.setId("1");
      patient.addIdentifier().setSystem("system").setValue("identifier123");
      if (theParam != null) {
        patient.addName().setFamily("id" + theParam.getValue());
        if (theName != null) {
          patient.addName().addGiven("name" + theName.getValue());
        }
      }
      retVal.add(patient);
      return retVal;
    }

    @Search
    public List<Patient> findPatientByAAA01(@RequiredParam(name = "AAA") StringParam theParam) {
      ArrayList<Patient> retVal = new ArrayList<Patient>();

      Patient patient = new Patient();
      patient.setId("1");
      patient.addIdentifier().setSystem("system").setValue("AAA");
      retVal.add(patient);
      return retVal;
    }

    @Search(queryName = "findPatientByAAA")
    public List<Patient> findPatientByAAA02Named(@OptionalParam(name = "AAA") StringParam theParam) {
      ArrayList<Patient> retVal = new ArrayList<Patient>();

      Patient patient = new Patient();
      patient.setId("1");
      patient.addIdentifier().setSystem("system").setValue( "AAANamed");
      retVal.add(patient);
      return retVal;
    }

    @Search()
    public List<Patient> findPatientWithAndList(@RequiredParam(name = "findPatientWithAndList") StringAndListParam theParam) {
      ourLastAndList = theParam;
      ArrayList<Patient> retVal = new ArrayList<Patient>();
      return retVal;
    }

    @Search()
    public List<Patient> findPatientWithOrList(@RequiredParam(name = "findPatientWithOrList") StringOrListParam theParam) {
      ArrayList<Patient> retVal = new ArrayList<Patient>();

      Patient patient = new Patient();
      patient.setId("1");
      for (StringParam next : theParam.getValuesAsQueryTokens()) {
        patient.addIdentifier().setSystem("system").setValue( next.getValue());
      }
      retVal.add(patient);
      return retVal;
    }

    @Search()
    public List<Patient> findPatientWithToken(@RequiredParam(name = "tokenParam") TokenParam theParam) {
      ArrayList<Patient> retVal = new ArrayList<Patient>();

      Patient patient = new Patient();
      patient.setId("1");
      patient.addName().setFamily(theParam.getSystem()).addGiven(theParam.getValue());
      retVal.add(patient);
      return retVal;
    }

    @Search(queryName = "findWithLinks")
    public List<Patient> findWithLinks() {
      ArrayList<Patient> retVal = new ArrayList<Patient>();

      for (int i = 1; i <= 20; i++) {
        Patient patient = new Patient();
        patient.setId("" + i);
        patient.addIdentifier().setSystem("system").setValue( "AAANamed");
        retVal.add(patient);
      }

      return retVal;
    }

    @Override
    public Class<Patient> getResourceType() {
      return Patient.class;
    }

    @Search(queryName = "searchIncludes")
    public List<Patient> searchIncludes(@IncludeParam Set<Include> theIncludes) {
      ourLastIncludes = theIncludes;

      ArrayList<Patient> retVal = new ArrayList<Patient>();
      return retVal;
    }

    @Search(queryName = "searchIncludesList")
    public List<Patient> searchIncludesList(@IncludeParam List<Include> theIncludes) {
      if (theIncludes != null) {
        ourLastIncludes = new HashSet<Include>(theIncludes);
      }

      ArrayList<Patient> retVal = new ArrayList<Patient>();
      return retVal;
    }

    @Search(queryName = "searchWithRef")
    public Patient searchWithRef() {
      Patient patient = new Patient();
      patient.setId("Patient/1/_history/1");
      patient.getManagingOrganization().setReference("http://localhost:" + ourPort + "/Organization/555/_history/666");
      return patient;
    }

  }

}
