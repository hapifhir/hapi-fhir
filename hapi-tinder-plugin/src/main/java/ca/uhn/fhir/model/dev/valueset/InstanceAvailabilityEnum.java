
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum InstanceAvailabilityEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/instance-availability
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/instance-availability";

	/**
	 * Name for this Value Set:
	 * InstanceAvailability
	 */
	public static final String VALUESET_NAME = "InstanceAvailability";

	private static Map<String, InstanceAvailabilityEnum> CODE_TO_ENUM = new HashMap<String, InstanceAvailabilityEnum>();
	private static Map<String, Map<String, InstanceAvailabilityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, InstanceAvailabilityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (InstanceAvailabilityEnum next : InstanceAvailabilityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, InstanceAvailabilityEnum>());
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
	public InstanceAvailabilityEnum forCode(String theCode) {
		InstanceAvailabilityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<InstanceAvailabilityEnum> VALUESET_BINDER = new IValueSetEnumBinder<InstanceAvailabilityEnum>() {
		@Override
		public String toCodeString(InstanceAvailabilityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(InstanceAvailabilityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public InstanceAvailabilityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public InstanceAvailabilityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, InstanceAvailabilityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	InstanceAvailabilityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
