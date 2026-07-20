package ca.uhn.fhir.serializer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;

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

		when(fhirContext.newJsonParser()).thenReturn(parser);
		when(parser.setPrettyPrint(true)).thenReturn(parser);
		when(parser.parseResource(EXPECTED_MINIFIED_JSON)).thenReturn(resource);
		FhirResourceDeserializer deserializer = new FhirResourceDeserializer(fhirContext);

		JsonFactory jsonFactory = new JsonFactory();
		try (JsonParser jsonParser = jsonFactory.createParser(RESOURCE_JSON)) {
			jsonParser.setCodec(new ObjectMapper());

			IBaseResource actual = deserializer.deserialize(jsonParser, deserializationContext);

			assertThat(actual).isSameAs(resource);
		}

		verify(parser).setPrettyPrint(true);
		verify(parser).parseResource(EXPECTED_MINIFIED_JSON);
		verify(fhirContext).newJsonParser();
	}
}
