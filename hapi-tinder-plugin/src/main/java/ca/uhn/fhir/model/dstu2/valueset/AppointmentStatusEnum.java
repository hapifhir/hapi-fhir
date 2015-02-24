
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AppointmentStatusEnum {

	/**
	 * Display: <b>Pending</b><br>
	 * Code Value: <b>pending</b>
	 *
	 * Some or all of the participant(s) have not finalized their acceptance of the appointment request.
	 */
	PENDING("pending", "http://hl7.org/fhir/appointmentstatus"),
	
	/**
	 * Display: <b>Booked</b><br>
	 * Code Value: <b>booked</b>
	 *
	 * All participant(s) have been considered and the appointment is condirmed to go ahead at the date/times specified.
	 */
	BOOKED("booked", "http://hl7.org/fhir/appointmentstatus"),
	
	/**
	 * Display: <b>Arrived</b><br>
	 * Code Value: <b>arrived</b>
	 *
	 * Some of the patients have arrived.
	 */
	ARRIVED("arrived", "http://hl7.org/fhir/appointmentstatus"),
	
	/**
	 * Display: <b>Fulfilled</b><br>
	 * Code Value: <b>fulfilled</b>
	 *
	 * This appointment has completed and may have resulted in an encounter.
	 */
	FULFILLED("fulfilled", "http://hl7.org/fhir/appointmentstatus"),
	
	/**
	 * Display: <b>Cancelled</b><br>
	 * Code Value: <b>cancelled</b>
	 *
	 * The appointment has been cancelled.
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/appointmentstatus"),
	
	/**
	 * Display: <b>No Show</b><br>
	 * Code Value: <b>noshow</b>
	 *
	 * Some or all of the participant(s) have not/did not appear for the appointment (usually the patient).
	 */
	NO_SHOW("noshow", "http://hl7.org/fhir/appointmentstatus"),
	
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
