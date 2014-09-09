
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

public enum QuestionnaireGroupNameEnum {

	/**
	 * Display: <b>Hair Color</b><br/>
	 * Code Value: <b>B.001</b>
	 */
	HAIR_COLOR("B.001", "http://hl7.org/fhir/questionnaire-group-name"),
	
	/**
	 * Display: <b>Vision</b><br/>
	 * Code Value: <b>B.002</b>
	 */
	VISION("B.002", "http://hl7.org/fhir/questionnaire-group-name"),
	
	/**
	 * Display: <b>Sleepwalker</b><br/>
	 * Code Value: <b>B.003</b>
	 */
	SLEEPWALKER("B.003", "http://hl7.org/fhir/questionnaire-group-name"),
	
	/**
	 * Display: <b>Tooth extraction</b><br/>
	 * Code Value: <b>B.004</b>
	 */
	TOOTH_EXTRACTION("B.004", "http://hl7.org/fhir/questionnaire-group-name"),
	
	/**
	 * Display: <b>Stutter</b><br/>
	 * Code Value: <b>B.005</b>
	 */
	STUTTER("B.005", "http://hl7.org/fhir/questionnaire-group-name"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/questionnaire-group-name
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/questionnaire-group-name";

	/**
	 * Name for this Value Set:
	 * QuestionnaireGroupName
	 */
	public static final String VALUESET_NAME = "QuestionnaireGroupName";

	private static Map<String, QuestionnaireGroupNameEnum> CODE_TO_ENUM = new HashMap<String, QuestionnaireGroupNameEnum>();
	private static Map<String, Map<String, QuestionnaireGroupNameEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, QuestionnaireGroupNameEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (QuestionnaireGroupNameEnum next : QuestionnaireGroupNameEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, QuestionnaireGroupNameEnum>());
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
	public QuestionnaireGroupNameEnum forCode(String theCode) {
		QuestionnaireGroupNameEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<QuestionnaireGroupNameEnum> VALUESET_BINDER = new IValueSetEnumBinder<QuestionnaireGroupNameEnum>() {
		@Override
		public String toCodeString(QuestionnaireGroupNameEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(QuestionnaireGroupNameEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public QuestionnaireGroupNameEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public QuestionnaireGroupNameEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, QuestionnaireGroupNameEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	QuestionnaireGroupNameEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
