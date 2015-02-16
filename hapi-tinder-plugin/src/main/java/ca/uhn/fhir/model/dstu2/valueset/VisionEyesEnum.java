
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum VisionEyesEnum {

	/**
	 * Code Value: <b>right</b>
	 *
	 * right eye.
	 */
	RIGHT("right", "http://hl7.org/fhir/eye-codes"),
	
	/**
	 * Code Value: <b>left</b>
	 *
	 * left eye.
	 */
	LEFT("left", "http://hl7.org/fhir/eye-codes"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/eye-codes
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/eye-codes";

	/**
	 * Name for this Value Set:
	 * VisionEyes
	 */
	public static final String VALUESET_NAME = "VisionEyes";

	private static Map<String, VisionEyesEnum> CODE_TO_ENUM = new HashMap<String, VisionEyesEnum>();
	private static Map<String, Map<String, VisionEyesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, VisionEyesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (VisionEyesEnum next : VisionEyesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, VisionEyesEnum>());
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
	public VisionEyesEnum forCode(String theCode) {
		VisionEyesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<VisionEyesEnum> VALUESET_BINDER = new IValueSetEnumBinder<VisionEyesEnum>() {
		@Override
		public String toCodeString(VisionEyesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(VisionEyesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public VisionEyesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public VisionEyesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, VisionEyesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	VisionEyesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
