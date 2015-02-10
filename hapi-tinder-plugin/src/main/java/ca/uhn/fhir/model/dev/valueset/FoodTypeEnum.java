
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum FoodTypeEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/food-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/food-type";

	/**
	 * Name for this Value Set:
	 * FoodType
	 */
	public static final String VALUESET_NAME = "FoodType";

	private static Map<String, FoodTypeEnum> CODE_TO_ENUM = new HashMap<String, FoodTypeEnum>();
	private static Map<String, Map<String, FoodTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, FoodTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (FoodTypeEnum next : FoodTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, FoodTypeEnum>());
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
	public FoodTypeEnum forCode(String theCode) {
		FoodTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<FoodTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<FoodTypeEnum>() {
		@Override
		public String toCodeString(FoodTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(FoodTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public FoodTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public FoodTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, FoodTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	FoodTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
