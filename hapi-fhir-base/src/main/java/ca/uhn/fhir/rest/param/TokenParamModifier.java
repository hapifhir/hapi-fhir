package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.util.HashMap;
import java.util.Map;

/**
 * Modifiers for {@link TokenParam}
 */
public enum TokenParamModifier {
	/** 
	 * :above
	 */
	ABOVE(":above"),
	
	/** 
	 * :above
	 */
	BELOW(":below"),
	
	/** 
	 * :in
	 */
	IN(":in"),
	
	/** 
	 * :not
	 */
	NOT(":not"),
	
	/** 
	 * :not-in
	 */
	NOT_IN(":not-in"),
	
	/** 
	 * :text
	 */
	TEXT(":text");
	
	private static final Map<String, TokenParamModifier> VALUE_TO_ENUM;

	static {
		Map<String, TokenParamModifier> valueToEnum = new HashMap<String, TokenParamModifier>();
		for (TokenParamModifier next : values()) {
			valueToEnum.put(next.getValue(), next);
		}
		VALUE_TO_ENUM = valueToEnum;
	}
	private final String myValue;

	private TokenParamModifier(String theValue) {
		myValue = theValue;
	}
	
	public String getValue() {
		return myValue;
	}

	public static TokenParamModifier forValue(String theValue) {
		return VALUE_TO_ENUM.get(theValue);
	}
	
}
