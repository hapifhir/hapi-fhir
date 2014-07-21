
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

public enum ImmunizationRecommendationStatusCodesEnum {

	/**
	 * Display: <b>due</b><br/>
	 * Code Value: <b>due</b>
	 *
	 * The patient is due for their next vaccination
	 */
	DUE("due", "http://hl7.org/fhir/immunization-recommendation-status"),
	
	/**
	 * Display: <b>overdue</b><br/>
	 * Code Value: <b>overdue</b>
	 *
	 * The patient is considered overdue for their next vaccination
	 */
	OVERDUE("overdue", "http://hl7.org/fhir/immunization-recommendation-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/immunization-recommendation-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/immunization-recommendation-status";

	/**
	 * Name for this Value Set:
	 * Immunization Recommendation Status Codes
	 */
	public static final String VALUESET_NAME = "Immunization Recommendation Status Codes";

	private static Map<String, ImmunizationRecommendationStatusCodesEnum> CODE_TO_ENUM = new HashMap<String, ImmunizationRecommendationStatusCodesEnum>();
	private static Map<String, Map<String, ImmunizationRecommendationStatusCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ImmunizationRecommendationStatusCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ImmunizationRecommendationStatusCodesEnum next : ImmunizationRecommendationStatusCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ImmunizationRecommendationStatusCodesEnum>());
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
	public ImmunizationRecommendationStatusCodesEnum forCode(String theCode) {
		ImmunizationRecommendationStatusCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ImmunizationRecommendationStatusCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ImmunizationRecommendationStatusCodesEnum>() {
		@Override
		public String toCodeString(ImmunizationRecommendationStatusCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ImmunizationRecommendationStatusCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ImmunizationRecommendationStatusCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ImmunizationRecommendationStatusCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ImmunizationRecommendationStatusCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ImmunizationRecommendationStatusCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
