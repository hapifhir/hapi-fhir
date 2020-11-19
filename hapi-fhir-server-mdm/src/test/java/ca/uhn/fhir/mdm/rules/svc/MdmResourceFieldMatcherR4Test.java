package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmSimilarityJson;
import ca.uhn.fhir.mdm.rules.similarity.MdmSimilarityEnum;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmResourceFieldMatcherR4Test extends BaseMdmRulesR4Test {
	protected MdmResourceFieldMatcher myComparator;
	private Patient myJohn;
	private Patient myJohny;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		myComparator = new MdmResourceFieldMatcher(ourFhirContext, myGivenNameMatchField, myMdmRulesJson);
		myJohn = buildJohn();
		myJohny = buildJohny();
	}

	@Test
	public void testSimplePatient() {
		Patient patient = new Patient();
		patient.setActive(true);

		assertFalse(myComparator.match(patient, myJohny).match);
	}

	@Test
	public void testBadType() {
		Encounter encounter = new Encounter();
		encounter.setId("Encounter/1");

		try {
			myComparator.match(encounter, myJohny);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Expecting resource type Patient got resource type Encounter", e.getMessage());
		}
		try {
			myComparator.match(myJohn, encounter);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Expecting resource type Patient got resource type Encounter", e.getMessage());
		}
	}

	@Test
	public void testBadPath() {
		try {
			MdmFieldMatchJson matchField = new MdmFieldMatchJson()
				.setName("patient-foo")
				.setResourceType("Patient")
				.setResourcePath("foo")
				.setSimilarity(new MdmSimilarityJson().setAlgorithm(MdmSimilarityEnum.COSINE).setMatchThreshold(NAME_THRESHOLD));
			MdmResourceFieldMatcher comparator = new MdmResourceFieldMatcher(ourFhirContext, matchField, myMdmRulesJson);
			comparator.match(myJohn, myJohny);
			fail();
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), startsWith("Unknown child name 'foo' in element Patient"));
		}
	}

	@Test
	public void testMatch() {
		assertTrue(myComparator.match(myJohn, myJohny).match);
	}
}
