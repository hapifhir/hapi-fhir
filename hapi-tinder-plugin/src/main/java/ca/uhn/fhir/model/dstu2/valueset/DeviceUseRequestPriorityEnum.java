
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum DeviceUseRequestPriorityEnum {

	/**
	 * Code Value: <b>routine</b>
	 *
	 * The request has a normal priority.
	 */
	ROUTINE("routine", "http://hl7.org/fhir/device-use-request-priority"),
	
	/**
	 * Code Value: <b>urgent</b>
	 *
	 * The request should be done urgently.
	 */
	URGENT("urgent", "http://hl7.org/fhir/device-use-request-priority"),
	
	/**
	 * Code Value: <b>stat</b>
	 *
	 * The request is time-critical.
	 */
	STAT("stat", "http://hl7.org/fhir/device-use-request-priority"),
	
	/**
	 * Code Value: <b>asap</b>
	 *
	 * The request should be acted on as soon as possible.
	 */
	ASAP("asap", "http://hl7.org/fhir/device-use-request-priority"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/device-use-request-priority
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/device-use-request-priority";

	/**
	 * Name for this Value Set:
	 * DeviceUseRequestPriority
	 */
	public static final String VALUESET_NAME = "DeviceUseRequestPriority";

	private static Map<String, DeviceUseRequestPriorityEnum> CODE_TO_ENUM = new HashMap<String, DeviceUseRequestPriorityEnum>();
	private static Map<String, Map<String, DeviceUseRequestPriorityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DeviceUseRequestPriorityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DeviceUseRequestPriorityEnum next : DeviceUseRequestPriorityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DeviceUseRequestPriorityEnum>());
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
	public DeviceUseRequestPriorityEnum forCode(String theCode) {
		DeviceUseRequestPriorityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DeviceUseRequestPriorityEnum> VALUESET_BINDER = new IValueSetEnumBinder<DeviceUseRequestPriorityEnum>() {
		@Override
		public String toCodeString(DeviceUseRequestPriorityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DeviceUseRequestPriorityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DeviceUseRequestPriorityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DeviceUseRequestPriorityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DeviceUseRequestPriorityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DeviceUseRequestPriorityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
