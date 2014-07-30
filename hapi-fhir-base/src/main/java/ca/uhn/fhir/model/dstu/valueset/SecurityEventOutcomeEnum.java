
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

public enum SecurityEventOutcomeEnum {

	/**
	 * Display: <b>Success</b><br/>
	 * Code Value: <b>0</b>
	 *
	 * The operation completed successfully (whether with warnings or not).
	 */
	SUCCESS("0", "http://hl7.org/fhir/security-event-outcome"),
	
	/**
	 * Display: <b>Minor failure</b><br/>
	 * Code Value: <b>4</b>
	 *
	 * The action was not successful due to some kind of catered for error (often equivalent to an HTTP 400 response).
	 */
	MINOR_FAILURE("4", "http://hl7.org/fhir/security-event-outcome"),
	
	/**
	 * Display: <b>Serious failure</b><br/>
	 * Code Value: <b>8</b>
	 *
	 * The action was not successful due to some kind of unexpected error (often equivalent to an HTTP 500 response).
	 */
	SERIOUS_FAILURE("8", "http://hl7.org/fhir/security-event-outcome"),
	
	/**
	 * Display: <b>Major failure</b><br/>
	 * Code Value: <b>12</b>
	 *
	 * An error of such magnitude occurred that the system is not longer available for use (i.e. the system died).
	 */
	MAJOR_FAILURE("12", "http://hl7.org/fhir/security-event-outcome"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/security-event-outcome
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/security-event-outcome";

	/**
	 * Name for this Value Set:
	 * SecurityEventOutcome
	 */
	public static final String VALUESET_NAME = "SecurityEventOutcome";

	private static Map<String, SecurityEventOutcomeEnum> CODE_TO_ENUM = new HashMap<String, SecurityEventOutcomeEnum>();
	private static Map<String, Map<String, SecurityEventOutcomeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SecurityEventOutcomeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SecurityEventOutcomeEnum next : SecurityEventOutcomeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SecurityEventOutcomeEnum>());
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
	public SecurityEventOutcomeEnum forCode(String theCode) {
		SecurityEventOutcomeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SecurityEventOutcomeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SecurityEventOutcomeEnum>() {
		@Override
		public String toCodeString(SecurityEventOutcomeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SecurityEventOutcomeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SecurityEventOutcomeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SecurityEventOutcomeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SecurityEventOutcomeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SecurityEventOutcomeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
