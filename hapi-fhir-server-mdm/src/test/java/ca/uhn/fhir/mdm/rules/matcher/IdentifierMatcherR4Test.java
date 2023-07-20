package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.IdentifierMatcher;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IdentifierMatcherR4Test extends BaseMatcherR4Test {
	public static final String MATCHING_SYSTEM = "http://match";
	public static final String OTHER_SYSTEM = "http://other";
	private static final String MATCHING_VALUE = "matchme";
	private static final String OTHER_VALUE = "strange";

	private IdentifierMatcher myIdentifierMatcher;

	@BeforeEach
	public void before() {
		super.before();
		myIdentifierMatcher = new IdentifierMatcher();
	}

	@Test
	public void testIdentifierMatch() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);
		Identifier right = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);

		assertTrue(match(left, right));
	}

	@Test
	public void testIdentifierNoMatch() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);
		Identifier rightWrongSystem = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);
		Identifier rightWrongValue = new Identifier().setSystem(MATCHING_SYSTEM).setValue(OTHER_VALUE);
		Identifier rightNoSystem = new Identifier().setValue(MATCHING_VALUE);
		Identifier rightNoValue = new Identifier().setSystem(MATCHING_SYSTEM);


		assertFalse(match(left, rightWrongSystem));
		assertFalse(match(left, rightWrongValue));
		assertFalse(match(left, rightNoSystem));
		assertFalse(match(left, rightNoValue));
		assertFalse(match(rightWrongSystem, left));
		assertFalse(match(rightWrongValue, left));
		assertFalse(match(rightNoSystem, left));
		assertFalse(match(rightNoValue, left));
	}

	@Test
	public void testIdentifierMatchWithNoValues() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM);
		Identifier right = new Identifier().setSystem(MATCHING_SYSTEM);

		myMdmMatcherJson.setIdentifierSystem(MATCHING_SYSTEM);

		assertFalse(match(left, right));
	}

	@Test
	public void testIdentifierNamedSystemMatch() {
		Identifier left = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);
		Identifier right = new Identifier().setSystem(MATCHING_SYSTEM).setValue(MATCHING_VALUE);

		myMdmMatcherJson.setIdentifierSystem(MATCHING_SYSTEM);

		assertTrue(match(left, right));
	}

	@Test
	public void testIdentifierSystemNoMatch() {
		Identifier left = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);
		Identifier right = new Identifier().setSystem(OTHER_SYSTEM).setValue(MATCHING_VALUE);

		myMdmMatcherJson.setIdentifierSystem(MATCHING_SYSTEM);

		assertFalse(match(left, right));
	}

	private boolean match(IBase theFirst, IBase theSecond) {
		return myIdentifierMatcher.matches(theFirst, theSecond, myMdmMatcherJson);
	}
}
