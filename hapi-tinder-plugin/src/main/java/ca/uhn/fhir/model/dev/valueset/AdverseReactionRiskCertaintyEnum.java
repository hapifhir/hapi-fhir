
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdverseReactionRiskCertaintyEnum {

	/**
	 * Display: <b>Unlikely</b><br/>
	 * Code Value: <b>unlikely</b>
	 *
	 * There is a low level of clinical certainty that the reaction was caused by the identified Substance.
	 */
	UNLIKELY("unlikely", "http://hl7.org/fhir/reaction-risk-certainty"),
	
	/**
	 * Display: <b>Likely</b><br/>
	 * Code Value: <b>likely</b>
	 *
	 * There is a high level of clinical certainty that the reaction was caused by the identified Substance.
	 */
	LIKELY("likely", "http://hl7.org/fhir/reaction-risk-certainty"),
	
	/**
	 * Display: <b>Confirmed</b><br/>
	 * Code Value: <b>confirmed</b>
	 *
	 * There is a very high level of clinical certainty that the reaction was due to the identified Substance, which may include clinical evidence by testing or rechallenge.
	 */
	CONFIRMED("confirmed", "http://hl7.org/fhir/reaction-risk-certainty"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reaction-risk-certainty
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reaction-risk-certainty";

	/**
	 * Name for this Value Set:
	 * AdverseReactionRiskCertainty
	 */
	public static final String VALUESET_NAME = "AdverseReactionRiskCertainty";

	private static Map<String, AdverseReactionRiskCertaintyEnum> CODE_TO_ENUM = new HashMap<String, AdverseReactionRiskCertaintyEnum>();
	private static Map<String, Map<String, AdverseReactionRiskCertaintyEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdverseReactionRiskCertaintyEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdverseReactionRiskCertaintyEnum next : AdverseReactionRiskCertaintyEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdverseReactionRiskCertaintyEnum>());
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
	public AdverseReactionRiskCertaintyEnum forCode(String theCode) {
		AdverseReactionRiskCertaintyEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdverseReactionRiskCertaintyEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdverseReactionRiskCertaintyEnum>() {
		@Override
		public String toCodeString(AdverseReactionRiskCertaintyEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdverseReactionRiskCertaintyEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdverseReactionRiskCertaintyEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdverseReactionRiskCertaintyEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdverseReactionRiskCertaintyEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdverseReactionRiskCertaintyEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
