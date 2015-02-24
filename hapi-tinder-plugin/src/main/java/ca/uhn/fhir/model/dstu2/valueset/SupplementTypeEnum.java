
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SupplementTypeEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/supplement-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/supplement-type";

	/**
	 * Name for this Value Set:
	 * SupplementType
	 */
	public static final String VALUESET_NAME = "SupplementType";

	private static Map<String, SupplementTypeEnum> CODE_TO_ENUM = new HashMap<String, SupplementTypeEnum>();
	private static Map<String, Map<String, SupplementTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SupplementTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SupplementTypeEnum next : SupplementTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SupplementTypeEnum>());
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
	public SupplementTypeEnum forCode(String theCode) {
		SupplementTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SupplementTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SupplementTypeEnum>() {
		@Override
		public String toCodeString(SupplementTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SupplementTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SupplementTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SupplementTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SupplementTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SupplementTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
