package ca.uhn.hapi.fhir.cdshooks.serializer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.serializer.FhirResourceDeserializer;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceRegistryImpl;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.util.LinkedHashMap;

public class CdsServiceRequestJsonDeserializer extends StdDeserializer<CdsServiceRequestJson> {

	private final CdsServiceRegistryImpl myCdsServiceRegistry;
	private final ObjectMapper myObjectMapper;
	private final FhirContext myFhirContext = FhirContext.forR4();
	private final IParser myParser = myFhirContext.newJsonParser().setPrettyPrint(true);

	public CdsServiceRequestJsonDeserializer(CdsServiceRegistryImpl theCdsServiceRegistry, ObjectMapper theObjectMapper) {
		super(CdsServiceRequestJson.class);
		myCdsServiceRegistry = theCdsServiceRegistry;
		myObjectMapper = theObjectMapper;
	}

	@Override
	public CdsServiceRequestJson deserialize(JsonParser theJsonParser, DeserializationContext theDeserializationContext)
		throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(IBaseResource.class, new FhirResourceDeserializer(myFhirContext));
		objectMapper.registerModule(module);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode cdsServiceRequestJsonNode = theJsonParser.getCodec().readTree(theJsonParser);
		JsonNode hookNode = cdsServiceRequestJsonNode.get("hook");
		JsonNode extensionNode = cdsServiceRequestJsonNode.get("extension");
		JsonNode requestContext = cdsServiceRequestJsonNode.get("context");
		CdsServiceRequestJson cdsServiceRequestJson1 = objectMapper.treeToValue(cdsServiceRequestJsonNode, CdsServiceRequestJson.class);
		if(extensionNode != null) {
			CdsServiceJson cdsServicesJson = myCdsServiceRegistry.getCdsServiceJson(hookNode.textValue());
			Class<? extends CdsHooksExtension> extensionClass = cdsServicesJson.getExtensionClass();
			if(extensionClass == null) {
				extensionClass = CdsHooksExtension.class;
			}
			CdsHooksExtension myRequestExtension = objectMapper.readValue(extensionNode.toString(),
				extensionClass);
			cdsServiceRequestJson1.setExtension(myRequestExtension);
		}

		if(requestContext != null) {
			LinkedHashMap<String, Object> map = objectMapper.readValue(
				requestContext.toString(), LinkedHashMap.class);
			cdsServiceRequestJson1.setContext(getContext(map));
		}
		return cdsServiceRequestJson1;
	}

	CdsServiceRequestContextJson getContext(LinkedHashMap<String, Object> theMap) throws JsonProcessingException {
		CdsServiceRequestContextJson retval = new CdsServiceRequestContextJson();
		for (String key : theMap.keySet()) {
			Object value = theMap.get(key);
			// Convert LinkedHashMap entries to Resources
			if (value instanceof LinkedHashMap) {
				String json = myObjectMapper.writeValueAsString(value);
				IBaseResource resource = myParser.parseResource(json);
				retval.put(key, resource);
			} else {
				retval.put(key, value);
			}
		}
		return retval;
	}


}
