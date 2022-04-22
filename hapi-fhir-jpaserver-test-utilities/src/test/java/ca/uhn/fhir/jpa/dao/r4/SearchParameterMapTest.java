package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

public class SearchParameterMapTest extends BaseTest {
	private static final Logger ourLog = getLogger(SearchParameterMapTest.class);

	private final FhirContext myContext = FhirContext.forR4Cached();

	@Test
	public void toNormalizedQueryStringTest() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|FOO"));
		String criteria = params.toNormalizedQueryString(myContext);
		assertEquals(criteria, "?_has:Observation:subject:identifier=urn%3Asystem%7CFOO");
	}

	@Test
	public void testCloningRecreatesCorrectQueryString() {
		SearchParameterMap params = new SearchParameterMap();

		params.add("_id", new StringOrListParam().addOr(new StringParam("123")).addOr(new StringParam("456")));
		params.add("given", new StringOrListParam().addOr(new StringParam("Gary")).addOr(new StringParam("Ken ")));

		params.setSummaryMode(SummaryEnum.COUNT);

		params.setSort(new SortSpec().setOrder(SortOrderEnum.DESC).setParamName("_id"));

		params.setCount(10);

		params.setLastUpdated(new DateRangeParam(new DateParam("2020-01-01")));

		params.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		String originalQueryString = params.toNormalizedQueryString(myContext);
		ourLog.info("Original query string: {}", originalQueryString);
		SearchParameterMap params2 = params.clone();

		String clonedQueryString = params2.toNormalizedQueryString(myContext);
		ourLog.info("Cloned query string: {}", clonedQueryString);

		assertEquals(originalQueryString, clonedQueryString);
	}
}
