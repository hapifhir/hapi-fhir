package ca.uhn.fhir.jpaserver.rules;

import ca.uhn.fhir.jpaserver.BaseTest;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmpiResourceComparatorTest extends BaseTest {
	private EmpiResourceComparator myEmpiResourceComparator;
	@Before
	public void before() {
		super.before();
		myEmpiResourceComparator = new EmpiResourceComparator(ourFhirContext, myRules);
	}

	@Test
	public void testCompareFirstNameMatch() {
		double result = myEmpiResourceComparator.compare(myPatient1, myPatient2);
		assertEquals(EXPECTED_FIRST_NAME_WEIGHT, result, NAME_DELTA);
	}

	@Test
	public void testCompareBothNamesMatch() {
		myPatient1.addName().setFamily("Smith");
		myPatient2.addName().setFamily("Smith");
		double result = myEmpiResourceComparator.compare(myPatient1, myPatient2);
		assertEquals(EXPECTED_BOTH_NAMES_WEIGHT, result, NAME_DELTA);
	}

	@Test
	public void testMatchResult() {
		assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, myEmpiResourceComparator.getMatchResult(myPatient1, myPatient2));
		myPatient1.addName().setFamily("Smith");
		myPatient2.addName().setFamily("Smith");
		assertEquals(EmpiMatchResultEnum.MATCH, myEmpiResourceComparator.getMatchResult(myPatient1, myPatient2));
		Patient patient3 = new Patient();
		patient3.setId("Patient/3");
		patient3.addName().addGiven("Henry");
		assertEquals(EmpiMatchResultEnum.NO_MATCH, myEmpiResourceComparator.getMatchResult(myPatient1, patient3));
	}
}
