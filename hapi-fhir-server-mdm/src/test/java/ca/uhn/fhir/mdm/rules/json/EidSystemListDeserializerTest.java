package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.i18n.Msg;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by claude-opus-5
class EidSystemListDeserializerTest {

	private static final ObjectMapper ourObjectMapper = new ObjectMapper();

	/**
	 * Mirrors the annotation shape used by {@link MdmRulesJson#myEnterpriseEidSystems} so that the
	 * deserializer can be exercised in isolation from the rest of the rules document.
	 */
	static class EidSystemsHolder {
		@JsonProperty("eidSystems")
		@JsonDeserialize(contentUsing = EidSystemListDeserializer.class)
		Map<String, List<String>> myEidSystems;
	}

	@Test
	void deserialize_scalar_returnsSingletonList() throws Exception {
		EidSystemsHolder holder = deserialize("""
			{"eidSystems": {"Patient": "http://example.com/mrn"}}""");

		assertThat(holder.myEidSystems).containsOnlyKeys("Patient");
		assertThat(holder.myEidSystems.get("Patient")).containsExactly("http://example.com/mrn");
	}

	@Test
	void deserialize_array_returnsListInDeclaredOrder() throws Exception {
		EidSystemsHolder holder = deserialize("""
			{"eidSystems": {"Patient": ["http://example.com/mrn", "http://example.com/npi"]}}""");

		assertThat(holder.myEidSystems.get("Patient"))
			.containsExactly("http://example.com/mrn", "http://example.com/npi");
	}

	@Test
	void deserialize_mixedScalarAndArray_bothFormsCoexist() throws Exception {
		EidSystemsHolder holder = deserialize("""
			{"eidSystems": {
				"Patient": ["http://example.com/mrn", "http://example.com/npi"],
				"Practitioner": "http://example.com/npi"
			}}""");

		assertThat(holder.myEidSystems.get("Patient"))
			.containsExactly("http://example.com/mrn", "http://example.com/npi");
		assertThat(holder.myEidSystems.get("Practitioner")).containsExactly("http://example.com/npi");
	}

	@Test
	void deserialize_emptyArray_returnsEmptyList() throws Exception {
		EidSystemsHolder holder = deserialize("""
			{"eidSystems": {"Patient": []}}""");

		assertThat(holder.myEidSystems.get("Patient")).isEmpty();
	}

	@Test
	void deserialize_null_returnsEmptyList() throws Exception {
		EidSystemsHolder holder = deserialize("""
			{"eidSystems": {"Patient": null}}""");

		assertThat(holder.myEidSystems.get("Patient")).isEmpty();
	}

	@Test
	void deserialize_nonStringValue_throwsNamingTheResourceType() {
		assertThatThrownBy(() -> deserialize("""
			{"eidSystems": {"Patient": 42}}"""))
			.isInstanceOf(JsonMappingException.class)
			.hasMessageContaining(Msg.code(3046))
			.hasMessageContaining("eidSystems")
			.hasMessageContaining("Patient");
	}

	@Test
	void deserialize_arrayContainingNonString_throwsNamingTheResourceType() {
		assertThatThrownBy(() -> deserialize("""
			{"eidSystems": {"Patient": ["http://example.com/mrn", 42]}}"""))
			.isInstanceOf(JsonMappingException.class)
			.hasMessageContaining(Msg.code(3046))
			.hasMessageContaining("eidSystems")
			.hasMessageContaining("Patient");
	}

	private EidSystemsHolder deserialize(String theJson) throws Exception {
		return ourObjectMapper.readValue(theJson, EidSystemsHolder.class);
	}
}
