package ca.uhn.hapi.fhir.cdshooks.serializer;

/*
 * LFJT3 — "Looking Forward to Jackson Tools 3"
 * =============================================
 * Written for Jackson 2 (com.fasterxml.jackson). Only the import block
 * and createMapper() factory method change during the LFJT3 uplift.
 * All @Test methods need zero changes.
 *
 * LFJT3 MIGRATION CHECKLIST
 * --------------------------
 * [ ] Imports:
 *       com.fasterxml.jackson.databind.ObjectMapper      → tools.jackson.databind.ObjectMapper
 *       com.fasterxml.jackson.databind.JsonNode          → tools.jackson.databind.JsonNode
 *       com.fasterxml.jackson.databind.module.SimpleModule → tools.jackson.databind.module.SimpleModule
 * [ ] createMapper(): new ObjectMapper() → JsonMapper.builder().build()
 *
 * KEY BEHAVIORS LOCKED DOWN
 * -------------------------
 * CdsServiceRequestContextSerializer:
 *   - Each context entry is written as a JSON object/value, NOT a quoted string
 *     (uses writeRawValue() not writeString())
 *   - writeFieldName(key) [Jackson 2] → writeName(key) [Jackson 3] is a
 *     PRODUCTION CLASS change, not a test change — tests assert on output only
 *
 * CdsServiceRequestContextDeserializer:
 *   - LinkedHashMap entries that are maps are re-parsed as IBaseResource
 *   - Scalar values are passed through as-is
 *   - theJsonParser.getCodec().readTree() [Jackson 2] →
 *     theJsonParser.readValueAsTree() [Jackson 3] — production class change only
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
// ── LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────────
// Jackson 2 (NOW):
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
// Jackson 3 (LFJT3):
//   import tools.jackson.databind.JsonNode;
//   import tools.jackson.databind.ObjectMapper;
//   import tools.jackson.databind.module.SimpleModule;
// ── END LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LFJT3-aware tests for {@link CdsServiceRequestContextSerializer} and
 * {@link CdsServiceRequestContextDeserializer}.
 */
class CdsServiceRequestContextSerializerDeserializerTest {

	private static final FhirContext FHIR_CONTEXT = FhirContext.forR4();

	private ObjectMapper myMapper;

	// ── LFJT3 MAPPER FACTORY ─────────────────────────────────────────────────
	// Jackson 2 (NOW): new ObjectMapper()
	// Jackson 3 (LFJT3): JsonMapper.builder().build()
	private ObjectMapper createMapper() {
		// Note: CdsServiceRequestContextSerializer/Deserializer take the mapper
		// as a constructor arg — this mirrors CdsHooksObjectMapperFactory.newMapper()
		ObjectMapper baseMapper = new ObjectMapper();
		// LFJT3: ObjectMapper baseMapper = JsonMapper.builder().build();

		SimpleModule module = new SimpleModule();
		module.addSerializer(new CdsServiceRequestContextSerializer(FHIR_CONTEXT, baseMapper));
		module.addDeserializer(
			CdsServiceRequestContextJson.class,
			new CdsServiceRequestContextDeserializer(FHIR_CONTEXT, baseMapper));
		return baseMapper.registerModule(module);
		// LFJT3: return JsonMapper.builder().addModule(module).build();
	}
	// ── END LFJT3 MAPPER FACTORY ─────────────────────────────────────────────

