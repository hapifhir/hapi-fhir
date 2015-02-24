
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ExcludeFoodModifierEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/exclude-food-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/exclude-food-type";

	/**
	 * Name for this Value Set:
	 * ExcludeFoodModifier
	 */
	public static final String VALUESET_NAME = "ExcludeFoodModifier";

	private static Map<String, ExcludeFoodModifierEnum> CODE_TO_ENUM = new HashMap<String, ExcludeFoodModifierEnum>();
	private static Map<String, Map<String, ExcludeFoodModifierEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ExcludeFoodModifierEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ExcludeFoodModifierEnum next : ExcludeFoodModifierEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ExcludeFoodModifierEnum>());
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
	public ExcludeFoodModifierEnum forCode(String theCode) {
		ExcludeFoodModifierEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ExcludeFoodModifierEnum> VALUESET_BINDER = new IValueSetEnumBinder<ExcludeFoodModifierEnum>() {
		@Override
		public String toCodeString(ExcludeFoodModifierEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ExcludeFoodModifierEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ExcludeFoodModifierEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ExcludeFoodModifierEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ExcludeFoodModifierEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ExcludeFoodModifierEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
