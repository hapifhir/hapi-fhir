package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseResourceReferenceDtTest {

	private static FhirContext ourCtx;

	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@BeforeEach
	public void before() {
		ourCtx = FhirContext.forDstu2();
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	private ArgumentCaptor<HttpUriRequest> fixtureJson() throws IOException {
		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String input = ourCtx.newJsonParser().encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Sat, 20 Jun 2015 19:32:17 GMT") });
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(input), StandardCharsets.UTF_8);
			}
		});
		return capt;
	}

	private ArgumentCaptor<HttpUriRequest> fixtureXml() throws IOException {
		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String input = ourCtx.newXmlParser().encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Sat, 20 Jun 2015 19:32:17 GMT") });
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(input), StandardCharsets.UTF_8);
			}
		});
		return capt;
	}

	@Test
	public void testLoadResourceFromAnnotationClientJson() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = fixtureJson();

		IClientType client = ourCtx.newRestfulClient(IClientType.class, "http://example.com/fhir");

		ResourceReferenceDt ref = new ResourceReferenceDt();
		ref.setReference("http://domain2.example.com/base/Patient/123");
		Patient response = (Patient) ref.loadResource(client);

		assertEquals("http://domain2.example.com/base/Patient/123", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
		assertEquals("http://domain2.example.com/base/Patient/123", response.getId().getValue());
	}

	@Test
	public void testErrors() {
		IClientType client = ourCtx.newRestfulClient(IClientType.class, "http://example.com/fhir");

		try {
			new ResourceReferenceDt().loadResource(client);
			fail();
		} catch (IllegalStateException e) {
			assertEquals(Msg.code(1905) + "Reference has no resource ID defined", e.getMessage());
		}

		try {
			new ResourceReferenceDt("123").loadResource(client);
			fail();
		} catch (IllegalStateException e) {
			assertEquals(Msg.code(1906) + "Reference is not complete (must be in the form [baseUrl]/[resource type]/[resource ID]) - Reference is: 123", e.getMessage());
		}

		try {
			new ResourceReferenceDt("Patient/123").loadResource(client);
			fail();
		} catch (IllegalStateException e) {
			assertEquals(Msg.code(1906) + "Reference is not complete (must be in the form [baseUrl]/[resource type]/[resource ID]) - Reference is: Patient/123", e.getMessage());
		}

		try {
			new ResourceReferenceDt("http://foo/123123").loadResource(client);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1684) + "Unknown resource name \"123123\" (this name is not known in FHIR version \"DSTU2\")", e.getMessage());
		}

		try {
			new ResourceReferenceDt("http://foo/Sometype/123123").loadResource(client);
			fail();
		} catch (DataFormatException e) {
			e.printStackTrace();
			assertEquals(Msg.code(1684) + "Unknown resource name \"Sometype\" (this name is not known in FHIR version \"DSTU2\")", e.getMessage());
		}
	
	}

	@Test
	public void testReturnAlreadyLoadedInstance() throws IOException {
		ArgumentCaptor<HttpUriRequest> capt = fixtureJson();
		IClientType client = ourCtx.newRestfulClient(IClientType.class, "http://example4.com/fhir");

		Patient pat = new Patient();

		ResourceReferenceDt ref = new ResourceReferenceDt();
		ref.setReference("http://domain2.example.com/base/Patient/123");
		ref.setResource(pat);
		Patient response = (Patient) ref.loadResource(client);

		assertEquals(0, capt.getAllValues().size());
		assertSame(pat, response);
	}
	
	@Test
	public void testLoadResourceFromAnnotationClientXml() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = fixtureXml();

		IClientType client = ourCtx.newRestfulClient(IClientType.class, "http://example3.com/fhir");

		ResourceReferenceDt ref = new ResourceReferenceDt();
		ref.setReference("http://domain2.example.com/base/Patient/123");
		Patient response = (Patient) ref.loadResource(client);

		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
		assertEquals("http://domain2.example.com/base/Patient/123", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testLoadResourceFromGenericClientJson() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = fixtureJson();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example1.com/fhir");

		ResourceReferenceDt ref = new ResourceReferenceDt();
		ref.setReference("http://domain2.example.com/base/Patient/123");
		Patient response = (Patient) ref.loadResource(client);

		assertEquals("http://domain2.example.com/base/Patient/123", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testLoadResourceFromGenericClientXml() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = fixtureXml();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example2.com/fhir");

		ResourceReferenceDt ref = new ResourceReferenceDt();
		ref.setReference("http://domain2.example.com/base/Patient/123");
		Patient response = (Patient) ref.loadResource(client);

		assertEquals("http://domain2.example.com/base/Patient/123", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
	}

	public interface IClientType extends IRestfulClient {

	}
}
