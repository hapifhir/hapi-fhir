
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

public enum EncounterTypeEnum {

	/**
	 * Display: <b>Annual diabetes mellitus screening</b><br/>
	 * Code Value: <b>ADMS</b>
	 */
	ANNUAL_DIABETES_MELLITUS_SCREENING("ADMS", "http://hl7.org/fhir/encounter-type"),
	
	/**
	 * Display: <b>Bone drilling/bone marrow punction in clinic</b><br/>
	 * Code Value: <b>BD/BM-clin</b>
	 */
	BONE_DRILLING_BONE_MARROW_PUNCTION_IN_CLINIC("BD/BM-clin", "http://hl7.org/fhir/encounter-type"),
	
	/**
	 * Display: <b>Infant colon screening - 60 minutes</b><br/>
	 * Code Value: <b>CCS60</b>
	 */
	INFANT_COLON_SCREENING___60_MINUTES("CCS60", "http://hl7.org/fhir/encounter-type"),
	
	/**
	 * Display: <b>Outpatient Kenacort injection</b><br/>
	 * Code Value: <b>OKI</b>
	 */
	OUTPATIENT_KENACORT_INJECTION("OKI", "http://hl7.org/fhir/encounter-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/encounter-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/encounter-type";

	/**
	 * Name for this Value Set:
	 * EncounterType
	 */
	public static final String VALUESET_NAME = "EncounterType";

	private static Map<String, EncounterTypeEnum> CODE_TO_ENUM = new HashMap<String, EncounterTypeEnum>();
	private static Map<String, Map<String, EncounterTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, EncounterTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (EncounterTypeEnum next : EncounterTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, EncounterTypeEnum>());
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
	public EncounterTypeEnum forCode(String theCode) {
		EncounterTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EncounterTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<EncounterTypeEnum>() {
		@Override
		public String toCodeString(EncounterTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(EncounterTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public EncounterTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public EncounterTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, EncounterTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	EncounterTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
