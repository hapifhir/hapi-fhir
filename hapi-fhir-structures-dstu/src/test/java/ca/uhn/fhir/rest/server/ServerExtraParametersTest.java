package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.StringContains;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.ServerBase;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class ServerExtraParametersTest {

	private int myPort;
	private Server myServer;
	private RestfulServer myServlet;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@Before
	public void before() {
		myPort = PortUtil.findFreePort();
		myServer = new Server(myPort);

		ServletHandler proxyHandler = new ServletHandler();
		myServlet = new RestfulServer(ourCtx);
		myServlet.setFhirContext(ourCtx);
		
		ServletHolder servletHolder = new ServletHolder(myServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);

	}

	@Test
	public void testServerBase() throws Exception {
		MyServerBaseProvider patientProvider = new MyServerBaseProvider();
		myServlet.setResourceProviders(patientProvider);

		myServer.start();

		FhirContext ctx = ourCtx;
		PatientClient client = ctx.newRestfulClient(PatientClient.class, "http://localhost:" + myPort + "/");

		List<Patient> actualPatients = client.searchForPatients(new StringDt("AAAABBBB"));
		assertEquals(1, actualPatients.size());
		assertEquals("AAAABBBB", actualPatients.get(0).getNameFirstRep().getFamilyAsSingleString());

		assertEquals("http://localhost:" + myPort, patientProvider.getServerBase());
	}

	@Test
	public void testNonRepeatableParam() throws Exception {
		MyServerBaseProvider patientProvider = new MyServerBaseProvider();
		myServlet.setResourceProviders(patientProvider);

		myServer.start();

		FhirContext ctx = ourCtx;
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:" + myPort + "/");
		client.registerInterceptor(new LoggingInterceptor(true));

		try {
			client.search().forResource("Patient").where(new StringClientParam("singleParam").matches().values(Arrays.asList("AA", "BB"))).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(
					e.getMessage(),
					StringContains
							.containsString("HTTP 400 Bad Request: Multiple values detected for non-repeatable parameter 'singleParam'. This server is not configured to allow multiple (AND/OR) values for this param."));
		}
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
		public List<Patient> searchSingleParam(@RequiredParam(name = "singleParam") StringParam theFooParam) {
			Patient retVal = new Patient();
			retVal.setId("1");
			return Collections.singletonList(retVal);
		}

		@Search
		public List<Patient> searchForPatients(@RequiredParam(name = "fooParam") StringDt theFooParam, @ServerBase String theServerBase) {
			myServerBase = theServerBase;

			Patient retVal = new Patient();
			retVal.setId("1");
			retVal.addName().addFamily(theFooParam.getValue());
			return Collections.singletonList(retVal);
		}

	}

	private static interface PatientClient extends IBasicClient {

		@Search
		public List<Patient> searchForPatients(@RequiredParam(name = "fooParam") StringDt theFooParam);

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
