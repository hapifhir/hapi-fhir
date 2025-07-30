package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.repository.impl.ISearchQueryBuilder;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SearchParameterMapQueryBuilderTest {
	FhirContext myFhirContext = FhirContext.forR4Cached();
	SearchParameterMapQueryBuilder myBuilder = new SearchParameterMapQueryBuilder(myFhirContext);
	SearchParameterMap myResult;

	@Test
	void testBuilder_SPMapIsUsedAsIs() {
	    // given
		SearchParameterMap spMap = new SearchParameterMap();

		// when
		SearchParameterMap result = SearchParameterMapQueryBuilder.buildFromQueryContributor(myFhirContext, spMap);

	    // then
	    assertThat(result).isSameAs(spMap);
	}

	@Test
	void testBuilder_callbackBuildsSPMap() {
		// given
		ISearchQueryBuilder.ISearchQueryContributor queryContributor =
			builder -> builder.addNumericParameter("_count", 123);

		// when
		SearchParameterMap result = SearchParameterMapQueryBuilder.buildFromQueryContributor(myFhirContext, queryContributor);

		// then
		assertThat(result.toNormalizedQueryString(myFhirContext)).isEqualTo("?_count=123");
	}

	@Test
	void  testIdParameter() {
	    // when
		myBuilder.addOrList("_id", new ReferenceParam("123"), new ReferenceParam("345"));

	    // then
		assertConvertsTo("?_id=123,345");
	}

	@Test
	void testCountParameter() {
		// when
		myBuilder.addNumericParameter("_count", 123);

		myResult = myBuilder.build();

		assertThat(myResult.getCount()).isEqualTo(123);
	}


	@Test
	void testMultipleCountUsesLast() {
		// when
		myBuilder.addOrList("_count", new NumberParam("123"), new NumberParam("50"));
		myBuilder.addOrList("_count", new NumberParam("23"), new NumberParam("42"));

		myResult = myBuilder.build();

		assertThat(myResult.getCount()).isEqualTo(42);
	}

	@Test
	void testOffset() {
	    // given
		myBuilder.addOrList("_offset", new NumberParam("900"));

		myResult = myBuilder.build();

		assertThat(myResult.getOffset()).isEqualTo(900);
	}


	@Test
	void testSummary() {
		// given
		myBuilder.addOrList("_summary", new TokenParam("count"));

		myResult = myBuilder.build();

		assertThat(myResult.getSummaryMode()).isEqualTo(SummaryEnum.COUNT);
	}

	@Test
	void testTotal() {
		// given
		myBuilder.addOrList("_total", new TokenParam("accurate"));

		myResult = myBuilder.build();

		assertThat(myResult.getSearchTotalMode()).isEqualTo(SearchTotalModeEnum.ACCURATE);
	}

	private void assertConvertsTo(String expected) {
		myResult = myBuilder.build();
		assertThat(myResult.toNormalizedQueryString(myFhirContext)).isEqualTo(expected);
	}
}
