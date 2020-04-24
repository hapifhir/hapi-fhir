package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParserErrorHandler;
import ca.uhn.fhir.parser.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Iterator;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;

class TolerantJsonParser extends JsonParser {

	TolerantJsonParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		super(theContext, theParserErrorHandler);
	}

	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		try {
			return super.parseResource(theResourceType, theMessageString);
		} catch (DataFormatException e) {

			/*
			 * The following is a hacky and gross workaround until the following PR is hopefully merged:
			 * https://github.com/FasterXML/jackson-core/pull/611
			 *
			 * The issue this solves is that under Gson it was possible to store JSON containing
			 * decimal numbers with no leading integer (e.g. .123) and numbers with double leading
			 * zeros (e.g. 000.123).
			 *
			 * These don't parse in Jackson (which is valid behaviour, these aren't ok according to the
			 * JSON spec), meaning we can be stuck with data in the database that can't be loaded back out.
			 *
			 * Note that if we fix this in the future to rely on Jackson natively handing this
			 * nicely we may or may not be able to remove some code from
			 * ParserState.Primitive state too.
			 */

			String msg = defaultString(e.getMessage());
			if (msg.contains("Unexpected character ('.' (code 46))") || msg.contains("Invalid numeric value: Leading zeroes not allowed")) {
				Gson gson = new Gson();

				JsonObject object = gson.fromJson(theMessageString, JsonObject.class);
				String corrected = gson.toJson(object);

				return super.parseResource(theResourceType, corrected);
			}

			throw e;
		}
	}

}
