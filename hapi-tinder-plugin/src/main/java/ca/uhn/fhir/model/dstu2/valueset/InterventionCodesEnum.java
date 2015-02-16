
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum InterventionCodesEnum {

	/**
	 * Code Value: <b>unknown</b>
	 */
	UNKNOWN("unknown", "http://hl7.org/fhir/intervention"),
	
	/**
	 * Code Value: <b>other</b>
	 */
	OTHER("other", "http://hl7.org/fhir/intervention"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/intervention
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/intervention";

	/**
	 * Name for this Value Set:
	 * Intervention Codes
	 */
	public static final String VALUESET_NAME = "Intervention Codes";

	private static Map<String, InterventionCodesEnum> CODE_TO_ENUM = new HashMap<String, InterventionCodesEnum>();
	private static Map<String, Map<String, InterventionCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, InterventionCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (InterventionCodesEnum next : InterventionCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, InterventionCodesEnum>());
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
	public InterventionCodesEnum forCode(String theCode) {
		InterventionCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<InterventionCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<InterventionCodesEnum>() {
		@Override
		public String toCodeString(InterventionCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(InterventionCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public InterventionCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public InterventionCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, InterventionCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	InterventionCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
