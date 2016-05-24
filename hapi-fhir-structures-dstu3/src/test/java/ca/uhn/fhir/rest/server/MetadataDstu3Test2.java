package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;

public class MetadataDstu3Test2 {

	private static CloseableHttpClient ourClient;
	private static final FhirContext ourCtx = FhirContext.forDstu3();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer servlet;

	@Test
	public void testHttpMethods() throws Exception {
		String output;
		
		HttpRequestBase httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata");
		HttpResponse status = ourClient.execute(httpPost);
		output = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(output, containsString("<Conformance"));
		
		assertEquals("HAPI FHIR " + VersionUtil.getVersion() + " REST Server (FHIR Server; FHIR " + FhirVersionEnum.DSTU3.getFhirVersionString() + "/DSTU3)", status.getFirstHeader("X-Powered-By").getValue());
		
		httpPost = new HttpPost("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpPost);
		output = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(405, status.getStatusLine().getStatusCode());
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"/metadata request must use HTTP GET\"/></issue></OperationOutcome>", output);

		/*
		 * There is no @read on the RP below, so this should fail. Otherwise it
		 * would be interpreted as a read on ID "metadata"
		 */
		httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient/metadata");
		status = ourClient.execute(httpPost);
		output = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(400, status.getStatusLine().getStatusCode());
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Validate()
		public MethodOutcome validate(@ResourceParam Patient theResource) {
			return new MethodOutcome();
		}
	}

}
