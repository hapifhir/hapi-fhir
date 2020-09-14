package ca.uhn.fhir.rest.server.messaging.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.springframework.messaging.MessageHeaders;

import java.io.IOException;

public class MessageHeaderDeserializer extends JsonDeserializer<MessageHeaders> {
	@Override
	public MessageHeaders deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		return null;
	}

	@Override
	public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
		return super.deserializeWithType(p, ctxt, typeDeserializer);
	}
}
