package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.UrlUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SearchParameterMapTest {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	
	
	@Test
	public void testToQueryStringAndOr() {
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
		assertEquals("?birthdate=ge2001&birthdate=lt2002&name=bouvier,simpson&name=homer,jay&name:exact=ZZZ%3F", queryString);
		assertEquals("?birthdate=ge2001&birthdate=lt2002&name=bouvier,simpson&name=homer,jay&name:exact=ZZZ?", UrlUtil.unescape(queryString));
	}

	@Test
	public void testContainedParameterIsIncludedInNormalizedString() {
		SearchParameterMap map = new SearchParameterMap();
		map.add("name", new StringParam("Smith"));
		map.setSearchContainedMode(SearchContainedModeEnum.TRUE);
		String containedQueryString = map.toNormalizedQueryString(ourCtx);

		SearchParameterMap uncontainedMap = new SearchParameterMap();
		uncontainedMap.add("name", new StringParam("Smith"));
		uncontainedMap.setSearchContainedMode(SearchContainedModeEnum.FALSE);
		String uncontainedQueryString = uncontainedMap.toNormalizedQueryString(ourCtx);

		ourLog.info(containedQueryString);
		ourLog.info(uncontainedQueryString);
		assertNotEquals(containedQueryString, uncontainedQueryString);

	}

	@Test
	public void testToQueryStringEmpty() {
		SearchParameterMap map = new SearchParameterMap();
		
		String queryString = map.toNormalizedQueryString(ourCtx);
		ourLog.info(queryString);
		assertEquals("?", queryString);
		assertEquals("?", UrlUtil.unescape(queryString));
	}

	@Test
	public void testToQueryStringInclude() {
		SearchParameterMap map = new SearchParameterMap();
		
		map.add("birthdate", new DateParam(ParamPrefixEnum.APPROXIMATE, "2011"));
		
		map.addInclude(new Include("Patient:subject"));
		map.addInclude(new Include("Patient:aartvark", true));
		map.addInclude(new Include("Patient:aartvark:z"));
		map.addInclude(new Include("Patient:aartvark:a"));
		
		String queryString = map.toNormalizedQueryString(ourCtx);
		ourLog.info(queryString);
		ourLog.info(UrlUtil.unescape(queryString));
		assertEquals("?birthdate=ap2011&_include=Patient:aartvark&_include=Patient:aartvark:a&_include=Patient:aartvark:z&_include=Patient:subject", queryString);
		assertEquals("?birthdate=ap2011&_include=Patient:aartvark&_include=Patient:aartvark:a&_include=Patient:aartvark:z&_include=Patient:subject", UrlUtil.unescape(queryString));
	}

	@Test
	public void testToQueryStringRevInclude() {
		SearchParameterMap map = new SearchParameterMap();
		
		map.add("birthdate", new DateParam(ParamPrefixEnum.APPROXIMATE, "2011"));
		
		map.addRevInclude(new Include("Patient:subject"));
		map.addRevInclude(new Include("Patient:aartvark", true));
		map.addRevInclude(new Include("Patient:aartvark:z"));
		map.addRevInclude(new Include("Patient:aartvark:a"));
		
		String queryString = map.toNormalizedQueryString(ourCtx);
		ourLog.info(queryString);
		ourLog.info(UrlUtil.unescape(queryString));
		assertEquals("?birthdate=ap2011&_revinclude=Patient:aartvark&_revinclude=Patient:aartvark:a&_revinclude=Patient:aartvark:z&_revinclude=Patient:subject", queryString);
		assertEquals("?birthdate=ap2011&_revinclude=Patient:aartvark&_revinclude=Patient:aartvark:a&_revinclude=Patient:aartvark:z&_revinclude=Patient:subject", UrlUtil.unescape(queryString));
	}

	@Test
	public void testToQueryStringSort() {
		SearchParameterMap map = new SearchParameterMap();
		
		TokenAndListParam tokenAnd = new TokenAndListParam()
				.addAnd(new TokenOrListParam().add(new TokenParam("SYS", "|VAL"))); // | needs escaping
		map.add("identifier", tokenAnd);
		
		map.setSort(new SortSpec("name").setChain(new SortSpec("identifier", SortOrderEnum.DESC)));
		
		String queryString = map.toNormalizedQueryString(ourCtx);
		ourLog.info(queryString);
		ourLog.info(UrlUtil.unescape(queryString));
		
		assertEquals("?identifier=SYS%7C%5C%7CVAL&_sort=name,-identifier", queryString);
		assertEquals("?identifier=SYS|\\|VAL&_sort=name,-identifier", UrlUtil.unescape(queryString));
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
