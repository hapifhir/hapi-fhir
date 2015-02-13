
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum UDICodesEnum {

	/**
	 * Code Value: <b>{01}123456789</b>
	 */
	_01_123456789("{01}123456789", "http://hl7.org/fhir/ex-udi"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/udi
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/udi";

	/**
	 * Name for this Value Set:
	 * UDI Codes
	 */
	public static final String VALUESET_NAME = "UDI Codes";

	private static Map<String, UDICodesEnum> CODE_TO_ENUM = new HashMap<String, UDICodesEnum>();
	private static Map<String, Map<String, UDICodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, UDICodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (UDICodesEnum next : UDICodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, UDICodesEnum>());
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
	public UDICodesEnum forCode(String theCode) {
		UDICodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<UDICodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<UDICodesEnum>() {
		@Override
		public String toCodeString(UDICodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(UDICodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public UDICodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public UDICodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, UDICodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	UDICodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
