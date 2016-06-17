package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Conformance;
import org.hl7.fhir.dstu3.model.Device;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Charsets;
import com.phloc.commons.io.streams.StringInputStream;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.CustomTypeDstu3Test;
import ca.uhn.fhir.parser.CustomTypeDstu3Test.MyCustomPatient;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.interceptor.CookieInterceptor;
import ca.uhn.fhir.rest.client.interceptor.UserInfoInterceptor;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.VersionUtil;

public class GenericClientDstu3Test {
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientDstu3Test.class);
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
		return "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client; FHIR " + FhirVersionEnum.DSTU3.getFhirVersionString() + "/DSTU3; apache)";
	}

	private byte[] extractBodyAsByteArray(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		byte[] body = IOUtils.toByteArray(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent());
		return body;
	}

	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent(), "UTF-8");
		return body;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testInvalidConformanceCall() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.conformance();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Must call fetchConformance() instead of conformance() for RI/STU3+ structures", e.getMessage());
		}
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
			assertEquals("Failed to parse response from server when performing GET to URL http://example.com/fhir/Patient/123?_elements=identifier%2Cname - ca.uhn.fhir.parser.DataFormatException: Invalid JSON content detected, missing required element: 'resourceType'", e.getMessage());
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
			public StringInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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

	@SuppressWarnings("unchecked")
	@Test
	public void testClientFailures() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenThrow(IllegalStateException.class, RuntimeException.class, Exception.class);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (FhirClientConnectionException e) {
			assertEquals("java.lang.IllegalStateException", e.getMessage());
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
			assertEquals("java.lang.Exception", e.getMessage());
		}
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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

		assertEquals("application/xml+fhir;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.CT_FHIR_XML, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		Binary output = ourCtx.newXmlParser().parseResource(Binary.class, extractBodyAsString(capt));
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContent(new byte[] { 0, 1, 2, 3, 4 });
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Binary", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/xml+fhir;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.CT_FHIR_XML, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, ourCtx.newXmlParser().parseResource(Binary.class, extractBodyAsString(capt)).getContent());

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
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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
	public void testUserInfoInterceptor() throws Exception {
		final String respString = CustomTypeDstu3Test.createBundle(CustomTypeDstu3Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.registerInterceptor(new UserInfoInterceptor("user_id", "user_name", "app-name"));

		//@formatter:off
		Bundle resp = client
			.history()
			.onType(Patient.class)
			.andReturnBundle(Bundle.class)
			.execute();
		//@formatter:on

	}

	@Test
	public void testCookieInterceptor() throws Exception {
		final String respString = CustomTypeDstu3Test.createBundle(CustomTypeDstu3Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.registerInterceptor(new CookieInterceptor("foo=bar"));

		//@formatter:off
		Bundle resp = client
			.history()
			.onType(Patient.class)
			.andReturnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals("foo=bar", capt.getAllValues().get(0).getFirstHeader("Cookie").getValue());
	}

	@Test
	public void testExplicitCustomTypeHistoryType() throws Exception {
		final String respString = CustomTypeDstu3Test.createBundle(CustomTypeDstu3Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle resp = client
			.history()
			.onType(CustomTypeDstu3Test.MyCustomPatient.class)
			.andReturnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient/_history", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeLoadPage() throws Exception {
		final String respString = CustomTypeDstu3Test.createBundle(CustomTypeDstu3Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		Bundle bundle = new Bundle();
		bundle.addLink().setRelation("next").setUrl("http://foo/next");

		//@formatter:off
		Bundle resp = client
			.loadPage()
			.next(bundle)
			.preferResponseType(MyCustomPatient.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://foo/next", capt.getAllValues().get(0).getURI().toASCIIString());

		//@formatter:off
		resp = client
			.loadPage()
			.next(bundle)
			.preferResponseTypes(toTypeList(MyCustomPatient.class))
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://foo/next", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeOperation() throws Exception {

		Parameters param = new Parameters();
		Patient patient = new Patient();
		patient.addName().addFamily("FOO");
		param.addParameter().setName("foo").setResource(patient);
		final String respString = ourCtx.newXmlParser().encodeResourceToString(param);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("foo")
			.withNoParameters(Parameters.class)
			.preferResponseType(MyCustomPatient.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getParameter().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getParameter().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/$foo", capt.getAllValues().get(0).getURI().toASCIIString());

		//@formatter:off
		resp = client
			.operation()
			.onType(MyCustomPatient.class)
			.named("foo")
			.withNoParameters(Parameters.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getParameter().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getParameter().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient/$foo", capt.getAllValues().get(1).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeSearch() throws Exception {
		final String respString = CustomTypeDstu3Test.createBundle(CustomTypeDstu3Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle resp = client
			.search()
			.forResource(CustomTypeDstu3Test.MyCustomPatient.class)
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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

		assertEquals("application/xml+fhir;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.CT_FHIR_XML, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		String body = extractBodyAsString(capt);
		assertThat(body, containsString("<id value=\"111\"/>"));
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
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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
	public void testValidate() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] {};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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
	public void testUpdateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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
	public void testSearchForUnknownType() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.search(new UriDt("?aaaa"));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Unable to determine the resource type from the given URI: ?aaaa", e.getMessage());
		}
	}

	@Test
	public void testUserAgentForBinary() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContentType("application/foo");
		bin.setContent(new byte[] { 0, 1, 2, 3, 4 });
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Binary", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/foo", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue());
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, extractBodyAsByteArray(capt));

	}

	/**
	 * See #371
	 */
	@Test
	public void testSortDstu3Test() throws Exception {
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		//@formatter:off
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
	//@formatter:on
	}

	@Test
	public void testUserAgentForConformance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		client.fetchConformance().ofType(Conformance.class).execute();
		assertEquals("http://example.com/fhir/metadata", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);
	}

	@Test
	public void testForceConformance() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			private int myCount = 0;

			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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
		IGenericClient client = ourCtx.newRestfulGenericClient("http://testForceConformance.com/fhir");

		client.read().resource("Patient").withId("1").execute();
		assertEquals(2, capt.getAllValues().size());
		assertEquals("http://testForceConformance.com/fhir/metadata", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://testForceConformance.com/fhir/Patient/1", capt.getAllValues().get(1).getURI().toASCIIString());

		client.read().resource("Patient").withId("1").execute();
		assertEquals(3, capt.getAllValues().size());
		assertEquals("http://testForceConformance.com/fhir/Patient/1", capt.getAllValues().get(2).getURI().toASCIIString());

		client.forceConformanceCheck();
		assertEquals(4, capt.getAllValues().size());
		assertEquals("http://testForceConformance.com/fhir/metadata", capt.getAllValues().get(3).getURI().toASCIIString());
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
		ourCtx = FhirContext.forDstu3();
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
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.approximately().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=ap123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.approximately().number("123").andUnits("CODE"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=ap123||CODE", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.approximately().number("123").andUnits("SYSTEM", "CODE"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=ap123|SYSTEM|CODE", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.exactly().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.exactly().number("123").andUnits("CODE"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=123||CODE", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.exactly().number("123").andUnits("SYSTEM", "CODE"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=123|SYSTEM|CODE", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.greaterThan().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=gt123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.lessThan().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=lt123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.greaterThanOrEquals().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=ge123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.lessThanOrEquals().number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=le123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.withComparator(QuantityCompararatorEnum.GREATERTHAN).number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=gt123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Observation")
			.where(Observation.VALUE_QUANTITY.withPrefix(ParamPrefixEnum.GREATERTHAN).number(123).andNoUnits())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Observation?value-quantity=gt123||", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
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
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		//@formatter:off
		client.search()
			.forResource("Device")
			.where(Device.URL.matches().value("http://foo.com"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Device?url=http://foo.com", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("http://example.com/fhir/Device?url=http%3A%2F%2Ffoo.com", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Device")
			.where(Device.URL.matches().value(new StringDt("http://foo.com")))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Device?url=http://foo.com", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
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
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("AAA"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=AAA", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value(new StringDt("AAA")))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=AAA", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values("AAA", "BBB"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=AAA,BBB", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values(Arrays.asList("AAA", "BBB")))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=AAA,BBB", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().value("AAA"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name%3Aexact=AAA", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().value(new StringDt("AAA")))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name%3Aexact=AAA", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().values("AAA", "BBB"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name:exact=AAA,BBB", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().values(Arrays.asList("AAA", "BBB")))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name:exact=AAA,BBB", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
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
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		Date date = new DateTimeDt("2011-01-02T22:33:01Z").getValue();

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().day("2011-01-02"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=gt2011-01-02", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().day(date))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=gt2011-01-02", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=ge2011-01-02", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.before().day("2011-01-02"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=lt2011-01-02", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.beforeOrEquals().day("2011-01-02"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=le2011-01-02", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.exactly().day("2011-01-02"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=2011-01-02", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().second("2011-01-02T22:33:01Z"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=gt2011-01-02T22:33:01Z", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		String expDate = new DateTimeDt(date).getValueAsString();

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().second(date))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=gt" + expDate, UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().now())
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertThat(capt.getAllValues().get(idx).getURI().toString(), startsWith("http://example.com/fhir/Patient?birthdate=gt2"));
		String dateString = UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()).substring(44);
		ourLog.info(dateString);
		assertEquals(TemporalPrecisionEnum.SECOND, new DateTimeDt(dateString).getPrecision());
		idx++;
	}

}
