package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * This set of Unit Tests simulates the call to a remote server and therefore, only tests the code in the
 * {@link org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport#invokeRemoteValidateCode}
 * method, before and after it makes that remote client call.
 */
public class RemoteTerminologyServiceResourceProviderDstu3Test {
	private static final String DISPLAY = "DISPLAY";
	private static final String CODE_SYSTEM = "CODE_SYS";
	private static final String CODE = "CODE";
	private static final String VALUE_SET_URL = "http://value.set/url";
	private static final String SAMPLE_MESSAGE = "This is a sample message";
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final MyCodeSystemProvider ourCodeSystemProvider = new MyCodeSystemProvider();
	private static final MyValueSetProvider ourValueSetProvider = new MyValueSetProvider();

	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx, ourCodeSystemProvider,
		ourValueSetProvider);

	private RemoteTerminologyServiceValidationSupport mySvc;

	@BeforeEach
	public void before_ConfigureService() {
		String myBaseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, myBaseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(false).setLogRequestSummary(true).setLogResponseSummary(true));
	}

	@AfterEach
	public void after_UnregisterProviders() {
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		ourRestfulServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testValidateCodeInCodeSystem_BlankCode_ReturnsNull() {
		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, null, DISPLAY, null);
		assertNull(outcome);
	}

	@Test
	public void testValidateCodeInCodeSystem_ProvidingMinimalInputs_ReturnsSuccess() {
		createNextCodeSystemReturnParameters(true, null, null);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, null, null);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());

		assertEquals(CODE, ourCodeSystemProvider.myLastCode.getValue());
		assertEquals(CODE_SYSTEM, ourCodeSystemProvider.myLastUrl.getValueAsString());
	}

	@Test
	public void testValidateCodeInCodeSystem_WithMessageValue_ReturnsMessage() {
		createNextCodeSystemReturnParameters(true, DISPLAY, SAMPLE_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, null);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());

		assertEquals(CODE, ourCodeSystemProvider.myLastCode.getValue());
		assertEquals(DISPLAY, ourCodeSystemProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, ourCodeSystemProvider.myLastUrl.getValueAsString());
		assertEquals(SAMPLE_MESSAGE, getParameterValue(ourCodeSystemProvider.myNextReturnParams, "message").toString());
	}

	@Test
	public void testValidateCodeInCodeSystem_AssumeFailure_ReturnsFailureCodeAndFailureMessage() {
		createNextCodeSystemReturnParameters(false, null, SAMPLE_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, null, null);
		assertNotNull(outcome);
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals(SAMPLE_MESSAGE, outcome.getMessage());

		assertFalse(((BooleanType) getParameterValue(ourCodeSystemProvider.myNextReturnParams, "result")).booleanValue());
	}

	@Test
	public void testValidateCodeInValueSet_ProvidingMinimalInputs_ReturnsSuccess() {
		ourValueSetProvider.myNextReturnParams = new Parameters();
		ourValueSetProvider.myNextReturnParams.addParameter().setName("result").setValue(new BooleanType(true));

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, null, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());

		assertEquals(CODE, ourValueSetProvider.myLastCode.getValue());
		assertEquals(VALUE_SET_URL, ourValueSetProvider.myLastUrl.getValueAsString());
	}

	@Test
	public void testValidateCodeInValueSet_WithMessageValue_ReturnsMessage() {
		ourValueSetProvider.myNextReturnParams = new Parameters();
		ourValueSetProvider.myNextReturnParams.addParameter().setName("result").setValue(new BooleanType(true));
		ourValueSetProvider.myNextReturnParams.addParameter().setName("display").setValue(new StringType(DISPLAY));
		ourValueSetProvider.myNextReturnParams.addParameter().setName("message").setValue(new StringType(SAMPLE_MESSAGE));

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());

		assertEquals(CODE, ourValueSetProvider.myLastCode.getValue());
		assertEquals(DISPLAY, ourValueSetProvider.myLastDisplay.getValue());
		assertEquals(VALUE_SET_URL, ourValueSetProvider.myLastUrl.getValueAsString());
		assertEquals(SAMPLE_MESSAGE, getParameterValue(ourValueSetProvider.myNextReturnParams, "message").toString());
	}

	@Test
	public void lookupCode_withParametersOutput_convertsCorrectly() {
		String paramsAsString = ClasspathUtil.loadResource("/r4/CodeSystem-lookup-output-with-subproperties.json");
		IBaseResource baseResource = ourCtx.newJsonParser().parseResource(paramsAsString);
		assertTrue(baseResource instanceof Parameters);
		Parameters resultParameters = (Parameters) baseResource;
		ourCodeSystemProvider.myNextReturnParams = resultParameters;

		LookupCodeRequest request = new LookupCodeRequest(CODE_SYSTEM, CODE);

		// test
		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(null, request);
		assertNotNull(outcome);

		IBaseParameters theActualParameters = outcome.toParameters(ourCtx, request.getPropertyNames().stream().map(StringType::new).toList());
		String actual = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(theActualParameters);
		String expected = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resultParameters);

		assertEquals(expected, actual);
	}

	private void createNextCodeSystemReturnParameters(boolean theResult, String theDisplay, String theMessage) {
		ourCodeSystemProvider.myNextReturnParams = new Parameters();
		ourCodeSystemProvider.myNextReturnParams.addParameter().setName("result").setValue(new BooleanType(theResult));
		ourCodeSystemProvider.myNextReturnParams.addParameter().setName("display").setValue(new StringType(theDisplay));
		if (theMessage != null) {
			ourCodeSystemProvider.myNextReturnParams.addParameter().setName("message").setValue(new StringType(theMessage));
		}
	}

	private Type getParameterValue(Parameters theParameters, String theParameterName) {
		Optional<Parameters.ParametersParameterComponent> paramOpt = theParameters.getParameter()
				.stream().filter(param -> param.getName().equals(theParameterName)).findFirst();
		assertTrue(paramOpt.isPresent());
		return paramOpt.get().getValue();
	}

	private static class MyCodeSystemProvider implements IResourceProvider {
		private UriType myLastUrl;
		private CodeType myLastCode;
		private StringType myLastDisplay;
		private Parameters myNextReturnParams;

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
			myLastUrl = theCodeSystemUrl;
			myLastCode = theCode;
			myLastDisplay = theDisplay;
			return myNextReturnParams;

		}

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
				@OperationParam(name = "name", type = StringType.class, min = 1),
				@OperationParam(name = "version", type = StringType.class),
				@OperationParam(name = "display", type = StringType.class, min = 1),
				@OperationParam(name = "abstract", type = BooleanType.class, min = 1),
				@OperationParam(name = "property", type = StringType.class, min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public IBaseParameters lookup(
				HttpServletRequest theServletRequest,
				@OperationParam(name = "code", max = 1) CodeType theCode,
				@OperationParam(name = "system",max = 1) UriType theSystem,
				@OperationParam(name = "coding", max = 1) Coding theCoding,
				@OperationParam(name = "version", max = 1) StringType theVersion,
				@OperationParam(name = "displayLanguage", max = 1) CodeType theDisplayLanguage,
				@OperationParam(name = "property", max = OperationParam.MAX_UNLIMITED) List<CodeType> thePropertyNames,
				RequestDetails theRequestDetails
		) {
			myLastCode = theCode;
			return myNextReturnParams;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}


	private static class MyValueSetProvider implements IResourceProvider {
		private Parameters myNextReturnParams;
		private UriType myLastUrl;
		private CodeType myLastCode;
		private StringType myLastDisplay;

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
			myLastUrl = theValueSetUrl;
			myLastCode = theCode;
			myLastDisplay = theDisplay;
			return myNextReturnParams;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}

	}
}
