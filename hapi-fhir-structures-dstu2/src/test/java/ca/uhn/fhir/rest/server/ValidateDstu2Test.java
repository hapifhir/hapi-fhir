package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateDstu2Test {
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static EncodingEnum ourLastEncoding;
	private static ValidationModeEnum ourLastMode;
	private static String ourLastProfile;
	private static String ourLastResourceBody;
	private static BaseOperationOutcome ourOutcomeToReturn;
	private static int ourPort;
	private static Server ourServer;

	@BeforeEach
	public void before() {
		ourLastResourceBody = null;
		ourLastEncoding = null;
		ourOutcomeToReturn = null;
		ourLastMode = null;
		ourLastProfile = null;
	}


	@Test
	public void testValidate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(patient);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(resp, stringContainsInOrder("<OperationOutcome"));
	}

	@Test
	public void testValidateWithNoParsed() throws Exception {

		Organization org = new Organization();
		org.addIdentifier().setValue("001");
		org.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(org);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Organization/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(ourLastResourceBody, stringContainsInOrder("\"resourceType\":\"Organization\"", "\"identifier\"", "\"value\":\"001"));
		assertEquals(EncodingEnum.JSON, ourLastEncoding);

	}

	@Test
	public void testValidateWithOptions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(patient);
		params.addParameter().setName("profile").setValue(new StringDt("http://foo"));
		params.addParameter().setName("mode").setValue(new StringDt(ValidationModeEnum.CREATE.getCode()));

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(resp, stringContainsInOrder("<OperationOutcome"));
		assertEquals("http://foo", ourLastProfile);
		assertEquals(ValidationModeEnum.CREATE, ourLastMode);
	}

	@Test
	public void testValidateWithResults() throws Exception {

		ourOutcomeToReturn = new OperationOutcome();
		ourOutcomeToReturn.addIssue().setDetails("FOOBAR");

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(patient);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(resp, stringContainsInOrder("<OperationOutcome", "FOOBAR"));
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
		servlet.setResourceProviders(patientProvider, new OrganizationProvider());
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

	public static class OrganizationProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Organization.class;
		}

		@Validate()
		public MethodOutcome validate(@ResourceParam String theResourceBody, @ResourceParam EncodingEnum theEncoding) {
			ourLastResourceBody = theResourceBody;
			ourLastEncoding = theEncoding;

			return new MethodOutcome(new IdDt("001"));
		}

	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Validate()
		public MethodOutcome validatePatient(@ResourceParam Patient thePatient, @Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile) {
			IdDt id = new IdDt(thePatient.getIdentifier().get(0).getValue());
			if (thePatient.getId().isEmpty() == false) {
				id = thePatient.getId();
			}

			ourLastMode = theMode;
			ourLastProfile = theProfile;

			MethodOutcome outcome = new MethodOutcome(id.withVersion("002"));
			outcome.setOperationOutcome(ourOutcomeToReturn);
			return outcome;
		}

	}

}
