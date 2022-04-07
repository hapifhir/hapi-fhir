package ca.uhn.fhir.rest.param;

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

import java.util.*;

/**
 * Comparator/qualifier for values used in REST params, such as {@link DateParam}, {@link NumberParam}, and
 * {@link QuantityParam}
 * 
 * @since 1.5
 */
public enum ParamPrefixEnum {

	/**
	 * Code Value: <b>eq</b>
	 *
	 * The actual value is equal to the given value
	 */
	APPROXIMATE("ap"),

	/**
	 * Code Value: <b>eb</b>
	 *
	 * The range of the search value does overlap not with the range of the target value, and the range above the search value contains the range of the target value
	 */
	ENDS_BEFORE("eb"),

	/**
	 * Code Value: <b>eq</b>
	 *
	 * The actual value is equal to the given value
	 */
	EQUAL("eq"),

	/**
	 * Code Value: <b>gt</b>
	 *
	 * The actual value is greater than the given value.
	 */
	GREATERTHAN("gt"),

	/**
	 * Code Value: <b>ge</b>
	 *
	 * The actual value is greater than or equal to the given value.
	 */
	GREATERTHAN_OR_EQUALS("ge"),

	/**
	 * Code Value: <b>lt</b>
	 *
	 * The actual value is less than the given value.
	 */
	LESSTHAN("lt"),

	/**
	 * Code Value: <b>le</b>
	 *
	 * The actual value is less than or equal to the given value.
	 */
	LESSTHAN_OR_EQUALS("le"), 
	
	/**
	 * Code Value: <b>ne</b>
	 *
	 * The actual value is not equal to the given value
	 */
	NOT_EQUAL("ne"),
	
	/**
	 * Code Value: <b>sa</b>
	 *
	 * The range of the search value does not overlap with the range of the target value, and the range below the search value contains the range of the target value
	 */
	STARTS_AFTER("sa");
	
	private static final Map<String, ParamPrefixEnum> VALUE_TO_PREFIX;

	static {
		HashMap<String, ParamPrefixEnum> valueToPrefix = new HashMap<String, ParamPrefixEnum>();
		for (ParamPrefixEnum next : values()) {
			valueToPrefix.put(next.getValue(), next);
		}

		VALUE_TO_PREFIX = Collections.unmodifiableMap(valueToPrefix);
	}

	private final String myValue;

	private ParamPrefixEnum(String theValue) {
		myValue = theValue;
	}

	/**
	 * Returns the value, e.g. <code>lt</code> or <code>eq</code>
	 */
	public String getValue() {
		return myValue;
	}

	/**
	 * Returns the prefix associated with a given DSTU2+ value (e.g. <code>lt</code> or <code>eq</code>)
	 * 
	 * @param theValue
	 *           e.g. <code>&lt;</code> or <code>~</code>
	 * @return The prefix, or <code>null</code> if no prefix matches the value
	 */
	public static ParamPrefixEnum forValue(String theValue) {
		return VALUE_TO_PREFIX.get(theValue);
	}
}
