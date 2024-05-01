package ca.uhn.fhir.jpa.searchparam;

import static org.junit.jupiter.api.Assertions.assertNull;
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

class SearchParameterMapTest {
	static FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Test
	void toNormalizedQueryStringLower() {
		SearchParameterMap map = new SearchParameterMap();
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setLowerBound("2021-05-31");
		map.setLastUpdated(dateRangeParam);
		assertThat(map.toNormalizedQueryString(ourFhirContext)).isEqualTo("?_lastUpdated=ge2021-05-31");
	}

	@Test
	void toNormalizedQueryString_IncludeNormal() {
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(new Include("Patient:name"));
		assertThat(map.toNormalizedQueryString(ourFhirContext)).isEqualTo("?_include=Patient:name");
	}

	@Test
	void toNormalizedQueryString_IncludeStar() {
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(new Include("*"));
		assertThat(map.toNormalizedQueryString(ourFhirContext)).isEqualTo("?_include=*");
	}

	@Test
	void toNormalizedQueryString_IncludeTypedStar() {
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(new Include("Patient:*"));
		assertThat(map.toNormalizedQueryString(ourFhirContext)).isEqualTo("?_include=Patient:*");
	}

	@Test
	void toNormalizedQueryStringUpper() {
		SearchParameterMap map = new SearchParameterMap();
		DateRangeParam dateRangeParam = new DateRangeParam();
		dateRangeParam.setUpperBound("2021-05-31");
		map.setLastUpdated(dateRangeParam);
		assertThat(map.toNormalizedQueryString(ourFhirContext)).isEqualTo("?_lastUpdated=le2021-05-31");
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
		assertThat(copy.toNormalizedQueryString(null)).isEqualTo(orig.toNormalizedQueryString(null));
		assertThat(copy.getOffset()).isEqualTo(orig.getOffset());
		assertThat(copy.getLoadSynchronousUpTo()).isEqualTo(orig.getLoadSynchronousUpTo());
		assertThat(copy.isLoadSynchronous()).isEqualTo(orig.isLoadSynchronous());
		assertThat(copy.getNearDistanceParam()).isEqualTo(orig.getNearDistanceParam());
		assertThat(copy.getCount()).isEqualTo(orig.getCount());
		assertThat(copy.getLastNMax()).isEqualTo(orig.getLastNMax());
		assertThat(copy.isLastN()).isEqualTo(orig.isLastN());
		assertThat(copy.isDeleteExpunge()).isEqualTo(orig.isDeleteExpunge());
		assertThat(copy.getIncludes()).isEqualTo(orig.getIncludes());
		assertThat(copy.getSearchTotalMode()).isEqualTo(orig.getSearchTotalMode());
		assertThat(copy.getLastUpdated()).isEqualTo(orig.getLastUpdated());
		assertThat(copy.getSearchContainedMode()).isEqualTo(orig.getSearchContainedMode());
		assertThat(copy.getEverythingMode()).isEqualTo(orig.getEverythingMode());
		assertThat(copy.getSort()).isEqualTo(orig.getSort());
		assertThat(copy.get("something")).isEqualTo(orig.get("something"));

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
		assertThat(clone.size()).isEqualTo(orig.size());
		assertThat(clone.get("string")).isEqualTo(orig.get("string"));
		assertThat(clone.get("datetime")).isEqualTo(orig.get("datetime"));
		assertThat(clone.get("int")).isEqualTo(orig.get("int"));
	}


	@Test
	public void testCompareParameters() {

		// Missing
		assertThat(compare(ourFhirContext, new StringParam().setMissing(true), new StringParam().setMissing(true))).isEqualTo(0);
		assertThat(compare(ourFhirContext, new StringParam("A"), new StringParam().setMissing(true))).isEqualTo(-1);
		assertThat(compare(ourFhirContext, new StringParam().setMissing(true), new StringParam("A"))).isEqualTo(1);

		// Qualifier
		assertThat(compare(ourFhirContext, new StringParam("A").setContains(true), new StringParam("A").setContains(true))).isEqualTo(0);
		assertThat(compare(ourFhirContext, new StringParam("A").setContains(true), new StringParam("A"))).isEqualTo(1);
		assertThat(compare(ourFhirContext, new StringParam("A"), new StringParam("A").setContains(true))).isEqualTo(-1);

		// Value
		assertThat(compare(ourFhirContext, new StringParam("A"), new StringParam("A"))).isEqualTo(0);
		assertThat(compare(ourFhirContext, new StringParam("A"), new StringParam("B"))).isEqualTo(-1);
		assertThat(compare(ourFhirContext, new StringParam("B"), new StringParam("A"))).isEqualTo(1);

		// Value + Comparator (value should have no effect if comparator is changed)
		assertThat(compare(ourFhirContext, new StringParam("B").setContains(true), new StringParam("A"))).isEqualTo(1);
		assertThat(compare(ourFhirContext, new StringParam("A").setContains(true), new StringParam("B"))).isEqualTo(1);

	}



}
