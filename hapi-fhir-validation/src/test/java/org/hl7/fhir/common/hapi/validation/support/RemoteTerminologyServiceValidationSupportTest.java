package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoteTerminologyServiceValidationSupportTest {

	private static final String DISPLAY = "DISPLAY";
	private static final String CODE_SYSTEM = "CODE_SYS";
	private static final String CODE = "CODE";
	private static final String VALUE_SET_URL = "http://value.set/url";
	private static final String ERROR_MESSAGE = "This is an error message";
	private static FhirContext ourCtx = FhirContext.forR4();

	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private MyValueSetProvider myValueSetProvider;
	private RemoteTerminologyServiceValidationSupport mySvc;
	private MyCodeSystemProvider myCodeSystemProvider;

	@BeforeEach
	public void before() {
		myValueSetProvider = new MyValueSetProvider();
		myRestfulServerExtension.getRestfulServer().registerProvider(myValueSetProvider);

		myCodeSystemProvider = new MyCodeSystemProvider();
		myRestfulServerExtension.getRestfulServer().registerProvider(myCodeSystemProvider);

		String baseUrl = "http://localhost:" + myRestfulServerExtension.getPort();

		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(false));
	}

	@AfterEach
	public void after() {
		assertThat(myValueSetProvider.myInvocationCount, lessThan(2));
	}

	@Test
	public void testValidateCode_SystemCodeDisplayUrl_BlankCode() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, "", DISPLAY, VALUE_SET_URL);
		assertEquals(null, outcome);
	}

	@Test
	public void testValidateCode_SystemCodeDisplayUrl_Success() {
		createNextValueSetReturnParameters(true, DISPLAY, null);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myValueSetProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myValueSetProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myValueSetProvider.myLastSystem.getValue());
		assertEquals(VALUE_SET_URL, myValueSetProvider.myLastUrl.getValue());
		assertEquals(null, myValueSetProvider.myLastValueSet);
	}

	@Test
	public void testValidateCode_SystemCodeDisplayUrl_Error() {
		createNextValueSetReturnParameters(false, null, ERROR_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertEquals(null, outcome.getCode());
		assertEquals(null, outcome.getDisplay());
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals(ERROR_MESSAGE, outcome.getMessage());

		assertEquals(CODE, myValueSetProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myValueSetProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myValueSetProvider.myLastSystem.getValue());
		assertEquals(VALUE_SET_URL, myValueSetProvider.myLastUrl.getValue());
		assertEquals(null, myValueSetProvider.myLastValueSet);
	}

	@Test
	public void testValidateCodeInCodeSystem_Good() {
		createNextCodeSystemReturnParameters(true, DISPLAY, null);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, null);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myCodeSystemProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myCodeSystemProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());
	}


	@Test
	public void testValidateCodeInValueSet_SystemCodeDisplayVS_Good() {
		createNextValueSetReturnParameters(true, DISPLAY, null);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VALUE_SET_URL);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null, new ConceptValidationOptions(), CODE_SYSTEM, CODE, DISPLAY, valueSet);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
		assertEquals(null, outcome.getSeverity());
		assertEquals(null, outcome.getMessage());

		assertEquals(CODE, myValueSetProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myValueSetProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myValueSetProvider.myLastSystem.getValue());
		assertEquals(VALUE_SET_URL, myValueSetProvider.myLastUrl.getValueAsString());
		assertEquals(null, myValueSetProvider.myLastValueSet);
	}

	/**
	 * Remote terminology services shouldn't be used to validatre codes with an implied system
	 */
	@Test
	public void testValidateCodeInValueSet_InferSystem() {
		createNextValueSetReturnParameters(true, DISPLAY, null);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VALUE_SET_URL);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null, new ConceptValidationOptions().setInferSystem(true), null, CODE, DISPLAY, valueSet);
		assertEquals(null, outcome);
	}

	@Test
	public void testIsValueSetSupported_False() {
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();

		boolean outcome = mySvc.isValueSetSupported(null, "http://loinc.org/VS");
		assertEquals(false, outcome);
		assertEquals("http://loinc.org/VS", myValueSetProvider.myLastUrlParam.getValue());
	}

	@Test
	public void testIsValueSetSupported_True() {
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();
		myValueSetProvider.myNextReturnValueSets.add((ValueSet) new ValueSet().setId("ValueSet/123"));

		boolean outcome = mySvc.isValueSetSupported(null, "http://loinc.org/VS");
		assertEquals(true, outcome);
		assertEquals("http://loinc.org/VS", myValueSetProvider.myLastUrlParam.getValue());
	}

	@Test
	public void testIsCodeSystemSupported_False() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();

		boolean outcome = mySvc.isCodeSystemSupported(null, "http://loinc.org");
		assertEquals(false, outcome);
		assertEquals("http://loinc.org", myCodeSystemProvider.myLastUrlParam.getValue());
	}

	@Test
	public void testIsCodeSystemSupported_True() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myNextReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/123"));

		boolean outcome = mySvc.isCodeSystemSupported(null, "http://loinc.org");
		assertEquals(true, outcome);
		assertEquals("http://loinc.org", myCodeSystemProvider.myLastUrlParam.getValue());
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
