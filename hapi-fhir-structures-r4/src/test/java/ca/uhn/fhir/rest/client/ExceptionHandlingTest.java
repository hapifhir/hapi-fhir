package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.io.StringReader;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExceptionHandlingTest {

	private static FhirContext ourCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forR4();
	}

	@BeforeEach
	public void before() {

		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testFail500WithPlainMessage() throws Exception {
		String msg = "Help I'm a bug";
		String contentType = Constants.CT_TEXT;

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Internal Error"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", contentType + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId(new IdType("Patient/1234")).execute();
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), StringContains.containsString("HTTP 500 Internal Error"));
			assertThat(e.getMessage(), StringContains.containsString("Help I'm a bug"));
		}

	}

	@Test
	public void testFail500WithOperationOutcomeMessage() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getDiagnosticsElement().setValue("Help I'm a bug");
		String msg = ourCtx.newXmlParser().encodeResourceToString(oo);
		String contentType = Constants.CT_FHIR_XML;

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Internal Error"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", contentType + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
        client.read().resource(Patient.class).withId(new IdType("Patient/1234")).execute();
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), StringContains.containsString("HTTP 500 Internal Error"));
			assertThat(e.getMessage(), StringContains.containsString("Help I'm a bug"));
		}

	}

	@Test
	public void testFail500WithUnexpectedResource() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("foo").setValue("bar");
		String msg = ourCtx.newXmlParser().encodeResourceToString(patient);
		String contentType = Constants.CT_FHIR_XML;

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Internal Error"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", contentType + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
        client.read().resource(Patient.class).withId(new IdType("Patient/1234")).execute();
			fail();
		} catch (InternalErrorException e) {
			assertEquals("HTTP 500 Internal Error", e.getMessage());
			assertThat(e.getResponseBody(), StringContains.containsString("value=\"foo\""));
		}

	}

	@Test
	public void testFail500WithOperationOutcomeMessageJson() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getDiagnosticsElement().setValue("Help I'm a bug");
		String msg = ourCtx.newJsonParser().encodeResourceToString(oo);
		String contentType = Constants.CT_FHIR_JSON;

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Internal Error"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", contentType + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
        client.read().resource(Patient.class).withId(new IdType("Patient/1234")).execute();
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), StringContains.containsString("HTTP 500 Internal Error"));
			assertThat(e.getMessage(), StringContains.containsString("Help I'm a bug"));
			assertNotNull(e.getOperationOutcome());
			assertEquals("Help I'm a bug", ((OperationOutcome) e.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
		}

	}

	@Test
	public void testFail500WithOperationOutcomeMessageGeneric() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getDiagnosticsElement().setValue("Help I'm a bug");
		String msg = ourCtx.newJsonParser().encodeResourceToString(oo);
		String contentType = Constants.CT_FHIR_JSON;

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Internal Error"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", contentType + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IMyClient client = ourCtx.newRestfulClient(IMyClient.class, "http://example.com/fhir");
		try {
			client.read(new IdType("Patient/1234"));
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), StringContains.containsString("HTTP 500 Internal Error"));
			assertThat(e.getMessage(), StringContains.containsString("Help I'm a bug"));
			assertNotNull(e.getOperationOutcome());
			assertEquals("Help I'm a bug", ((OperationOutcome) e.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
		}

	}

	public interface IMyClient extends IRestfulClient {
		@Read
		Patient read(@IdParam IdType theId);
	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
