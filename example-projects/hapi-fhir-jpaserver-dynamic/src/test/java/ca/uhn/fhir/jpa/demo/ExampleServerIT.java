package ca.uhn.fhir.jpa.demo;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;

public class ExampleServerIT {

	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExampleServerIT.class);

	private static int ourPort;

	private static Server ourServer;
	private static String ourServerBase;

	@Test
	@Ignore
	public void testCreateAndRead() throws IOException {
		ourLog.info("Base URL is: http://localhost:" + ourPort + "/baseDstu2");
		String methodName = "testCreateResourceConditional";

		Patient pt = new Patient(); 
		StringDt famName = new StringDt(methodName);
		List<StringDt>  famNames =Arrays.asList(famName);
		pt.addName().setFamily(famNames);
		IIdType id = ourClient.create().resource(pt).execute().getId();

		Patient pt2 = ourClient.read().resource(Patient.class).withId(id).execute();
		assertEquals(famNames, pt2.getName().get(0).getFamily());
	}

	@AfterClass
	public static void afterClass() throws Exception {
		JettyUtil.closeServer(ourServer);
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
        
		ourServer = new Server(0);

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		//webAppContext.setDescriptor(path + "/src/main/webapp/WEB-INF/web.xml");
		webAppContext.setResourceBase(path + "/target/hapi-fhir-jpaserver-example");
		webAppContext.setParentLoaderPriority(true);

		ourServer.setHandler(webAppContext);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		ourServerBase = "http://localhost:" + ourPort + "/baseDstu2";
		ourClient = ourCtx.newRestfulGenericClient(ourServerBase);
		ourClient.registerInterceptor(new LoggingInterceptor(true));

	}

}
