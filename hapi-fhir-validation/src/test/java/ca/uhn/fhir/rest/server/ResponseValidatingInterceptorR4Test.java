package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ca.uhn.fhir.rest.api.EncodingEnum;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.*;
import org.mockito.Mockito;

import com.google.common.base.Charsets;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.*;

public class ResponseValidatingInterceptorR4Test {
	public static IBaseResource myReturnResource;

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseValidatingInterceptorR4Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;
	private ResponseValidatingInterceptor myInterceptor;

	@Before
	public void before() {
		myReturnResource = null;
		ourServlet.getInterceptorService().unregisterAllInterceptors();

		myInterceptor = new ResponseValidatingInterceptor();
		// myInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		// myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		// myInterceptor.setResponseHeaderName("X-RESP");
		// myInterceptor.setResponseHeaderValue(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_VALUE);

		ourServlet.registerInterceptor(myInterceptor);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorExceptionNpeNoIgnore() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(false);

		Mockito.doThrow(new NullPointerException("SOME MESSAGE")).when(module).validateResource(Mockito.any(IValidationContext.class));
		
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<diagnostics value=\"SOME MESSAGE\"/>"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorExceptionNpeIgnore() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(true);

		Mockito.doThrow(NullPointerException.class).when(module).validateResource(Mockito.any(IValidationContext.class));
		
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Response-Validation")));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorExceptionIseNoIgnore() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(false);

		Mockito.doThrow(new InternalErrorException("FOO")).when(module).validateResource(Mockito.any(IValidationContext.class));
		
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<diagnostics value=\"FOO\"/>"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInterceptorExceptionIseIgnore() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(true);

		Mockito.doThrow(InternalErrorException.class).when(module).validateResource(Mockito.any(IValidationContext.class));
		
		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Response-Validation")));
	}

	
	/**
	 * Test for #345
	 */
	@Test
	public void testDelete() throws Exception {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpDelete httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/123");

		CloseableHttpResponse status = ourClient.execute(httpDelete);
		try {
			ourLog.info("Response was:\n{}", status);

			assertEquals(204, status.getStatusLine().getStatusCode());
			assertThat(status.toString(), not(containsString("X-FHIR-Response-Validation")));
		} finally {
			IOUtils.closeQuietly(status);
		}
	}


	@Test
	public void testLongHeaderTruncated() throws Exception {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
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

			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
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

			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());

			ourLog.info("Response was:\n{}", status);
			ourLog.trace("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue(), endsWith("..."));
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue(), startsWith("{\"resourceType\":\"OperationOutcome\""));
		}
	}

	@Test
	public void testOperationOutcome() throws Exception {
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), (containsString(
				"X-FHIR-Response-Validation: {\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"information\",\"code\":\"informational\",\"diagnostics\":\"No issues detected\"}]}")));
	}

	/**
	 * Ignored until #264 is fixed
	 */
	@Test
	public void testSearchJsonInvalidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<severity value=\"error\"/>"));
	}

	@Test
	public void testSearchJsonValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
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
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), (containsString("X-FHIR-Response-Validation: NO ISSUES")));
	}

	@Test
	public void testSearchXmlInvalidInstanceValidator() throws Exception {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), containsString("X-FHIR-Response-Validation"));
	}

	/**
	 * Ignored until #264 is fixed
	 */
	@Test
	public void testSearchXmlInvalidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		Assert.assertThat(responseContent, Matchers.containsString("<severity value=\"error\"/>"));
	}

	@Test
	public void testSearchXmlValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Response-Validation")));
	}

	@Test
	public void testSkipEnabled() throws Exception {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.addExcludeOperationType(RestOperationTypeEnum.METADATA);
		myInterceptor.setResponseHeaderValueNoIssues("No issues");

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), not(containsString("X-FHIR-Response-Validation")));
	}

	@Test
	public void testSkipNotEnabled() throws Exception {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setResponseHeaderValueNoIssues("No issues");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpGet httpPost = new HttpGet("http://localhost:" + ourPort + "/metadata?_pretty=true");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
		ourLog.info(responseContent);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString(), (containsString("X-FHIR-Response-Validation")));
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
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setResourceProviders(patientProvider);
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);

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

	public static class PatientProvider implements IResourceProvider {

		@Delete
		public MethodOutcome delete(@IdParam IdType theId) {
			return new MethodOutcome(theId.withVersion("2"));
		}

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
