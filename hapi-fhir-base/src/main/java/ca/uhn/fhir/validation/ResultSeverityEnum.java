package ca.uhn.fhir.validation;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ResultSeverityEnum {

	/**
	 * The issue has no relation to the degree of success of the action
	 */
	INFORMATION("information"),
	
	/**
	 * The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired
	 */
	WARNING("warning"),
	
	/**
	 * The issue is sufficiently important to cause the action to fail
	 */
	ERROR("error"),

	/**
	 * The issue caused the action to fail, and no further checking could be performed
	 */
	FATAL("fatal");

	private static Map<String, ResultSeverityEnum> ourValues;
	private String myCode;

	private ResultSeverityEnum(String theCode) {
		myCode = theCode;
	}

	public String getCode() {
		return myCode;
	}

	public static ResultSeverityEnum fromCode(String theCode) {
		if (ourValues == null) {
			HashMap<String, ResultSeverityEnum> values = new HashMap<String, ResultSeverityEnum>();
			for (ResultSeverityEnum next : values()) {
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
	 * <code>ResultSeverityEnum resultSeverityEnum = ResultSeverityEnum.values[ordinal];</code>
	 */
	public static final ResultSeverityEnum values[] = values();
}
