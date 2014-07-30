
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

public enum CarePlanActivityCategoryEnum {

	/**
	 * Code Value: <b>diet</b>
	 *
	 * Plan for the patient to consume food of a specified nature.
	 */
	DIET("diet", "http://hl7.org/fhir/care-plan-activity-category"),
	
	/**
	 * Code Value: <b>drug</b>
	 *
	 * Plan for the patient to consume/receive a drug, vaccine or other product.
	 */
	DRUG("drug", "http://hl7.org/fhir/care-plan-activity-category"),
	
	/**
	 * Code Value: <b>encounter</b>
	 *
	 * Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.).
	 */
	ENCOUNTER("encounter", "http://hl7.org/fhir/care-plan-activity-category"),
	
	/**
	 * Code Value: <b>observation</b>
	 *
	 * Plan to capture information about a patient (vitals, labs, diagnostic images, etc.).
	 */
	OBSERVATION("observation", "http://hl7.org/fhir/care-plan-activity-category"),
	
	/**
	 * Code Value: <b>procedure</b>
	 *
	 * Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.).
	 */
	PROCEDURE("procedure", "http://hl7.org/fhir/care-plan-activity-category"),
	
	/**
	 * Code Value: <b>supply</b>
	 *
	 * Plan to provide something to the patient (medication, medical supply, etc.).
	 */
	SUPPLY("supply", "http://hl7.org/fhir/care-plan-activity-category"),
	
	/**
	 * Code Value: <b>other</b>
	 *
	 * Some other form of action.
	 */
	OTHER("other", "http://hl7.org/fhir/care-plan-activity-category"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/care-plan-activity-category
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/care-plan-activity-category";

	/**
	 * Name for this Value Set:
	 * CarePlanActivityCategory
	 */
	public static final String VALUESET_NAME = "CarePlanActivityCategory";

	private static Map<String, CarePlanActivityCategoryEnum> CODE_TO_ENUM = new HashMap<String, CarePlanActivityCategoryEnum>();
	private static Map<String, Map<String, CarePlanActivityCategoryEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CarePlanActivityCategoryEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CarePlanActivityCategoryEnum next : CarePlanActivityCategoryEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CarePlanActivityCategoryEnum>());
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
	public CarePlanActivityCategoryEnum forCode(String theCode) {
		CarePlanActivityCategoryEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CarePlanActivityCategoryEnum> VALUESET_BINDER = new IValueSetEnumBinder<CarePlanActivityCategoryEnum>() {
		@Override
		public String toCodeString(CarePlanActivityCategoryEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CarePlanActivityCategoryEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CarePlanActivityCategoryEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CarePlanActivityCategoryEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CarePlanActivityCategoryEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CarePlanActivityCategoryEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
