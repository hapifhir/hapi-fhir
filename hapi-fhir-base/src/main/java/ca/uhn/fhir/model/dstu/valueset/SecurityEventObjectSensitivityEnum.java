
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum SecurityEventObjectSensitivityEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/security-event-sensitivity
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/security-event-sensitivity";

	/**
	 * Name for this Value Set:
	 * Security Event Object Sensitivity
	 */
	public static final String VALUESET_NAME = "Security Event Object Sensitivity";

	private static Map<String, SecurityEventObjectSensitivityEnum> CODE_TO_ENUM = new HashMap<String, SecurityEventObjectSensitivityEnum>();
	private static Map<String, Map<String, SecurityEventObjectSensitivityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SecurityEventObjectSensitivityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SecurityEventObjectSensitivityEnum next : SecurityEventObjectSensitivityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SecurityEventObjectSensitivityEnum>());
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
	public SecurityEventObjectSensitivityEnum forCode(String theCode) {
		SecurityEventObjectSensitivityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SecurityEventObjectSensitivityEnum> VALUESET_BINDER = new IValueSetEnumBinder<SecurityEventObjectSensitivityEnum>() {
		@Override
		public String toCodeString(SecurityEventObjectSensitivityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SecurityEventObjectSensitivityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SecurityEventObjectSensitivityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SecurityEventObjectSensitivityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SecurityEventObjectSensitivityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SecurityEventObjectSensitivityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
