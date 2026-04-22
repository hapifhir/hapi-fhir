/*
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class JsonPatchUtils {

	public static <T extends IBaseResource> T apply(FhirContext theCtx, T theResourceToUpdate, String thePatchBody) {
		// Parse the patch
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, false);

		JsonFactory factory = mapper.getFactory();

		final JsonPatch patch;
		try {
			JsonParser parser = factory.createParser(thePatchBody);
			JsonNode jsonPatchNode = mapper.readTree(parser);
			patch = JsonPatch.fromJson(jsonPatchNode);

			JsonNode originalJsonDocument =
					mapper.readTree(theCtx.newJsonParser().encodeResourceToString(theResourceToUpdate));

			// Pre-populate missing parent arrays for "add" operations targeting array elements.
			// When a FHIR resource has an empty repeating field (e.g., Group with no members),
			// the serialized JSON omits the key entirely. JSON Patch "add" at /field/0 requires
			// the parent /field to exist, so we insert an empty array where needed.
			ensureParentArraysExist(jsonPatchNode, originalJsonDocument);

			JsonNode after = patch.apply(originalJsonDocument);

			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) theResourceToUpdate.getClass();

			String postPatchedContent = mapper.writeValueAsString(after);

			IParser fhirJsonParser = theCtx.newJsonParser();
			fhirJsonParser.setParserErrorHandler(new StrictErrorHandler());

			T retVal;
			try {
				retVal = fhirJsonParser.parseResource(clazz, postPatchedContent);
			} catch (DataFormatException e) {
				String resourceId = theResourceToUpdate
						.getIdElement()
						.toUnqualifiedVersionless()
						.getValue();
				String resourceType =
						theCtx.getResourceDefinition(theResourceToUpdate).getName();
				resourceId = defaultString(resourceId, resourceType);
				String msg = theCtx.getLocalizer()
						.getMessage(JsonPatchUtils.class, "failedToApplyPatch", resourceId, e.getMessage());
				throw new InvalidRequestException(Msg.code(1271) + msg);
			}
			return retVal;

		} catch (IOException | JsonPatchException theE) {
			throw new InvalidRequestException(Msg.code(1272) + theE.getMessage());
		}
	}

	/**
	 * For each "add" operation in the patch whose path targets an array element (e.g. {@code /member/0}),
	 * ensures the parent field exists in the document as an empty array. This handles the case where
	 * HAPI's JSON serializer omits empty repeating fields entirely.
	 *
	 * @author Claude Opus 4.6
	 */
	private static void ensureParentArraysExist(JsonNode thePatchNode, JsonNode theDocument) {
		if (!thePatchNode.isArray() || !theDocument.isObject()) {
			return;
		}
		ObjectNode documentObject = (ObjectNode) theDocument;
		for (JsonNode operation : thePatchNode) {
			JsonNode opNode = operation.get("op");
			JsonNode pathNode = operation.get("path");
			if (opNode == null || pathNode == null) {
				continue;
			}
			if (!"add".equals(opNode.asText())) {
				continue;
			}
			String path = pathNode.asText();
			// Match paths like /field/index where index is a non-negative integer
			int lastSlash = path.lastIndexOf('/');
			if (lastSlash <= 0) {
				continue;
			}
			String index = path.substring(lastSlash + 1);
			if (!index.matches("\\d+") && !"-".equals(index)) {
				continue;
			}
			String parentPath = path.substring(1, lastSlash); // strip leading '/'
			if (parentPath.contains("/")) {
				// Nested paths not handled — only top-level parent arrays
				continue;
			}
			if (!documentObject.has(parentPath)) {
				documentObject.putArray(parentPath);
			}
		}
	}
}
