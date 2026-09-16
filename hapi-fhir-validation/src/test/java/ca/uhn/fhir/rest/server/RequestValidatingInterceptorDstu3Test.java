package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class RequestValidatingInterceptorDstu3Test extends BaseValidationTestWithInlineMocks {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static boolean ourLastRequestWasSearch;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RequestValidatingInterceptorDstu3Test.class);
	private RequestValidatingInterceptor myInterceptor;

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider())
		.setDefaultResponseEncoding(EncodingEnum.JSON)
		.setDefaultPrettyPrint(false);

	@BeforeEach
	void before() {
		ourLastRequestWasSearch = false;
		ourServer.getInterceptorService().unregisterAllInterceptors();

		myInterceptor = new RequestValidatingInterceptor();
		//		myInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		//		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		//		myInterceptor.setResponseHeaderName("X-RESP");
		//		myInterceptor.setResponseHeaderValue(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_VALUE);

		ourServer.registerInterceptor(myInterceptor);
	}

	@Test
	void testCreateJsonInvalidNoFailure() {
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNotNull();
		assertThat(status.getBody()).doesNotContain("<severity value=\"error\"/>");
	}

	@Test
	void testCreateJsonInvalidNoValidatorsSpecified() {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNotNull();
		assertThat(status.getBody()).contains("\"severity\":\"error\"");
	}

	@Test
	void testCreateJsonValidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNull();
	}

	@Test
	void testCreateJsonValidNoValidatorsSpecifiedDefaultMessage() {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newJsonParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isEqualTo("NO ISSUES");
	}

	@Test
	void testCreateXmlInvalidInstanceValidator() {
		IValidatorModule module = new FhirInstanceValidator(ourCtx);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNotNull();
	}

	@Test
	void testCreateXmlInvalidNoValidatorsSpecified() {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNotNull();
	}

	@Test
	void testCreateXmlInvalidNoValidatorsSpecifiedOutcomeHeader() {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).startsWith("{\"resourceType\":\"OperationOutcome");
	}


	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionNpeNoIgnore() {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(false);

		Mockito.doThrow(new NullPointerException("SOME MESSAGE")).when(module).validateResource(Mockito.any(IValidationContext.class));

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(500);
		assertThat(status.getBody()).contains("<diagnostics value=\"" + Msg.code(331) + "java.lang.NullPointerException: SOME MESSAGE\"/>");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionNpeIgnore() {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(true);

		Mockito.doThrow(NullPointerException.class).when(module).validateResource(Mockito.any(IValidationContext.class));

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNull();
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionIseNoIgnore() {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(false);

		Mockito.doThrow(new InternalErrorException("FOO")).when(module).validateResource(Mockito.any(IValidationContext.class));

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(500);
		assertThat(status.getBody()).contains("<diagnostics value=\"FOO\"/>");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionIseIgnore() {
		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(true);

		Mockito.doThrow(InternalErrorException.class).when(module).validateResource(Mockito.any(IValidationContext.class));

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNull();
	}

	@Test
	void testCreateXmlValidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		HttpTestResponse status = ourServer.fhirRequest("/Patient").post(encoded, Constants.CT_FHIR_XML);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(201);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNull();
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
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNull();
	}

	@Test
	void testFetchMetadata() {
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		// This header caused a crash
		HttpTestResponse status = ourServer.fhirRequest("/metadata")
			.withHeader("Content-Type", "application/xml+fhir")
			.get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getBody()).contains("CapabilityStatement");
	}

	@Test
	void testSearch() {
		HttpTestResponse status = ourServer.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Request-Validation")).isNull();
		assertEquals(true, ourLastRequestWasSearch);
	}

	@AfterAll
	static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @IdParam IdType theIdParam) {
			return new MethodOutcome(new IdDt("Patient/001/_history/002"));
		}

		@Delete
		public MethodOutcome delete(@IdParam IdType theId) {
			return new MethodOutcome(theId.withVersion("2"));
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<IResource> search(@OptionalParam(name = "foo") StringParam theString) {
			ourLastRequestWasSearch = true;
			return new ArrayList<IResource>();
		}

	}

}
