package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class CustomTypeTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = new FhirContext(ExtendedPatient.class);
	private static int ourPort;
	private static Server ourServer;

	
	@Test
	public void testSearchReturnsProfile() throws Exception {
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
	
	

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		servlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	
	@ResourceDef(name="Patient", profile="http://foo/profiles/Profile")
	public static class ExtendedPatient extends Patient {
		
		
		
	}
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {
		
		@Search
		public List<Patient> findPatient(@OptionalParam(name = "_id") StringParam theParam) {
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			ExtendedPatient patient = new ExtendedPatient();
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

}
