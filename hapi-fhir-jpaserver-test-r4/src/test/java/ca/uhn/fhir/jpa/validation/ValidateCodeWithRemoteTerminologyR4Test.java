package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.gclient.IOperationUnnamed;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersR4;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	private static final String LOCAL_CS_URL = "http://example.org/CodeSystem/multi-version";
	private static final String LOCAL_OLDER_VERSION = "1.0.0";
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
		assertThat(respParam.getParameterValue("message").toString()).isEqualTo("CodeSystem is unknown and can't be validated: %s for '%s%s'", INVALID_CODE_SYSTEM_URI, INVALID_CODE_SYSTEM_URI, "#P");
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
		myCodeSystemProvider.setShouldThrowExceptionForResourceNotFound(false);

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

	@Test
	public void validateCode_withValidCodeAndSystem_returnsIsValid() {

		Parameters params = new Parameters().addParameter("result", true).addParameter("display", DISPLAY);
		setupCodeSystemValidateCode(CODE_SYSTEM_V2_0247_URI, CODE_BODY_MASS_INDEX, params);

		Parameters inputParam = new Parameters()
			.addParameter("code", CODE_BODY_MASS_INDEX)
			.addParameter("url", new UriType(CODE_SYSTEM_V2_0247_URI));

		Parameters respParam = myClient.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameters(inputParam)
			.execute();

		assertThat(respParam.getParameterBool("result")).isTrue();
	}

	@Test
	void validateCode_withCodeableConcept_isValidatedByTheRemoteServer() {
		Parameters params = new Parameters().addParameter("result", true).addParameter("display", DISPLAY);
		setupCodeSystemValidateCode(CODE_SYSTEM_V2_0247_URI, CODE_BODY_MASS_INDEX, params);
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding()
			.setSystem(CODE_SYSTEM_V2_0247_URI)
			.setCode(CODE_BODY_MASS_INDEX);

		Parameters inParams = new Parameters()
			.addParameter("url", new UriType(CODE_SYSTEM_V2_0247_URI))
			.addParameter("codeableConcept", cc);

		Parameters respParam = myClient.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameters(inParams)
			.execute();

		assertThat(respParam.getParameterBool("result")).isTrue();
		assertThat(respParam.getParameterValue("display")).hasToString(DISPLAY);
	}

	// Created by Claude Opus 5.5
	@Test
	void validateCodeOperationOnValueSetInstance_includeNamesOlderLocalVersion_validatesAgainstThatVersion() {
		IIdType valueSetId = createLocalCodeSystemVersionsAndValueSet();

		Parameters codeInNamedVersion = validateCode(myClient.operation().onInstance(valueSetId), new Parameters()
			.addParameter("code", new CodeType("code-a"))
			.addParameter("system", new UriType(LOCAL_CS_URL)));
		Parameters codeOnlyInCurrentVersion = validateCode(myClient.operation().onInstance(valueSetId), new Parameters()
			.addParameter("code", new CodeType("code-b"))
			.addParameter("system", new UriType(LOCAL_CS_URL)));

		assertThat(codeInNamedVersion.getParameterBool("result")).as(message(codeInNamedVersion)).isTrue();
		assertThat(codeOnlyInCurrentVersion.getParameterBool("result")).as(message(codeOnlyInCurrentVersion)).isFalse();
	}

	// Created by Claude Opus 5.5
	@Test
	void validateCodeOperationOnCodeSystem_olderLocalVersion_validatesAgainstThatVersion() {
		createLocalCodeSystemVersionsAndValueSet();

		Parameters codeInNamedVersion = validateCode(myClient.operation().onType(CodeSystem.class), new Parameters()
			.addParameter("url", new UriType(LOCAL_CS_URL))
			.addParameter("version", new StringType(LOCAL_OLDER_VERSION))
			.addParameter("code", new CodeType("code-a")));
		Parameters codeOnlyInCurrentVersion = validateCode(myClient.operation().onType(CodeSystem.class), new Parameters()
			.addParameter("url", new UriType(LOCAL_CS_URL))
			.addParameter("version", new StringType(LOCAL_OLDER_VERSION))
			.addParameter("code", new CodeType("code-b")));

		assertThat(codeInNamedVersion.getParameterBool("result")).as(message(codeInNamedVersion)).isTrue();
		assertThat(codeOnlyInCurrentVersion.getParameterBool("result")).as(message(codeOnlyInCurrentVersion)).isFalse();
	}

	/**
	 * The remote server answers only for the version registered below, so a request that loses the version on
	 * the way finds no answer.
	 */
	// Created by Claude Opus 5.5
	@Test
	void validateCodeOperation_versionOfRemoteCodeSystem_sendsTheVersion() {
		final String version = "2.0.0";
		final String valueSetUrl = "http://hl7.org/fhir/ValueSet/list-example-codes";
		Parameters remoteResponse = new Parameters().addParameter("result", true).addParameter("display", DISPLAY);
		myCodeSystemProvider.addTerminologyResource(CODE_SYSTEM_V2_0247_URI, version);
		myCodeSystemProvider.addTerminologyResponse(OPERATION_VALIDATE_CODE, CODE_SYSTEM_V2_0247_URI, version, "P", remoteResponse);
		myValueSetProvider.addTerminologyResource(valueSetUrl);
		myValueSetProvider.addTerminologyResponse(OPERATION_VALIDATE_CODE, valueSetUrl, version, "P", remoteResponse);

		Parameters onCodeSystem = validateCode(myClient.operation().onType(CodeSystem.class), new Parameters()
			.addParameter("url", new UriType(CODE_SYSTEM_V2_0247_URI))
			.addParameter("version", new StringType(version))
			.addParameter("code", new CodeType("P")));
		Parameters onValueSet = validateCode(myClient.operation().onType(ValueSet.class), new Parameters()
			.addParameter("url", new UriType(valueSetUrl))
			.addParameter("code", new CodeType("P"))
			.addParameter("system", new UriType(CODE_SYSTEM_V2_0247_URI))
			.addParameter("systemVersion", new StringType(version)));

		assertThat(onCodeSystem.getParameterBool("result")).as(message(onCodeSystem)).isTrue();
		assertThat(onValueSet.getParameterBool("result")).as(message(onValueSet)).isTrue();
	}

	@Test
	void lookupOperation_versionOfRemoteCodeSystem_sendsSystemAndVersionApart() {
		final String version = "2.0.0";
		List<String> requestUrls = new ArrayList<>();
		mySvc.addClientInterceptor(new Object() {
			@Hook(Pointcut.CLIENT_REQUEST)
			public void capture(IHttpRequest theRequest) {
				requestUrls.add(theRequest.getUri());
			}
		});
		myCodeSystemProvider.addTerminologyResource(CODE_SYSTEM_V2_0247_URI, version);
		myCodeSystemProvider.addTerminologyResponse(JpaConstants.OPERATION_LOOKUP, CODE_SYSTEM_V2_0247_URI, "P", new Parameters()
			.addParameter("name", "v2-0247")
			.addParameter("version", version)
			.addParameter("display", DISPLAY));

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_LOOKUP)
			.withParameter(Parameters.class, "code", new CodeType("P"))
			.andParameter("system", new UriType(CODE_SYSTEM_V2_0247_URI))
			.andParameter("version", new StringType(version))
			.execute();

		assertThat(respParam.getParameterValue("display").primitiveValue()).isEqualTo(DISPLAY);
		assertThat(requestUrls)
			.filteredOn(url -> url.contains(JpaConstants.OPERATION_LOOKUP))
			.singleElement()
			.satisfies(url -> assertThat(url)
				.contains("system=" + URLEncoder.encode(CODE_SYSTEM_V2_0247_URI, StandardCharsets.UTF_8))
				.contains("version=" + version)
				.doesNotContain(URLEncoder.encode("|", StandardCharsets.UTF_8)));
	}

	private IIdType createLocalCodeSystemVersionsAndValueSet() {
		// the remote server holds neither, so the locally stored terminology must answer
		myCodeSystemProvider.setShouldThrowExceptionForResourceNotFound(false);
		myValueSetProvider.setShouldThrowExceptionForResourceNotFound(false);
		// 1.0.0 is written first and 1.0.1 second, so 1.0.1 is the current version
		createLocalCodeSystem(LOCAL_OLDER_VERSION, "code-a");
		createLocalCodeSystem("1.0.1", "code-b");
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl("http://example.org/ValueSet/older-version-only");
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.getCompose().addInclude().setSystem(LOCAL_CS_URL).setVersion(LOCAL_OLDER_VERSION).addConcept().setCode("code-a");
		IIdType valueSetId = myValueSetDao.create(valueSet, mySrd).getId().toUnqualifiedVersionless();
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		return valueSetId;
	}

	private void createLocalCodeSystem(String theVersion, String theCode) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(LOCAL_CS_URL);
		codeSystem.setVersion(theVersion);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.addConcept().setCode(theCode);
		myCodeSystemDao.create(codeSystem, mySrd);
	}

	private static Parameters validateCode(IOperationUnnamed theTarget, Parameters theParameters) {
		return theTarget.named(OPERATION_VALIDATE_CODE).withParameters(theParameters).execute();
	}

	private static String message(Parameters theResult) {
		return String.valueOf(theResult.getParameterValue("message"));
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
