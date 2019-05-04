package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.IDeleteTyped;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class HashMapResourceProviderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(HashMapResourceProviderTest.class);
	private static MyRestfulServer ourRestServer;
	private static Server ourListenerServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static HashMapResourceProvider<Patient> myPatientResourceProvider;
	private static HashMapResourceProvider<Observation> myObservationResourceProvider;

	@Before
	public void before() {
		ourRestServer.clearData();
		myPatientResourceProvider.clearCounts();
		myObservationResourceProvider.clearCounts();
	}

	@Test
	public void testCreateAndRead() {
		// Create
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourClient.create().resource(p).execute().getId();
		assertThat(id.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("1", id.getVersionIdPart());

		// Read
		p = (Patient) ourClient.read().resource("Patient").withId(id).execute();
		assertEquals(true, p.getActive());

		assertEquals(1, myPatientResourceProvider.getCountRead());
	}

	@Test
	public void testCreateWithClientAssignedIdAndRead() {
		// Create
		Patient p = new Patient();
		p.setId("ABC");
		p.setActive(true);
		IIdType id = ourClient.update().resource(p).execute().getId();
		assertEquals("ABC", id.getIdPart());
		assertEquals("1", id.getVersionIdPart());

		// Read
		p = (Patient) ourClient.read().resource("Patient").withId(id).execute();
		assertEquals(true, p.getActive());
	}

	@Test
	public void testDelete() {
		// Create
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourClient.create().resource(p).execute().getId();
		assertThat(id.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("1", id.getVersionIdPart());

		assertEquals(0, myPatientResourceProvider.getCountDelete());

		IDeleteTyped iDeleteTyped = ourClient.delete().resourceById(id.toUnqualifiedVersionless());
		ourLog.info("About to execute");
		try {
			iDeleteTyped.execute();
		} catch (NullPointerException e) {
			ourLog.error("NPE", e);
			fail(e.toString());
		}

		assertEquals(1, myPatientResourceProvider.getCountDelete());

		// Read
		ourClient.read().resource("Patient").withId(id.withVersion("1")).execute();
		try {
			ourClient.read().resource("Patient").withId(id.withVersion("2")).execute();
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	@Test
	public void testHistoryInstance() {
		// Create Res 1
		Patient p = new Patient();
		p.setActive(true);
		IIdType id1 = ourClient.create().resource(p).execute().getId();
		assertThat(id1.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("1", id1.getVersionIdPart());

		// Create Res 2
		p = new Patient();
		p.setActive(true);
		IIdType id2 = ourClient.create().resource(p).execute().getId();
		assertThat(id2.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("1", id2.getVersionIdPart());

		// Update Res 2
		p = new Patient();
		p.setId(id2);
		p.setActive(false);
		id2 = ourClient.update().resource(p).execute().getId();
		assertThat(id2.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("2", id2.getVersionIdPart());

		Bundle history = ourClient
			.history()
			.onInstance(id2.toUnqualifiedVersionless())
			.andReturnBundle(Bundle.class)
			.encodedJson()
			.prettyPrint()
			.execute();
		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(history));
		List<String> ids = history
			.getEntry()
			.stream()
			.map(t -> t.getResource().getIdElement().toUnqualified().getValue())
			.collect(Collectors.toList());
		assertThat(ids, contains(
			id2.toUnqualified().withVersion("2").getValue(),
			id2.toUnqualified().withVersion("1").getValue()
		));

	}

	@Test
	public void testHistoryType() {
		// Create Res 1
		Patient p = new Patient();
		p.setActive(true);
		IIdType id1 = ourClient.create().resource(p).execute().getId();
		assertThat(id1.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("1", id1.getVersionIdPart());

		// Create Res 2
		p = new Patient();
		p.setActive(true);
		IIdType id2 = ourClient.create().resource(p).execute().getId();
		assertThat(id2.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("1", id2.getVersionIdPart());

		// Update Res 2
		p = new Patient();
		p.setId(id2);
		p.setActive(false);
		id2 = ourClient.update().resource(p).execute().getId();
		assertThat(id2.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("2", id2.getVersionIdPart());

		Bundle history = ourClient
			.history()
			.onType(Patient.class)
			.andReturnBundle(Bundle.class)
			.execute();
		List<String> ids = history
			.getEntry()
			.stream()
			.map(t -> t.getResource().getIdElement().toUnqualified().getValue())
			.collect(Collectors.toList());
		ourLog.info("Received IDs: {}", ids);
		assertThat(ids, contains(
			id2.toUnqualified().withVersion("2").getValue(),
			id2.toUnqualified().withVersion("1").getValue(),
			id1.toUnqualified().withVersion("1").getValue()
		));

	}

	@Test
	public void testSearchAll() {
		// Create
		for (int i = 0; i < 100; i++) {
			Patient p = new Patient();
			p.addName().setFamily("FAM" + i);
			ourClient.registerInterceptor(new LoggingInterceptor(true));
			IIdType id = ourClient.create().resource(p).execute().getId();
			assertThat(id.getIdPart(), matchesPattern("[0-9]+"));
			assertEquals("1", id.getVersionIdPart());
		}

		// Search
		Bundle resp = ourClient
			.search()
			.forResource("Patient")
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(100, resp.getTotal());
		assertEquals(100, resp.getEntry().size());

		assertEquals(1, myPatientResourceProvider.getCountSearch());

	}

	@Test
	public void testSearchById() {
		// Create
		for (int i = 0; i < 100; i++) {
			Patient p = new Patient();
			p.addName().setFamily("FAM" + i);
			IIdType id = ourClient.create().resource(p).execute().getId();
			assertThat(id.getIdPart(), matchesPattern("[0-9]+"));
			assertEquals("1", id.getVersionIdPart());
		}

		// Search
		Bundle resp = ourClient
			.search()
			.forResource("Patient")
			.where(IAnyResource.RES_ID.exactly().codes("2", "3"))
			.returnBundle(Bundle.class).execute();
		assertEquals(2, resp.getTotal());
		assertEquals(2, resp.getEntry().size());
		List<String> respIds = resp.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(respIds, containsInAnyOrder("Patient/2", "Patient/3"));

		// Search
		resp = ourClient
			.search()
			.forResource("Patient")
			.where(IAnyResource.RES_ID.exactly().codes("2", "3"))
			.where(IAnyResource.RES_ID.exactly().codes("2", "3"))
			.returnBundle(Bundle.class).execute();
		assertEquals(2, resp.getTotal());
		assertEquals(2, resp.getEntry().size());
		respIds = resp.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(respIds, containsInAnyOrder("Patient/2", "Patient/3"));

		resp = ourClient
			.search()
			.forResource("Patient")
			.where(IAnyResource.RES_ID.exactly().codes("2", "3"))
			.where(IAnyResource.RES_ID.exactly().codes("4", "3"))
			.returnBundle(Bundle.class).execute();
		respIds = resp.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(respIds, containsInAnyOrder("Patient/3"));
		assertEquals(1, resp.getTotal());
		assertEquals(1, resp.getEntry().size());

	}

	@Test
	public void testUpdate() {
		// Create
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourClient.create().resource(p).execute().getId();
		assertThat(id.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("1", id.getVersionIdPart());

		// Update
		p = new Patient();
		p.setId(id);
		p.setActive(false);
		id = ourClient.update().resource(p).execute().getId();
		assertThat(id.getIdPart(), matchesPattern("[0-9]+"));
		assertEquals("2", id.getVersionIdPart());

		assertEquals(1, myPatientResourceProvider.getCountCreate());
		assertEquals(1, myPatientResourceProvider.getCountUpdate());

		// Read
		p = (Patient) ourClient.read().resource("Patient").withId(id.withVersion("1")).execute();
		assertEquals(true, p.getActive());
		p = (Patient) ourClient.read().resource("Patient").withId(id.withVersion("2")).execute();
		assertEquals(false, p.getActive());
		try {
			ourClient.read().resource("Patient").withId(id.withVersion("3")).execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourListenerServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void startListenerServer() throws Exception {
		int ourListenerPort = PortUtil.findFreePort();
		ourRestServer = new MyRestfulServer();
		String ourBase = "http://localhost:" + ourListenerPort + "/";
		ourListenerServer = new Server(ourListenerPort);

		ourCtx.getRestfulClientFactory().setSocketTimeout(120000);
		ourClient = ourCtx.newRestfulGenericClient(ourBase);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(ourRestServer);
		proxyHandler.addServlet(servletHolder, "/*");

		ourListenerServer.setHandler(proxyHandler);
		ourListenerServer.start();
	}

	private static class MyRestfulServer extends RestfulServer {

		MyRestfulServer() {
			super(ourCtx);
		}

		void clearData() {
			for (IResourceProvider next : getResourceProviders()) {
				if (next instanceof HashMapResourceProvider) {
					((HashMapResourceProvider) next).clear();
				}
			}
		}

		@Override
		protected void initialize() throws ServletException {
			super.initialize();

			myPatientResourceProvider = new HashMapResourceProvider<>(ourCtx, Patient.class);
			myObservationResourceProvider = new HashMapResourceProvider<>(ourCtx, Observation.class);
			registerProvider(myPatientResourceProvider);
			registerProvider(myObservationResourceProvider);
		}


	}


}