	@BeforeEach
	void setUp() {
		myMapper = createMapper();
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Wrapper so we can embed CdsServiceRequestContextJson in an outer object
	// ─────────────────────────────────────────────────────────────────────────
	static class ContextHolder {
		public CdsServiceRequestContextJson context;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 1. CdsServiceRequestContextSerializer
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("CdsServiceRequestContextSerializer")
	class SerializerTests {

		@Test
		@DisplayName("Scalar string entry is serialized as a JSON string value")
		void serialize_scalarString_isJsonString() throws Exception {
			CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
			context.put("patientId", "Patient/123");

			String json = myMapper.writeValueAsString(context);
			JsonNode root = myMapper.readTree(json);

			assertThat(root.get("patientId").asText()).isEqualTo("Patient/123");
		}

		@Test
		@DisplayName("FHIR resource entry is embedded as JSON object, not quoted string")
		void serialize_fhirResource_isJsonObject() throws Exception {
			Patient patient = new Patient();
			patient.setId("pt-001");

			CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
			context.put("patient", patient);

			String json = myMapper.writeValueAsString(context);
			JsonNode root = myMapper.readTree(json);
			JsonNode patientNode = root.get("patient");

			// Must be a JSON object, not a quoted/escaped string
			assertNotNull(patientNode, "patient field must be present");
			assertTrue(patientNode.isObject(),
				"patient must be embedded as a JSON object (writeRawValue), not a quoted string (writeString)");
		}

		@Test
		@DisplayName("FHIR resource entry contains resourceType")
		void serialize_fhirResource_hasResourceType() throws Exception {
			Patient patient = new Patient();
			patient.setId("pt-002");

			CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
			context.put("patient", patient);

			String json = myMapper.writeValueAsString(context);
			JsonNode patientNode = myMapper.readTree(json).get("patient");

			assertThat(patientNode.get("resourceType").asText()).isEqualTo("Patient");
		}

		@Test
		@DisplayName("Mixed context (scalar + FHIR resource) serializes both correctly")
		void serialize_mixedContext() throws Exception {
			Patient patient = new Patient();
			patient.setId("pt-003");

			CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
			context.put("patientId", "Patient/123");
			context.put("patient", patient);

			String json = myMapper.writeValueAsString(context);
			JsonNode root = myMapper.readTree(json);

			assertThat(root.get("patientId").asText()).isEqualTo("Patient/123");
			assertTrue(root.get("patient").isObject());
		}

		@Test
		@DisplayName("Produces valid JSON (parseable without exception)")
		void serialize_producesValidJson() throws Exception {
			CdsServiceRequestContextJson context = new CdsServiceRequestContextJson();
			context.put("userId", "Practitioner/P1");

			assertDoesNotThrow(() -> {
				String json = myMapper.writeValueAsString(context);
				myMapper.readTree(json);
			});
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 2. CdsServiceRequestContextDeserializer
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("CdsServiceRequestContextDeserializer")
	class DeserializerTests {

		@Test
		@DisplayName("Scalar string entry is deserialized as String")
		void deserialize_scalarString_isString() throws Exception {
			String json = "{\"patientId\":\"Patient/123\"}";

			CdsServiceRequestContextJson context =
				myMapper.readValue(json, CdsServiceRequestContextJson.class);

			assertThat(context.get("patientId")).isEqualTo("Patient/123");
		}

		@Test
		@DisplayName("FHIR resource entry (as nested map) is deserialized as IBaseResource")
		void deserialize_fhirResourceMap_isIBaseResource() throws Exception {
			String json = "{"
				+ "\"patient\":{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"pt-abc\""
				+ "}"
				+ "}";

			CdsServiceRequestContextJson context =
				myMapper.readValue(json, CdsServiceRequestContextJson.class);

			Object patient = context.get("patient");
			assertNotNull(patient);
			assertThat(patient).isInstanceOf(org.hl7.fhir.instance.model.api.IBaseResource.class);
		}

		@Test
		@DisplayName("FHIR resource round-trip preserves patient ID")
		void deserialize_fhirResource_preservesId() throws Exception {
			String json = "{"
				+ "\"patient\":{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"pt-round-trip\""
				+ "}"
				+ "}";

			CdsServiceRequestContextJson context =
				myMapper.readValue(json, CdsServiceRequestContextJson.class);

			Patient patient = (Patient) context.get("patient");
			assertThat(patient.getIdElement().getIdPart()).isEqualTo("pt-round-trip");
		}

		@Test
		@DisplayName("Mixed context (scalar + resource) deserializes both correctly")
		void deserialize_mixedContext() throws Exception {
			String json = "{"
				+ "\"userId\":\"Practitioner/P1\","
				+ "\"patient\":{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"pt-mixed\""
				+ "}"
				+ "}";

			CdsServiceRequestContextJson context =
				myMapper.readValue(json, CdsServiceRequestContextJson.class);

			assertThat(context.get("userId")).isEqualTo("Practitioner/P1");
			assertThat(context.get("patient")).isInstanceOf(org.hl7.fhir.instance.model.api.IBaseResource.class);
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 3. Round-trip: serialize → deserialize
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Round-trip: serialize → deserialize")
	class RoundTripTests {

		@Test
		@DisplayName("FHIR Patient round-trip preserves resourceType and id")
		void roundTrip_fhirPatient() throws Exception {
			Patient original = new Patient();
			original.setId("rt-patient-1");

			CdsServiceRequestContextJson originalContext = new CdsServiceRequestContextJson();
			originalContext.put("patient", original);

			String json = myMapper.writeValueAsString(originalContext);
			CdsServiceRequestContextJson reparsed =
				myMapper.readValue(json, CdsServiceRequestContextJson.class);

			Patient reparsedPatient = (Patient) reparsed.get("patient");
			assertThat(reparsedPatient.getIdElement().getIdPart()).isEqualTo("rt-patient-1");
		}

		@Test
		@DisplayName("Scalar string round-trip preserves value")
		void roundTrip_scalarString() throws Exception {
			CdsServiceRequestContextJson originalContext = new CdsServiceRequestContextJson();
			originalContext.put("hookInstance", "uuid-999");

			String json = myMapper.writeValueAsString(originalContext);
			CdsServiceRequestContextJson reparsed =
				myMapper.readValue(json, CdsServiceRequestContextJson.class);

			assertThat(reparsed.get("hookInstance")).isEqualTo("uuid-999");
		}

		@Test
		@DisplayName("Multiple keys all survive round-trip")
		void roundTrip_multipleKeys() throws Exception {
			Patient patient = new Patient();
			patient.setId("multi-pt");

			CdsServiceRequestContextJson originalContext = new CdsServiceRequestContextJson();
			originalContext.put("patientId", "Patient/multi-pt");
			originalContext.put("userId", "Practitioner/dr-jones");
			originalContext.put("patient", patient);

			String json = myMapper.writeValueAsString(originalContext);
			CdsServiceRequestContextJson reparsed =
				myMapper.readValue(json, CdsServiceRequestContextJson.class);

			assertThat(reparsed.get("patientId")).isEqualTo("Patient/multi-pt");
			assertThat(reparsed.get("userId")).isEqualTo("Practitioner/dr-jones");
			assertThat(reparsed.get("patient")).isInstanceOf(org.hl7.fhir.instance.model.api.IBaseResource.class);
		}
	}
}
