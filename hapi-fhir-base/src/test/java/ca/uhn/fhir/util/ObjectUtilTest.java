package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ObjectUtilTest {

	@Test
	void testEquals() {
		String a = "a";
		String b = "b";
		assertFalse(ObjectUtil.equals(b, a));
		assertFalse(ObjectUtil.equals(a, b));
		assertFalse(ObjectUtil.equals(a, null));
		assertFalse(ObjectUtil.equals(null, a));
		assertTrue(ObjectUtil.equals(null, null));
		assertTrue(ObjectUtil.equals(a, a));
	}
	
	@Test
	void testRequireNonNull() {
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
	void testRequireNotEmpty() {
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

	@Test
	void testCast_isInstance_present() {
		Boolean value = Boolean.FALSE;

		Optional<Boolean> result = ObjectUtil.castIfInstanceof(value, Boolean.class);

		assertThat(result).isPresent();
	}

	@Test
	void testCast_isNotInstance_empty() {
		Boolean value = Boolean.FALSE;

		Optional<Integer> result = ObjectUtil.castIfInstanceof(value, Integer.class);

		assertTrue(result.isEmpty());
	}
}
