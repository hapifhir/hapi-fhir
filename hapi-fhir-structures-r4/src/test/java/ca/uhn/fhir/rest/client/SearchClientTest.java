package ca.uhn.fhir.rest.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.TestUtil;

public class SearchClientTest {

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchClientTest.class);

  private FhirContext ourCtx;
  private HttpClient ourHttpClient;
  private HttpResponse ourHttpResponse;

  @Before
  public void before() {
    ourCtx = FhirContext.forR4();

    ourHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
    ourCtx.getRestfulClientFactory().setHttpClient(ourHttpClient);
    ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

    ourHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
  }

  @Test
  public void testPostOnLongParamsList() throws Exception {
    String resp = createBundle();

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
    when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
    when(ourHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(resp), Charset.forName("UTF-8")));

    ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
    Set<Include> includes = new HashSet<Include>();
    includes.add(new Include("one"));
    includes.add(new Include("two"));
    TokenOrListParam params = new TokenOrListParam();
    for (int i = 0; i < 1000; i++) {
      params.add(new TokenParam("system", "value"));
    }
    List<Encounter> found = client.searchByList(params, includes);

    assertEquals(1, found.size());

    Encounter encounter = found.get(0);
    assertNotNull(encounter.getSubject().getReference());
    HttpUriRequest value = capt.getValue();

    assertTrue("Expected request of type POST on long params list", value instanceof HttpPost);
    HttpPost post = (HttpPost) value;
    String body = IOUtils.toString(post.getEntity().getContent());
    ourLog.info(body);
    assertThat(body, Matchers.containsString("_include=one"));
    assertThat(body, Matchers.containsString("_include=two"));
  }

  @Test
  public void testReturnTypedList() throws Exception {
    
    String resp = createBundle();

    ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
    when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
    when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
    when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
    when(ourHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(resp), Charset.forName("UTF-8")));

    ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
    List<Encounter> found = client.search();
    assertEquals(1, found.size());

    Encounter encounter = found.get(0);
    assertNotNull(encounter.getSubject().getReference());
  }

  private String createBundle() {
    Bundle bundle = new Bundle();
    
    Encounter enc = new Encounter();
    enc.getSubject().setReference("Patient/1");
    
    bundle.addEntry().setResource(enc);
    
    String retVal = ourCtx.newXmlParser().encodeResourceToString(bundle);
    return retVal;
  }

  private interface ITestClient extends IBasicClient {

    @Search
    List<Encounter> search();

    @Search
    List<Encounter> searchByList(@RequiredParam(name = Encounter.SP_IDENTIFIER) TokenOrListParam tokenOrListParam, @IncludeParam Set<Include> theIncludes) throws BaseServerResponseException;

  }

  @AfterClass
  public static void afterClassClearContext() {
    TestUtil.clearAllStaticFieldsForUnitTest();
  }

}
