package ca.uhn.fhir.android.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericClientDstu3IT {

	
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientDstu3IT.class);
	private int myAnswerCount;
	private Call.Factory myHttpClient;
	private ArgumentCaptor<Request> capt;
	private Response myHttpResponse;
	private Request myRequest;
	private Protocol myProtocol;

	@BeforeEach
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
	@Disabled
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
		client.setEncoding(EncodingEnum.JSON);

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
		client.setEncoding(EncodingEnum.JSON);

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
		client.setEncoding(EncodingEnum.JSON);

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
		client.setEncoding(EncodingEnum.JSON);

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals(1, capt.getAllValues().size());
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

	
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() {
		
//		// Force StAX to fail like it will on android
//		System.setProperty("javax.xml.stream.XMLInputFactory", "FOO");
//		System.setProperty("javax.xml.stream.XMLOutputFactory", "FOO");
		
		ourCtx = FhirContext.forDstu3();
	}
}
