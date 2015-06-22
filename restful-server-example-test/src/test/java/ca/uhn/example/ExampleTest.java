package ca.uhn.example;

import static org.junit.Assert.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;

public class ExampleTest {

	private static Integer ourPort;
	private static Server ourServer;
	private static FhirContext ourCtx;
	private static IGenericClient ourClient;

	@AfterClass
	public static void afterClass() throws Exception {
		if (ourServer != null) {
			ourServer.stop();
		}

		System.clearProperty("ca.uhn.fhir.to.TesterConfig_SYSPROP_FORCE_SERVERS");

	}

	/**
	 * Tests here have some weird windows inconsistency relating to the path for finding the WAR file. Since this test isn't really important to work multiplatform, we can skip it
	 */
	public static boolean isWindows() {
		return System.getProperty("os.name").startsWith("Windows");
	}

	@Test
	public void test01Search() throws Exception {
		if (isWindows()) {
			return;
		}

		Bundle results = ourClient.search().forResource(Patient.class).execute();
		assertEquals(1, results.size());
	}

	@Test
	public void test02Read() throws Exception {
		if (isWindows()) {
			return;
		}

		Patient results = ourClient.read(Patient.class, "1");
		assertThat(results.getNameFirstRep().getGivenAsSingleString(), StringContains.containsString("PatientOne"));
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		if (isWindows()) {
			return;
		}
		
		if (ourPort != null) {
			return;
		}

		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);

		String base = "http://localhost:" + ourPort + "/fhir";
		System.setProperty("ca.uhn.fhir.to.TesterConfig_SYSPROP_FORCE_SERVERS", "example , Restful Server Example , " + base);

		WebAppContext root = new WebAppContext();
		root.setAllowDuplicateFragmentNames(true);

		root.setWar("file:../restful-server-example/target/restful-server-example.war");
		root.setContextPath("/");
		root.setAttribute(WebAppContext.BASETEMPDIR, "target/tempextrtact");
		root.setParentLoaderPriority(false);
		root.setCopyWebInf(true);
		root.setCopyWebDir(true);

		ourServer.setHandler(root);

		ourServer.start();

		ourCtx = FhirContext.forDstu2();
		ourClient = ourCtx.newRestfulGenericClient(base);

	}

}
