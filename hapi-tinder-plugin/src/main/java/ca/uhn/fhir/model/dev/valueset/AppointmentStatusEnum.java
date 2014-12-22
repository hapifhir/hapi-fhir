
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AppointmentStatusEnum {

	/**
	 * Code Value: <b>busy</b>
	 *
	 * The participant(s) will be unavailable during this appointment.
	 */
	BUSY("busy", "http://hl7.org/fhir/appointmentstatus"),
	
	/**
	 * Code Value: <b>free</b>
	 *
	 * The participant(s) will still be available during this appointment.
	 */
	FREE("free", "http://hl7.org/fhir/appointmentstatus"),
	
	/**
	 * Code Value: <b>tentative</b>
	 *
	 * This appointment has not been confirmed, and may become available.
	 */
	TENTATIVE("tentative", "http://hl7.org/fhir/appointmentstatus"),
	
	/**
	 * Code Value: <b>outofoffice</b>
	 *
	 * The participant(s) will not be at the usual location.
	 */
	OUTOFOFFICE("outofoffice", "http://hl7.org/fhir/appointmentstatus"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/appointmentstatus
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/appointmentstatus";

	/**
	 * Name for this Value Set:
	 * AppointmentStatus
	 */
	public static final String VALUESET_NAME = "AppointmentStatus";

	private static Map<String, AppointmentStatusEnum> CODE_TO_ENUM = new HashMap<String, AppointmentStatusEnum>();
	private static Map<String, Map<String, AppointmentStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AppointmentStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AppointmentStatusEnum next : AppointmentStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AppointmentStatusEnum>());
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
	public AppointmentStatusEnum forCode(String theCode) {
		AppointmentStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AppointmentStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<AppointmentStatusEnum>() {
		@Override
		public String toCodeString(AppointmentStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AppointmentStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AppointmentStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AppointmentStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AppointmentStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AppointmentStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
