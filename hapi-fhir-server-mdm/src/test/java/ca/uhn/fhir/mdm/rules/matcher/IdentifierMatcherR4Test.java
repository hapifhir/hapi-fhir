package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import org.hl7.fhir.r4.model.Identifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IdentifierMatcherR4Test extends BaseMatcherR4Test {
	public static final String MATCHING_SYSTEM = "http://match";
	public static final String OTHER_SYSTEM = "http://other";
	private static final String MATCHING_VALUE = "matchme";
	private static final String OTHER_VALUE = "strange";

	@Test
	public void testIdentifierMatch() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);
		Identifier right = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);

		MdmMatcherJson matcher = new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.IDENTIFIER);
		MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson().setMatcher(matcher);

		assertTrue(fieldMatch.match(ourFhirContext, left, right).match);
	}

	@Test
	public void testIdentifierNoMatch() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);
		Identifier rightWrongSystem = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);
		Identifier rightWrongValue = new Identifier().setSystem(MATCHING_SYSTEM).setValue(OTHER_VALUE);
		Identifier rightNoSystem = new Identifier().setValue(MATCHING_VALUE);
		Identifier rightNoValue = new Identifier().setSystem(MATCHING_SYSTEM);

		MdmMatcherJson matcher = new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.IDENTIFIER);
		MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson().setMatcher(matcher);

		assertFalse(fieldMatch.match(ourFhirContext, left, rightWrongSystem).match);
		assertFalse(fieldMatch.match(ourFhirContext, left, rightWrongValue).match);
		assertFalse(fieldMatch.match(ourFhirContext, left, rightNoSystem).match);
		assertFalse(fieldMatch.match(ourFhirContext, left, rightNoValue).match);
		assertFalse(fieldMatch.match(ourFhirContext, rightWrongSystem, left).match);
		assertFalse(fieldMatch.match(ourFhirContext, rightWrongValue, left).match);
		assertFalse(fieldMatch.match(ourFhirContext, rightNoSystem, left).match);
		assertFalse(fieldMatch.match(ourFhirContext, rightNoValue, left).match);
	}

	@Test
	public void testIdentifierMatchWithNoValues() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM);
		Identifier right = new Identifier().setSystem(MATCHING_SYSTEM);

		MdmMatcherJson matcher = new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.IDENTIFIER).setIdentifierSystem(MATCHING_SYSTEM);
		MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson().setMatcher(matcher);

		assertFalse(fieldMatch.match(ourFhirContext, left, right).match);
	}

	@Test
	public void testIdentifierNamedSystemMatch() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);
		Identifier right = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);

		MdmMatcherJson matcher = new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.IDENTIFIER).setIdentifierSystem(MATCHING_SYSTEM);
		MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson().setMatcher(matcher);

		assertTrue(fieldMatch.match(ourFhirContext, left, right).match);
	}

	@Test
	public void testIdentifierSystemNoMatch() {
		Identifier left = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);
		Identifier right = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);

		MdmMatcherJson matcher = new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.IDENTIFIER).setIdentifierSystem(MATCHING_SYSTEM);
		MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson().setMatcher(matcher);

		assertFalse(fieldMatch.match(ourFhirContext, left, right).match);
	}
}
