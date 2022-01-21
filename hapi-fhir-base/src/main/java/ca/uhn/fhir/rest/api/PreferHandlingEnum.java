package ca.uhn.fhir.rest.api;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import java.util.HashMap;

/**
 * Represents values for "handling" value as provided in the the <a href="http://hl7.org/fhir/search.html">FHIR Search Spec</a>.
 */
public enum PreferHandlingEnum {

	STRICT(Constants.HEADER_PREFER_HANDLING_STRICT), LENIENT(Constants.HEADER_PREFER_HANDLING_LENIENT);

	private static HashMap<String, PreferHandlingEnum> ourValues;
	private String myHeaderValue;

	PreferHandlingEnum(String theHeaderValue) {
		myHeaderValue = theHeaderValue;
	}

	public String getHeaderValue() {
		return myHeaderValue;
	}

	public static PreferHandlingEnum fromHeaderValue(String theHeaderValue) {
		if (ourValues == null) {
			HashMap<String, PreferHandlingEnum> values = new HashMap<>();
			for (PreferHandlingEnum next : PreferHandlingEnum.values()) {
				values.put(next.getHeaderValue(), next);
			}
			ourValues = values;
		}
		return ourValues.get(theHeaderValue);
	}

}
