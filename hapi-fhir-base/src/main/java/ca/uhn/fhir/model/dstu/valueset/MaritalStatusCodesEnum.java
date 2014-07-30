
package ca.uhn.fhir.model.dstu.valueset;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum MaritalStatusCodesEnum {

	/**
	 * Display: <b>unmarried</b><br/>
	 * Code Value: <b>U</b>
	 *
	 * The person is not presently married. The marital history is not known or stated
	 */
	UNMARRIED("U", "http://hl7.org/fhir/marital-status"),
	
	/**
	 * Code Value: <b>A</b>
	 */
	A("A", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>D</b>
	 */
	D("D", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>I</b>
	 */
	I("I", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>L</b>
	 */
	L("L", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>M</b>
	 */
	M("M", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>P</b>
	 */
	P("P", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>S</b>
	 */
	S("S", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>T</b>
	 */
	T("T", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>W</b>
	 */
	W("W", "http://hl7.org/fhir/v3/MaritalStatus"),
	
	/**
	 * Code Value: <b>UNK</b>
	 */
	UNK("UNK", "http://hl7.org/fhir/v3/NullFlavor"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/marital-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/marital-status";

	/**
	 * Name for this Value Set:
	 * Marital Status Codes
	 */
	public static final String VALUESET_NAME = "Marital Status Codes";

	private static Map<String, MaritalStatusCodesEnum> CODE_TO_ENUM = new HashMap<String, MaritalStatusCodesEnum>();
	private static Map<String, Map<String, MaritalStatusCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, MaritalStatusCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (MaritalStatusCodesEnum next : MaritalStatusCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, MaritalStatusCodesEnum>());
			}
			SYSTEM_TO_CODE_TO_ENUM.get(next.getSystem()).put(next.getCode(), next);			
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}
	
	/**
	 * Returns the code system associated with this enumerated value
	 */
	public String getSystem() {
		return mySystem;
	}
	
	/**
	 * Returns the enumerated value associated with this code
	 */
	public MaritalStatusCodesEnum forCode(String theCode) {
		MaritalStatusCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<MaritalStatusCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<MaritalStatusCodesEnum>() {
		@Override
		public String toCodeString(MaritalStatusCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(MaritalStatusCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public MaritalStatusCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public MaritalStatusCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, MaritalStatusCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	MaritalStatusCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
