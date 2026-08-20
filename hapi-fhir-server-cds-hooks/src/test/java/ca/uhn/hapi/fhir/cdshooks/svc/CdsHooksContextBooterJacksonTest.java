package ca.uhn.hapi.fhir.cdshooks.svc;

/*
 * LFJT3 — "Looking Forward to Jackson Tools 3"
 * =============================================
 * Written for Jackson 2 (com.fasterxml.jackson). Only the import block
 * and createMapper() factory method change during the future LFJT3 uplift.
 *
 * LFJT3 MIGRATION CHECKLIST
 * --------------------------
 * [ ] Imports: com.fasterxml.jackson.* → tools.jackson.*
 * [ ] createMapper(): new ObjectMapper() → JsonMapper.builder().build()
 *
 * KEY BEHAVIORS LOCKED DOWN (CdsHooksContextBooter.serializeExtensions)
 * -----------------------------------------------------------------------
 * - Valid JSON string → CdsHooksExtension subclass instance
 * - Empty/blank string → null (no deserialization attempted)
 * - Invalid JSON → UnprocessableEntityException
 * - Null extensionClass → handled upstream (not tested here)
 *
 * NOTE: serializeExtensions() uses `new ObjectMapper()` internally.
 * In LFJT3 that becomes `JsonMapper.builder().build()`. The behavior under
 * test is Jackson-version-agnostic: the output is the deserialized object,
 * not the wire format.
 */

import ca.uhn.fhir.rest.api.server.cdshooks.CdsHooksExtension;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.fasterxml.jackson.annotation.JsonProperty;
// ── LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────────
// Jackson 2 (NOW):
import com.fasterxml.jackson.databind.ObjectMapper;
// Jackson 3 (LFJT3):
//   import tools.jackson.databind.ObjectMapper;
// ── END LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * LFJT3-aware tests for the Jackson-relevant behavior in
 * {@link CdsHooksContextBooter#serializeExtensions(String, Class)}.
 */
class CdsHooksContextBooterJacksonTest {

	private CdsHooksContextBooter myBooter;

	// ── LFJT3 MAPPER FACTORY ─────────────────────────────────────────────────
	// Not used directly in this test (the booter creates its own mapper internally),
	// but retained here to document the LFJT3 change point clearly.
	// When LFJT3 uplift lands in CdsHooksContextBooter.serializeExtensions(),
	// the internal `new ObjectMapper()` becomes `JsonMapper.builder().build()`.
	// Jackson 2 (NOW): new ObjectMapper()
	// Jackson 3 (LFJT3): JsonMapper.builder().build()
	private ObjectMapper createMapper() {
		return new ObjectMapper();
		// LFJT3: return JsonMapper.builder().build();
	}
	// ── END LFJT3 MAPPER FACTORY ─────────────────────────────────────────────

	/** Minimal CdsHooksExtension subclass for testing. */
	static class TestExtension extends CdsHooksExtension {
		@JsonProperty("configItem")
		// @JsonProperty stays on com.fasterxml.jackson.annotation — never moves
		private String configItem;

		public String getConfigItem() { return configItem; }
		public void setConfigItem(String theConfigItem) { configItem = theConfigItem; }
	}

	@BeforeEach
	void setUp() {
		myBooter = new CdsHooksContextBooter();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 1. serializeExtensions — valid JSON
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("serializeExtensions — valid JSON")
	class ValidJsonTests {

		@Test
		@DisplayName("Valid JSON deserializes to the correct extension type")
		void serializeExtensions_validJson_returnsExtensionInstance() {
			String json = "{\"configItem\":\"test-value\"}";

			CdsHooksExtension result = myBooter.serializeExtensions(json, TestExtension.class);

			assertThat(result).isInstanceOf(TestExtension.class);
		}

		@Test
		@DisplayName("Extension field value is correctly deserialized")
		void serializeExtensions_validJson_fieldValueCorrect() {
			String json = "{\"configItem\":\"hello-world\"}";

			TestExtension result = (TestExtension) myBooter.serializeExtensions(json, TestExtension.class);

			assertThat(result.getConfigItem()).isEqualTo("hello-world");
		}

		@Test
		@DisplayName("Valid JSON with unknown fields does not throw")
		void serializeExtensions_unknownFields_doesNotThrow() {
			String json = "{\"configItem\":\"val\",\"unknownField\":\"ignored\"}";

			// Jackson ignores unknown fields by default — must still succeed
			CdsHooksExtension result = myBooter.serializeExtensions(json, TestExtension.class);

			assertThat(result).isNotNull();
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 2. serializeExtensions — empty / blank input
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("serializeExtensions — empty/blank input")
	class EmptyInputTests {

		@Test
		@DisplayName("Empty string returns null (isEmpty catches empty string)")
		void serializeExtensions_emptyString_returnsNull() {
			// StringUtils.isEmpty("") == true → returns null before deserialization
			CdsHooksExtension result = myBooter.serializeExtensions("", TestExtension.class);
			assertNull(result);
		}

		@Test
		@DisplayName("Null string returns null (isEmpty catches null)")
		void serializeExtensions_nullString_returnsNull() {
			// StringUtils.isEmpty(null) == true → returns null before deserialization
			CdsHooksExtension result = myBooter.serializeExtensions(null, TestExtension.class);
			assertNull(result);
		}

		@Test
		@DisplayName("Blank/whitespace string throws UnprocessableEntityException")
		void serializeExtensions_blankString_throwsUnprocessableEntity() {
			// StringUtils.isEmpty("   ") == FALSE — whitespace passes the guard
			// and reaches mapper.readValue(), which fails on non-JSON input
			// LFJT3 NOTE: this behavior is unchanged — isEmpty() guard remains the same
			assertThrows(UnprocessableEntityException.class,
				() -> myBooter.serializeExtensions("   ", TestExtension.class));
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 3. serializeExtensions — invalid JSON
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("serializeExtensions — invalid JSON")
	class InvalidJsonTests {

		@Test
		@DisplayName("Malformed JSON throws UnprocessableEntityException")
		void serializeExtensions_malformedJson_throwsUnprocessableEntity() {
			String badJson = "{this is not valid json}";

			assertThrows(UnprocessableEntityException.class,
				() -> myBooter.serializeExtensions(badJson, TestExtension.class));
		}

		@Test
		@DisplayName("Plain text (not JSON) throws UnprocessableEntityException")
		void serializeExtensions_plainText_throwsUnprocessableEntity() {
			assertThrows(UnprocessableEntityException.class,
				() -> myBooter.serializeExtensions("hello world", TestExtension.class));
		}
	}
}
