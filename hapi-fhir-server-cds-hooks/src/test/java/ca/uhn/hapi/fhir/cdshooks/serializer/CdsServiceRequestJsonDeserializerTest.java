package ca.uhn.hapi.fhir.cdshooks.serializer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.custom.extensions.model.ExampleExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CdsServiceRequestJsonDeserializerTest {
	private static final String SERVICE_ID = "service-id";
	private static final String EXAMPLE_PROPERTY_VALUE = "example-value";
	private static final String EXAMPLE_PROPERTY_KEY = "example-property";
	private final FhirContext myFhirContext = FhirContext.forR4();
	private final ObjectMapper myObjectMapper = new ObjectMapper();
	private CdsServiceRequestJsonDeserializer myFixture;

	@BeforeEach()
	void setup() {
		myFixture = new CdsServiceRequestJsonDeserializer(myFhirContext, myObjectMapper);
	}

	@Test
	void deserialize_shouldDeserialize_whenValidCdsServiceRequestWithExtensionReceived() {
		// setup
		final CdsServiceJson cdsServiceJson = withCdsServiceJsonIncludingExtensionClass();
		final LinkedHashMap<String, Object> extension = withExtension();
		final LinkedHashMap<String, Object> request = withRequest(extension);
		// execute
		final CdsServiceRequestJson actual = myFixture.deserialize(cdsServiceJson, request);
		// validate
		assertThat(actual.getExtension()).isInstanceOf(ExampleExtension.class);
		final ExampleExtension actualExtension = (ExampleExtension) actual.getExtension();
		assertThat(actualExtension.getExampleProperty()).isEqualTo(EXAMPLE_PROPERTY_VALUE);
	}

	@Test
	void deserialize_shouldIgnoreExtraFieldsInsideExtension_whenExtensionContainsMoreFieldsThanDefinedInClass() {
		// setup
		final CdsServiceJson cdsServiceJson = withCdsServiceJsonIncludingExtensionClass();
		final LinkedHashMap<String, Object> extension = withExtension();
		extension.put("example-extra-property", "example-extra-value");
		final LinkedHashMap<String, Object> request = withRequest(extension);
		final LinkedHashMap<String, Object> context = new LinkedHashMap<>();
		context.put("encounterId", "Encounter/123");
		request.put("context", context);
		// execute
		final CdsServiceRequestJson actual = myFixture.deserialize(cdsServiceJson, request);
		// validate
		assertThat(actual.getExtension()).isInstanceOf(ExampleExtension.class);
		final ExampleExtension actualExtension = (ExampleExtension) actual.getExtension();
		assertThat(actualExtension.getExampleProperty()).isEqualTo(EXAMPLE_PROPERTY_VALUE);
		assertThat(actual.getContext().get("encounterId")).isEqualTo("Encounter/123");
	}

	@Test
	void deserialize_shouldThrow_whenCdsServiceRequestIncludesInvalidProperty() {
		// setup
		final CdsServiceJson cdsServiceJson = withCdsServiceJsonIncludingExtensionClass();
		final LinkedHashMap<String, Object> extension = withExtension();
		final LinkedHashMap<String, Object> request = withRequest(extension);
		request.put("invalid-key", "some-value");
		// execute & validate
		assertThatThrownBy(
			() -> myFixture.deserialize(cdsServiceJson, request))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Invalid CdsServiceRequest received.");
	}

	@Test
	void deserialize_shouldReturnNullExtension_whenNotClassFound() {
		// setup
		final CdsServiceJson cdsServiceJson = new CdsServiceJson();
		cdsServiceJson.setId(SERVICE_ID);
		final LinkedHashMap<String, Object> extension = withExtension();
		extension.put("example-extra-property", "example-extra-value");
		final LinkedHashMap<String, Object> request = withRequest(extension);
		// execute
		final CdsServiceRequestJson actual = myFixture.deserialize(cdsServiceJson, request);
		// validate
		assertThat(actual.getExtension()).isNull();
	}

	@Test
	void deserializeRequestContext_shouldDeserialize_whenContextIsValid() throws JsonProcessingException {
		// setup
		final String encounterId = "123";
		final Patient patientContext = new Patient();
		patientContext.setId("456");
		final LinkedHashMap<String, Object> input = new LinkedHashMap<>();
		input.put("encounterId", encounterId);
		input.put("patient", patientContext);
		// execute
		final CdsServiceRequestContextJson actual = myFixture.deserializeRequestContext(input);
		// validate
		assertThat(actual.get("encounterId")).isEqualTo(encounterId);
		assertThat(actual.get("patient")).usingRecursiveComparison().isEqualTo(patientContext);
	}

	@Nonnull
	private static LinkedHashMap<String, Object> withExtension() {
		final LinkedHashMap<String, Object> extension = new LinkedHashMap<>();
		extension.put(EXAMPLE_PROPERTY_KEY, EXAMPLE_PROPERTY_VALUE);
		return extension;
	}

	@Nonnull
	private static CdsServiceJson withCdsServiceJsonIncludingExtensionClass() {
		final CdsServiceJson cdsServiceJson = new CdsServiceJson();
		cdsServiceJson.setId(SERVICE_ID);
		cdsServiceJson.setExtensionClass(ExampleExtension.class);
		return cdsServiceJson;
	}

	@Nonnull
	private static LinkedHashMap<String, Object> withRequest(@Nonnull LinkedHashMap<String, Object> theExtension) {
		final LinkedHashMap<String, Object> request = new LinkedHashMap<>();
		request.put("extension", theExtension);
		return request;
	}
}
