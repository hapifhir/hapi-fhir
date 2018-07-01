package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.Observation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class SearchParamExtractorR4Test {

	private static FhirContext ourCtx = FhirContext.forR4();
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
			public void refreshCacheIfNecessary() {
				// nothing
			}

			@Override
			public void requestRefresh() {
				// nothing
			}
		};

		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(new DaoConfig(), ourCtx, ourValidationSupport, searchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(new ResourceTable(), obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
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
