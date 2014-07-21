
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

public enum EncounterStateEnum {

	/**
	 * Code Value: <b>planned</b>
	 *
	 * The Encounter has not yet started.
	 */
	PLANNED("planned", "http://hl7.org/fhir/encounter-state"),
	
	/**
	 * Code Value: <b>in progress</b>
	 *
	 * The Encounter has begun and the patient is present / the practitioner and the patient are meeting.
	 */
	IN_PROGRESS("in progress", "http://hl7.org/fhir/encounter-state"),
	
	/**
	 * Code Value: <b>onleave</b>
	 *
	 * The Encounter has begun, but the patient is temporarily on leave.
	 */
	ONLEAVE("onleave", "http://hl7.org/fhir/encounter-state"),
	
	/**
	 * Code Value: <b>finished</b>
	 *
	 * The Encounter has ended.
	 */
	FINISHED("finished", "http://hl7.org/fhir/encounter-state"),
	
	/**
	 * Code Value: <b>cancelled</b>
	 *
	 * The Encounter has ended before it has begun.
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/encounter-state"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/encounter-state
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/encounter-state";

	/**
	 * Name for this Value Set:
	 * EncounterState
	 */
	public static final String VALUESET_NAME = "EncounterState";

	private static Map<String, EncounterStateEnum> CODE_TO_ENUM = new HashMap<String, EncounterStateEnum>();
	private static Map<String, Map<String, EncounterStateEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, EncounterStateEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (EncounterStateEnum next : EncounterStateEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, EncounterStateEnum>());
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
	public EncounterStateEnum forCode(String theCode) {
		EncounterStateEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EncounterStateEnum> VALUESET_BINDER = new IValueSetEnumBinder<EncounterStateEnum>() {
		@Override
		public String toCodeString(EncounterStateEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(EncounterStateEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public EncounterStateEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public EncounterStateEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, EncounterStateEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	EncounterStateEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
