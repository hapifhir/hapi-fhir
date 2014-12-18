
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdverseReactionRiskCategoryEnum {

	/**
	 * Display: <b>Food</b><br/>
	 * Code Value: <b>food</b>
	 *
	 * Any substance consumed to provide nutritional support for the body.
	 */
	FOOD("food", "http://hl7.org/fhir/reaction-risk-category"),
	
	/**
	 * Display: <b>Medication</b><br/>
	 * Code Value: <b>medication</b>
	 *
	 * Substances administered to achieve a physiological effect.
	 */
	MEDICATION("medication", "http://hl7.org/fhir/reaction-risk-category"),
	
	/**
	 * Display: <b>Environment</b><br/>
	 * Code Value: <b>environment</b>
	 *
	 * Substances that are encountered in the environment.
	 */
	ENVIRONMENT("environment", "http://hl7.org/fhir/reaction-risk-category"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reaction-risk-category
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reaction-risk-category";

	/**
	 * Name for this Value Set:
	 * AdverseReactionRiskCategory
	 */
	public static final String VALUESET_NAME = "AdverseReactionRiskCategory";

	private static Map<String, AdverseReactionRiskCategoryEnum> CODE_TO_ENUM = new HashMap<String, AdverseReactionRiskCategoryEnum>();
	private static Map<String, Map<String, AdverseReactionRiskCategoryEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdverseReactionRiskCategoryEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdverseReactionRiskCategoryEnum next : AdverseReactionRiskCategoryEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdverseReactionRiskCategoryEnum>());
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
	public AdverseReactionRiskCategoryEnum forCode(String theCode) {
		AdverseReactionRiskCategoryEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdverseReactionRiskCategoryEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdverseReactionRiskCategoryEnum>() {
		@Override
		public String toCodeString(AdverseReactionRiskCategoryEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdverseReactionRiskCategoryEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdverseReactionRiskCategoryEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdverseReactionRiskCategoryEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdverseReactionRiskCategoryEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdverseReactionRiskCategoryEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
