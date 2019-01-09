package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.HasParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestR4Config.class})
public class SearchParameterMapTest {
	@Autowired
	FhirContext myContext;

	@Test
	public void toNormalizedQueryStringTest() {
		SearchParameterMap params = new SearchParameterMap();
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|FOO"));
		String criteria = params.toNormalizedQueryString(myContext);
		assertEquals(criteria, "?_has:Observation:identifier:urn:system|FOO=urn%3Asystem%7CFOO");
	}
}
