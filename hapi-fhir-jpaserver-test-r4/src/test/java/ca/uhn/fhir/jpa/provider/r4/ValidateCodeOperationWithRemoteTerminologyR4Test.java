package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/*
 * This set of integration tests that instantiates and injects an instance of
 * {@link org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport}
 * into the ValidationSupportChain, which tests the logic of dynamically selecting the correct Remote Terminology
 * implementation. It also exercises the code found in
 * {@link org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport#invokeRemoteValidateCode}
 */
public class ValidateCodeOperationWithRemoteTerminologyR4Test extends BaseResourceProviderR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateCodeOperationWithRemoteTerminologyR4Test.class);
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
	private MyCodeSystemProvider myCodeSystemProvider;
	private MyValueSetProvider myValueSetProvider;

	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;

	@BeforeEach
	public void before() throws Exception {
		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, baseUrl);
		myValidationSupportChain.addValidationSupport(0, mySvc);
		myCodeSystemProvider = new MyCodeSystemProvider();
		myValueSetProvider = new MyValueSetProvider();
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
		myCodeSystemProvider.myReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/v2-0247"));
		myCodeSystemProvider.myReturnParams = new Parameters();
		myCodeSystemProvider.myReturnParams.addParameter("result", true);
		myCodeSystemProvider.myReturnParams.addParameter("display", DISPLAY);

		logAllConcepts();

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "coding", new Coding().setSystem(CODE_SYSTEM_V2_0247_URI).setCode("P"))
			.andParameter("url", new UriType(CODE_SYSTEM_V2_0247_URI))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameterValue("result")).booleanValue());
		assertEquals(DISPLAY, respParam.getParameterValue("display").toString());
	}

	@Test
	public void validateCodeOperationOnCodeSystem_byCodingAndUrlWhereCodeSystemIsUnknown_returnsFalse() {
		myCodeSystemProvider.myReturnCodeSystems = new ArrayList<>();

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
		myCodeSystemProvider.myReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/list-example-use-codes"));
		myValueSetProvider.myReturnValueSets = new ArrayList<>();
		myValueSetProvider.myReturnValueSets.add((ValueSet) new ValueSet().setId("ValueSet/list-example-codes"));
		myValueSetProvider.myReturnParams = new Parameters();
		myValueSetProvider.myReturnParams.addParameter("result", true);
		myValueSetProvider.myReturnParams.addParameter("display", DISPLAY);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "code", new CodeType("alerts"))
			.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/list-example-use-codes"))
			.andParameter("url", new UriType("http://hl7.org/fhir/ValueSet/list-example-codes"))
			.useHttpGet()
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameterValue("result")).booleanValue());
		assertEquals(DISPLAY, respParam.getParameterValue("display").toString());
	}

	@Test
	public void validateCodeOperationOnValueSet_byUrlSystemAndCode() {
		myCodeSystemProvider.myReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/list-example-use-codes"));
		myValueSetProvider.myReturnValueSets = new ArrayList<>();
		myValueSetProvider.myReturnValueSets.add((ValueSet) new ValueSet().setId("ValueSet/list-example-codes"));
		myValueSetProvider.myReturnParams = new Parameters();
		myValueSetProvider.myReturnParams.addParameter("result", true);
		myValueSetProvider.myReturnParams.addParameter("display", DISPLAY_BODY_MASS_INDEX);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named(JpaConstants.OPERATION_VALIDATE_CODE)
			.withParameter(Parameters.class, "code", new CodeType(CODE_BODY_MASS_INDEX))
			.andParameter("url", new UriType("https://loinc.org"))
			.andParameter("system", new UriType("http://loinc.org"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameterValue("result")).booleanValue());
		assertEquals(DISPLAY_BODY_MASS_INDEX, respParam.getParameterValue("display").toString());
	}

	@Test
	public void validateCodeOperationOnValueSet_byCodingAndUrlWhereValueSetIsUnknown_returnsFalse() {
		myValueSetProvider.myReturnValueSets = new ArrayList<>();

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

	@SuppressWarnings("unused")
	private static class MyCodeSystemProvider implements IResourceProvider {
		private List<CodeSystem> myReturnCodeSystems;
		private Parameters myReturnParams;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1),
			@OperationParam(name = "message", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "url", min = 0, max = 1) UriType theCodeSystemUrl,
			@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
			@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay
		) {
			return myReturnParams;
		}

		@Search
		public List<CodeSystem> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			assert myReturnCodeSystems != null;
			return myReturnCodeSystems;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}

	@SuppressWarnings("unused")
	private static class MyValueSetProvider implements IResourceProvider {
		private Parameters myReturnParams;
		private List<ValueSet> myReturnValueSets;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1),
			@OperationParam(name = "message", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class)
		})
		public Parameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "url", min = 0, max = 1) UriType theValueSetUrl,
			@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
			@OperationParam(name = "system", min = 0, max = 1) UriType theSystem,
			@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay,
			@OperationParam(name = "valueSet") ValueSet theValueSet
		) {
			return myReturnParams;
		}

		@Search
		public List<ValueSet> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			assert myReturnValueSets != null;
			return myReturnValueSets;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}

	}
}
