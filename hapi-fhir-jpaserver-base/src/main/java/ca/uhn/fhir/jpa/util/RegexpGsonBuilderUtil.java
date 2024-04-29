/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The whole purpose of his class is to ease construction of a non-trivial gson.JsonObject,
 * which can't be done the easy way in this case (using a JSON string), because there are
 * valid regex strings which break gson, as this: ".*\\^Donor$"
 */
public class RegexpGsonBuilderUtil {

	private RegexpGsonBuilderUtil() {}

	/**
	 * Builds a json object as this sample:
	 * {"regexp":{" + thePropName + ":{"value":" + theValue + "}}}
	 */
	public static JsonObject toGson(String thePropName, String theValue) {
		JsonObject valueJO = new JsonObject();
		valueJO.add("value", new JsonPrimitive(theValue));

		JsonObject systemValueJO = new JsonObject();
		systemValueJO.add(thePropName, valueJO);

		JsonObject regexpJO = new JsonObject();
		regexpJO.add("regexp", systemValueJO);

		JsonArray a = new JsonArray();
		return regexpJO;
	}
}
