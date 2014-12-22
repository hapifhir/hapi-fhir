
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdverseReactionRiskStatusEnum {

	/**
	 * Display: <b>Unconfirmed</b><br/>
	 * Code Value: <b>unconfirmed</b>
	 *
	 * A low level of certainty about the propensity for a reaction to the identified Substance.
	 */
	UNCONFIRMED("unconfirmed", "http://hl7.org/fhir/reaction-risk-status"),
	
	/**
	 * Display: <b>Confirmed</b><br/>
	 * Code Value: <b>confirmed</b>
	 *
	 * A high level of certainty about the propensity for a reaction to the identified Substance, which may include clinical evidence by testing or rechallenge.
	 */
	CONFIRMED("confirmed", "http://hl7.org/fhir/reaction-risk-status"),
	
	/**
	 * Display: <b>Resolved</b><br/>
	 * Code Value: <b>resolved</b>
	 *
	 * A reaction to the identified Substance has been clinically reassessed by testing or rechallenge and considered to be resolved.
	 */
	RESOLVED("resolved", "http://hl7.org/fhir/reaction-risk-status"),
	
	/**
	 * Display: <b>Refuted</b><br/>
	 * Code Value: <b>refuted</b>
	 *
	 * A propensity for a reaction to the identified Substance has been disproven with a high level of clinical certainty, which may include testing or rechallenge, and is refuted.
	 */
	REFUTED("refuted", "http://hl7.org/fhir/reaction-risk-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reaction-risk-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reaction-risk-status";

	/**
	 * Name for this Value Set:
	 * AdverseReactionRiskStatus
	 */
	public static final String VALUESET_NAME = "AdverseReactionRiskStatus";

	private static Map<String, AdverseReactionRiskStatusEnum> CODE_TO_ENUM = new HashMap<String, AdverseReactionRiskStatusEnum>();
	private static Map<String, Map<String, AdverseReactionRiskStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdverseReactionRiskStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdverseReactionRiskStatusEnum next : AdverseReactionRiskStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdverseReactionRiskStatusEnum>());
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
	public AdverseReactionRiskStatusEnum forCode(String theCode) {
		AdverseReactionRiskStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdverseReactionRiskStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdverseReactionRiskStatusEnum>() {
		@Override
		public String toCodeString(AdverseReactionRiskStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdverseReactionRiskStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdverseReactionRiskStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdverseReactionRiskStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdverseReactionRiskStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdverseReactionRiskStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
