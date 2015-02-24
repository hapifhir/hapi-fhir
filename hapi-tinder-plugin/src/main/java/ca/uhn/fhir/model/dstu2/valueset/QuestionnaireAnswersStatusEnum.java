
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum QuestionnaireAnswersStatusEnum {

	/**
	 * Display: <b>in progress</b><br>
	 * Code Value: <b>in progress</b>
	 *
	 * This QuestionnaireAnswers has been partially filled out with answers, but changes or additions are still expected to be made to it.
	 */
	IN_PROGRESS("in progress", "http://hl7.org/fhir/questionnaire-answers-status"),
	
	/**
	 * Display: <b>completed</b><br>
	 * Code Value: <b>completed</b>
	 *
	 * This QuestionnaireAnswers has been filled out with answers, and the current content is regarded as definitive.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/questionnaire-answers-status"),
	
	/**
	 * Display: <b>amended</b><br>
	 * Code Value: <b>amended</b>
	 *
	 * This QuestionnaireAnswers has been filled out with answers, then marked as complete, yet changes or additions have been made to it afterwards.
	 */
	AMENDED("amended", "http://hl7.org/fhir/questionnaire-answers-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/questionnaire-answers-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/questionnaire-answers-status";

	/**
	 * Name for this Value Set:
	 * QuestionnaireAnswersStatus
	 */
	public static final String VALUESET_NAME = "QuestionnaireAnswersStatus";

	private static Map<String, QuestionnaireAnswersStatusEnum> CODE_TO_ENUM = new HashMap<String, QuestionnaireAnswersStatusEnum>();
	private static Map<String, Map<String, QuestionnaireAnswersStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, QuestionnaireAnswersStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (QuestionnaireAnswersStatusEnum next : QuestionnaireAnswersStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, QuestionnaireAnswersStatusEnum>());
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
	public QuestionnaireAnswersStatusEnum forCode(String theCode) {
		QuestionnaireAnswersStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<QuestionnaireAnswersStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<QuestionnaireAnswersStatusEnum>() {
		@Override
		public String toCodeString(QuestionnaireAnswersStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(QuestionnaireAnswersStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public QuestionnaireAnswersStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public QuestionnaireAnswersStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, QuestionnaireAnswersStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	QuestionnaireAnswersStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
