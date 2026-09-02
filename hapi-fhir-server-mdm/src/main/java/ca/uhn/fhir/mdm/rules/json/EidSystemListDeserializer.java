/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.i18n.Msg;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deserializes the values of the {@code eidSystems} map in an MDM rules document.
 * <p>
 * A resource type may be mapped either to a single EID system URI or to an array of them. The scalar
 * form is escalated to a one-element list so that older rule definitions, which supported only one
 * EID system per resource type, continue to deserialize unchanged.
 * </p>
 * <pre>
 * "eidSystems": {
 *     "Patient":      ["http://example.com/mrn", "http://example.com/npi"],
 *     "Practitioner": "http://example.com/npi"
 * }
 * </pre>
 * <p>
 * Declaration order is preserved, as it determines which system is treated as the primary one for a
 * resource type.
 * </p>
 */
// Created by claude-opus-5
public class EidSystemListDeserializer extends JsonDeserializer<List<String>> {

	/**
	 * @param theParser the parser positioned on the value for one resource type
	 * @param theContext the active deserialization context
	 * @return the configured EID systems, in declaration order; never {@literal null}
	 * @throws JsonMappingException if the value is neither a string nor an array of strings
	 */
	@Override
	public List<String> deserialize(JsonParser theParser, DeserializationContext theContext) throws IOException {
		JsonToken token = theParser.currentToken();

		if (token == JsonToken.VALUE_STRING) {
			return Collections.singletonList(theParser.getText());
		}

		if (token == JsonToken.START_ARRAY) {
			List<String> retVal = new ArrayList<>();
			while (theParser.nextToken() != JsonToken.END_ARRAY) {
				if (theParser.currentToken() != JsonToken.VALUE_STRING) {
					throw invalidValue(theParser);
				}
				retVal.add(theParser.getText());
			}
			return retVal;
		}

		throw invalidValue(theParser);
	}

	/**
	 * An explicit {@literal null} is treated as "no EID systems configured for this resource type"
	 * rather than as a null list, so that callers never have to null-check.
	 */
	@Override
	public List<String> getNullValue(DeserializationContext theContext) {
		return Collections.emptyList();
	}

	private JsonMappingException invalidValue(JsonParser theParser) throws IOException {
		return JsonMappingException.from(
				theParser,
				Msg.code(3046) + "eidSystems entry for '" + theParser.currentName()
						+ "' must be an EID system URI or an array of EID system URIs");
	}
}
