package ca.uhn.fhir.rest.server.util;

/*
 * LFJT3 — "Looking Forward to Jackson Tools 3"
 * =============================================
 * Written for Jackson 2 (com.fasterxml.jackson). Structured so that only
 * the two clearly marked sections change during the future LFJT3 uplift:
 *
 *   1. "── LFJT3 JACKSON IMPORT BLOCK ──"
 *   2. "── LFJT3 MAPPER FACTORY ──"
 *
 * All @Test methods are Jackson-version-agnostic and need zero changes.
 *
 * LFJT3 MIGRATION CHECKLIST
 * --------------------------
 * [ ] Imports:
 *       com.fasterxml.jackson.databind.JsonDeserializer  → tools.jackson.databind.ValueDeserializer
 *       com.fasterxml.jackson.databind.JsonSerializer    → tools.jackson.databind.ValueSerializer
 *       com.fasterxml.jackson.databind.ObjectMapper      → tools.jackson.databind.ObjectMapper
 *       com.fasterxml.jackson.databind.module.SimpleModule → tools.jackson.databind.module.SimpleModule
 * [ ] createMapper() factory: new ObjectMapper() → JsonMapper.builder().build()
 * [ ] JsonDateDeserializer.deserialize() loses "throws IOException" in Jackson 3 —
 *     but that change is in the production class, not here.
 * [ ] JsonDateSerializer.serialize() loses "throws IOException" in Jackson 3 —
 *     same — production class change only.
 */

import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;

// ── LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────────
// Jackson 2 (NOW — do not change yet):
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
// Jackson 3 (LFJT3 — swap the three lines above for):
//   import com.fasterxml.jackson.annotation.JsonProperty;   // stays — annotations never move
//   import tools.jackson.databind.ObjectMapper;
//   import tools.jackson.databind.module.SimpleModule;
// ── END LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * LFJT3-aware unit tests for {@link JsonDateDeserializer} and {@link JsonDateSerializer}.
 *
 * <p>Tests run under Jackson 2. The {@link #createMapper()} factory method is the
 * single change point for LFJT3 — see the migration checklist in the file header.
 */
class JsonDateSerializerDeserializerTest {

	private ObjectMapper myMapper;

	// ── LFJT3 MAPPER FACTORY ─────────────────────────────────────────────────
	// Jackson 2 (NOW): new ObjectMapper()
	// Jackson 3 (LFJT3): JsonMapper.builder().build()
	private ObjectMapper createMapper() {
		SimpleModule module = new SimpleModule();
		module.addSerializer(Date.class, new JsonDateSerializer());
		module.addDeserializer(Date.class, new JsonDateDeserializer());
		return new ObjectMapper().registerModule(module);
		// LFJT3: return JsonMapper.builder().addModule(module).build();
	}
	// ── END LFJT3 MAPPER FACTORY ─────────────────────────────────────────────

	@BeforeEach
	void setUp() {
		myMapper = createMapper();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Wrapper for round-trip tests — uses @JsonProperty (stays com.fasterxml in Jackson 3)
	// ─────────────────────────────────────────────────────────────────────────
	static class DateHolder {
		@JsonProperty("date")
		public Date date;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 1. JsonDateSerializer
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("JsonDateSerializer")
	class SerializerTests {

		@Test
		@DisplayName("Serializes Date as ISO-8601 string")
		void serialize_producesIso8601String() throws Exception {
			DateHolder holder = new DateHolder();
			holder.date = new InstantDt("2024-06-01T10:00:00Z").getValue();

			String json = myMapper.writeValueAsString(holder);

			// Value must be a quoted string, not a number
			assertThat(json).contains("\"date\"");
			assertThat(json).contains("2024-06-01");
		}

		@Test
		@DisplayName("Serializes null Date without error")
		void serialize_nullDate_producesNullJson() throws Exception {
			DateHolder holder = new DateHolder();
			holder.date = null;

			String json = assertDoesNotThrow(() -> myMapper.writeValueAsString(holder));
			// null value → null in JSON
			assertThat(json).contains("null");
		}

		@Test
		@DisplayName("Serialized value is parseable by HAPI InstantDt")
		void serialize_producesValueParseableByHapi() throws Exception {
			Date original = new InstantDt("2024-03-15T14:30:00Z").getValue();
			DateHolder holder = new DateHolder();
			holder.date = original;

			String json = myMapper.writeValueAsString(holder);

			// Extract the string value between the quotes after "date":
			// Use Jackson to read it back as a raw string
			String dateValue = myMapper.readTree(json).get("date").asText();

			// Verify HAPI can parse the output
			assertDoesNotThrow(() -> new InstantDt(dateValue),
				"Serialized value must be parseable by HAPI InstantDt");
		}

		@Test
		@DisplayName("Serialized output is not an epoch-millis number")
		void serialize_isNotEpochMillis() throws Exception {
			DateHolder holder = new DateHolder();
			holder.date = new Date();

			String json = myMapper.writeValueAsString(holder);
			String dateNode = myMapper.readTree(json).get("date").toString();

			// Must start with a quote (string), not a digit (epoch)
			assertThat(dateNode).startsWith("\"");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 2. JsonDateDeserializer
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("JsonDateDeserializer")
	class DeserializerTests {

		@Test
		@DisplayName("Deserializes ISO-8601 string to Date")
		void deserialize_iso8601String_returnsDate() throws Exception {
			String json = "{\"date\":\"2024-06-01T10:00:00.000Z\"}";

			DateHolder holder = myMapper.readValue(json, DateHolder.class);

			assertNotNull(holder.date, "Deserialized Date must not be null");
			// Verify round-trip value matches the HAPI-parsed date
			Date expected = new DateTimeDt("2024-06-01T10:00:00.000Z").getValue();
			assertThat(holder.date).isEqualTo(expected);
		}

		@Test
		@DisplayName("Deserializes null JSON value to null Date")
		void deserialize_nullJson_returnsNull() throws Exception {
			String json = "{\"date\":null}";
			DateHolder holder = myMapper.readValue(json, DateHolder.class);
			assertNull(holder.date, "null JSON value must deserialize to null Date");
		}

		@Test
		@DisplayName("Deserializes date-only string (no time component)")
		void deserialize_dateOnlyString_returnsDate() throws Exception {
			String json = "{\"date\":\"2024-06-01\"}";
			DateHolder holder = myMapper.readValue(json, DateHolder.class);
			assertNotNull(holder.date);
		}

		@Test
		@DisplayName("Deserializes empty string to null")
		void deserialize_emptyString_returnsNull() throws Exception {
			String json = "{\"date\":\"\"}";
			DateHolder holder = myMapper.readValue(json, DateHolder.class);
			assertNull(holder.date,
				"Empty string must deserialize to null (matches isNotBlank check in production code)");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 3. Round-trip: Serialize then Deserialize
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Round-trip: serialize → deserialize")
	class RoundTripTests {

		@Test
		@DisplayName("Date survives full round-trip with second precision")
		void roundTrip_secondPrecision() throws Exception {
			Date original = new InstantDt("2024-06-15T08:30:00Z").getValue();
			DateHolder holder = new DateHolder();
			holder.date = original;

			String json = myMapper.writeValueAsString(holder);
			DateHolder reparsed = myMapper.readValue(json, DateHolder.class);

			assertNotNull(reparsed.date);
			// Compare as strings via HAPI to avoid millisecond rounding differences
			String originalStr = new InstantDt(original).getValueAsString();
			String reparsedStr = new InstantDt(reparsed.date).getValueAsString();
			assertThat(reparsedStr).isEqualTo(originalStr);
		}

		@Test
		@DisplayName("Null Date survives full round-trip")
		void roundTrip_nullDate() throws Exception {
			DateHolder holder = new DateHolder();
			holder.date = null;

			String json = myMapper.writeValueAsString(holder);
			DateHolder reparsed = myMapper.readValue(json, DateHolder.class);

			assertNull(reparsed.date);
		}

		@Test
		@DisplayName("Multiple different dates round-trip independently")
		void roundTrip_multipleDates() throws Exception {
			String[] isoStrings = {
				"2020-01-01T00:00:00Z",
				"2024-12-31T23:59:59Z",
				"2000-06-15T12:00:00Z"
			};

			for (String iso : isoStrings) {
				Date original = new InstantDt(iso).getValue();
				DateHolder holder = new DateHolder();
				holder.date = original;

				String json = myMapper.writeValueAsString(holder);
				DateHolder reparsed = myMapper.readValue(json, DateHolder.class);

				assertThat(new InstantDt(reparsed.date).getValueAsString())
					.as("Round-trip failed for: " + iso)
					.isEqualTo(new InstantDt(original).getValueAsString());
			}
		}
	}
}
