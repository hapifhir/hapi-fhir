package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringEndsWith;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.annotation.Elements;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.apache.ApacheHttpRequest;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.UrlUtil;

public class ClientDstu1Test {

	private FhirContext ourCtx;
	private HttpClient httpClient;
	private HttpResponse httpResponse;

	@Before
	public void before() {
		ourCtx = FhirContext.forDstu1();

		httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(httpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		httpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	// atom-document-large.xml

	private String getPatientFeedWithOneResult() {
		//@formatter:off
		String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
				"<title/>\n" + 
				"<id>d039f91a-cc3c-4013-988e-af4d8d0614bd</id>\n" + 
				"<os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">1</os:totalResults>\n" + 
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

	@Test
	public void testCreate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location", "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		CapturingInterceptor interceptor = new CapturingInterceptor();
		client.registerInterceptor(interceptor);

		MethodOutcome response = client.createPatient(patient);

		assertEquals(((ApacheHttpRequest) interceptor.getLastRequest()).getApacheRequest().getURI().toASCIIString(), "http://foo/Patient");

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertEquals("http://example.com/fhir/Patient/100/_history/200", response.getId().getValue());
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(0).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals("200", response.getId().getVersionIdPart());
	}

	@Test
	public void testCreateBad() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, "foobar"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("foobar"), Charset.forName("UTF-8")));

