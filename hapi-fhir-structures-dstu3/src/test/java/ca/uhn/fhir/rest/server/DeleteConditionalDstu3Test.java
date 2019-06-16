package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;

public class DeleteConditionalDstu3Test {
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static IGenericClient ourHapiClient;
	private static String ourLastConditionalUrl;
	private static IdType ourLastIdParam;
	private static boolean ourLastRequestWasDelete;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DeleteConditionalDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;
	

	@Before
	public void before() {
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourLastRequestWasDelete = false;
	}


	
	@Test
	public void testSearchStillWorks() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

//		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_pretty=true");
//
//		HttpResponse status = ourClient.execute(httpGet);
//
//		String responseContent = IOUtils.toString(status.getEntity().getContent());
//		IOUtils.closeQuietly(status.getEntity().getContent());
//
//		ourLog.info("Response was:\n{}", responseContent);

		//@formatter:off
		ourHapiClient
			.delete()
			.resourceConditionalByType(Patient.class)
			.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("SOMESYS","SOMEID"))
			.execute();
		//@formatter:on
		
		assertTrue(ourLastRequestWasDelete);
		assertEquals(null, ourLastIdParam);
		assertEquals("Patient?identifier=SOMESYS%7CSOMEID", ourLastConditionalUrl);

	}


	
	@AfterClass
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
		
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		PatientProvider patientProvider = new PatientProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		ourCtx.getRestfulClientFactory().setSocketTimeout(500 * 1000);
		ourHapiClient = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/");
		ourHapiClient.registerInterceptor(new LoggingInterceptor());
	}
	
	public static class PatientProvider implements IResourceProvider {

		@Delete()
		public MethodOutcome deletePatient(@IdParam IdType theIdParam, @ConditionalUrlParam String theConditional) {
			ourLastRequestWasDelete = true;
			ourLastConditionalUrl = theConditional;
			ourLastIdParam = theIdParam;
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}
		
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

}
