
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

public enum QuestionnaireNameEnum {

	/**
	 * Display: <b>Cancer Treatment Quality Questionnaire 2012</b><br/>
	 * Code Value: <b>CTQQ-2012</b>
	 */
	CANCER_TREATMENT_QUALITY_QUESTIONNAIRE_2012("CTQQ-2012", "http://hl7.org/fhir/questionnaire-name"),
	
	/**
	 * Display: <b>Pre-admission standard Form A</b><br/>
	 * Code Value: <b>PA.form.A</b>
	 */
	PRE_ADMISSION_STANDARD_FORM_A("PA.form.A", "http://hl7.org/fhir/questionnaire-name"),
	
	/**
	 * Display: <b>Brazelton Neonatal Assessment Scale</b><br/>
	 * Code Value: <b>BNAS</b>
	 */
	BRAZELTON_NEONATAL_ASSESSMENT_SCALE("BNAS", "http://hl7.org/fhir/questionnaire-name"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/questionnaire-name
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/questionnaire-name";

	/**
	 * Name for this Value Set:
	 * QuestionnaireName
	 */
	public static final String VALUESET_NAME = "QuestionnaireName";

	private static Map<String, QuestionnaireNameEnum> CODE_TO_ENUM = new HashMap<String, QuestionnaireNameEnum>();
	private static Map<String, Map<String, QuestionnaireNameEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, QuestionnaireNameEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (QuestionnaireNameEnum next : QuestionnaireNameEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, QuestionnaireNameEnum>());
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
	public QuestionnaireNameEnum forCode(String theCode) {
		QuestionnaireNameEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<QuestionnaireNameEnum> VALUESET_BINDER = new IValueSetEnumBinder<QuestionnaireNameEnum>() {
		@Override
		public String toCodeString(QuestionnaireNameEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(QuestionnaireNameEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public QuestionnaireNameEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public QuestionnaireNameEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, QuestionnaireNameEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	QuestionnaireNameEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
