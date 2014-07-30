
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

public enum LocationTypeEnum {

	/**
	 * Display: <b>Building</b><br/>
	 * Code Value: <b>bu</b>
	 */
	BUILDING("bu", "http://hl7.org/fhir/location-physical-type"),
	
	/**
	 * Display: <b>Wing</b><br/>
	 * Code Value: <b>wi</b>
	 */
	WING("wi", "http://hl7.org/fhir/location-physical-type"),
	
	/**
	 * Display: <b>Corridor</b><br/>
	 * Code Value: <b>co</b>
	 */
	CORRIDOR("co", "http://hl7.org/fhir/location-physical-type"),
	
	/**
	 * Display: <b>Room</b><br/>
	 * Code Value: <b>ro</b>
	 */
	ROOM("ro", "http://hl7.org/fhir/location-physical-type"),
	
	/**
	 * Display: <b>Vehicle</b><br/>
	 * Code Value: <b>ve</b>
	 */
	VEHICLE("ve", "http://hl7.org/fhir/location-physical-type"),
	
	/**
	 * Display: <b>House</b><br/>
	 * Code Value: <b>ho</b>
	 */
	HOUSE("ho", "http://hl7.org/fhir/location-physical-type"),
	
	/**
	 * Display: <b>Cabinet</b><br/>
	 * Code Value: <b>ca</b>
	 */
	CABINET("ca", "http://hl7.org/fhir/location-physical-type"),
	
	/**
	 * Display: <b>Road</b><br/>
	 * Code Value: <b>rd</b>
	 */
	ROAD("rd", "http://hl7.org/fhir/location-physical-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/location-physical-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/location-physical-type";

	/**
	 * Name for this Value Set:
	 * LocationType
	 */
	public static final String VALUESET_NAME = "LocationType";

	private static Map<String, LocationTypeEnum> CODE_TO_ENUM = new HashMap<String, LocationTypeEnum>();
	private static Map<String, Map<String, LocationTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, LocationTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (LocationTypeEnum next : LocationTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, LocationTypeEnum>());
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
	public LocationTypeEnum forCode(String theCode) {
		LocationTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<LocationTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<LocationTypeEnum>() {
		@Override
		public String toCodeString(LocationTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(LocationTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public LocationTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public LocationTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, LocationTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	LocationTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
