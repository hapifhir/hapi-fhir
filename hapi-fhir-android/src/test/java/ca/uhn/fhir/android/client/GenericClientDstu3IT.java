package ca.uhn.fhir.android.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import ca.uhn.fhir.rest.api.PreferReturnEnum;
import org.hl7.fhir.dstu3.model.*;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;
import okhttp3.*;
import okio.Buffer;

public class GenericClientDstu3IT {

	
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientDstu3IT.class);
	private int myAnswerCount;
	private Call.Factory myHttpClient;
	private ArgumentCaptor<Request> capt;
	private Response myHttpResponse;
	private Request myRequest;
	private Protocol myProtocol;

	@Before
	public void before() throws IOException {
		myHttpClient = mock(Call.Factory.class, Mockito.RETURNS_DEEP_STUBS);
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		Call httpResponse = mock(Call.class, Mockito.RETURNS_DEEP_STUBS);
		capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(httpResponse);

		myRequest = new Request.Builder().url("http://127.0.0.1").build();
		myProtocol = Protocol.HTTP_1_1;

		when(httpResponse.execute()).thenAnswer(new Answer<Response>() {
			@Override
			public Response answer(InvocationOnMock theInvocation) {
				myAnswerCount++;
				return myHttpResponse;
			}});
		myAnswerCount = 0;

	}

	private String expectedUserAgent() {
		return "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client; FHIR " + FhirVersionEnum.DSTU3.getFhirVersionString() + "/DSTU3; okhttp/3.8.1)";
	}


	private String extractBodyAsString(ArgumentCaptor<Request> capt) throws IOException {
		Buffer sink = new Buffer();
		capt.getValue().body().writeTo(sink);
		return new String(sink.readByteArray(), StandardCharsets.UTF_8);
	}

	private void validateUserAgent(ArgumentCaptor<Request> capt) {
		assertEquals(expectedUserAgent(), capt.getAllValues().get(0).header("User-Agent"));
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
		String respString = p.encodeResourceToString(conf);

		myHttpResponse = new Response.Builder()
				.request(myRequest)
				.protocol(myProtocol)
				.code(200)
				.body(ResponseBody.create(MediaType.parse(Constants.CT_FHIR_XML + "; charset=UTF-8"), respString))
				.message("")
				.build();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		Binary bin = new Binary();
		bin.setContent(ourCtx.newJsonParser().encodeResourceToString(pt).getBytes(StandardCharsets.UTF_8));
		bin.setContentType(Constants.CT_FHIR_JSON);
		client.create().resource(bin).execute();

		Request request = capt.getAllValues().get(0);
		ourLog.info(request.headers().toString());

		assertEquals("http://example.com/fhir/Binary", request.url().toString());
		validateUserAgent(capt);

		assertEquals(Constants.CT_FHIR_XML_NEW + ";charset=utf-8", request.body().contentType().toString().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_NON_LEGACY, request.header("Accept"));
		Binary output = ourCtx.newXmlParser().parseResource(Binary.class, extractBodyAsString(capt));
		assertEquals(Constants.CT_FHIR_JSON, output.getContentType());

		Patient outputPt = (Patient) ourCtx.newJsonParser().parseResource(new String(output.getContent(), StandardCharsets.UTF_8));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">A PATIENT</div>", outputPt.getText().getDivAsString());
	}

	/**
	 * See #150
	 */
	@Test
	public void testNullAndEmptyParamValuesAreIgnored() throws Exception {
		ArgumentCaptor<Request> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

      client
      	.search()
      	.forResource(Patient.class)
      	.where(Patient.FAMILY.matches().value((String)null))
      	.and(Patient.BIRTHDATE.exactly().day((Date)null))
      	.and(Patient.GENDER.exactly().code(null))
      	.and(Patient.ORGANIZATION.hasId((String)null))
      	.returnBundle(Bundle.class)
      	.execute();

		assertEquals("http://example.com/fhir/Patient?_format=json", capt.getAllValues().get(idx).url().toString());
		idx++;
		
	}
	
	
	
	/**
	 * TODO: narratives don't work without stax
	 */
	@Test
	public void testBinaryCreateWithNoContentType() throws Exception {
		IParser p = ourCtx.newJsonParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		myHttpResponse = new Response.Builder()
				.request(myRequest)
				.protocol(myProtocol)
				.code(200)
				.body(ResponseBody.create(MediaType.parse(Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"), respString))
				.message("")
				.build();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContent(new byte[] { 0, 1, 2, 3, 4 });
		client.create().resource(bin).execute();

		Request request = capt.getAllValues().get(0);
		ourLog.info(request.headers().toString());

		assertEquals("http://example.com/fhir/Binary?_format=json", request.url().toString());
		validateUserAgent(capt);

		assertEquals(Constants.CT_FHIR_JSON_NEW + ";charset=utf-8", request.body().contentType().toString().toLowerCase().replace(" ", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, request.header("Accept"));
		assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, ourCtx.newJsonParser().parseResource(Binary.class, extractBodyAsString(capt)).getContent());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testClientFailures() {
		ResponseBody body = mock(ResponseBody.class);
		when(body.source()).thenThrow(IllegalStateException.class, RuntimeException.class);
		
		myHttpResponse = new Response.Builder()
				.request(myRequest)
				.protocol(myProtocol)
				.code(200)
				.body(body)
				.message("")
				.build();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (FhirClientConnectionException e) {
			// good
		}

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail();
		} catch (RuntimeException e) {
			// good
		}

	}



	/**
	 * TODO: narratives don't work without stax
	 */
	@Test
	public void testCreateWithPreferRepresentationServerReturnsResource() {
		final IParser p = ourCtx.newJsonParser();

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");
		String respString = p.encodeResourceToString(resp1);

		myHttpResponse = new Response.Builder()
				.request(myRequest)
				.protocol(myProtocol)
				.code(200)
				.body(ResponseBody.create(MediaType.parse(Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"), respString))
				.headers(Headers.of(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3"))
				.message("")
				.build();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());
		assertEquals("http://example.com/fhir/Patient?_format=json", capt.getAllValues().get(0).url().toString());
	}

	
	private ArgumentCaptor<Request> prepareClientForSearchResponse() {
		final String respString = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";
		myHttpResponse = new Response.Builder()
				.request(myRequest)
				.protocol(myProtocol)
				.code(200)
				.body(ResponseBody.create(MediaType.parse(Constants.CT_FHIR_JSON + "; charset=UTF-8"), respString))
				.headers(Headers.of(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3"))
				.message("")
				.build();

		return capt;
	}

	
/*

	

	@Test
	public void testForceConformance() throws Exception {
		final IParser p = ourCtx.newJsonParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAM");

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8"));
		when(myHttpResponse.execute().body()).thenReturn(ResponseBody.create(MediaType.parse(Constants.CT_FHIR_XML + "; charset=UTF-8"), respString));
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
		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
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
		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
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
		
		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

	//TODO: narratives don't work without stax
	@Test
	@Ignore
	public void testUpdateById() throws Exception {
		IParser p = ourCtx.newJsonParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

	// TODO: narratives don't work without stax
	@Test
	@Ignore
	public void testUpdateWithPreferRepresentationServerReturnsOO() throws Exception {
		final IParser p = ourCtx.newJsonParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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
		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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


	// TODO: narratives don't work without stax
	@Test
	@Ignore
	public void testValidate() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		ArgumentCaptor<Request> capt = ArgumentCaptor.forClass(Request.class);
		when(myHttpClient.newCall(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.execute().code()).thenReturn(200);
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


 */

	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		
//		// Force StAX to fail like it will on android
//		System.setProperty("javax.xml.stream.XMLInputFactory", "FOO");
//		System.setProperty("javax.xml.stream.XMLOutputFactory", "FOO");
		
		ourCtx = FhirContext.forDstu3();
	}
}
