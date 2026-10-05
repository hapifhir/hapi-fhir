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
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
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
import static org.mockito.Mockito.mock;

class ResponseValidatingInterceptorDstu3Test extends BaseValidationTestWithInlineMocks {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseValidatingInterceptorDstu3Test.class);
	public static IBaseResource myReturnResource;
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private ResponseValidatingInterceptor myInterceptor;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider())
		.setDefaultResponseEncoding(EncodingEnum.XML);

	@BeforeEach
	void before() {
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
	void testInterceptorExceptionNpeNoIgnore() {
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

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(500);
		assertThat(status.getBody()).contains("<diagnostics value=\"" + Msg.code(331) + "java.lang.NullPointerException: SOME MESSAGE\"/>");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionNpeIgnore() {
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

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNull();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionIseNoIgnore() {
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

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(500);
		assertThat(status.getBody()).contains("<diagnostics value=\"FOO\"/>");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionIseIgnore() {
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

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNull();
	}


	/**
	 * Test for #345
	 */
	@Test
	void testDelete() {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpTestResponse status = ourServer.fhirRequest("/Patient/123").delete();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(204);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNull();
	}


	@Test
	void testLongHeaderTruncated() {
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

		{
			HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

			ourLog.info("Response was:\n{}", status);

			status.assertStatus(200);
			assertThat(status.getHeader("X-FHIR-Response-Validation")).endsWith("...");
			assertThat(status.getHeader("X-FHIR-Response-Validation")).startsWith("{\"resourceType\":\"OperationOutcome\"");
		}
		{
			myInterceptor.setMaximumHeaderLength(100);
			HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

			ourLog.info("Response was:\n{}", status);

			status.assertStatus(200);
			assertThat(status.getHeader("X-FHIR-Response-Validation")).endsWith("...");
			assertThat(status.getHeader("X-FHIR-Response-Validation")).startsWith("{\"resourceType\":\"OperationOutcome\"");
		}
	}

	@Test
	void testOperationOutcome() {
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation"))
			.isEqualTo(
				"{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"information\",\"code\":\"informational\",\"diagnostics\":\"No issues detected\"}]}");
	}

	/**
	 * Ignored until #264 is fixed
	 */
	@Test
	void testSearchJsonInvalidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getBody()).contains("<severity value=\"error\"/>");
	}

	@Test
	void testSearchJsonValidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNull();
	}

	@Test
	void testSearchJsonValidNoValidatorsSpecifiedDefaultMessage() {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isEqualTo("NO ISSUES");
	}

	@Test
	void testSearchXmlInvalidInstanceValidator() {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNotNull();
	}

	/**
	 * Ignored until #264 is fixed
	 */
	@Test
	void testSearchXmlInvalidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		myReturnResource = patient;

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getBody()).contains("<severity value=\"error\"/>");
	}

	@Test
	void testSearchXmlValidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		myReturnResource = patient;

		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNull();
	}

	@Test
	void testSkipEnabled() {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.addExcludeOperationType(RestOperationTypeEnum.METADATA);
		myInterceptor.setResponseHeaderValueNoIssues("No issues");

		HttpTestResponse status = ourServer.fhirRequest("/metadata").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNull();
	}

	@Test
	void testSkipNotEnabled() {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setResponseHeaderValueNoIssues("No issues");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		HttpTestResponse status = ourServer.fhirRequest("/metadata?_pretty=true").get();
		ourLog.info(status.getBody());

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNotNull();
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
	static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


}
