
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ParticipantStatusEnum {

	/**
	 * Code Value: <b>accepted</b>
	 *
	 * The appointment participant has accepted that they can attend the appointment at the time specified in the AppointmentResponse.
	 */
	ACCEPTED("accepted", "http://hl7.org/fhir/participantstatus"),
	
	/**
	 * Code Value: <b>declined</b>
	 *
	 * The appointment participant has declined the appointment.
	 */
	DECLINED("declined", "http://hl7.org/fhir/participantstatus"),
	
	/**
	 * Code Value: <b>tentative</b>
	 *
	 * The appointment participant has tentatively accepted the appointment.
	 */
	TENTATIVE("tentative", "http://hl7.org/fhir/participantstatus"),
	
	/**
	 * Code Value: <b>in-process</b>
	 *
	 * The participant has in-process the appointment.
	 */
	IN_PROCESS("in-process", "http://hl7.org/fhir/participantstatus"),
	
	/**
	 * Code Value: <b>completed</b>
	 *
	 * The participant has completed the appointment.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/participantstatus"),
	
	/**
	 * Code Value: <b>needs-action</b>
	 *
	 * This is the intitial status of an appointment participant until a participant has replied. It implies that there is no commitment for the appointment.
	 */
	NEEDS_ACTION("needs-action", "http://hl7.org/fhir/participantstatus"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/participantstatus
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/participantstatus";

	/**
	 * Name for this Value Set:
	 * ParticipantStatus
	 */
	public static final String VALUESET_NAME = "ParticipantStatus";

	private static Map<String, ParticipantStatusEnum> CODE_TO_ENUM = new HashMap<String, ParticipantStatusEnum>();
	private static Map<String, Map<String, ParticipantStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ParticipantStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ParticipantStatusEnum next : ParticipantStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ParticipantStatusEnum>());
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
	public ParticipantStatusEnum forCode(String theCode) {
		ParticipantStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ParticipantStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ParticipantStatusEnum>() {
		@Override
		public String toCodeString(ParticipantStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ParticipantStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ParticipantStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ParticipantStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ParticipantStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ParticipantStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
