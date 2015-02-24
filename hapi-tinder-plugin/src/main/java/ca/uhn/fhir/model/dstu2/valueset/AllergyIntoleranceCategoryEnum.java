
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AllergyIntoleranceCategoryEnum {

	/**
	 * Display: <b>Food</b><br>
	 * Code Value: <b>food</b>
	 *
	 * Any substance consumed to provide nutritional support for the body.
	 */
	FOOD("food", "http://hl7.org/fhir/allergy-intolerance-category"),
	
	/**
	 * Display: <b>Medication</b><br>
	 * Code Value: <b>medication</b>
	 *
	 * Substances administered to achieve a physiological effect.
	 */
	MEDICATION("medication", "http://hl7.org/fhir/allergy-intolerance-category"),
	
	/**
	 * Display: <b>Environment</b><br>
	 * Code Value: <b>environment</b>
	 *
	 * Substances that are encountered in the environment.
	 */
	ENVIRONMENT("environment", "http://hl7.org/fhir/allergy-intolerance-category"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/allergy-intolerance-category
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/allergy-intolerance-category";

	/**
	 * Name for this Value Set:
	 * AllergyIntoleranceCategory
	 */
	public static final String VALUESET_NAME = "AllergyIntoleranceCategory";

	private static Map<String, AllergyIntoleranceCategoryEnum> CODE_TO_ENUM = new HashMap<String, AllergyIntoleranceCategoryEnum>();
	private static Map<String, Map<String, AllergyIntoleranceCategoryEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AllergyIntoleranceCategoryEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AllergyIntoleranceCategoryEnum next : AllergyIntoleranceCategoryEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AllergyIntoleranceCategoryEnum>());
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
	public AllergyIntoleranceCategoryEnum forCode(String theCode) {
		AllergyIntoleranceCategoryEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AllergyIntoleranceCategoryEnum> VALUESET_BINDER = new IValueSetEnumBinder<AllergyIntoleranceCategoryEnum>() {
		@Override
		public String toCodeString(AllergyIntoleranceCategoryEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AllergyIntoleranceCategoryEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AllergyIntoleranceCategoryEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AllergyIntoleranceCategoryEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AllergyIntoleranceCategoryEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AllergyIntoleranceCategoryEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
