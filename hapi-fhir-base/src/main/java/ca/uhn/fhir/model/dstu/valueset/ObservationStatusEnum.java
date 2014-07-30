
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

public enum ObservationStatusEnum {

	/**
	 * Code Value: <b>registered</b>
	 *
	 * The existence of the observation is registered, but there is no result yet available.
	 */
	REGISTERED("registered", "http://hl7.org/fhir/observation-status"),
	
	/**
	 * Code Value: <b>preliminary</b>
	 *
	 * This is an initial or interim observation: data may be incomplete or unverified.
	 */
	PRELIMINARY("preliminary", "http://hl7.org/fhir/observation-status"),
	
	/**
	 * Code Value: <b>final</b>
	 *
	 * The observation is complete and verified by an authorized person.
	 */
	FINAL("final", "http://hl7.org/fhir/observation-status"),
	
	/**
	 * Code Value: <b>amended</b>
	 *
	 * The observation has been modified subsequent to being Final, and is complete and verified by an authorized person.
	 */
	AMENDED("amended", "http://hl7.org/fhir/observation-status"),
	
	/**
	 * Code Value: <b>cancelled</b>
	 *
	 * The observation is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/observation-status"),
	
	/**
	 * Code Value: <b>entered in error</b>
	 *
	 * The observation has been withdrawn following previous Final release.
	 */
	ENTERED_IN_ERROR("entered in error", "http://hl7.org/fhir/observation-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/observation-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/observation-status";

	/**
	 * Name for this Value Set:
	 * ObservationStatus
	 */
	public static final String VALUESET_NAME = "ObservationStatus";

	private static Map<String, ObservationStatusEnum> CODE_TO_ENUM = new HashMap<String, ObservationStatusEnum>();
	private static Map<String, Map<String, ObservationStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ObservationStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ObservationStatusEnum next : ObservationStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ObservationStatusEnum>());
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
	public ObservationStatusEnum forCode(String theCode) {
		ObservationStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ObservationStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ObservationStatusEnum>() {
		@Override
		public String toCodeString(ObservationStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ObservationStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ObservationStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ObservationStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ObservationStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ObservationStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
