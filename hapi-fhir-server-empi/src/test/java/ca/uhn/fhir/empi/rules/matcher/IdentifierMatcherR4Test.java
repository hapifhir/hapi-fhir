package ca.uhn.fhir.empi.rules.matcher;

import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiMatcherJson;
import org.hl7.fhir.r4.model.Identifier;
import org.junit.jupiter.api.Test;

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

		EmpiMatcherJson matcher = new EmpiMatcherJson().setAlgorithm(EmpiMatcherEnum.IDENTIFIER).setIdentifierSystem(MATCHING_SYSTEM);
		EmpiFieldMatchJson fieldMatch = new EmpiFieldMatchJson().setMatcher(matcher);

		assertTrue(fieldMatch.match(ourFhirContext, left, right).match);
	}

}
