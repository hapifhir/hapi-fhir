package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.Assert.assertEquals;

import java.util.*;

import ca.uhn.fhir.jpa.search.JpaRuntimeSearchParam;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.Observation;
import org.junit.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.util.TestUtil;

public class SearchParamExtractorR4Test {

	private static FhirContext ourCtx = FhirContext.forR4();
	private static IValidationSupport ourValidationSupport;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourValidationSupport = new DefaultProfileValidationSupport();
	}
	
	@Test
	public void testParamWithOrInPath() {
		Observation obs = new Observation();
		obs.addCategory().addCoding().setSystem("SYSTEM").setCode("CODE");
		
		ISearchParamRegistry searchParamRegistry = new ISearchParamRegistry() {
			@Override
			public Map<String,RuntimeSearchParam> getActiveSearchParams(String theResourceName) {
				RuntimeResourceDefinition nextResDef = ourCtx.getResourceDefinition(theResourceName);
				Map<String, RuntimeSearchParam> sps = new HashMap<String, RuntimeSearchParam>();
				for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
					sps.put(nextSp.getName(), nextSp);
				}
				return sps;
			}

			@Override
			public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName) {
				throw new UnsupportedOperationException();
			}

			@Override
			public List<JpaRuntimeSearchParam> getActiveUniqueSearchParams(String theResourceName, Set<String> theParamNames) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void forceRefresh() {
				// nothing
			}

			@Override
			public Map<String, Map<String, RuntimeSearchParam>> getActiveSearchParams() {
				throw new UnsupportedOperationException();
			}

			@Override
			public RuntimeSearchParam getActiveSearchParam(String theResourceName, String theParamName) {
				throw new UnsupportedOperationException();
			}
		};
		
		SearchParamExtractorR4 extractor = new SearchParamExtractorR4(ourCtx, ourValidationSupport, searchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(new ResourceTable(), obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
	}
	
}
