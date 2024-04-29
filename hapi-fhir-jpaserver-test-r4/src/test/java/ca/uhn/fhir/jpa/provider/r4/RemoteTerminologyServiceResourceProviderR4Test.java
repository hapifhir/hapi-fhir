package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/*
 * This set of Unit Tests simulates the call to a remote server and therefore, only tests the code in the
 * {@link org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport#invokeRemoteValidateCode}
 * method, before and after it makes that remote client call.
 */
public class RemoteTerminologyServiceResourceProviderR4Test {
	private static final String DISPLAY = "DISPLAY";
	private static final String CODE_SYSTEM = "CODE_SYS";
	private static final String CODE = "CODE";
	private static final String VALUE_SET_URL = "http://value.set/url";
	private static final String SAMPLE_MESSAGE = "This is a sample message";
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
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

		assertEquals(CODE, ourCodeSystemProvider.myLastCode.getCode());
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

		assertEquals(CODE, ourCodeSystemProvider.myLastCode.getCode());
		assertEquals(DISPLAY, ourCodeSystemProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, ourCodeSystemProvider.myLastUrl.getValueAsString());
		assertEquals(SAMPLE_MESSAGE, ourCodeSystemProvider.myNextReturnParams.getParameterValue("message").toString());
	}

	@Test
	public void testValidateCodeInCodeSystem_AssumeFailure_ReturnsFailureCodeAndFailureMessage() {
		createNextCodeSystemReturnParameters(false, null, SAMPLE_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, null, null);
		assertNotNull(outcome);
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals(SAMPLE_MESSAGE, outcome.getMessage());

        assertFalse(((BooleanType) ourCodeSystemProvider.myNextReturnParams.getParameterValue("result")).booleanValue());
	}

	@Test
	public void testValidateCodeInValueSet_ProvidingMinimalInputs_ReturnsSuccess() {
		ourValueSetProvider.myNextReturnParams = new Parameters().addParameter("result", true);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, null, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());

		assertEquals(CODE, ourValueSetProvider.myLastCode.getCode());
		assertEquals(VALUE_SET_URL, ourValueSetProvider.myLastUrl.getValueAsString());
	}

	@Test
	public void testValidateCodeInValueSet_WithMessageValue_ReturnsMessage() {
		ourValueSetProvider.myNextReturnParams = new Parameters().addParameter("result", true)
			.addParameter("display", DISPLAY)
			.addParameter("message", SAMPLE_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
        assertNull(outcome.getSeverity());
		assertNull(outcome.getMessage());

		assertEquals(CODE, ourValueSetProvider.myLastCode.getCode());
		assertEquals(DISPLAY, ourValueSetProvider.myLastDisplay.getValue());
		assertEquals(VALUE_SET_URL, ourValueSetProvider.myLastUrl.getValueAsString());
		assertEquals(SAMPLE_MESSAGE, ourValueSetProvider.myNextReturnParams.getParameterValue("message").toString());
	}

	private void createNextCodeSystemReturnParameters(boolean theResult, String theDisplay, String theMessage) {
		ourCodeSystemProvider.myNextReturnParams = new Parameters();
		ourCodeSystemProvider.myNextReturnParams.addParameter("result", theResult);
		ourCodeSystemProvider.myNextReturnParams.addParameter("display", theDisplay);
		if (theMessage != null) {
			ourCodeSystemProvider.myNextReturnParams.addParameter("message", theMessage);
		}
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
