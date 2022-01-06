package ca.uhn.fhir.jpa.entity;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum is used to indicate the pre-expansion status of a given ValueSet in the terminology tables. In this context,
 * an expanded ValueSet has its included concepts stored in the terminology tables as well.
 */
public enum TermValueSetPreExpansionStatusEnum {
	/*
	 * Sorting agnostic.
	 */

	NOT_EXPANDED("notExpanded", "The ValueSet is waiting to be picked up and pre-expanded by a scheduled task."),
	EXPANSION_IN_PROGRESS("expansionInProgress", "The ValueSet has been picked up by a scheduled task and pre-expansion is in progress."),
	EXPANDED("expanded", "The ValueSet has been picked up by a scheduled task and pre-expansion is complete."),
	FAILED_TO_EXPAND("failedToExpand", "The ValueSet has been picked up by a scheduled task and pre-expansion has failed.");

	private static Map<String, TermValueSetPreExpansionStatusEnum> ourValues;
	private String myCode;
	private String myDescription;

	TermValueSetPreExpansionStatusEnum(String theCode, String theDescription) {
		myCode = theCode;
		myDescription = theDescription;
	}

	public String getCode() {
		return myCode;
	}

	public String getDescription() {
		return myDescription;
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
