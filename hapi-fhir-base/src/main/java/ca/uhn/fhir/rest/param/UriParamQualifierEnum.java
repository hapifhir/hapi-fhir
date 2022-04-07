package ca.uhn.fhir.rest.param;

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

/**
 * Qualifiers for {@link UriParam}
 */
public enum UriParamQualifierEnum {

	/**
	 * The search parameter is a concept with the form <code>[system]|[code]</code>, 
	 * and the search parameter tests whether the coding in a resource subsumes the 
	 * specified search code. For example, the search concept has an is-a relationship 
	 * with the coding in the resource, and this includes the coding itself.
	 * <p>
	 * Value <code>:above</code>
	 * </p> 
	 */
	ABOVE(":above"),
	
	/**
	 * The search parameter is a concept with the form <code>[system]|[code]</code>, 
	 * and the search parameter tests whether the coding in a resource subsumes the 
	 * specified search code. For example, the search concept has an is-a relationship 
	 * with the coding in the resource, and this includes the coding itself.
	 * <p>
	 * Value <code>:below</code>
	 * </p> 
	 */
	BELOW(":below");
	
	private static final Map<String, UriParamQualifierEnum> KEY_TO_VALUE;

	static {
		HashMap<String, UriParamQualifierEnum> key2value = new HashMap<String, UriParamQualifierEnum>();
		for (UriParamQualifierEnum next : values()) {
			key2value.put(next.getValue(), next);
		}
		KEY_TO_VALUE = Collections.unmodifiableMap(key2value);
	}

	private final String myValue;
	private UriParamQualifierEnum(String theValue) {
		myValue = theValue;
	}
	
	/**
	 * Returns the qualifier value, e.g. <code>:below</code>
	 */
	public String getValue() {
		return myValue;
	}
	
	/**
	 * Returns the {@link UriParamQualifierEnum} matching the given qualifier value, such as <code>:below</code>,
	 * or <code>null</code>
	 */
	public static UriParamQualifierEnum forValue(String theValue) {
		return KEY_TO_VALUE.get(theValue);
	}
	
}
