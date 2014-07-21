
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

public enum ValueSetStatusEnum {

	/**
	 * Code Value: <b>draft</b>
	 *
	 * This valueset is still under development.
	 */
	DRAFT("draft", "http://hl7.org/fhir/valueset-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * This valueset is ready for normal use.
	 */
	ACTIVE("active", "http://hl7.org/fhir/valueset-status"),
	
	/**
	 * Code Value: <b>retired</b>
	 *
	 * This valueset has been withdrawn or superceded and should no longer be used.
	 */
	RETIRED("retired", "http://hl7.org/fhir/valueset-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/valueset-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/valueset-status";

	/**
	 * Name for this Value Set:
	 * ValueSetStatus
	 */
	public static final String VALUESET_NAME = "ValueSetStatus";

	private static Map<String, ValueSetStatusEnum> CODE_TO_ENUM = new HashMap<String, ValueSetStatusEnum>();
	private static Map<String, Map<String, ValueSetStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ValueSetStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ValueSetStatusEnum next : ValueSetStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ValueSetStatusEnum>());
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
	public ValueSetStatusEnum forCode(String theCode) {
		ValueSetStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ValueSetStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ValueSetStatusEnum>() {
		@Override
		public String toCodeString(ValueSetStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ValueSetStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ValueSetStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ValueSetStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ValueSetStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ValueSetStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
