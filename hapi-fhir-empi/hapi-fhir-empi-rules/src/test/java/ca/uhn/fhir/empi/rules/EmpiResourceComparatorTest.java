package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.empi.BaseTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmpiResourceComparatorTest extends BaseTest {
	private EmpiResourceComparator myEmpiResourceComparator;
	@Before
	public void before() {
		super.before();
		myEmpiResourceComparator = new EmpiResourceComparator(ourFhirContext, myEmpiRules);
	}

	@Test
	public void testCompare() {
		double result = myEmpiResourceComparator.compare(myPatient1, myPatient2);
		assertEquals(NAME_SIMILARITY, result, NAME_DELTA);
	}

}
