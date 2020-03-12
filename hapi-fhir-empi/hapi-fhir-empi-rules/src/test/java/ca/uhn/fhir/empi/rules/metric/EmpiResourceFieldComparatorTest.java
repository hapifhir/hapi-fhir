package ca.uhn.fhir.empi.rules.metric;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.rules.EmpiMatchFieldJson;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

public class EmpiResourceFieldComparatorTest extends BaseTest {
	private Patient myPatient1;
	private Patient myPatient2;
	private EmpiMatchFieldJson myGivenNameMatchField;
	private EmpiResourceFieldComparator myComparator;

	@Before
	public void before() {
		myGivenNameMatchField = new EmpiMatchFieldJson("Patient", "name.given", DistanceMetricEnum.COSINE);
		myComparator = new EmpiResourceFieldComparator(ourFhirContext, myGivenNameMatchField);

		initializePatients();
	}

	private void initializePatients() {
		myPatient1 = new Patient();
		myPatient1.addName().addGiven("John");
		myPatient1.setId("Patient/1");
		myPatient2 = new Patient();
		myPatient2.addName().addGiven("Johny");
		myPatient2.setId("Patient/2");
	}

	@Test
	public void testBadType() {
		Encounter encounter = new Encounter();
		encounter.setId("Encounter/1");
		try {
			myComparator.compare(encounter, myPatient2);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Expecting resource type Patient got resource type Encounter", e.getMessage());
		}
		try {
			myComparator.compare(myPatient1, encounter);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Expecting resource type Patient got resource type Encounter", e.getMessage());
		}
	}

	@Test
	public void testBadPath() {
		try {
			EmpiMatchFieldJson matchField = new EmpiMatchFieldJson("Patient", "foo", DistanceMetricEnum.COSINE);
			EmpiResourceFieldComparator comparator = new EmpiResourceFieldComparator(ourFhirContext, matchField);
			comparator.compare(myPatient1, myPatient2);
			fail();
		} catch (DataFormatException e) {
			assertThat( e.getMessage(), startsWith("Unknown child name 'foo' in element Patient"));
		}
	}

	@Test
	public void testCompare() {
		assertEquals(0.8165, myComparator.compare(myPatient1, myPatient2), 0.0001);
	}
}
