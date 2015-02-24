
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ConditionsCodesEnum {

	/**
	 * Code Value: <b>123987</b>
	 */
	_123987("123987", "http://hl7.org/fhir/fm-conditions"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/fm-conditions
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/fm-conditions";

	/**
	 * Name for this Value Set:
	 * Conditions Codes
	 */
	public static final String VALUESET_NAME = "Conditions Codes";

	private static Map<String, ConditionsCodesEnum> CODE_TO_ENUM = new HashMap<String, ConditionsCodesEnum>();
	private static Map<String, Map<String, ConditionsCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ConditionsCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ConditionsCodesEnum next : ConditionsCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ConditionsCodesEnum>());
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
	public ConditionsCodesEnum forCode(String theCode) {
		ConditionsCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ConditionsCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ConditionsCodesEnum>() {
		@Override
		public String toCodeString(ConditionsCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ConditionsCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ConditionsCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ConditionsCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ConditionsCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ConditionsCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
