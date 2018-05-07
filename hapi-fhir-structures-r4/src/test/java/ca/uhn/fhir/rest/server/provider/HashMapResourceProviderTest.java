package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.ServletException;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;

public class HashMapResourceProviderTest {

	private static MyRestfulServer ourRestServer;
	private static Server ourListenerServer;
	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();


	@Before
	public void before() {
		ourRestServer.clearData();
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

		ourClient.delete().resourceById(id.toUnqualifiedVersionless()).execute();

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
	public void testSearchAll() {
		// Create
		for (int i = 0; i < 100; i++) {
			Patient p = new Patient();
			p.addName().setFamily("FAM" + i);
			IIdType id = ourClient.create().resource(p).execute().getId();
			assertThat(id.getIdPart(), matchesPattern("[0-9]+"));
			assertEquals("1", id.getVersionIdPart());
		}

		// Search
		Bundle resp = ourClient.search().forResource("Patient").returnBundle(Bundle.class).execute();
		assertEquals(100, resp.getTotal());
		assertEquals(100, resp.getEntry().size());
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

			registerProvider(new HashMapResourceProvider<>(ourCtx, Patient.class));
			registerProvider(new HashMapResourceProvider<>(ourCtx, Observation.class));
		}


	}


}
