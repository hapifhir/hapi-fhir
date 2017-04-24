package ca.uhn.fhir.jpa.dao.dstu3;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport;
import org.hl7.fhir.dstu3.model.Observation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.util.TestUtil;

public class SearchParamExtractorDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();
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
		
		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(ourCtx, ourValidationSupport, searchParamRegistry);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(new ResourceTable(), obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
	}
	
}
