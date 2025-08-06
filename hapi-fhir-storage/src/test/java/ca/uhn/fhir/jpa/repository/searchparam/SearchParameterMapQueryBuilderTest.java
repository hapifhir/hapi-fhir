package ca.uhn.fhir.jpa.repository.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SearchParameterMapQueryBuilderTest {
	FhirContext myFhirContext = FhirContext.forR4Cached();
	SearchParameterMapRepositoryRestQueryBuilder myBuilder = new SearchParameterMapRepositoryRestQueryBuilder();
	SearchParameterMap myResult;

	@Test
	void testBuilder_SPMapIsUsedAsIs() {
	    // given
		SearchParameterMap spMap = new SearchParameterMap();

		// when
		SearchParameterMap result = SearchParameterMapRepositoryRestQueryBuilder.buildFromQueryContributor(spMap);

	    // then
	    assertThat(result).isSameAs(spMap);
	}

	@Test
	void testBuilder_callbackBuildsSPMap() {
		// given
		IRepository.IRepositoryRestQueryContributor queryContributor =
			builder -> builder.addNumericParameter("_count", 123);

		// when
		SearchParameterMap result = SearchParameterMapRepositoryRestQueryBuilder.buildFromQueryContributor(queryContributor);

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
	void testSort() {
		myBuilder.addOrList("_sort", new TokenParam("-date"));

		myResult = myBuilder.build();

		assertThat(myResult.getSort().getParamName()).isEqualTo("date");
		assertThat(myResult.getSort().getOrder()).isEqualTo(SortOrderEnum.DESC);
	}

	@Test
	void testSortChain() {
		myBuilder.addOrList("_sort", new TokenParam("-date"), new  TokenParam("_id"));

		assertConvertsTo("?_sort=-date,_id");
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

	@Test
	void testContained() {
		// given
		myBuilder.addOrList("_contained", new TokenParam("true"));

		myResult = myBuilder.build();

		assertThat(myResult.getSearchContainedMode()).isEqualTo(SearchContainedModeEnum.TRUE);
	}

	@Test
	void testInclude() {
		// given
		myBuilder.addOrList("_include", new StringParam("Patient:general-practitioner"));

		myResult = myBuilder.build();

		assertThat(myResult.getIncludes()).contains(new Include("Patient:general-practitioner"));
	}


	// fixme we normally put modifiers on values, not keys..
	@Test
	void testIncludeIterate() {
		// given
		myBuilder.addOrList("_include:iterate", new StringParam("Patient:general-practitioner"));

		myResult = myBuilder.build();

		assertThat(myResult.getIncludes()).contains(new Include("Patient:general-practitioner", true));
	}

	@Test
	void testRevinclude() {
		// given
		myBuilder.addOrList("_revinclude", new StringParam("Patient:general-practitioner"));

		myResult = myBuilder.build();

		assertThat(myResult.getRevIncludes()).contains(new Include("Patient:general-practitioner"));
	}

	@Test
	void testRevincludeIterate() {
		// given
		myBuilder.addOrList("_revinclude:iterate", new StringParam("Patient:general-practitioner"));

		myResult = myBuilder.build();

		assertThat(myResult.getRevIncludes()).contains(new Include("Patient:general-practitioner", true));
	}

	private void assertConvertsTo(String expected) {
		myResult = myBuilder.build();
		assertThat(myResult.toNormalizedQueryString(myFhirContext)).isEqualTo(expected);
	}
}
