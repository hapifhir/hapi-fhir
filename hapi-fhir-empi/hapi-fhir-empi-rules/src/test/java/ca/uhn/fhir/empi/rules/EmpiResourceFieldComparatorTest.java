package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.r4.model.Encounter;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

public class EmpiResourceFieldComparatorTest extends BaseTest {
	protected EmpiResourceFieldComparator myComparator;

	@Before
	public void before() {
		super.before();
		myComparator = new EmpiResourceFieldComparator(ourFhirContext, myGivenNameMatchField);
	}

	@Test
	public void testBadType() {
		Encounter encounter = new Encounter();
		encounter.setId("Encounter/1");
		try {
			myComparator.match(encounter, myPatient2);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Expecting resource type Patient got resource type Encounter", e.getMessage());
		}
		try {
			myComparator.match(myPatient1, encounter);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Expecting resource type Patient got resource type Encounter", e.getMessage());
		}
	}

	@Test
	public void testBadPath() {
		try {
			EmpiFieldMatchJson matchField = new EmpiFieldMatchJson("patient-foo", "Patient", "foo", DistanceMetricEnum.COSINE, NAME_THRESHOLD);
			EmpiResourceFieldComparator comparator = new EmpiResourceFieldComparator(ourFhirContext, matchField);
			comparator.match(myPatient1, myPatient2);
			fail();
		} catch (DataFormatException e) {
			assertThat( e.getMessage(), startsWith("Unknown child name 'foo' in element Patient"));
		}
	}

	@Test
	public void testMatch() {
		assertTrue(myComparator.match(myPatient1, myPatient2));
	}
}
