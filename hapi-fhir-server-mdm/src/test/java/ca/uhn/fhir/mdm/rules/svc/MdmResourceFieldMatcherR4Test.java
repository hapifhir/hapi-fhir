package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.json.MdmSimilarityJson;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
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

		myComparator = new MdmResourceFieldMatcher(
			ourFhirContext,
			myIMatcherFactory,
			mySimilarityFactory,
			myGivenNameMatchField,
			myMdmRulesJson
		);
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
			.setMatcher(new MdmMatcherJson().setAlgorithm(MatchTypeEnum.EMPTY_FIELD));
		myComparator = new MdmResourceFieldMatcher(
			ourFhirContext,
			myIMatcherFactory,
			mySimilarityFactory,
			myGivenNameMatchField,
			myMdmRulesJson
		);

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
			fail();		} catch (IllegalArgumentException e) {
			assertEquals("Expecting resource type Patient got resource type Encounter", e.getMessage());
		}
		try {
			myComparator.match(myJohn, encounter);
			fail();		} catch (IllegalArgumentException e) {
			assertEquals("Expecting resource type Patient got resource type Encounter", e.getMessage());
		}
	}

	// TODO - what is this supposed to test?
	// it relies on matcher being null (is this a reasonable assumption?)
	// and falls through to similarity check
	@Test
	public void testMatch() {
		assertTrue(myComparator.match(myJohn, myJohny).match);
	}

	@Test
	void match_customRegisteredMatcher_isUsedByFieldMatcher() {
		// Register a custom matcher that always matches
		myIMatcherFactory.register("ALWAYS_TRUE", (theLeftBase, theRightBase, theParams) -> true);

		MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson()
			.setName("custom-given")
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMatcher(new MdmMatcherJson().setAlgorithm("ALWAYS_TRUE"));

		MdmResourceFieldMatcher matcher = new MdmResourceFieldMatcher(
			ourFhirContext,
			myIMatcherFactory,
			mySimilarityFactory,
			fieldMatch,
			myMdmRulesJson
		);

		// Two completely different names should still match via custom matcher
		Patient jane = new Patient();
		jane.addName().addGiven("Jane");
		jane.setId("Patient/3");

		assertThat(matcher.match(myJohn, jane))
			.returns(true, evaluation -> evaluation.match)
			.returns(1.0, evaluation -> evaluation.score);
	}

	@Test
	void match_customRegisteredSimilarity_isUsedByFieldMatcher() {
		// Register a custom similarity that always returns 0.95
		double expectedScore = 0.95;
		mySimilarityFactory.register(
			"FIXED_SCORE", (theFhirContext, theLeftBase, theRightBase, theExact) -> expectedScore);

		MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson()
			.setName("custom-similarity-given")
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setSimilarity(new MdmSimilarityJson().setAlgorithm("FIXED_SCORE").setMatchThreshold(0.9));

		MdmResourceFieldMatcher matcher = new MdmResourceFieldMatcher(
			ourFhirContext,
			myIMatcherFactory,
			mySimilarityFactory,
			fieldMatch,
			myMdmRulesJson
		);

		// Two completely different names should match because custom similarity returns 0.95 > threshold 0.9
		Patient jane = new Patient();
		jane.addName().addGiven("Zzzzzzz");
		jane.setId("Patient/4");

		assertThat(matcher.match(myJohn, jane))
			.returns(true, evaluation -> evaluation.match)
			.returns(expectedScore, evaluation -> evaluation.score);
	}
}
