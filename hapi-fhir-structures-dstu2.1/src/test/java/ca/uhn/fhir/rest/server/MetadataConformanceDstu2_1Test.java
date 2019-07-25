package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu2016may.hapi.rest.server.ServerConformanceProvider;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;

@SuppressWarnings("deprecation")
public class MetadataConformanceDstu2_1Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MetadataConformanceDstu2_1Test.class);

	
	@Test
	public void testSummary() throws Exception {
		String output;

		// With
		HttpRequestBase httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata?_summary=true&_pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			ourLog.info(output);
			assertThat(output, containsString("<Conformance"));
			assertThat(output, stringContainsInOrder("<meta>", "SUBSETTED", "</meta>"));
			assertThat(output, not(stringContainsInOrder("searchParam")));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		// Without
		httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata?_pretty=true");
		status = ourClient.execute(httpPost);
		try {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			ourLog.info(output);
			assertThat(output, containsString("<Conformance"));
			assertThat(output, not(stringContainsInOrder("<meta>", "SUBSETTED", "</meta>")));
			assertThat(output, stringContainsInOrder("searchParam"));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testElements() throws Exception {
		String output;

		HttpRequestBase httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata?_elements=fhirVersion&_pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			ourLog.info(output);
			assertThat(output, containsString("<Conformance"));
			assertThat(output, stringContainsInOrder("<meta>", "SUBSETTED", "</meta>"));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testHttpMethods() throws Exception {
		String output;

		HttpRequestBase httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata");
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(output, containsString("<Conformance"));
			assertEquals("HAPI FHIR " + VersionUtil.getVersion() + " REST Server (FHIR Server; FHIR " + FhirVersionEnum.DSTU2_1.getFhirVersionString() + "/DSTU2_1)", status.getFirstHeader("X-Powered-By").getValue());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		try {
			httpPost = new HttpPost("http://localhost:" + ourPort + "/metadata");
			status = ourClient.execute(httpPost);
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(405, status.getStatusLine().getStatusCode());
			assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"/metadata request must use HTTP GET\"/></issue></OperationOutcome>", output);
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		/*
		 * There is no @read on the RP below, so this should fail. Otherwise it
		 * would be interpreted as a read on ID "metadata"
		 */
		try {
			httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient/metadata");
			status = ourClient.execute(httpPost);
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(400, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
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
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setServerConformanceProvider(new ServerConformanceProvider(ourServlet));
		ourServlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	@SuppressWarnings("unused")
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> search(@OptionalParam(name="foo") StringParam theFoo) {
			throw new UnsupportedOperationException();
		}
		
		@Validate()
		public MethodOutcome validate(@ResourceParam Patient theResource) {
			return new MethodOutcome();
		}
	}

}
