package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;

public class ResponseValidatingInterceptorDstu3Test {
	private static CloseableHttpClient ourClient;

	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseValidatingInterceptorDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;
	private ResponseValidatingInterceptor myInterceptor;
	public static IBaseResource myReturnResource;

	@Before
	public void before() {
		myReturnResource = null;
		while (ourServlet.getInterceptors().size() > 0) {
			ourServlet.unregisterInterceptor(ourServlet.getInterceptors().get(0));
		}

		myInterceptor = new ResponseValidatingInterceptor();
		// myInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		// myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		// myInterceptor.setResponseHeaderName("X-RESP");
		// myInterceptor.setResponseHeaderValue(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_VALUE);

		ourServlet.registerInterceptor(myInterceptor);
	}

	/**
	 * Ignored until #264 is fixed
	 */
	@Test
	@Ignore
	public void testSearchJsonInvalidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-FHIR-Response-Validation"));
		assertThat(responseContent, containsString("<severity value=\"error\"/>"));
	}

	@Test
	public void testSearchJsonValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Response-Validation")));
	}

	@Test
	public void testSearchJsonValidNoValidatorsSpecifiedDefaultMessage() throws Exception {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), (containsString("X-FHIR-Response-Validation: NO ISSUES")));
	}

	/**
	 * Ignored until #264 is fixed
	 */
	@Test
	@Ignore
	public void testSearchXmlInvalidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-FHIR-Response-Validation"));
	}

	@Test
	public void testSearchXmlInvalidInstanceValidator() throws Exception {
		IValidatorModule module = new FhirInstanceValidator();
		myInterceptor.addValidatorModule(module);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-FHIR-Response-Validation"));
	}

	@Test
	public void testSkipEnabled() throws Exception {
		IValidatorModule module = new FhirInstanceValidator();
		myInterceptor.addValidatorModule(module);
		myInterceptor.addExcludeOperationType(RestOperationTypeEnum.METADATA);
		myInterceptor.setResponseHeaderValueNoIssues("No issues");

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Response-Validation")));
	}

	@Test
	public void testSkipNotEnabled() throws Exception {
		IValidatorModule module = new FhirInstanceValidator();
		myInterceptor.addValidatorModule(module);
		myInterceptor.setResponseHeaderValueNoIssues("No issues");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), (containsString("X-FHIR-Response-Validation")));
	}

	@Test
	public void testSearchXmlValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Response-Validation")));
	}

	@Test
	public void testOperationOutcome() throws Exception {
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), (containsString(
				"X-FHIR-Response-Validation: {\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"information\",\"code\":\"informational\",\"diagnostics\":\"No issues detected\"}]}")));
	}

	@Test
	public void testLongHeaderTruncated() throws Exception {
		IValidatorModule module = new FhirInstanceValidator();
		myInterceptor.addValidatorModule(module);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		myInterceptor.setFailOnSeverity(null);

		Patient patient = new Patient();
		for (int i = 0; i < 1000; i++) {
			patient.addContact().setGender(AdministrativeGender.MALE);
		}
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		{
			HttpResponse status = ourClient.execute(httpPost);

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			ourLog.info("Response was:\n{}", status);
			ourLog.trace("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue(), endsWith("..."));
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue(), startsWith("{\"resourceType\":\"OperationOutcome\""));
		}
		{
			myInterceptor.setMaximumHeaderLength(100);
			HttpResponse status = ourClient.execute(httpPost);

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			ourLog.info("Response was:\n{}", status);
			ourLog.trace("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue(), endsWith("..."));
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue(), startsWith("{\"resourceType\":\"OperationOutcome\""));
		}
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		PatientProvider patientProvider = new PatientProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
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

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search
		public ArrayList<IBaseResource> search(@OptionalParam(name = "foo") StringParam theString) {
			ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();
			myReturnResource.setId("1");
			retVal.add(myReturnResource);
			return retVal;
		}

	}

}
