
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NutritionOrderStatusEnum {

	/**
	 * Display: <b>Requested</b><br>
	 * Code Value: <b>requested</b>
	 *
	 * TODO.
	 */
	REQUESTED("requested", "http://hl7.org/fhir/nutrition-order-status"),
	
	/**
	 * Display: <b>Active</b><br>
	 * Code Value: <b>active</b>
	 *
	 * TODO.
	 */
	ACTIVE("active", "http://hl7.org/fhir/nutrition-order-status"),
	
	/**
	 * Display: <b>Inactive</b><br>
	 * Code Value: <b>inactive</b>
	 *
	 * TODO.
	 */
	INACTIVE("inactive", "http://hl7.org/fhir/nutrition-order-status"),
	
	/**
	 * Display: <b>Held</b><br>
	 * Code Value: <b>held</b>
	 *
	 * TODO.
	 */
	HELD("held", "http://hl7.org/fhir/nutrition-order-status"),
	
	/**
	 * Display: <b>Cancelled</b><br>
	 * Code Value: <b>cancelled</b>
	 *
	 * TODO.
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/nutrition-order-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/nutrition-order-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/nutrition-order-status";

	/**
	 * Name for this Value Set:
	 * NutritionOrderStatus
	 */
	public static final String VALUESET_NAME = "NutritionOrderStatus";

	private static Map<String, NutritionOrderStatusEnum> CODE_TO_ENUM = new HashMap<String, NutritionOrderStatusEnum>();
	private static Map<String, Map<String, NutritionOrderStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NutritionOrderStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NutritionOrderStatusEnum next : NutritionOrderStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NutritionOrderStatusEnum>());
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
	public NutritionOrderStatusEnum forCode(String theCode) {
		NutritionOrderStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NutritionOrderStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<NutritionOrderStatusEnum>() {
		@Override
		public String toCodeString(NutritionOrderStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NutritionOrderStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NutritionOrderStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NutritionOrderStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NutritionOrderStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NutritionOrderStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
