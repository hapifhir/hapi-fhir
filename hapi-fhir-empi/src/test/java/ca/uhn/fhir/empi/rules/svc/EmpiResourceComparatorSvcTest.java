package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmpiResourceComparatorSvcTest extends BaseTest {
	private EmpiResourceComparatorSvc myEmpiResourceComparatorSvc;
	public static final double NAME_DELTA = 0.0001;

	private Patient myJohn;
	private Patient myJohny;

	@Before
	public void before() {
		super.before();

		myEmpiResourceComparatorSvc = buildComparator(buildActiveBirthdateIdRules());

		myJohn = buildJohn();
		myJohny = buildJohny();
	}

	@Test
	public void testCompareFirstNameMatch() {
		EmpiMatchResultEnum result = myEmpiResourceComparatorSvc.compare(myJohn, myJohny);
		assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, result);
	}

	@Test
	public void testCompareBothNamesMatch() {
		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("Smith");
		EmpiMatchResultEnum result = myEmpiResourceComparatorSvc.compare(myJohn, myJohny);
		assertEquals(EmpiMatchResultEnum.MATCH, result);
	}

	@Test
	public void testMatchResult() {
		assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, myEmpiResourceComparatorSvc.getMatchResult(myJohn, myJohny));
		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("Smith");
		assertEquals(EmpiMatchResultEnum.MATCH, myEmpiResourceComparatorSvc.getMatchResult(myJohn, myJohny));
		Patient patient3 = new Patient();
		patient3.setId("Patient/3");
		patient3.addName().addGiven("Henry");
		assertEquals(EmpiMatchResultEnum.NO_MATCH, myEmpiResourceComparatorSvc.getMatchResult(myJohn, patient3));
	}
}
