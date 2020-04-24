package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import ca.uhn.fhir.test.utilities.JettyUtil;
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
import ca.uhn.fhir.rest.api.Constants;
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
		assertEquals(null, status.getFirstHeader(Constants.HEADER_LOCATION));
		assertEquals("http://localhost:" + ourPort + "/Patient/2/_history/2", status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());

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
	public void testInvalidQueryParamsInRead() throws Exception {
		CloseableHttpResponse status;
		HttpGet httpGet;

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2?_contained=both&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"Invalid query parameter(s) for this request: &quot;[_contained]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2?_containedType=contained&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"Invalid query parameter(s) for this request: &quot;[_containedType]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2?_count=10&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"Invalid query parameter(s) for this request: &quot;[_count]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2?_include=Patient:organization&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"Invalid query parameter(s) for this request: &quot;[_include]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2?_revinclude=Provenance:target&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"Invalid query parameter(s) for this request: &quot;[_revinclude]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2?_sort=family&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"Invalid query parameter(s) for this request: &quot;[_sort]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2?_total=accurate&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"Invalid query parameter(s) for this request: &quot;[_total]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}
	}

	@Test
	public void testIfModifiedSince() throws Exception {

		CloseableHttpResponse status;
		HttpGet httpGet;

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it hasn't changed after the later time of 2012-01-01T13:00:00Z
		// so we expect a 304 (Not Modified)
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T13:00:00Z").getValue()));
		status = ourClient.execute(httpGet);
		try {
			assertEquals(304, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status);
		}

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it hasn't changed after the same time of 2012-01-01T12:12:12Z
		// so we expect a 304 (Not Modified)
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T12:12:12Z").getValue()));
		status = ourClient.execute(httpGet);
		try {
			assertEquals(304, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status);
		}

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it has changed after the earlier time of 2012-01-01T10:00:00Z
		// so we expect a 200
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T10:00:00Z").getValue()));
		status = ourClient.execute(httpGet);
		try {
			assertEquals(200, status.getStatusLine().getStatusCode());
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
