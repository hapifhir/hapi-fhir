package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.StringNormalizer;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class SearchParamExtractorDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static IValidationSupport ourValidationSupport;

	@Test
	public void testParamWithOrInPath() {
		Observation obs = new Observation();
		obs.addCategory().addCoding().setSystem("SYSTEM").setCode("CODE");

		ISearchParamRegistry searchParamRegistry = new ISearchParamRegistry() {
			@Override
			public void forceRefresh() {
				// nothing
			}

			@Override
			public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean refreshCacheIfNecessary() {
				// nothing
				return false;
			}

			@Override
			public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
				RuntimeResourceDefinition nextResDef = ourCtx.getResourceDefinition(theResourceName);
				Map<String, RuntimeSearchParam> sps = new HashMap<>();
				for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
					sps.put(nextSp.getName(), nextSp);
				}
				return sps;
			}

			@Override
			public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
				throw new UnsupportedOperationException();
			}

			@Override
			public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void requestRefresh() {
				// nothing
			}

			@Override
			public RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
				return null;
			}

			@Override
			public Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef) {
				return null;
			}
		};

		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(new ResourceTable(), obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
	}

	@Test
	public void testNormalizedStringIsShortened() {
		// String with character that will change it's length on normalization
		String value = IntStream.range(1, 200).mapToObj(v -> "a").collect(Collectors.joining()) + "ئ";
		assertEquals(value.length(), 200);
		assertEquals(Normalizer.normalize(value, Normalizer.Form.NFD).length(), 201);
		assertEquals(StringNormalizer.normalizeString(value).length(), 201);

		Questionnaire questionnaire = new Questionnaire();
		questionnaire.setDescription(value);

		ISearchParamRegistry searchParamRegistry = new ISearchParamRegistry() {
			@Override
			public void forceRefresh() {
				// nothing
			}

			@Override
			public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
				throw new UnsupportedOperationException();
			}

			@Override
			public boolean refreshCacheIfNecessary() {
				// nothing
				return false;
			}

			@Override
			public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Map<String, RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
				RuntimeResourceDefinition nextResDef = ourCtx.getResourceDefinition(theResourceName);
				Map<String, RuntimeSearchParam> sps = new HashMap<>();
				for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
					sps.put(nextSp.getName(), nextSp);
				}
				return sps;
			}

			@Override
			public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
				throw new UnsupportedOperationException();
			}

			@Override
			public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void requestRefresh() {
				// nothing
			}

			@Override
			public RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName) {
				return null;
			}

			@Override
			public Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef) {
				return null;
			}
		};

		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(new ModelConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		extractor.start();
		Set<ResourceIndexedSearchParamString> params = extractor.extractSearchParamStrings(new ResourceTable(), questionnaire);
		assertEquals(1, params.size());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourValidationSupport = new DefaultProfileValidationSupport();
	}

}
