
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

public enum CarePlanStatusEnum {

	/**
	 * Code Value: <b>planned</b>
	 *
	 * The plan is in development or awaiting use but is not yet intended to be acted upon.
	 */
	PLANNED("planned", "http://hl7.org/fhir/care-plan-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * The plan is intended to be followed and used as part of patient care.
	 */
	ACTIVE("active", "http://hl7.org/fhir/care-plan-status"),
	
	/**
	 * Code Value: <b>completed</b>
	 *
	 * The plan is no longer in use and is not expected to be followed or used in patient care.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/care-plan-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/care-plan-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/care-plan-status";

	/**
	 * Name for this Value Set:
	 * CarePlanStatus
	 */
	public static final String VALUESET_NAME = "CarePlanStatus";

	private static Map<String, CarePlanStatusEnum> CODE_TO_ENUM = new HashMap<String, CarePlanStatusEnum>();
	private static Map<String, Map<String, CarePlanStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CarePlanStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CarePlanStatusEnum next : CarePlanStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CarePlanStatusEnum>());
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
	public CarePlanStatusEnum forCode(String theCode) {
		CarePlanStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CarePlanStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<CarePlanStatusEnum>() {
		@Override
		public String toCodeString(CarePlanStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CarePlanStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CarePlanStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CarePlanStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CarePlanStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CarePlanStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
