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
import com.fasterxml.jackson.databind.SerializerProvider;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.json.JsonMapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import tools.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CdsServiceRequestContextSerializer extends StdSerializer<CdsServiceRequestContextJson> {
	private final IParser myParser;
	private final JsonMapper myJsonMapper;

	public CdsServiceRequestContextSerializer(FhirContext theFhirContext, JsonMapper theJsonMapper) {
		super(CdsServiceRequestContextJson.class);
		myParser = theFhirContext.newJsonParser().setPrettyPrint(true);
		myJsonMapper = theJsonMapper;
	}

	@Override
	public void serialize(
			CdsServiceRequestContextJson theContext, JsonGenerator theJsonGenerator, SerializationContext theProvider) {
		theJsonGenerator.writeStartObject();
		for (String key : theContext.getKeys()) {
			theJsonGenerator.writeName(key);
			Object value = theContext.get(key);
			String json;
			if (value instanceof IBaseResource) {
				IBaseResource resource = (IBaseResource) value;
				json = myParser.encodeResourceToString(resource);
			} else {
				json = myJsonMapper.writeValueAsString(value);
			}
			theJsonGenerator.writeRawValue(json);
		}
		theJsonGenerator.writeEndObject();
	}
}
