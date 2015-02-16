
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SlotStatusEnum {

	/**
	 * Code Value: <b>BUSY</b>
	 *
	 * Indicates that the time interval is busy because one  or more events have been scheduled for that interval.
	 */
	BUSY("BUSY", "http://hl7.org/fhir/slotstatus"),
	
	/**
	 * Code Value: <b>FREE</b>
	 *
	 * Indicates that the time interval is free for scheduling.
	 */
	FREE("FREE", "http://hl7.org/fhir/slotstatus"),
	
	/**
	 * Code Value: <b>BUSY-UNAVAILABLE</b>
	 *
	 * Indicates that the time interval is busy and that the interval can not be scheduled.
	 */
	BUSY_UNAVAILABLE("BUSY-UNAVAILABLE", "http://hl7.org/fhir/slotstatus"),
	
	/**
	 * Code Value: <b>BUSY-TENTATIVE</b>
	 *
	 * Indicates that the time interval is busy because one or more events have been tentatively scheduled for that interval.
	 */
	BUSY_TENTATIVE("BUSY-TENTATIVE", "http://hl7.org/fhir/slotstatus"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/slotstatus
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/slotstatus";

	/**
	 * Name for this Value Set:
	 * SlotStatus
	 */
	public static final String VALUESET_NAME = "SlotStatus";

	private static Map<String, SlotStatusEnum> CODE_TO_ENUM = new HashMap<String, SlotStatusEnum>();
	private static Map<String, Map<String, SlotStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SlotStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SlotStatusEnum next : SlotStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SlotStatusEnum>());
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
	public SlotStatusEnum forCode(String theCode) {
		SlotStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SlotStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<SlotStatusEnum>() {
		@Override
		public String toCodeString(SlotStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SlotStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SlotStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SlotStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SlotStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SlotStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
