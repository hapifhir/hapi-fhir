
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

public enum ConditionStatusEnum {

	/**
	 * Code Value: <b>provisional</b>
	 *
	 * This is a tentative diagnosis - still a candidate that is under consideration.
	 */
	PROVISIONAL("provisional", "http://hl7.org/fhir/condition-status"),
	
	/**
	 * Code Value: <b>working</b>
	 *
	 * The patient is being treated on the basis that this is the condition, but it is still not confirmed.
	 */
	WORKING("working", "http://hl7.org/fhir/condition-status"),
	
	/**
	 * Code Value: <b>confirmed</b>
	 *
	 * There is sufficient diagnostic and/or clinical evidence to treat this as a confirmed condition.
	 */
	CONFIRMED("confirmed", "http://hl7.org/fhir/condition-status"),
	
	/**
	 * Code Value: <b>refuted</b>
	 *
	 * This condition has been ruled out by diagnostic and clinical evidence.
	 */
	REFUTED("refuted", "http://hl7.org/fhir/condition-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/condition-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/condition-status";

	/**
	 * Name for this Value Set:
	 * ConditionStatus
	 */
	public static final String VALUESET_NAME = "ConditionStatus";

	private static Map<String, ConditionStatusEnum> CODE_TO_ENUM = new HashMap<String, ConditionStatusEnum>();
	private static Map<String, Map<String, ConditionStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ConditionStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ConditionStatusEnum next : ConditionStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ConditionStatusEnum>());
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
	public ConditionStatusEnum forCode(String theCode) {
		ConditionStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ConditionStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ConditionStatusEnum>() {
		@Override
		public String toCodeString(ConditionStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ConditionStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ConditionStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ConditionStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ConditionStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ConditionStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
