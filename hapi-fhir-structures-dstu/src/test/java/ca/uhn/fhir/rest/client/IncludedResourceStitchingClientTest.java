package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IncludeTest;
import ca.uhn.fhir.rest.server.IncludeTest.ExtPatient;
import ca.uhn.fhir.util.TestUtil;

public class IncludedResourceStitchingClientTest {

	private FhirContext ctx;
	private HttpClient httpClient;
	private HttpResponse httpResponse;

	// atom-document-large.xml

	@Before
	public void before() {
		ctx = new FhirContext(Patient.class, Conformance.class);

		httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ctx.getRestfulClientFactory().setHttpClient(httpClient);
		ctx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.NEVER);

		httpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testWithParam() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), Charset.forName("UTF-8")));

		IGenericClient client = ctx.newRestfulGenericClient( "http://foo");
		Bundle bundle = client.search().forResource("Patient").execute();

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient", get.getURI().toString());
		
		assertEquals(3, bundle.size());
		
		Patient p = (Patient) bundle.getEntries().get(0).getResource();
		List<ExtensionDt> exts = p.getUndeclaredExtensionsByUrl("http://foo");
		assertEquals(1,exts.size());
		ExtensionDt ext = exts.get(0);
		ResourceReferenceDt ref = (ResourceReferenceDt) ext.getValue();
		assertEquals("Organization/o1", ref.getReference().getValue());
		assertNotNull(ref.getResource());
		
	}

	
	@Test
	public void testWithDeclaredExtension() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createLinkedBundle()), Charset.forName("UTF-8")));

		IGenericClient client = ctx.newRestfulGenericClient( "http://foo");
		Bundle bundle = client.search().forResource(IncludeTest.ExtPatient.class).execute();

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient", get.getURI().toString());
		
		assertEquals(4, bundle.size());
		
		ExtPatient p = (ExtPatient) bundle.getEntries().get(0).getResource();
		ResourceReferenceDt ref = (ResourceReferenceDt) p.getSecondOrg();
		assertEquals("Organization/o1", ref.getReference().getValue());
		assertNotNull(ref.getResource());
		
		Organization o1 = (Organization) ref.getResource();
		assertEquals("o2", o1.getPartOf().getReference().toUnqualifiedVersionless().getIdPart());
		assertNotNull(o1.getPartOf().getResource());
		
	}
	
	private String createLinkedBundle() {
		//@formatter:off
		return "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
				"   <title/>\n" + 
				"   <id>6cfcd90e-877a-40c6-a11c-448006712979</id>\n" + 
				"   <link rel=\"self\" href=\"http://localhost:49782/Patient?_query=declaredExtInclude&amp;_pretty=true\"/>\n" + 
				"   <link rel=\"fhir-base\" href=\"http://localhost:49782\"/>\n" + 
				"   <os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">2</os:totalResults>\n" + 
				"   <author>\n" +
				"      <name>HAPI FHIR Server</name>\n" + 
				"   </author>\n" + 
				"   <entry>\n" + 
				"      <title>Patient p1</title>\n" + 
				"      <id>http://localhost:49782/Patient/p1</id>\n" + 
				"      <published>2014-08-12T10:22:19-04:00</published>\n" + 
				"      <link rel=\"self\" href=\"http://localhost:49782/Patient/p1\"/>\n" + 
				"      <content type=\"text/xml\">\n" + 
				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"            <extension url=\"http://foo#secondOrg\">\n" + 
				"               <valueResource>\n" + 
				"                  <reference value=\"Organization/o1\"/>\n" + 
				"               </valueResource>\n" + 
				"            </extension>\n" + 
				"            <identifier>\n" + 
				"               <label value=\"p1\"/>\n" + 
				"            </identifier>\n" + 
				"         </Patient>\n" + 
				"      </content>\n" + 
				"   </entry>\n" + 
				"   <entry>\n" + 
				"      <title>Patient p2</title>\n" + 
				"      <id>http://localhost:49782/Patient/p2</id>\n" + 
				"      <published>2014-08-12T10:22:19-04:00</published>\n" + 
				"      <link rel=\"self\" href=\"http://localhost:49782/Patient/p2\"/>\n" + 
				"      <content type=\"text/xml\">\n" + 
				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"            <extension url=\"http://foo#secondOrg\">\n" + 
				"               <valueResource>\n" + 
				"                  <reference value=\"Organization/o1\"/>\n" + 
				"               </valueResource>\n" + 
				"            </extension>\n" + 
				"            <identifier>\n" + 
				"               <label value=\"p2\"/>\n" + 
				"            </identifier>\n" + 
				"         </Patient>\n" + 
				"      </content>\n" + 
				"   </entry>\n" + 
				"   <entry>\n" + 
				"      <title>Organization o1</title>\n" + 
				"      <id>http://localhost:49782/Organization/o1</id>\n" + 
				"      <published>2014-08-12T10:22:19-04:00</published>\n" + 
				"      <link rel=\"self\" href=\"http://localhost:49782/Organization/o1\"/>\n" + 
				"      <content type=\"text/xml\">\n" + 
				"         <Organization xmlns=\"http://hl7.org/fhir\">\n" + 
				"            <name value=\"o1\"/>\n" + 
				"            <partOf>\n" + 
				"               <reference value=\"Organization/o2\"/>\n" + 
				"            </partOf>\n" + 
				"         </Organization>\n" + 
				"      </content>\n" + 
				"   </entry>\n" + 
				"   <entry>\n" + 
				"      <title>Organization o2</title>\n" + 
				"      <id>http://localhost:49782/Organization/o2</id>\n" + 
				"      <published>2014-08-12T10:22:19-04:00</published>\n" + 
				"      <link rel=\"self\" href=\"http://localhost:49782/Organization/o2\"/>\n" + 
				"      <content type=\"text/xml\">\n" + 
				"         <Organization xmlns=\"http://hl7.org/fhir\">\n" + 
				"            <name value=\"o2\"/>\n" + 
				"         </Organization>\n" + 
				"      </content>\n" + 
				"   </entry>\n" + 
				"</feed>";
		//@formatter:on
	}
	
	
	private String createBundle() {
		//@formatter:on
		return "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
				"   <title/>\n" + 
				"   <id>f051fd86-4daa-48da-80f7-5a0443bf6f11</id>\n" + 
				"   <link rel=\"self\" href=\"http://localhost:49627/Patient?_query=extInclude&amp;_pretty=true\"/>\n" + 
				"   <link rel=\"fhir-base\" href=\"http://localhost:49627\"/>\n" + 
				"   <os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">2</os:totalResults>\n" + 
				"   <author>\n" +
				"      <name>HAPI FHIR Server</name>\n" + 
				"   </author>\n" + 
				"   <entry>\n" + 
				"      <title>Patient p1</title>\n" + 
				"      <id>http://localhost:49627/Patient/p1</id>\n" + 
				"      <published>2014-08-05T15:22:08-04:00</published>\n" + 
				"      <link rel=\"self\" href=\"http://localhost:49627/Patient/p1\"/>\n" + 
				"      <content type=\"text/xml\">\n" + 
				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"            <extension url=\"http://foo\">\n" + 
				"               <valueResource>\n" + 
				"                  <reference value=\"Organization/o1\"/>\n" + 
				"               </valueResource>\n" + 
				"            </extension>\n" + 
				"            <identifier>\n" + 
				"               <label value=\"p1\"/>\n" + 
				"            </identifier>\n" + 
				"         </Patient>\n" + 
				"      </content>\n" + 
				"   </entry>\n" + 
				"   <entry>\n" + 
				"      <title>Patient p2</title>\n" + 
				"      <id>http://localhost:49627/Patient/p2</id>\n" + 
				"      <published>2014-08-05T15:22:08-04:00</published>\n" + 
				"      <link rel=\"self\" href=\"http://localhost:49627/Patient/p2\"/>\n" + 
				"      <content type=\"text/xml\">\n" + 
				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"            <extension url=\"http://foo\">\n" + 
				"               <valueResource>\n" + 
				"                  <reference value=\"Organization/o1\"/>\n" + 
				"               </valueResource>\n" + 
				"            </extension>\n" + 
				"            <identifier>\n" + 
				"               <label value=\"p2\"/>\n" + 
				"            </identifier>\n" + 
				"         </Patient>\n" + 
				"      </content>\n" + 
				"   </entry>\n" + 
				"   <entry>\n" + 
				"      <title>Organization o1</title>\n" + 
				"      <id>http://localhost:49627/Organization/o1</id>\n" + 
				"      <published>2014-08-05T15:22:08-04:00</published>\n" + 
				"      <link rel=\"self\" href=\"http://localhost:49627/Organization/o1\"/>\n" + 
				"      <content type=\"text/xml\">\n" + 
				"         <Organization xmlns=\"http://hl7.org/fhir\">\n" + 
				"            <name value=\"o1\"/>\n" + 
				"         </Organization>\n" + 
				"      </content>\n" + 
				"   </entry>\n" + 
				"</feed>";
		//@formatter:off
		
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
