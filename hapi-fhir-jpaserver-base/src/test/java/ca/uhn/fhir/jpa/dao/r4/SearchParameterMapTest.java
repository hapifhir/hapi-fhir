package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.test.BaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchParameterMapTest extends BaseTest {

	private FhirContext myContext = FhirContext.forR4();

	@Test
	public void toNormalizedQueryStringTest() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|FOO"));
		String criteria = params.toNormalizedQueryString(myContext);
		assertEquals(criteria, "?_has:Observation:identifier:urn:system|FOO=urn%3Asystem%7CFOO");
	}

}
