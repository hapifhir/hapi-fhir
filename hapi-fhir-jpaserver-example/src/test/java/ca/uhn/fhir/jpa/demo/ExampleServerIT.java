package ca.uhn.fhir.jpa.demo;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

public class ExampleServerIT {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExampleServerIT.class);
	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static int ourPort;

	private static Server ourServer;
	private static String ourServerBase;

	@Test
	public void testCreateAndRead() throws IOException {
		ourLog.info("Base URL is: http://localhost:" + ourPort + "/baseDstu3");
		String methodName = "testCreateResourceConditional";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		IIdType id = ourClient.create().resource(pt).execute().getId();

		Patient pt2 = ourClient.read().resource(Patient.class).withId(id).execute();
		assertEquals(methodName, pt2.getName().get(0).getFamily());
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		/*
		 * This runs under maven, and I'm not sure how else to figure out the target directory from code..
		 */
		String path = ExampleServerIT.class.getClassLoader().getResource(".keep_hapi-fhir-jpaserver-example").getPath();
		path = new File(path).getParent();
		path = new File(path).getParent();
		path = new File(path).getParent();

		ourLog.info("Project base path is: {}", path);

		if (ourPort == 0) {
			ourPort = RandomServerPortProvider.findFreePort();
		}
		ourServer = new Server(ourPort);

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		webAppContext.setDescriptor(path + "/src/main/webapp/WEB-INF/web.xml");
		webAppContext.setResourceBase(path + "/target/hapi-fhir-jpaserver-example");
		webAppContext.setParentLoaderPriority(true);

		ourServer.setHandler(webAppContext);
		ourServer.start();

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		ourServerBase = "http://localhost:" + ourPort + "/baseDstu3";
		ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
		ourClient.registerInterceptor(new LoggingInterceptor(true));

	}

	public static void main(String[] theArgs) throws Exception {
		ourPort = 8080;
		beforeClass();
	}


}
