
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdverseReactionRiskSeverityEnum {

	/**
	 * Display: <b>Mild</b><br/>
	 * Code Value: <b>mild</b>
	 *
	 * Causes mild physiological effects.
	 */
	MILD("mild", "http://hl7.org/fhir/reaction-risk-severity"),
	
	/**
	 * Display: <b>Moderate</b><br/>
	 * Code Value: <b>moderate</b>
	 *
	 * Causes moderate physiological effects.
	 */
	MODERATE("moderate", "http://hl7.org/fhir/reaction-risk-severity"),
	
	/**
	 * Display: <b>Severe</b><br/>
	 * Code Value: <b>severe</b>
	 *
	 * Causes severe physiological effects.
	 */
	SEVERE("severe", "http://hl7.org/fhir/reaction-risk-severity"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reaction-risk-severity
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reaction-risk-severity";

	/**
	 * Name for this Value Set:
	 * AdverseReactionRiskSeverity
	 */
	public static final String VALUESET_NAME = "AdverseReactionRiskSeverity";

	private static Map<String, AdverseReactionRiskSeverityEnum> CODE_TO_ENUM = new HashMap<String, AdverseReactionRiskSeverityEnum>();
	private static Map<String, Map<String, AdverseReactionRiskSeverityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdverseReactionRiskSeverityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdverseReactionRiskSeverityEnum next : AdverseReactionRiskSeverityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdverseReactionRiskSeverityEnum>());
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
	public AdverseReactionRiskSeverityEnum forCode(String theCode) {
		AdverseReactionRiskSeverityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdverseReactionRiskSeverityEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdverseReactionRiskSeverityEnum>() {
		@Override
		public String toCodeString(AdverseReactionRiskSeverityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdverseReactionRiskSeverityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdverseReactionRiskSeverityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdverseReactionRiskSeverityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdverseReactionRiskSeverityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdverseReactionRiskSeverityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
