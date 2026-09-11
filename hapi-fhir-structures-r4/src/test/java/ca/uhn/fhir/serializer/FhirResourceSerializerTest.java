package ca.uhn.fhir.serializer;

/*
 * FhirResourceSerializerTest
 *
 * PURPOSE
 * -------
 * These tests lock down the behaviour of FhirResourceSerializer under Jackson 3
 * (tools.jackson). The tests are structure so that they remain
 * Jackson-version-agnostic.
 *
 * MIGRATION COMPLETE
 * ------------------
 * This file has been migrated from Jackson 2 (com.fasterxml.jackson) to
 * Jackson 3 (tools.jackson). Only the import block and createMapper() factory
 * method were changed. Test logic remains untouched.
 */

import ca.uhn.fhir.context.FhirContext;

// ── JACKSON IMPORT BLOCK ─────────────────────────────────────────────────────
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
// ── END JACKSON IMPORT BLOCK ──────────────────────────────────────────────────

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link FhirResourceSerializer}.
 *
 * <p>Tests are written for Jackson 3 ({@code tools.jackson}). All assertions
 * operate on plain {@code String} and {@code JsonNode} so they are
 * Jackson-version-agnostic.
 */
class FhirResourceSerializerTest {

	// ── Shared FHIR context (expensive to construct — reuse across tests) ────
	private static final FhirContext FHIR_CONTEXT = FhirContext.forR4();

	private ObjectMapper myMapper;

	// ── JACKSON FACTORY METHOD ───────────────────────────────────────────────
	// Jackson 3: replace  new ObjectMapper()
	//            with     JsonMapper.builder().addModule().build()
	// Everything else in this method stays the same.
	private ObjectMapper createMapper(FhirContext theFhirContext) {
		SimpleModule module = new SimpleModule();
		module.addSerializer(new FhirResourceSerializer(theFhirContext));
		return JsonMapper.builder().addModule(module).build();
	}
	// ── END JACKSON FACTORY METHOD ───────────────────────────────────────────

