
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

public enum ObservationReliabilityEnum {

	/**
	 * Code Value: <b>ok</b>
	 *
	 * The result has no reliability concerns.
	 */
	OK("ok", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>ongoing</b>
	 *
	 * An early estimate of value; measurement is still occurring.
	 */
	ONGOING("ongoing", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>early</b>
	 *
	 * An early estimate of value; processing is still occurring.
	 */
	EARLY("early", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>questionable</b>
	 *
	 * The observation value should be treated with care.
	 */
	QUESTIONABLE("questionable", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>calibrating</b>
	 *
	 * The result has been generated while calibration is occurring.
	 */
	CALIBRATING("calibrating", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>error</b>
	 *
	 * The observation could not be completed because of an error.
	 */
	ERROR("error", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>unknown</b>
	 *
	 * No observation value was available.
	 */
	UNKNOWN("unknown", "http://hl7.org/fhir/observation-reliability"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/observation-reliability
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/observation-reliability";

	/**
	 * Name for this Value Set:
	 * ObservationReliability
	 */
	public static final String VALUESET_NAME = "ObservationReliability";

	private static Map<String, ObservationReliabilityEnum> CODE_TO_ENUM = new HashMap<String, ObservationReliabilityEnum>();
	private static Map<String, Map<String, ObservationReliabilityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ObservationReliabilityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ObservationReliabilityEnum next : ObservationReliabilityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ObservationReliabilityEnum>());
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
	public ObservationReliabilityEnum forCode(String theCode) {
		ObservationReliabilityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ObservationReliabilityEnum> VALUESET_BINDER = new IValueSetEnumBinder<ObservationReliabilityEnum>() {
		@Override
		public String toCodeString(ObservationReliabilityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ObservationReliabilityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ObservationReliabilityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ObservationReliabilityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ObservationReliabilityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ObservationReliabilityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
