package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.util.PortUtil;

public class DeleteConditionalDstu3Test {
	private static CloseableHttpClient ourClient;
	private static String ourLastConditionalUrl;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DeleteConditionalDstu3Test.class);
	private static int ourPort;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static Server ourServer;
	private static IdType ourLastIdParam;
	private static boolean ourLastRequestWasDelete;
	private static IGenericClient ourHapiClient;
	
	
	
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
	public static void afterClass() throws Exception {
		ourServer.stop();
	}
		
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		PatientProvider patientProvider = new PatientProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

		ourCtx.getRestfulClientFactory().setSocketTimeout(500 * 1000);
		ourHapiClient = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort + "/");
		ourHapiClient.registerInterceptor(new LoggingInterceptor());
	}
	
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}
		
		@Delete()
		public MethodOutcome deletePatient(@IdParam IdType theIdParam, @ConditionalUrlParam String theConditional) {
			ourLastRequestWasDelete = true;
			ourLastConditionalUrl = theConditional;
			ourLastIdParam = theIdParam;
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}

	}

}
