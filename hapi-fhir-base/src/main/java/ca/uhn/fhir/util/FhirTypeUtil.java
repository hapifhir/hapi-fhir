/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

public final class FhirTypeUtil {

	private FhirTypeUtil() {}

	/**
	 * Returns true if the type is a primitive fhir type
	 * (ie, a type that is IPrimitiveType), false otherwise.
	 */
	public static boolean isPrimitiveType(String theFhirType) {
		switch (theFhirType) {
			default:
				// non-primitive type (or unknown type)
				return false;
			case "string":
			case "code":
			case "markdown":
			case "id":
			case "uri":
			case "url":
			case "canonical":
			case "oid":
			case "uuid":
			case "boolean":
			case "unsignedInt":
			case "positiveInt":
			case "decimal":
			case "integer64":
			case "integer":
			case "date":
			case "dateTime":
			case "time":
			case "instant":
			case "base64Binary":
				return true;
		}
	}
}
