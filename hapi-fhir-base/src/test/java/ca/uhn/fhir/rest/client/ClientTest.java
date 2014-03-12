package ca.uhn.fhir.rest.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
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
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.server.Constants;

public class ClientTest {

	private HttpClient httpClient;
	private HttpResponse httpResponse;
	private FhirContext ctx;
	private IRestfulClientFactory clientFactory;

	// atom-document-large.xml

	@Before
	public void before() {
		ctx = new FhirContext(Patient.class, Conformance.class);
		clientFactory = ctx.newRestfulClientFactory();

		httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		clientFactory.setHttpClient(httpClient);

		httpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testRead() throws Exception {

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

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = clientFactory.newClient(ITestClient.class, "http://foo");
		// Patient response = client.findPatientByMrn(new IdentifierDt("urn:foo", "123"));
		Patient response = client.getPatientById(new IdDt("111"));

		assertEquals("http://foo/Patient/111", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testVRead() throws Exception {

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

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = clientFactory.newClient(ITestClient.class, "http://foo");
		// Patient response = client.findPatientByMrn(new IdentifierDt("urn:foo", "123"));
		Patient response = client.getPatientByVersionId(new IdDt("111"), new IdDt("999"));

		assertEquals("http://foo/Patient/111/_history/999", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testSearchByToken() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = clientFactory.newClient(ITestClient.class, "http://foo");
		Patient response = client.findPatientByMrn(new IdentifierDt("urn:foo", "123"));

		assertEquals("http://foo/Patient?identifier=urn%3Afoo%7C123", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testGetConformance() throws Exception {

		String msg = IOUtils.toString(ClientTest.class.getResourceAsStream("/example-metadata.xml"));

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = clientFactory.newClient(ITestClient.class, "http://foo");
		Conformance response = client.getServerConformanceStatement();

		assertEquals("http://foo/metadata", capt.getValue().getURI().toString());
		assertEquals("Health Intersections", response.getPublisher().getValue());

	}

	@Test
	public void testSearchWithOptionalParam() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = clientFactory.newClient(ITestClient.class, "http://foo");
		Bundle response = client.findPatientByName(new StringDt("AAA"), null);

		assertEquals("http://foo/Patient?family=AAA", capt.getValue().getURI().toString());
		Patient resource = (Patient) response.getEntries().get(0).getResource();
		assertEquals("PRP1660", resource.getIdentifier().get(0).getValue().getValue());

		/*
		 * Now with a first name
		 */

		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		client = clientFactory.newClient(ITestClient.class, "http://foo");
		response = client.findPatientByName(new StringDt("AAA"), new StringDt("BBB"));

		assertEquals("http://foo/Patient?family=AAA&given=BBB", capt.getValue().getURI().toString());
		resource = (Patient) response.getEntries().get(0).getResource();
		assertEquals("PRP1660", resource.getIdentifier().get(0).getValue().getValue());

	}

	private String getPatientFeedWithOneResult() {
		//@formatter:off
		String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
				"<title/>\n" + 
				"<id>d039f91a-cc3c-4013-988e-af4d8d0614bd</id>\n" + 
				"<os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">1</os:totalResults>\n" + 
				"<published>2014-03-11T16:35:07-04:00</published>\n" + 
				"<author>\n" + 
				"<name>ca.uhn.fhir.rest.server.DummyRestfulServer</name>\n" + 
				"</author>\n" + 
				"<entry>\n" + 
				"<content type=\"text/xml\">" 
				+ "<Patient xmlns=\"http://hl7.org/fhir\">" 
				+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div></text>"
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
				+ "<name><family value=\"Kramer\" /><given value=\"Doe\" /></name>"
				+ "<telecom><system value=\"phone\" /><value value=\"555-555-2004\" /><use value=\"work\" /></telecom>"
				+ "<gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\" /><code value=\"M\" /></coding></gender>"
				+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
				+ "</Patient>"
				+ "</content>\n"  
				+ "   </entry>\n"  
				+ "</feed>";
		//@formatter:on
		return msg;
	}
}
