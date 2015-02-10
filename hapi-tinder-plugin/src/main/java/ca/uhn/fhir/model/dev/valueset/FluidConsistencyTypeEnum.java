
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum FluidConsistencyTypeEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/consistency-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/consistency-type";

	/**
	 * Name for this Value Set:
	 * FluidConsistencyType
	 */
	public static final String VALUESET_NAME = "FluidConsistencyType";

	private static Map<String, FluidConsistencyTypeEnum> CODE_TO_ENUM = new HashMap<String, FluidConsistencyTypeEnum>();
	private static Map<String, Map<String, FluidConsistencyTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, FluidConsistencyTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (FluidConsistencyTypeEnum next : FluidConsistencyTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, FluidConsistencyTypeEnum>());
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
	public FluidConsistencyTypeEnum forCode(String theCode) {
		FluidConsistencyTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<FluidConsistencyTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<FluidConsistencyTypeEnum>() {
		@Override
		public String toCodeString(FluidConsistencyTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(FluidConsistencyTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public FluidConsistencyTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public FluidConsistencyTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, FluidConsistencyTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	FluidConsistencyTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
