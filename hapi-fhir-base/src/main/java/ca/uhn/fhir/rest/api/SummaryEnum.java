package ca.uhn.fhir.rest.api;

/*
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
import java.util.Map;

/**
 * Enum representing the values for the <code>_summary</code> search parameter
 */
public enum SummaryEnum {

	/**
	 * Search only: just return a count of the matching resources, without returning the actual matches
	 */
	COUNT("count"),

	/**
	 * Return only the 'text' element, and any mandatory elements
	 */
	TEXT("text"),

	/**
	 * Remove the text element
	 */
	DATA("data"),

	/**
	 * Return only those elements marked as 'summary' in the base definition of the resource(s)
	 */
	TRUE("true"),

	/**
	 * Return all parts of the resource(s)
	 */
	FALSE("false");
	
	private String myCode;
	private static Map<String, SummaryEnum> ourCodeToSummary = null;

	SummaryEnum(String theCode) {
		myCode = theCode;
	}

	public String getCode() {
		return myCode;
	}

	public static SummaryEnum fromCode(String theCode) {
		Map<String, SummaryEnum> c2s = ourCodeToSummary;
		if (c2s == null) {
			c2s = new HashMap<>();
			for (SummaryEnum next : values()) {
				c2s.put(next.getCode(), next);
			}
			ourCodeToSummary = c2s;
		}
		return c2s.get(theCode);
	}
	
}
