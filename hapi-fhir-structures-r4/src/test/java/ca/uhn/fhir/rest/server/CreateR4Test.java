package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CreateR4Test.class);
	public static OperationOutcome ourReturnOo;
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@AfterEach
	public void after() {
		ourServlet.setDefaultPreferReturn(RestfulServer.DEFAULT_PREFER_RETURN);
	}

	@BeforeEach
	public void before() {
		ourReturnOo = null;
	}

	@Test
	public void testCreateIgnoresIdInResourceBody() throws Exception {

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("{\"resourceType\":\"Patient\", \"id\":\"999\", \"status\":\"active\"}", ContentType.parse("application/fhir+json; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());

		assertEquals(1, status.getHeaders("Location").length);
		assertEquals(1, status.getHeaders("Content-Location").length);
		assertEquals("http://localhost:" + ourPort + "/Patient/1", status.getFirstHeader("Location").getValue());

	}

	@Test
	public void testCreateFailsIfNoContentTypeProvided() throws Exception {

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("{\"resourceType\":\"Patient\", \"id\":\"999\", \"status\":\"active\"}", (ContentType) null));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);

			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent, containsString("No Content-Type header was provided in the request. This is required for \\\"CREATE\\\" operation"));

		}
	}

	/**
	 * #472
	 */
	@Test
	public void testCreateReturnsLocationHeader() throws Exception {

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("{\"resourceType\":\"Patient\", \"status\":\"active\"}", ContentType.parse("application/fhir+json; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());

		assertEquals(1, status.getHeaders("Location").length);
		assertEquals(1, status.getHeaders("Content-Location").length);
		assertEquals("http://localhost:" + ourPort + "/Patient/1", status.getFirstHeader("Location").getValue());

	}

	@Test
	public void testCreateReturnsOperationOutcome() throws Exception {
		ourReturnOo = new OperationOutcome().addIssue(new OperationOutcomeIssueComponent().setDiagnostics("DIAG"));

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("{\"resourceType\":\"Patient\", \"status\":\"active\"}", ContentType.parse("application/fhir+json; charset=utf-8")));
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());

		assertThat(responseContent, containsString("DIAG"));
	}

	@Test
	public void testCreateReturnsRepresentation() throws Exception {
		ourReturnOo = new OperationOutcome().addIssue(new OperationOutcomeIssueComponent().setDiagnostics("DIAG"));
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"gender\":\"male\"}";

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("{\"resourceType\":\"Patient\", \"gender\":\"male\"}", ContentType.parse("application/fhir+json; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(expectedResponseContent, responseContent);
	}

	@Test
	public void testCreateWithIncorrectContent1() throws Exception {

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("{\"foo\":\"bar\"}", ContentType.parse("application/xml+fhir; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());

		assertThat(responseContent, containsString("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\""));
		assertThat(responseContent, containsString("Failed to parse request body as XML resource."));

	}

	@Test
	public void testCreateWithIncorrectContent2() throws Exception {

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("{\"foo\":\"bar\"}", ContentType.parse("application/fhir+xml; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());

		assertThat(responseContent, containsString("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\""));
		assertThat(responseContent, containsString("Failed to parse request body as XML resource."));

	}

	@Test
	public void testCreateWithIncorrectContent3() throws Exception {

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("{\"foo\":\"bar\"}", ContentType.parse("application/fhir+json; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());

		assertThat(responseContent, containsString("Failed to parse request body as JSON resource."));

	}

	/**
	 * #342
	 */
	@Test
	public void testCreateWithInvalidContent() throws Exception {

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity("FOO", ContentType.parse("application/xml+fhir; charset=utf-8")));
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());

		assertThat(responseContent, containsString("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\""));
		assertThat(responseContent, containsString(Msg.code(450) + "Failed to parse request body as XML resource. Error was: " + Msg.code(1852) +  "com.ctc.wstx.exc.WstxUnexpectedCharException: Unexpected character 'F'"));

	}


	@Test
	public void testCreatePreferDefaultRepresentation() throws Exception {
		ourReturnOo = new OperationOutcome();
		ourReturnOo.addIssue().setDiagnostics("FOO");

		Patient p = new Patient();
		p.setActive(true);
		String body = ourCtx.newJsonParser().encodeResourceToString(p);
		HttpPost httpPost;

		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(body, ContentType.parse("application/fhir+json; charset=utf-8")));
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			assertEquals(201, status.getStatusLine().getStatusCode());
			assertEquals("application/fhir+json;charset=utf-8", status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());

			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was:\n{}", responseContent);
			assertThat(responseContent, containsString("\"resourceType\":\"Patient\""));
		}

	}

	@Test
	public void testCreatePreferDefaultOperationOutcome() throws Exception {
		ourReturnOo = new OperationOutcome();
		ourReturnOo.addIssue().setDiagnostics("FOO");

		Patient p = new Patient();
		p.setActive(true);
		String body = ourCtx.newJsonParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(body, ContentType.parse("application/fhir+json; charset=utf-8")));
		ourServlet.setDefaultPreferReturn(PreferReturnEnum.OPERATION_OUTCOME);
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			assertEquals(201, status.getStatusLine().getStatusCode());
			assertEquals("application/fhir+json;charset=utf-8", status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());

			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was:\n{}", responseContent);
			assertThat(responseContent, containsString("\"resourceType\":\"OperationOutcome\""));
		}


	}

	@Test
	public void testCreatePreferDefaultMinimal() throws Exception {
		ourReturnOo = new OperationOutcome();
		ourReturnOo.addIssue().setDiagnostics("FOO");

		Patient p = new Patient();
		p.setActive(true);
		String body = ourCtx.newJsonParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(body, ContentType.parse("application/fhir+json; charset=utf-8")));
		ourServlet.setDefaultPreferReturn(PreferReturnEnum.MINIMAL);
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {

			assertEquals(201, status.getStatusLine().getStatusCode());
			assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE));

			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseContent, emptyOrNullString());
		}

	}

	@Test
	public void testSearch() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=xml&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());

		//@formatter:off
		assertThat(responseContent, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">",
			"<id value=\"0\"/>",
			"<meta>",
			"<profile value=\"http://example.com/StructureDefinition/patient_with_extensions\"/>",
			"</meta>",
			"<modifierExtension url=\"http://example.com/ext/date\">",
			"<valueDate value=\"2011-01-01\"/>",
			"</modifierExtension>",
			"</Patient>"));
		//@formatter:on

		assertThat(responseContent, not(containsString("http://hl7.org/fhir/")));
	}

	public static class PatientProviderRead implements IResourceProvider {

		@Read()
		public MyPatientWithExtensions read(@IdParam IdType theIdParam) {
			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(theIdParam);
			p0.setDateExt(new DateType("2011-01-01"));
			return p0;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}
	}

	public static class PatientProviderCreate implements IResourceProvider {
		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Create()
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			assertNull(thePatient.getIdElement().getIdPart());
			thePatient.setId("1");
			thePatient.getMeta().setVersionId("1");
			return new MethodOutcome(new IdType("Patient", "1"), true).setOperationOutcome(ourReturnOo).setResource(thePatient);
		}
	}

	public static class PatientProviderSearch implements IResourceProvider {


		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}


		@Search
		public List<IBaseResource> search() {
			ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();

			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(new IdType("Patient/0"));
			p0.setDateExt(new DateType("2011-01-01"));
			retVal.add(p0);

			Patient p1 = new Patient();
			p1.setId(new IdType("Patient/1"));
			p1.addName().setFamily("The Family");
			retVal.add(p1);

			return retVal;
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		PatientProviderCreate patientProviderCreate = new PatientProviderCreate();
		PatientProviderRead patientProviderRead = new PatientProviderRead();
		PatientProviderSearch patientProviderSearch = new PatientProviderSearch();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);

		ourServlet.setResourceProviders(patientProviderCreate, patientProviderRead, patientProviderSearch);
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

}
