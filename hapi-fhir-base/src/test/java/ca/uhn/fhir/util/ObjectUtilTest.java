package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.*;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

public class ObjectUtilTest {

	@Test
	public void testEquals() {
		String a = new String("a");
		String b = new String("b");
		assertFalse(ObjectUtil.equals(b, a));
		assertFalse(ObjectUtil.equals(a, b));
		assertFalse(ObjectUtil.equals(a, null));
		assertFalse(ObjectUtil.equals(null, a));
		assertTrue(ObjectUtil.equals(null, null));
		assertTrue(ObjectUtil.equals(a, a));
	}
	
	@Test
	public void testRequireNonNull() {
		String message = "Must not be null in test";
		try {
			ObjectUtil.requireNonNull(null, message);
			fail("should not get here.");
		} catch (NullPointerException e) {
			assertEquals(Msg.code(1776) + message, e.getMessage());
		}
		assertNotNull(ObjectUtil.requireNonNull("some string", message));
	}
	
	@Test
	public void testRequireNotEmpty() {
		//All these are empty, null or whitespace strings.
		testRequireNotEmptyErrorScenario(null);
		testRequireNotEmptyErrorScenario("");
		testRequireNotEmptyErrorScenario(" ");
		testRequireNotEmptyErrorScenario("  ");
		//All these are non empty, some non whitespace char in the string.
		ObjectUtil.requireNotEmpty("abc ", "");
		ObjectUtil.requireNotEmpty(" abc ", "");
		ObjectUtil.requireNotEmpty(" abc", "");
		
	}

	private void testRequireNotEmptyErrorScenario(String string) {
		String message = "must not be empty in test";
		try {
			ObjectUtil.requireNotEmpty(string, message);
			fail("should not get here.");
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1777) + message, e.getMessage());
		}
	}
	
}
