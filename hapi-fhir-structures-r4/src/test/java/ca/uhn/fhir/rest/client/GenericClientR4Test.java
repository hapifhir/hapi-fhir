package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.CustomTypeR4Test;
import ca.uhn.fhir.parser.CustomTypeR4Test.MyCustomPatient;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.impl.BaseClient;
import ca.uhn.fhir.rest.client.interceptor.CookieInterceptor;
import ca.uhn.fhir.rest.client.interceptor.UserInfoInterceptor;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.VersionUtil;
import com.google.common.base.Charsets;
import com.helger.commons.io.stream.StringInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericClientR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientR4Test.class);
	private static FhirContext ourCtx;
	private int myAnswerCount;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@Before
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
		myAnswerCount = 0;
		System.setProperty(BaseClient.HAPI_CLIENT_KEEPRESPONSES, "true");
	}

	private String expectedUserAgent() {
		return "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client; FHIR " + FhirVersionEnum.R4.getFhirVersionString() + "/R4; apache)";
	}

	private byte[] extractBodyAsByteArray(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		byte[] body = IOUtils.toByteArray(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent());
		return body;
	}

	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent(), "UTF-8");
		return body;
	}

	private ArgumentCaptor<HttpUriRequest> prepareClientForSearchResponse() throws IOException {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});
		return capt;
	}

	@Test
	public void testAcceptHeaderCustom() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		// Custom accept value

		client.setEncoding(EncodingEnum.XML);
		client.search()
			.forResource("Device")
			.returnBundle(Bundle.class)
			.accept("application/json")
			.execute();
		assertEquals("http://example.com/fhir/Device", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("application/json", capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue());
		idx++;

		// Empty accept value

		client.setEncoding(EncodingEnum.XML);
		client.search()
			.forResource("Device")
			.returnBundle(Bundle.class)
			.accept("")
			.execute();
		assertEquals("http://example.com/fhir/Device?_format=xml", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_NON_LEGACY, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue());
		idx++;

		// Null accept value

		client.setEncoding(EncodingEnum.XML);
		client.search()
			.forResource("Device")
			.returnBundle(Bundle.class)
			.accept(null)
			.execute();
		assertEquals("http://example.com/fhir/Device?_format=xml", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_NON_LEGACY, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue());
		idx++;
	}

	@Test
	public void testBinaryCreateWithFhirContentType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		Binary bin = new Binary();
		bin.setContent(ourCtx.newJsonParser().encodeResourceToString(pt).getBytes("UTF-8"));
		bin.setContentType(Constants.CT_FHIR_JSON);
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Binary", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/fhir+json;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		Binary output = ourCtx.newJsonParser().parseResource(Binary.class, extractBodyAsString(capt));
		assertEquals(Constants.CT_FHIR_JSON, output.getContentType());

		Patient outputPt = (Patient) ourCtx.newJsonParser().parseResource(new String(output.getContent(), "UTF-8"));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">A PATIENT</div>", outputPt.getText().getDivAsString());
	}

	@Test
	public void testBinaryCreateWithNoContentType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContent(new byte[]{0, 1, 2, 3, 4});
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Binary", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/fhir+json;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, ourCtx.newJsonParser().parseResource(Binary.class, extractBodyAsString(capt)).getContent());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testClientFailures() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenThrow(IllegalStateException.class, RuntimeException.class, IOException.class);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (FhirClientConnectionException e) {
			assertEquals(null, e.getMessage());
		}

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (RuntimeException e) {
			assertEquals("java.lang.RuntimeException", e.toString());
		}

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (FhirClientConnectionException e) {
			assertThat(e.getMessage(), containsString("java.io.IOException"));
		}
	}

	@Test
	public void testCookieInterceptor() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.registerInterceptor(new CookieInterceptor("foo=bar"));

		Bundle resp = client
			.history()
			.onType(Patient.class)
			.andReturnBundle(Bundle.class)
			.execute();

		assertEquals("foo=bar", capt.getAllValues().get(0).getFirstHeader("Cookie").getValue());
	}

	@Test
	public void testCreateWithPreferRepresentationServerReturnsOO() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				if (myAnswerCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), Charset.forName("UTF-8"));
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
				}
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals(2, myAnswerCount);
		assertNotNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>", ((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals(Constants.CT_FHIR_JSON_NEW, capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));

		assertEquals("http://foo.com/base/Patient/222/_history/3", capt.getAllValues().get(1).getURI().toASCIIString());
	}

	@Test
	public void testCreateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals(1, myAnswerCount);
		assertNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeHistoryType() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle resp = client
			.history()
			.onType(CustomTypeR4Test.MyCustomPatient.class)
			.andReturnBundle(Bundle.class)
			.execute();

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeR4Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient/_history", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeLoadPage() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		Bundle bundle = new Bundle();
		bundle.addLink().setRelation("next").setUrl("http://foo/next");

		Bundle resp = client
			.loadPage()
			.next(bundle)
			.preferResponseType(MyCustomPatient.class)
			.execute();

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeR4Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://foo/next", capt.getAllValues().get(0).getURI().toASCIIString());

		resp = client
			.loadPage()
			.next(bundle)
			.preferResponseTypes(toTypeList(MyCustomPatient.class))
			.execute();

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeR4Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://foo/next", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeOperation() throws Exception {

		Parameters param = new Parameters();
		Patient patient = new Patient();
		patient.addName().setFamily("FOO");
		param.addParameter().setName("foo").setResource(patient);
		final String respString = ourCtx.newXmlParser().encodeResourceToString(param);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters resp = client
			.operation()
			.onServer()
			.named("foo")
			.withNoParameters(Parameters.class)
			.preferResponseType(MyCustomPatient.class)
			.execute();

		assertEquals(1, resp.getParameter().size());
		assertEquals(CustomTypeR4Test.MyCustomPatient.class, resp.getParameter().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/$foo", capt.getAllValues().get(0).getURI().toASCIIString());

		resp = client
			.operation()
			.onType(MyCustomPatient.class)
			.named("foo")
			.withNoParameters(Parameters.class)
			.execute();

		assertEquals(1, resp.getParameter().size());
		assertEquals(CustomTypeR4Test.MyCustomPatient.class, resp.getParameter().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient/$foo", capt.getAllValues().get(1).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeSearch() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle resp = client
			.search()
			.forResource(CustomTypeR4Test.MyCustomPatient.class)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeR4Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testFetchCapabilityStatementReceiveCapabilityStatement() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().setFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			private int myCount = 0;

			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				final String respString;
				if (myCount == 1 || myCount == 2) {
					ourLog.info("Encoding patient");
					respString = p.encodeResourceToString(patient);
				} else {
					ourLog.info("Encoding conformance");
					respString = p.encodeResourceToString(conf);
				}
				myCount++;
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://testForceConformanceCapabilityStatement.com/fhir");

		client.read().resource("Patient").withId("1").execute();
		assertEquals(2, capt.getAllValues().size());
		assertEquals("http://testForceConformanceCapabilityStatement.com/fhir/metadata", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://testForceConformanceCapabilityStatement.com/fhir/Patient/1", capt.getAllValues().get(1).getURI().toASCIIString());

		client.read().resource("Patient").withId("1").execute();
		assertEquals(3, capt.getAllValues().size());
		assertEquals("http://testForceConformanceCapabilityStatement.com/fhir/Patient/1", capt.getAllValues().get(2).getURI().toASCIIString());

		client.forceConformanceCheck();
		assertEquals(4, capt.getAllValues().size());
		assertEquals("http://testForceConformanceCapabilityStatement.com/fhir/metadata", capt.getAllValues().get(3).getURI().toASCIIString());
	}

	@Test
	public void testForceConformance() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().setFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {

			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				final String respString;
				if (myAnswerCount >= 1) {
					ourLog.info("Encoding patient");
					respString = p.encodeResourceToString(patient);
				} else {
					ourLog.info("Encoding conformance");
					respString = p.encodeResourceToString(conf);
				}
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://testForceConformance.com/fhir");

		client.read().resource("Patient").withId("1").execute();
		assertEquals(2, capt.getAllValues().size());
		assertEquals("http://testForceConformance.com/fhir/metadata", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://testForceConformance.com/fhir/Patient/1", capt.getAllValues().get(1).getURI().toASCIIString());

		client.read().resource("Patient").withId("1").execute();
		assertEquals(3, capt.getAllValues().size());
		assertEquals("http://testForceConformance.com/fhir/Patient/1", capt.getAllValues().get(2).getURI().toASCIIString());

		myAnswerCount = 0;
		client.forceConformanceCheck();
		assertEquals(4, capt.getAllValues().size());
		assertEquals("http://testForceConformance.com/fhir/metadata", capt.getAllValues().get(3).getURI().toASCIIString());
	}

	@Test
	public void testHistoryTypeWithAt() throws Exception {

		final Bundle resp1 = new Bundle();
		resp1.setTotal(0);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[0];
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> {
			IParser p = ourCtx.newXmlParser();
			return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle outcome = client
			.history()
			.onServer().andReturnBundle(Bundle.class)
			.at(new DateRangeParam().setLowerBound("2011").setUpperBound("2018"))
			.execute();

		assertEquals(0, outcome.getTotal());
		assertEquals("http://example.com/fhir/_history?_at=ge2011&_at=le2018", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testHttp499() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 499, "Wacky Message"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public StringInputStream answer(InvocationOnMock theInvocation) {
				return new StringInputStream("HELLO", Charsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (UnclassifiedServerFailureException e) {
			assertEquals("ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException: HTTP 499 Wacky Message", e.toString());
			assertEquals("HELLO", e.getResponseBody());
		}

	}

	@Test
	public void testHttp501() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 501, "Not Implemented"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public StringInputStream answer(InvocationOnMock theInvocation) {
				return new StringInputStream("not implemented", Charsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (NotImplementedOperationException e) {
			assertEquals("HTTP 501 Not Implemented", e.getMessage());
		}

	}

	/**
	 * See #150
	 */
	@Test
	public void testNullAndEmptyParamValuesAreIgnored() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
			.search()
			.forResource(Patient.class)
			.where(Patient.FAMILY.matches().value((String) null))
			.and(Patient.BIRTHDATE.exactly().day((Date) null))
			.and(Patient.GENDER.exactly().code(null))
			.and(Patient.ORGANIZATION.hasId((String) null))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client
			.search()
			.forResource(Encounter.class)
			.where(Encounter.LENGTH.exactly().number(null).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Encounter", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client
			.search()
			.forResource(Observation.class)
			.where(Observation.VALUE_QUANTITY.exactly().number(null).andUnits(null))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Observation", capt.getAllValues().get(idx).getURI().toString());
		idx++;

	}

	@Test
	public void testOperationInstance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		Parameters outputParams = new Parameters();
		outputParams.addParameter().setName("name").setValue(new BooleanType(false));

		final String respString = p.encodeResourceToString(outputParams);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters result = client
			.operation()
			.onInstance(new IdType("Patient/123/_history/456"))
			.named("opname")
			.withParameters(inputParams)
			.execute();

		assertEquals("name", result.getParameterFirstRep().getName());
		assertEquals("false", ((IPrimitiveType<?>) result.getParameterFirstRep().getValue()).getValueAsString());

		assertEquals("http://example.com/fhir/Patient/123/$opname", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/fhir+json;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		Parameters output = ourCtx.newJsonParser().parseResource(Parameters.class, extractBodyAsString(capt));
		assertEquals("name", output.getParameterFirstRep().getName());
		assertEquals("true", ((IPrimitiveType<?>) output.getParameterFirstRep().getValue()).getValueAsString());
	}

	@Test
	public void testOperationInstanceVersion() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		Parameters outputParams = new Parameters();
		outputParams.addParameter().setName("name").setValue(new BooleanType(false));

		final String respString = p.encodeResourceToString(outputParams);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters result = client
			.operation()
			.onInstanceVersion(new IdType("Patient/123/_history/456"))
			.named("opname")
			.withParameters(inputParams)
			.execute();

		assertEquals("name", result.getParameterFirstRep().getName());
		assertEquals("false", ((IPrimitiveType<?>) result.getParameterFirstRep().getValue()).getValueAsString());

		assertEquals("http://example.com/fhir/Patient/123/_history/456/$opname", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/fhir+json;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		Parameters output = ourCtx.newJsonParser().parseResource(Parameters.class, extractBodyAsString(capt));
		assertEquals("name", output.getParameterFirstRep().getName());
		assertEquals("true", ((IPrimitiveType<?>) output.getParameterFirstRep().getValue()).getValueAsString());
	}

	@Test
	public void testOperationServer() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		Parameters outputParams = new Parameters();
		outputParams.addParameter().setName("name").setValue(new BooleanType(false));

		final String respString = p.encodeResourceToString(outputParams);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters result = client
			.operation()
			.onServer()
			.named("opname")
			.withParameters(inputParams)
			.execute();

		assertEquals("name", result.getParameterFirstRep().getName());
		assertEquals("false", ((IPrimitiveType<?>) result.getParameterFirstRep().getValue()).getValueAsString());

		assertEquals("http://example.com/fhir/$opname", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/fhir+json;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		Parameters output = ourCtx.newJsonParser().parseResource(Parameters.class, extractBodyAsString(capt));
		assertEquals("name", output.getParameterFirstRep().getName());
		assertEquals("true", ((IPrimitiveType<?>) output.getParameterFirstRep().getValue()).getValueAsString());
	}

	/**
	 * Invoke an operation that returns HTML
	 * as a response (a HAPI FHIR server could accomplish this by returning
	 * a Binary resource)
	 */
	@Test
	public void testOperationReturningArbitraryBinaryContentTextual() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		final String respString = "<html>VALUE</html>";
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "text/html"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8")));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{
			new BasicHeader("content-type", "text/html")
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome result = client
			.operation()
			.onServer()
			.named("opname")
			.withParameters(inputParams)
			.returnMethodOutcome()
			.execute();

		assertEquals(Binary.class, result.getResource().getClass());
		Binary binary = (Binary) result.getResource();
		assertEquals(respString, new String(binary.getContent(), Charsets.UTF_8));
		assertEquals("text/html", binary.getContentType());

		assertEquals("http://example.com/fhir/$opname", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	/**
	 * Invoke an operation that returns HTML
	 * as a response (a HAPI FHIR server could accomplish this by returning
	 * a Binary resource)
	 */
	@Test
	public void testOperationReturningArbitraryBinaryContentNonTextual() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		final byte[] respBytes = new byte[]{0,1,2,3,4,5,6,7,8,9,100};
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "application/weird-numbers"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> new ByteArrayInputStream(respBytes));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{
			new BasicHeader("content-Type", "application/weird-numbers")
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome result = client
			.operation()
			.onServer()
			.named("opname")
			.withParameters(inputParams)
			.returnMethodOutcome()
			.execute();

		assertEquals(Binary.class, result.getResource().getClass());
		Binary binary = (Binary) result.getResource();
		assertEquals("application/weird-numbers", binary.getContentType());
		assertArrayEquals(respBytes, binary.getContent());
		assertEquals("http://example.com/fhir/$opname", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testOperationType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inputParams = new Parameters();
		inputParams.addParameter().setName("name").setValue(new BooleanType(true));

		Parameters outputParams = new Parameters();
		outputParams.addParameter().setName("name").setValue(new BooleanType(false));

		final String respString = p.encodeResourceToString(outputParams);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters result = client
			.operation()
			.onType(Patient.class)
			.named("opname")
			.withParameters(inputParams)
			.execute();

		assertEquals("name", result.getParameterFirstRep().getName());
		assertEquals("false", ((IPrimitiveType<?>) result.getParameterFirstRep().getValue()).getValueAsString());

		assertEquals("http://example.com/fhir/Patient/$opname", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/fhir+json;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		Parameters output = ourCtx.newJsonParser().parseResource(Parameters.class, extractBodyAsString(capt));
		assertEquals("name", output.getParameterFirstRep().getName());
		assertEquals("true", ((IPrimitiveType<?>) output.getParameterFirstRep().getValue()).getValueAsString());
	}

	@Test
	public void testPatchInvalid() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client
				.patch()
				.withBody("AA")
				.withId("Patient/123")
				.execute();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to determine encoding of patch", e.getMessage());
		}
	}

	@Test
	public void testPatchJsonByConditionalParam() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.conditional("Patient").where(Patient.NAME.matches().value("TEST"))
			.and(Patient.FAMILY.matches().value("TEST2"))
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=TEST&family=TEST2", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("PATCH", capt.getAllValues().get(0).getRequestLine().getMethod());
		assertEquals(patch, extractBodyAsString(capt));
		assertEquals(Constants.CT_JSON_PATCH, capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", ""));
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString(), containsString("OK!"));
	}

	@Test
	public void testPatchJsonByConditionalParamResourceType() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.conditional(Patient.class).where(Patient.NAME.matches().value("TEST"))
			.and(Patient.FAMILY.matches().value("TEST2"))
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=TEST&family=TEST2", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("PATCH", capt.getAllValues().get(0).getRequestLine().getMethod());
		assertEquals(patch, extractBodyAsString(capt));
		assertEquals(Constants.CT_JSON_PATCH, capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", ""));
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString(), containsString("OK!"));
	}

	@Test
	public void testPatchJsonByConditionalString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.conditionalByUrl("Patient?foo=bar")
			.execute();

		assertEquals("http://example.com/fhir/Patient?foo=bar", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("PATCH", capt.getAllValues().get(0).getRequestLine().getMethod());
		assertEquals(patch, extractBodyAsString(capt));
		assertEquals(Constants.CT_JSON_PATCH, capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", ""));
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString(), containsString("OK!"));
	}

	@Test
	public void testPatchJsonByIdString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.withId("Patient/123")
			.execute();

		assertEquals("http://example.com/fhir/Patient/123", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("PATCH", capt.getAllValues().get(0).getRequestLine().getMethod());
		assertEquals(patch, extractBodyAsString(capt));
		assertEquals(Constants.CT_JSON_PATCH, capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", ""));
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString(), containsString("OK!"));
	}

	@Test
	public void testPatchJsonByIdType() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.withId(new IdType("http://localhost/fhir/Patient/123/_history/234"))
			.execute();

		assertEquals("http://example.com/fhir/Patient/123", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("PATCH", capt.getAllValues().get(0).getRequestLine().getMethod());
		assertEquals(patch, extractBodyAsString(capt));
		assertEquals(Constants.CT_JSON_PATCH, capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", ""));
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString(), containsString("OK!"));
	}

	@Test
	public void testPatchXmlByIdString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diff xmlns:fhir=\"http://hl7.org/fhir\"><replace sel=\"fhir:Patient/fhir:active/@value\">false</replace></diff>";

		MethodOutcome outcome = client
			.patch()
			.withBody(patch)
			.withId("Patient/123")
			.execute();

		assertEquals("http://example.com/fhir/Patient/123", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("PATCH", capt.getAllValues().get(0).getRequestLine().getMethod());
		assertEquals(patch, extractBodyAsString(capt));
		assertEquals(Constants.CT_XML_PATCH, capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", ""));
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString(), containsString("OK!"));
	}

	@Test
	public void testPutDoesntForceAllIdsJson() throws Exception {
		IParser p = ourCtx.newJsonParser();

		Patient patient = new Patient();
		patient.setId("PATIENT1");
		patient.addName().setFamily("PATIENT1");

		Bundle bundle = new Bundle();
		bundle.setId("BUNDLE1");
		bundle.addEntry().setResource(patient);

		final String encoded = p.encodeResourceToString(bundle);
		assertEquals("{\"resourceType\":\"Bundle\",\"id\":\"BUNDLE1\",\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"PATIENT1\",\"name\":[{\"family\":\"PATIENT1\"}]}}]}", encoded);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(encoded), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
			.update()
			.resource(bundle)
			.prefer(PreferReturnEnum.REPRESENTATION)
			.encodedJson()
			.execute();

		HttpPut httpRequest = (HttpPut) capt.getValue();
		assertEquals("http://example.com/fhir/Bundle/BUNDLE1", httpRequest.getURI().toASCIIString());

		String requestString = IOUtils.toString(httpRequest.getEntity().getContent(), StandardCharsets.UTF_8);
		assertEquals(encoded, requestString);
	}

	@Test
	public void testPutDoesntForceAllIdsXml() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.setId("PATIENT1");
		patient.addName().setFamily("PATIENT1");

		Bundle bundle = new Bundle();
		bundle.setId("BUNDLE1");
		bundle.addEntry().setResource(patient);

		final String encoded = p.encodeResourceToString(bundle);
		assertEquals(
			"<Bundle xmlns=\"http://hl7.org/fhir\"><id value=\"BUNDLE1\"/><entry><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"PATIENT1\"/><name><family value=\"PATIENT1\"/></name></Patient></resource></entry></Bundle>",
			encoded);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(encoded), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
			.update()
			.resource(bundle)
			.prefer(PreferReturnEnum.REPRESENTATION)
			.encodedXml()
			.execute();

		HttpPut httpRequest = (HttpPut) capt.getValue();
		assertEquals("http://example.com/fhir/Bundle/BUNDLE1", httpRequest.getURI().toASCIIString());

		String requestString = IOUtils.toString(httpRequest.getEntity().getContent(), StandardCharsets.UTF_8);
		assertEquals(encoded, requestString);
	}

	@Test
	public void testReadWithUnparseableResponse() throws Exception {
		String msg = "{\"resourceTypeeeee\":\"Patient\"}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource("Patient").withId("123").elementsSubset("name", "identifier").execute();
			fail();
		} catch (FhirClientConnectionException e) {
			assertEquals(
				"Failed to parse response from server when performing GET to URL http://example.com/fhir/Patient/123?_elements=identifier%2Cname - ca.uhn.fhir.parser.DataFormatException: Invalid JSON content detected, missing required element: 'resourceType'",
				e.getMessage());
		}
	}

	@Test
	public void testResponseHasContentTypeMissing() throws Exception {
		IParser p = ourCtx.newXmlParser();
		Patient patient = new Patient();
		patient.addName().setFamily("FAM");
		final String respString = p.encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		// when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(null);
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (NonFhirResponseException e) {
			assertEquals("Response contains no Content-Type", e.getMessage());
		}

		// Patient resp = client.read().resource(Patient.class).withId("1").execute();
		// assertEquals("FAM", resp.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testResponseHasContentTypeNonFhir() throws Exception {
		IParser p = ourCtx.newXmlParser();
		Patient patient = new Patient();
		patient.addName().setFamily("FAM");
		final String respString = p.encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "text/plain"));
		// when(myHttpResponse.getEntity().getContentType()).thenReturn(null);
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (NonFhirResponseException e) {
			assertEquals("Response contains non FHIR Content-Type 'text/plain' : <Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"FAM\"/></name></Patient>", e.getMessage());
		}

		// Patient resp = client.read().resource(Patient.class).withId("1").execute();
		// assertEquals("FAM", resp.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testRevIncludeRecursive() throws IOException {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
			.forResource(EpisodeOfCare.class)
			.where(EpisodeOfCare.PATIENT.hasId("123"))
			.revInclude(Encounter.INCLUDE_EPISODE_OF_CARE)
			.revInclude(Observation.INCLUDE_ENCOUNTER.asRecursive())
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/EpisodeOfCare?patient=123&_revinclude=Encounter%3Aepisode-of-care&_revinclude%3Arecurse=Observation%3Aencounter",
			capt.getAllValues().get(idx).getURI().toString());
		idx++;

	}

	@Test
	public void testSearchByDate() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		DateTimeDt now = DateTimeDt.withCurrentTime();
		String dateString = now.getValueAsString().substring(0, 10);

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().day(dateString))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=gt" + dateString, capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().day(now.getValue()))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=gt" + dateString, capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.afterOrEquals().day(dateString))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=ge" + dateString, capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.before().day(dateString))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=lt" + dateString, capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.beforeOrEquals().day(dateString))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=le" + dateString, capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.exactly().day(dateString))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=" + dateString, capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().second("2011-01-02T22:33:01Z"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=gt2011-01-02T22:33:01Z", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().second(now.getValueAsString()))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?birthdate=gt" + now.getValueAsString(), UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().now())
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString(), startsWith("http://example.com/fhir/Patient?birthdate=gt2"));
		dateString = UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()).substring(44);
		ourLog.info(dateString);
		assertEquals(TemporalPrecisionEnum.SECOND, new DateTimeDt(dateString).getPrecision());
		idx++;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSearchByQuantity() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.approximately().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Observation?value-quantity=ap123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.approximately().number("123").andUnits("CODE"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Observation?value-quantity=ap123||CODE", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.approximately().number("123").andUnits("SYSTEM", "CODE"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=ap123|SYSTEM|CODE", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.exactly().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.exactly().number("123").andUnits("CODE"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=123||CODE", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.exactly().number("123").andUnits("SYSTEM", "CODE"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=123|SYSTEM|CODE", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.greaterThan().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=gt123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.lessThan().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=lt123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.greaterThanOrEquals().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=ge123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.lessThanOrEquals().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=le123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.withPrefix(ParamPrefixEnum.GREATERTHAN).number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Observation?value-quantity=gt123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

	}

	@Test
	public void testSearchByString() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("AAA"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=AAA", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value(new StringDt("AAA")))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=AAA", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values("AAA", "BBB"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=AAA,BBB", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values(Arrays.asList("AAA", "BBB")))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=AAA,BBB", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().value("AAA"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name%3Aexact=AAA", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().value(new StringDt("AAA")))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name%3Aexact=AAA", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().values("AAA", "BBB"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name:exact=AAA,BBB", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().values(Arrays.asList("AAA", "BBB")))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name:exact=AAA,BBB", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

	}

	@Test
	public void testSearchByUrl() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
			.forResource("Device")
			.where(Device.URL.matches().value("http://foo.com"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Device?url=http://foo.com", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("http://example.com/fhir/Device?url=http%3A%2F%2Ffoo.com", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.search()
			.forResource("Device")
			.where(Device.URL.matches().value(new StringDt("http://foo.com")))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Device?url=http://foo.com", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

	}

	@Test
	public void testSearchWithMap() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		HashMap<String, List<IQueryParameterType>> params = new HashMap<String, List<IQueryParameterType>>();
		params.put("foo", Arrays.asList((IQueryParameterType) new DateParam("2001")));
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.where(params)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?foo=2001", capt.getValue().getURI().toString());
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchWithMultipleTokens() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		Collection<String> values = Arrays.asList("VAL1", "VAL2", "VAL3A,B");

		client.search()
			.forResource("Patient")
			.where(Patient.IDENTIFIER.exactly().systemAndValues("SYS", values))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?identifier=SYS%7CVAL1%2CSYS%7CVAL2%2CSYS%7CVAL3A%5C%2CB", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("http://example.com/fhir/Patient?identifier=SYS|VAL1,SYS|VAL2,SYS|VAL3A\\,B", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

	}

	@Test
	public void testSearchWithNoExplicitBundleReturnType() throws Exception {

		String msg = ClientR4Test.getPatientFeedWithOneResult(ourCtx);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		// httpResponse = new BasicHttpResponse(statusline, catalog, locale)
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		Bundle response = (Bundle) client.search().forResource(Patient.class).execute();

		assertEquals("http://foo/Patient", capt.getValue().getURI().toString());
		Patient patient = (Patient) response.getEntry().get(0).getResource();
		assertEquals("PRP1660", patient.getIdentifier().get(0).getValueElement().getValue());

	}

	@Test
	public void testSearchWithNullParameters() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		DateTimeDt now = DateTimeDt.withCurrentTime();
		String dateString = now.getValueAsString().substring(0, 10);

		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value((String) null))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		idx++;
	}

	@Test
	public void testSearchWithParameterMap() throws Exception {

		final Bundle resp1 = new Bundle();
		resp1.setTotal(0);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[0];
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> {
			IParser p = ourCtx.newXmlParser();
			return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Map<String, List<String>> rawMap = new HashMap<>();
		rawMap.put("param1", Arrays.asList("val1a,val1b", "<html>"));

		Bundle outcome = client
			.search()
			.forResource(Patient.class)
			.whereMap(rawMap)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(0, outcome.getTotal());
		assertEquals("http://example.com/fhir/Patient?param1=val1a%2Cval1b&param1=%3Chtml%3E", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://example.com/fhir/Patient?param1=val1a,val1b&param1=<html>", UrlUtil.unescape(capt.getAllValues().get(0).getURI().toASCIIString()));
	}

	/**
	 * See #371
	 */
	@Test
	public void testSortR4Test() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Bundle b = new Bundle();
		b.setType(BundleType.SEARCHSET);

		final String respString = p.encodeResourceToString(b);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
			.search()
			.forResource(Patient.class)
			.sort().ascending("address")
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?_sort=address", capt.getAllValues().get(idx++).getURI().toASCIIString());

		client
			.search()
			.forResource(Patient.class)
			.sort().descending("address")
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?_sort=-address", capt.getAllValues().get(idx++).getURI().toASCIIString());

		client
			.search()
			.forResource(Patient.class)
			.sort().descending("address")
			.sort().ascending("name")
			.sort().descending(Patient.BIRTHDATE)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals("http://example.com/fhir/Patient?_sort=-address%2Cname%2C-birthdate", capt.getAllValues().get(idx++).getURI().toASCIIString());

	}

	@Test
	public void testSortUsingSortSpec() throws Exception {

		final Bundle resp1 = new Bundle();
		resp1.setTotal(0);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[0];
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(t -> {
			IParser p = ourCtx.newXmlParser();
			return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		SortSpec sortSpec = new SortSpec();
		sortSpec.setParamName("BDESC");
		sortSpec.setOrder(SortOrderEnum.DESC);
		sortSpec.setChain(new SortSpec());
		sortSpec.getChain().setParamName("CASC");
		sortSpec.getChain().setOrder(SortOrderEnum.ASC);

		Bundle outcome = client
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.sort().ascending("AASC")
			.sort(sortSpec)
			.sort().defaultOrder("DDEF")
			.execute();

		assertEquals(0, outcome.getTotal());
		assertEquals("http://example.com/fhir/Patient?_sort=AASC%2C-BDESC%2CCASC%2CDDEF", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://example.com/fhir/Patient?_sort=AASC,-BDESC,CASC,DDEF", UrlUtil.unescape(capt.getAllValues().get(0).getURI().toASCIIString()));
	}

	@Test
	public void testTransactionWithInvalidBody() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		// Transaction
		try {
			client.transaction().withBundle("FOO");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to determing encoding of request (body does not appear to be valid XML or JSON)", e.getMessage());
		}

		// Create
		try {
			client.create().resource("FOO").execute();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to determing encoding of request (body does not appear to be valid XML or JSON)", e.getMessage());
		}

		// Update
		try {
			client.update().resource("FOO").execute();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to determing encoding of request (body does not appear to be valid XML or JSON)", e.getMessage());
		}

		// Validate
		try {
			client.validate().resource("FOO").execute();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to determing encoding of request (body does not appear to be valid XML or JSON)", e.getMessage());
		}

	}

	@Test
	public void testUpdateById() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("222");
		pt.getText().setDivAsString("A PATIENT");

		client.update().resource(pt).withId("111").execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Patient/111", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/fhir+json;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		String body = extractBodyAsString(capt);
		assertThat(body, containsString("\"id\":\"111\""));
	}

	@Test
	public void testUpdateWithPreferRepresentationServerReturnsOO() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				if (myAnswerCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), Charset.forName("UTF-8"));
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
				}
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.update().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals(2, myAnswerCount);
		assertNotNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>", ((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient/222", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://foo.com/base/Patient/222/_history/3", capt.getAllValues().get(1).getURI().toASCIIString());
	}

	@Test
	public void testUpdateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3")};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.update().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals(1, myAnswerCount);
		assertNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient/222", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testUserAgentForBinary() throws Exception {
		IParser p = ourCtx.newXmlParser();

		CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContentType("application/foo");
		bin.setContent(new byte[]{0, 1, 2, 3, 4});
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Binary", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/foo", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue());
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		assertArrayEquals(new byte[]{0, 1, 2, 3, 4}, extractBodyAsByteArray(capt));

	}

	@Test
	public void testUserAgentForConformance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		CapabilityStatement conf = new CapabilityStatement();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		client.fetchConformance().ofType(CapabilityStatement.class).execute();
		assertEquals("http://example.com/fhir/metadata", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);
	}

	@Test
	public void testUserInfoInterceptor() throws Exception {
		final String respString = CustomTypeR4Test.createBundle(CustomTypeR4Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.registerInterceptor(new UserInfoInterceptor("user_id", "user_name", "app-name"));

		Bundle resp = client
			.history()
			.onType(Patient.class)
			.andReturnBundle(Bundle.class)
			.execute();

	}

	@Test
	public void testValidate() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[]{};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.validate().resource(pt).execute();

		assertNotNull(outcome.getOperationOutcome());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>", ((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString());

	}

	@Test
	public void testValidateCustomTypeFromClientRead() throws Exception {
		IParser p = ourCtx.newXmlParser();

		MyPatientWithExtensions patient = new MyPatientWithExtensions();
		patient.setId("123");
		patient.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(patient);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		MyPatientWithExtensions read = client.read().resource(MyPatientWithExtensions.class).withId(new IdType("1")).execute();
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>", read.getText().getDivAsString());

		// Ensure that we haven't overridden the default type for the name
		assertFalse(MyPatientWithExtensions.class.isAssignableFrom(Patient.class));
		assertFalse(Patient.class.isAssignableFrom(MyPatientWithExtensions.class));
		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");
		IParser parser = ourCtx.newXmlParser();
		String encoded = parser.encodeResourceToString(pt);
		pt = (Patient) parser.parseResource(encoded);

	}

	@Test
	public void testValidateCustomTypeFromClientSearch() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Bundle b = new Bundle();

		MyPatientWithExtensions patient = new MyPatientWithExtensions();
		patient.setId("123");
		patient.getText().setDivAsString("OK!");
		b.addEntry().setResource(patient);

		final String respString = p.encodeResourceToString(b);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		Bundle bundle = client.search().forResource(MyPatientWithExtensions.class).returnBundle(Bundle.class).execute();

		assertEquals(1, bundle.getEntry().size());
		assertEquals(MyPatientWithExtensions.class, bundle.getEntry().get(0).getResource().getClass());
	}

	private List<Class<? extends IBaseResource>> toTypeList(Class<? extends IBaseResource> theClass) {
		ArrayList<Class<? extends IBaseResource>> retVal = new ArrayList<Class<? extends IBaseResource>>();
		retVal.add(theClass);
		return retVal;
	}

	private void validateUserAgent(ArgumentCaptor<HttpUriRequest> capt) {
		assertEquals(1, capt.getAllValues().get(0).getHeaders("User-Agent").length);
		assertEquals(expectedUserAgent(), capt.getAllValues().get(0).getHeaders("User-Agent")[0].getValue());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forR4();
	}


}
