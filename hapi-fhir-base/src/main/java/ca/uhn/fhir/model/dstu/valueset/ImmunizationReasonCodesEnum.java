
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

public enum ImmunizationReasonCodesEnum {

	/**
	 * Code Value: <b>429060002</b>
	 */
	_429060002("429060002", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>281657000</b>
	 */
	_281657000("281657000", "http://snomed.info/sct"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/immunization-reason
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/immunization-reason";

	/**
	 * Name for this Value Set:
	 * Immunization Reason Codes
	 */
	public static final String VALUESET_NAME = "Immunization Reason Codes";

	private static Map<String, ImmunizationReasonCodesEnum> CODE_TO_ENUM = new HashMap<String, ImmunizationReasonCodesEnum>();
	private static Map<String, Map<String, ImmunizationReasonCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ImmunizationReasonCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ImmunizationReasonCodesEnum next : ImmunizationReasonCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ImmunizationReasonCodesEnum>());
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
	public ImmunizationReasonCodesEnum forCode(String theCode) {
		ImmunizationReasonCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ImmunizationReasonCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ImmunizationReasonCodesEnum>() {
		@Override
		public String toCodeString(ImmunizationReasonCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ImmunizationReasonCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ImmunizationReasonCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ImmunizationReasonCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ImmunizationReasonCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ImmunizationReasonCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
