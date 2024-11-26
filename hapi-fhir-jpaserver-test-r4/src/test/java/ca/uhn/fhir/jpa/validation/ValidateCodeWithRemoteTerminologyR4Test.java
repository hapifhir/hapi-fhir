package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersR4;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_VALIDATE_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This set of integration tests that instantiates and injects an instance of
 * {@link org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport}
 * into the ValidationSupportChain, which tests the logic of dynamically selecting the correct Remote Terminology
 * implementation. It also exercises the validateCode output translation code found in
 * {@link org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport}
 */
public class ValidateCodeWithRemoteTerminologyR4Test extends BaseResourceProviderR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateCodeWithRemoteTerminologyR4Test.class);
	private static final String DISPLAY = "DISPLAY";
	private static final String DISPLAY_BODY_MASS_INDEX = "Body mass index (BMI) [Ratio]";
	private static final String CODE_BODY_MASS_INDEX = "39156-5";
	private static final String CODE_SYSTEM_V2_0247_URI = "http://terminology.hl7.org/CodeSystem/v2-0247";
	private static final String INVALID_CODE_SYSTEM_URI = "http://terminology.hl7.org/CodeSystem/INVALID-CODESYSTEM";
	private static final String UNKNOWN_VALUE_SYSTEM_URI = "http://hl7.org/fhir/ValueSet/unknown-value-set";
	private static final FhirContext ourCtx = FhirContext.forR4();

	@RegisterExtension
	protected static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private RemoteTerminologyServiceValidationSupport mySvc;
	private IValidationProviders.MyValidationProvider<CodeSystem> myCodeSystemProvider;
	private IValidationProviders.MyValidationProvider<ValueSet> myValueSetProvider;

	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;

	@BeforeEach
	public void before() throws Exception {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, baseUrl);
		myValidationSupportChain.addValidationSupport(0, mySvc);
		myCodeSystemProvider = new IValidationProvidersR4.MyCodeSystemProviderR4();
		myValueSetProvider = new IValidationProvidersR4.MyValueSetProviderR4();
		ourRestfulServerExtension.registerProvider(myCodeSystemProvider);
		ourRestfulServerExtension.registerProvider(myValueSetProvider);
	}

	@AfterEach
	public void after() {
		myValidationSupportChain.removeValidationSupport(mySvc);
		ourRestfulServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
		ourRestfulServerExtension.unregisterProvider(myCodeSystemProvider);
		ourRestfulServerExtension.unregisterProvider(myValueSetProvider);
	}

	@Test
	public void validateCodeOperationOnCodeSystem_byCodingAndUrlWhereSystemIsDifferent_throwsException() {
		assertThatExceptionOfType(InvalidRequestException.class).isThrownBy(() -> myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_VALIDATE_CODE)
				.withParameter(Parameters.class, "coding", new Coding().setSystem(CODE_SYSTEM_V2_0247_URI).setCode("P"))
				.andParameter("url", new UriType(INVALID_CODE_SYSTEM_URI))
				.execute());
	}

	@Test
	public void validateCodeOperationOnCodeSystem_byCodingAndUrl_usingBuiltInCodeSystems() {
		final String code = "P";
		final String system = CODE_SYSTEM_V2_0247_URI;;

		Parameters params = new Parameters().addParameter("result", true).addParameter("display", DISPLAY);
		setupCodeSystemValidateCode(system, code, params);

		logAllConcepts();

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "coding", new Coding().setSystem(system).setCode(code))
			.andParameter("url", new UriType(system))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameterValue("result")).booleanValue());
		assertEquals(DISPLAY, respParam.getParameterValue("display").toString());
	}

	@Test
	public void validateCodeOperationOnCodeSystem_byCodingAndUrlWhereCodeSystemIsUnknown_returnsFalse() {
		myCodeSystemProvider.setShouldThrowExceptionForResourceNotFound(false);

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "coding", new Coding()
				.setSystem(INVALID_CODE_SYSTEM_URI).setCode("P"))
			.andParameter("url", new UriType(INVALID_CODE_SYSTEM_URI))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertFalse(((BooleanType) respParam.getParameterValue("result")).booleanValue());
		assertThat(respParam.getParameterValue("message").toString()).isEqualTo("Terminology service was unable to provide validation for " + INVALID_CODE_SYSTEM_URI +
			"#P");
	}

	@Test
	public void validateCodeOperationOnValueSet_byCodingAndUrlWhereSystemIsDifferent_throwsException() {
		try {
			myClient.operation()
				.onType(ValueSet.class)
				.named(JpaConstants.OPERATION_VALIDATE_CODE)
				.withParameter(Parameters.class, "coding", new Coding().setSystem(CODE_SYSTEM_V2_0247_URI).setCode("P"))
				.andParameter("url", new UriType("http://hl7.org/fhir/ValueSet/list-example-codes"))
				.andParameter("system", new UriType(INVALID_CODE_SYSTEM_URI))
				.execute();
			fail();
		} catch (InvalidRequestException exception) {
			assertThat(exception.getMessage()).isEqualTo("HTTP 400 Bad Request: HAPI-2352: Coding.system '" + CODE_SYSTEM_V2_0247_URI + "' " +
				"does not equal param system '" + INVALID_CODE_SYSTEM_URI + "'. Unable to validate-code.");
		}
	}

	@Test
	public void validateCodeOperationOnValueSet_byUrlAndSystem_usingBuiltInCodeSystems() {
		final String code = "alerts";
		final String system = "http://terminology.hl7.org/CodeSystem/list-example-use-codes";
		final String valueSetUrl = "http://hl7.org/fhir/ValueSet/list-example-codes";

		Parameters params = new Parameters().addParameter("result", true).addParameter("display", DISPLAY);
		setupValueSetValidateCode(valueSetUrl, system, code, params);
		setupCodeSystemValidateCode(system, code, params);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "code", new CodeType(code))
			.andParameter("system", new UriType(system))
			.andParameter("url", new UriType(valueSetUrl))
			.useHttpGet()
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameterValue("result")).booleanValue());
		assertEquals(DISPLAY, respParam.getParameterValue("display").toString());
	}

	@Test
	public void validateCodeOperationOnValueSet_byUrlSystemAndCode() {
		final String code = CODE_BODY_MASS_INDEX;
		final String system = "http://terminology.hl7.org/CodeSystem/list-example-use-codes";
		final String valueSetUrl = "http://hl7.org/fhir/ValueSet/list-example-codes";

		Parameters params = new Parameters().addParameter("result", true).addParameter("display", DISPLAY_BODY_MASS_INDEX);
		setupValueSetValidateCode(valueSetUrl, system, code, params);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "code", new CodeType(code))
			.andParameter("url", new UriType(valueSetUrl))
			.andParameter("system", new UriType(system))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameterValue("result")).booleanValue());
		assertEquals(DISPLAY_BODY_MASS_INDEX, respParam.getParameterValue("display").toString());
	}

	@Test
	public void validateCodeOperationOnValueSet_byCodingAndUrlWhereValueSetIsUnknown_returnsFalse() {
		myValueSetProvider.setShouldThrowExceptionForResourceNotFound(false);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "coding", new Coding()
				.setSystem(CODE_SYSTEM_V2_0247_URI).setCode("P"))
			.andParameter("url", new UriType(UNKNOWN_VALUE_SYSTEM_URI))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertFalse(((BooleanType) respParam.getParameterValue("result")).booleanValue());
		assertThat(respParam.getParameterValue("message").toString()).isEqualTo("Validator is unable to provide validation for P#" + CODE_SYSTEM_V2_0247_URI +
			" - Unknown or unusable ValueSet[" + UNKNOWN_VALUE_SYSTEM_URI + "]");
	}

	private void setupValueSetValidateCode(String theUrl, String theSystem, String theCode, IBaseParameters theResponseParams) {
		ValueSet valueSet = myValueSetProvider.addTerminologyResource(theUrl);
		myValueSetProvider.addTerminologyResource(theSystem);
		myValueSetProvider.addTerminologyResponse(OPERATION_VALIDATE_CODE, valueSet.getUrl(), theCode, theResponseParams);

		// we currently do this because VersionSpecificWorkerContextWrapper has logic to infer the system when missing
		// based on the ValueSet by calling ValidationSupportUtils#extractCodeSystemForCode.
		valueSet.getCompose().addInclude().setSystem(theSystem);
	}

	private void setupCodeSystemValidateCode(String theUrl, String theCode, IBaseParameters theResponseParams) {
		CodeSystem codeSystem = myCodeSystemProvider.addTerminologyResource(theUrl);
		myCodeSystemProvider.addTerminologyResponse(OPERATION_VALIDATE_CODE, codeSystem.getUrl(), theCode, theResponseParams);
	}
}
