package ca.uhn.fhir.jpa.binary.api;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * This deserializer exists to fix a break that changing this property name caused.
 * in 7.2.0 we went from blobId to binaryContentId. However this did not consider installations using filesystem
 * mode storage in which the data on disk was not updated, and existing stored details used `blobId`. This causes
 * Jackson deserialization failures which are tough to recover from without manually modifying all those stored details
 * on disk.
 * <p>
 * <p>
 * This class is a shim to support the old and new names.
 */
class BinaryContentIdDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
		JsonNode node = jp.getCodec().readTree(jp);
		JsonNode binaryContentIdNode = node.get("binaryContentId");
		if (binaryContentIdNode != null && !binaryContentIdNode.isNull()) {
			return binaryContentIdNode.asText();
		}
		JsonNode blobIdNode = node.get("blobId");
		if (blobIdNode != null && !blobIdNode.isNull()) {
			return blobIdNode.asText();
		}
		return null;
	}
}
