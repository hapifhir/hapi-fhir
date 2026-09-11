package ca.uhn.fhir.serializer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FhirResourceDeserializerTest {

	private static final String RESOURCE_JSON = """
			{
			  "resourceType": "Patient",
			  "id": "patient-123",
			  "active": true,
			  "name": [
			    {
			      "family": "Example",
			      "given": ["Pat"]
			    }
			  ]
			}
			""";
	private static final String EXPECTED_MINIFIED_JSON = "{\"resourceType\":\"Patient\",\"id\":\"patient-123\",\"active\":true,\"name\":[{\"family\":\"Example\",\"given\":[\"Pat\"]}]}";

	@Test
	void deserializesIBaseResourceThroughRegisteredDeserializer() throws IOException {
		FhirContext fhirContext = mock(FhirContext.class);
		IParser parser = mock(IParser.class);
		IBaseResource resource = mock(IBaseResource.class);
		DeserializationContext deserializationContext = mock(DeserializationContext.class);
		JsonParser jsonParser = mock(JsonParser.class);

		when(fhirContext.newJsonParser()).thenReturn(parser);
		when(parser.setPrettyPrint(true)).thenReturn(parser);
		when(parser.parseResource(EXPECTED_MINIFIED_JSON)).thenReturn(resource);
		when(deserializationContext.readTree(jsonParser)).thenReturn(new JsonMapper().readTree(RESOURCE_JSON));
		FhirResourceDeserializer deserializer = new FhirResourceDeserializer(fhirContext);

		IBaseResource actual = deserializer.deserialize(jsonParser, deserializationContext);

		assertThat(actual).isSameAs(resource);

		verify(parser).setPrettyPrint(true);
		verify(parser).parseResource(EXPECTED_MINIFIED_JSON);
		verify(fhirContext).newJsonParser();
	}
}
