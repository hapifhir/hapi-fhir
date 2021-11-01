package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.collect.Lists;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoteTerminologyServiceValidationSupportTest {

	private static final String DISPLAY = "DISPLAY";
	private static final String CODE_SYSTEM = "CODE_SYS";
	private static final String CODE_SYSTEM_NAME = "CODE SYSTEM NAME";
	private static final String CODE_SYSTEM_VERSION = "v1.1";
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
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
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
	public void testLookupOperation_CodeSystem_Success() {
		createNextCodeSystemLookupReturnParameters(true, CODE_SYSTEM_VERSION, DISPLAY, null);

		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(null, CODE_SYSTEM, CODE);
		assertNotNull(outcome, "Call to lookupCode() should return a non-NULL result!");
		assertEquals(DISPLAY, outcome.getCodeDisplay());
		assertEquals(CODE_SYSTEM_VERSION, outcome.getCodeSystemVersion());

		assertEquals(CODE, myCodeSystemProvider.myLastCode.getCode());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());
		assertTrue(Boolean.parseBoolean(myCodeSystemProvider.myNextReturnParams.getParameter("result").primitiveValue()));
	}

	@Test
	public void testLookupCode_BlankCode_ThrowsException() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(null, CODE_SYSTEM,
				"", null);
		});
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
	 * Remote terminology services shouldn't be used to validate codes with an implied system
	 */
	@Test
	public void testValidateCodeInValueSet_InferSystem() {
		createNextValueSetReturnParameters(true, DISPLAY, null);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VALUE_SET_URL);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null, new ConceptValidationOptions().setInferSystem(true), null, CODE, DISPLAY, valueSet);
		assertEquals(null, outcome);
	}

	/**
	 * Remote terminology services can be used to validate codes when code system is present,
	 * even when inferSystem is true
	 */
	@Nested
	public class ExtractCodeSystemFromValueSet {

		@Test
		public void testUniqueComposeInclude() {
			createNextValueSetReturnParameters(true, DISPLAY, null);

			ValueSet valueSet = new ValueSet();
			valueSet.setUrl(VALUE_SET_URL);
			String systemUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
			valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
				Collections.singletonList(new ValueSet.ConceptSetComponent().setSystem(systemUrl)) ));

			IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
				new ConceptValidationOptions().setInferSystem(true), null, CODE, DISPLAY, valueSet);

			// validate service doesn't do early return (as when no code system is present)
			assertNotNull(outcome);
		}


		@Nested
		public class MultiComposeIncludeValueSet {

			@Test
			public void SystemNotPresentReturnsNull() {
				createNextValueSetReturnParameters(true, DISPLAY, null);

				ValueSet valueSet = new ValueSet();
				valueSet.setUrl(VALUE_SET_URL);
				valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
					Lists.newArrayList(new ValueSet.ConceptSetComponent(), new ValueSet.ConceptSetComponent()) ));

				IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
					new ConceptValidationOptions().setInferSystem(true), null, CODE, DISPLAY, valueSet);

				assertNull(outcome);
			}


			@Test
			public void SystemPresentCodeNotPresentReturnsNull() {
				createNextValueSetReturnParameters(true, DISPLAY, null);

				ValueSet valueSet = new ValueSet();
				valueSet.setUrl(VALUE_SET_URL);
				String systemUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
				String systemUrl2 = "http://hl7.org/fhir/ValueSet/other-valueset";
				valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
					Lists.newArrayList(
						new ValueSet.ConceptSetComponent().setSystem(systemUrl),
						new ValueSet.ConceptSetComponent().setSystem(systemUrl2)) ));

				IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
					new ConceptValidationOptions().setInferSystem(true), null, CODE, DISPLAY, valueSet);

				assertNull(outcome);
			}


			@Test
			public void SystemPresentCodePresentValidatesOKNoVersioned() {
				createNextValueSetReturnParameters(true, DISPLAY, null);

				ValueSet valueSet = new ValueSet();
				valueSet.setUrl(VALUE_SET_URL);
				String systemUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
				String systemUrl2 = "http://hl7.org/fhir/ValueSet/other-valueset";
				valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
					Lists.newArrayList(
						new ValueSet.ConceptSetComponent().setSystem(systemUrl),
						new ValueSet.ConceptSetComponent().setSystem(systemUrl2).setConcept(
							Lists.newArrayList(
								new ValueSet.ConceptReferenceComponent().setCode("not-the-code"),
								new ValueSet.ConceptReferenceComponent().setCode(CODE) )
						)) ));

				TestClientInterceptor requestInterceptor = new TestClientInterceptor();
				mySvc.addClientInterceptor(requestInterceptor);

				IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
					new ConceptValidationOptions().setInferSystem(true), null, CODE, DISPLAY, valueSet);

				assertNotNull(outcome);
				assertEquals(systemUrl2, requestInterceptor.getCapturedSystemParameter());
			}


			@Test
			public void SystemPresentCodePresentValidatesOKVersioned() {
				createNextValueSetReturnParameters(true, DISPLAY, null);

				ValueSet valueSet = new ValueSet();
				valueSet.setUrl(VALUE_SET_URL);
				String systemUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
				String systemVersion = "3.0.2";
				String systemUrl2 = "http://hl7.org/fhir/ValueSet/other-valueset";
				String system2Version = "4.0.1";
				valueSet.setCompose(new ValueSet.ValueSetComposeComponent().setInclude(
					Lists.newArrayList(
						new ValueSet.ConceptSetComponent().setSystem(systemUrl).setVersion(systemVersion),
						new ValueSet.ConceptSetComponent().setSystem(systemUrl2).setVersion(system2Version).setConcept(
							Lists.newArrayList(
								new ValueSet.ConceptReferenceComponent().setCode("not-the-code"),
								new ValueSet.ConceptReferenceComponent().setCode(CODE) )
						)) ));

				TestClientInterceptor requestInterceptor = new TestClientInterceptor();
				mySvc.addClientInterceptor(requestInterceptor);

				IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null,
					new ConceptValidationOptions().setInferSystem(true), null, CODE, DISPLAY, valueSet);

				assertNotNull(outcome);
				assertEquals(systemUrl2 + "|" + system2Version, requestInterceptor.getCapturedSystemParameter());
			}


		}

		/**
		 * Captures the system parameter of the request
		 */
		private class TestClientInterceptor implements IClientInterceptor {

			private String capturedSystemParameter;

			@Override
			public void interceptRequest(IHttpRequest theRequest) {
				try {
					String content = theRequest.getRequestBodyFromStream();
					if (content != null) {
						IJsonLikeParser parser = (IJsonLikeParser) ourCtx.newJsonParser();
						Parameters params = parser.parseResource(Parameters.class, content);
						List<String> systemValues = ParametersUtil.getNamedParameterValuesAsString(
							ourCtx, params, "system");
						assertEquals(1, systemValues.size());
						capturedSystemParameter = systemValues.get(0);
					}
				} catch (IOException theE) {
					theE.printStackTrace();
				}
			}

			@Override
			public void interceptResponse(IHttpResponse theResponse) throws IOException { }

			public String getCapturedSystemParameter() { return capturedSystemParameter; }
		}
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

	private void createNextCodeSystemLookupReturnParameters(boolean theResult, String theVersion, String theDisplay,
																			  String theMessage) {
		myCodeSystemProvider.myNextReturnParams = new Parameters();
		myCodeSystemProvider.myNextReturnParams.addParameter("result", theResult);
		myCodeSystemProvider.myNextReturnParams.addParameter("version", theVersion);
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
		private Coding myLastCoding;
		private StringType myLastVersion;
		private CodeType myLastDisplayLanguage;
		private StringType myLastDisplay;
		private Parameters myNextReturnParams;
		private IValidationSupport.LookupCodeResult myNextLookupCodeResult;

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

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
			@OperationParam(name="name", type=StringType.class, min=1),
			@OperationParam(name="version", type=StringType.class, min=0),
			@OperationParam(name="display", type=StringType.class, min=1),
			@OperationParam(name="abstract", type=BooleanType.class, min=1),
		})
		public Parameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="code", min=0, max=1) CodeType theCode,
			@OperationParam(name="system", min=0, max=1) UriType theSystem,
			@OperationParam(name="coding", min=0, max=1) Coding theCoding,
			@OperationParam(name="version", min=0, max=1) StringType theVersion,
			@OperationParam(name="displayLanguage", min=0, max=1) CodeType theDisplayLanguage,
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED) List<CodeType> theProperties,
			RequestDetails theRequestDetails
		) {
			myInvocationCount++;
			myLastCode = theCode;
			myLastUrl = theSystem;
			myLastCoding = theCoding;
			myLastVersion = theVersion;
			myLastDisplayLanguage = theDisplayLanguage;
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
