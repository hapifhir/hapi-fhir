package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Provenance;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.method.SearchStyleEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;

public class GenericClientTest {

	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientTest.class);
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

	private String extractBody(ArgumentCaptor<HttpUriRequest> capt, int count) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(count)).getEntity().getContent(), "UTF-8");
		return body;
	}

	@Test
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
	
	private String getPatientFeedWithOneResult() {
		//@formatter:off
		String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
				"<title/>\n" + 
				"<id>d039f91a-cc3c-4013-988e-af4d8d0614bd</id>\n" + 
				"<os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">1</os:totalResults>\n" + 
				"<author>\n" +
				"<name>ca.uhn.fhir.rest.server.DummyRestfulServer</name>\n" + 
				"</author>\n" + 
				"<entry>\n" + 
				"<content type=\"text/xml\">" 
				+ "<Patient xmlns=\"http://hl7.org/fhir\">" 
				+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div></text>"
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
				+ "<name><family value=\"Kramer\" /><given value=\"Doe\" /></name>"
				+ "<telecom><system value=\"phone\" /><value value=\"555-555-2004\" /><use value=\"work\" /></telecom>"
				+ "<gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\" /><code value=\"M\" /></coding></gender>"
				+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
				+ "</Patient>"
				+ "</content>\n"  
				+ "   </entry>\n"  
				+ "</feed>";
		//@formatter:on
		return msg;
	}

	private String getResourceResult() {
		//@formatter:off
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
		//@formatter:on
		return msg;
	}

	@Test
	public void testCreatePopulatesIsCreated() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		MethodOutcome resp = client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertTrue(resp.getCreated());

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		resp = client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertNull(resp.getCreated());

		ourLog.info("lastRequest: {}", ((GenericClient)client).getLastRequest());
		ourLog.info("lastResponse: {}", ((GenericClient)client).getLastResponse());
		ourLog.info("lastResponseBody: {}", ((GenericClient)client).getLastResponseBody());
	}

	@Test
	public void testCreateWithStringAutoDetectsEncoding() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("value=\"John\""));
		count++;

		String resourceAsString = ourCtx.newJsonParser().encodeResourceToString(p1);
		client
			.create()
			.resource(resourceAsString)
			.execute();
		
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("[\"John\"]"));
		count++;

		/*
		 * e.g. Now try with reversed encoding (provide a string that's in JSON and ask the client to use XML)
		 */

		client.create().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).encodedJson().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("[\"John\"]"));
		count++;

		client.create().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).encodedXml().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("value=\"John\""));
		count++;

	}
	@Test
	public void testCreateWithTag() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");
		TagList list = new TagList();
		list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
		ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client.create().resource(p1).execute();
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		int count = 0;

		assertEquals("http://example.com/fhir/Patient", capt.getValue().getURI().toString());
		assertEquals("POST", capt.getValue().getMethod());
		Header catH = capt.getValue().getFirstHeader("Category");
		assertNotNull(Arrays.asList(capt.getValue().getAllHeaders()).toString(), catH);
		assertEquals("urn:happytag; label=\"This is a happy resource\"; scheme=\"http://hl7.org/fhir/tag\"", catH.getValue());
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		count++;

		/*
		 * Try fluent options
		 */
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		client.create().resource(p1).withId("123").execute();
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(1).getURI().toString());
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		count++;

		String resourceText = "<Patient xmlns=\"http://hl7.org/fhir\">    </Patient>";
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		client.create().resource(resourceText).withId("123").execute();
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(2).getURI().toString());
		assertEquals(resourceText, IOUtils.toString(((HttpPost) capt.getAllValues().get(2)).getEntity().getContent()));
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		count++;
	}

	@Test
	public void testCreateWithTagNonFluent() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");
		TagList list = new TagList();
		list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
		ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client.create().resource(p1).execute();
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		assertEquals("http://example.com/fhir/Patient", capt.getValue().getURI().toString());
		assertEquals("POST", capt.getValue().getMethod());
		Header catH = capt.getValue().getFirstHeader("Category");
		assertNotNull(Arrays.asList(capt.getValue().getAllHeaders()).toString(), catH);
		assertEquals("urn:happytag; label=\"This is a happy resource\"; scheme=\"http://hl7.org/fhir/tag\"", catH.getValue());
	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testCreateWithUtf8Characters() throws Exception {
		String name = "測試醫院";
		Organization org = new Organization();
		org.setName(name);
		org.addIdentifier("urn:system", "testCreateWithUtf8Characters_01");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.create().resource(org).prettyPrint().encodedXml().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("<name value=\"測試醫院\"/>"));
		count++;

	}

	@Test
	public void testDelete() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().addLocation().setValue("testDelete01");
		String ooStr = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ooStr), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		OperationOutcome outcome = (OperationOutcome) client.delete().resourceById("Patient", "123").execute();

		assertEquals("http://example.com/fhir/Patient/123", capt.getValue().getURI().toString());
		assertEquals("DELETE", capt.getValue().getMethod());
		assertEquals("testDelete01", outcome.getIssueFirstRep().getLocationFirstRep().getValue());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("LKJHLKJGLKJKLL"), Charset.forName("UTF-8")));
		outcome = (OperationOutcome) client.delete().resourceById(new IdDt("Location", "123", "456")).prettyPrint().encodedJson().execute();

		assertEquals("http://example.com/fhir/Location/123?_pretty=true", capt.getAllValues().get(1).getURI().toString());
		assertEquals("DELETE", capt.getValue().getMethod());
		assertEquals(null, outcome);

	}

	@Test
	public void testGetTags() throws Exception {

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));
		String msg = ourCtx.newXmlParser().encodeTagListToString(tagList);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		TagList response = client.getTags()
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/_tags", capt.getValue().getURI().toString());
		assertEquals(1, response.size());
		assertEquals("CCC", response.get(0).getScheme());

		// Now for patient

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		response = client.getTags().forResource(Patient.class)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient/_tags", capt.getValue().getURI().toString());
		assertEquals(1, response.size());
		assertEquals("CCC", response.get(0).getScheme());

	}

	@Test
	public void testHistory() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;
		Bundle response;

		//@formatter:off
		response = client
				.history()
				.onServer()
				.andReturnDstu1Bundle()
				.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals(1, response.size());
		idx++;

		//@formatter:off
		response = client
				.history()
				.onType(Patient.class)
				.andReturnDstu1Bundle()
				.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals(1, response.size());
		idx++;

		//@formatter:off
		response = client
				.history()
				.onInstance(new IdDt("Patient", "123"))
				.andReturnDstu1Bundle()
				.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/123/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals(1, response.size());
		idx++;
	}

	@Test
	public void testMissing() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return (new ReaderInputStream(new StringReader(getPatientFeedWithOneResult()), Charset.forName("UTF-8")));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));

		client.search().forResource("Patient").where(Patient.NAME.isMissing(true)).execute();
		assertEquals("http://example.com/fhir/Patient?name%3Amissing=true", capt.getValue().getRequestLine().getUri());

		client.search().forResource("Patient").where(Patient.NAME.isMissing(false)).execute();
		assertEquals("http://example.com/fhir/Patient?name%3Amissing=false", capt.getValue().getRequestLine().getUri());
	}

	@Test
	public void testRead() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		Header[] headers = new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"), new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
				new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\"; label=\"Some tag\"") };
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Patient response = client.read(Patient.class, new IdDt("Patient/1234"));
		//@formatter:on

		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));

		assertEquals("http://foo.com/Patient/123/_history/2333", response.getId().getValue());

		InstantDt lm = (InstantDt) response.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		lm.setTimeZoneZulu(true);
		assertEquals("1995-11-15T04:58:08.000Z", lm.getValueAsString());

		TagList tags = ResourceMetadataKeyEnum.TAG_LIST.get(response);
		assertNotNull(tags);
		assertEquals(1, tags.size());
		assertEquals("http://foo/tagdefinition.html", tags.get(0).getTerm());
		assertEquals("http://hl7.org/fhir/tag", tags.get(0).getScheme());
		assertEquals("Some tag", tags.get(0).getLabel());

	}

	@Test
	public void testReadFluent() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		Header[] headers = new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"), new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
				new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\"; label=\"Some tag\"") };
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;

		Patient response = client.read().resource(Patient.class).withId(new IdDt("Patient/1234")).execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		response = (Patient) client.read().resource("Patient").withId("1234").execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		response = (Patient) client.read().resource("Patient").withId(567L).execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/567", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		response = client.read().resource(Patient.class).withIdAndVersion("1234", "22").execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234/_history/22", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		response = client.read().resource(Patient.class).withUrl("http://foo/Patient/22").execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://foo/Patient/22", capt.getAllValues().get(count++).getURI().toString());

	}

	@Test
	public void testReadWithAbsoluteUrl() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		Header[] headers = new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"), new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
				new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\"; label=\"Some tag\"") };
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client.read(Patient.class, new IdDt("http://somebase.com/path/to/base/Patient/1234"));
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://somebase.com/path/to/base/Patient/1234", capt.getAllValues().get(0).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		response = client.read(Patient.class, new IdDt("http://somebase.com/path/to/base/Patient/1234/_history/2222"));
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://somebase.com/path/to/base/Patient/1234", capt.getAllValues().get(1).getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchAllResources() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forAllResources()
				.where(Patient.NAME.matches().value("james"))
				.execute();
		//@formatter:on

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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		String longValue = StringUtils.leftPad("", 20000, 'B');

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value(longValue))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient/_search", capt.getValue().getURI().toString());

		HttpEntityEnclosingRequestBase enc = (HttpEntityEnclosingRequestBase) capt.getValue();
		UrlEncodedFormEntity ent = (UrlEncodedFormEntity) enc.getEntity();
		String string = IOUtils.toString(ent.getContent());
		ourLog.info(string);
		assertEquals("name=" + longValue, string);
	}

	@Test
	public void testLoadPageAndReturnDstu1Bundle() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		//@formatter:off
		client
			.loadPage()
			.byUrl("http://example.com/page1")
			.andReturnDstu1Bundle()
			.execute();
		//@formatter:on

		assertEquals("http://example.com/page1", capt.getValue().getURI().toString());
	}
	
	
	@Test
	public void testSearchByCompartment() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		//@formatter:off
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.withIdAndCompartment("123", "fooCompartment")
			.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
			.execute();
		//@formatter:on

		assertEquals("http://foo/Patient/123/fooCompartment?birthdate=%3E%3D2011-01-02", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getResources(Patient.class).get(0).getIdentifier().get(0).getValue().getValue());

		try {
			//@formatter:off
			client
				.search()
				.forResource(Patient.class)
				.withIdAndCompartment("", "fooCompartment")
				.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
				.execute();
			//@formatter:on
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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Observation")
				.where(Observation.NAME_VALUE_DATE
						.withLeft(Observation.NAME.exactly().code("FOO$BAR"))
						.withRight(Observation.VALUE_DATE.exactly().day("2001-01-01"))
					  )
				.execute();
		//@formatter:on

		assertEquals("http://foo/Observation?" + Observation.SP_NAME_VALUE_DATE + "=" + URLEncoder.encode("FOO\\$BAR$2001-01-01", "UTF-8"), capt.getValue().getURI().toString());

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
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;
		
		//@formatter:off
		@SuppressWarnings("deprecation")
		Bundle response = client.search()
				.forResource(Patient.class)
				.encodedJson()
				.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
				.and(Patient.BIRTHDATE.after().day("2011-01-01"))
				.include(Patient.INCLUDE_MANAGINGORGANIZATION)
				.sort().ascending(Patient.BIRTHDATE)
				.sort().descending(Patient.NAME)
				.sort().defaultOrder(Patient.ADDRESS)
				.limitTo(123)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?birthdate=%3C%3D2012-01-22&birthdate=%3E2011-01-01&_include=Patient.managingOrganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_sort=address&_count=123&_format=json", capt.getAllValues().get(idx++).getURI().toString());

		//@formatter:off
		response = client.search()
				.forResource(Patient.class)
				.encodedJson()
				.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
				.and(Patient.BIRTHDATE.after().day("2011-01-01"))
				.include(Patient.INCLUDE_MANAGINGORGANIZATION)
				.sort().ascending(Patient.BIRTHDATE)
				.sort().descending(Patient.NAME)
				.sort().defaultOrder(Patient.ADDRESS)
				.count(123)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?birthdate=%3C%3D2012-01-22&birthdate=%3E2011-01-01&_include=Patient.managingOrganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_sort=address&_count=123&_format=json", capt.getAllValues().get(idx++).getURI().toString());

		
		//@formatter:off
		response = client.search()
				.forResource(Patient.class)
				.encodedJson()
				.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22").orAfter().day("2020-01-01"))
				.and(Patient.BIRTHDATE.after().day("2011-01-01"))
				.execute();
		//@formatter:on

		String comma = "%2C";
		assertEquals("http://example.com/fhir/Patient?birthdate=%3C%3D2012-01-22" + comma + "%3E2020-01-01&birthdate=%3E2011-01-01&_format=json", capt.getAllValues().get(idx++).getURI().toString());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testSearchByNumberExact() throws Exception {

		String msg = ourCtx.newXmlParser().encodeBundleToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Observation.class)
				.where(Observation.VALUE_QUANTITY.greaterThan().number(123).andUnits("foo", "bar"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Observation?value-quantity=%3E123%7Cfoo%7Cbar", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByProfile() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.withProfile("http://1")
				.withProfile("http://2")
				.execute();
		//@formatter:on

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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.where(Encounter.LENGTH.exactly().number(123))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?length=123", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByReferenceProperty() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		//@formatter:off
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
				.forResource(Patient.class)
				.where(Patient.PROVIDER.hasChainedProperty(Organization.NAME.matches().value("ORG0")))
				.execute();

		assertEquals("http://example.com/fhir/Patient?provider.name=ORG0", capt.getValue().getURI().toString());
		//@formatter:on

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByReferenceSimple() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.PROVIDER.hasId("123"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?provider=123", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchBySecurity() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.withSecurity("urn:foo", "123")
				.withSecurity("urn:bar", "456")
				.execute();
		//@formatter:on

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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value("james"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?name=james", capt.getValue().getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().values("AAA", "BBB", "C,C"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?name=" + URLEncoder.encode("AAA,BBB,C\\,C", "UTF-8"), capt.getAllValues().get(1).getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByStringExact() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matchesExactly().value("james"))
				.execute();
		//@formatter:on

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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.withTag("urn:foo", "123")
				.withTag("urn:bar", "456")
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?_tag=urn%3Afoo%7C123&_tag=urn%3Abar%7C456", capt.getValue().getURI().toString());

	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void testSearchByToken() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().systemAndCode("http://example.com/fhir", "ZZZ"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?identifier=http%3A%2F%2Fexample.com%2Ffhir%7CZZZ", capt.getValue().getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().code("ZZZ"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?identifier=ZZZ", capt.getAllValues().get(1).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().identifiers(new ca.uhn.fhir.model.dstu.composite.IdentifierDt("A", "B"), new ca.uhn.fhir.model.dstu.composite.IdentifierDt("C", "D")))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?identifier=" + URLEncoder.encode("A|B,C|D", "UTF-8"), capt.getAllValues().get(2).getURI().toString());

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
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;
		
		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.hasSystemWithAnyCode("urn:foo"))
				.execute();
		assertEquals("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C", capt.getAllValues().get(idx++).getURI().toString());
		//@formatter:on

		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:foo", null))
				.execute();
		assertEquals("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C", capt.getAllValues().get(idx++).getURI().toString());
		//@formatter:on

		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().systemAndCode("urn:foo", ""))
				.execute();
		assertEquals("http://example.com/fhir/Patient?identifier=urn%3Afoo%7C", capt.getAllValues().get(idx++).getURI().toString());
		//@formatter:on
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
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		int index = 0;
		String wantPrefix = "http://foo/Patient?identifier=";

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().systemAndCode("1", "2"))
				.execute();
		String wantValue = "1|2";
		String url = capt.getAllValues().get(index).getURI().toString();
		assertThat(url, Matchers.startsWith(wantPrefix));
		assertEquals(wantValue, UrlUtil.unescape(url.substring(wantPrefix.length())));
		assertEquals(UrlUtil.escape(wantValue), url.substring(wantPrefix.length()));
		index++;
		//@formatter:on

		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().systemAndCode("1,2", "3,4"))
				.execute();
		wantValue = "1\\,2|3\\,4";
		url = capt.getAllValues().get(index).getURI().toString();
		assertThat(url, Matchers.startsWith(wantPrefix));
		assertEquals(wantValue, UrlUtil.unescape(url.substring(wantPrefix.length())));
		assertEquals(UrlUtil.escape(wantValue), url.substring(wantPrefix.length()));
		index++;
		//@formatter:on

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchIncludeRecursive() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.include(Patient.INCLUDE_MANAGINGORGANIZATION)
				.include(Patient.INCLUDE_LINK_OTHER.asRecursive())
				.include(Patient.INCLUDE_ALL.asNonRecursive())
				.execute();
		//@formatter:on

		assertThat(capt.getValue().getURI().toString(), containsString("http://example.com/fhir/Patient?"));
		assertThat(capt.getValue().getURI().toString(), containsString("_include=" + Patient.INCLUDE_MANAGINGORGANIZATION.getValue()));
		assertThat(capt.getValue().getURI().toString(), containsString("_include%3Arecurse=" + Patient.INCLUDE_LINK_OTHER.getValue()));
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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value("james"))
				.usingStyle(SearchStyleEnum.GET_WITH_SEARCH)
				.execute();
		//@formatter:on

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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value("james"))
				.usingStyle(SearchStyleEnum.POST)
				.execute();
		//@formatter:on

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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search(new UriDt("http://example.com/fhir/Patient?birthdate=%3C%3D2012-01-22&birthdate=%3E2011-01-01&_include=Patient.managingOrganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_count=123&_format=json"));

		assertEquals("http://example.com/fhir/Patient?birthdate=%3C%3D2012-01-22&birthdate=%3E2011-01-01&_include=Patient.managingOrganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_count=123&_format=json", capt.getValue().getURI().toString());

		assertEquals(1, response.size());
	}

	@Test
	public void testSearchWithAbsoluteUrlAndType() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search(Patient.class, new UriDt("http://example.com/fhir/Patient?birthdate=%3C%3D2012-01-22&birthdate=%3E2011-01-01&_include=Patient.managingOrganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_count=123&_format=json"));

		assertEquals("http://example.com/fhir/Patient?birthdate=%3C%3D2012-01-22&birthdate=%3E2011-01-01&_include=Patient.managingOrganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_count=123&_format=json", capt.getValue().getURI().toString());

		assertEquals(1, response.size());
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithClientEncodingAndPrettyPrintConfig() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		GenericClient client = (GenericClient) ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.execute();
		//@formatter:on

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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().values("NE,NE", "NE,NE"))
				.where(Patient.NAME.matchesExactly().values("E$E"))
				.where(Patient.NAME.matches().values("NE\\NE"))
				.where(Patient.NAME.matchesExactly().values("E|E"))
				.execute();
		//@formatter:on

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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Server Issues!"), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.search().forResource(Patient.class).execute();
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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Server Issues!"), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.search().forResource(Patient.class).execute();
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
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.encodedJson()
				.revInclude(Provenance.INCLUDE_TARGET)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?_revinclude=Provenance.target&_format=json", capt.getValue().getURI().toString());

	}

	@Test
	public void testSetDefaultEncoding() throws Exception {

		String msg = ourCtx.newJsonParser().encodeResourceToString(new Patient());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		// Header[] headers = new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08
		// GMT"),
		// new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		// new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\";
		// label=\"Some tag\"") };
		// when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		(client).setEncoding(EncodingEnum.JSON);
		int count = 0;

		client.read(Patient.class, new IdDt("Patient/1234"));
		assertEquals("http://example.com/fhir/Patient/1234?_format=json", capt.getAllValues().get(count).getURI().toString());
		count++;

	}

	@Test
	public void testTransaction() throws Exception {
		String bundleStr = IOUtils.toString(getClass().getResourceAsStream("/bundle.json"));
		Bundle bundle = ourCtx.newJsonParser().parseBundle(bundleStr);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(bundleStr), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.transaction()
				.withBundle(bundle)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir", capt.getValue().getURI().toString());
		assertEquals(bundle.getEntries().get(0).getResource().getId(), response.getEntries().get(0).getResource().getId());
		assertEquals(EncodingEnum.XML.getBundleContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(0).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testTransactionOldStyle() throws Exception {
		String bundleStr = IOUtils.toString(getClass().getResourceAsStream("/bundle.json"));
		Bundle bundle = ourCtx.newJsonParser().parseBundle(bundleStr);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(bundleStr), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		List<IBaseResource> input = new ArrayList<IBaseResource>();
		input.addAll(bundle.toListOfResources());
		client.transaction(input);
	}

	@Test
	public void testTransactionJson() throws Exception {
		String bundleStr = IOUtils.toString(getClass().getResourceAsStream("/bundle.json"));
		Bundle bundle = ourCtx.newJsonParser().parseBundle(bundleStr);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(bundleStr), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.transaction()
				.withBundle(bundle)
				.encodedJson()
				.execute();
		//@formatter:on

		HttpEntityEnclosingRequestBase value = (HttpEntityEnclosingRequestBase) capt.getValue();

		Header ct = value.getFirstHeader(Constants.HEADER_CONTENT_TYPE);
		assertNotNull(ct);
		assertEquals(Constants.CT_FHIR_JSON + "; charset=UTF-8", ct.getValue());

		assertEquals("http://example.com/fhir", value.getURI().toString());
		assertThat(IOUtils.toString(value.getEntity().getContent()), StringContains.containsString("\"resourceType\""));
		assertEquals(bundle.getEntries().get(0).getResource().getId(), response.getEntries().get(0).getResource().getId());
	}

	@Test
	public void testUpdate() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");
		TagList list = new TagList();
		list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
		ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

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
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		count++;

		MethodOutcome outcome = client.update().resource(p1).execute();
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		assertEquals(2, capt.getAllValues().size());

		assertEquals("http://example.com/fhir/Patient/44", capt.getValue().getURI().toString());
		assertEquals("PUT", capt.getValue().getMethod());
		Header catH = capt.getValue().getFirstHeader("Category");
		assertNotNull(Arrays.asList(capt.getValue().getAllHeaders()).toString(), catH);
		assertEquals("urn:happytag; label=\"This is a happy resource\"; scheme=\"http://hl7.org/fhir/tag\"", catH.getValue());
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());

		/*
		 * Try fluent options
		 */
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		client.update().resource(p1).withId("123").execute();
		assertEquals(3, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(2).getURI().toString());

		String resourceText = "<Patient xmlns=\"http://hl7.org/fhir\">    </Patient>";
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		client.update().resource(resourceText).withId("123").execute();
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(3).getURI().toString());
		assertEquals(resourceText, IOUtils.toString(((HttpPut) capt.getAllValues().get(3)).getEntity().getContent()));
		assertEquals(4, capt.getAllValues().size());

	}

	@Test
	public void testUpdateWithStringAutoDetectsEncoding() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;
		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).withId("1").execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("value=\"John\""));
		count++;

		client.update().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).withId("1").execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("[\"John\"]"));
		count++;

		/*
		 * e.g. Now try with reversed encoding (provide a string that's in JSON and ask the client to use XML)
		 */

		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p1)).withId("1").encodedJson().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.JSON.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("[\"John\"]"));
		count++;

		client.update().resource(ourCtx.newJsonParser().encodeResourceToString(p1)).withId("1").encodedXml().execute();
		assertEquals(1, capt.getAllValues().get(count).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(count).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, count), containsString("value=\"John\""));
		count++;
	}

	@Test
	public void testValidateNonFluent() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDetails("OOOK");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ourCtx.newXmlParser().encodeResourceToString(oo)), Charset.forName("UTF-8")));
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");
		
		MethodOutcome resp = client.validate(p1);
		assertEquals("http://example.com/fhir/Patient/_validate", capt.getValue().getURI().toString());
		oo = (OperationOutcome) resp.getOperationOutcome();
		assertEquals("OOOK", oo.getIssueFirstRep().getDetails().getValue());

	}

	@Test
	public void testVReadWithAbsoluteUrl() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		Header[] headers = new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"), new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
				new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\"; label=\"Some tag\"") };
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client.vread(Patient.class, new IdDt("http://somebase.com/path/to/base/Patient/1234/_history/2222"));
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://somebase.com/path/to/base/Patient/1234/_history/2222", capt.getAllValues().get(0).getURI().toString());

		try {
			client.vread(Patient.class, new IdDt("http://somebase.com/path/to/base/Patient/1234"));
			fail();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), containsString("No version specified in URL"));
		}

	}

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu1();
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
