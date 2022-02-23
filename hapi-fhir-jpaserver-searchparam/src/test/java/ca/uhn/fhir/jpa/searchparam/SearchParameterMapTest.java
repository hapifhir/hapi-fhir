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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import static ca.uhn.fhir.rest.param.TokenParamModifier.TEXT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertThat(andList, hasSize(1));
		List<IQueryParameterType> orList = andList.get(0);
		assertThat(orList, hasSize(2));

		List<List<IQueryParameterType>> unqualifiedAnds = map.get("code");
		assertThat(unqualifiedAnds, hasSize(1));


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
		assertThat(andList, hasSize(1));
		List<IQueryParameterType> orList = andList.get(0);
		assertThat(orList, hasSize(2));

		List<List<IQueryParameterType>> qualifiedAnds = map.get("code");
		assertThat(qualifiedAnds, hasSize(1));
	}

	@Test
	public void testRemoveByQualifierRemovesAll() {
		SearchParameterMap map = new SearchParameterMap();

		TokenOrListParam qualifiedTokenParam = new TokenOrListParam()
			.addOr(new TokenParam("weight-text-1").setModifier(TEXT))
			.addOr(new TokenParam("weight-text-2").setModifier(TEXT));

		map.add("code", qualifiedTokenParam);
		List<List<IQueryParameterType>> andList = map.removeByNameAndModifier("code", TEXT);
		assertThat(andList, hasSize(1));
		List<IQueryParameterType> orList = andList.get(0);
		assertThat(orList, hasSize(2));

		List<List<IQueryParameterType>> unqualifiedAnds = map.remove("code");
		assertThat(unqualifiedAnds, is(nullValue()));
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
		Assertions.assertNotEquals(orig, copy);

		// ... but that they are equal
		Assertions.assertEquals(orig.toNormalizedQueryString(null),
			copy.toNormalizedQueryString(null));
		Assertions.assertEquals(orig.getOffset(), copy.getOffset());
		Assertions.assertEquals(orig.getLoadSynchronousUpTo(), copy.getLoadSynchronousUpTo());
		Assertions.assertEquals(orig.isLoadSynchronous(), copy.isLoadSynchronous());
		Assertions.assertEquals(orig.getNearDistanceParam(), copy.getNearDistanceParam());
		Assertions.assertEquals(orig.getCount(), copy.getCount());
		Assertions.assertEquals(orig.getLastNMax(), copy.getLastNMax());
		Assertions.assertEquals(orig.isLastN(), copy.isLastN());
		Assertions.assertEquals(orig.isDeleteExpunge(), copy.isDeleteExpunge());
		Assertions.assertEquals(orig.getIncludes(), copy.getIncludes());
		Assertions.assertEquals(orig.getSearchTotalMode(), copy.getSearchTotalMode());
		Assertions.assertEquals(orig.getLastUpdated(), copy.getLastUpdated());
		Assertions.assertEquals(orig.getSearchContainedMode(), copy.getSearchContainedMode());
		Assertions.assertEquals(orig.getEverythingMode(), copy.getEverythingMode());
		Assertions.assertEquals(orig.getSort(), copy.getSort());
		Assertions.assertEquals(orig.get("something"), copy.get("something"));

		// verify changing one does not change the other
		orig.setOffset(100);
		Assertions.assertNotEquals(orig.toNormalizedQueryString(null),
			copy.toNormalizedQueryString(null));
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
		Assertions.assertEquals(orig.size(), clone.size());
		Assertions.assertEquals(orig.get("string"), clone.get("string"));
		Assertions.assertEquals(orig.get("datetime"), clone.get("datetime"));
		Assertions.assertEquals(orig.get("int"), clone.get("int"));
	}
}
