package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.*;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.server.Constants;

public class ClientServerValidationTest {

	private FhirContext myCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@Before
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());

		myCtx = FhirContext.forDev();
		myCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
	}

	@Test
	public void testServerReturnsAppropriateVersionDev() throws Exception {
		Conformance conf = new Conformance();
		conf.setFhirVersion("0.4.0");
		String msg = myCtx.newXmlParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		myCtx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.ONCE);
		myCtx.newRestfulGenericClient("http://foo");
		myCtx.newRestfulGenericClient("http://foo");

		verify(myHttpClient, times(1)).execute(Matchers.any(HttpUriRequest.class));
	}

	@Test
	public void testServerReturnsWrongVersionDev() throws Exception {
		Conformance conf = new Conformance();
		conf.setFhirVersion("0.0.8");
		String msg = myCtx.newXmlParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		myCtx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.ONCE);
		try {
			myCtx.newRestfulGenericClient("http://foo");
			fail();
		} catch (FhirClientConnectionException e) {
			assertThat(e.toString(), containsString("The server at base URL \"http://foo/metadata\" returned a conformance statement indicating that it supports FHIR version \"0.4\" which corresponds to DSTU2, but this client is configured to use DSTU1 (at FhirContext creation time)"));
		}
	}
}
