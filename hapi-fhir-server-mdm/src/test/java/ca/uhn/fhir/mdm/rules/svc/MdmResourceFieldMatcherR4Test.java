package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.json.MdmSimilarityJson;
import ca.uhn.fhir.mdm.rules.matcher.MdmMatcherEnum;
import ca.uhn.fhir.mdm.rules.similarity.MdmSimilarityEnum;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
	public void testEmptyPath() {
		myMdmRulesJson = new MdmRulesJson();
		myMdmRulesJson.setMdmTypes(Arrays.asList(new String[]{"Patient"}));

		myGivenNameMatchField = new MdmFieldMatchJson()
			.setName("empty-given")
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMatcher(new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.EMPTY_FIELD));
		myComparator = new MdmResourceFieldMatcher(ourFhirContext, myGivenNameMatchField, myMdmRulesJson);

		assertFalse(myComparator.match(myJohn, myJohny).match);

		myJohn.getName().clear();
		myJohny.getName().clear();

		assertTrue(myComparator.match(myJohn, myJohny).match);

		myJohn = buildJohn();
		myJohny.getName().clear();
		assertFalse(myComparator.match(myJohn, myJohny).match);

		myJohn.getName().clear();
		myJohny = buildJohny();
		assertFalse(myComparator.match(myJohn, myJohny).match);
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
	public void testMatch() {
		assertTrue(myComparator.match(myJohn, myJohny).match);
	}
}
