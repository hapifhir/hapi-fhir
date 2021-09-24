package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class CompartmentDstu2Test {
	private static CloseableHttpClient ourClient;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CompartmentDstu2Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static IdDt ourLastId;

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastId = null;
	}


	@Test
	public void testReadFirst() throws Exception {
		init(new TempPatientResourceProvider());
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);
		assertEquals("read", ourLastMethod);
		assertEquals("Patient", ourLastId.getResourceType());
		assertEquals("123", ourLastId.getIdPart());
		assertThat(responseContent, startsWith("<Patient"));
	}

	@Test
	public void testCompartmentSecond() throws Exception {
		init(new TempPatientResourceProvider());
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/Encounter");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);
		assertEquals("searchEncounterCompartment", ourLastMethod);
		assertEquals("Patient", ourLastId.getResourceType());
		assertEquals("123", ourLastId.getIdPart());
		assertThat(responseContent, startsWith("<Bundle"));
		assertThat(responseContent, containsString("<Encounter"));
	}

	@Test
	public void testCompartmentSecond2() throws Exception {
		init(new TempPatientResourceProvider());
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/Observation");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);
		assertEquals("searchObservationCompartment", ourLastMethod);
		assertEquals("Patient", ourLastId.getResourceType());
		assertEquals("123", ourLastId.getIdPart());
		assertThat(responseContent, startsWith("<Bundle"));
		assertThat(responseContent, containsString("<Observation"));
	}

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(ourServer);
		ourClient.close();
	}

	public void init(IResourceProvider... theProviders) throws Exception {
		ourServer = new Server(0);

		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(theProviders);
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);

		ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class TempPatientResourceProvider implements IResourceProvider {
		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read()
		public Patient method1Read(final @IdParam IdDt theId) {
			ourLastMethod = "read";
			ourLastId = theId;
			Patient patient = new Patient();
			patient.setId(theId);
			return patient;
		}

		@Search(compartmentName = "Encounter")
		public List<Encounter> method2SearchCompartment(final @IdParam IdDt theId) {
			ourLastId = theId;
			ourLastMethod = "searchEncounterCompartment";
			System.out.println("Encounter compartment search");
			List<Encounter> encounters = new ArrayList<Encounter>();
			Encounter encounter = new Encounter();
			encounter.setId("1");
			encounter.setPatient(new ResourceReferenceDt(theId));
			encounters.add(encounter);
			return encounters;
		}

		@Search(compartmentName = "Observation")
		public List<Observation> method2SearchCompartment2(final @IdParam IdDt theId) {
			ourLastId = theId;
			ourLastMethod = "searchObservationCompartment";
			System.out.println("Encounter compartment search");
			List<Observation> encounters = new ArrayList<Observation>();
			Observation obs = new Observation();
			obs.setId("1");
			obs.setSubject(new ResourceReferenceDt(theId));
			encounters.add(obs);
			return encounters;
		}

	}

}
