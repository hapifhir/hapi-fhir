package ca.uhn.fhir.jpa.util.jsonpatch;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import net.riotopsys.json_patch.JsonPatch;
import net.riotopsys.json_patch.JsonPath;
import net.riotopsys.json_patch.operation.AddOperation;
import net.riotopsys.json_patch.operation.MoveOperation;
import net.riotopsys.json_patch.operation.RemoveOperation;
import net.riotopsys.json_patch.operation.ReplaceOperation;

public class JsonPatchUtils {

	public static <T extends IBaseResource> T apply(FhirContext theCtx, T theResourceToUpdate, String thePatchBody) {
		JsonPatch parsedPatch = new JsonPatch();
		
		// Parse the patch
		Gson gson = JsonParser.newGson();
		JsonElement jsonElement = gson.fromJson(thePatchBody, JsonElement.class);
		JsonArray array = jsonElement.getAsJsonArray();
		for (JsonElement nextElement : array) {
			JsonObject nextElementAsObject = (JsonObject) nextElement;
			
			String opName = nextElementAsObject.get("op").getAsString();
			if ("add".equals(opName)) {
				AddOperation op = new AddOperation(toPath(nextElementAsObject), nextElementAsObject.get("value"));
				parsedPatch.add(op);
			} else if ("remove".equals(opName)) {
				RemoveOperation op = new RemoveOperation(toPath(nextElementAsObject));
				parsedPatch.add(op);
			} else if ("replace".equals(opName)) {
				ReplaceOperation op = new ReplaceOperation(toPath(nextElementAsObject), nextElementAsObject.get("value"));
				parsedPatch.add(op);
			} else if ("copy".equals(opName)) {
				CopyOperation op = new CopyOperation(toPath(nextElementAsObject), toFromPath(nextElementAsObject));
				parsedPatch.add(op);
			} else if ("move".equals(opName)) {
				MoveOperation op = new MoveOperation(toPath(nextElementAsObject), toFromPath(nextElementAsObject));
				parsedPatch.add(op);
			} else {
				throw new InvalidRequestException("Invalid JSON PATCH operation: " + opName);
			}
			
		}
		
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) theResourceToUpdate.getClass();
		
		JsonElement originalJsonDocument = gson.fromJson(theCtx.newJsonParser().encodeResourceToString(theResourceToUpdate), JsonElement.class);
		JsonElement target = parsedPatch.apply(originalJsonDocument);
		T retVal = theCtx.newJsonParser().parseResource(clazz, gson.toJson(target));
		
		return retVal;
	}

	private static JsonPath toFromPath(JsonObject nextElementAsObject) {
		return new JsonPath(nextElementAsObject.get("from").getAsString());
	}

	private static JsonPath toPath(JsonObject nextElementAsObject) {
		return new JsonPath(nextElementAsObject.get("path").getAsString());
	}

}
