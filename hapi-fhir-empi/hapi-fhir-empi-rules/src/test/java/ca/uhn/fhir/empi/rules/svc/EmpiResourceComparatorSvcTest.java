package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmpiResourceComparatorSvcTest extends BaseTest {
	private EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;

	@Before
	public void before() {
		super.before();
		myEmpiResourceComparatorSvc = new EmpiResourceComparatorSvc(ourFhirContext, new EmpiRulesSvc(myRules));
		myEmpiResourceComparatorSvc.init();
	}

	@Test
	public void testCompareFirstNameMatch() {
		double result = myEmpiResourceComparatorSvc.compare(myPatient1, myPatient2);
		assertEquals(EXPECTED_FIRST_NAME_WEIGHT, result, NAME_DELTA);
	}

	@Test
	public void testCompareBothNamesMatch() {
		myPatient1.addName().setFamily("Smith");
		myPatient2.addName().setFamily("Smith");
		double result = myEmpiResourceComparatorSvc.compare(myPatient1, myPatient2);
		assertEquals(EXPECTED_BOTH_NAMES_WEIGHT, result, NAME_DELTA);
	}

	@Test
	public void testMatchResult() {
		assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, myEmpiResourceComparatorSvc.getMatchResult(myPatient1, myPatient2));
		myPatient1.addName().setFamily("Smith");
		myPatient2.addName().setFamily("Smith");
		assertEquals(EmpiMatchResultEnum.MATCH, myEmpiResourceComparatorSvc.getMatchResult(myPatient1, myPatient2));
		Patient patient3 = new Patient();
		patient3.setId("Patient/3");
		patient3.addName().addGiven("Henry");
		assertEquals(EmpiMatchResultEnum.NO_MATCH, myEmpiResourceComparatorSvc.getMatchResult(myPatient1, patient3));
	}
}
