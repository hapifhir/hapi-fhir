
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum PriorityCodesEnum {

	/**
	 * Code Value: <b>stat</b>
	 */
	STAT("stat", "http://hl7.org/fhir/processpriority"),
	
	/**
	 * Code Value: <b>normal</b>
	 */
	NORMAL("normal", "http://hl7.org/fhir/processpriority"),
	
	/**
	 * Code Value: <b>deferred</b>
	 */
	DEFERRED("deferred", "http://hl7.org/fhir/processpriority"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/process-priority
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/process-priority";

	/**
	 * Name for this Value Set:
	 * Priority Codes
	 */
	public static final String VALUESET_NAME = "Priority Codes";

	private static Map<String, PriorityCodesEnum> CODE_TO_ENUM = new HashMap<String, PriorityCodesEnum>();
	private static Map<String, Map<String, PriorityCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, PriorityCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (PriorityCodesEnum next : PriorityCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, PriorityCodesEnum>());
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
	public PriorityCodesEnum forCode(String theCode) {
		PriorityCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<PriorityCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<PriorityCodesEnum>() {
		@Override
		public String toCodeString(PriorityCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(PriorityCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public PriorityCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public PriorityCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, PriorityCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	PriorityCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
