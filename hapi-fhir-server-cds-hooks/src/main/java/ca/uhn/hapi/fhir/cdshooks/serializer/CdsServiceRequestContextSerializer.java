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
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestContextJson;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;

public class CdsServiceRequestContextSerializer extends StdSerializer<CdsServiceRequestContextJson> {
	private final IParser myParser;
	private final ObjectMapper myObjectMapper;

	public CdsServiceRequestContextSerializer(FhirContext theFhirContext, ObjectMapper theObjectMapper) {
		super(CdsServiceRequestContextJson.class);
		myParser = theFhirContext.newJsonParser().setPrettyPrint(true);
		myObjectMapper = theObjectMapper;
	}

	@Override
	public void serialize(
			CdsServiceRequestContextJson theContext, JsonGenerator theJsonGenerator, SerializerProvider theProvider)
			throws IOException {
		theJsonGenerator.writeStartObject();
		for (String key : theContext.getKeys()) {
			theJsonGenerator.writeFieldName(key);
			Object value = theContext.get(key);
			String json;
			if (value instanceof IBaseResource) {
				IBaseResource resource = (IBaseResource) value;
				json = myParser.encodeResourceToString(resource);
			} else {
				json = myObjectMapper.writeValueAsString(value);
			}
			theJsonGenerator.writeRawValue(json);
		}
		theJsonGenerator.writeEndObject();
	}
}
