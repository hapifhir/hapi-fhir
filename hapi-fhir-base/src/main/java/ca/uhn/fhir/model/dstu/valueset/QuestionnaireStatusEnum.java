
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

public enum QuestionnaireStatusEnum {

	/**
	 * Display: <b>draft</b><br/>
	 * Code Value: <b>draft</b>
	 *
	 * This Questionnaire is used as a template but the template is not ready for use or publication.
	 */
	DRAFT("draft", "http://hl7.org/fhir/questionnaire-status"),
	
	/**
	 * Display: <b>published</b><br/>
	 * Code Value: <b>published</b>
	 *
	 * This Questionnaire is used as a template, is published and ready for use.
	 */
	PUBLISHED("published", "http://hl7.org/fhir/questionnaire-status"),
	
	/**
	 * Display: <b>retired</b><br/>
	 * Code Value: <b>retired</b>
	 *
	 * This Questionnaire is used as a template but should no longer be used for new Questionnaires.
	 */
	RETIRED("retired", "http://hl7.org/fhir/questionnaire-status"),
	
	/**
	 * Display: <b>in progress</b><br/>
	 * Code Value: <b>in progress</b>
	 *
	 * This Questionnaire has been filled out with answers, but changes or additions are still expected to be made to it.
	 */
	IN_PROGRESS("in progress", "http://hl7.org/fhir/questionnaire-status"),
	
	/**
	 * Display: <b>complete</b><br/>
	 * Code Value: <b>completed</b>
	 *
	 * This Questionnaire has been filled out with answers, and the current content is regarded as definitive.
	 */
	COMPLETE("completed", "http://hl7.org/fhir/questionnaire-status"),
	
	/**
	 * Display: <b>amended</b><br/>
	 * Code Value: <b>amended</b>
	 *
	 * This Questionnaire has been filled out with answers, then marked as complete, yet changes or additions have been made to it afterwards.
	 */
	AMENDED("amended", "http://hl7.org/fhir/questionnaire-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/questionnaire-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/questionnaire-status";

	/**
	 * Name for this Value Set:
	 * QuestionnaireStatus
	 */
	public static final String VALUESET_NAME = "QuestionnaireStatus";

	private static Map<String, QuestionnaireStatusEnum> CODE_TO_ENUM = new HashMap<String, QuestionnaireStatusEnum>();
	private static Map<String, Map<String, QuestionnaireStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, QuestionnaireStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (QuestionnaireStatusEnum next : QuestionnaireStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, QuestionnaireStatusEnum>());
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
	public QuestionnaireStatusEnum forCode(String theCode) {
		QuestionnaireStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<QuestionnaireStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<QuestionnaireStatusEnum>() {
		@Override
		public String toCodeString(QuestionnaireStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(QuestionnaireStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public QuestionnaireStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public QuestionnaireStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, QuestionnaireStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	QuestionnaireStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
