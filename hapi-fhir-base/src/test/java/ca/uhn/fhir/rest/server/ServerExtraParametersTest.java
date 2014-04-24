package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.ServerBase;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

public class ServerExtraParametersTest {

	private int myPort;
	private Server myServer;
	private RestfulServer myServlet;

	@Before
	public void before() {
		myPort = RandomServerPortProvider.findFreePort();
		myServer = new Server(myPort);

		ServletHandler proxyHandler = new ServletHandler();
		myServlet = new RestfulServer();
		ServletHolder servletHolder = new ServletHolder(myServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);

	}

	@Test
	public void testServerBase() throws Exception {
		MyServerBaseProvider patientProvider = new MyServerBaseProvider();
		myServlet.setResourceProviders(patientProvider);

		myServer.start();

		FhirContext ctx = new FhirContext();
		PatientClient client = ctx.newRestfulClient(PatientClient.class, "http://localhost:" + myPort + "/");

		List<Patient> actualPatients = client.searchForPatients(new StringDt("AAAABBBB"));
		assertEquals(1, actualPatients.size());
		assertEquals("AAAABBBB", actualPatients.get(0).getNameFirstRep().getFamilyAsSingleString());

		assertEquals("http://localhost:" + myPort, patientProvider.getServerBase());
	}

	@After
	public void after() throws Exception {
		myServer.stop();
	}

	public static class MyServerBaseProvider implements IResourceProvider {
		private String myServerBase;

		public String getServerBase() {
			return myServerBase;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> searchForPatients(@RequiredParam(name = "fooParam") StringDt theFooParam, @ServerBase String theServerBase) {
			myServerBase = theServerBase;

			Patient retVal = new Patient();
			retVal.addName().addFamily(theFooParam.getValue());
			return Collections.singletonList(retVal);
		}

	}

	private static interface PatientClient extends IBasicClient {

		@Search
		public List<Patient> searchForPatients(@RequiredParam(name = "fooParam") StringDt theFooParam);

	}

}
