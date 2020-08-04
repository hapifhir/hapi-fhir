package ca.uhn.fhir.jpa.empi.searchparam;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchParameterTest extends BaseEmpiR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParameterTest.class);

	@BeforeEach
	public void before() {
		super.loadEmpiSearchParameters();
	}

	@Test
	public void testCanFindPossibleMatches() {
		// Create a possible match
		Patient patient = buildJanePatient();
		patient.getNameFirstRep().setFamily("familyone");
		patient = createPatientAndUpdateLinks(patient);

		Patient patient2 = buildJanePatient();
		patient2.getNameFirstRep().setFamily("pleasedonotmatchatall");
		patient2 = createPatientAndUpdateLinks(patient2);

		assertThat(patient2, is(possibleMatchWith(patient)));
		// Now confirm we can find it using our custom search parameter

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("assurance", new TokenParam(Person.IdentityAssuranceLevel.LEVEL2.toCode()));
		IBundleProvider result = myPersonDao.search(map);

		assertEquals(1, result.size().intValue());
		Person person = (Person) result.getResources(0, 1).get(0);
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(person);
		ourLog.info("Search result: {}", encoded);
		List<Person.PersonLinkComponent> links = person.getLink();
		assertEquals(2, links.size());
		assertEquals(Person.IdentityAssuranceLevel.LEVEL2, links.get(0).getAssurance());
		assertEquals(Person.IdentityAssuranceLevel.LEVEL1, links.get(1).getAssurance());
	}
}
