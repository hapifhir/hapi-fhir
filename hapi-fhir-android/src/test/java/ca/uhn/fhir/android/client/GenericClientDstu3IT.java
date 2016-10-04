package ca.uhn.fhir.android.client;

import static org.hamcrest.Matchers.containsString;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.interceptor.UserInfoInterceptor;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.VersionUtil;

public class GenericClientDstu3IT {

	
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientDstu3IT.class);
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

	}

	private String expectedUserAgent() {
		return "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client; FHIR " + FhirVersionEnum.DSTU3.getFhirVersionString() + "/DSTU3; apache)";
	}


	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent(), "UTF-8");
		return body;
	}

	/**
	 * TODO: narratives don't work without stax
	 */
	@Test
	@Ignore
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

	/**
	 * See #150
	 */
	@Test
	public void testNullAndEmptyParamValuesAreIgnored() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		//@formatter:off
      client
      	.search()
      	.forResource(Patient.class)
      	.where(Patient.FAMILY.matches().value((String)null))
      	.and(Patient.BIRTHDATE.exactly().day((Date)null))
      	.and(Patient.GENDER.exactly().code((String)null))
      	.and(Patient.ORGANIZATION.hasId((String)null))
      	.returnBundle(Bundle.class)
      	.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?_format=json", capt.getAllValues().get(idx).getURI().toString());
		idx++;
		
	}
	
	
	
	/**
	 * TODO: narratives don't work without stax
	 */
	@Test
	@Ignore
	public void testBinaryCreateWithNoContentType() throws Exception {
		IParser p = ourCtx.newJsonParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
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



	/**
	 * TODO: narratives don't work without stax
	 */
	@Test
	@Ignore
	public void testCreateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newJsonParser();

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
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
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
	public void testForceConformance() throws Exception {
		final IParser p = ourCtx.newJsonParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
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
		assertEquals("http://testForceConformance.com/fhir/metadata?_format=json", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://testForceConformance.com/fhir/Patient/1?_format=json", capt.getAllValues().get(1).getURI().toASCIIString());

		client.read().resource("Patient").withId("1").execute();
		assertEquals(3, capt.getAllValues().size());
		assertEquals("http://testForceConformance.com/fhir/Patient/1?_format=json", capt.getAllValues().get(2).getURI().toASCIIString());

		client.forceConformanceCheck();
		assertEquals(4, capt.getAllValues().size());
		assertEquals("http://testForceConformance.com/fhir/metadata?_format=json", capt.getAllValues().get(3).getURI().toASCIIString());
	}

	@Test
	public void testHttp499() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 499, "Wacky Message"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader("HELLO"), StandardCharsets.UTF_8);
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
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader("not implemented"), Charset.forName("UTF-8"));
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
	public void testPutDoesntForceAllIdsJson() throws Exception {
		IParser p = ourCtx.newJsonParser();
		
		Patient patient = new Patient();
		patient.setId("PATIENT1");
		patient.addName().addFamily("PATIENT1");
		
		Bundle bundle = new Bundle();
		bundle.setId("BUNDLE1");
		bundle.addEntry().setResource(patient);
		
		final String encoded = p.encodeResourceToString(bundle);
		assertEquals("{\"resourceType\":\"Bundle\",\"id\":\"BUNDLE1\",\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"PATIENT1\",\"name\":[{\"family\":[\"PATIENT1\"]}]}}]}", encoded);
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(encoded), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		//@formatter:off
		client
			.update()
			.resource(bundle)
			.prefer(PreferReturnEnum.REPRESENTATION)
			.encodedJson()
			.execute();
		//@formatter:on
		
		HttpPut httpRequest = (HttpPut) capt.getValue();
		assertEquals("http://example.com/fhir/Bundle/BUNDLE1?_format=json", httpRequest.getURI().toASCIIString());
		
		String requestString = IOUtils.toString(httpRequest.getEntity().getContent(), StandardCharsets.UTF_8);
		assertEquals(encoded, requestString);
	}
	
	@Test
	public void testResponseHasContentTypeMissing() throws Exception {
		IParser p = ourCtx.newJsonParser();
		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String respString = p.encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(null);
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
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

		//		Patient resp = client.read().resource(Patient.class).withId("1").execute();
		//		assertEquals("FAM", resp.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testResponseHasContentTypeNonFhir() throws Exception {
		IParser p = ourCtx.newJsonParser();
		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String respString = p.encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "text/plain"));
		//		when(myHttpResponse.getEntity().getContentType()).thenReturn(null);
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (NonFhirResponseException e) {
			assertEquals("Response contains non FHIR Content-Type 'text/plain' : {\"resourceType\":\"Patient\",\"name\":[{\"family\":[\"FAM\"]}]}", e.getMessage());
		}

		//		Patient resp = client.read().resource(Patient.class).withId("1").execute();
		//		assertEquals("FAM", resp.getNameFirstRep().getFamilyAsSingleString());
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

		DateTimeDt now = DateTimeDt.withCurrentTime();
		String dateString = now.getValueAsString().substring(0, 10);

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.BIRTHDATE.after().day(dateString))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?birthdate=gt"+dateString + "&_format=json", capt.getAllValues().get(idx).getURI().toString());
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
		assertEquals("http://example.com/fhir/Patient?name=AAA&_format=json", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value(new StringDt("AAA")))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=AAA&_format=json", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values("AAA", "BBB"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=AAA,BBB&_format=json", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().values(Arrays.asList("AAA", "BBB")))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=AAA,BBB&_format=json", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().value("AAA"))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name%3Aexact=AAA&_format=json", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Patient")
			.where(Patient.NAME.matchesExactly().value(new StringDt("AAA")))
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name%3Aexact=AAA&_format=json", capt.getAllValues().get(idx).getURI().toString());
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
		assertEquals("http://example.com/fhir/Device?url=http://foo.com&_format=json", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		idx++;

	}

	@Test
	public void testAcceptHeaderWithEncodingSpecified() throws Exception {
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
		client.setEncoding(EncodingEnum.JSON);
		client.search()
			.forResource("Device")
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Device?_format=json", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("application/fhir+json;q=1.0, application/json+fhir;q=0.9", capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue());
		idx++;
		
		//@formatter:off
		client.setEncoding(EncodingEnum.XML);
		client.search()
			.forResource("Device")
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Device?_format=xml", UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()));
		assertEquals("application/fhir+xml;q=1.0, application/xml+fhir;q=0.9", capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue());
		idx++;
		
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

	private ArgumentCaptor<HttpUriRequest> prepareClientForSearchResponse() throws IOException, ClientProtocolException {
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
		return capt;
	}



	@Test
	public void testTransactionWithInvalidBody() throws Exception {
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

	/**
	 * TODO: narratives don't work without stax
	 */
	@Test
	@Ignore
	public void testUpdateById() throws Exception {
		IParser p = ourCtx.newJsonParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
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
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		String body = extractBodyAsString(capt);
		assertThat(body, containsString("<id value=\"111\"/>"));
	}

	/**
	 * TODO: narratives don't work without stax
	 */
	@Test
	@Ignore
	public void testUpdateWithPreferRepresentationServerReturnsOO() throws Exception {
		final IParser p = ourCtx.newJsonParser();

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
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
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
	public void testUpdateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newJsonParser();

		final Patient resp1 = new Patient();
		resp1.setActive(true);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
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

		assertEquals(true, ((Patient) outcome.getResource()).getActive());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient/222?_format=json", capt.getAllValues().get(0).getURI().toASCIIString());
	}


	@Test
	public void testUserAgentForConformance() throws Exception {
		IParser p = ourCtx.newJsonParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		client.fetchConformance().ofType(Conformance.class).execute();
		assertEquals("http://example.com/fhir/metadata?_format=json", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);
	}


	/**
	 * TODO: narratives don't work without stax
	 */
	@Test
	@Ignore
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
		
		// Force StAX to fail like it will on android
		System.setProperty("javax.xml.stream.XMLInputFactory", "FOO");
		System.setProperty("javax.xml.stream.XMLOutputFactory", "FOO");
		
		ourCtx = FhirContext.forDstu3();
	}

}