		try {
			ourCtx.newRestfulClient(ITestClient.class, "http://foo").createPatient(patient);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), StringContains.containsString("foobar"));
		}
	}

	/**
	 * Some servers (older ones?) return the resourcde you created instead of an OperationOutcome. We just need to ignore
	 * it.
	 */
	@Test
	public void testCreateWithResourceResponse() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ourCtx.newXmlParser().encodeResourceToString(patient)), Charset.forName("UTF-8")));
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location", "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.createPatient(patient);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertEquals("http://example.com/fhir/Patient/100/_history/200", response.getId().getValue());
		assertEquals("200", response.getId().getVersionIdPart());
	}

	@Test
	public void testCreateWithTagList() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location", "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		TagList tagList = new TagList();
		tagList.add(new Tag((String) null, "Dog", "DogLabel"));
		tagList.add(new Tag("http://cats", "Cat", "CatLabel"));
		patient.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);

		MethodOutcome response = client.createPatient(patient);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertEquals("http://example.com/fhir/Patient/100/_history/200", response.getId().getValue());
		assertEquals("200", response.getId().getVersionIdPart());

		Header[] headers = post.getHeaders("Category");
		assertEquals(2, headers.length);
		assertEquals("Dog; label=\"DogLabel\"", headers[0].getValue());
		assertEquals("Cat; label=\"CatLabel\"; scheme=\"http://cats\"", headers[1].getValue());

	}

	@Test
	public void testDelete() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDetails("Hello");
		String resp = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(resp), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.deletePatient(new IdDt("1234"));

		assertEquals(HttpDelete.class, capt.getValue().getClass());
		assertEquals("http://foo/Patient/1234", capt.getValue().getURI().toString());
		assertEquals("Hello", ((OperationOutcome) response.getOperationOutcome()).getIssueFirstRep().getDetailsElement().getValue());
	}

	@Test
	public void testDeleteNoResponse() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 204, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.deleteDiagnosticReport(new IdDt("1234"));

		assertEquals(HttpDelete.class, capt.getValue().getClass());
		assertEquals("http://foo/DiagnosticReport/1234", capt.getValue().getURI().toString());
	}

	@Test
	public void testGetConformance() throws Exception {

		String msg = IOUtils.toString(ClientDstu1Test.class.getResourceAsStream("/example-metadata.xml"));

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		BaseConformance response = client.getServerConformanceStatement();

		assertEquals("http://foo/metadata", capt.getValue().getURI().toString());
		assertEquals("Health Intersections", response.getPublisherElement().getValue());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testHistoryResourceInstance() throws Exception {

		InstantDt date1 = new InstantDt(new Date(20000L));
		InstantDt date2 = new InstantDt(new Date(10000L));
		InstantDt date3 = new InstantDt(new Date(30000L));
		InstantDt date4 = new InstantDt(new Date(10000L));

		//@formatter:off
		String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\"><title/><id>6c1d93be-027f-468d-9d47-f826cd15cf42</id>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history\"/>"
				+ "<link rel=\"fhir-base\" href=\"http://localhost:51698\"/><os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">2</os:totalResults>"
				+ "<author><name>ca.uhn.fhir.rest.method.HistoryMethodBinding</name></author>"
				+ "<entry><title>Patient 222</title><id>222</id>"
				+ "<updated>"+date1.getValueAsString()+"</updated>"
				+ "<published>"+date2.getValueAsString()+"</published>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history/1\"/>"
				+ "<content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><identifier><use value=\"official\"/><system value=\"urn:hapitest:mrns\"/><value value=\"00001\"/></identifier><name><family value=\"OlderFamily\"/><given value=\"PatientOne\"/></name><gender><text value=\"M\"/></gender></Patient></content>"
				+ "</entry>"
				+ "<entry><title>Patient 222</title><id>222</id>"
				+ "<updated>"+date3.getValueAsString()+"</updated>"
				+ "<published>"+date4.getValueAsString()+"</published>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history/2\"/><content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><identifier><use value=\"official\"/><system value=\"urn:hapitest:mrns\"/><value value=\"00001\"/></identifier><name><family value=\"NewerFamily\"/><given value=\"PatientOne\"/></name><gender><text value=\"M\"/></gender></Patient></content></entry></feed>";
		//@formatter:on

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		Bundle response = client.getHistoryPatientInstance(new IdDt("111"));

		assertEquals("http://foo/Patient/111/_history", capt.getValue().getURI().toString());

		assertEquals(2, response.getEntries().size());

		// Older resource
		{
			BundleEntry olderEntry = response.getEntries().get(0);
			assertEquals("222", olderEntry.getId().getValue());
			assertThat(olderEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/222/_history/1"));
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = olderEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(20000L));
			InstantDt updActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = olderEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}
		// Newer resource
		{
			BundleEntry newerEntry = response.getEntries().get(1);
			assertEquals("222", newerEntry.getId().getValue());
			assertThat(newerEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/222/_history/2"));
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = newerEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(30000L));
			InstantDt updActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = newerEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testHistoryResourceType() throws Exception {

		InstantDt date1 = new InstantDt(new Date(20000L));
		InstantDt date2 = new InstantDt(new Date(10000L));
		InstantDt date3 = new InstantDt(new Date(30000L));
		InstantDt date4 = new InstantDt(new Date(10000L));

		//@formatter:off
		String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\"><title/><id>6c1d93be-027f-468d-9d47-f826cd15cf42</id>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history\"/>"
				+ "<link rel=\"fhir-base\" href=\"http://localhost:51698\"/><os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">2</os:totalResults>"
				+ "<author><name>ca.uhn.fhir.rest.method.HistoryMethodBinding</name></author>"
				+ "<entry><title>Patient 222</title><id>222</id>"
				+ "<updated>"+date1.getValueAsString()+"</updated>"
				+ "<published>"+date2.getValueAsString()+"</published>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history/1\"/>"
				+ "<content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><identifier><use value=\"official\"/><system value=\"urn:hapitest:mrns\"/><value value=\"00001\"/></identifier><name><family value=\"OlderFamily\"/><given value=\"PatientOne\"/></name><gender><text value=\"M\"/></gender></Patient></content>"
				+ "</entry>"
				+ "<entry><title>Patient 222</title><id>222</id>"
				+ "<updated>"+date3.getValueAsString()+"</updated>"
				+ "<published>"+date4.getValueAsString()+"</published>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history/2\"/><content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><identifier><use value=\"official\"/><system value=\"urn:hapitest:mrns\"/><value value=\"00001\"/></identifier><name><family value=\"NewerFamily\"/><given value=\"PatientOne\"/></name><gender><text value=\"M\"/></gender></Patient></content></entry></feed>";
		//@formatter:on

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		Bundle response = client.getHistoryPatientType();

		assertEquals("http://foo/Patient/_history", capt.getValue().getURI().toString());

		assertEquals(2, response.getEntries().size());

		// Older resource
		{
			BundleEntry olderEntry = response.getEntries().get(0);
			assertEquals("222", olderEntry.getId().getValue());
			assertThat(olderEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/222/_history/1"));
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = olderEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(20000L));
			InstantDt updActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = olderEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}
		// Newer resource
		{
			BundleEntry newerEntry = response.getEntries().get(1);
			assertEquals("222", newerEntry.getId().getValue());
			assertThat(newerEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/222/_history/2"));
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = newerEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(30000L));
			InstantDt updActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = newerEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testHistoryServer() throws Exception {
		InstantDt date1 = new InstantDt(new Date(20000L));
		InstantDt date2 = new InstantDt(new Date(10000L));
		InstantDt date3 = new InstantDt(new Date(30000L));
		InstantDt date4 = new InstantDt(new Date(10000L));

		//@formatter:off
		String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\"><title/><id>6c1d93be-027f-468d-9d47-f826cd15cf42</id>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history\"/>"
				+ "<link rel=\"fhir-base\" href=\"http://localhost:51698\"/><os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">2</os:totalResults>"
				+ "<author><name>ca.uhn.fhir.rest.method.HistoryMethodBinding</name></author>"
				+ "<entry><title>Patient 222</title><id>222</id>"
				+ "<updated>"+date1.getValueAsString()+"</updated>"
				+ "<published>"+date2.getValueAsString()+"</published>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history/1\"/>"
				+ "<content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><identifier><use value=\"official\"/><system value=\"urn:hapitest:mrns\"/><value value=\"00001\"/></identifier><name><family value=\"OlderFamily\"/><given value=\"PatientOne\"/></name><gender><text value=\"M\"/></gender></Patient></content>"
				+ "</entry>"
				+ "<entry><title>Patient 222</title><id>222</id>"
				+ "<updated>"+date3.getValueAsString()+"</updated>"
				+ "<published>"+date4.getValueAsString()+"</published>"
				+ "<link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history/2\"/><content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><identifier><use value=\"official\"/><system value=\"urn:hapitest:mrns\"/><value value=\"00001\"/></identifier><name><family value=\"NewerFamily\"/><given value=\"PatientOne\"/></name><gender><text value=\"M\"/></gender></Patient></content></entry></feed>";
		//@formatter:on

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		Bundle response = client.getHistoryServer();

		assertEquals("http://foo/_history", capt.getValue().getURI().toString());

		assertEquals(2, response.getEntries().size());

		// Older resource
		{
			BundleEntry olderEntry = response.getEntries().get(0);
			assertEquals("222", olderEntry.getId().getValue());
			assertThat(olderEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/222/_history/1"));
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = olderEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(20000L));
			InstantDt updActualRes = (InstantDt) olderEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = olderEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}
		// Newer resource
		{
			BundleEntry newerEntry = response.getEntries().get(1);
			assertEquals("222", newerEntry.getId().getValue());
			assertThat(newerEntry.getLinkSelf().getValue(), StringEndsWith.endsWith("/Patient/222/_history/2"));
			InstantDt pubExpected = new InstantDt(new Date(10000L));
			InstantDt pubActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
			InstantDt pubActualBundle = newerEntry.getPublished();
			assertEquals(pubExpected.getValueAsString(), pubActualRes.getValueAsString());
			assertEquals(pubExpected.getValueAsString(), pubActualBundle.getValueAsString());
			InstantDt updExpected = new InstantDt(new Date(30000L));
			InstantDt updActualRes = (InstantDt) newerEntry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			InstantDt updActualBundle = newerEntry.getUpdated();
			assertEquals(updExpected.getValueAsString(), updActualRes.getValueAsString());
			assertEquals(updExpected.getValueAsString(), updActualBundle.getValueAsString());
		}
	}

	@Test
	public void testHistoryWithParams() throws Exception {

		//@formatter:off
		final String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\"><title/><id>6c1d93be-027f-468d-9d47-f826cd15cf42</id><link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history\"/><link rel=\"fhir-base\" href=\"http://localhost:51698\"/><os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">2</os:totalResults><author><name>ca.uhn.fhir.rest.method.HistoryMethodBinding</name></author><entry><title>Patient 222</title><id>222</id><updated>1969-12-31T19:00:20.000-05:00</updated><published>1969-12-31T19:00:10.000-05:00</published><link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history/1\"/><content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><identifier><use value=\"official\"/><system value=\"urn:hapitest:mrns\"/><value value=\"00001\"/></identifier><name><family value=\"OlderFamily\"/><given value=\"PatientOne\"/></name><gender><text value=\"M\"/></gender></Patient></content></entry><entry><title>Patient 222</title><id>222</id><updated>1969-12-31T19:00:30.000-05:00</updated><published>1969-12-31T19:00:10.000-05:00</published><link rel=\"self\" href=\"http://localhost:51698/Patient/222/_history/2\"/><content type=\"text/xml\"><Patient xmlns=\"http://hl7.org/fhir\"><identifier><use value=\"official\"/><system value=\"urn:hapitest:mrns\"/><value value=\"00001\"/></identifier><name><family value=\"NewerFamily\"/><given value=\"PatientOne\"/></name><gender><text value=\"M\"/></gender></Patient></content></entry></feed>";
		//@formatter:on

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");

		// ensures the local timezone
		String expectedDateString = new InstantDt(new InstantDt("2012-01-02T12:01:02").getValue()).getValueAsString();
		expectedDateString = expectedDateString.replace(":", "%3A").replace("+", "%2B");

		client.getHistoryPatientInstance(new IdDt("111"), new InstantDt("2012-01-02T12:01:02"), new IntegerDt(12));
		assertThat(capt.getAllValues().get(0).getURI().toString(), containsString("http://foo/Patient/111/_history?"));
		assertThat(capt.getAllValues().get(0).getURI().toString(), containsString("_since=" + expectedDateString.replaceAll("\\..*", "")));
		assertThat(capt.getAllValues().get(0).getURI().toString(), containsString("_count=12"));

		client.getHistoryPatientInstance(new IdDt("111"), new InstantDt("2012-01-02T12:01:02").getValue(), new IntegerDt(12).getValue());
		assertThat(capt.getAllValues().get(1).getURI().toString(), containsString("http://foo/Patient/111/_history?"));
		assertThat(capt.getAllValues().get(1).getURI().toString(), containsString("_since=" + expectedDateString));
		assertThat(capt.getAllValues().get(1).getURI().toString(), containsString("_count=12"));

		client.getHistoryPatientInstance(new IdDt("111"), null, new IntegerDt(12));
		assertEquals("http://foo/Patient/111/_history?_count=12", capt.getAllValues().get(2).getURI().toString());

		client.getHistoryPatientInstance(new IdDt("111"), new InstantDt("2012-01-02T00:01:02"), null);
		assertEquals("http://foo/Patient/111/_history?_since=2012-01-02T00%3A01%3A02", capt.getAllValues().get(3).getURI().toString());

		client.getHistoryPatientInstance(new IdDt("111"), new InstantDt(), new IntegerDt(12));
		assertEquals("http://foo/Patient/111/_history?_count=12", capt.getAllValues().get(4).getURI().toString());

		client.getHistoryPatientInstance(new IdDt("111"), new InstantDt("2012-01-02T00:01:02"), new IntegerDt());
		assertEquals("http://foo/Patient/111/_history?_since=2012-01-02T00%3A01%3A02", capt.getAllValues().get(5).getURI().toString());

	}

	@Test
	public void testNonAnnotatedMethodFailsGracefully() {

		// TODO: remove the read annotation and make sure we get a sensible
		// error message to tell the user why the method isn't working
		FhirContext ctx = ourCtx;
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		ClientWithoutAnnotation client = ctx.newRestfulClient(ClientWithoutAnnotation.class, "http://wildfhir.aegis.net/fhir");

		try {
			client.read(new IdDt("8"));
			fail();
		} catch (UnsupportedOperationException e) {
			assertThat(e.getMessage(), containsString("annotation"));
		}

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
		Header[] headers = new Header[] { new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"), new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),
				new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\"; label=\"Some tag\"") };

		when(httpResponse.getAllHeaders()).thenReturn(headers);
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		// Patient response = client.findPatientByMrn(new
		// IdentifierDt("urn:foo", "123"));
		Patient response = client.getPatientById(new IdDt("111"));

		assertEquals("http://foo/Patient/111", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

		assertEquals("http://foo.com/Patient/123/_history/2333", response.getId().getValue());

		InstantDt lm = (InstantDt) response.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		lm.setTimeZoneZulu(true);
		assertEquals("1995-11-15T04:58:08.000Z", lm.getValueAsString());

		TagList tags = ResourceMetadataKeyEnum.TAG_LIST.get(response);
		assertNotNull(tags);
		assertEquals(1, tags.size());
		assertEquals("http://foo/tagdefinition.html", tags.get(0).getTerm());
		assertEquals("http://hl7.org/fhir/tag", tags.get(0).getScheme());
		assertEquals("Some tag", tags.get(0).getLabel());

	}

	@Test
	public void testResponseContainingOldStyleXmlContentType() throws Exception {

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
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "application/fhir+xml; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		// Patient response = client.findPatientByMrn(new
		// IdentifierDt("urn:foo", "123"));
		Patient response = client.getPatientById(new IdDt("111"));

		assertEquals("http://foo/Patient/111", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testReadFailureInternalError() throws Exception {

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "INTERNAL"));
		Header[] headers = new Header[1];
		headers[0] = new BasicHeader(Constants.HEADER_LAST_MODIFIED, "2011-01-02T22:01:02");
		when(httpResponse.getAllHeaders()).thenReturn(headers);
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Internal Failure"), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		try {
			client.getPatientById(new IdDt("111"));
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), containsString("INTERNAL"));
			assertThat(e.getResponseBody(), containsString("Internal Failure"));
		}

	}

	@Test
	public void testReadFailureNoCharset() throws Exception {

		//@formatter:off
		String msg = "<OperationOutcome xmlns=\"http://hl7.org/fhir\"></OperationOutcome>";
		//@formatter:on

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 404, "NOT FOUND"));
		Header[] headers = new Header[1];
		headers[0] = new BasicHeader(Constants.HEADER_LAST_MODIFIED, "2011-01-02T22:01:02");
		when(httpResponse.getAllHeaders()).thenReturn(headers);
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		try {
			client.getPatientById(new IdDt("111"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

	}

	@Test
	public void testReadNoCharset() throws Exception {

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
		Header[] headers = new Header[1];
		headers[0] = new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT");
		when(httpResponse.getAllHeaders()).thenReturn(headers);
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		// Patient response = client.findPatientByMrn(new
		// IdentifierDt("urn:foo", "123"));
		Patient response = client.getPatientById(new IdDt("111"));

		assertEquals("http://foo/Patient/111", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

		InstantDt lm = (InstantDt) response.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		lm.setTimeZoneZulu(true);
		assertEquals("1995-11-15T04:58:08.000Z", lm.getValueAsString());

	}

	@Test
	public void testSearchByDateRange() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		DateRangeParam param = new DateRangeParam();
		param.setLowerBound(new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-01"));
		param.setUpperBound(new DateParam(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS, "2021-01-01"));
		client.getPatientByDateRange(param);

		assertEquals("http://foo/Patient?dateRange=%3E%3D2011-01-01&dateRange=%3C%3D2021-01-01", capt.getValue().getURI().toString());

	}

	@Test
	public void testSearchByDob() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		// httpResponse = new BasicHttpResponse(statusline, catalog, locale)
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		List<Patient> response = client.getPatientByDob(new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));

		assertEquals("http://foo/Patient?birthdate=%3E%3D2011-01-02", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.get(0).getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testSearchWithSummary() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		// httpResponse = new BasicHttpResponse(statusline, catalog, locale)
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);

		ITestClientWithSummary client = ourCtx.newRestfulClient(ITestClientWithSummary.class, "http://foo");

		int idx = 0;

		client.getPatientWithIncludes((SummaryEnum) null);
		assertEquals("http://foo/Patient", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.getPatientWithIncludes(SummaryEnum.COUNT);
		assertEquals("http://foo/Patient?_summary=count", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.getPatientWithIncludes(SummaryEnum.DATA);
		assertEquals("http://foo/Patient?_summary=data", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.getPatientWithIncludes(Arrays.asList(SummaryEnum.DATA));
		assertEquals("http://foo/Patient?_summary=data", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.getPatientWithIncludes(Arrays.asList(SummaryEnum.COUNT, SummaryEnum.DATA));
		assertThat(capt.getAllValues().get(idx).getURI().toString(), either(equalTo("http://foo/Patient?_summary=data&_summary=count")).or(equalTo("http://foo/Patient?_summary=count&_summary=data")));
		idx++;

		client.getPatientWithIncludes(new ArrayList<SummaryEnum>());
		assertEquals("http://foo/Patient", capt.getAllValues().get(idx).getURI().toString());
		idx++;
	}

	@Test
	public void testSearchWithElements() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		// httpResponse = new BasicHttpResponse(statusline, catalog, locale)
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);

		ITestClientWithElements client = ourCtx.newRestfulClient(ITestClientWithElements.class, "http://foo");

		int idx = 0;

		client.getPatientWithIncludes((String) null);
		assertEquals("http://foo/Patient", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.getPatientWithIncludes((Set<String>) null);
		assertEquals("http://foo/Patient", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.getPatientWithIncludes("test");
		assertEquals("http://foo/Patient?_elements=test", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.getPatientWithIncludes("test,foo");
		assertEquals("http://foo/Patient?_elements=test%2Cfoo", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.getPatientWithIncludes(new HashSet<String>(Arrays.asList("test","foo", "")));
		assertEquals("http://foo/Patient?_elements=test%2Cfoo", capt.getAllValues().get(idx).getURI().toString());
		idx++;

	}

	@Test
	public void testSearchByCompartment() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		List<Patient> response = client.getPatientByCompartmentAndDob(new IdDt("123"), new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));

		assertEquals("http://foo/Patient/123/compartmentName?birthdate=%3E%3D2011-01-02", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.get(0).getIdentifier().get(0).getValue().getValue());

		try {
			client.getPatientByCompartmentAndDob(new IdDt(""), new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.toString(), containsString("null or empty for compartment"));
		}

	}

	@Test
	public void testSearchByQuantity() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		Patient response = client.findPatientQuantity(new QuantityParam(QuantityCompararatorEnum.GREATERTHAN, 123L, "foo", "bar"));

		assertEquals("http://foo/Patient?quantityParam=%3E123%7Cfoo%7Cbar", capt.getValue().getURI().toString());
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

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		Patient response = client.findPatientByMrn(new TokenParam("urn:foo", "123"));

		assertEquals("http://foo/Patient?identifier=urn%3Afoo%7C123", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testSearchOrList() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		TokenOrListParam identifiers = new TokenOrListParam();
		identifiers.add(new CodingDt("foo", "bar"));
		identifiers.add(new CodingDt("baz", "boz"));
		client.getPatientMultipleIdentifiers(identifiers);

		assertEquals("http://foo/Patient?ids=foo%7Cbar%2Cbaz%7Cboz", capt.getValue().getURI().toString());

	}

	@Test
	public void testSearchNamedQueryNoParams() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.getPatientNoParams();

		assertEquals("http://foo/Patient?_query=someQueryNoParams", capt.getValue().getURI().toString());

	}

	@Test
	public void testSearchNamedQueryOneParam() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.getPatientOneParam(new StringParam("BB"));

		assertEquals("http://foo/Patient?_query=someQueryOneParam&param1=BB", capt.getValue().getURI().toString());

	}

	@Test
	public void testSearchWithCustomType() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClientWithCustomType client = ourCtx.newRestfulClient(ITestClientWithCustomType.class, "http://foo");
		CustomPatient response = client.getPatientByDob(new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));

		assertEquals("http://foo/Patient?birthdate=%3E%3D2011-01-02", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testSearchWithCustomTypeList() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClientWithCustomTypeList client = ourCtx.newRestfulClient(ITestClientWithCustomTypeList.class, "http://foo");
		List<CustomPatient> response = client.getPatientByDob(new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));

		assertEquals("http://foo/Patient?birthdate=%3E%3D2011-01-02", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.get(0).getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testSearchWithFormatAndPrettyPrint() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		// TODO: document this

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.getPatientByDob(new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));
		assertEquals("http://foo/Patient?birthdate=%3E%3D2011-01-02", capt.getAllValues().get(0).getURI().toString());

		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		client.setEncoding(EncodingEnum.JSON); // this needs to be actually
		// implemented
		client.getPatientByDob(new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));
		assertEquals("http://foo/Patient?birthdate=%3E%3D2011-01-02&_format=json", capt.getAllValues().get(1).getURI().toString());

		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		client.setPrettyPrint(true);
		client.getPatientByDob(new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, "2011-01-02"));
		assertEquals("http://foo/Patient?birthdate=%3E%3D2011-01-02&_format=json&_pretty=true", capt.getAllValues().get(2).getURI().toString());

	}

	@Test
	public void testSearchWithIncludes() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.getPatientWithIncludes(new StringParam("aaa"), Arrays.asList(new Include[] { new Include("inc1"), new Include("inc2", true), new Include("inc3", true) }));

		assertEquals("http://foo/Patient?withIncludes=aaa&_include=inc1&_include%3Arecurse=inc2&_include%3Arecurse=inc3", capt.getValue().getURI().toString());

	}

	@Test
	public void testSearchWithGlobalSummary() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.setSummary(SummaryEnum.DATA);
		client.findPatientByMrn(new TokenParam("sysm", "val"));

		assertEquals("http://foo/Patient?identifier=sysm%7Cval&_summary=data", capt.getValue().getURI().toString());

	}

	
	@Test
	public void testSearchWithOptionalParam() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		Bundle response = client.findPatientByName(new StringParam("AAA"), null);

		assertEquals("http://foo/Patient?family=AAA", capt.getValue().getURI().toString());
		Patient resource = (Patient) response.getEntries().get(0).getResource();
		assertEquals("PRP1660", resource.getIdentifier().get(0).getValue().getValue());

		/*
		 * Now with a first name
		 */

		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		response = client.findPatientByName(new StringParam("AAA"), new StringParam("BBB"));

		assertEquals("http://foo/Patient?family=AAA&given=BBB", capt.getValue().getURI().toString());
		resource = (Patient) response.getEntries().get(0).getResource();
		assertEquals("PRP1660", resource.getIdentifier().get(0).getValue().getValue());

	}

	@Test
	public void testSearchWithEscapedValues() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		StringAndListParam andListParam = new StringAndListParam();
		StringOrListParam orListParam1 = new StringOrListParam().addOr(new StringParam("NE,NE", false)).addOr(new StringParam("NE,NE", false));
		StringOrListParam orListParam2 = new StringOrListParam().addOr(new StringParam("E$E", true));
		StringOrListParam orListParam3 = new StringOrListParam().addOr(new StringParam("NE\\NE", false));
		StringOrListParam orListParam4 = new StringOrListParam().addOr(new StringParam("E|E", true));
		client.findPatient(andListParam.addAnd(orListParam1).addAnd(orListParam2).addAnd(orListParam3).addAnd(orListParam4));
		
		assertThat(capt.getValue().getURI().toString(), containsString("%3A"));
		assertEquals("http://foo/Patient?param=NE\\,NE,NE\\,NE&param=NE\\\\NE&param:exact=E\\$E&param:exact=E\\|E", UrlUtil.unescape(capt.getValue().getURI().toString()));
		
	}

	
	@Test
	public void testSearchByCompositeParam() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		StringParam str = new StringParam("FOO$BAR");
		DateParam date = new DateParam("2001-01-01");
		client.getObservationByNameValueDate(new CompositeParam<StringParam, DateParam>(str, date));

		assertEquals("http://foo/Observation?" + Observation.SP_NAME_VALUE_DATE + "=" + URLEncoder.encode("FOO\\$BAR$2001-01-01", "UTF-8"), capt.getValue().getURI().toString());

	}

	@Test
	public void testSearchWithStringIncludes() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		ITestClientWithStringIncludes client = ourCtx.newRestfulClient(ITestClientWithStringIncludes.class, "http://foo");
		client.getPatientWithIncludes(new StringParam("aaa"), "inc1");

		assertEquals("http://foo/Patient?withIncludes=aaa&_include=inc1", capt.getValue().getURI().toString());

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
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location", "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.updatePatient(new IdDt("100"), patient);

		assertEquals(HttpPut.class, capt.getValue().getClass());
		HttpPut post = (HttpPut) capt.getValue();
		assertThat(post.getURI().toASCIIString(), StringEndsWith.endsWith("/Patient/100"));
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertEquals("http://example.com/fhir/Patient/100/_history/200", response.getId().getValue());
		assertEquals("200", response.getId().getVersionIdPart());
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(0).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
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
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Content-Location", "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.updatePatient(new IdDt("Patient/100/_history/200"), patient);

		assertEquals(HttpPut.class, capt.getValue().getClass());
		HttpPut post = (HttpPut) capt.getValue();
		assertEquals("http://foo/Patient/100", post.getURI().toASCIIString());

		Header h = post.getFirstHeader("content-location");
		assertEquals("Patient/100/_history/200", h.getValue());

	}

	@Test(expected = ResourceVersionConflictException.class)
	public void testUpdateWithResourceConflict() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_409_CONFLICT, "Conflict"));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		client.updatePatient(new IdDt("Patient/100/_history/200"), patient);
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
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location", "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.updatePatient(new IdDt("Patient/100/_history/200"), patient);

		assertEquals(HttpPut.class, capt.getValue().getClass());
		HttpPut post = (HttpPut) capt.getValue();
		assertThat(post.getURI().toASCIIString(), StringEndsWith.endsWith("/Patient/100"));
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertThat(post.getFirstHeader("Content-Location").getValue(), StringEndsWith.endsWith("Patient/100/_history/200"));
		assertEquals("http://example.com/fhir/Patient/100/_history/200", response.getId().getValue());
		assertEquals("200", response.getId().getVersionIdPart());
	}

	@Test
	public void testValidate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier("urn:foo", "123");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		when(httpResponse.getAllHeaders()).thenReturn(toHeaderArray("Location", "http://example.com/fhir/Patient/100/_history/200"));

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		MethodOutcome response = client.validatePatient(patient);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertThat(post.getURI().toASCIIString(), StringEndsWith.endsWith("/Patient/_validate"));
		assertThat(IOUtils.toString(post.getEntity().getContent()), StringContains.containsString("<Patient"));
		assertEquals("http://example.com/fhir/Patient/100/_history/200", response.getId().getValue());
		assertEquals("200", response.getId().getVersionIdPart());

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

		ITestClient client = ourCtx.newRestfulClient(ITestClient.class, "http://foo");
		// Patient response = client.findPatientByMrn(new
		// IdentifierDt("urn:foo", "123"));
		Patient response = client.getPatientById(new IdDt("Patient/111/_history/999"));

		assertEquals("http://foo/Patient/111/_history/999", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getIdentifier().get(0).getValue().getValue());

	}

	private Header[] toHeaderArray(String theName, String theValue) {
		return new Header[] { new BasicHeader(theName, theValue) };
	}

	private interface ClientWithoutAnnotation extends IBasicClient {
		Patient read(@IdParam IdDt theId);
	}

	@ResourceDef(name = "Patient")
	public static class CustomPatient extends Patient {

		private static final long serialVersionUID = 1L;

		// nothing
	}

	public interface ITestClientWithCustomType extends IBasicClient {
		@Search()
		public CustomPatient getPatientByDob(@RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theBirthDate);
	}

	public interface ITestClientWithCustomTypeList extends IBasicClient {
		@Search()
		public List<CustomPatient> getPatientByDob(@RequiredParam(name = Patient.SP_BIRTHDATE) DateParam theBirthDate);
	}

	public interface ITestClientWithStringIncludes extends IBasicClient {
		@Search()
		public Patient getPatientWithIncludes(@RequiredParam(name = "withIncludes") StringParam theString, @IncludeParam String theInclude);
	}

	public interface ITestClientWithSummary extends IBasicClient {
		@Search()
		public List<Patient> getPatientWithIncludes(SummaryEnum theSummary);

		@Search()
		public List<Patient> getPatientWithIncludes(List<SummaryEnum> theSummary);

	}

	public interface ITestClientWithElements extends IBasicClient {
		@Search()
		public List<Patient> getPatientWithIncludes(@Elements String theElements);

		@Search()
		public List<Patient> getPatientWithIncludes(@Elements Set<String> theElements);

	}

}
