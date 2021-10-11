package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.IncludeTest;
import ca.uhn.fhir.rest.server.IncludeTest.ExtPatient;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IncludedResourceStitchingClientTest {

	private FhirContext ctx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	// atom-document-large.xml

	@BeforeEach
	public void before() {
		ctx = FhirContext.forR4();

		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ctx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testWithParam() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), StandardCharsets.UTF_8));

		IGenericClient client = ctx.newRestfulGenericClient( "http://foo");
		Bundle bundle = client
		    .search()
		    .forResource("Patient")
		    .returnBundle(Bundle.class)
		    .execute();

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient", get.getURI().toString());
		
		assertEquals(3, bundle.getEntry().size());
		
		Patient p = (Patient) bundle.getEntry().get(0).getResource();
		List<Extension> exts = p.getExtensionsByUrl("http://foo");
		assertEquals(1,exts.size());
		Extension ext = exts.get(0);
		Reference ref = (Reference) ext.getValue();
		assertEquals("Organization/o1", ref.getReferenceElement().getValue());
		assertNotNull(ref.getResource());
		
	}

	
	@Test
	public void testWithDeclaredExtension() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createLinkedBundle()), StandardCharsets.UTF_8));

		IGenericClient client = ctx.newRestfulGenericClient( "http://foo");
		Bundle bundle = client.search().forResource(IncludeTest.ExtPatient.class).returnBundle(Bundle.class).execute();

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient", get.getURI().toString());
		
		assertEquals(4, bundle.getEntry().size());
		
		ExtPatient p = (ExtPatient) bundle.getEntry().get(0).getResource();
		Reference ref = p.getSecondOrg();
		assertEquals("Organization/o1", ref.getReferenceElement().getValue());
		assertNotNull(ref.getResource());
		
		Organization o1 = (Organization) ref.getResource();
		assertEquals("o2", o1.getPartOf().getReferenceElement().toUnqualifiedVersionless().getIdPart());
		assertNotNull(o1.getPartOf().getResource());
		
	}
	
	private String createLinkedBundle() {
	  
	  Bundle bundle = new Bundle();
	  
	  Patient p1 = new Patient();
	  p1.addIdentifier().setValue("p1");
	  p1.addExtension().setUrl("http://foo#secondOrg").setValue(new Reference("Organization/o1"));
	  bundle.addEntry().setResource(p1);

     Patient p2 = new Patient();
     p2.addIdentifier().setValue("p2");
     p2.addExtension().setUrl("http://foo#secondOrg").setValue(new Reference("Organization/o1"));
     bundle.addEntry().setResource(p2);

     Organization o1 = new Organization();
     o1.setId("o1");
     o1.setName("o1");
     o1.getPartOf().setReference("Organization/o2");
     bundle.addEntry().setResource(o1);
     
     Organization o2 = new Organization();
     o2.setId("o2");
     o2.setName("o2");
     bundle.addEntry().setResource(o2);
     
     return ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
     
//		//@formatter:off
//		return "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
//				"   <title/>\n" + 
//				"   <id>6cfcd90e-877a-40c6-a11c-448006712979</id>\n" + 
//				"   <link rel=\"self\" href=\"http://localhost:49782/Patient?_query=declaredExtInclude&amp;_pretty=true\"/>\n" + 
//				"   <link rel=\"fhir-base\" href=\"http://localhost:49782\"/>\n" + 
//				"   <os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">2</os:totalResults>\n" + 
//				"   <author>\n" +
//				"      <name>HAPI FHIR Server</name>\n" + 
//				"   </author>\n" + 
//				"   <entry>\n" + 
//				"      <title>Patient p1</title>\n" + 
//				"      <id>http://localhost:49782/Patient/p1</id>\n" + 
//				"      <published>2014-08-12T10:22:19-04:00</published>\n" + 
//				"      <link rel=\"self\" href=\"http://localhost:49782/Patient/p1\"/>\n" + 
//				"      <content type=\"text/xml\">\n" + 
//				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
//				"            <extension url=\"http://foo#secondOrg\">\n" + 
//				"               <valueResource>\n" + 
//				"                  <reference value=\"Organization/o1\"/>\n" + 
//				"               </valueResource>\n" + 
//				"            </extension>\n" + 
//				"            <identifier>\n" + 
//				"               <label value=\"p1\"/>\n" + 
//				"            </identifier>\n" + 
//				"         </Patient>\n" + 
//				"      </content>\n" + 
//				"   </entry>\n" + 
//				"   <entry>\n" + 
//				"      <title>Patient p2</title>\n" + 
//				"      <id>http://localhost:49782/Patient/p2</id>\n" + 
//				"      <published>2014-08-12T10:22:19-04:00</published>\n" + 
//				"      <link rel=\"self\" href=\"http://localhost:49782/Patient/p2\"/>\n" + 
//				"      <content type=\"text/xml\">\n" + 
//				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
//				"            <extension url=\"http://foo#secondOrg\">\n" + 
//				"               <valueResource>\n" + 
//				"                  <reference value=\"Organization/o1\"/>\n" + 
//				"               </valueResource>\n" + 
//				"            </extension>\n" + 
//				"            <identifier>\n" + 
//				"               <label value=\"p2\"/>\n" + 
//				"            </identifier>\n" + 
//				"         </Patient>\n" + 
//				"      </content>\n" + 
//				"   </entry>\n" + 
//				"   <entry>\n" + 
//				"      <title>Organization o1</title>\n" + 
//				"      <id>http://localhost:49782/Organization/o1</id>\n" + 
//				"      <published>2014-08-12T10:22:19-04:00</published>\n" + 
//				"      <link rel=\"self\" href=\"http://localhost:49782/Organization/o1\"/>\n" + 
//				"      <content type=\"text/xml\">\n" + 
//				"         <Organization xmlns=\"http://hl7.org/fhir\">\n" + 
//				"            <name value=\"o1\"/>\n" + 
//				"            <partOf>\n" + 
//				"               <reference value=\"Organization/o2\"/>\n" + 
//				"            </partOf>\n" + 
//				"         </Organization>\n" + 
//				"      </content>\n" + 
//				"   </entry>\n" + 
//				"   <entry>\n" + 
//				"      <title>Organization o2</title>\n" + 
//				"      <id>http://localhost:49782/Organization/o2</id>\n" + 
//				"      <published>2014-08-12T10:22:19-04:00</published>\n" + 
//				"      <link rel=\"self\" href=\"http://localhost:49782/Organization/o2\"/>\n" + 
//				"      <content type=\"text/xml\">\n" + 
//				"         <Organization xmlns=\"http://hl7.org/fhir\">\n" + 
//				"            <name value=\"o2\"/>\n" + 
//				"         </Organization>\n" + 
//				"      </content>\n" + 
//				"   </entry>\n" + 
//				"</feed>";
//		//@formatter:on
	}
	
	
	private String createBundle() {
     Bundle bundle = new Bundle();
     
     Patient p1 = new Patient();
     p1.addIdentifier().setValue("p1");
     p1.addExtension().setUrl("http://foo").setValue(new Reference("Organization/o1"));
     bundle.addEntry().setResource(p1);

     Patient p2 = new Patient();
     p2.addIdentifier().setValue("p2");
     p2.addExtension().setUrl("http://foo#secondOrg").setValue(new Reference("Organization/o1"));
     bundle.addEntry().setResource(p2);

     Organization o1 = new Organization();
     o1.setId("o1");
     o1.setName("o1");
     o1.getPartOf().setReference("Organization/o2");
     bundle.addEntry().setResource(o1);

     return ctx.newXmlParser().encodeResourceToString(bundle);
     
//		//@formatter:on
//		return "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
//				"   <title/>\n" + 
//				"   <id>f051fd86-4daa-48da-80f7-5a0443bf6f11</id>\n" + 
//				"   <link rel=\"self\" href=\"http://localhost:49627/Patient?_query=extInclude&amp;_pretty=true\"/>\n" + 
//				"   <link rel=\"fhir-base\" href=\"http://localhost:49627\"/>\n" + 
//				"   <os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">2</os:totalResults>\n" + 
//				"   <author>\n" +
//				"      <name>HAPI FHIR Server</name>\n" + 
//				"   </author>\n" + 
//				"   <entry>\n" + 
//				"      <title>Patient p1</title>\n" + 
//				"      <id>http://localhost:49627/Patient/p1</id>\n" + 
//				"      <published>2014-08-05T15:22:08-04:00</published>\n" + 
//				"      <link rel=\"self\" href=\"http://localhost:49627/Patient/p1\"/>\n" + 
//				"      <content type=\"text/xml\">\n" + 
//				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
//				"            <extension url=\"http://foo\">\n" + 
//				"               <valueResource>\n" + 
//				"                  <reference value=\"Organization/o1\"/>\n" + 
//				"               </valueResource>\n" + 
//				"            </extension>\n" + 
//				"            <identifier>\n" + 
//				"               <label value=\"p1\"/>\n" + 
//				"            </identifier>\n" + 
//				"         </Patient>\n" + 
//				"      </content>\n" + 
//				"   </entry>\n" + 
//				"   <entry>\n" + 
//				"      <title>Patient p2</title>\n" + 
//				"      <id>http://localhost:49627/Patient/p2</id>\n" + 
//				"      <published>2014-08-05T15:22:08-04:00</published>\n" + 
//				"      <link rel=\"self\" href=\"http://localhost:49627/Patient/p2\"/>\n" + 
//				"      <content type=\"text/xml\">\n" + 
//				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
//				"            <extension url=\"http://foo\">\n" + 
//				"               <valueResource>\n" + 
//				"                  <reference value=\"Organization/o1\"/>\n" + 
//				"               </valueResource>\n" + 
//				"            </extension>\n" + 
//				"            <identifier>\n" + 
//				"               <label value=\"p2\"/>\n" + 
//				"            </identifier>\n" + 
//				"         </Patient>\n" + 
//				"      </content>\n" + 
//				"   </entry>\n" + 
//				"   <entry>\n" + 
//				"      <title>Organization o1</title>\n" + 
//				"      <id>http://localhost:49627/Organization/o1</id>\n" + 
//				"      <published>2014-08-05T15:22:08-04:00</published>\n" + 
//				"      <link rel=\"self\" href=\"http://localhost:49627/Organization/o1\"/>\n" + 
//				"      <content type=\"text/xml\">\n" + 
//				"         <Organization xmlns=\"http://hl7.org/fhir\">\n" + 
//				"            <name value=\"o1\"/>\n" + 
//				"         </Organization>\n" + 
//				"      </content>\n" + 
//				"   </entry>\n" + 
//				"</feed>";
//		//@formatter:off
		
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