	@BeforeEach
	void setUp() {
		myMapper = createMapper(FHIR_CONTEXT);
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 1. OUTPUT FORMAT — the most critical behavioural contract:
	//    writeRawValue() must be used, not writeString().
	//    If writeString() were used the FHIR JSON would appear as an escaped
	//    string value in the parent JSON, breaking all consumers.
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Output format — raw embedded JSON, not escaped string")
	class OutputFormatTests {

		@Test
		@DisplayName("FHIR resource is embedded as a JSON object, not a quoted string")
		void serialize_embedsFhirResourceAsJsonObject_notQuotedString() throws Exception {
			// Given
			Patient patient = buildMinimalPatient();
			Wrapper wrapper = new Wrapper(patient);

			// When
			String json = myMapper.writeValueAsString(wrapper);

			// Then — the value of the "resource" field must be a JSON object {…},
			// NOT a quoted string "…".  If writeString() were called instead of
			// writeRawValue() this assertion would fail.
			JsonNode root = myMapper.readTree(json);
			JsonNode resourceNode = root.get("resource");

			assertNotNull(resourceNode, "root JSON must contain a 'resource' field");
			assertTrue(resourceNode.isObject(),
				 "'resource' field must be a JSON object, was: " + resourceNode.getNodeType());
		}

		@Test
		@DisplayName("Serialized output is valid JSON (parseable without error)")
		void serialize_producesValidJson() {
			Patient patient = buildMinimalPatient();
			Wrapper wrapper = new Wrapper(patient);

			assertDoesNotThrow(() -> {
				String json = myMapper.writeValueAsString(wrapper);
				myMapper.readTree(json); // throws if invalid JSON
			}, "Serialized output must be valid JSON");
		}

		@Test
		@DisplayName("Embedded FHIR JSON contains resourceType field")
		void serialize_embeddedJsonContainsResourceType() throws Exception {
			Patient patient = buildMinimalPatient();
			Wrapper wrapper = new Wrapper(patient);

			String json = myMapper.writeValueAsString(wrapper);
			JsonNode resourceNode = myMapper.readTree(json).get("resource");

			assertThat(resourceNode.has("resourceType"))
				 .as("Embedded FHIR JSON must contain 'resourceType'")
				 .isTrue();
			assertThat(resourceNode.get("resourceType").asText())
				 .isEqualTo("Patient");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 2. PRETTY PRINT — parser is constructed with setPrettyPrint(true)
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Pretty print — parser constructed with setPrettyPrint(true)")
	class PrettyPrintTests {

		@Test
		@DisplayName("Embedded FHIR JSON contains newlines (pretty-printed)")
		void serialize_embeddedFhirJsonIsPrettyPrinted() throws Exception {
			Patient patient = buildMinimalPatient();
			Wrapper wrapper = new Wrapper(patient);

			String json = myMapper.writeValueAsString(wrapper);

			// Extract the raw embedded FHIR fragment — it should contain newlines
			// because the parser is created with setPrettyPrint(true).
			// We check for \n in the overall output since the FHIR block is inlined.
			assertThat(json)
				 .as("Serialized output should contain newlines from pretty-printed FHIR JSON")
				 .contains("\n");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 3. RESOURCE TYPE COVERAGE — serializer handles IBaseResource subtypes
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Resource type coverage")
	class ResourceTypeCoverageTests {

		@Test
		@DisplayName("Serializes Patient resource")
		void serialize_patient() throws Exception {
			Patient patient = buildMinimalPatient();
			assertSerializesWithResourceType(patient, "Patient");
		}

		@Test
		@DisplayName("Serializes Patient with demographic fields")
		void serialize_patientWithDemographics() throws Exception {
			Patient patient = new Patient();
			patient.setId("patient-demo-1");
			patient.setGender(Enumerations.AdministrativeGender.FEMALE);
			patient.addName().setFamily("Smith").addGiven("Jane");
			patient.addAddress().setCity("Boston").setState("MA").setPostalCode("02101");

			String json = myMapper.writeValueAsString(new Wrapper(patient));
			JsonNode resourceNode = myMapper.readTree(json).get("resource");

			assertThat(resourceNode.get("resourceType").asText()).isEqualTo("Patient");
			assertThat(resourceNode.has("gender")).isTrue();
			assertThat(resourceNode.get("gender").asText()).isEqualTo("female");
			assertThat(resourceNode.has("name")).isTrue();
			assertThat(resourceNode.has("address")).isTrue();
		}

		@Test
		@DisplayName("Serializes Observation resource")
		void serialize_observation() throws Exception {
			Observation observation = new Observation();
			observation.setId("obs-1");
			observation.setStatus(Observation.ObservationStatus.FINAL);
			assertSerializesWithResourceType(observation, "Observation");
		}

		@Test
		@DisplayName("Serializes resource with ID preserved")
		void serialize_preservesResourceId() throws Exception {
			Patient patient = new Patient();
			patient.setId("test-patient-id-999");

			String json = myMapper.writeValueAsString(new Wrapper(patient));
			JsonNode resourceNode = myMapper.readTree(json).get("resource");

			assertThat(resourceNode.has("id")).isTrue();
			assertThat(resourceNode.get("id").asText()).isEqualTo("test-patient-id-999");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 4. DIRECT SERIALIZATION — serialize the resource as the root value
	//    (not wrapped in an outer object)
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Direct serialization — resource as root value")
	class DirectSerializationTests {

		@Test
		@DisplayName("Resource serialized directly to String is valid JSON")
		void serialize_directlyToString_isValidJson() throws Exception {
			Patient patient = buildMinimalPatient();

			// Serialize directly (no wrapper object)
			StringWriter writer = new StringWriter();
			myMapper.writeValue(writer, patient);
			String json = writer.toString();

			assertDoesNotThrow(() -> myMapper.readTree(json),
				 "Direct serialization output must be valid JSON");
		}

		@Test
		@DisplayName("Resource serialized directly contains resourceType")
		void serialize_directlyToString_containsResourceType() throws Exception {
			Patient patient = buildMinimalPatient();

			StringWriter writer = new StringWriter();
			myMapper.writeValue(writer, patient);

			JsonNode root = myMapper.readTree(writer.toString());
			assertThat(root.get("resourceType").asText()).isEqualTo("Patient");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 5. ROUND-TRIP — serialize then re-parse the embedded FHIR JSON
	//    back into a FHIR resource and verify field integrity
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Round-trip — serialize → re-parse embedded FHIR JSON")
	class RoundTripTests {

		@Test
		@DisplayName("Round-trip Patient preserves family name")
		void roundTrip_patientFamilyName() throws Exception {
			Patient original = new Patient();
			original.setId("rt-patient-1");
			original.addName().setFamily("Johnson").addGiven("Robert");

			// Serialize
			String json = myMapper.writeValueAsString(new Wrapper(original));
			JsonNode resourceNode = myMapper.readTree(json).get("resource");

			// Re-parse the embedded FHIR JSON back into a Patient
			String embeddedFhirJson = myMapper.writeValueAsString(resourceNode);
			Patient reparsed = (Patient) FHIR_CONTEXT.newJsonParser()
				 .parseResource(embeddedFhirJson);

			assertThat(reparsed.getNameFirstRep().getFamily())
				 .isEqualTo("Johnson");
			assertThat(reparsed.getNameFirstRep().getGivenAsSingleString())
				 .isEqualTo("Robert");
		}

		@Test
		@DisplayName("Round-trip Patient preserves gender")
		void roundTrip_patientGender() throws Exception {
			Patient original = new Patient();
			original.setId("rt-patient-2");
			original.setGender(Enumerations.AdministrativeGender.MALE);

			String json = myMapper.writeValueAsString(new Wrapper(original));
			JsonNode resourceNode = myMapper.readTree(json).get("resource");

			String embeddedFhirJson = myMapper.writeValueAsString(resourceNode);
			Patient reparsed = (Patient) FHIR_CONTEXT.newJsonParser()
				 .parseResource(embeddedFhirJson);

			assertThat(reparsed.getGender())
				 .isEqualTo(Enumerations.AdministrativeGender.MALE);
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Helpers
	// ─────────────────────────────────────────────────────────────────────────

	private static Patient buildMinimalPatient() {
		Patient patient = new Patient();
		patient.setId("minimal-patient-1");
		return patient;
	}

	private void assertSerializesWithResourceType(IBaseResource theResource, String theExpectedType)
		 throws Exception {
		String json = myMapper.writeValueAsString(new Wrapper(theResource));
		JsonNode resourceNode = myMapper.readTree(json).get("resource");

		assertNotNull(resourceNode, "root JSON must contain a 'resource' field");
		assertTrue(resourceNode.isObject(), "'resource' must be a JSON object");
		assertThat(resourceNode.get("resourceType").asText()).isEqualTo(theExpectedType);
	}

	/**
	 * Simple wrapper to produce an outer JSON object with a single "resource"
	 * field — this lets us verify that the serializer writes a JSON object
	 * value rather than a JSON string value (i.e. writeRawValue vs writeString).
	 */
	static class Wrapper {
		private final IBaseResource myResource;

		Wrapper(IBaseResource theResource) {
			myResource = theResource;
		}

		public IBaseResource getResource() {
			return myResource;
		}
	}
}
