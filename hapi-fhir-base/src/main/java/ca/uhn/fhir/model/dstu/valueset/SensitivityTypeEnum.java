
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

public enum SensitivityTypeEnum {

	/**
	 * Code Value: <b>allergy</b>
	 *
	 * Allergic Reaction.
	 */
	ALLERGY("allergy", "http://hl7.org/fhir/sensitivitytype"),
	
	/**
	 * Code Value: <b>intolerance</b>
	 *
	 * Non-Allergic Reaction.
	 */
	INTOLERANCE("intolerance", "http://hl7.org/fhir/sensitivitytype"),
	
	/**
	 * Code Value: <b>unknown</b>
	 *
	 * Unknown type.
	 */
	UNKNOWN("unknown", "http://hl7.org/fhir/sensitivitytype"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/sensitivitytype
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/sensitivitytype";

	/**
	 * Name for this Value Set:
	 * SensitivityType
	 */
	public static final String VALUESET_NAME = "SensitivityType";

	private static Map<String, SensitivityTypeEnum> CODE_TO_ENUM = new HashMap<String, SensitivityTypeEnum>();
	private static Map<String, Map<String, SensitivityTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SensitivityTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SensitivityTypeEnum next : SensitivityTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SensitivityTypeEnum>());
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
	public SensitivityTypeEnum forCode(String theCode) {
		SensitivityTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SensitivityTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SensitivityTypeEnum>() {
		@Override
		public String toCodeString(SensitivityTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SensitivityTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SensitivityTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SensitivityTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SensitivityTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SensitivityTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
