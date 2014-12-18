
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NutrientModifierEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/nutrient-code
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/nutrient-code";

	/**
	 * Name for this Value Set:
	 * NutrientModifier
	 */
	public static final String VALUESET_NAME = "NutrientModifier";

	private static Map<String, NutrientModifierEnum> CODE_TO_ENUM = new HashMap<String, NutrientModifierEnum>();
	private static Map<String, Map<String, NutrientModifierEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NutrientModifierEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NutrientModifierEnum next : NutrientModifierEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NutrientModifierEnum>());
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
	public NutrientModifierEnum forCode(String theCode) {
		NutrientModifierEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NutrientModifierEnum> VALUESET_BINDER = new IValueSetEnumBinder<NutrientModifierEnum>() {
		@Override
		public String toCodeString(NutrientModifierEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NutrientModifierEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NutrientModifierEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NutrientModifierEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NutrientModifierEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NutrientModifierEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
