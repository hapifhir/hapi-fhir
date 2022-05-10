package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.NotModifiedException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.core.StringContains;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ETagClientDstu2Test {

	private static FhirContext ourCtx;
	private HttpClient myHttpClient;

	private HttpResponse myHttpResponse;

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeEach
	public void before() {

		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.NEVER);

		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	private String getResourceResult() {
		//@formatter:off
		String msg = 
				"<Patient xmlns=\"http://hl7.org/fhir\">" 
				+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div></text>"
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
				+ "<name><family value=\"Kramer\" /><given value=\"Doe\" /></name>"
				+ "<telecom><system value=\"phone\" /><value value=\"555-555-2004\" /><use value=\"work\" /></telecom>"
				+ "<gender value=\"male\"/>"
				+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
				+ "</Patient>";
		//@formatter:on
		return msg;
	}

	private Patient getResource() {
		return ourCtx.newXmlParser().parseResource(Patient.class, getResourceResult());
	}

	@Test
	public void testReadWithContentLocationInResponse() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		Header[] headers = new Header[] { 
				new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
				new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
				new BasicHeader(Constants.HEADER_ETAG, "\"9999\"")
				};
		//@formatter:on
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response = client.read(Patient.class, new IdDt("Patient/1234"));

		assertEquals("http://foo.com/Patient/123/_history/2333", response.getId().getValue());

		InstantDt lm = (InstantDt) response.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		lm.setTimeZoneZulu(true);
		assertEquals("1995-11-15T04:58:08.000Z", lm.getValueAsString());

	}

	@Test
	public void testReadWithIfNoneMatch() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_304_NOT_MODIFIED, "Not modified"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;

		//@formatter:off
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("")));
		try {
			client
				.read()
				.resource(Patient.class)
				.withId(new IdDt("Patient/1234"))
				.execute();
			fail();
		} catch (NotModifiedException e) {
			// good!
		}
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count).getURI().toString());
		count++;

		//@formatter:off
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("")));
		Patient expected = new Patient();
		Patient response = client
			.read()
			.resource(Patient.class)
			.withId(new IdDt("Patient/1234"))
			.ifVersionMatches("9876").returnResource(expected)
			.execute();
		//@formatter:on
		assertSame(expected, response);
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count).getURI().toString());
		assertEquals("\"9876\"", capt.getAllValues().get(count).getHeaders(Constants.HEADER_IF_NONE_MATCH_LC)[0].getValue());
		count++;

	}

	@Test
	public void testUpdateWithIfMatch() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_200_OK, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;

		//@formatter:off
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("")));
		client
			.update()
			.resource(getResource())
			.withId(new IdDt("Patient/1234"))
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count).getURI().toString());
		assertEquals(0, capt.getAllValues().get(count).getHeaders(Constants.HEADER_IF_MATCH_LC).length);
		count++;

		//@formatter:off
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("")));
		client
			.update()
			.resource(getResource())
			.withId(new IdDt("Patient/1234/_history/9876"))
		.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count).getURI().toString());
		assertEquals("\"9876\"", capt.getAllValues().get(count).getHeaders(Constants.HEADER_IF_MATCH_LC)[0].getValue());
		count++;

	}

	@Test
	public void testUpdateWithIfMatchWithPreconditionFailed() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_412_PRECONDITION_FAILED, "Precondition Failed"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;

		//@formatter:off
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("")));
		try {
			client
				.update()
				.resource(getResource())
				.withId(new IdDt("Patient/1234/_history/9876"))
				.execute();
			fail();
		} catch (PreconditionFailedException e) {
			// good
		}
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count).getURI().toString());
		assertEquals("\"9876\"", capt.getAllValues().get(count).getHeaders(Constants.HEADER_IF_MATCH_LC)[0].getValue());
		count++;

		//@formatter:off
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("")));
		try {
			Patient resource = getResource();
			resource.setId(new IdDt("Patient/1234/_history/9876"));
			client
				.update()
				.resource(resource)
				.execute();
			fail();
		} catch (PreconditionFailedException e) {
			// good
		}
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count).getURI().toString());
		assertEquals("\"9876\"", capt.getAllValues().get(count).getHeaders(Constants.HEADER_IF_MATCH_LC)[0].getValue());
		count++;
	}

	@Test
	public void testReadWithETag() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		Header[] headers = new Header[] {
				new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
				new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int count = 0;

		Patient response = client.read().resource(Patient.class).withId(new IdDt("Patient/1234")).execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		response = (Patient) client.read().resource("Patient").withId("1234").execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		response = client.read().resource(Patient.class).withIdAndVersion("1234", "22").execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://example.com/fhir/Patient/1234/_history/22", capt.getAllValues().get(count++).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		response = client.read().resource(Patient.class).withUrl("http://foo/Patient/22").execute();
		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));
		assertEquals("http://foo/Patient/22", capt.getAllValues().get(count++).getURI().toString());

	}

	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu2();
	}

}
