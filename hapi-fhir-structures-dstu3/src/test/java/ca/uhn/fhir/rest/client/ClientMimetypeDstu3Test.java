package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * http://gforge.hl7.org/gf/project/fhir/tracker/?action=TrackerItemEdit&tracker_id=677&tracker_item_id=10199
 */
public class ClientMimetypeDstu3Test {
	private static FhirContext ourCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@BeforeEach
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testMimetypeXmlNew() throws Exception {
		String requestCt = Constants.CT_FHIR_XML_NEW;

		ArgumentCaptor<HttpUriRequest> capt = prepareMimetypePostTest(requestCt, true);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).encodedXml().execute();

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals(Constants.CT_FHIR_XML_NEW, capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_NON_LEGACY, capt.getAllValues().get(0).getFirstHeader("accept").getValue());
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">A PATIENT</div></text></Patient>", extractBodyAsString(capt));
	}

	@Test
	public void testMimetypeXmlLegacy() throws Exception {
		String requestCt = Constants.CT_FHIR_XML;

		ArgumentCaptor<HttpUriRequest> capt = prepareMimetypePostTest(requestCt, true);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).encodedXml().execute();

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals(Constants.CT_FHIR_XML_NEW, capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_NON_LEGACY, capt.getAllValues().get(0).getFirstHeader("accept").getValue());
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">A PATIENT</div></text></Patient>", extractBodyAsString(capt));
	}

	@Test
	public void testMimetypeJsonNew() throws Exception {
		String requestCt = Constants.CT_FHIR_JSON_NEW;

		ArgumentCaptor<HttpUriRequest> capt = prepareMimetypePostTest(requestCt, false);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).encodedJson().execute();

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals(Constants.CT_FHIR_JSON_NEW, capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getFirstHeader("accept").getValue());
		assertEquals("{\"resourceType\":\"Patient\",\"text\":{\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">A PATIENT</div>\"}}", extractBodyAsString(capt));
	}

	@Test
	public void testMimetypeJsonLegacy() throws Exception {
		String requestCt = Constants.CT_FHIR_JSON;

		ArgumentCaptor<HttpUriRequest> capt = prepareMimetypePostTest(requestCt, false);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).encodedJson().execute();

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals(Constants.CT_FHIR_JSON_NEW, capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals(Constants.HEADER_ACCEPT_VALUE_JSON_NON_LEGACY, capt.getAllValues().get(0).getFirstHeader("accept").getValue());
		assertEquals("{\"resourceType\":\"Patient\",\"text\":{\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">A PATIENT</div>\"}}", extractBodyAsString(capt));
	}

	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent(), "UTF-8");
		return body;
	}

	private ArgumentCaptor<HttpUriRequest> prepareMimetypePostTest(String requestCt, boolean theXml) throws IOException, ClientProtocolException {
		final IParser p = theXml ? ourCtx.newXmlParser() : ourCtx.newJsonParser();

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
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", requestCt + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
			}
		});
		return capt;
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu3();
	}

}
