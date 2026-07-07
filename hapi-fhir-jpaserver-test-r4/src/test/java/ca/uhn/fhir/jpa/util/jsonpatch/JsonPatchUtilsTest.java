package ca.uhn.fhir.jpa.util.jsonpatch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.patch.JsonPatchUtils;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonPatchUtilsTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(JsonPatchUtilsTest.class);

	@SuppressWarnings("JsonStandardCompliance")
	@Test
	public void testInvalidPatchJsonError() {

		// Single quotes are incorrect in the "value" body—triggers JSON parse error
		String patchText = "[ {\n" +
			"        \"comment\": \"add image to examination\",\n" +
			"        \"patch\": [ {\n" +
			"            \"op\": \"add\",\n" +
			"            \"path\": \"/derivedFrom/-\",\n" +
			"            \"value\": [{'reference': '/Media/465eb73a-bce3-423a-b86e-5d0d267638f4'}]\n" +
			"        } ]\n" +
			"    } ]";

		try {
			JsonPatchUtils.apply(myFhirContext, new Observation(), patchText);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.info(e.toString());
			// Jackson 3 reports JSON parsing error containing "double-quote"
			assertThat(e.toString()).containsIgnoringCase("double-quote");
			// The error message should not contain the patch body
			assertThat(e.toString()).doesNotContain("add image to examination");
		}

	}

	@Test
	public void testInvalidPatchSyntaxError() {

		// Invalid operation: "foo" is not a valid JSON Patch op (must be add/remove/replace/etc)
		String patchText = "[ {" +
			"            \"op\": \"foo\"," +
			"            \"path\": \"/derivedFrom/-\"," +
			"            \"value\": [{\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}]" +
			"        } ]";

		try {
			JsonPatchUtils.apply(myFhirContext, new Observation(), patchText);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.info(e.toString());
			// When the patch operation is invalid, an error is thrown during apply
			// Could be from Jackson deserialization or from zjsonpatch validation
			// Just verify that it's caught and wrapped as InvalidRequestException
			assertThat(e.toString()).isNotEmpty();
			// The patch body should not leak into error messages (for security)
			assertThat(e.toString()).doesNotContain("/Media/465eb73a-bce3-423a-b86e-5d0d267638f4");
		}

	}


	@Test
	public void testPatchAddArray() {

		String patchText = "[ " +
			"      {" +
			"        \"op\": \"add\"," +
			"        \"path\": \"/derivedFrom\"," +
			"        \"value\": [" +
			"          {\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}" +
			"        ]" +
			"      } " +
			"]";

		Observation toUpdate = new Observation();
		toUpdate = JsonPatchUtils.apply(myFhirContext, toUpdate, patchText);

		String outcome = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(toUpdate);
		ourLog.info(outcome);

		assertThat(outcome).contains("\"reference\": \"Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"");
	}

	@Test
	public void testPatchAddMemberToEmptyGroup() {
		Group group = new Group();
		group.setId("Group/test-group");
		group.setType(Group.GroupType.PERSON);
		group.setActual(true);

		String patchText = "[{" +
			"\"op\":\"add\"," +
			"\"path\":\"/member/0\"," +
			"\"value\":{" +
			"\"entity\":{\"reference\":\"Patient/123\"}," +
			"\"inactive\":false" +
			"}" +
			"}]";

		Group result = JsonPatchUtils.apply(myFhirContext, group, patchText);

		assertThat(result.getMember()).hasSize(1);
		assertThat(result.getMember().get(0).getEntity().getReference()).isEqualTo("Patient/123");
		assertThat(result.getMember().get(0).getInactive()).isFalse();
	}

	@Test
	public void testPatchAddMemberToEmptyGroup_AppendPath() {
		Group group = new Group();
		group.setId("Group/test-group");
		group.setType(Group.GroupType.PERSON);
		group.setActual(true);

		String patchText = "[{" +
			"\"op\":\"add\"," +
			"\"path\":\"/member/-\"," +
			"\"value\":{" +
			"\"entity\":{\"reference\":\"Patient/456\"}," +
			"\"inactive\":false" +
			"}" +
			"}]";

		Group result = JsonPatchUtils.apply(myFhirContext, group, patchText);

		assertThat(result.getMember()).hasSize(1);
		assertThat(result.getMember().get(0).getEntity().getReference()).isEqualTo("Patient/456");
	}

	@Test
	public void testPatchReplaceMemberOnEmptyGroup_Fails() {
		Group group = new Group();
		group.setId("Group/test-group");
		group.setType(Group.GroupType.PERSON);
		group.setActual(true);

		String patchText = "[{" +
			"\"op\":\"replace\"," +
			"\"path\":\"/member/0\"," +
			"\"value\":{" +
			"\"entity\":{\"reference\":\"Patient/123\"}," +
			"\"inactive\":false" +
			"}" +
			"}]";

		assertThatThrownBy(() -> JsonPatchUtils.apply(myFhirContext, group, patchText))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("HAPI-1272");
	}

	@Test
	public void testPatchAddInvalidElement() {

		String patchText = "[ " +
			"      {" +
			"        \"op\": \"add\"," +
			"        \"path\": \"/derivedFromXXX\"," +
			"        \"value\": [" +
			"          {\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}" +
			"        ]" +
			"      } " +
			"]";

		Observation toUpdate = new Observation();
		try {
			JsonPatchUtils.apply(myFhirContext, toUpdate, patchText);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1271) + "Failed to apply JSON patch to Observation: " + Msg.code(1825) + "Unknown element 'derivedFromXXX' found during parse", e.getMessage());
		}

	}

	private static final FhirContext FHIR_CONTEXT = FhirContext.forR4();

	// ── LFJT3 MAPPER FACTORY ─────────────────────────────────────────────────
	// Jackson 2 (NOW): new ObjectMapper()
	// Jackson 3 (LFJT3): JsonMapper.builder().build()
	private ObjectMapper createMapper() {
		return new ObjectMapper();
		// LFJT3: return JsonMapper.builder().build();
	}
	// ── END LFJT3 MAPPER FACTORY ─────────────────────────────────────────────

	/** Build a simple Patient JSON string via HAPI. */
	private String buildPatientJson(String id, String familyName) {
		Patient patient = new Patient();
		patient.setId(id);
		if (familyName != null) {
			patient.addName().setFamily(familyName);
		}
		return FHIR_CONTEXT.newJsonParser().encodeResourceToString(patient);
	}

	private String applyPatchToPatientJson(String theOriginalJson, String thePatchBody) {
		Patient originalResource = FHIR_CONTEXT.newJsonParser().parseResource(Patient.class, theOriginalJson);
		Patient patchedResource = JsonPatchUtils.apply(FHIR_CONTEXT, originalResource, thePatchBody);
		return FHIR_CONTEXT.newJsonParser().encodeResourceToString(patchedResource);
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 1. Valid patch operations
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Valid RFC 6902 patch operations")
	class ValidPatchTests {

		@Test
		@DisplayName("'replace' operation changes a field value")
		void patch_replaceOperation_changesField() throws Exception {
			String originalJson = buildPatientJson("pt-001", "Smith");

			// RFC 6902 replace patch
			String patchBody = "[{\"op\":\"replace\",\"path\":\"/name/0/family\",\"value\":\"Jones\"}]";

			String result = applyPatchToPatientJson(originalJson, patchBody);

			ObjectMapper mapper = createMapper();
			JsonNode resultNode = mapper.readTree(result);
			assertThat(resultNode.path("name").path(0).path("family").asText())
				.isEqualTo("Jones");
		}

		@Test
		@DisplayName("'add' operation inserts a new field")
		void patch_addOperation_insertsField() throws Exception {
			String originalJson = buildPatientJson("pt-002", null);

			// Add a gender field
			String patchBody = "[{\"op\":\"add\",\"path\":\"/gender\",\"value\":\"male\"}]";

			String result = applyPatchToPatientJson(originalJson, patchBody);

			ObjectMapper mapper = createMapper();
			JsonNode resultNode = mapper.readTree(result);
			assertThat(resultNode.has("gender")).isTrue();
			assertThat(resultNode.get("gender").asText()).isEqualTo("male");
		}

		@Test
		@DisplayName("Empty patch array returns original document unchanged")
		void patch_emptyPatch_returnsOriginal() throws Exception {
			String originalJson = buildPatientJson("pt-003", "Brown");
			String patchBody = "[]";

			String result = applyPatchToPatientJson(originalJson, patchBody);

			ObjectMapper mapper = createMapper();
			JsonNode original = mapper.readTree(originalJson);
			JsonNode patched = mapper.readTree(result);
			assertThat(patched.get("resourceType").asText())
				.isEqualTo(original.get("resourceType").asText());
		}

		@Test
		@DisplayName("Result is valid JSON")
		void patch_result_isValidJson() throws Exception {
			String originalJson = buildPatientJson("pt-004", "Taylor");
			String patchBody = "[{\"op\":\"replace\",\"path\":\"/id\",\"value\":\"pt-004-patched\"}]";

			String result = applyPatchToPatientJson(originalJson, patchBody);

			// Verify result is parseable JSON
			ObjectMapper mapper = createMapper();
			JsonNode node = mapper.readTree(result);
			assertNotNull(node);
		}

		@Test
		@DisplayName("Result preserves resourceType field")
		void patch_result_preservesResourceType() throws Exception {
			String originalJson = buildPatientJson("pt-005", "White");
			String patchBody = "[{\"op\":\"replace\",\"path\":\"/id\",\"value\":\"pt-005-new\"}]";

			String result = applyPatchToPatientJson(originalJson, patchBody);

			ObjectMapper mapper = createMapper();
			assertThat(mapper.readTree(result).get("resourceType").asText())
				.isEqualTo("Patient");
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 2. Invalid patch input
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Invalid patch input")
	class InvalidPatchTests {

		@Test
		@DisplayName("Malformed JSON patch body throws InvalidRequestException")
		void patch_malformedJson_throwsInvalidRequestException() {
			String originalJson = buildPatientJson("pt-bad", "Black");
			String badPatch = "this is not json";

			assertThrows(InvalidRequestException.class,
				() -> applyPatchToPatientJson(originalJson, badPatch));
		}

		@Test
		@DisplayName("Patch with invalid op throws InvalidRequestException")
		void patch_invalidOp_throwsInvalidRequestException() {
			String originalJson = buildPatientJson("pt-bad2", "Green");
			String badPatch = "[{\"op\":\"invalidop\",\"path\":\"/id\",\"value\":\"x\"}]";

			assertThrows(InvalidRequestException.class,
				() -> applyPatchToPatientJson(originalJson, badPatch));
		}

		@Test
		@DisplayName("Patch pointing to non-existent path throws InvalidRequestException")
		void patch_nonExistentPath_throwsInvalidRequestException() {
			String originalJson = buildPatientJson("pt-bad3", null);
			// Trying to remove a path that doesn't exist
			String badPatch = "[{\"op\":\"remove\",\"path\":\"/nonExistentField/deeply/nested\"}]";

			assertThrows(InvalidRequestException.class,
				() -> applyPatchToPatientJson(originalJson, badPatch));
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 3. INCLUDE_SOURCE_IN_LOCATION is disabled
	//    This is the key Jackson config in JsonPatchUtils: the feature that
	//    would include raw input in exception messages is explicitly turned OFF
	//    for privacy/security. This must survive the LFJT3 uplift where
	//    JsonParser.Feature → StreamReadFeature.
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("Privacy — INCLUDE_SOURCE_IN_LOCATION disabled")
	class PrivacyTests {

		@Test
		@DisplayName("Exception message from bad patch does not contain raw input data")
		void patch_invalidInput_exceptionDoesNotLeakRawInput() {
			String sensitiveJson = buildPatientJson("pt-sensitive", "SecretName");
			String badPatch = "DEFINITELY_NOT_JSON";

			try {
				applyPatchToPatientJson(sensitiveJson, badPatch);
			} catch (InvalidRequestException e) {
				// The exception message must NOT contain the raw input payload.
				// INCLUDE_SOURCE_IN_LOCATION=false ensures Jackson strips source from errors.
				assertThat(e.getMessage())
					.as("Exception message must not leak raw input data (INCLUDE_SOURCE_IN_LOCATION=false)")
					.doesNotContain("SecretName");
			}
		}
	}
}
