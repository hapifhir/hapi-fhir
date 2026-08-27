package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.ResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.validation.IValidationContext;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ResponseValidatingInterceptorR4Test extends BaseValidationTestWithInlineMocks {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseValidatingInterceptorR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	static RestfulServerExtension ourServlet = new RestfulServerExtension(ourCtx);
	@RegisterExtension
	@Order(1)
	static ResourceProviderExtension<RequestValidatingInterceptorR4Test.PatientProvider> ourProvider = new ResourceProviderExtension<>(ourServlet, new RequestValidatingInterceptorR4Test.PatientProvider());
	private ResponseValidatingInterceptor myInterceptor;

	@BeforeEach
	void before() {
		ourProvider.getProvider().setReturnResource(null);
		ourProvider.getProvider().ourLastGraphQlQueryGet = null;
		ourProvider.getProvider().ourLastGraphQlQueryPost = null;
		ourServlet.unregisterAllInterceptors();

		myInterceptor = new ResponseValidatingInterceptor();
		// myInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
		// myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		// myInterceptor.setResponseHeaderName("X-RESP");
		// myInterceptor.setResponseHeaderValue(RequestValidatingInterceptor.DEFAULT_RESPONSE_HEADER_VALUE);

		ourServlet.registerInterceptor(myInterceptor);
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionNpeNoIgnore() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		ourProvider.getProvider().setReturnResource(patient);

		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(false);

		Mockito.doThrow(new NullPointerException("SOME MESSAGE")).when(module).validateResource(Mockito.any(IValidationContext.class));

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(500);
		assertThat(status.getBody()).contains("\"diagnostics\": \"" + Msg.code(331) + "java.lang.NullPointerException: SOME MESSAGE\"");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionNpeIgnore() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		ourProvider.getProvider().setReturnResource(patient);

		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(true);

		Mockito.doThrow(NullPointerException.class).when(module).validateResource(Mockito.any(IValidationContext.class));

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

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
		ourProvider.getProvider().setReturnResource(patient);

		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(false);

		Mockito.doThrow(new InternalErrorException("FOO")).when(module).validateResource(Mockito.any(IValidationContext.class));

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(500);
		assertThat(status.getBody()).contains("\"diagnostics\": \"FOO\"");
	}

	@SuppressWarnings("unchecked")
	@Test
	void testInterceptorExceptionIseIgnore() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		ourProvider.getProvider().setReturnResource(patient);

		myInterceptor.setAddResponseHeaderOnSeverity(null);
		myInterceptor.setFailOnSeverity(null);
		myInterceptor.setAddResponseOutcomeHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
		IValidatorModule module = mock(IValidatorModule.class);
		myInterceptor.addValidatorModule(module);
		myInterceptor.setIgnoreValidatorExceptions(true);

		Mockito.doThrow(InternalErrorException.class).when(module).validateResource(Mockito.any(IValidationContext.class));

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

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

		HttpTestResponse status = ourServlet.fhirRequest("/Patient/123").delete();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(204);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNull();
	}

	@Test
	void testGraphQlRequestResponse_GET() {
		HttpTestResponse status = ourServlet.fhirRequest("/Patient/123/$graphql?query=" + UrlUtil.escapeUrlParam("{name}")).get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertEquals("{\"name\":{\"family\": \"foo\"}}", status.getBody());
		assertEquals("{name}", ourProvider.getProvider().ourLastGraphQlQueryGet);
	}

	@Test
	void testGraphQlRequestResponse_POST() {
		HttpTestResponse status = ourServlet.fhirRequest("/Patient/123/$graphql").post("{\"query\": \"{name}\"}", Constants.CT_JSON);

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertEquals("{\"name\":{\"family\": \"foo\"}}", status.getBody());
		assertEquals("{name}", ourProvider.getProvider().ourLastGraphQlQueryPost);
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
		ourProvider.getProvider().setReturnResource(patient);

		{
			HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

			ourLog.info("Response was:\n{}", status);

			status.assertStatus(200);
			assertThat(status.getHeader("X-FHIR-Response-Validation")).endsWith("...");
			assertThat(status.getHeader("X-FHIR-Response-Validation")).startsWith("{\"resourceType\":\"OperationOutcome\"");
		}
		{
			myInterceptor.setMaximumHeaderLength(100);
			HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

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
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		ourProvider.getProvider().setReturnResource(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation"))
			.isEqualTo(
				"{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"information\",\"code\":\"informational\",\"diagnostics\":\"No issues detected\"}]}");
	}

	@Test
	void testSearchJsonInvalidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		patient.addContact().addRelationship().setText("FOO");
		ourProvider.getProvider().setReturnResource(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getBody()).contains("\"severity\": \"error\"");
	}

	@Test
	void testSearchJsonValidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		ourProvider.getProvider().setReturnResource(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNull();
	}

	@Test
	void testSearchJsonValidNoValidatorsSpecifiedDefaultMessage() {
		myInterceptor.setResponseHeaderValueNoIssues("NO ISSUES");
		myInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);

		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		ourProvider.getProvider().setReturnResource(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

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
		ourProvider.getProvider().setReturnResource(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

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
		ourProvider.getProvider().setReturnResource(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(422);
		assertThat(status.getBody()).contains("\"severity\": \"error\"");
	}

	@Test
	void testSearchXmlValidNoValidatorsSpecified() {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.addIdentifier().setValue("002");
		patient.setGender(AdministrativeGender.MALE);
		ourProvider.getProvider().setReturnResource(patient);

		HttpTestResponse status = ourServlet.fhirRequest("/Patient?foo=bar").get();

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

		HttpTestResponse status = ourServlet.fhirRequest("/metadata").get();

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

		HttpTestResponse status = ourServlet.fhirRequest("/metadata?_pretty=true").get();
		ourLog.info(status.getBody());

		ourLog.info("Response was:\n{}", status);

		status.assertStatus(200);
		assertThat(status.getHeader("X-FHIR-Response-Validation")).isNotNull();
	}

}
