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
		assertEquals(NAME_SIMILARITY, myComparator.compare(myPatient1, myPatient2), NAME_DELTA);
	}
}
