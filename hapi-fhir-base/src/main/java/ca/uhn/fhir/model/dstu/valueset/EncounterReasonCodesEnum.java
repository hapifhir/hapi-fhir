
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum EncounterReasonCodesEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/encounter-reason
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/encounter-reason";

	/**
	 * Name for this Value Set:
	 * Encounter Reason Codes
	 */
	public static final String VALUESET_NAME = "Encounter Reason Codes";

	private static Map<String, EncounterReasonCodesEnum> CODE_TO_ENUM = new HashMap<String, EncounterReasonCodesEnum>();
	private static Map<String, Map<String, EncounterReasonCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, EncounterReasonCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (EncounterReasonCodesEnum next : EncounterReasonCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, EncounterReasonCodesEnum>());
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
	public EncounterReasonCodesEnum forCode(String theCode) {
		EncounterReasonCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EncounterReasonCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<EncounterReasonCodesEnum>() {
		@Override
		public String toCodeString(EncounterReasonCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(EncounterReasonCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public EncounterReasonCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public EncounterReasonCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, EncounterReasonCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	EncounterReasonCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
