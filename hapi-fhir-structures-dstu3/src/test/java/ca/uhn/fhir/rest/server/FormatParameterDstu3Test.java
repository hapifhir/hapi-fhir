package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;

public class FormatParameterDstu3Test {

	private static final String VALUE_XML = "<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"p1ReadId\"/><meta><profile value=\"http://foo_profile\"/></meta><identifier><value value=\"p1ReadValue\"/></identifier></Patient>";
	private static final String VALUE_JSON = "{\"resourceType\":\"Patient\",\"id\":\"p1ReadId\",\"meta\":{\"profile\":[\"http://foo_profile\"]},\"identifier\":[{\"value\":\"p1ReadValue\"}]}";
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FormatParameterDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	/**
	 * See #346
	 */
	@Test
	public void testFormatXml() throws Exception {
		ourServlet.setDefaultResponseEncoding(EncodingEnum.JSON);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=xml");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(VALUE_XML, responseContent);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationXml() throws Exception {
		ourServlet.setDefaultResponseEncoding(EncodingEnum.JSON);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=application/xml");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(VALUE_XML, responseContent);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationXmlFhir() throws Exception {
		ourServlet.setDefaultResponseEncoding(EncodingEnum.JSON);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=application/xml%2Bfhir");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(VALUE_XML, responseContent);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationXmlFhirUnescaped() throws Exception {
		ourServlet.setDefaultResponseEncoding(EncodingEnum.JSON);

		// The plus isn't escaped here, and it should be.. but we'll be lenient
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=application/xml+fhir");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(VALUE_XML, responseContent);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatJson() throws Exception {
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=json");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(VALUE_JSON, responseContent);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationJson() throws Exception {
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=application/json");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(VALUE_JSON, responseContent);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationJsonFhir() throws Exception {
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=application/json%2Bfhir");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(VALUE_JSON, responseContent);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	/**
	 * See #346
	 */
	@Test
	public void testFormatApplicationJsonFhirUnescaped() throws Exception {
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);

		// The plus isn't escaped here, and it should be.. but we'll be lenient
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=application/json+fhir");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(VALUE_JSON, responseContent);
		} finally {
			IOUtils.closeQuietly(status);
		}
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

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

		@Read(version = true)
		public Patient read(@IdParam IdType theId) {
			Patient p1 = new MyPatient();
			p1.setId("p1ReadId");
			p1.addIdentifier().setValue("p1ReadValue");
			return p1;
		}

	}

	@ResourceDef(name = "Patient", profile = "http://foo_profile")
	public static class MyPatient extends Patient {

		private static final long serialVersionUID = 1L;

	}

}
