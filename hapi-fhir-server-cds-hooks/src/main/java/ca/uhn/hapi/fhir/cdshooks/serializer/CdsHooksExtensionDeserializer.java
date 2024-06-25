package ca.uhn.hapi.fhir.cdshooks.serializer;

import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServicesJson;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CdsHooksExtensionDeserializer extends StdDeserializer<CdsHooksExtension> {

	private final CdsServiceRegistryImpl myCdsServiceRegistry;
	private final ObjectMapper myObjectMapper;
	public CdsHooksExtensionDeserializer(CdsServiceRegistryImpl theCdsServiceRegistry, ObjectMapper theObjectMapper) {
		super(CdsHooksExtension.class);
		myCdsServiceRegistry = theCdsServiceRegistry;
		myObjectMapper = theObjectMapper;
	}

	@Override
	public CdsHooksExtension deserialize(JsonParser theJsonParser, DeserializationContext theDeserializationContext)
			throws IOException {
		JsonNode extensionNode = theJsonParser.getCodec().readTree(theJsonParser);
		TreeNode hookNode = theJsonParser.getCodec().readTree(theJsonParser).get("hook");
		CdsServicesJson cdsServicesJson = myCdsServiceRegistry.getCdsServicesJson();
		return new CdsHooksExtension();
	}
}
