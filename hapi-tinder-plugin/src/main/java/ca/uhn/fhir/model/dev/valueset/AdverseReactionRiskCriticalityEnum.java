
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdverseReactionRiskCriticalityEnum {

	/**
	 * Display: <b>Low Risk</b><br/>
	 * Code Value: <b>low</b>
	 *
	 * The potential clinical impact of a future reaction is estimated as low risk. Future exposure to the Substance is considered a relative contra-indication.
	 */
	LOW_RISK("low", "http://hl7.org/fhir/reaction-risk-criticality"),
	
	/**
	 * Display: <b>High Risk</b><br/>
	 * Code Value: <b>high</b>
	 *
	 * The potential clinical impact of a future reaction is estimated as high risk. Future exposure to the Substance may be considered an absolute contra-indication.
	 */
	HIGH_RISK("high", "http://hl7.org/fhir/reaction-risk-criticality"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reaction-risk-criticality
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reaction-risk-criticality";

	/**
	 * Name for this Value Set:
	 * AdverseReactionRiskCriticality
	 */
	public static final String VALUESET_NAME = "AdverseReactionRiskCriticality";

	private static Map<String, AdverseReactionRiskCriticalityEnum> CODE_TO_ENUM = new HashMap<String, AdverseReactionRiskCriticalityEnum>();
	private static Map<String, Map<String, AdverseReactionRiskCriticalityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdverseReactionRiskCriticalityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdverseReactionRiskCriticalityEnum next : AdverseReactionRiskCriticalityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdverseReactionRiskCriticalityEnum>());
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
	public AdverseReactionRiskCriticalityEnum forCode(String theCode) {
		AdverseReactionRiskCriticalityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdverseReactionRiskCriticalityEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdverseReactionRiskCriticalityEnum>() {
		@Override
		public String toCodeString(AdverseReactionRiskCriticalityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdverseReactionRiskCriticalityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdverseReactionRiskCriticalityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdverseReactionRiskCriticalityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdverseReactionRiskCriticalityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdverseReactionRiskCriticalityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
