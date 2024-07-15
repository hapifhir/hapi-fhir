/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
import java.util.Map;

public class CdsServiceRequestJsonDeserializer extends StdDeserializer<CdsServiceRequestJson> {

	private final CdsServiceRegistryImpl myCdsServiceRegistry;
	private final ObjectMapper myObjectMapper;
	private final FhirContext myFhirContext;
	private final IParser myParser;

	public CdsServiceRequestJsonDeserializer(CdsServiceRegistryImpl theCdsServiceRegistry, FhirContext theFhirContext) {
		super(CdsServiceRequestJson.class);
		myCdsServiceRegistry = theCdsServiceRegistry;
		myFhirContext = theFhirContext;
		myParser = myFhirContext.newJsonParser().setPrettyPrint(true);
		// We create a new ObjectMapper instead of using the one from the ApplicationContext to avoid an infinite loop
		// during deserialization.
		myObjectMapper = new ObjectMapper();
		configureObjectMapper(myObjectMapper);
	}

	@Override
	public CdsServiceRequestJson deserialize(JsonParser theJsonParser, DeserializationContext theDeserializationContext)
			throws IOException {
		final JsonNode cdsServiceRequestJsonNode = theJsonParser.getCodec().readTree(theJsonParser);
		final JsonNode hookNode = cdsServiceRequestJsonNode.get("hook");
		final JsonNode extensionNode = cdsServiceRequestJsonNode.get("extension");
		final JsonNode requestContext = cdsServiceRequestJsonNode.get("context");
		final CdsServiceRequestJson cdsServiceRequestJson =
				myObjectMapper.treeToValue(cdsServiceRequestJsonNode, CdsServiceRequestJson.class);
		if (extensionNode != null) {
			CdsHooksExtension myRequestExtension = deserializeExtension(hookNode.textValue(), extensionNode.toString());
			cdsServiceRequestJson.setExtension(myRequestExtension);
		}
		if (requestContext != null) {
			LinkedHashMap<String, Object> map =
					myObjectMapper.readValue(requestContext.toString(), LinkedHashMap.class);
			cdsServiceRequestJson.setContext(deserializeRequestContext(map));
		}
		return cdsServiceRequestJson;
	}

	void configureObjectMapper(ObjectMapper theObjectMapper) {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(IBaseResource.class, new FhirResourceDeserializer(myFhirContext));
		theObjectMapper.registerModule(module);
		// set this as we will need to ignore properties which are not defined by specific implementation.
		theObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	CdsHooksExtension deserializeExtension(String theServiceId, String theExtension) throws JsonProcessingException {
		final CdsServiceJson cdsServicesJson = myCdsServiceRegistry.getCdsServiceJson(theServiceId);
		Class<? extends CdsHooksExtension> extensionClass = cdsServicesJson.getExtensionClass();
		if (extensionClass == null) {
			return null;
		}
		return myObjectMapper.readValue(theExtension, extensionClass);
	}

	CdsServiceRequestContextJson deserializeRequestContext(LinkedHashMap<String, Object> theMap)
			throws JsonProcessingException {
		final CdsServiceRequestContextJson cdsServiceRequestContextJson = new CdsServiceRequestContextJson();
		for (Map.Entry<String, Object> entry : theMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			// Convert LinkedHashMap entries to Resources
			if (value instanceof LinkedHashMap) {
				String json = myObjectMapper.writeValueAsString(value);
				IBaseResource resource = myParser.parseResource(json);
				cdsServiceRequestContextJson.put(key, resource);
			} else {
				cdsServiceRequestContextJson.put(key, value);
			}
		}
		return cdsServiceRequestContextJson;
	}
}
