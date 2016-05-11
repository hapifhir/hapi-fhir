package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.TestUtil;

public class ExceptionHandlingTest {

	private static FhirContext ourCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu1();
	}

	@Before
	public void before() {

		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.NEVER);

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
			client.read(Patient.class, new IdDt("Patient/1234"));
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), StringContains.containsString("HTTP 500 Internal Error"));
			assertThat(e.getMessage(), StringContains.containsString("Help I'm a bug"));
		}

	}

	@Test
	public void testFail500WithOperationOutcomeMessage() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getDetails().setValue("Help I'm a bug");
		String msg = ourCtx.newXmlParser().encodeResourceToString(oo);
		String contentType = Constants.CT_FHIR_XML;

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Internal Error"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", contentType + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read(Patient.class, new IdDt("Patient/1234"));
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
			client.read(Patient.class, new IdDt("Patient/1234"));
			fail();
		} catch (InternalErrorException e) {
			assertEquals("HTTP 500 Internal Error", e.getMessage());
			assertThat(e.getResponseBody(), StringContains.containsString("value=\"foo\""));
		}

	}

	@Test
	public void testFail500WithOperationOutcomeMessageJson() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getDetails().setValue("Help I'm a bug");
		String msg = ourCtx.newJsonParser().encodeResourceToString(oo);
		String contentType = Constants.CT_FHIR_JSON;

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Internal Error"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", contentType + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read(Patient.class, new IdDt("Patient/1234"));
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), StringContains.containsString("HTTP 500 Internal Error"));
			assertThat(e.getMessage(), StringContains.containsString("Help I'm a bug"));
			assertNotNull(e.getOperationOutcome());
			assertEquals("Help I'm a bug", ((BaseOperationOutcome) e.getOperationOutcome()).getIssueFirstRep().getDetailsElement().getValue());
		}

	}

	@Test
	public void testFail500WithOperationOutcomeMessageGeneric() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.getIssueFirstRep().getDetails().setValue("Help I'm a bug");
		String msg = ourCtx.newJsonParser().encodeResourceToString(oo);
		String contentType = Constants.CT_FHIR_JSON;

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Internal Error"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", contentType + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IMyClient client = ourCtx.newRestfulClient(IMyClient.class, "http://example.com/fhir");
		try {
			client.read(new IdDt("Patient/1234"));
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), StringContains.containsString("HTTP 500 Internal Error"));
			assertThat(e.getMessage(), StringContains.containsString("Help I'm a bug"));
			assertNotNull(e.getOperationOutcome());
			assertEquals("Help I'm a bug", ((BaseOperationOutcome) e.getOperationOutcome()).getIssueFirstRep().getDetailsElement().getValue());
		}

	}

	public interface IMyClient extends IRestfulClient {
		@Read
		Patient read(@IdParam IdDt theId);
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
