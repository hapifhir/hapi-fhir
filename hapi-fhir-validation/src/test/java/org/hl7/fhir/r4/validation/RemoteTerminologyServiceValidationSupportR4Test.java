package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResult;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseCoding;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Version specific tests for validation using RemoteTerminologyValidationSupport.
 * The tests in this class simulate the call to a remote server and therefore, only tests the code in
 * the RemoteTerminologyServiceValidationSupport itself. The remote client call is simulated using the test providers.
 * @see RemoteTerminologyServiceValidationSupport
 *
 * Other operations are tested separately.
 * @see RemoteTerminologyLookupCodeR4Test
 * @see RemoteTerminologyValidateCodeR4Test
 */
public class RemoteTerminologyServiceValidationSupportR4Test extends BaseValidationTestWithInlineMocks {
	private static final String CODE_SYSTEM = "CODE_SYS";
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

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public static RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(ourCtx);

	private final MyValueSetProvider myValueSetProvider = new MyValueSetProvider();
	private final MyCodeSystemProvider myCodeSystemProvider = new MyCodeSystemProvider();
	private final MyConceptMapProvider myConceptMapProvider = new MyConceptMapProvider();
	private final RemoteTerminologyServiceValidationSupport mySvc = new RemoteTerminologyServiceValidationSupport(ourCtx);

	@BeforeEach
	public void before() {
		myRestfulServerExtension.getRestfulServer().registerProvider(myValueSetProvider);
		myRestfulServerExtension.getRestfulServer().registerProvider(myCodeSystemProvider);
		myRestfulServerExtension.getRestfulServer().registerProvider(myConceptMapProvider);

		String baseUrl = "http://localhost:" + myRestfulServerExtension.getPort();
		mySvc.setBaseUrl(baseUrl);
		mySvc.addClientInterceptor(new LoggingInterceptor(true));
	}

	@Test
	void fetchValueSet_forcesSummaryFalse() {
		// given
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();

		// when
		mySvc.fetchValueSet(VALUE_SET_URL);

		// then
		assertEquals(SummaryEnum.FALSE, myValueSetProvider.myLastSummaryParam);
	}

	@Test
	void translateCode_AllInParams_AllOutParams() {
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
		assertEquals(2, results.getResults().size());
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
	void translateCode_NoInParams_NoOutParams() {
		myConceptMapProvider.myNextReturnParams = new Parameters();

		List<IBaseCoding> codings = new ArrayList<>();
		codings.add(new Coding(null, null, null));
		IValidationSupport.TranslateCodeRequest request = new IValidationSupport.TranslateCodeRequest(codings, null);

		TranslateConceptResults results = mySvc.translateConcept(request);
		assertNotNull(results);
		assertFalse(results.getResult());
		assertEquals(0, results.getResults().size());

		assertNull(myConceptMapProvider.myLastCodeableConcept);
		assertNull(myConceptMapProvider.myLastTargetCodeSystem);
		assertNull(myConceptMapProvider.myLastConceptMapUrl);
		assertNull(myConceptMapProvider.myLastConceptMapVersion);
		assertNull(myConceptMapProvider.myLastSourceValueSet);
		assertNull(myConceptMapProvider.myLastTargetValueSet);
		assertNull(myConceptMapProvider.myLastReverse);
	}

	@Test
	void fetchCodeSystem_forcesSummaryFalse() {
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

	@Test
	void isValueSetSupported_False() {
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();

		boolean outcome = mySvc.isValueSetSupported(null, "http://loinc.org/VS");
		assertFalse(outcome);
		assertEquals("http://loinc.org/VS", myValueSetProvider.myLastUrlParam.getValue());
	}

	@Test
	void isValueSetSupported_True() {
		myValueSetProvider.myNextReturnValueSets = new ArrayList<>();
		myValueSetProvider.myNextReturnValueSets.add((ValueSet) new ValueSet().setId("ValueSet/123"));

		boolean outcome = mySvc.isValueSetSupported(null, "http://loinc.org/VS");
		assertTrue(outcome);
		assertEquals("http://loinc.org/VS", myValueSetProvider.myLastUrlParam.getValue());
	}

	@Test
	void isCodeSystemSupported_False() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();

		boolean outcome = mySvc.isCodeSystemSupported(null, "http://loinc.org");
		assertFalse(outcome);
		assertEquals("http://loinc.org", myCodeSystemProvider.myLastUrlParam.getValue());
	}

	@Test
	void isCodeSystemSupported_True() {
		myCodeSystemProvider.myNextReturnCodeSystems = new ArrayList<>();
		myCodeSystemProvider.myNextReturnCodeSystems.add((CodeSystem) new CodeSystem().setId("CodeSystem/123"));

		boolean outcome = mySvc.isCodeSystemSupported(null, "http://loinc.org");
		assertTrue(outcome);
		assertEquals("http://loinc.org", myCodeSystemProvider.myLastUrlParam.getValue());
	}

	@SuppressWarnings("unused")
	private static class MyCodeSystemProvider implements IResourceProvider {
		private SummaryEnum myLastSummaryParam;
		private UriParam myLastUrlParam;
		private List<CodeSystem> myNextReturnCodeSystems;

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CodeSystem.class;
		}

		@Search
		public List<CodeSystem> find(@RequiredParam(name = "url") UriParam theUrlParam, SummaryEnum theSummaryParam) {
			myLastUrlParam = theUrlParam;
			myLastSummaryParam = theSummaryParam;
			assert myNextReturnCodeSystems != null;
			return myNextReturnCodeSystems;
		}
	}

	@SuppressWarnings("unused")
	private static class MyValueSetProvider implements IResourceProvider {
		private List<ValueSet> myNextReturnValueSets;
		private UriParam myLastUrlParam;
		private SummaryEnum myLastSummaryParam;

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

	@SuppressWarnings("unused")
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
