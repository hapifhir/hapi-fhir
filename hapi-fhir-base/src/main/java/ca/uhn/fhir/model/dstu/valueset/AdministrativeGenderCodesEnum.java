
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

public enum AdministrativeGenderCodesEnum {

	/**
	 * Code Value: <b>F</b>
	 */
	F("F", "http://hl7.org/fhir/v3/AdministrativeGender"),
	
	/**
	 * Code Value: <b>M</b>
	 */
	M("M", "http://hl7.org/fhir/v3/AdministrativeGender"),
	
	/**
	 * Code Value: <b>UN</b>
	 */
	UN("UN", "http://hl7.org/fhir/v3/AdministrativeGender"),
	
	/**
	 * Code Value: <b>UNK</b>
	 */
	UNK("UNK", "http://hl7.org/fhir/v3/NullFlavor"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/administrative-gender
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/administrative-gender";

	/**
	 * Name for this Value Set:
	 * Administrative Gender Codes
	 */
	public static final String VALUESET_NAME = "Administrative Gender Codes";

	private static Map<String, AdministrativeGenderCodesEnum> CODE_TO_ENUM = new HashMap<String, AdministrativeGenderCodesEnum>();
	private static Map<String, Map<String, AdministrativeGenderCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdministrativeGenderCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdministrativeGenderCodesEnum next : AdministrativeGenderCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdministrativeGenderCodesEnum>());
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
	public AdministrativeGenderCodesEnum forCode(String theCode) {
		AdministrativeGenderCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdministrativeGenderCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdministrativeGenderCodesEnum>() {
		@Override
		public String toCodeString(AdministrativeGenderCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdministrativeGenderCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdministrativeGenderCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdministrativeGenderCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdministrativeGenderCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdministrativeGenderCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
