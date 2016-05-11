package ca.uhn.fhir.rest.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class BasicAuthInterceptorTest {

	private static FhirContext ourCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@Before
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.NEVER);

		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	private String crerateMsg() {
		//@formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">" 
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
	public void testRequest() throws Exception {
		String msg = crerateMsg();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		Header[] headers = new Header[] {};

		when(myHttpResponse.getAllHeaders()).thenReturn(headers);
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.registerInterceptor(new BasicAuthInterceptor("myuser", "mypass"));
		client.getPatientById(new IdDt("111"));

		assertEquals(1, capt.getAllValues().size());
		HttpUriRequest req = capt.getAllValues().get(0);
		assertEquals(1, req.getHeaders("Authorization").length);
		assertEquals("Basic bXl1c2VyOm15cGFzcw==", req.getFirstHeader("Authorization").getValue());

		// Create a second client and make sure we get the same results

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.registerInterceptor(new BasicAuthInterceptor("myuser", "mypass"));
		client.getPatientById(new IdDt("111"));

		assertEquals(2, capt.getAllValues().size());
		req = capt.getAllValues().get(1);
		assertEquals(1, req.getHeaders("Authorization").length);
		assertEquals("Basic bXl1c2VyOm15cGFzcw==", req.getFirstHeader("Authorization").getValue());

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
