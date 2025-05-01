package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ResponseValidatingInterceptorDstu3Test extends BaseValidationTestWithInlineMocks {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseValidatingInterceptorDstu3Test.class);
	public static IBaseResource myReturnResource;
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private ResponseValidatingInterceptor myInterceptor;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider())
		.setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		myReturnResource = null;
		ourServer.getInterceptorService().unregisterAllInterceptors();

		myInterceptor = new ResponseValidatingInterceptor();
		// myInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		// myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		// myInterceptor.setResponseHeaderName("X-RESP");
		// myInterceptor.setResponseHeaderValue(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_VALUE);

		ourServer.registerInterceptor(myInterceptor);
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

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<diagnostics value=\"" + Msg.code(331) + "java.lang.NullPointerException: SOME MESSAGE\"/>");
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

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Response-Validation");
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

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<diagnostics value=\"FOO\"/>");
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

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Response-Validation");
	}


	/**
	 * Test for #345
	 */
	@Test
	public void testDelete() throws Exception {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpDelete httpDelete = new HttpDelete(ourServer.getBaseUrl() + "/Patient/123");

		CloseableHttpResponse status = ourClient.execute(httpDelete);
		try {
			ourLog.info("Response was:\n{}", status);

			assertEquals(204, status.getStatusLine().getStatusCode());
			assertThat(status.toString()).doesNotContain("X-FHIR-Response-Validation");
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

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		{
			HttpResponse status = ourClient.execute(httpPost);

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			ourLog.info("Response was:\n{}", status);
			ourLog.trace("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue()).endsWith("...");
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue()).startsWith("{\"resourceType\":\"OperationOutcome\"");
		}
		{
			myInterceptor.setMaximumHeaderLength(100);
			HttpResponse status = ourClient.execute(httpPost);

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());

			ourLog.info("Response was:\n{}", status);
			ourLog.trace("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue()).endsWith("...");
			assertThat(status.getFirstHeader("X-FHIR-Response-Validation").getValue()).startsWith("{\"resourceType\":\"OperationOutcome\"");
		}
	}

	@Test
	public void testOperationOutcome() throws Exception {
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString().contains(
			"X-FHIR-Response-Validation: {\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"information\",\"code\":\"informational\",\"diagnostics\":\"No issues detected\"}]}"));
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

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<severity value=\"error\"/>");
	}

	@Test
	public void testSearchJsonValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Response-Validation");
	}

	@Test
	public void testSearchJsonValidNoValidatorsSpecifiedDefaultMessage() throws Exception {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString().contains("X-FHIR-Response-Validation: NO ISSUES"));
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

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).contains("X-FHIR-Response-Validation");
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

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<severity value=\"error\"/>");
	}

	@Test
	public void testSearchXmlValidNoValidatorsSpecified() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/Patient?foo=bar");

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.trace("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Response-Validation");
	}

	@Test
	public void testSkipEnabled() throws Exception {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.addExcludeOperationType(RestOperationTypeEnum.METADATA);
		myInterceptor.setResponseHeaderValueNoIssues("No issues");

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/metadata");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.toString()).doesNotContain("X-FHIR-Response-Validation");
	}

	@Test
	public void testSkipNotEnabled() throws Exception {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setResponseHeaderValueNoIssues("No issues");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpGet httpPost = new HttpGet(ourServer.getBaseUrl() + "/metadata?_pretty=true");
		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info(responseContent);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", status);
		ourLog.info("Response was:\n{}", responseContent);

		assertThat(status.getStatusLine().getStatusCode()).as(responseContent).isEqualTo(200);
		assertThat(status.toString().contains("X-FHIR-Response-Validation"));
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
			ArrayList<IBaseResource> retVal = new ArrayList<>();
			myReturnResource.setId("1");
			retVal.add(myReturnResource);
			return retVal;
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


}
