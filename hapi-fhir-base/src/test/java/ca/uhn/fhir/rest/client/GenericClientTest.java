package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.method.SearchStyleEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class GenericClientTest {

	private static FhirContext myCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientTest.class);
	private HttpClient myHttpClient;

	private HttpResponse myHttpResponse;

	@Before
	public void before() {

		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		myCtx.getRestfulClientFactory().setHttpClient(myHttpClient);

		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
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

	
	private String getResourceResult() {
		//@formatter:off
		String msg = 
				"<Patient xmlns=\"http://hl7.org/fhir\">" 
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
	public void testSearchByCompartment() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		IGenericClient client = myCtx.newRestfulGenericClient("http://foo");
		//@formatter:off
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.withIdAndCompartment("123", "fooCompartment")
			.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
			.execute();
		//@formatter:on

		assertEquals("http://foo/Patient/123/fooCompartment?birthdate=%3E%3D2011-01-02", capt.getValue().getURI().toString());
		assertEquals("PRP1660", response.getResources(Patient.class).get(0).getIdentifier().get(0).getValue().getValue());

		try {
			//@formatter:off
			client
				.search()
				.forResource(Patient.class)
				.withIdAndCompartment("", "fooCompartment")
				.where(Patient.BIRTHDATE.afterOrEquals().day("2011-01-02"))
				.execute();
			//@formatter:on
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.toString(), containsString("null or empty for compartment"));
		}

	}

	
	
	@Test
	public void testCreateWithTag() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");
		TagList list = new TagList();
		list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
		ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client.create().resource(p1).execute();
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		assertEquals("http://example.com/fhir/Patient", capt.getValue().getURI().toString());
		assertEquals("POST", capt.getValue().getMethod());
		Header catH = capt.getValue().getFirstHeader("Category");
		assertNotNull(Arrays.asList(capt.getValue().getAllHeaders()).toString(), catH);
		assertEquals("urn:happytag; label=\"This is a happy resource\"; scheme=\"http://hl7.org/fhir/tag\"", catH.getValue());
		
		/*
		 * Try fluent options
		 */
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		client.create().resource(p1).withId("123").execute();
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(1).getURI().toString());
		
		String resourceText = "<Patient xmlns=\"http://hl7.org/fhir\">    </Patient>";
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		client.create().resource(resourceText).withId("123").execute();
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(2).getURI().toString());
		assertEquals(resourceText, IOUtils.toString(((HttpPost)capt.getAllValues().get(2)).getEntity().getContent()));
		
	}
	
	@Test
	public void testCreateWithTagNonFluent() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");
		TagList list = new TagList();
		list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
		ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		MethodOutcome outcome = client.create(p1);
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		assertEquals("http://example.com/fhir/Patient", capt.getValue().getURI().toString());
		assertEquals("POST", capt.getValue().getMethod());
		Header catH = capt.getValue().getFirstHeader("Category");
		assertNotNull(Arrays.asList(capt.getValue().getAllHeaders()).toString(), catH);
		assertEquals("urn:happytag; label=\"This is a happy resource\"; scheme=\"http://hl7.org/fhir/tag\"", catH.getValue());
	}

	
	@Test
	public void testDelete() throws Exception {
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().addLocation().setValue("testDelete01");
		String ooStr = myCtx.newXmlParser().encodeResourceToString(oo);
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(ooStr), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		OperationOutcome outcome = client.delete().resourceById("Patient", "123").execute();
		
		assertEquals("http://example.com/fhir/Patient/123", capt.getValue().getURI().toString());
		assertEquals("DELETE", capt.getValue().getMethod());
		assertEquals("testDelete01",outcome.getIssueFirstRep().getLocationFirstRep().getValue());
		
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("LKJHLKJGLKJKLL"), Charset.forName("UTF-8")));
		outcome = client.delete().resourceById(new IdDt("Location", "123","456")).prettyPrint().encodedJson().execute();

		assertEquals("http://example.com/fhir/Location/123?_format=json&_pretty=true", capt.getAllValues().get(1).getURI().toString());
		assertEquals("DELETE", capt.getValue().getMethod());
		assertEquals(null,outcome);
		
	}

	@Test
	public void testGetTags() throws Exception {

		TagList tagList = new TagList();
		tagList.add(new Tag("CCC", "AAA", "BBB"));
		String msg = myCtx.newXmlParser().encodeTagListToString(tagList);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		TagList response = client.getTags()
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/_tags", capt.getValue().getURI().toString());
		assertEquals(1, response.size());
		assertEquals("CCC", response.get(0).getScheme());

		// Now for patient

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		response = client.getTags().forResource(Patient.class)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient/_tags", capt.getValue().getURI().toString());
		assertEquals(1, response.size());
		assertEquals("CCC", response.get(0).getScheme());

	}

	
	@Test
	public void testRead() throws Exception {

		String msg = getResourceResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		Header[] headers = new Header[] {
				new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Wed, 15 Nov 1995 04:58:08 GMT"),
				new BasicHeader(Constants.HEADER_CONTENT_LOCATION, "http://foo.com/Patient/123/_history/2333"),				
				new BasicHeader(Constants.HEADER_CATEGORY, "http://foo/tagdefinition.html; scheme=\"http://hl7.org/fhir/tag\"; label=\"Some tag\"")
		};
		when(myHttpResponse.getAllHeaders()).thenReturn(headers);
	
		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Patient response = client.read(Patient.class, new IdDt("Patient/1234"));
		//@formatter:on

		assertThat(response.getNameFirstRep().getFamilyAsSingleString(), StringContains.containsString("Cardinal"));

		assertEquals("http://foo.com/Patient/123/_history/2333", response.getId().getValue());

		InstantDt lm = (InstantDt) response.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		lm.setTimeZoneZulu(true);
		assertEquals("1995-11-15T04:58:08.000Z", lm.getValueAsString());

		TagList tags = ResourceMetadataKeyEnum.TAG_LIST.get(response);
		assertNotNull(tags);
		assertEquals(1,tags.size());
		assertEquals("http://foo/tagdefinition.html", tags.get(0).getTerm());
		assertEquals("http://hl7.org/fhir/tag",tags.get(0).getScheme());
		assertEquals("Some tag",tags.get(0).getLabel());
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testSearchAllResources() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forAllResources()
				.where(Patient.NAME.matches().value("james"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/?name=james", capt.getValue().getURI().toString());

	}
	@SuppressWarnings("unused")
	@Test
	public void testSearchByComposite() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://foo");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Observation")
				.where(Observation.NAME_VALUE_DATE
						.withLeft(Observation.NAME.exactly().code("FOO$BAR"))
						.withRight(Observation.VALUE_DATE.exactly().day("2001-01-01"))
					  )
				.execute();
		//@formatter:on

		assertEquals("http://foo/Observation?" + Observation.SP_NAME_VALUE_DATE + "=" + URLEncoder.encode("FOO\\$BAR$2001-01-01","UTF-8"), capt.getValue().getURI().toString());

	}
	

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithClientEncodingAndPrettyPrintConfig() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		GenericClient client = (GenericClient) myCtx.newRestfulGenericClient("http://example.com/fhir");
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?_format=json&_pretty=true", capt.getValue().getURI().toString());

	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void testSearchByDate() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.encodedJson()
				.where(Patient.BIRTHDATE.beforeOrEquals().day("2012-01-22"))
				.and(Patient.BIRTHDATE.after().day("2011-01-01"))
				.include(Patient.INCLUDE_MANAGINGORGANIZATION)
				.sort().ascending(Patient.BIRTHDATE)
				.sort().descending(Patient.NAME)
				.limitTo(123)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?birthdate=%3C%3D2012-01-22&birthdate=%3E2011-01-01&_include=Patient.managingOrganization&_sort%3Aasc=birthdate&_sort%3Adesc=name&_count=123&_format=json", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByNumberExact() throws Exception {

		String msg = new FhirContext().newXmlParser().encodeBundleToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Observation.class)
				.where(Observation.VALUE_QUANTITY.greaterThan().number(123).andUnits("foo", "bar"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Observation?value-quantity=%3E123%7Cfoo%7Cbar", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByQuantity() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.where(Encounter.LENGTH.exactly().number(123))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?length=123", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByReferenceProperty() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		//@formatter:off
		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
				.forResource(Patient.class)
				.where(Patient.PROVIDER.hasChainedProperty(Organization.NAME.matches().value("ORG0")))
				.execute();

		assertEquals("http://example.com/fhir/Patient?provider.name=ORG0", capt.getValue().getURI().toString());
		//@formatter:on

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByReferenceSimple() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.PROVIDER.hasId("123"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?provider=123", capt.getValue().getURI().toString());

	}
	
	@SuppressWarnings("unused")
	@Test
	public void testSearchByString() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value("james"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?name=james", capt.getValue().getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().values("AAA", "BBB", "C,C"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?name=" + URLEncoder.encode("AAA,BBB,C\\,C","UTF-8"), capt.getAllValues().get(1).getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByStringExact() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matchesExactly().value("james"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?name%3Aexact=james", capt.getValue().getURI().toString());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchByToken() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().systemAndCode("http://example.com/fhir", "ZZZ"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?identifier=http%3A%2F%2Fexample.com%2Ffhir%7CZZZ", capt.getValue().getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().code("ZZZ"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?identifier=ZZZ", capt.getAllValues().get(1).getURI().toString());

		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));
		//@formatter:off
		response = client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().identifiers(new ca.uhn.fhir.model.dstu.composite.IdentifierDt("A", "B"), new ca.uhn.fhir.model.dstu.composite.IdentifierDt("C", "D")))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?identifier=" + URLEncoder.encode("A|B,C|D", "UTF-8"), capt.getAllValues().get(2).getURI().toString());

	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void testSearchUsingPost() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value("james"))
				.usingStyle(SearchStyleEnum.POST)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient/_search", capt.getValue().getURI().toString());
		
		HttpEntityEnclosingRequestBase enc = (HttpEntityEnclosingRequestBase) capt.getValue();
		UrlEncodedFormEntity ent = (UrlEncodedFormEntity) enc.getEntity();
		String string = IOUtils.toString(ent.getContent());
		ourLog.info(string);
		assertEquals("name=james", string);
	}

	
	@SuppressWarnings("unused")
	@Test
	public void testSearchAutomaticallyUsesPost() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		String longValue = StringUtils.leftPad("", 20000, 'B');
		
		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value(longValue))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient/_search", capt.getValue().getURI().toString());
		
		HttpEntityEnclosingRequestBase enc = (HttpEntityEnclosingRequestBase) capt.getValue();
		UrlEncodedFormEntity ent = (UrlEncodedFormEntity) enc.getEntity();
		String string = IOUtils.toString(ent.getContent());
		ourLog.info(string);
		assertEquals("name="+longValue, string);
	}
	
	
	@SuppressWarnings("unused")
	@Test
	public void testSearchUsingGetSearch() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value("james"))
				.usingStyle(SearchStyleEnum.GET_WITH_SEARCH)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient/_search?name=james", capt.getValue().getURI().toString());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testSearchWithInternalServerError() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "INTERNAL ERRORS"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Server Issues!"), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.search().forResource(Patient.class).execute();
			fail();
		} catch (InternalErrorException e) {
			assertEquals(e.getMessage(), "HTTP 500 INTERNAL ERRORS: Server Issues!");
			assertEquals(e.getResponseBody(), "Server Issues!");
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithNonFhirResponse() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader("Server Issues!"), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.search().forResource(Patient.class).execute();
			fail();
		} catch (NonFhirResponseException e) {
			assertThat(e.getMessage(), StringContains.containsString("Server Issues!"));
		}

	}

	@Test
	public void testTransaction() throws Exception {
		String bundleStr = IOUtils.toString(getClass().getResourceAsStream("/bundle.json"));
		Bundle bundle = myCtx.newJsonParser().parseBundle(bundleStr);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(bundleStr), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.transaction()
				.withBundle(bundle)
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir", capt.getValue().getURI().toString());
		assertEquals(bundle.getEntries().get(0).getId(), response.getEntries().get(0).getId());
	}
	
	
	
	@Test
	public void testTransactionJson() throws Exception {
		String bundleStr = IOUtils.toString(getClass().getResourceAsStream("/bundle.json"));
		Bundle bundle = myCtx.newJsonParser().parseBundle(bundleStr);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(bundleStr), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.transaction()
				.withBundle(bundle)
				.encodedJson()
				.execute();
		//@formatter:on

		HttpEntityEnclosingRequestBase value = (HttpEntityEnclosingRequestBase) capt.getValue();
		
		Header ct = value.getEntity().getContentType();
		assertNotNull(ct);
		assertEquals(Constants.CT_FHIR_JSON + "; charset=UTF-8", ct.getValue());
		
		assertEquals("http://example.com/fhir?_format=json", value.getURI().toString());
		assertThat(IOUtils.toString(value.getEntity().getContent()), StringContains.containsString("\"resourceType\""));
		assertEquals(bundle.getEntries().get(0).getId(), response.getEntries().get(0).getId());
	}
	

	@Test
	public void testUpdate() throws Exception {

		Patient p1 = new Patient();
		p1.addIdentifier("foo:bar", "12345");
		p1.addName().addFamily("Smith").addGiven("John");
		TagList list = new TagList();
		list.addTag("http://hl7.org/fhir/tag", "urn:happytag", "This is a happy resource");
		ResourceMetadataKeyEnum.TAG_LIST.put(p1, list);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "/Patient/44/_history/22") });
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));

		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.update().resource(p1).execute();
			fail();
		} catch (InvalidRequestException e) {
			// should happen because no ID set
		}
		
		assertEquals(0, capt.getAllValues().size());

		p1.setId("44");
		client.update().resource(p1).execute();
		
		assertEquals(1, capt.getAllValues().size());

		MethodOutcome outcome = client.update().resource(p1).execute();
		assertEquals("44", outcome.getId().getIdPart());
		assertEquals("22", outcome.getId().getVersionIdPart());

		assertEquals(2, capt.getAllValues().size());

		assertEquals("http://example.com/fhir/Patient/44", capt.getValue().getURI().toString());
		assertEquals("PUT", capt.getValue().getMethod());
		Header catH = capt.getValue().getFirstHeader("Category");
		assertNotNull(Arrays.asList(capt.getValue().getAllHeaders()).toString(), catH);
		assertEquals("urn:happytag; label=\"This is a happy resource\"; scheme=\"http://hl7.org/fhir/tag\"", catH.getValue());
		
		/*
		 * Try fluent options
		 */
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		client.update().resource(p1).withId("123").execute();
		assertEquals(3, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(2).getURI().toString());
		
		String resourceText = "<Patient xmlns=\"http://hl7.org/fhir\">    </Patient>";
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8")));
		client.update().resource(resourceText).withId("123").execute();
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(3).getURI().toString());
		assertEquals(resourceText, IOUtils.toString(((HttpPut)capt.getAllValues().get(3)).getEntity().getContent()));
		assertEquals(4, capt.getAllValues().size());
		
	}

	@BeforeClass
	public static void beforeClass() {
		myCtx = new FhirContext();
	}

}
