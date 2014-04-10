package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringEndsWith;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QualifiedDateParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;

public class ClientTest {

	private HttpClient httpClient;
	private HttpResponse httpResponse;
	private FhirContext ctx;

	// atom-document-large.xml

	@Before
	public void before() {
		ctx = new FhirContext(Patient.class, Conformance.class);

		httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ctx.getRestfulClientFactory().setHttpClient(httpClient);

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

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		// Patient response = client.findPatientByMrn(new IdentifierDt("urn:foo", "123"));
		Patient response = client.getPatientById(new IdDt("111"));

		assertEquals("http://foo/Patient/111", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}

	
	@Test
	public void testCreate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location" , "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.createPatient(patient);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertEquals("100", response.getId().getValue());
		assertEquals("200", response.getVersionId().getValue());
	}
	
	
	@Test
	public void testDelete() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDetails("Hello");
		String resp = new FhirContext().newXmlParser().encodeResourceToString(oo);
				
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(resp), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.deletePatient(new IdDt("1234"));

		assertEquals(HttpDelete.class, capt.getValue().getClass());
		assertEquals("http://foo/Patient/1234", capt.getValue().getURI().toString());
		assertEquals("Hello", response.getOperationOutcome().getIssueFirstRep().getDetails().getValue());
	}
	
	@Test
	public void testDeleteNoResponse() throws Exception {
				
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 204, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		client.deleteDiagnosticReport(new IdDt("1234"));

		assertEquals(HttpDelete.class, capt.getValue().getClass());
		assertEquals("http://foo/DiagnosticReport/1234", capt.getValue().getURI().toString());
	}
	
	
	@Test
	public void testUpdate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location" , "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.updatePatient(new IdDt("100"), patient);

		assertEquals(HttpPut.class, capt.getValue().getClass());
		HttpPut post = (HttpPut) capt.getValue();
		assertThat(post.getURI().toASCIIString(), StringEndsWith.endsWith("/Patient/100"));
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertEquals("100", response.getId().getValue());
		assertEquals("200", response.getVersionId().getValue());
	}

	/**
	 * Return a FHIR content type, but no content and make sure we handle this without crashing
	 */
	@Test
	public void testUpdateWithEmptyResponse() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location" , "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		client.updatePatient(new IdDt("100"), new IdDt("200"), patient);
	}

	@Test
	public void testUpdateWithVersion() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location" , "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.updatePatient(new IdDt("100"), new IdDt("200"), patient);

		assertEquals(HttpPut.class, capt.getValue().getClass());
		HttpPut post = (HttpPut) capt.getValue();
		assertThat(post.getURI().toASCIIString(), StringEndsWith.endsWith("/Patient/100"));
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertThat(post.getFirstHeader("Content-Location").getValue(), StringEndsWith.endsWith("/Patient/100/_history/200"));
		assertEquals("100", response.getId().getValue());
		assertEquals("200", response.getVersionId().getValue());
	}

	
	@Test(expected=ResourceVersionConflictException.class)
	public void testUpdateWithResourceConflict() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_409_CONFLICT, "Conflict"));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		client.updatePatient(new IdDt("100"), new IdDt("200"), patient);
	}

	
	@Test
	public void testCreateBad() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("foobar"), Charset.forName("UTF-8")));

		try {
			ctx.newRestfulClient(ITestClient.class, "http://foo").createPatient(patient);
			fail();
		}catch (InvalidRequestException e) {
			assertThat(e.getMessage(), StringContains.containsString("foobar"));
		}
	}

	private Header[] toHeaderArray(String theName, String theValue) {
		return new Header[] {new BasicHeader(theName, theValue)};
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

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
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

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		Patient response = client.findPatientByMrn(new IdentifierDt("urn:foo", "123"));

		assertEquals("http://foo/Patient?identifier=urn%3Afoo%7C123", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}
	
	
	@Test
	public void testSearchByDateRange() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		DateRangeParam param = new DateRangeParam();
		param.setLowerBound(new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-01"));
		param.setUpperBound(new QualifiedDateParam(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS, "2021-01-01"));
		List<Patient> response = client.getPatientByDateRange(param);

		assertEquals("http://foo/Patient?dateRange=%3E%3D2011-01-01&dateRange=%3C%3D2021-01-01", capt.getValue().getURI().toString());

	}
	


	
	
	@Test
	public void testSearchByDob() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		List<Patient> response = client.getPatientByDob(new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));

		assertEquals("http://foo/Patient?birthdate=%3E%3D2011-01-02", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.get(0).getIdentifier().get(0).getValue().getValue());

	}
	
	@Test
	public void testSearchWithIncludes() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		client.getPatientWithIncludes(new StringDt("aaa"), Arrays.asList(new PathSpecification[] {new PathSpecification("inc1"), new PathSpecification("inc2")}));

		assertEquals("http://foo/Patient?withIncludes=aaa&_include=inc1&_include=inc2", capt.getValue().getURI().toString());
		
	}
	
	@Test
	public void testSearchComposite() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		CodingListParam identifiers = new CodingListParam();
		identifiers.add(new CodingDt("foo", "bar"));
		identifiers.add(new CodingDt("baz", "boz"));
		List<Patient> response = client.getPatientMultipleIdentifiers(identifiers);

		assertEquals("http://foo/Patient?ids=foo%7Cbar%2Cbaz%7Cboz", capt.getValue().getURI().toString());

	}
	
	@Test
	public void testGetConformance() throws Exception {

		String msg = IOUtils.toString(ClientTest.class.getResourceAsStream("/example-metadata.xml"));

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
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

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		Bundle response = client.findPatientByName(new StringDt("AAA"), null);

		assertEquals("http://foo/Patient?family=AAA", capt.getValue().getURI().toString());
		Patient resource = (Patient) response.getEntries().get(0).getResource();
		assertEquals("PRP1660", resource.getIdentifier().get(0).getValue().getValue());

		/*
		 * Now with a first name
		 */

		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		response = client.findPatientByName(new StringDt("AAA"), new StringDt("BBB"));

		assertEquals("http://foo/Patient?family=AAA&given=BBB", capt.getValue().getURI().toString());
		resource = (Patient) response.getEntries().get(0).getResource();
		assertEquals("PRP1660", resource.getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testSearchNamedQueryOneParam() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		client.getPatientOneParam(new StringDt("BB"));

		assertEquals("http://foo/Patient?_query=someQueryOneParam&param1=BB", capt.getValue().getURI().toString());

	}
	
	@Test
	public void testSearchNamedQueryNoParams() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ctx.newRestfulClient(ITestClient.class, "http://foo");
		client.getPatientNoParams();

		assertEquals("http://foo/Patient?_query=someQueryNoParams", capt.getValue().getURI().toString());

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
