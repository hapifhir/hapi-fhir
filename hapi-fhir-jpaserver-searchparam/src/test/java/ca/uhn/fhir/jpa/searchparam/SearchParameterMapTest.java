package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.junit.jupiter.api.Test;

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
}
