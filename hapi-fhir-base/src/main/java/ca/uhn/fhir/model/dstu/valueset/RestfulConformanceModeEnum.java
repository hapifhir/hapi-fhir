
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

public enum RestfulConformanceModeEnum {

	/**
	 * Code Value: <b>client</b>
	 *
	 * The application acts as a server for this resource.
	 */
	CLIENT("client", "http://hl7.org/fhir/restful-conformance-mode"),
	
	/**
	 * Code Value: <b>server</b>
	 *
	 * The application acts as a client for this resource.
	 */
	SERVER("server", "http://hl7.org/fhir/restful-conformance-mode"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/restful-conformance-mode
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/restful-conformance-mode";

	/**
	 * Name for this Value Set:
	 * RestfulConformanceMode
	 */
	public static final String VALUESET_NAME = "RestfulConformanceMode";

	private static Map<String, RestfulConformanceModeEnum> CODE_TO_ENUM = new HashMap<String, RestfulConformanceModeEnum>();
	private static Map<String, Map<String, RestfulConformanceModeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, RestfulConformanceModeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (RestfulConformanceModeEnum next : RestfulConformanceModeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, RestfulConformanceModeEnum>());
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
	public RestfulConformanceModeEnum forCode(String theCode) {
		RestfulConformanceModeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<RestfulConformanceModeEnum> VALUESET_BINDER = new IValueSetEnumBinder<RestfulConformanceModeEnum>() {
		@Override
		public String toCodeString(RestfulConformanceModeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(RestfulConformanceModeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public RestfulConformanceModeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public RestfulConformanceModeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, RestfulConformanceModeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	RestfulConformanceModeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
