package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.impl.BaseClient;
import ca.uhn.fhir.rest.client.impl.GenericClient;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.model.Bundle.BundleType;
import org.hl7.fhir.r5.model.Bundle.HTTPVerb;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericClientTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientTest.class);
	private static FhirContext ourCtx;
	private HttpClient myHttpClient;

	private HttpResponse myHttpResponse;

	@Before
	public void before() {

		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());

		System.setProperty(BaseClient.HAPI_CLIENT_KEEPRESPONSES, "true");
	}

	private Patient createPatientP1() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo:bar").setValue("12345");
		p1.addName().setFamily("Smith").addGiven("John");
		return p1;
	}

	private Bundle createTransactionBundleInput() {
		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		input
			.addEntry()
			.setResource(createPatientP1())
			.getRequest()
			.setMethod(HTTPVerb.POST);
		return input;
	}

	private Bundle createTransactionBundleOutput() {
		Bundle output = new Bundle();
		output.setType(BundleType.TRANSACTIONRESPONSE);
		output
			.addEntry()
			.setResource(createPatientP1())
			.getResponse()
			.setLocation(createPatientP1().getId());
		return output;
	}

	private String extractBody(ArgumentCaptor<HttpUriRequest> capt, int count) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(count)).getEntity().getContent(), StandardCharsets.UTF_8);
		return body;
	}

	private String getResourceResult() {
		String msg =
			"<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div></text>"
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
				+ "<name><family value=\"Kramer\" /><given value=\"Doe\" /></name>"
				+ "<telecom><system value=\"phone\" /><value value=\"555-555-2004\" /><use value=\"work\" /></telecom>"
				+ "<gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\" /><code value=\"M\" /></coding></gender>"
				+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
				+ "</Patient>";
		return msg;
	}

	@SuppressWarnings("unused")
	@Test
	public void testCacheControlNoStore() throws Exception {

		String msg = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true))
			.execute();

		assertEquals("http://example.com/fhir/Observation", capt.getValue().getURI().toString());
		assertEquals(1, capt.getValue().getHeaders("Cache-Control").length);
		assertEquals("no-store", capt.getValue().getHeaders("Cache-Control")[0].getValue());
	}

	@SuppressWarnings("unused")
	@Test
	public void testCacheControlNoStoreMaxResults() throws Exception {

		String msg = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true).setMaxResults(100))
			.execute();

		assertEquals("http://example.com/fhir/Observation", capt.getValue().getURI().toString());
		assertEquals(1, capt.getValue().getHeaders("Cache-Control").length);
		assertEquals("no-store, max-results=100", capt.getValue().getHeaders("Cache-Control")[0].getValue());
	}

	@SuppressWarnings("unused")
	@Test
	public void testCacheControlNoStoreNoCache() throws Exception {

		String msg = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Observation.class)
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoStore(true).setNoCache(true))
			.execute();

		assertEquals("http://example.com/fhir/Observation", capt.getValue().getURI().toString());
		assertEquals(1, capt.getValue().getHeaders("Cache-Control").length);
		assertEquals("no-cache, no-store", capt.getValue().getHeaders("Cache-Control")[0].getValue());
	}

	@Test
	public void testCreatePopulatesIsCreated() throws Exception {

		Patient p1 = createPatientP1();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		MethodOutcome resp = client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertTrue(resp.getCreated());

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		resp = client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertNull(resp.getCreated());

		ourLog.info("lastRequest: {}", ((GenericClient) client).getLastRequest());
		ourLog.info("lastResponse: {}", ((GenericClient) client).getLastResponse());
		ourLog.info("lastResponseBody: {}", ((GenericClient) client).getLastResponseBody());
	}

	@Test
	public void testCreateWithStringAutoDetectsEncoding() throws Exception {

		Patient p1 = createPatientP1();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("value=\"John\""));
		count++;

		String resourceAsString = ourCtx.newJsonParser().encodeResourceToString(p1);
		client
			.create()
			.resource(resourceAsString)
			.execute();

		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("[\"John\"]"));
		count++;

		/*
		 * e.g. Now try with reversed encoding (provide a string that's in JSON and ask the client to use XML)
		 */

		client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).encodedJson().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("[\"John\"]"));
		count++;

		client.create().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).encodedXml().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("value=\"John\""));

	}

	@Test
	public void testCreateWithTag() throws Exception {

		Patient p1 = createPatientP1();
		p1.getMeta().addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client.create().resource(p1).withAdditionalHeader("myHeaderName", "myHeaderValue").execute();
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		int count = 0;

		assertEquals("http://example.com/fhir/Patient", capt.getValue().getURI().toString());
		assertEquals("POST", capt.getValue().getMethod());
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals("myHeaderValue", capt.getValue().getFirstHeader("myHeaderName").getValue());
		count++;

		/*
		 * Try fluent options
		 */
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));
		client.create().resource(p1).execute();
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(1).getURI().toString());
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		count++;

		String resourceText = "<Patient xmlns=\"http://hl7.org/fhir\">    </Patient>";
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));
		client.create().resource(resourceText).execute();
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(2).getURI().toString());
		assertEquals(resourceText, IOUtils.toString(((HttpPost) capt.getAllValues().get(2)).getEntity().getContent()));
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		count++;
	}

	@Test
	public void testCreateWithTagNonFluent() throws Exception {

		Patient p1 = createPatientP1();
		p1.getMeta().addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client.create().resource(p1).execute();
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		assertEquals("http://example.com/fhir/Patient", capt.getValue().getURI().toString());
		assertEquals("POST", capt.getValue().getMethod());
		Header catH = capt.getValue().getFirstHeader("Category");
		assertNull(catH);
	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testCreateWithUtf8Characters() throws Exception {
		String name = "測試醫院";
		Organization org = new Organization();
		org.setName(name);
		org.addIdentifier().setSystem("urn:system").setValue("testCreateWithUtf8Characters_01");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.create().resource(org).prettyPrint().encodedXml().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("<name value=\"測試醫院\"/>"));
		count++;

	}

	@Test
	public void testDelete() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().addLocation("testDelete01");
		String ooStr = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ooStr), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client
			.delete()
			.resourceById("Patient", "123")
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.execute();

		oo = (OperationOutcome) outcome.getOperationOutcome();
		assertEquals("http://example.com/fhir/Patient/123", capt.getValue().getURI().toString());
		assertEquals("DELETE", capt.getValue().getMethod());
		Assert.assertEquals("testDelete01", oo.getIssueFirstRep().getLocation().get(0).getValue());
		assertEquals("myHeaderValue", capt.getValue().getFirstHeader("myHeaderName").getValue());

	}


	@Test
	public void testDeleteInvalidResponse() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().addLocation("testDelete01");
		String ooStr = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("LKJHLKJGLKJKLL"), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		// Try with invalid response
		try {
			client
				.delete()
				.resourceById(new IdType("Location", "123", "456"))
				.prettyPrint()
				.encodedJson()
				.execute();
		} catch (FhirClientConnectionException e) {
			assertEquals(0, e.getStatusCode());
			assertThat(e.getMessage(), containsString("Failed to parse response from server when performing DELETE to URL"));
		}

	}
	
	
	@Test
	public void testDeleteNoResponse() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().addLocation("testDelete01");
		String ooStr = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ooStr), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client
			.delete()
			.resourceById("Patient", "123")
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.execute();

		oo = (OperationOutcome) outcome.getOperationOutcome();
		assertEquals("http://example.com/fhir/Patient/123", capt.getValue().getURI().toString());
		assertEquals("DELETE", capt.getValue().getMethod());
		Assert.assertEquals("testDelete01", oo.getIssueFirstRep().getLocation().get(0).getValue());
		assertEquals("myHeaderValue", capt.getValue().getFirstHeader("myHeaderName").getValue());

	}


	@Test
	public void testHistory() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t ->
			new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;
		Bundle response;

		response = client
			.history()
			.onServer()
			.returnBundle(Bundle.class)
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.execute();
		assertEquals("http://example.com/fhir/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("myHeaderValue", capt.getValue().getFirstHeader("myHeaderName").getValue());
		Assert.assertEquals(1, response.getEntry().size());
		idx++;

		response = client
			.history()
			.onType(Patient.class)
			.returnBundle(Bundle.class)
			.withAdditionalHeader("myHeaderName", "myHeaderValue1")
			.withAdditionalHeader("myHeaderName", "myHeaderValue2")
			.execute();
		assertEquals("http://example.com/fhir/Patient/_history", capt.getAllValues().get(idx).getURI().toString());
		Assert.assertEquals(1, response.getEntry().size());
		assertEquals("myHeaderValue1", capt.getValue().getHeaders("myHeaderName")[0].getValue());
		assertEquals("myHeaderValue2", capt.getValue().getHeaders("myHeaderName")[1].getValue());
		idx++;

		response = client
			.history()
			.onInstance(new IdType("Patient", "123"))
			.andReturnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient/123/_history", capt.getAllValues().get(idx).getURI().toString());
		Assert.assertEquals(1, response.getEntry().size());
		idx++;
	}

	@Test
	@Ignore
	public void testInvalidCalls() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.meta();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("Can not call $meta operations on a DSTU1 client", e.getMessage());
		}
		try {
			client.operation();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("Operations are only supported in FHIR DSTU2 and later. This client was created using a context configured for DSTU1", e.getMessage());
		}
	}

	@Test
	public void testLoadPageAndReturnDstu1Bundle() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		client
			.loadPage()
			.byUrl("http://example.com/page1")
			.andReturnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/page1", capt.getValue().getURI().toString());
	}

	@Test
	public void testMissing() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return (new ReaderInputStream(new StringReader(getPatientFeedWithOneResult()), StandardCharsets.UTF_8));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));

		client.search().forResource("Patient").where(Patient.NAME.isMissing(true)).returnBundle(Bundle.class).execute();
		assertEquals("http://example.com/fhir/Patient?name%3Amissing=true", capt.getValue().getRequestLine().getUri());

		client.search().forResource("Patient").where(Patient.NAME.isMissing(false)).returnBundle(Bundle.class).execute();
		assertEquals("http://example.com/fhir/Patient?name%3Amissing=false", capt.getValue().getRequestLine().getUri());
	}

	@Test
	public void testProcessMessage() throws IOException {
		Bundle respBundle = new Bundle();
		respBundle.setType(BundleType.MESSAGE);
		String respString = ourCtx.newJsonParser().encodeResourceToString(respBundle);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[0]);

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.MESSAGE);

		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName("content")
			.setResource(bundle);

		int count = 0;
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.operation().onType(MessageHeader.class).named("$process-message").withParameters(parameters).execute();

		assertEquals("http://example.com/fhir/MessageHeader/$process-message", capt.getAllValues().get(count).getURI().toString());
		String requestContent = IOUtils.toString(((HttpPost) capt.getAllValues().get(count)).getEntity().getContent(), Charsets.UTF_8);
		assertThat(requestContent, containsString("{\"resourceType\":\"Parameters\""));
		count++;
	}

	@Test
	public void testRead() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		Header[] headers = new Header[]{
			new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
			new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client
			.read()
			.resource(Patient.class)
			.withId(new IdType("Patient/1234"))
			.execute();

		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));

		Assert.assertEquals("http://foo.com/Patient/123/_history/2333", response.getIdElement().getValue());

		InstantType lm = response.getMeta().getLastUpdatedElement();
		lm.setTimeZoneZulu(true);
		Assert.assertEquals("1995-11-15T04:58:08.000Z", lm.getValueAsString());

	}

	@Test
	public void testReadFluent() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		Header[] headers = new Header[]{
			new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
			new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;

		Patient response = client.read().resource(Patient.class).withId(new IdType("Patient/1234")).execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = (Patient) client.read().resource("Patient").withId("1234").execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = (Patient) client.read().resource("Patient").withId(567L).execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/567", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.read().resource(Patient.class).withIdAndVersion("1234", "22").execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234/_history/22", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.read().resource(Patient.class).withUrl("http://foo/Patient/22").execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertEquals("http://foo/Patient/22", capt.getAllValues().get(count++).getURI().toString());

	}

	@Test
	public void testReadWithAbsoluteUrl() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		Header[] headers = new Header[]{new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
			new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client
			.read()
			.resource(Patient.class)
			.withUrl(new IdType("http://somebase.com/path/to/base/Patient/1234"))
			.execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertEquals("http://somebase.com/path/to/base/Patient/1234", capt.getAllValues().get(0).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client
			.read()
			.resource(Patient.class)
			.withUrl(new IdType("http://somebase.com/path/to/base/Patient/1234/_history/222"))
			.execute();
		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertEquals("http://somebase.com/path/to/base/Patient/1234/_history/222", capt.getAllValues().get(1).getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchAllResources() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forAllResources()
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/?name=james", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchAutomaticallyUsesPost() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		String longValue = StringUtils.leftPad("", 20000, 'B');

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value(longValue))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient/_search", capt.getValue().getURI().toString());

		HttpEntityEnclosingRequestBase enc = (HttpEntityEnclosingRequestBase) capt.getValue();
		UrlEncodedFormEntity ent = (UrlEncodedFormEntity) enc.getEntity();
		String string = IOUtils.toString(ent.getContent());
		ourLog.info(string);
		assertEquals("name=" + longValue, string);
	}

	@Test
	public void testSearchByCompartment() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.withIdAndCompartment("123", "fooCompartment")
			.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://foo/Patient/123/fooCompartment?birthdate=ge2011-01-02", capt.getValue().getURI().toString());

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));

		Assert.assertEquals("PRP1660", BundleUtil.toListOfResourcesOfType(ourCtx, response, Patient.class).get(0).getIdentifier().get(0).getValue());

		try {
			client
				.search()
				.forResource(Patient.class)
				.withIdAndCompartment("", "fooCompartment")
				.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
				.returnBundle(Bundle.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.toString(), containsString("null or empty for compartment"));
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByComposite() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");

		Bundle response = client.search()
			.forResource("Observation")
			.where(Observation.CODE_VALUE_DATE
				.withLeft(Observation.CODE.exactly().code("FOO$BAR"))
				.withRight(Observation.VALUE_DATE.exactly().day("2001-01-01")))
			.returnBundle(Bundle.class)
			.execute();

		Assert.assertEquals("http://foo/Observation?" + Observation.SP_CODE_VALUE_DATE + "=" + UrlUtil.escapeUrlParam("FOO\\$BAR$2001-01-01"), capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByDate() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		@SuppressWarnings("deprecation")
		Bundle response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
			.and(Patient.BIRTHDATE.after().day("2011-01-01"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.sort().ascending(Patient.BIRTHDATE)
			.sort().descending(Patient.NAME)
			.sort().defaultOrder(Patient.ADDRESS)
			.count(123)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=le2012-01-22&birthdate=gt2011-01-01&_include=Patient%3Aorganization&_sort=birthdate%2C-name%2Caddress&_count=123&_format=json",
			capt.getAllValues().get(idx++).getURI().toString());

		response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
			.and(Patient.BIRTHDATE.after().day("2011-01-01"))
			.include(Patient.INCLUDE_ORGANIZATION)
			.sort().ascending(Patient.BIRTHDATE)
			.sort().descending(Patient.NAME)
			.sort().defaultOrder(Patient.ADDRESS)
			.count(123)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=le2012-01-22&birthdate=gt2011-01-01&_include=Patient%3Aorganization&_sort=birthdate%2C-name%2Caddress&_count=123&_format=json",
			capt.getAllValues().get(idx++).getURI().toString());

		response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22").orAfter().day("2020-01-01"))
			.and(Patient.BIRTHDATE.after().day("2011-01-01"))
			.returnBundle(Bundle.class)
			.execute();

		String comma = "%2C";
		assertEquals("http://example.com/fhir/Patient?birthdate=le2012-01-22" + comma + "gt2020-01-01&birthdate=gt2011-01-01&_format=json", capt.getAllValues().get(idx++).getURI().toString());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByNumberExact() throws Exception {

		String msg = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Observation.class)
			.where(Observation.VALUE_QUANTITY.greaterThan().number(123).andUnits("foo", "bar"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Observation?value-quantity=gt123%7Cfoo%7Cbar", capt.getValue().getURI().toString());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByProfile() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.withProfile("http://1")
			.withProfile("http://2")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?_profile=http%3A%2F%2F1&_profile=http%3A%2F%2F2", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByQuantity() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.where(Encounter.LENGTH.exactly().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?length=123%7C%7C", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByReferenceProperty() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.where(Patient.GENERAL_PRACTITIONER.hasChainedProperty(Organization.NAME.matches().value("ORG0")))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?general-practitioner.name=ORG0", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByReferenceSimple() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.GENERAL_PRACTITIONER.hasId("123"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?general-practitioner=123", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchBySecurity() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.withSecurity("urn:foo", "123")
			.withSecurity("urn:bar", "456")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?_security=urn%3Afoo%7C123&_security=urn%3Abar%7C456", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByString() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=james", capt.getValue().getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values("AAA", "BBB", "C,C"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=" + UrlUtil.escapeUrlParam("AAA,BBB,C\\,C"), capt.getAllValues().get(1).getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByStringContains() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().value("FOO"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?name%3Acontains=FOO", capt.getValue().getURI().toString());

		response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().values("FOO", "BAR"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?name%3Acontains=FOO%2CBAR", capt.getValue().getURI().toString());

		response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().values(Arrays.asList("FOO", "BAR")))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?name%3Acontains=FOO%2CBAR", capt.getValue().getURI().toString());

		response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.contains().value(new StringType("FOO")))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?name%3Acontains=FOO", capt.getValue().getURI().toString());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByStringExact() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name%3Aexact=james", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByTag() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.withTag("urn:foo", "123")
			.withTag("urn:bar", "456")
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?_tag=urn%3Afoo%7C123&_tag=urn%3Abar%7C456", capt.getValue().getURI().toString());
		assertEquals("myHeaderValue", capt.getValue().getFirstHeader("myHeaderName").getValue());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByToken() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("http://example.com/fhir", "ZZZ"))
			.withAdditionalHeader("myHeaderName", "myHeaderValue")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?identifier=http%3A%2F%2Fexample.com%2Ffhir%7CZZZ", capt.getValue().getURI().toString());
		assertEquals("myHeaderValue", capt.getValue().getFirstHeader("myHeaderName").getValue());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().code("ZZZ"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?identifier=ZZZ", capt.getAllValues().get(1).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().codings(new Coding("A", "B", "ZZZ"), new Coding("C", "D", "ZZZ")))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?identifier=" + UrlUtil.escapeUrlParam("A|B,C|D"), capt.getAllValues().get(2).getURI().toString());

	}

	/**
	 * Test for #192
	 */
	@SuppressWarnings("unused")
	@Test
	public void testSearchByTokenWithEscaping() throws Exception {
		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		int index = 0;
		String wantPrefix = "http://foo/Patient?identifier=";

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("1", "2"))
			.returnBundle(Bundle.class)
			.execute();
		String wantValue = "1|2";
		String url = capt.getAllValues().get(index).getURI().toString();
		assertThat(url, Matchers.startsWith(wantPrefix));
		assertEquals(wantValue, UrlUtil.unescape(url.substring(wantPrefix.length())));
		assertEquals(UrlUtil.escapeUrlParam(wantValue), url.substring(wantPrefix.length()));
		index++;

		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("1,2", "3,4"))
			.returnBundle(Bundle.class)
			.execute();
		wantValue = "1\\,2|3\\,4";
		url = capt.getAllValues().get(index).getURI().toString();
		assertThat(url, Matchers.startsWith(wantPrefix));
		assertEquals(wantValue, UrlUtil.unescape(url.substring(wantPrefix.length())));
		assertEquals(UrlUtil.escapeUrlParam(wantValue), url.substring(wantPrefix.length()));
		index++;
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByTokenWithSystemAndNoCode() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.hasSystemWithAnyCode("urn:foo"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C", capt.getAllValues().get(idx++).getURI().toString());

		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:foo", null))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C", capt.getAllValues().get(idx++).getURI().toString());

		response = client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:foo", ""))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C", capt.getAllValues().get(idx++).getURI().toString());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchIncludeRecursive() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.include(Patient.INCLUDE_ORGANIZATION)
			.include(Patient.INCLUDE_LINK.asRecursive())
			.include(IBaseResource.INCLUDE_ALL.asNonRecursive())
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString(), containsString("http://example.com/fhir/Patient?"));
		assertThat(capt.getValue().getURI().toString(), containsString("_include=" + UrlUtil.escapeUrlParam(Patient.INCLUDE_ORGANIZATION.getValue())));
		assertThat(capt.getValue().getURI().toString(), containsString("_include%3Aiterate=" + UrlUtil.escapeUrlParam(Patient.INCLUDE_LINK.getValue())));
		assertThat(capt.getValue().getURI().toString(), containsString("_include=*"));

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchUsingGetSearch() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.usingStyle(SearchStyleEnum.GET_WITH_SEARCH)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient/_search?name=james", capt.getValue().getURI().toString());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchUsingPost() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.usingStyle(SearchStyleEnum.POST)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient/_search", capt.getValue().getURI().toString());

		HttpEntityEnclosingRequestBase enc = (HttpEntityEnclosingRequestBase) capt.getValue();
		UrlEncodedFormEntity ent = (UrlEncodedFormEntity) enc.getEntity();
		String string = IOUtils.toString(ent.getContent());
		ourLog.info(string);
		assertEquals("name=james", string);
	}

	@Test
	public void testSearchWithAbsoluteUrl() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client
			.search()
			.byUrl("http://example.com/fhir/Patient?birthdate=le2012-01-22&birthdate=gt2011-01-01&_include=Patient%3Aorganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_count=123&_format=json")
			.returnBundle(Bundle.class)
			.execute();

		Assert.assertEquals(1, response.getEntry().size());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithClientEncodingAndPrettyPrintConfig() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);

		Bundle response = client.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?_format=json&_pretty=true", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithEscapedParameters() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values("NE,NE", "NE,NE"))
			.where(Patient.NAME.matchesExactly().values("E$E"))
			.where(Patient.NAME.matches().values("NE\\NE"))
			.where(Patient.NAME.matchesExactly().values("E|E"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString(), containsString("%3A"));
		assertEquals("http://example.com/fhir/Patient?name=NE\\,NE,NE\\,NE&name=NE\\\\NE&name:exact=E\\$E&name:exact=E\\|E", UrlUtil.unescape(capt.getValue().getURI().toString()));
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithInternalServerError() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "INTERNAL ERRORS"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Server Issues!"), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client
				.search()
				.forResource(Patient.class)
				.returnBundle(Bundle.class)
				.execute();
			fail();
		} catch (InternalErrorException e) {
			assertEquals(e.getMessage(), "HTTP 500 INTERNAL ERRORS: Server Issues!");
			assertEquals(e.getResponseBody(), "Server Issues!");
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithNonFhirResponse() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Server Issues!"), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
			fail();
		} catch (NonFhirResponseException e) {
			assertThat(e.getMessage(), StringContains.containsString("Server Issues!"));
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithReverseInclude() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.revInclude(Provenance.INCLUDE_TARGET)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?_revinclude=Provenance%3Atarget&_format=json", capt.getValue().getURI().toString());

	}

	@Test
	public void testSetDefaultEncoding() throws Exception {

		String msg = ourCtx.newJsonParser().encodeResourceToString(new Patient());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		// Header[] headers = new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08
		// GMT"),
		// new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		// new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\";
		// label=\"Some tag\"") };
		// when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		(client).setEncoding(EncodingEnum.JSON);
		int count = 0;

		client
			.read()
			.resource(Patient.class)
			.withId(new IdType("Patient/1234"))
			.execute();
		assertEquals("http://example.com/fhir/Patient/1234?_format=json", capt.getAllValues().get(count).getURI().toString());
		count++;

	}

	@Test
	public void testTransactionJson() throws Exception {
		Bundle input = createTransactionBundleInput();
		Bundle output = createTransactionBundleOutput();

		String msg = ourCtx.newJsonParser().encodeResourceToString(output);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.transaction()
			.withBundle(input)
			.execute();

		assertEquals("http://example.com/fhir", capt.getValue().getURI().toString());
		Assert.assertEquals(input.getEntry().get(0).getResource().getId(), response.getEntry().get(0).getResource().getId());
		assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(0).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());

	}

	@Test
	public void testTransactionXml() throws Exception {
		Bundle input = createTransactionBundleInput();
		Bundle output = createTransactionBundleOutput();

		String msg = ourCtx.newXmlParser().encodeResourceToString(output);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.transaction()
			.withBundle(input)
			.encodedXml()
			.execute();

		assertEquals("http://example.com/fhir", capt.getValue().getURI().toString());
		Assert.assertEquals(input.getEntry().get(0).getResource().getId(), response.getEntry().get(0).getResource().getId());
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(0).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());

	}

	@Test
	public void testUpdate() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo:bar").setValue("12345");
		p1.addName().setFamily("Smith").addGiven("John");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.update().resource(p1).execute();
			fail();
		} catch (InvalidRequestException e) {
			// should happen because no ID set
		}

		assertEquals(0, capt.getAllValues().size());

		p1.setId("44");
		client.update().resource(p1).execute();

		int count = 0;

		assertEquals(1, capt.getAllValues().size());
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		count++;

		MethodOutcome outcome = client.update().resource(p1).execute();
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		assertEquals(2, capt.getAllValues().size());

		assertEquals("http://example.com/fhir/Patient/44", capt.getValue().getURI().toString());
		assertEquals("PUT", capt.getValue().getMethod());

		/*
		 * Try fluent options
		 */
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));
		client.update().resource(p1).withId("123").execute();
		assertEquals(3, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(2).getURI().toString());

		String resourceText = "<Patient xmlns=\"http://hl7.org/fhir\">    </Patient>";
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));
		client.update().resource(resourceText).withId("123").execute();
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(3).getURI().toString());
		assertEquals(resourceText, IOUtils.toString(((HttpPut) capt.getAllValues().get(3)).getEntity().getContent()));
		assertEquals(4, capt.getAllValues().size());

	}

	@Test
	public void testUpdateWithStringAutoDetectsEncoding() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo:bar").setValue("12345");
		p1.addName().setFamily("Smith").addGiven("John");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22")});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).withId("1").execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("value=\"John\""));
		count++;

		client.update().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).withId("1").execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("[\"John\"]"));
		count++;

		/*
		 * e.g. Now try with reversed encoding (provide a string that's in JSON and ask the client to use XML)
		 */

		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).withId("1").encodedJson().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("[\"John\"]"));
		count++;

		client.update().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).withId("1").encodedXml().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentTypeNonLegacy() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("value=\"John\""));
		count++;
	}

	@Test
	public void testVReadWithAbsoluteUrl() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));
		Header[] headers = new Header[]{
			new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
			new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client
			.read()
			.resource(Patient.class)
			.withUrl("http://somebase.com/path/to/base/Patient/1234/_history/2222")
			.execute();

		assertThat(response.getNameFirstRep().getFamily(), StringContains.containsString("Cardinal"));
		assertEquals("http://somebase.com/path/to/base/Patient/1234/_history/2222", capt.getAllValues().get(0).getURI().toString());

	}

	@Test
	public void testValidateNonFluent() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("OOOK");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ourCtx.newXmlParser().encodeResourceToString(oo)), StandardCharsets.UTF_8));
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("foo:bar").setValue("12345");
		p1.addName().setFamily("Smith").addGiven("John");

		MethodOutcome resp = client.validate(p1);
		assertEquals("http://example.com/fhir/Patient/$validate", capt.getValue().getURI().toString());
		oo = (OperationOutcome) resp.getOperationOutcome();
		Assert.assertEquals("OOOK", oo.getIssueFirstRep().getDiagnostics());

	}

	 private String getPatientFeedWithOneResult() {

		Bundle retVal = new Bundle();

		Patient p = new Patient();
		p.addName().setFamily("Cardinal").addGiven("John");
		p.addIdentifier().setValue("PRP1660");
		retVal.addEntry().setResource(p);

		return ourCtx.newXmlParser().encodeResourceToString(retVal);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forR5();
	}

}
