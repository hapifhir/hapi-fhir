package ca.uhn.hapi.fhir.cdshooks.serializer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import ca.uhn.hapi.fhir.cdshooks.custom.extensions.model.ExampleExtension;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CdsServiceRequestJsonDeserializerTest {
	@Mock
	private CdsServiceRegistryImpl myCdsServiceRegistry;
	private final FhirContext myFhirContext = FhirContext.forR4();
	private CdsServiceRequestJsonDeserializer myFixture;

	@BeforeEach()
	void setup() {
		myFixture = new CdsServiceRequestJsonDeserializer(myCdsServiceRegistry, myFhirContext);
	}

	@Test
	void configureObjectMapper() {
		// setup
		ObjectMapper input = new ObjectMapper();
		// execute
		myFixture.configureObjectMapper(input);
		// validate
		assertThat(input.getRegisteredModuleIds()).hasSize(1);
	}

	@Test
	void deserializeExtensionWhenClassFoundShouldDeserializeExtension() throws JsonProcessingException {
		// setup
		final String serviceId = "service-id";
		final String extension = """
		{
			"example-property": "example-value"
		}
		""";
		final CdsServiceJson cdsServiceJson = new CdsServiceJson();
		cdsServiceJson.setId(serviceId);
		cdsServiceJson.setExtensionClass(ExampleExtension.class);
		doReturn(cdsServiceJson).when(myCdsServiceRegistry).getCdsServiceJson(serviceId);
		// execute
		final ExampleExtension actual = (ExampleExtension) myFixture.deserializeExtension(serviceId, extension);
		// validate
		assertThat(actual.getExampleProperty()).isEqualTo("example-value");
	}

	@Test
	void deserializeExtensionWhenClassFoundButExtensionHasExtraPropertiesShouldIgnoreExtraProperties() throws JsonProcessingException {
		// setup
		final String serviceId = "service-id";
		final String extension = """
		{
			"example-property": "example-value",
			"example-extra-property": "example-extra-value"
		}
		""";
		final CdsServiceJson cdsServiceJson = new CdsServiceJson();
		cdsServiceJson.setId(serviceId);
		cdsServiceJson.setExtensionClass(ExampleExtension.class);
		doReturn(cdsServiceJson).when(myCdsServiceRegistry).getCdsServiceJson(serviceId);
		// execute
		final ExampleExtension actual = (ExampleExtension) myFixture.deserializeExtension(serviceId, extension);
		// validate
		assertThat(actual.getExampleProperty()).isEqualTo("example-value");
	}

	@Test
	void deserializeExtensionWhenNotClassFoundShouldReturnNull() throws JsonProcessingException {
		// setup
		final String serviceId = "service-id";
		final String extension = """
		{
			"example-property": "example-value"
		}
		""";
		final CdsServiceJson cdsServiceJson = new CdsServiceJson();
		cdsServiceJson.setId(serviceId);
		doReturn(cdsServiceJson).when(myCdsServiceRegistry).getCdsServiceJson(serviceId);
		// execute
		final CdsHooksExtension actual = myFixture.deserializeExtension(serviceId, extension);
		// validate
		assertThat(actual).isNull();
	}

	@Test
	void deserializeRequestContextShouldDeserializeValidContext() throws JsonProcessingException {
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
}
