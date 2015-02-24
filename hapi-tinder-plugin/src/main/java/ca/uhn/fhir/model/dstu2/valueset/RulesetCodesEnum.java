
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum RulesetCodesEnum {

	/**
	 * Code Value: <b>x12-4010</b>
	 */
	X12_4010("x12-4010", "http://hl7.org/fhir/ruleset"),
	
	/**
	 * Code Value: <b>x12-5010</b>
	 */
	X12_5010("x12-5010", "http://hl7.org/fhir/ruleset"),
	
	/**
	 * Code Value: <b>x12-7010</b>
	 */
	X12_7010("x12-7010", "http://hl7.org/fhir/ruleset"),
	
	/**
	 * Code Value: <b>cdanet-v2</b>
	 */
	CDANET_V2("cdanet-v2", "http://hl7.org/fhir/ruleset"),
	
	/**
	 * Code Value: <b>cdanet-v4</b>
	 */
	CDANET_V4("cdanet-v4", "http://hl7.org/fhir/ruleset"),
	
	/**
	 * Code Value: <b>cpha-3</b>
	 */
	CPHA_3("cpha-3", "http://hl7.org/fhir/ruleset"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/ruleset
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/ruleset";

	/**
	 * Name for this Value Set:
	 * Ruleset Codes
	 */
	public static final String VALUESET_NAME = "Ruleset Codes";

	private static Map<String, RulesetCodesEnum> CODE_TO_ENUM = new HashMap<String, RulesetCodesEnum>();
	private static Map<String, Map<String, RulesetCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, RulesetCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (RulesetCodesEnum next : RulesetCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, RulesetCodesEnum>());
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
	public RulesetCodesEnum forCode(String theCode) {
		RulesetCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<RulesetCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<RulesetCodesEnum>() {
		@Override
		public String toCodeString(RulesetCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(RulesetCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public RulesetCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public RulesetCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, RulesetCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	RulesetCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
