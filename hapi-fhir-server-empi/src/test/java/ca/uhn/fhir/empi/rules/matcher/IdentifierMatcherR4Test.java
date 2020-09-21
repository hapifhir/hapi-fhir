package ca.uhn.fhir.empi.rules.matcher;

import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiMatcherJson;
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

		EmpiMatcherJson matcher = new EmpiMatcherJson().setAlgorithm(EmpiMatcherEnum.IDENTIFIER);
		EmpiFieldMatchJson fieldMatch = new EmpiFieldMatchJson().setMatcher(matcher);

		assertTrue(fieldMatch.match(ourFhirContext, left, right).match);
	}

	@Test
	public void testIdentifierNoMatch() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);
		Identifier rightWrongSystem = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);
		Identifier rightWrongValue = new Identifier().setSystem(MATCHING_SYSTEM).setValue(OTHER_VALUE);
		Identifier rightNoSystem = new Identifier().setValue(MATCHING_VALUE);
		Identifier rightNoValue = new Identifier().setSystem(MATCHING_SYSTEM);

		EmpiMatcherJson matcher = new EmpiMatcherJson().setAlgorithm(EmpiMatcherEnum.IDENTIFIER);
		EmpiFieldMatchJson fieldMatch = new EmpiFieldMatchJson().setMatcher(matcher);

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
	public void testIdentifierNamedSystemMatch() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);
		Identifier right = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);

		EmpiMatcherJson matcher = new EmpiMatcherJson().setAlgorithm(EmpiMatcherEnum.IDENTIFIER).setIdentifierSystem(MATCHING_SYSTEM);
		EmpiFieldMatchJson fieldMatch = new EmpiFieldMatchJson().setMatcher(matcher);

		assertTrue(fieldMatch.match(ourFhirContext, left, right).match);
	}

	@Test
	public void testIdentifierSystemNoMatch() {
		Identifier left = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);
		Identifier right = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);

		EmpiMatcherJson matcher = new EmpiMatcherJson().setAlgorithm(EmpiMatcherEnum.IDENTIFIER).setIdentifierSystem(MATCHING_SYSTEM);
		EmpiFieldMatchJson fieldMatch = new EmpiFieldMatchJson().setMatcher(matcher);

		assertFalse(fieldMatch.match(ourFhirContext, left, right).match);
	}
}
