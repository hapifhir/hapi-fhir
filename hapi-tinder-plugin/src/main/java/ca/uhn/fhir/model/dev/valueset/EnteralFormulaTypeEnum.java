
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum EnteralFormulaTypeEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/entformula-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/entformula-type";

	/**
	 * Name for this Value Set:
	 * EnteralFormulaType
	 */
	public static final String VALUESET_NAME = "EnteralFormulaType";

	private static Map<String, EnteralFormulaTypeEnum> CODE_TO_ENUM = new HashMap<String, EnteralFormulaTypeEnum>();
	private static Map<String, Map<String, EnteralFormulaTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, EnteralFormulaTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (EnteralFormulaTypeEnum next : EnteralFormulaTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, EnteralFormulaTypeEnum>());
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
	public EnteralFormulaTypeEnum forCode(String theCode) {
		EnteralFormulaTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EnteralFormulaTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<EnteralFormulaTypeEnum>() {
		@Override
		public String toCodeString(EnteralFormulaTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(EnteralFormulaTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public EnteralFormulaTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public EnteralFormulaTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, EnteralFormulaTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	EnteralFormulaTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
