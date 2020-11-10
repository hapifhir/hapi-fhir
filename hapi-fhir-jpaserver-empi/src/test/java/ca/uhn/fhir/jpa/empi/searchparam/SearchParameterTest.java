package ca.uhn.fhir.jpa.empi.searchparam;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchParameterTest extends BaseEmpiR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SearchParameterTest.class);

	@BeforeEach
	public void before() {
		super.loadEmpiSearchParameters();
	}

	/**
	 * TODO GGG MDM ask ken if we still need this search parameter.
	 * The implementation this test tests will instead need to rely on MPI_LINK table?
	 */
	/*
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
	*/
}
