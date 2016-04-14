package ca.uhn.fhir.jpa.dao.dstu3;

import static org.junit.Assert.*;

import java.util.Set;

import org.hl7.fhir.dstu3.model.Observation;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.util.TestUtil;

public class SearchParamExtractorDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testParamWithOrInPath() {
		Observation obs = new Observation();
		obs.getCategory().addCoding().setSystem("SYSTEM").setCode("CODE");
		
		SearchParamExtractorDstu3 extractor = new SearchParamExtractorDstu3(ourCtx);
		Set<BaseResourceIndexedSearchParam> tokens = extractor.extractSearchParamTokens(new ResourceTable(), obs);
		assertEquals(1, tokens.size());
		ResourceIndexedSearchParamToken token = (ResourceIndexedSearchParamToken) tokens.iterator().next();
		assertEquals("category", token.getParamName());
		assertEquals("SYSTEM", token.getSystem());
		assertEquals("CODE", token.getValue());
	}
	
}
