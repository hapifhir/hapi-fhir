package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
	private static FhirContext ourCtx = FhirContext.forR4();
	private MyCodeSystemProvider myCodeSystemProvider = new MyCodeSystemProvider();
	private MyValueSetProvider myValueSetProvider = new MyValueSetProvider();

	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx, myCodeSystemProvider,
		myValueSetProvider);

	private RemoteTerminologyServiceValidationSupport mySvc;

	@BeforeEach
	public void before_ConfigureService() {
		String myBaseUrl = "http://localhost:" + myRestfulServerExtension.getPort();
		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx, myBaseUrl);
	}

	@AfterEach
	public void after_UnregisterProviders() {
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		myRestfulServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
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
		assertEquals(CODE, outcome.getCode());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myCodeSystemProvider.myLastCode.getCode());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());
	}

	@Test
	public void testValidateCodeInCodeSystem_WithMessageValue_ReturnsMessage() {
		createNextCodeSystemReturnParameters(true, DISPLAY, SAMPLE_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, null);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myCodeSystemProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myCodeSystemProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());
		assertEquals(SAMPLE_MESSAGE, myCodeSystemProvider.myNextReturnParams.getParameter("message").toString());
	}

	@Test
	public void testValidateCodeInCodeSystem_AssumeFailure_ReturnsFailureCodeAndFailureMessage() {
		createNextCodeSystemReturnParameters(false, null, SAMPLE_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, null, null);
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals(SAMPLE_MESSAGE, outcome.getMessage());

		assertEquals(false, ((BooleanType)myCodeSystemProvider.myNextReturnParams.getParameter("result")).booleanValue());
	}

	@Test
	public void testValidateCodeInValueSet_ProvidingMinimalInputs_ReturnsSuccess() {
		createNextValueSetReturnParameters(true, null, null);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, null, VALUE_SET_URL);
		assertEquals(CODE, outcome.getCode());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myValueSetProvider.myLastCode.getCode());
		assertEquals(VALUE_SET_URL, myValueSetProvider.myLastUrl.getValueAsString());
	}

	@Test
	public void testValidateCodeInValueSet_WithMessageValue_ReturnsMessage() {
		createNextValueSetReturnParameters(true, DISPLAY, SAMPLE_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc
			.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myValueSetProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myValueSetProvider.myLastDisplay.getValue());
		assertEquals(VALUE_SET_URL, myValueSetProvider.myLastUrl.getValueAsString());
		assertEquals(SAMPLE_MESSAGE, myValueSetProvider.myNextReturnParams.getParameter("message").toString());
	}

	private void createNextCodeSystemReturnParameters(boolean theResult, String theDisplay, String theMessage) {
		myCodeSystemProvider.myNextReturnParams = new Parameters();
		myCodeSystemProvider.myNextReturnParams.addParameter("result", theResult);
		myCodeSystemProvider.myNextReturnParams.addParameter("display", theDisplay);
		if (theMessage != null) {
			myCodeSystemProvider.myNextReturnParams.addParameter("message", theMessage);
		}
	}

	private void createNextValueSetReturnParameters(boolean theResult, String theDisplay, String theMessage) {
		myValueSetProvider.myNextReturnParams = new Parameters();
		myValueSetProvider.myNextReturnParams.addParameter("result", theResult);
		myValueSetProvider.myNextReturnParams.addParameter("display", theDisplay);
		if (theMessage != null) {
			myValueSetProvider.myNextReturnParams.addParameter("message", theMessage);
		}
	}

	private static class MyCodeSystemProvider implements IResourceProvider {
		private UriParam myLastUrlParam;
		private List<CodeSystem> myNextReturnCodeSystems;
		private int myInvocationCount;
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
			myInvocationCount++;
			myLastUrl = theCodeSystemUrl;
			myLastCode = theCode;
			myLastDisplay = theDisplay;
			return myNextReturnParams;

		}

		@Search
		public List<CodeSystem> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			myLastUrlParam = theUrlParam;
			assert myNextReturnCodeSystems != null;
			return myNextReturnCodeSystems;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}
	}


	private static class MyValueSetProvider implements IResourceProvider {
		private Parameters myNextReturnParams;
		private List<ValueSet> myNextReturnValueSets;
		private UriType myLastUrl;
		private CodeType myLastCode;
		private int myInvocationCount;
		private UriType myLastSystem;
		private StringType myLastDisplay;
		private ValueSet myLastValueSet;
		private UriParam myLastUrlParam;

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
			myInvocationCount++;
			myLastUrl = theValueSetUrl;
			myLastCode = theCode;
			myLastSystem = theSystem;
			myLastDisplay = theDisplay;
			myLastValueSet = theValueSet;
			return myNextReturnParams;
		}

		@Search
		public List<ValueSet> find(@RequiredParam(name = "url") UriParam theUrlParam) {
			myLastUrlParam = theUrlParam;
			assert myNextReturnValueSets != null;
			return myNextReturnValueSets;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}

	}
}
