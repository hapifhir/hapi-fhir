package ca.uhn.fhir.jpa.provider;

import static java.util.Collections.addAll;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.util.TestUtil;

public class SearchParameterMapTest {

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	
	@Test
	public void testToQueryString() {
		SearchParameterMap map = new SearchParameterMap();
		
		StringAndListParam familyAnd = new StringAndListParam()
				.addAnd(new StringOrListParam().add(new StringParam("ZZZ?").setExact(true)))
				.addAnd(new StringOrListParam().add(new StringParam("homer")).add(new StringParam("jay")))
				.addAnd(new StringOrListParam().add(new StringParam("simpson")).add(new StringParam("bouvier")));
		map.add("name", familyAnd);
		
		DateAndListParam birthdateAnd = new DateAndListParam()
				.addAnd(new DateOrListParam().add(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, "2001")))
				.addAnd(new DateOrListParam().add(new DateParam(ParamPrefixEnum.LESSTHAN, "2002")));
		map.add("birthdate", birthdateAnd);
		
		String queryString = map.toNormalizedQueryString(ourCtx);
		ourLog.info(queryString);
	}
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParameterMapTest.class);

	/**
	 * {@link Search} uses these ordinals so they shouldn't get out of order
	 */
	@Test
	public void testEverythingOrdinals() {
		assertEquals(0, EverythingModeEnum.ENCOUNTER_INSTANCE.ordinal());
		assertEquals(1, EverythingModeEnum.ENCOUNTER_TYPE.ordinal());
		assertEquals(2, EverythingModeEnum.PATIENT_INSTANCE.ordinal());
		assertEquals(3, EverythingModeEnum.PATIENT_TYPE.ordinal());
	}
	
}
