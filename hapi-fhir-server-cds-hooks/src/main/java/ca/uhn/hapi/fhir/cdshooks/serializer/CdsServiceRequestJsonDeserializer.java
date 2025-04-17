/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsHooksExtension;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.LinkedHashMap;
import java.util.Map;

public class CdsServiceRequestJsonDeserializer {
	private final ObjectMapper myObjectMapper;
	private final FhirContext myFhirContext;
	private final IParser myParser;

	public CdsServiceRequestJsonDeserializer(
			@Nonnull FhirContext theFhirContext, @Nonnull ObjectMapper theObjectMapper) {
		myFhirContext = theFhirContext;
		myParser = myFhirContext.newJsonParser().setPrettyPrint(true);
		myObjectMapper = theObjectMapper;
	}

	public CdsServiceRequestJson deserialize(
			@Nonnull CdsServiceJson theCdsServiceJson, @Nonnull Object theCdsServiceRequestJson) {
		final JsonNode cdsServiceRequestJsonNode =
				myObjectMapper.convertValue(theCdsServiceRequestJson, JsonNode.class);
		final JsonNode contextNode = cdsServiceRequestJsonNode.get("context");
		validateHookInstance(cdsServiceRequestJsonNode.get("hookInstance"));
		validateHook(cdsServiceRequestJsonNode.get("hook"));
		validateContext(contextNode);
		try {
			final JsonNode extensionNode = cdsServiceRequestJsonNode.get("extension");
			final CdsServiceRequestJson cdsServiceRequestJson =
					myObjectMapper.convertValue(cdsServiceRequestJsonNode, CdsServiceRequestJson.class);
			LinkedHashMap<String, Object> map = myObjectMapper.readValue(contextNode.toString(), LinkedHashMap.class);
			cdsServiceRequestJson.setContext(deserializeContext(map));
			if (extensionNode != null) {
				CdsHooksExtension myRequestExtension =
						deserializeExtension(theCdsServiceJson, extensionNode.toString());
				cdsServiceRequestJson.setExtension(myRequestExtension);
			}
			return cdsServiceRequestJson;
		} catch (JsonProcessingException | IllegalArgumentException theEx) {
			throw new InvalidRequestException(Msg.code(2551) + "Invalid CdsServiceRequest received. " + theEx);
		}
	}

	CdsServiceRequestContextJson deserializeContext(LinkedHashMap<String, Object> theMap)
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

	private CdsHooksExtension deserializeExtension(
			@Nonnull CdsServiceJson theCdsServiceJson, @Nonnull String theExtension) throws JsonProcessingException {
		Class<? extends CdsHooksExtension> extensionClass = theCdsServiceJson.getExtensionClass();
		if (extensionClass == null) {
			return null;
		}
		return myObjectMapper.readValue(theExtension, extensionClass);
	}

	private void validateHook(JsonNode hookIdNode) {
		if (hookIdNode == null) {
			throw new InvalidRequestException(Msg.code(2549) + "hook cannot be null for a CdsServiceRequest.");
		}
	}

	private void validateHookInstance(JsonNode hookInstanceNode) {
		if (hookInstanceNode == null) {
			throw new InvalidRequestException(Msg.code(2548) + "hookInstance cannot be null for a CdsServiceRequest.");
		}
	}

	private void validateContext(JsonNode requestContextNode) {
		if (requestContextNode == null) {
			throw new InvalidRequestException(Msg.code(2550) + "context cannot be null for a CdsServiceRequest.");
		}
	}
}
