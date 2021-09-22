package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.junit.jupiter.api.Test;

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
}
