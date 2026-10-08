package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.gclient.IOperationUnnamed;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
	private static final String VERSIONED_VS_URL = "http://example.org/ValueSet/versioned";
	private static final String PINNED_VS_URL = "http://example.org/ValueSet/older-version-only";
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

	// Created by Claude Opus 5.5
	@Test
	void validateCodeOperationOnCodeSystem_codingNamesOlderLocalVersion_validatesAgainstThatVersion() {
		createLocalCodeSystemVersionsAndValueSet();
		Coding codingInNamedVersion = new Coding(LOCAL_CS_URL, "code-a", null).setVersion(LOCAL_OLDER_VERSION);
		Coding codingOnlyInCurrentVersion = new Coding(LOCAL_CS_URL, "code-b", null).setVersion(LOCAL_OLDER_VERSION);

		Parameters byCoding = validateCode(myClient.operation().onType(CodeSystem.class), new Parameters()
			.addParameter("url", new UriType(LOCAL_CS_URL))
			.addParameter("coding", codingInNamedVersion));
		Parameters byCodeableConcept = validateCode(myClient.operation().onType(CodeSystem.class), new Parameters()
			.addParameter("url", new UriType(LOCAL_CS_URL))
			.addParameter("codeableConcept", new CodeableConcept(codingInNamedVersion)));
		Parameters notInNamedVersion = validateCode(myClient.operation().onType(CodeSystem.class), new Parameters()
			.addParameter("url", new UriType(LOCAL_CS_URL))
			.addParameter("coding", codingOnlyInCurrentVersion));

		assertThat(byCoding.getParameterBool("result")).as(message(byCoding)).isTrue();
		assertThat(byCodeableConcept.getParameterBool("result")).as(message(byCodeableConcept)).isTrue();
		assertThat(notInNamedVersion.getParameterBool("result")).as(message(notInNamedVersion)).isFalse();
	}

	// Created by Claude Opus 5.5
	@Test
	void validateCodeOperationOnCodeSystem_codingVersionDiffersFromVersionParameter_throwsException() {
		createLocalCodeSystemVersionsAndValueSet();

		assertThatExceptionOfType(InvalidRequestException.class)
			.isThrownBy(() -> validateCode(myClient.operation().onType(CodeSystem.class), new Parameters()
				.addParameter("url", new UriType(LOCAL_CS_URL))
				.addParameter("version", new StringType("1.0.1"))
				.addParameter("coding", new Coding(LOCAL_CS_URL, "code-a", null).setVersion(LOCAL_OLDER_VERSION))))
			.withMessageContaining("HAPI-2952");
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

	/**
	 * code-a is only in the older local version, so it is valid exactly when the version that reaches the
	 * validation is the older one, whichever parameter carries it.
	 */
	// Created by Claude Opus 5.5
	@ParameterizedTest(name = "{0}")
	@MethodSource("codeSystemVersionPlacements")
	void validateCodeOperationOnCodeSystem_versionPlacement_validatesAgainstTheNamedVersion(
			String theCase, Parameters theParameters, Expected theExpected) {
		createLocalCodeSystemVersionsAndValueSet();

		assertValidateCodeOutcome(myClient.operation().onType(CodeSystem.class), theParameters, theExpected);
	}

	static Stream<Arguments> codeSystemVersionPlacements() {
		String packed = LOCAL_CS_URL + "|" + LOCAL_OLDER_VERSION;
		Coding unversioned = new Coding(LOCAL_CS_URL, "code-a", null);
		Coding older = new Coding(LOCAL_CS_URL, "code-a", null).setVersion(LOCAL_OLDER_VERSION);
		Coding newer = new Coding(LOCAL_CS_URL, "code-a", null).setVersion("1.0.1");
		Coding noSystem = new Coding(null, "code-a", null);
		Coding otherSystem = new Coding("http://example.org/other", "code-a", null);
		return Stream.of(
				Arguments.of("code, no version", codeParams(LOCAL_CS_URL, null), Expected.INVALID),
				Arguments.of("code, version parameter", codeParams(LOCAL_CS_URL, LOCAL_OLDER_VERSION), Expected.VALID),
				Arguments.of("code, version packed into url", codeParams(packed, null), Expected.VALID),
				Arguments.of("code, packed and parameter agree", codeParams(packed, LOCAL_OLDER_VERSION), Expected.VALID),
				Arguments.of("code, packed and parameter differ", codeParams(packed, "1.0.1"), Expected.ERROR),
				Arguments.of("coding, no version", codingParams(LOCAL_CS_URL, null, unversioned), Expected.INVALID),
				Arguments.of("coding version", codingParams(LOCAL_CS_URL, null, older), Expected.VALID),
				Arguments.of(
						"coding without version, version parameter",
						codingParams(LOCAL_CS_URL, LOCAL_OLDER_VERSION, unversioned),
						Expected.VALID),
				Arguments.of(
						"coding version and parameter agree",
						codingParams(LOCAL_CS_URL, LOCAL_OLDER_VERSION, older),
						Expected.VALID),
				Arguments.of("coding version and parameter differ", codingParams(LOCAL_CS_URL, "1.0.1", older), Expected.ERROR),
				Arguments.of(
						"coding without system, version parameter",
						codingParams(LOCAL_CS_URL, LOCAL_OLDER_VERSION, noSystem),
						Expected.VALID),
				Arguments.of("coding version, version packed into url", codingParams(packed, null, older), Expected.VALID),
				Arguments.of("coding version and packed url differ", codingParams(packed, null, newer), Expected.ERROR),
				Arguments.of("coding version, no url", codingParams(null, null, older), Expected.VALID),
				Arguments.of(
						"codeableConcept coding version",
						codeableConceptParams(LOCAL_CS_URL, null, new CodeableConcept(older)),
						Expected.VALID),
				Arguments.of(
						"codeableConcept without version, version parameter",
						codeableConceptParams(LOCAL_CS_URL, LOCAL_OLDER_VERSION, new CodeableConcept(unversioned)),
						Expected.VALID),
				Arguments.of(
						"codeableConcept without version, version packed into url",
						codeableConceptParams(packed, null, new CodeableConcept(unversioned)),
						Expected.VALID),
				Arguments.of(
						"codeableConcept with a coding from another system",
						codeableConceptParams(LOCAL_CS_URL, null, new CodeableConcept(otherSystem).addCoding(older)),
						Expected.VALID),
				Arguments.of(
						"codeableConcept coding version and parameter differ",
						codeableConceptParams(LOCAL_CS_URL, "1.0.1", new CodeableConcept(older)),
						Expected.ERROR));
	}

	/**
	 * An instance is one stored version of the code system, so it is validated against that version, and a version
	 * named in the request must agree with it.
	 */
	// Created by Claude Opus 5.5
	@ParameterizedTest(name = "{0}")
	@MethodSource("codeSystemInstanceVersionPlacements")
	void validateCodeOperationOnCodeSystemInstance_versionPlacement_validatesAgainstTheInstanceVersion(
			String theCase, Parameters theParameters, Expected theExpected) {
		createLocalCodeSystemVersionsAndValueSet();
		IIdType olderVersionId = myCodeSystemDao
				.search(SearchParameterMap.newSynchronous()
						.add(CodeSystem.SP_URL, new UriParam(LOCAL_CS_URL))
						.add(CodeSystem.SP_VERSION, new TokenParam(LOCAL_OLDER_VERSION)), mySrd)
				.getResources(0, 1)
				.get(0)
				.getIdElement()
				.toUnqualifiedVersionless();

		assertValidateCodeOutcome(myClient.operation().onInstance(olderVersionId), theParameters, theExpected);
	}

	static Stream<Arguments> codeSystemInstanceVersionPlacements() {
		Coding older = new Coding(LOCAL_CS_URL, "code-a", null).setVersion(LOCAL_OLDER_VERSION);
		Coding newer = new Coding(LOCAL_CS_URL, "code-a", null).setVersion("1.0.1");
		return Stream.of(
				Arguments.of("code, no version", codeParams(null, null), Expected.VALID),
				Arguments.of("code, version parameter agrees", codeParams(null, LOCAL_OLDER_VERSION), Expected.VALID),
				Arguments.of("code, version parameter differs", codeParams(null, "1.0.1"), Expected.ERROR),
				Arguments.of("coding version agrees", codingParams(null, null, older), Expected.VALID),
				Arguments.of("coding version differs", codingParams(null, null, newer), Expected.ERROR));
	}

	/**
	 * Version 1 of the versioned ValueSet includes code-a from the older code system version, and version 2, the
	 * current one, includes code-b from the newer one.
	 */
	// Created by Claude Opus 5.5
	@ParameterizedTest(name = "{0}")
	@MethodSource("valueSetVersionPlacements")
	void validateCodeOperationOnValueSet_valueSetVersionPlacement_validatesAgainstTheNamedVersion(
			String theCase, Parameters theParameters, Expected theExpected) {
		createLocalValueSetVersions();

		assertValidateCodeOutcome(myClient.operation().onType(ValueSet.class), theParameters, theExpected);
	}

	static Stream<Arguments> valueSetVersionPlacements() {
		String packed = VERSIONED_VS_URL + "|1";
		return Stream.of(
				Arguments.of("url, no version", valueSetCodeParams(VERSIONED_VS_URL, null, LOCAL_CS_URL, null), Expected.INVALID),
				Arguments.of(
						"valueSetVersion parameter",
						valueSetCodeParams(VERSIONED_VS_URL, "1", LOCAL_CS_URL, null),
						Expected.VALID),
				Arguments.of("version packed into url", valueSetCodeParams(packed, null, LOCAL_CS_URL, null), Expected.VALID),
				Arguments.of(
						"packed and parameter agree", valueSetCodeParams(packed, "1", LOCAL_CS_URL, null), Expected.VALID),
				Arguments.of(
						"packed and parameter differ", valueSetCodeParams(packed, "2", LOCAL_CS_URL, null), Expected.ERROR));
	}

	/**
	 * The pinned ValueSet includes code-a from the older code system version only. The systemVersion parameter is
	 * the version of the system parameter, so it does not apply to a coding, which carries its own version.
	 */
	// Created by Claude Opus 5.5
	@ParameterizedTest(name = "{0}")
	@MethodSource("valueSetSystemVersionPlacements")
	void validateCodeOperationOnValueSet_systemVersionPlacement_validatesAgainstTheNamedVersion(
			String theCase, Parameters theParameters, Expected theExpected) {
		createLocalValueSetVersions();

		assertValidateCodeOutcome(myClient.operation().onType(ValueSet.class), theParameters, theExpected);
	}

	static Stream<Arguments> valueSetSystemVersionPlacements() {
		String packed = LOCAL_CS_URL + "|" + LOCAL_OLDER_VERSION;
		Coding unversioned = new Coding(LOCAL_CS_URL, "code-a", null);
		Coding older = new Coding(LOCAL_CS_URL, "code-a", null).setVersion(LOCAL_OLDER_VERSION);
		Coding newer = new Coding(LOCAL_CS_URL, "code-a", null).setVersion("1.0.1");
		Coding otherSystem = new Coding("http://example.org/other", "code-a", null);
		return Stream.of(
				Arguments.of("system, no version", valueSetCodeParams(PINNED_VS_URL, null, LOCAL_CS_URL, null), Expected.VALID),
				Arguments.of(
						"systemVersion in the ValueSet",
						valueSetCodeParams(PINNED_VS_URL, null, LOCAL_CS_URL, LOCAL_OLDER_VERSION),
						Expected.VALID),
				Arguments.of(
						"systemVersion not in the ValueSet",
						valueSetCodeParams(PINNED_VS_URL, null, LOCAL_CS_URL, "1.0.1"),
						Expected.INVALID),
				Arguments.of(
						"version packed into system", valueSetCodeParams(PINNED_VS_URL, null, packed, null), Expected.VALID),
				Arguments.of(
						"packed and systemVersion agree",
						valueSetCodeParams(PINNED_VS_URL, null, packed, LOCAL_OLDER_VERSION),
						Expected.VALID),
				Arguments.of(
						"packed and systemVersion differ",
						valueSetCodeParams(PINNED_VS_URL, null, packed, "1.0.1"),
						Expected.ERROR),
				Arguments.of("coding, no version", valueSetCodingParams(unversioned, null), Expected.VALID),
				Arguments.of("coding version in the ValueSet", valueSetCodingParams(older, null), Expected.VALID),
				Arguments.of("coding version not in the ValueSet", valueSetCodingParams(newer, null), Expected.INVALID),
				Arguments.of(
						"coding version, systemVersion does not apply",
						valueSetCodingParams(older, "1.0.1"),
						Expected.VALID),
				Arguments.of(
						"coding without version, systemVersion does not apply",
						valueSetCodingParams(unversioned, "1.0.1"),
						Expected.VALID),
				Arguments.of(
						"codeableConcept, systemVersion does not apply",
						valueSetCodeableConceptParams(new CodeableConcept(unversioned), "1.0.1"),
						Expected.VALID),
				Arguments.of(
						"codeableConcept coding version in the ValueSet",
						valueSetCodeableConceptParams(new CodeableConcept(older), null),
						Expected.VALID),
				Arguments.of(
						"codeableConcept with a coding from another system",
						valueSetCodeableConceptParams(new CodeableConcept(otherSystem).addCoding(older), null),
						Expected.VALID),
				Arguments.of(
						"codeableConcept coding version not in the ValueSet",
						valueSetCodeableConceptParams(new CodeableConcept(newer), null),
						Expected.INVALID));
	}

	// Created by Claude Opus 5.5
	@Test
	void validateCodeOperationOnValueSetInstance_olderVersion_validatesAgainstThatVersion() {
		IIdType olderVersionId = createLocalValueSetVersions();

		assertValidateCodeOutcome(
				myClient.operation().onInstance(olderVersionId),
				new Parameters().addParameter("code", new CodeType("code-a")).addParameter("system", new UriType(LOCAL_CS_URL)),
				Expected.VALID);
	}

	/**
	 * @return the id of version 1 of the versioned ValueSet
	 */
	private IIdType createLocalValueSetVersions() {
		createLocalCodeSystemVersionsAndValueSet();
		IIdType olderVersionId = createLocalValueSet(VERSIONED_VS_URL, "1", LOCAL_OLDER_VERSION, "code-a");
		createLocalValueSet(VERSIONED_VS_URL, "2", "1.0.1", "code-b");
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		return olderVersionId;
	}

	private IIdType createLocalValueSet(String theUrl, String theVersion, String theCodeSystemVersion, String theCode) {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(theUrl);
		valueSet.setVersion(theVersion);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.getCompose()
				.addInclude()
				.setSystem(LOCAL_CS_URL)
				.setVersion(theCodeSystemVersion)
				.addConcept()
				.setCode(theCode);
		return myValueSetDao.create(valueSet, mySrd).getId().toUnqualifiedVersionless();
	}

	private static Parameters valueSetCodeParams(
			String theUrl, String theValueSetVersion, String theSystem, String theSystemVersion) {
		Parameters retVal = new Parameters().addParameter("url", new UriType(theUrl));
		if (theValueSetVersion != null) {
			retVal.addParameter("valueSetVersion", new StringType(theValueSetVersion));
		}
		retVal.addParameter("code", new CodeType("code-a")).addParameter("system", new UriType(theSystem));
		if (theSystemVersion != null) {
			retVal.addParameter("systemVersion", new StringType(theSystemVersion));
		}
		return retVal;
	}

	private static Parameters valueSetCodingParams(Coding theCoding, String theSystemVersion) {
		Parameters retVal =
				new Parameters().addParameter("url", new UriType(PINNED_VS_URL)).addParameter("coding", theCoding);
		return theSystemVersion == null
				? retVal
				: retVal.addParameter("systemVersion", new StringType(theSystemVersion));
	}

	private static Parameters valueSetCodeableConceptParams(CodeableConcept theCodeableConcept, String theSystemVersion) {
		Parameters retVal = new Parameters()
				.addParameter("url", new UriType(PINNED_VS_URL))
				.addParameter("codeableConcept", theCodeableConcept);
		return theSystemVersion == null
				? retVal
				: retVal.addParameter("systemVersion", new StringType(theSystemVersion));
	}

	enum Expected {
		VALID,
		INVALID,
		ERROR
	}

	private static Parameters codeParams(String theUrl, String theVersion) {
		return withUrlAndVersion(theUrl, theVersion).addParameter("code", new CodeType("code-a"));
	}

	private static Parameters codingParams(String theUrl, String theVersion, Coding theCoding) {
		return withUrlAndVersion(theUrl, theVersion).addParameter("coding", theCoding);
	}

	private static Parameters codeableConceptParams(String theUrl, String theVersion, CodeableConcept theCodeableConcept) {
		return withUrlAndVersion(theUrl, theVersion).addParameter("codeableConcept", theCodeableConcept);
	}

	private static Parameters withUrlAndVersion(String theUrl, String theVersion) {
		Parameters retVal = new Parameters();
		if (theUrl != null) {
			retVal.addParameter("url", new UriType(theUrl));
		}
		return withVersion(retVal, theVersion);
	}

	private static Parameters withVersion(Parameters theParameters, String theVersion) {
		return theVersion == null ? theParameters : theParameters.addParameter("version", new StringType(theVersion));
	}

	private static void assertValidateCodeOutcome(IOperationUnnamed theTarget, Parameters theParameters, Expected theExpected) {
		if (theExpected == Expected.ERROR) {
			assertThatExceptionOfType(InvalidRequestException.class)
					.isThrownBy(() -> validateCode(theTarget, theParameters))
					.withMessageContaining("HAPI-2952");
			return;
		}
		Parameters result = validateCode(theTarget, theParameters);
		assertThat(result.getParameterBool("result")).as(message(result)).isEqualTo(theExpected == Expected.VALID);
	}

	private IIdType createLocalCodeSystemVersionsAndValueSet() {
		// the remote server holds neither, so the locally stored terminology must answer
		myCodeSystemProvider.setShouldThrowExceptionForResourceNotFound(false);
		myValueSetProvider.setShouldThrowExceptionForResourceNotFound(false);
		// 1.0.0 is written first and 1.0.1 second, so 1.0.1 is the current version
		createLocalCodeSystem(LOCAL_OLDER_VERSION, "code-a");
		createLocalCodeSystem("1.0.1", "code-b");
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(PINNED_VS_URL);
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
