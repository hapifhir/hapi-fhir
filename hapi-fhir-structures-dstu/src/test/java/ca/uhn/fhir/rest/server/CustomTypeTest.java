package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class CustomTypeTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = new FhirContext(ExtendedPatient.class);
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomTypeTest.class);
	private static int ourPort;
	private static boolean ourReturnExtended = false;

	
	private static Server ourServer;
	
	private static RestfulServer ourServlet;
	
	@Test
	public void testFindProfileItself() throws Exception {
		ourServlet.setAddProfileTag(AddProfileTagEnum.ONLY_FOR_CUSTOM);
		ourReturnExtended=true;
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Profile/prof2?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		
		ourLog.info(responseContent);
		
		Profile bundle = ourCtx.newXmlParser().parseResource(Profile.class, responseContent);
		
	}
	
	
	@Test
	public void testSearchReturnsNoProfileForExtendedType() throws Exception {
		ourServlet.setAddProfileTag(AddProfileTagEnum.NEVER);
		ourReturnExtended=true;
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=aaa");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		BundleEntry entry = bundle.getEntries().get(0);
		List<Tag> profileTags = entry.getCategories().getTagsWithScheme(Tag.HL7_ORG_PROFILE_TAG);
		assertEquals(0, profileTags.size());
	}
	
	@Test
	public void testSearchReturnsNoProfileForNormalType() throws Exception {
		ourServlet.setAddProfileTag(AddProfileTagEnum.ONLY_FOR_CUSTOM);
		ourReturnExtended=false;
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=aaa");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		BundleEntry entry = bundle.getEntries().get(0);
		List<Tag> profileTags = entry.getCategories().getTagsWithScheme(Tag.HL7_ORG_PROFILE_TAG);
		assertEquals(0, profileTags.size());
	}

	
	@Test
	public void testSearchReturnsProfile() throws Exception {
		ourServlet.setAddProfileTag(AddProfileTagEnum.ONLY_FOR_CUSTOM);
		ourReturnExtended=true;
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=aaa");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		BundleEntry entry = bundle.getEntries().get(0);
		List<Tag> profileTags = entry.getCategories().getTagsWithScheme(Tag.HL7_ORG_PROFILE_TAG);
		assertEquals(1, profileTags.size());
		assertEquals("http://foo/profiles/Profile", profileTags.get(0).getTerm());
		
		Patient p = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("idaaa", p.getNameFirstRep().getFamilyAsSingleString());
		
	}

	@Test
	public void testSearchReturnsProfileForNormalType() throws Exception {
		ourServlet.setAddProfileTag(AddProfileTagEnum.ALWAYS);
		ourReturnExtended=false;
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=aaa");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		BundleEntry entry = bundle.getEntries().get(0);
		List<Tag> profileTags = entry.getCategories().getTagsWithScheme(Tag.HL7_ORG_PROFILE_TAG);
		assertEquals(1, profileTags.size());
		assertEquals("http://hl7.org/fhir/profiles/Patient", profileTags.get(0).getTerm());
		
		Patient p = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("idaaa", p.getNameFirstRep().getFamilyAsSingleString());
		
	}

	
	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		ourServlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		FhirContext fhirContext = ourServlet.getFhirContext();
		fhirContext.getResourceDefinition(ExtendedPatient.class);
		
	}
	
	public static class DummyPatientResourceProvider implements IResourceProvider {
		
		@Search
		public List<Patient> findPatient(@OptionalParam(name = "_id") StringParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = ourReturnExtended ? new ExtendedPatient() : new Patient();
			patient.setId("1");
			patient.addIdentifier("system", "identifier123");
			if (theParam != null) {
				patient.addName().addFamily("id" + theParam.getValue());
			}
			retVal.add(patient);
			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}


	@ResourceDef(name="Patient", profile="http://foo/profiles/Profile", id="prof2")
	public static class ExtendedPatient extends Patient {
		
	    /**
	     * Each extension is defined in a field. Any valid HAPI Data Type
	     * can be used for the field type. Note that the [name=""] attribute
	     * in the @Child annotation needs to match the name for the bean accessor
	     * and mutator methods.
	     */
	    @Child(name="petName") 
	    @Extension(url="http://example.com/dontuse#petname", definedLocally=false, isModifier=false)
	    @Description(shortDefinition="The name of the patient's favourite pet")
	    private StringDt myPetName;

		public StringDt getPetName() {
			if (myPetName == null) {
				myPetName = new StringDt();
			}
			return myPetName;
		}

		public void setPetName(StringDt thePetName) {
			myPetName = thePetName;
		}
		
	}

}
