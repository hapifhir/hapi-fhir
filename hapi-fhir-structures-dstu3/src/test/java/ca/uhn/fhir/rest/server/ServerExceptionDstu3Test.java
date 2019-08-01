package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import ca.uhn.fhir.test.utilities.JettyUtil;

public class ServerExceptionDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerExceptionDstu3Test.class);
	public static BaseServerResponseException ourException;
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testAddHeadersNotFound() throws Exception {

		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome.addIssue().setCode(IssueType.BUSINESSRULE);

		ourException = new ResourceNotFoundException("SOME MESSAGE");
		ourException.addResponseHeader("X-Foo", "BAR BAR");


		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(404, status.getStatusLine().getStatusCode());
			assertEquals("BAR BAR", status.getFirstHeader("X-Foo").getValue());
			assertThat(status.getFirstHeader("X-Powered-By").getValue(), containsString("HAPI FHIR"));
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	@Test
	public void testResponseUsesCorrectEncoding() throws Exception {

		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome
			.addIssue()
			.setCode(IssueType.PROCESSING)
			.setSeverity(OperationOutcome.IssueSeverity.ERROR)
			.setDiagnostics("El nombre está vacío");

		ourException = new InternalErrorException("Error", operationOutcome);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=json");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			byte[] responseContentBytes = IOUtils.toByteArray(status.getEntity().getContent());
			String responseContent = new String(responseContentBytes, Charsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);
			assertThat(responseContent, containsString("El nombre está vacío"));
		}

	}

	@Test
	public void testPostWithNoBody() throws IOException {

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		try (CloseableHttpResponse status = ourClient.execute(httpPost)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(201, status.getStatusLine().getStatusCode());
			assertThat(status.getFirstHeader("Location").getValue(), containsString("Patient/123"));
		}

	}


	@Test
	public void testAuthorize() throws Exception {

		OperationOutcome operationOutcome = new OperationOutcome();
		operationOutcome.addIssue().setCode(IssueType.BUSINESSRULE);

		ourException = new AuthenticationException().addAuthenticateHeaderForRealm("REALM");

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(status.getStatusLine().toString());
			ourLog.info(responseContent);

			assertEquals(401, status.getStatusLine().getStatusCode());
			assertEquals("Basic realm=\"REALM\"", status.getFirstHeader("WWW-Authenticate").getValue());
		}

	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search()
		public List<Patient> search() {
			throw ourException;
		}

		@Create()
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			Validate.isTrue(thePatient == null);
			return new MethodOutcome().setId(new IdType("Patient/123"));
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
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

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

}
