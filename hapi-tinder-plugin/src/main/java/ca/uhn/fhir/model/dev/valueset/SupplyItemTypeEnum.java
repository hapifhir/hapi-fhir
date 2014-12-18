
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SupplyItemTypeEnum {

	/**
	 * Display: <b>Central Supply</b><br/>
	 * Code Value: <b>medication</b>
	 *
	 * Supply is a kind of medication.
	 */
	CENTRAL_SUPPLY("medication", "http://hl7.org/fhir/supply-item-type"),
	
	/**
	 * Display: <b>Device</b><br/>
	 * Code Value: <b>device</b>
	 *
	 * What is supplied (or requested) is a device
	 */
	DEVICE("device", "http://hl7.org/fhir/supply-item-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/supply-item
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/supply-item";

	/**
	 * Name for this Value Set:
	 * Supply Item Type
	 */
	public static final String VALUESET_NAME = "Supply Item Type";

	private static Map<String, SupplyItemTypeEnum> CODE_TO_ENUM = new HashMap<String, SupplyItemTypeEnum>();
	private static Map<String, Map<String, SupplyItemTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SupplyItemTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SupplyItemTypeEnum next : SupplyItemTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SupplyItemTypeEnum>());
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
	public SupplyItemTypeEnum forCode(String theCode) {
		SupplyItemTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SupplyItemTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SupplyItemTypeEnum>() {
		@Override
		public String toCodeString(SupplyItemTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SupplyItemTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SupplyItemTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SupplyItemTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SupplyItemTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SupplyItemTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
