package ca.uhn.fhir.jpa.binary.api;

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
 *       com.fasterxml.jackson.databind.ObjectMapper      → tools.jackson.databind.ObjectMapper
 *       com.fasterxml.jackson.databind.JsonNode          → tools.jackson.databind.JsonNode
 *       NOTE: com.fasterxml.jackson.annotation.*         stays unchanged in Jackson 3
 *             (@JsonProperty, @JsonSerialize, @JsonDeserialize on the production class)
 *             BUT @JsonSerialize/@JsonDeserialize are in com.fasterxml.jackson.databind.annotation
 *             and THOSE move to tools.jackson.databind.annotation (production class change)
 * [ ] createMapper() factory: new ObjectMapper() → JsonMapper.builder().build()
 *
 * KEY CONTRACT THIS TEST LOCKS DOWN
 * ----------------------------------
 * StoredDetails uses @JsonSerialize(using=JsonDateSerializer.class) and
 * @JsonDeserialize(using=JsonDateDeserializer.class) on the `published` field.
 * After LFJT3 uplift, that annotation package moves but the round-trip behavior
 * must remain identical. These tests lock down that behavior.
 */

// ── LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────────
// Jackson 2 (NOW):
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
// Jackson 3 (LFJT3):
//   import tools.jackson.databind.JsonNode;
//   import tools.jackson.databind.ObjectMapper;
// ── END LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * LFJT3-aware unit tests for {@link StoredDetails}.
 *
 * <p>Focuses on Jackson serialization/deserialization behavior — particularly
 * the custom Date serializer/deserializer on the {@code published} field, and
 * the legacy {@code blobId} → {@code binaryContentId} backward-compat field.
 */
class StoredDetailsTest {

	// ── LFJT3 MAPPER FACTORY ─────────────────────────────────────────────────
	// Jackson 2 (NOW): new ObjectMapper()
	// Jackson 3 (LFJT3): JsonMapper.builder().build()
	private ObjectMapper createMapper() {
		return new ObjectMapper();
		// LFJT3: return JsonMapper.builder().build();
	}
	// ── END LFJT3 MAPPER FACTORY ─────────────────────────────────────────────

	// ─────────────────────────────────────────────────────────────────────────
	// Helpers
	// ─────────────────────────────────────────────────────────────────────────

