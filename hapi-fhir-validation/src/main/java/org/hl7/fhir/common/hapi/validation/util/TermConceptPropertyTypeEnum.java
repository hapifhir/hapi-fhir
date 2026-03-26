/*-
 * #%L
 * HAPI FHIR JPA Server
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
package org.hl7.fhir.common.hapi.validation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * An enum representing the possible types of a concept property
 */
public enum TermConceptPropertyTypeEnum {

	/**
	 * Do not change order - the ordinal is used by hibernate in the column.
	 * TermConceptProperty#getType()
	 */

	/**
	 * String
	 */
	STRING("string", true),
	/**
	 * Code
	 */
	CODE("code", true),
	/**
	 * Coding
	 */
	CODING("Coding", false),
	/**
	 * Boolean values
	 */
	BOOLEAN("boolean", true),
	/**
	 * Integer values
	 */
	INTEGER("integer", true),
	/**
	 * Decimal or float values.
	 */
	DECIMAL("decimal", true),
	/**
	 * Date and time values.
	 */
	DATETIME("dateTime", true);

	private static final Logger ourLog = LoggerFactory.getLogger(TermConceptPropertyTypeEnum.class);
	private static final Map<String, TermConceptPropertyTypeEnum> ourDatatypeMap = new HashMap<>();
	private final String myDatatype;
	private final boolean myPrimitive;

	static {
		for (var value : TermConceptPropertyTypeEnum.values()) {
			ourDatatypeMap.put(value.myDatatype, value);
		}
	}

	TermConceptPropertyTypeEnum(String theDatatype, boolean thePrimitive) {
		myDatatype = theDatatype;
		myPrimitive = thePrimitive;
	}

	public boolean isPrimitive() {
		return myPrimitive;
	}

	public static TermConceptPropertyTypeEnum fromString(String theString) {
		TermConceptPropertyTypeEnum retVal;
		try {
			retVal =
					TermConceptPropertyTypeEnum.valueOf(defaultString(theString).toUpperCase(Locale.US));
		} catch (Exception e) {
			ourLog.warn("Unknown Concept Property Type: {}", theString);
			retVal = TermConceptPropertyTypeEnum.STRING;
		}
		return retVal;
	}

	public static TermConceptPropertyTypeEnum forDatatype(String theDatatype) {
		return ourDatatypeMap.get(theDatatype);
	}
}
