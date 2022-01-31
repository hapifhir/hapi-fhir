package ca.uhn.fhir.jpa.patch;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.intellij.lang.annotations.Language;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class JsonPatchUtils {

	public static <T extends IBaseResource> T apply(FhirContext theCtx, T theResourceToUpdate, @Language("JSON") String thePatchBody) {
		// Parse the patch
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, false);

		JsonFactory factory = mapper.getFactory();

		final JsonPatch patch;
		try {
			com.fasterxml.jackson.core.JsonParser parser = factory.createParser(thePatchBody);
			JsonNode jsonPatchNode = mapper.readTree(parser);
			patch = JsonPatch.fromJson(jsonPatchNode);

			JsonNode originalJsonDocument = mapper.readTree(theCtx.newJsonParser().encodeResourceToString(theResourceToUpdate));
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
				String resourceId = theResourceToUpdate.getIdElement().toUnqualifiedVersionless().getValue();
				String resourceType = theCtx.getResourceDefinition(theResourceToUpdate).getName();
				resourceId = defaultString(resourceId, resourceType);
				String msg = theCtx.getLocalizer().getMessage(JsonPatchUtils.class, "failedToApplyPatch", resourceId, e.getMessage());
				throw new InvalidRequestException(Msg.code(1271) + msg);
			}
			return retVal;

		} catch (IOException | JsonPatchException theE) {
			throw new InvalidRequestException(Msg.code(1272) + theE.getMessage());
		}

	}

}
