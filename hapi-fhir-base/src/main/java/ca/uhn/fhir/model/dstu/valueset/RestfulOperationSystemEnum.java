
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

public enum RestfulOperationSystemEnum {

	/**
	 * Code Value: <b>transaction</b>
	 */
	TRANSACTION("transaction", "http://hl7.org/fhir/restful-operation"),
	
	/**
	 * Code Value: <b>search-system</b>
	 */
	SEARCH_SYSTEM("search-system", "http://hl7.org/fhir/restful-operation"),
	
	/**
	 * Code Value: <b>history-system</b>
	 */
	HISTORY_SYSTEM("history-system", "http://hl7.org/fhir/restful-operation"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/system-restful-operation
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/system-restful-operation";

	/**
	 * Name for this Value Set:
	 * RestfulOperationSystem
	 */
	public static final String VALUESET_NAME = "RestfulOperationSystem";

	private static Map<String, RestfulOperationSystemEnum> CODE_TO_ENUM = new HashMap<String, RestfulOperationSystemEnum>();
	private static Map<String, Map<String, RestfulOperationSystemEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, RestfulOperationSystemEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (RestfulOperationSystemEnum next : RestfulOperationSystemEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, RestfulOperationSystemEnum>());
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
	public RestfulOperationSystemEnum forCode(String theCode) {
		RestfulOperationSystemEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<RestfulOperationSystemEnum> VALUESET_BINDER = new IValueSetEnumBinder<RestfulOperationSystemEnum>() {
		@Override
		public String toCodeString(RestfulOperationSystemEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(RestfulOperationSystemEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public RestfulOperationSystemEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public RestfulOperationSystemEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, RestfulOperationSystemEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	RestfulOperationSystemEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
