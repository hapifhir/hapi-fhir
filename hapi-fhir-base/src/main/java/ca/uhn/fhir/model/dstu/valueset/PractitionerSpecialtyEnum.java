
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

public enum PractitionerSpecialtyEnum {

	/**
	 * Display: <b>Cardiologist</b><br/>
	 * Code Value: <b>cardio</b>
	 */
	CARDIOLOGIST("cardio", "http://hl7.org/fhir/practitioner-specialty"),
	
	/**
	 * Display: <b>Dentist</b><br/>
	 * Code Value: <b>dent</b>
	 */
	DENTIST("dent", "http://hl7.org/fhir/practitioner-specialty"),
	
	/**
	 * Display: <b>Dietary consultant</b><br/>
	 * Code Value: <b>dietary</b>
	 */
	DIETARY_CONSULTANT("dietary", "http://hl7.org/fhir/practitioner-specialty"),
	
	/**
	 * Display: <b>Midwife</b><br/>
	 * Code Value: <b>midw</b>
	 */
	MIDWIFE("midw", "http://hl7.org/fhir/practitioner-specialty"),
	
	/**
	 * Display: <b>Systems architect</b><br/>
	 * Code Value: <b>sysarch</b>
	 */
	SYSTEMS_ARCHITECT("sysarch", "http://hl7.org/fhir/practitioner-specialty"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/practitioner-specialty
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/practitioner-specialty";

	/**
	 * Name for this Value Set:
	 * PractitionerSpecialty
	 */
	public static final String VALUESET_NAME = "PractitionerSpecialty";

	private static Map<String, PractitionerSpecialtyEnum> CODE_TO_ENUM = new HashMap<String, PractitionerSpecialtyEnum>();
	private static Map<String, Map<String, PractitionerSpecialtyEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, PractitionerSpecialtyEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (PractitionerSpecialtyEnum next : PractitionerSpecialtyEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, PractitionerSpecialtyEnum>());
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
	public PractitionerSpecialtyEnum forCode(String theCode) {
		PractitionerSpecialtyEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<PractitionerSpecialtyEnum> VALUESET_BINDER = new IValueSetEnumBinder<PractitionerSpecialtyEnum>() {
		@Override
		public String toCodeString(PractitionerSpecialtyEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(PractitionerSpecialtyEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public PractitionerSpecialtyEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public PractitionerSpecialtyEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, PractitionerSpecialtyEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	PractitionerSpecialtyEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
