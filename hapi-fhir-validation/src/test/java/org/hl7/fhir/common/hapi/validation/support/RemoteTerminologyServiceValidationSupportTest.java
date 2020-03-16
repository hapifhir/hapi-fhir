package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerRule;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class RemoteTerminologyServiceValidationSupportTest {

	private static final String DISPLAY = "DISPLAY";
	private static final String CODE_SYSTEM = "CODE_SYS";
	private static final String CODE = "CODE";
	private static final String VALUE_SET_URL = "http://value.set/url";
	private static final String ERROR_MESSAGE = "This is an error message";
	private static FhirContext ourCtx = FhirContext.forR4();

	@Rule
	public RestfulServerRule myRestfulServerRule = new RestfulServerRule(ourCtx);

	private MyMockTerminologyServiceProvider myProvider;
	private RemoteTerminologyServiceValidationSupport mySvc;

	@Before
	public void before() {
		myProvider = new MyMockTerminologyServiceProvider();
		myRestfulServerRule.getRestfulServer().registerProvider(myProvider);
		String baseUrl = "http://localhost:" + myRestfulServerRule.getPort();

		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(false));
	}

	@After
	public void after() {
		assertThat(myProvider.myInvocationCount, lessThan(2));
	}

	@Test
	public void testValidateCode_SystemCodeDisplayUrl_BlankCode() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, "", DISPLAY, VALUE_SET_URL);
		assertEquals(null, outcome);
	}

	@Test
	public void testValidateCode_SystemCodeDisplayUrl_Success() {
		createNextReturnParameters(true, DISPLAY, null);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myProvider.myLastSystem.getValue());
		assertEquals(VALUE_SET_URL, myProvider.myLastUrl.getValue());
		assertEquals(null, myProvider.myLastValueSet);
	}

	@Test
	public void testValidateCode_SystemCodeDisplayUrl_Error() {
		createNextReturnParameters(false, null, ERROR_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertEquals(null, outcome.getCode());
		assertEquals(null, outcome.getDisplay());
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals(ERROR_MESSAGE, outcome.getMessage());

		assertEquals(CODE, myProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myProvider.myLastSystem.getValue());
		assertEquals(VALUE_SET_URL, myProvider.myLastUrl.getValue());
		assertEquals(null, myProvider.myLastValueSet);
	}

	@Test
	public void testValidateCodeInValueSet_SystemCodeDisplayVS_Good() {
		createNextReturnParameters(true, DISPLAY, null);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VALUE_SET_URL);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null, null, CODE_SYSTEM, CODE, DISPLAY, valueSet);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myProvider.myLastSystem.getValue());
		assertEquals(null, myProvider.myLastUrl);
		assertEquals(VALUE_SET_URL, myProvider.myLastValueSet.getUrl());
	}

	public void createNextReturnParameters(boolean theResult, String theDisplay, String theMessage) {
		myProvider.myNextReturn = new Parameters();
		myProvider.myNextReturn.addParameter("result", theResult);
		myProvider.myNextReturn.addParameter("display", theDisplay);
		if (theMessage != null) {
			myProvider.myNextReturn.addParameter("message", theMessage);
		}
	}

	private static class MyMockTerminologyServiceProvider {


		private Parameters myNextReturn;
		private UriType myLastUrl;
		private CodeType myLastCode;
		private int myInvocationCount;
		private UriType myLastSystem;
		private StringType myLastDisplay;
		private ValueSet myLastValueSet;

		@Operation(name = "validate-code", idempotent = true, typeName = "ValueSet", returnParameters = {
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
			return myNextReturn;

		}


	}

}
