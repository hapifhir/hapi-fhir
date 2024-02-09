package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class ObjectUtilTest {

	@Test
	void testEquals() {
		String a = "a";
		String b = "b";
		assertThat(ObjectUtil.equals(b, a)).isFalse();
		assertThat(ObjectUtil.equals(a, b)).isFalse();
		assertThat(ObjectUtil.equals(a, null)).isFalse();
		assertThat(ObjectUtil.equals(null, a)).isFalse();
		assertThat(ObjectUtil.equals(null, null)).isTrue();
		assertThat(ObjectUtil.equals(a, a)).isTrue();
	}
	
	@Test
	void testRequireNonNull() {
		String message = "Must not be null in test";
		try {
			ObjectUtil.requireNonNull(null, message);
			fail("should not get here.");
		} catch (NullPointerException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1776) + message);
		}
		assertThat(ObjectUtil.requireNonNull("some string", message)).isNotNull();
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
			assertThat(e.getMessage()).isEqualTo(Msg.code(1777) + message);
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

		assertThat(result.isEmpty()).isTrue();
	}
}
