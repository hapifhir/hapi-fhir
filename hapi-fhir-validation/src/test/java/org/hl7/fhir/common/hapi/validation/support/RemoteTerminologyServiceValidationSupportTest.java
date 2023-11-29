package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.SummaryEnum;
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
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoteTerminologyServiceValidationSupportTest {
	private static final String DISPLAY = "DISPLAY";
	private static final String LANGUAGE = "en";
	private static final String CODE_SYSTEM = "CODE_SYS";
	private static final String CODE_SYSTEM_NAME = "Code System";
	private static final String CODE = "CODE";
	private static final String VALUE_SET_URL = "http://value.set/url";
	private static final String TARGET_SYSTEM = "http://target.system/url";
	private static final String CONCEPT_MAP_URL = "http://concept.map/url";
	private static final String CONCEPT_MAP_VERSION = "2.1";
	private static final String SOURCE_VALUE_SET_URL = "http://source.vs.system/url";
	private static final String TARGET_VALUE_SET_URL = "http://target.vs.system/url";
	private static final String TARGET_CODE = "CODE";
	private static final String TARGET_CODE_DISPLAY = "code";
	private static final boolean REVERSE = true;
	private static final String EQUIVALENCE_CODE = "equivalent";

	private static final String ERROR_MESSAGE = "This is an error message";
	private static final String SUCCESS_MESSAGE = "This is a success message";

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public static RestfulServerExtension ourRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private MyValueSetProvider myValueSetProvider;
	private RemoteTerminologyServiceValidationSupport mySvc;
	private MyCodeSystemProvider myCodeSystemProvider;
	private MyConceptMapProvider myConceptMapProvider;

	@BeforeEach
	public void before() {
		myValueSetProvider = new MyValueSetProvider();
		ourRestfulServerExtension.getRestfulServer().registerProvider(myValueSetProvider);

		myCodeSystemProvider = new MyCodeSystemProvider();
		ourRestfulServerExtension.getRestfulServer().registerProvider(myCodeSystemProvider);

		myConceptMapProvider = new MyConceptMapProvider();
		ourRestfulServerExtension.getRestfulServer().registerProvider(myConceptMapProvider);

		String baseUrl = "http://localhost:" + ourRestfulServerExtension.getPort();

		mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
	}

	@AfterEach
	public void after() {
		assertThat(myValueSetProvider.myInvocationCount, lessThan(2));
	}

	@Test
	public void testValidateCode_withBlankCode_returnsNull() {
		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, "", DISPLAY, VALUE_SET_URL);
      assertNull(outcome);
	}


	@Test
	public void testLookupCode_forCodeSystemWithAllParams_returnsCorrectParameters() {
		myCodeSystemProvider.myNextLookupCodeResult = new IValidationSupport.LookupCodeResult();
		myCodeSystemProvider.myNextLookupCodeResult.setFound(true);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeSystemVersion(CODE_SYSTEM);
		myCodeSystemProvider.myNextLookupCodeResult.setSearchedForCode(CODE);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeSystemDisplayName(CODE_SYSTEM_NAME);
		myCodeSystemProvider.myNextLookupCodeResult.setCodeDisplay(DISPLAY);

		// property
		String propertyName = "birthDate";
		String propertyValue = "1930-01-01";
		IValidationSupport.BaseConceptProperty property = new IValidationSupport.StringConceptProperty(propertyName, propertyValue);
		myCodeSystemProvider.myNextLookupCodeResult.getProperties().add(property);

		// designation
		IValidationSupport.ConceptDesignation designation = new IValidationSupport.ConceptDesignation();
		designation.setLanguage("en");
		designation.setUseCode("code");
		designation.setUseSystem("system");
		designation.setUseDisplay("display");
		designation.setValue("some value");
		myCodeSystemProvider.myNextLookupCodeResult.getDesignations().add(designation);

		IValidationSupport.LookupCodeResult outcome = mySvc.lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, CODE, null, Set.of("birthDate")));
		assertNotNull(outcome, "Call to lookupCode() should return a non-NULL result!");
		assertEquals(DISPLAY, outcome.getCodeDisplay());
		assertEquals(CODE_SYSTEM, outcome.getCodeSystemVersion());
		assertEquals(CODE_SYSTEM_NAME, myCodeSystemProvider.myNextReturnParams.getParameterValue("name").toString());

		assertEquals(CODE, myCodeSystemProvider.myLastCode.getCode());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());

		Parameters.ParametersParameterComponent propertyComponent = myCodeSystemProvider.myNextReturnParams.getParameter("property");
		assertNotNull(propertyComponent);

		Iterator<Parameters.ParametersParameterComponent> propertyComponentIterator = propertyComponent.getPart().iterator();
		propertyComponent = propertyComponentIterator.next();
		assertEquals("code", propertyComponent.getName());
		assertEquals(propertyName, ((StringType)propertyComponent.getValue()).getValue());

		propertyComponent = propertyComponentIterator.next();
		assertEquals("value", propertyComponent.getName());
		assertEquals(propertyValue, ((StringType)propertyComponent.getValue()).getValue());

		Parameters.ParametersParameterComponent designationComponent = myCodeSystemProvider.myNextReturnParams.getParameter("designation");
		Iterator<Parameters.ParametersParameterComponent> partParameter = designationComponent.getPart().iterator();
		designationComponent = partParameter.next();
		assertEquals("language", designationComponent.getName());
		assertEquals(LANGUAGE, designationComponent.getValue().toString());

		designationComponent = partParameter.next();
		assertEquals("use", designationComponent.getName());
		Coding coding = (Coding)designationComponent.getValue();
		assertNotNull(coding, "Coding value returned via designation use should NOT be NULL!");
		assertEquals("code", coding.getCode());
		assertEquals("system", coding.getSystem());
		assertEquals("display", coding.getDisplay());

		designationComponent = partParameter.next();
		assertEquals("value", designationComponent.getName());
		assertEquals("some value", designationComponent.getValue().toString());
	}

	@Test
	public void testLookupCode_forCodeSystemWithBlankCode_throwsException() {
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> mySvc.lookupCode(null, new LookupCodeRequest(CODE_SYSTEM, "")));
	}

	@Test
	public void testValidateCode_forValueSet_returnsCorrectly() {
		createNextValueSetReturnParameters(true, DISPLAY, null);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
      assertNull(outcome.getSeverity());
      assertNull(outcome.getMessage());

		assertEquals(CODE, myValueSetProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myValueSetProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myValueSetProvider.myLastSystem.getValue());
		assertEquals(VALUE_SET_URL, myValueSetProvider.myLastUrl.getValue());
      assertNull(myValueSetProvider.myLastValueSet);
	}

	@Test
	void testFetchValueSet_forcesSummaryFalse() {
	    // given
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();

		// when
		mySvc.fetchValueSet(VALUE_SET_URL);

	    // then
	    assertEquals(SummaryEnum.FALSE, myValueSetProvider.myLastSummaryParam);
	}

	@Test
	public void testValidateCode_forSystemCodeWithError_returnsCorrectly() {
		createNextValueSetReturnParameters(false, null, ERROR_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, VALUE_SET_URL);
		assertNotNull(outcome);
      assertNull(outcome.getCode());
      assertNull(outcome.getDisplay());
		assertEquals(IValidationSupport.IssueSeverity.ERROR, outcome.getSeverity());
		assertEquals(ERROR_MESSAGE, outcome.getMessage());

		assertEquals(CODE, myValueSetProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myValueSetProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myValueSetProvider.myLastSystem.getValue());
		assertEquals(VALUE_SET_URL, myValueSetProvider.myLastUrl.getValue());
      assertNull(myValueSetProvider.myLastValueSet);
	}

	@Test
	public void testValidateCode_forCodeSystem_returnsCorrectly() {
		myCodeSystemProvider.myNextValidationResult = new IValidationSupport.CodeValidationResult();
		myCodeSystemProvider.myNextValidationResult.setCodeSystemVersion(CODE_SYSTEM);
		myCodeSystemProvider.myNextValidationResult.setCode(CODE);
		myCodeSystemProvider.myNextValidationResult.setCodeSystemName(CODE_SYSTEM_NAME);
		myCodeSystemProvider.myNextValidationResult.setDisplay(DISPLAY);
		myCodeSystemProvider.myNextValidationResult.setMessage(SUCCESS_MESSAGE);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(null, null, CODE_SYSTEM, CODE, DISPLAY, null);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
      assertNull(outcome.getSeverity());
      assertNull(outcome.getMessage());

		assertEquals(CODE, myCodeSystemProvider.myLastCode.getCode());
		assertEquals(CODE_SYSTEM, myCodeSystemProvider.myLastUrl.getValueAsString());
	}


	@Test
	public void testValidateCodeInValueSet_SystemCodeDisplayVS_Good() {
		createNextValueSetReturnParameters(true, DISPLAY, null);

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(VALUE_SET_URL);

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCodeInValueSet(null, new ConceptValidationOptions(), CODE_SYSTEM, CODE, DISPLAY, valueSet);
		assertNotNull(outcome);
		assertEquals(CODE, outcome.getCode());
		assertEquals(DISPLAY, outcome.getDisplay());
      assertNull(outcome.getSeverity());
      assertNull(outcome.getMessage());

		assertEquals(CODE, myValueSetProvider.myLastCode.getCode());
		assertEquals(DISPLAY, myValueSetProvider.myLastDisplay.getValue());
		assertEquals(CODE_SYSTEM, myValueSetProvider.myLastSystem.getValue());
		assertEquals(VALUE_SET_URL, myValueSetProvider.myLastUrl.getValueAsString());
      assertNull(myValueSetProvider.myLastValueSet);
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
      assertNull(outcome);
	}

	@Test
	public void testTranslateCode_AllInParams_AllOutParams() {
		myConceptMapProvider.myNextReturnParams = new Parameters();
		myConceptMapProvider.myNextReturnParams.addParameter("result", true);
		myConceptMapProvider.myNextReturnParams.addParameter("message", ERROR_MESSAGE);

		TranslateConceptResults expectedResults = new TranslateConceptResults();
		expectedResults.setResult(true);

		// Add 2 matches
		addMatchToTranslateRequest(myConceptMapProvider.myNextReturnParams);
		addMatchToTranslateRequest(myConceptMapProvider.myNextReturnParams);

		List<TranslateConceptResult> translateResults = new ArrayList<>();
		TranslateConceptResult singleResult = new TranslateConceptResult();
		singleResult
			.setEquivalence(EQUIVALENCE_CODE)
			.setSystem(TARGET_SYSTEM)
			.setCode(TARGET_CODE)
			.setConceptMapUrl(CONCEPT_MAP_URL)
			.setDisplay(TARGET_CODE_DISPLAY);
		translateResults.add(singleResult);
		translateResults.add(singleResult);
		expectedResults.setResults(translateResults);

		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.addCoding(new Coding(CODE_SYSTEM, CODE, null));

		IValidationSupport.TranslateCodeRequest request = new IValidationSupport.TranslateCodeRequest(
			Collections.unmodifiableList(codeableConcept.getCoding()),
			TARGET_SYSTEM,
			CONCEPT_MAP_URL,
			CONCEPT_MAP_VERSION,
			SOURCE_VALUE_SET_URL,
			TARGET_VALUE_SET_URL,
			null,
			REVERSE);

		TranslateConceptResults results = mySvc.translateConcept(request);

		assertNotNull(results);
      assertTrue(results.getResult());
		assertEquals(results.getResults().size(), 2);
		for(TranslateConceptResult result : results.getResults()) {
			assertEquals(singleResult, result);
		}

		assertTrue(codeableConcept.equalsDeep(myConceptMapProvider.myLastCodeableConcept));
		assertEquals(TARGET_SYSTEM, myConceptMapProvider.myLastTargetCodeSystem.getValue());
		assertEquals(CONCEPT_MAP_URL, myConceptMapProvider.myLastConceptMapUrl.getValue());
		assertEquals(CONCEPT_MAP_VERSION, myConceptMapProvider.myLastConceptMapVersion.getValue());
		assertEquals(SOURCE_VALUE_SET_URL, myConceptMapProvider.myLastSourceValueSet.getValue());
		assertEquals(TARGET_VALUE_SET_URL, myConceptMapProvider.myLastTargetValueSet.getValue());
		assertEquals(REVERSE, myConceptMapProvider.myLastReverse.getValue());
	}

	@Test
	public void testTranslateCode_NoInParams_NoOutParams() {
		myConceptMapProvider.myNextReturnParams = new Parameters();

		List<IBaseCoding> codings = new ArrayList<>();
		codings.add(new Coding(null, null, null));
		IValidationSupport.TranslateCodeRequest request = new IValidationSupport.TranslateCodeRequest(codings, null);

		TranslateConceptResults results = mySvc.translateConcept(request);
		assertNotNull(results);
      assertFalse(results.getResult());
		assertEquals(results.getResults().size(), 0);

		assertNull(myConceptMapProvider.myLastCodeableConcept);
		assertNull(myConceptMapProvider.myLastTargetCodeSystem);
		assertNull(myConceptMapProvider.myLastConceptMapUrl);
		assertNull(myConceptMapProvider.myLastConceptMapVersion);
		assertNull(myConceptMapProvider.myLastSourceValueSet);
		assertNull(myConceptMapProvider.myLastTargetValueSet);
		assertNull(myConceptMapProvider.myLastReverse);
	}

	@Test
	void testFetchCodeSystem_forcesSummaryFalse() {
		// given
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();

		// when
		mySvc.fetchCodeSystem("http://loinc.org");

		// then
		assertEquals(SummaryEnum.FALSE, myCodeSystemProvider.myLastSummaryParam);
	}


	private void addMatchToTranslateRequest(Parameters params) {
		Parameters.ParametersParameterComponent matchParam = params.addParameter().setName("match");
		matchParam.addPart().setName("equivalence").setValue(new CodeType(EQUIVALENCE_CODE));
		Coding value = new Coding(TARGET_SYSTEM, TARGET_CODE, TARGET_CODE_DISPLAY);
		matchParam.addPart().setName("concept").setValue(value);
		matchParam.addPart().setName("source").setValue(new UriType(CONCEPT_MAP_URL));
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
		private static class TestClientInterceptor implements IClientInterceptor {

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
					// ignore
				}
			}

			@Override
			public void interceptResponse(IHttpResponse theResponse) { }

			public String getCapturedSystemParameter() { return capturedSystemParameter; }
		}
	}



	@Test
	public void testIsValueSetSupported_False() {
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();

		boolean outcome = mySvc.isValueSetSupported(null, "http://loinc.org/VS");
      assertFalse(outcome);
		assertEquals("http://loinc.org/VS", myValueSetProvider.myLastUrlParam.getValue());
	}

	@Test
	public void testIsValueSetSupported_True() {
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();
		myValueSetProvider.myNextReturnValueSets.add((ValueSet) new ValueSet().setId("ValueSet/123"));

		boolean outcome = mySvc.isValueSetSupported(null, "http://loinc.org/VS");
      assertTrue(outcome);
		assertEquals("http://loinc.org/VS", myValueSetProvider.myLastUrlParam.getValue());
	}

	@Test
	public void testIsCodeSystemSupported_False() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();

		boolean outcome = mySvc.isCodeSystemSupported(null, "http://loinc.org");
      assertFalse(outcome);
		assertEquals("http://loinc.org", myCodeSystemProvider.myLastUrlParam.getValue());
	}

	@Test
	public void testIsCodeSystemSupported_True() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myNextReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/123"));

		boolean outcome = mySvc.isCodeSystemSupported(null, "http://loinc.org");
      assertTrue(outcome);
		assertEquals("http://loinc.org", myCodeSystemProvider.myLastUrlParam.getValue());
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

		private SummaryEnum myLastSummaryParam;
		private UriParam myLastUrlParam;
		private List<CodeSystem> myNextReturnCodeSystems;
		private UriType myLastUrl;
		private CodeType myLastCode;
		private Parameters myNextReturnParams;
		private IValidationSupport.LookupCodeResult myNextLookupCodeResult;
		private IValidationSupport.CodeValidationResult myNextValidationResult;

		@Operation(name = "validate-code", idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1),
			@OperationParam(name = "message", type = StringType.class),
			@OperationParam(name = "display", type = StringType.class)
		})
		public IBaseParameters validateCode(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "url", min = 0, max = 1) UriType theCodeSystemUrl,
			@OperationParam(name = "code", min = 0, max = 1) CodeType theCode,
			@OperationParam(name = "display", min = 0, max = 1) StringType theDisplay
		) {
			myLastUrl = theCodeSystemUrl;
			myLastCode = theCode;
			myNextReturnParams = (Parameters)myNextValidationResult.toParameters(ourCtx);
			return myNextReturnParams;
		}

		@Operation(name = JpaConstants.OPERATION_LOOKUP, idempotent = true, returnParameters= {
			@OperationParam(name="name", type=StringType.class, min=1),
			@OperationParam(name="version", type=StringType.class, min=0),
			@OperationParam(name="display", type=StringType.class, min=1),
			@OperationParam(name="abstract", type=BooleanType.class, min=1),
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED)
		})
		public IBaseParameters lookup(
			HttpServletRequest theServletRequest,
			@OperationParam(name="code", min=0, max=1) CodeType theCode,
			@OperationParam(name="system", min=0, max=1) UriType theSystem,
			@OperationParam(name="coding", min=0, max=1) Coding theCoding,
			@OperationParam(name="version", min=0, max=1) StringType theVersion,
			@OperationParam(name="displayLanguage", min=0, max=1) CodeType theDisplayLanguage,
			@OperationParam(name="property", min = 0, max = OperationParam.MAX_UNLIMITED) List<CodeType> thePropertyNames,
			RequestDetails theRequestDetails
		) {
			myLastCode = theCode;
			myLastUrl = theSystem;
			myNextReturnParams = (Parameters)myNextLookupCodeResult.toParameters(theRequestDetails.getFhirContext(), thePropertyNames);
			return myNextReturnParams;
		}

		@Search
		public List<CodeSystem> find(@RequiredParam(name = "url") UriParam theUrlParam, SummaryEnum theSummaryParam) {
			myLastUrlParam = theUrlParam;
			myLastSummaryParam = theSummaryParam;
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
		private SummaryEnum myLastSummaryParam;

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
		public List<ValueSet> find(@RequiredParam(name = "url") UriParam theUrlParam, SummaryEnum theSummaryParam) {
			myLastUrlParam = theUrlParam;
			myLastSummaryParam = theSummaryParam;
			assert myNextReturnValueSets != null;
			return myNextReturnValueSets;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ValueSet.class;
		}

	}

	private static class MyConceptMapProvider implements IResourceProvider {
		private UriType myLastConceptMapUrl;
		private StringType myLastConceptMapVersion;
		private CodeableConcept myLastCodeableConcept;
		private UriType myLastSourceValueSet;
		private UriType myLastTargetValueSet;
		private UriType myLastTargetCodeSystem;
		private BooleanType myLastReverse;
		private Parameters myNextReturnParams;

		@Operation(name = JpaConstants.OPERATION_TRANSLATE, idempotent = true, returnParameters = {
			@OperationParam(name = "result", type = BooleanType.class, min = 1, max = 1),
			@OperationParam(name = "message", type = StringType.class, min = 0, max = 1),
		})
		public Parameters translate(
			HttpServletRequest theServletRequest,
			@IdParam(optional = true) IdType theId,
			@OperationParam(name = "url", min = 0, max = 1) UriType theConceptMapUrl,
			@OperationParam(name = "conceptMapVersion", min = 0, max = 1) StringType theConceptMapVersion,
			@OperationParam(name = "codeableConcept", min = 0, max = 1) CodeableConcept theSourceCodeableConcept,
			@OperationParam(name = "source", min = 0, max = 1) UriType theSourceValueSet,
			@OperationParam(name = "target", min = 0, max = 1) UriType theTargetValueSet,
			@OperationParam(name = "targetsystem", min = 0, max = 1) UriType theTargetCodeSystem,
			@OperationParam(name = "reverse", min = 0, max = 1) BooleanType theReverse,
			RequestDetails theRequestDetails
		) {
			myLastConceptMapUrl = theConceptMapUrl;
			myLastConceptMapVersion = theConceptMapVersion;
			myLastCodeableConcept = theSourceCodeableConcept;
			myLastSourceValueSet = theSourceValueSet;
			myLastTargetValueSet = theTargetValueSet;
			myLastTargetCodeSystem = theTargetCodeSystem;
			myLastReverse = theReverse;
			return myNextReturnParams;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ConceptMap.class;
		}

	}

}
