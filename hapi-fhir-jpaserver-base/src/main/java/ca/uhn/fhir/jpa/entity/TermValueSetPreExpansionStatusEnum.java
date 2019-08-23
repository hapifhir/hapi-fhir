package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum is used to indicate the pre-expansion status of a given ValueSet in the terminology tables. In this context,
 * an expanded ValueSet has its included concepts stored in the terminology tables as well.
 */
public enum TermValueSetPreExpansionStatusEnum {
	/**
	 * Sorting agnostic.
	 */

	NOT_EXPANDED("notExpanded"),
	EXPANSION_IN_PROGRESS("expansionInProgress"),
	EXPANDED("expanded"),
	FAILED_TO_EXPAND("failedToExpand");

	private static Map<String, TermValueSetPreExpansionStatusEnum> ourValues;
	private String myCode;

	TermValueSetPreExpansionStatusEnum(String theCode) {
		myCode = theCode;
	}

	public String getCode() {
		return myCode;
	}

	public static TermValueSetPreExpansionStatusEnum fromCode(String theCode) {
		if (ourValues == null) {
			HashMap<String, TermValueSetPreExpansionStatusEnum> values = new HashMap<String, TermValueSetPreExpansionStatusEnum>();
			for (TermValueSetPreExpansionStatusEnum next : values()) {
				values.put(next.getCode(), next);
			}
			ourValues = Collections.unmodifiableMap(values);
		}
		return ourValues.get(theCode);
	}

	/**
	 * Convert from Enum ordinal to Enum type.
	 *
	 * Usage:
	 *
	 * <code>TermValueSetExpansionStatusEnum termValueSetExpansionStatusEnum = TermValueSetExpansionStatusEnum.values[ordinal];</code>
	 */
	public static final TermValueSetPreExpansionStatusEnum values[] = values();
}
