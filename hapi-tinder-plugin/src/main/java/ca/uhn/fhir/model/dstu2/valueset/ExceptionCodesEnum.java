
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ExceptionCodesEnum {

	/**
	 * Code Value: <b>student</b>
	 */
	STUDENT("student", "http://hl7.org/fhir/exception"),
	
	/**
	 * Code Value: <b>disabled</b>
	 */
	DISABLED("disabled", "http://hl7.org/fhir/exception"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/exception
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/exception";

	/**
	 * Name for this Value Set:
	 * Exception Codes
	 */
	public static final String VALUESET_NAME = "Exception Codes";

	private static Map<String, ExceptionCodesEnum> CODE_TO_ENUM = new HashMap<String, ExceptionCodesEnum>();
	private static Map<String, Map<String, ExceptionCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ExceptionCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ExceptionCodesEnum next : ExceptionCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ExceptionCodesEnum>());
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
	public ExceptionCodesEnum forCode(String theCode) {
		ExceptionCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ExceptionCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ExceptionCodesEnum>() {
		@Override
		public String toCodeString(ExceptionCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ExceptionCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ExceptionCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ExceptionCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ExceptionCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ExceptionCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
