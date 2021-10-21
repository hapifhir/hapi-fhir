package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeleteDstu2Test {
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static boolean ourInvoked;
	private static String ourLastConditionalUrl;
	private static IdDt ourLastIdParam;
	private static int ourPort;
	private static Server ourServer;
	

	@BeforeEach
	public void before() {
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourInvoked = false;
	}


	
	@Test
	public void testDeleteWithConditionalUrl() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpDelete httpPost = new HttpDelete("http://localhost:" + ourPort + "/Patient?identifier=system%7C001");

		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(204, status.getStatusLine().getStatusCode());
		
		assertNull(ourLastIdParam);
		assertEquals("Patient?identifier=system%7C001", ourLastConditionalUrl);
	}

	@Test
	public void testDeleteWithoutConditionalUrl() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpDelete httpPost = new HttpDelete("http://localhost:" + ourPort + "/Patient/2");

		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(204, status.getStatusLine().getStatusCode());
		assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE));
		
		assertEquals("Patient/2", ourLastIdParam.toUnqualified().getValue());
		assertNull(ourLastConditionalUrl);
	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}
		
	
	@BeforeAll
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

	}
	
	public static class PatientProvider implements IResourceProvider {

		@Delete()
		public MethodOutcome delete(@ConditionalUrlParam String theConditional, @IdParam IdDt theIdParam) {
			ourLastConditionalUrl = theConditional;
			ourLastIdParam = theIdParam;
			ourInvoked = true;
			return new MethodOutcome(new IdDt("Patient/001/_history/002"));
		}

		
		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
