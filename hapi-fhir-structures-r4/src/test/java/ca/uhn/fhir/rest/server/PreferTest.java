package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PreferTest {
	private static CloseableHttpClient ourClient;

	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PreferTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static IBaseOperationOutcome ourReturnOperationOutcome;

	@BeforeEach
	public void before() {
		ourReturnOperationOutcome = null;
	}

	@Test
	public void testCreatePreferMinimalNoOperationOutcome() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		
		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_MINIMAL);
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(Constants.STATUS_HTTP_201_CREATED, status.getStatusLine().getStatusCode());
		assertThat(responseContent, is(emptyOrNullString()));
		// assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), not(containsString("fhir")));
		assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE));
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

	}

	@Test
	public void testCreatePreferOperationOutcomeWithOperationOutcome() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(Constants.STATUS_HTTP_201_CREATED, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("DIAG"));
		assertEquals("application/xml+fhir;charset=utf-8", status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue().toLowerCase().replace(" ", ""));
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

	}

	@Test
	public void testCreatePreferRepresentation() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(Constants.STATUS_HTTP_201_CREATED, status.getStatusLine().getStatusCode());
		assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), containsString(Constants.CT_FHIR_XML));
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"001\"/><meta><versionId value=\"002\"/></meta><identifier><value value=\"002\"/></identifier></Patient>", responseContent);
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

	}

	@Test
	public void testCreateWithNoPrefer() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());

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

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
			IdType id = new IdType("Patient/001/_history/002");
			MethodOutcome retVal = new MethodOutcome(id);

			thePatient.setId(id);
			retVal.setResource(thePatient);

			retVal.setOperationOutcome(ourReturnOperationOutcome);

			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update()
		public MethodOutcome updatePatient(@ResourceParam Patient thePatient, @IdParam IdType theIdParam) {
			IdDt id = new IdDt("Patient/001/_history/002");
			MethodOutcome retVal = new MethodOutcome(id);

			thePatient.setId(id);
			retVal.setResource(thePatient);

			return retVal;
		}

	}

}
