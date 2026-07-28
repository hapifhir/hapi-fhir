/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.rest.api.server.cdshooks.CdsServiceRequestContextJson;
import org.hl7.fhir.instance.model.api.IBaseResource;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.json.JsonMapper;

import java.util.LinkedHashMap;

public class CdsServiceRequestContextDeserializer extends StdDeserializer<CdsServiceRequestContextJson> {
	private final IParser myParser;
	private final JsonMapper myJsonMapper;

	public CdsServiceRequestContextDeserializer(FhirContext theFhirContext, JsonMapper theJsonMapper) {
		super(CdsServiceRequestContextJson.class);
		myParser = theFhirContext.newJsonParser().setPrettyPrint(true);
		myJsonMapper = theJsonMapper;
	}

	@Override
	public CdsServiceRequestContextJson deserialize(JsonParser theJsonParser, DeserializationContext theContext) {
		// First deserialize the context as a LinkedHashMap
		LinkedHashMap<String, Object> map = myJsonMapper.readValue(theJsonParser, LinkedHashMap.class);
		CdsServiceRequestContextJson retval = new CdsServiceRequestContextJson();

		for (String key : map.keySet()) {
			Object value = map.get(key);
			// Convert LinkedHashMap entries to Resources
			if (value instanceof LinkedHashMap) {
				String json = myJsonMapper.writeValueAsString(value);
				IBaseResource resource = myParser.parseResource(json);
				retval.put(key, resource);
			} else {
				retval.put(key, value);
			}
		}
		return retval;
	}
}
