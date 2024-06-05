package ca.uhn.fhir.rest.api.server.storage;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A deserialized version of the PID
 * Must be converted to appropriate PID type.
 */
public class SerializablePid extends BaseResourcePersistentId<Object> {

	private final Object myId;

	private final String myIdStr;
	private String myAssociatedResourceId;

	SerializablePid(String theResourceType, Object theId) {
		super(theResourceType);
		myId = theId;
		myIdStr = theId == null ? null : String.valueOf(theId);
	}

	public SerializablePid(String theResourceType, Object theId, String theIdStr) {
		super(theResourceType);
		myId = theId;
		myIdStr = theIdStr;
	}

	/**
	 * If the id couldn't be parsed into a known object, it will be a map of fields to properties
	 */
	@Override
	public Object getId() {
		return myId;
	}

	public String getIdStr() {
		return myIdStr;
	}

	public String getAssociatedResourceIdStr() {
		return myAssociatedResourceId;
	}

	public void setAssociatedResourceIdStr(String theAssociatedResourceId) {
		myAssociatedResourceId = theAssociatedResourceId;
	}

	public static SerializablePid fromPID(IResourcePersistentId<?> thePid) {
		Object id = thePid.getId();
		SerializablePid pid = new SerializablePid(thePid.getResourceType(), id);
		pid.setVersion(thePid.getVersion());
		if (thePid.getAssociatedResourceId() != null) {
			pid.setAssociatedResourceIdStr(thePid.getAssociatedResourceId().getValueAsString());
		}
		return pid;
	}

	@SuppressWarnings("rawtypes")
	public static class IResourcePersistenceSerializer extends StdSerializer<IResourcePersistentId> {

		IResourcePersistenceSerializer() {
			super(IResourcePersistentId.class);
		}

		@Override
		public void serialize(IResourcePersistentId value, JsonGenerator gen, SerializerProvider provider)
				throws IOException {
			SerializablePid serializablePid = SerializablePid.fromPID(value);

			gen.writeStartObject();
			gen.writeStringField("resourceType", serializablePid.getResourceType());
			Object idObj = value.getId();

			// check what kind it is
			JsonSerializer<Object> serializer = provider.findTypedValueSerializer(idObj.getClass(), true, null);
			if (serializer.isEmpty(provider, idObj)) {
				// treat it like a string
				gen.writeStringField("id", serializablePid.getIdStr());
			} else {
				gen.writeFieldName("id");
				serializer.serialize(idObj, gen, provider);
			}
			if (serializablePid.getVersion() != null) {
				gen.writeNumberField("version", serializablePid.getVersion());
			}
			if (serializablePid.getAssociatedResourceIdStr() != null) {
				gen.writeStringField("associatedResourceId", serializablePid.getAssociatedResourceIdStr());
			}
			gen.writeEndObject();
		}
	}

	public static class IResourcePersistenceDeserializer extends StdDeserializer<IResourcePersistentId<?>> {

		IResourcePersistenceDeserializer() {
			super(IResourcePersistentId.class);
		}

		@Override
		public IResourcePersistentId<?> deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JacksonException {
			JsonNode node = p.getCodec().readTree(p);

			String resourceType = node.get("resourceType").asText();
			String associatedResourceId = node.get("associatedResourceId").asText();
			Long version = node.get("version").asLong();
			JsonNode idNode = node.get("id");

			String idStr = idNode.asText();

			Object id;
			if (idNode.isObject()) {
				id = p.getCodec().treeToValue(idNode, HashMap.class);
			} else if (idNode.isArray()) {
				id = p.getCodec().treeToValue(idNode, ArrayList.class);
			} else if (idNode.isNumber() && idNode.isIntegralNumber()) {
				// we'll treat all ints as longs (We use longs)
				id = idNode.asLong();
			} else {
				// TODO - other options might be needed;
				// but currently only String and Long are used
				// so the rest are left for now as just 'text'
				id = idStr;
			}

			SerializablePid pid = new SerializablePid(resourceType, id, idStr);
			pid.setVersion(version);
			pid.setAssociatedResourceIdStr(associatedResourceId);

			return pid;
		}
	}
}
