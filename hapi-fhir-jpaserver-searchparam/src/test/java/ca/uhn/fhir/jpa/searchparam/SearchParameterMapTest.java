package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static ca.uhn.fhir.jpa.searchparam.SearchParameterMap.compare;
import static ca.uhn.fhir.rest.param.TokenParamModifier.TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SearchParameterMapTest {
	static FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Test
	void toNormalizedQueryStringLower() {
		SearchParameterMap map = new SearchParameterMap();
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setLowerBound("2021-05-31");
		map.setLastUpdated(dateRangeParam);
		assertEquals("?_lastUpdated=ge2021-05-31", map.toNormalizedQueryString(ourFhirContext));
	}

	@Test
	void toNormalizedQueryString_IncludeNormal() {
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(new Include("Patient:name"));
		assertEquals("?_include=Patient:name", map.toNormalizedQueryString(ourFhirContext));
	}

	@Test
	void toNormalizedQueryString_IncludeStar() {
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(new Include("*"));
		assertEquals("?_include=*", map.toNormalizedQueryString(ourFhirContext));
	}

	@Test
	void toNormalizedQueryString_IncludeTypedStar() {
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(new Include("Patient:*"));
		assertEquals("?_include=Patient:*", map.toNormalizedQueryString(ourFhirContext));
	}

	@Test
	void toNormalizedQueryStringUpper() {
		SearchParameterMap map = new SearchParameterMap();
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setUpperBound("2021-05-31");
		map.setLastUpdated(dateRangeParam);
		assertEquals("?_lastUpdated=le2021-05-31", map.toNormalizedQueryString(ourFhirContext));
	}

	@Test
	public void testRemoveByModifier() {
		SearchParameterMap map = new SearchParameterMap();

		TokenOrListParam qualifiedTokenParam = new TokenOrListParam()
			.addOr(new TokenParam("weight-text-1").setModifier(TEXT))
			.addOr(new TokenParam("weight-text-2").setModifier(TEXT));

		TokenParam unqualifiedTokenParam = new TokenParam("weight-no-text");

		map.add("code", qualifiedTokenParam);
		map.add("code", unqualifiedTokenParam);
		List<List<IQueryParameterType>> andList = map.removeByNameAndModifier("code", TEXT);
		assertThat(andList).hasSize(1);
		List<IQueryParameterType> orList = andList.get(0);
		assertThat(orList).hasSize(2);

		List<List<IQueryParameterType>> unqualifiedAnds = map.get("code");
		assertThat(unqualifiedAnds).hasSize(1);


	}

	@Test
	public void testRemoveByNullModifier() {
		SearchParameterMap map = new SearchParameterMap();

		TokenOrListParam unqualifiedTokenParam = new TokenOrListParam()
			.addOr(new TokenParam("http://example.com", "123"))
			.addOr(new TokenParam("http://example.com", "345"));

		TokenParam qualifiedTokenParam = new TokenParam("weight-text").setModifier(TEXT);

		map.add("code", unqualifiedTokenParam);
		map.add("code", qualifiedTokenParam);
		List<List<IQueryParameterType>> andList = map.removeByNameAndModifier("code", (String) null);
		assertThat(andList).hasSize(1);
		List<IQueryParameterType> orList = andList.get(0);
		assertThat(orList).hasSize(2);

		List<List<IQueryParameterType>> qualifiedAnds = map.get("code");
		assertThat(qualifiedAnds).hasSize(1);
	}

	@Test
	public void testRemoveByQualifierRemovesAll() {
		SearchParameterMap map = new SearchParameterMap();

		TokenOrListParam qualifiedTokenParam = new TokenOrListParam()
			.addOr(new TokenParam("weight-text-1").setModifier(TEXT))
			.addOr(new TokenParam("weight-text-2").setModifier(TEXT));

		map.add("code", qualifiedTokenParam);
		List<List<IQueryParameterType>> andList = map.removeByNameAndModifier("code", TEXT);
		assertThat(andList).hasSize(1);
		List<IQueryParameterType> orList = andList.get(0);
		assertThat(orList).hasSize(2);

		List<List<IQueryParameterType>> unqualifiedAnds = map.remove("code");
		assertNull(unqualifiedAnds);
	}

	@Test
	public void clone_searchParams_copiesAllFields() {
		HashSet<Include> includes = new HashSet<>();
		Include i = new Include("test", true);
		includes.add(i);

		SearchParameterMap orig = new SearchParameterMap();
		orig.setOffset(1);
		orig.setLoadSynchronousUpTo(2);
		orig.setLoadSynchronous(true);
		orig.setNearDistanceParam(new QuantityParam());
		orig.setCount(3);
		orig.setLastNMax(4);
		orig.setLastN(true);
		orig.setDeleteExpunge(true);
		orig.setIncludes(includes);
		orig.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		orig.setLastUpdated(new DateRangeParam());
		orig.setSearchContainedMode(SearchContainedModeEnum.BOTH);
		orig.setEverythingMode(SearchParameterMap.EverythingModeEnum.ENCOUNTER_INSTANCE);
		orig.setSort(new SortSpec());
		orig.add("something", new StringParam("value"));

		// test
		SearchParameterMap copy = orig.clone();

		// verify that they are not the same
		assertThat(copy).isNotEqualTo(orig);

		// ... but that they are equal
		assertEquals(orig.toNormalizedQueryString(null), copy.toNormalizedQueryString(null));
		assertEquals(orig.getOffset(), copy.getOffset());
		assertEquals(orig.getLoadSynchronousUpTo(), copy.getLoadSynchronousUpTo());
		assertEquals(orig.isLoadSynchronous(), copy.isLoadSynchronous());
		assertEquals(orig.getNearDistanceParam(), copy.getNearDistanceParam());
		assertEquals(orig.getCount(), copy.getCount());
		assertEquals(orig.getLastNMax(), copy.getLastNMax());
		assertEquals(orig.isLastN(), copy.isLastN());
		assertEquals(orig.isDeleteExpunge(), copy.isDeleteExpunge());
		assertEquals(orig.getIncludes(), copy.getIncludes());
		assertEquals(orig.getSearchTotalMode(), copy.getSearchTotalMode());
		assertEquals(orig.getLastUpdated(), copy.getLastUpdated());
		assertEquals(orig.getSearchContainedMode(), copy.getSearchContainedMode());
		assertEquals(orig.getEverythingMode(), copy.getEverythingMode());
		assertEquals(orig.getSort(), copy.getSort());
		assertEquals(orig.get("something"), copy.get("something"));

		// verify changing one does not change the other
		orig.setOffset(100);
		assertThat(copy.toNormalizedQueryString(null)).isNotEqualTo(orig.toNormalizedQueryString(null));
	}

	@Test
	public void clone_searchParams_haveSameSearchParamsMap() {
		SearchParameterMap orig = new SearchParameterMap();
		orig.add("string", new StringParam("stringvalue"));
		orig.add("datetime", new DateRangeParam(Date.from(Instant.now()),
			new Date(2000, 11, 11)));
		orig.add("int", new QuantityParam(1));

		// test
		SearchParameterMap clone = orig.clone();

		// verify
		assertEquals(orig.size(), clone.size());
		assertEquals(orig.get("string"), clone.get("string"));
		assertEquals(orig.get("datetime"), clone.get("datetime"));
		assertEquals(orig.get("int"), clone.get("int"));
	}


	@Test
	public void testCompareParameters() {

		// Missing
		assertEquals(0, compare(ourFhirContext, new StringParam().setMissing(true), new StringParam().setMissing(true)));
		assertEquals(-1, compare(ourFhirContext, new StringParam("A"), new StringParam().setMissing(true)));
		assertEquals(1, compare(ourFhirContext, new StringParam().setMissing(true), new StringParam("A")));

		// Qualifier
		assertEquals(0, compare(ourFhirContext, new StringParam("A").setContains(true), new StringParam("A").setContains(true)));
		assertEquals(1, compare(ourFhirContext, new StringParam("A").setContains(true), new StringParam("A")));
		assertEquals(-1, compare(ourFhirContext, new StringParam("A"), new StringParam("A").setContains(true)));

		// Value
		assertEquals(0, compare(ourFhirContext, new StringParam("A"), new StringParam("A")));
		assertEquals(-1, compare(ourFhirContext, new StringParam("A"), new StringParam("B")));
		assertEquals(1, compare(ourFhirContext, new StringParam("B"), new StringParam("A")));

		// Value + Comparator (value should have no effect if comparator is changed)
		assertEquals(1, compare(ourFhirContext, new StringParam("B").setContains(true), new StringParam("A")));
		assertEquals(1, compare(ourFhirContext, new StringParam("A").setContains(true), new StringParam("B")));

	}



}