	private StoredDetails buildStoredDetails(String binaryContentId, String contentType, Date published)
		throws IOException {
		byte[] data = "test-binary-data".getBytes(StandardCharsets.UTF_8);
		HashingInputStream his = new HashingInputStream(
			Hashing.sha256(), new ByteArrayInputStream(data));
		IOUtils.consume(his);
		return new StoredDetails(binaryContentId, data.length, contentType, his, published);
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 1. Basic serialization structure
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Serialization structure")
	class SerializationStructureTests {

		@Test
		@DisplayName("Serialized JSON contains expected top-level keys")
		void serialize_containsExpectedKeys() throws Exception {
			ObjectMapper mapper = createMapper();
			StoredDetails details = buildStoredDetails("binary-001", "application/pdf", new Date());

			JsonNode root = mapper.readTree(mapper.writeValueAsString(details));

			assertThat(root.has("binaryContentId")).isTrue();
			assertThat(root.has("bytes")).isTrue();
			assertThat(root.has("contentType")).isTrue();
			assertThat(root.has("hash")).isTrue();
			assertThat(root.has("published")).isTrue();
		}

		@Test
		@DisplayName("binaryContentId is serialized correctly")
		void serialize_binaryContentId() throws Exception {
			ObjectMapper mapper = createMapper();
			StoredDetails details = buildStoredDetails("my-binary-id", "image/png", null);

			JsonNode root = mapper.readTree(mapper.writeValueAsString(details));

			assertThat(root.get("binaryContentId").asText()).isEqualTo("my-binary-id");
		}

		@Test
		@DisplayName("bytes field contains the correct byte count")
		void serialize_bytesField() throws Exception {
			ObjectMapper mapper = createMapper();
			StoredDetails details = buildStoredDetails("id-1", "text/plain", null);

			JsonNode root = mapper.readTree(mapper.writeValueAsString(details));

			// "test-binary-data" is 16 bytes
			assertThat(root.get("bytes").asLong()).isEqualTo(16L);
		}

		@Test
		@DisplayName("hash field is non-empty after construction")
		void serialize_hashFieldPresent() throws Exception {
			ObjectMapper mapper = createMapper();
			StoredDetails details = buildStoredDetails("id-1", "text/plain", null);

			JsonNode root = mapper.readTree(mapper.writeValueAsString(details));

			assertThat(root.get("hash").asText()).isNotBlank();
		}

		@Test
		@DisplayName("contentType field is serialized correctly")
		void serialize_contentType() throws Exception {
			ObjectMapper mapper = createMapper();
			StoredDetails details = buildStoredDetails("id-1", "application/fhir+json", null);

			JsonNode root = mapper.readTree(mapper.writeValueAsString(details));

			assertThat(root.get("contentType").asText()).isEqualTo("application/fhir+json");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 2. Published date — custom @JsonSerialize/@JsonDeserialize
	//    This is the primary LFJT3-sensitive behavior: the annotation package
	//    moves from com.fasterxml.jackson.databind.annotation to
	//    tools.jackson.databind.annotation but behavior must remain identical.
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Published date — custom serializer/deserializer")
	class PublishedDateTests {

		@Test
		@DisplayName("Null published date serializes to null or is absent")
		void serialize_nullPublished_isNullOrAbsent() throws Exception {
			ObjectMapper mapper = createMapper();
			StoredDetails details = buildStoredDetails("id-1", "text/plain", null);

			JsonNode root = mapper.readTree(mapper.writeValueAsString(details));
			JsonNode publishedNode = root.path("published"); // path() never returns null

			// Jackson may omit null fields entirely OR write them as JSON null.
			// Both are valid — the contract is: no non-null date value is present.
			assertThat(publishedNode.isNull() || publishedNode.isMissingNode())
				.as("null published date must serialize to JSON null or be omitted entirely")
				.isTrue();
		}

		@Test
		@DisplayName("Published date serializes as an ISO-8601 string (not epoch millis)")
		void serialize_publishedDate_isIsoString() throws Exception {
			ObjectMapper mapper = createMapper();
			Date published = new Date(1717200000000L); // 2024-06-01
			StoredDetails details = buildStoredDetails("id-1", "text/plain", published);

			JsonNode root = mapper.readTree(mapper.writeValueAsString(details));
			JsonNode publishedNode = root.get("published");

			// Must be a text node (ISO string), NOT a number (epoch millis)
			assertThat(publishedNode.isTextual())
				.as("published must be an ISO-8601 string, not epoch millis")
				.isTrue();
			assertThat(publishedNode.asText()).contains("2024");
		}

		@Test
		@DisplayName("Published date round-trips correctly through serialize/deserialize")
		void roundTrip_publishedDate() throws Exception {
			ObjectMapper mapper = createMapper();
			Date original = new Date(1717200000000L);
			StoredDetails details = buildStoredDetails("id-1", "text/plain", original);

			String json = mapper.writeValueAsString(details);
			StoredDetails reparsed = mapper.readValue(json, StoredDetails.class);

			assertNotNull(reparsed.getPublished());
			// Compare at second granularity via the serialized string to avoid
			// sub-second rounding differences between Jackson 2 and Jackson 3
			String jsonOriginal = mapper.readTree(json).get("published").asText();
			String jsonReparsed = mapper.readTree(mapper.writeValueAsString(reparsed))
				.get("published").asText();
			assertThat(jsonReparsed).isEqualTo(jsonOriginal);
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 3. Legacy blobId backward-compat field
	//    StoredDetails has a blobId field to support pre-7.2.0 stored data.
	//    Deserializing old JSON that uses "blobId" must return that value
	//    via getBinaryContentId().
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Legacy blobId backward-compatibility")
	class LegacyBlobIdTests {

		@Test
		@DisplayName("JSON with blobId field deserializes and getBinaryContentId returns it")
		void deserialize_legacyBlobId_returnedByGetBinaryContentId() throws Exception {
			ObjectMapper mapper = createMapper();
			// This is the old JSON format (pre 7.2.0) — uses "blobId" not "binaryContentId"
			String legacyJson = "{"
				+ "\"blobId\":\"old-blob-id-123\","
				+ "\"bytes\":100,"
				+ "\"contentType\":\"application/pdf\","
				+ "\"hash\":\"abc123\""
				+ "}";

			StoredDetails details = mapper.readValue(legacyJson, StoredDetails.class);

			// getBinaryContentId() must fall back to blobId when binaryContentId is null
			assertThat(details.getBinaryContentId()).isEqualTo("old-blob-id-123");
		}

		@Test
		@DisplayName("JSON with both binaryContentId and blobId prefers binaryContentId")
		void deserialize_bothFields_prefersBinaryContentId() throws Exception {
			ObjectMapper mapper = createMapper();
			String json = "{"
				+ "\"binaryContentId\":\"new-id\","
				+ "\"blobId\":\"old-id\","
				+ "\"bytes\":100,"
				+ "\"contentType\":\"text/plain\","
				+ "\"hash\":\"xyz\""
				+ "}";

			StoredDetails details = mapper.readValue(json, StoredDetails.class);

			// binaryContentId is set, so it wins over blobId
			assertThat(details.getBinaryContentId()).isEqualTo("new-id");
		}

		@Test
		@DisplayName("JSON with only binaryContentId (new format) deserializes correctly")
		void deserialize_newFormat_binaryContentId() throws Exception {
			ObjectMapper mapper = createMapper();
			String json = "{"
				+ "\"binaryContentId\":\"binary-456\","
				+ "\"bytes\":200,"
				+ "\"contentType\":\"image/png\","
				+ "\"hash\":\"hashval\""
				+ "}";

			StoredDetails details = mapper.readValue(json, StoredDetails.class);

			assertThat(details.getBinaryContentId()).isEqualTo("binary-456");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 4. Full round-trip
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Full round-trip")
	class RoundTripTests {

		@Test
		@DisplayName("All fields survive serialize → deserialize")
		void roundTrip_allFields() throws Exception {
			ObjectMapper mapper = createMapper();
			Date published = new Date(1717200000000L);
			StoredDetails original = buildStoredDetails("rt-id-001", "application/fhir+json", published);

			String json = mapper.writeValueAsString(original);
			StoredDetails reparsed = mapper.readValue(json, StoredDetails.class);

			assertThat(reparsed.getBinaryContentId()).isEqualTo("rt-id-001");
			assertThat(reparsed.getContentType()).isEqualTo("application/fhir+json");
			assertThat(reparsed.getBytes()).isEqualTo(original.getBytes());
			assertThat(reparsed.getHash()).isEqualTo(original.getHash());
			assertNotNull(reparsed.getPublished());
		}

		@Test
		@DisplayName("Serialized JSON is valid (parseable without exception)")
		void roundTrip_isValidJson() throws Exception {
			ObjectMapper mapper = createMapper();
			StoredDetails details = buildStoredDetails("id-valid", "text/plain", new Date());

			assertDoesNotThrow(() -> {
				String json = mapper.writeValueAsString(details);
				mapper.readTree(json);
			});
		}

		@Test
		@DisplayName("Default constructor produces deserializable empty instance")
		void defaultConstructor_isDeserializable() throws Exception {
			ObjectMapper mapper = createMapper();
			String json = "{}";
			StoredDetails details = assertDoesNotThrow(
				() -> mapper.readValue(json, StoredDetails.class));
			assertNotNull(details);
		}
	}
}
