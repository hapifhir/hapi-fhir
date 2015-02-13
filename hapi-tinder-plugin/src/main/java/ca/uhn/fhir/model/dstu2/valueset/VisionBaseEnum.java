
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum VisionBaseEnum {

	/**
	 * Code Value: <b>up</b>
	 *
	 * top.
	 */
	UP("up", "http://hl7.org/fhir/base-codes"),
	
	/**
	 * Code Value: <b>down</b>
	 *
	 * bottom.
	 */
	DOWN("down", "http://hl7.org/fhir/base-codes"),
	
	/**
	 * Code Value: <b>in</b>
	 *
	 * inner edge.
	 */
	IN("in", "http://hl7.org/fhir/base-codes"),
	
	/**
	 * Code Value: <b>out</b>
	 *
	 * outer edge.
	 */
	OUT("out", "http://hl7.org/fhir/base-codes"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/base-codes
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/base-codes";

	/**
	 * Name for this Value Set:
	 * VisionBase
	 */
	public static final String VALUESET_NAME = "VisionBase";

	private static Map<String, VisionBaseEnum> CODE_TO_ENUM = new HashMap<String, VisionBaseEnum>();
	private static Map<String, Map<String, VisionBaseEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, VisionBaseEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (VisionBaseEnum next : VisionBaseEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, VisionBaseEnum>());
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
	public VisionBaseEnum forCode(String theCode) {
		VisionBaseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<VisionBaseEnum> VALUESET_BINDER = new IValueSetEnumBinder<VisionBaseEnum>() {
		@Override
		public String toCodeString(VisionBaseEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(VisionBaseEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public VisionBaseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public VisionBaseEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, VisionBaseEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	VisionBaseEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
