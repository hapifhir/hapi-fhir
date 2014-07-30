
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

public enum LocationModeEnum {

	/**
	 * Code Value: <b>instance</b>
	 *
	 * The Location resource represents a specific instance of a Location.
	 */
	INSTANCE("instance", "http://hl7.org/fhir/location-mode"),
	
	/**
	 * Code Value: <b>kind</b>
	 *
	 * The Location represents a class of Locations.
	 */
	KIND("kind", "http://hl7.org/fhir/location-mode"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/location-mode
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/location-mode";

	/**
	 * Name for this Value Set:
	 * LocationMode
	 */
	public static final String VALUESET_NAME = "LocationMode";

	private static Map<String, LocationModeEnum> CODE_TO_ENUM = new HashMap<String, LocationModeEnum>();
	private static Map<String, Map<String, LocationModeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, LocationModeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (LocationModeEnum next : LocationModeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, LocationModeEnum>());
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
	public LocationModeEnum forCode(String theCode) {
		LocationModeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<LocationModeEnum> VALUESET_BINDER = new IValueSetEnumBinder<LocationModeEnum>() {
		@Override
		public String toCodeString(LocationModeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(LocationModeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public LocationModeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public LocationModeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, LocationModeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	LocationModeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
