
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum EnteralFormulaAdditiveTypeEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/entformula-additive
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/entformula-additive";

	/**
	 * Name for this Value Set:
	 * EnteralFormulaAdditiveType
	 */
	public static final String VALUESET_NAME = "EnteralFormulaAdditiveType";

	private static Map<String, EnteralFormulaAdditiveTypeEnum> CODE_TO_ENUM = new HashMap<String, EnteralFormulaAdditiveTypeEnum>();
	private static Map<String, Map<String, EnteralFormulaAdditiveTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, EnteralFormulaAdditiveTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (EnteralFormulaAdditiveTypeEnum next : EnteralFormulaAdditiveTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, EnteralFormulaAdditiveTypeEnum>());
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
	public EnteralFormulaAdditiveTypeEnum forCode(String theCode) {
		EnteralFormulaAdditiveTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EnteralFormulaAdditiveTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<EnteralFormulaAdditiveTypeEnum>() {
		@Override
		public String toCodeString(EnteralFormulaAdditiveTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(EnteralFormulaAdditiveTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public EnteralFormulaAdditiveTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public EnteralFormulaAdditiveTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, EnteralFormulaAdditiveTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	EnteralFormulaAdditiveTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
