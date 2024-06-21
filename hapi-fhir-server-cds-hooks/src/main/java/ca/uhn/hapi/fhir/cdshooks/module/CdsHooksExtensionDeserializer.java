package ca.uhn.hapi.fhir.cdshooks.module;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CdsHooksExtensionDeserializer extends StdDeserializer<CdsHooksExtension> {
	protected CdsHooksExtensionDeserializer() {
		super(CdsHooksExtension.class);
	}

	@Override
	public CdsHooksExtension deserialize(JsonParser theJsonParser, DeserializationContext theDeserializationContext)
			throws IOException, JacksonException {
		return null;
	}
}
