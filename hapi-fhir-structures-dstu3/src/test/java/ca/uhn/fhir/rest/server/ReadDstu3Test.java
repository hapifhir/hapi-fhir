package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.*;
import org.junit.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.util.*;

public class ReadDstu3Test {
	private static CloseableHttpClient ourClient;

	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testRead() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2?_format=xml&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/2/_history/2", status.getFirstHeader(Constants.HEADER_LOCATION).getValue());
		assertEquals(null, status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION));

		assertThat(responseContent, stringContainsInOrder(
				"<Patient xmlns=\"http://hl7.org/fhir\">",
				" <id value=\"2\"/>",
				" <meta>",
				"  <profile value=\"http://example.com/StructureDefinition/patient_with_extensions\"/>",
				" </meta>",
				" <modifierExtension url=\"http://example.com/ext/date\">",
				"  <valueDate value=\"2011-01-01\"/>",
				" </modifierExtension>",
				"</Patient>"));
	}

	@Test
	public void testIfModifiedSince() throws Exception {

		CloseableHttpResponse status;
		HttpGet httpGet;

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T13:00:00Z").getValue()));
		status = ourClient.execute(httpGet);
		try {
			assertEquals(200, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status);
		}
		
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T10:00:00Z").getValue()));
		status = ourClient.execute(httpGet);
		try {
			assertEquals(304, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status);
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

	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read(version = true)
		public MyPatientWithExtensions read(@IdParam IdType theIdParam) {
			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.getMeta().getLastUpdatedElement().setValueAsString("2012-01-01T12:12:12Z");
			p0.setId(theIdParam);
			if (theIdParam.hasVersionIdPart() == false) {
				p0.setIdElement(p0.getIdElement().withVersion("2"));
			}
			p0.setDateExt(new DateType("2011-01-01"));
			return p0;
		}

	}

}
