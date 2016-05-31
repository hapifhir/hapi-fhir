package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class PreferTest {
	private static CloseableHttpClient ourClient;

	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PreferTest.class);
	private static int ourPort;
	private static Server ourServer;

	public static IBaseOperationOutcome ourReturnOperationOutcome;

	@Before
	public void before() {
		ourReturnOperationOutcome = null;
	}

	@Test
	public void testCreatePreferMinimalNoOperationOutcome() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

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
	public void testCreatePreferMinimalWithOperationOutcome() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_MINIMAL);
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
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"001\"/><meta><versionId value=\"002\"/></meta></Patient>", responseContent);
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

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
			IdDt id = new IdDt("Patient/001/_history/002");
			MethodOutcome retVal = new MethodOutcome(id);

			Patient pt = new Patient();
			pt.setId(id);
			retVal.setResource(pt);

			retVal.setOperationOutcome(ourReturnOperationOutcome);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Update()
		public MethodOutcome updatePatient(@ResourceParam Patient thePatient, @IdParam IdDt theIdParam) {
			IdDt id = new IdDt("Patient/001/_history/002");
			MethodOutcome retVal = new MethodOutcome(id);

			Patient pt = new Patient();
			pt.setId(id);
			retVal.setResource(pt);

			return retVal;
		}

	}

}
