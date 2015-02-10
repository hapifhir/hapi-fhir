
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ParticipationStatusEnum {

	/**
	 * Code Value: <b>accepted</b>
	 *
	 * The participant has accepted the appointment.
	 */
	ACCEPTED("accepted", "http://hl7.org/fhir/participationstatus"),
	
	/**
	 * Code Value: <b>declined</b>
	 *
	 * The participant has declined the appointment and will not participate in the appointment.
	 */
	DECLINED("declined", "http://hl7.org/fhir/participationstatus"),
	
	/**
	 * Code Value: <b>tentative</b>
	 *
	 * The participant has  tentatively accepted the appointment. This could be automatically created by a system and requires further processing before it can be accepted. There is no commitment that attendance will occur.
	 */
	TENTATIVE("tentative", "http://hl7.org/fhir/participationstatus"),
	
	/**
	 * Code Value: <b>in-process</b>
	 *
	 * The participant has started the appointment.
	 */
	IN_PROCESS("in-process", "http://hl7.org/fhir/participationstatus"),
	
	/**
	 * Code Value: <b>completed</b>
	 *
	 * The participant's involvement in the appointment has been completed.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/participationstatus"),
	
	/**
	 * Code Value: <b>needs-action</b>
	 *
	 * The participant needs to indicate if they accept the appointment by changing this status to one of the other statuses.
	 */
	NEEDS_ACTION("needs-action", "http://hl7.org/fhir/participationstatus"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/participationstatus
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/participationstatus";

	/**
	 * Name for this Value Set:
	 * ParticipationStatus
	 */
	public static final String VALUESET_NAME = "ParticipationStatus";

	private static Map<String, ParticipationStatusEnum> CODE_TO_ENUM = new HashMap<String, ParticipationStatusEnum>();
	private static Map<String, Map<String, ParticipationStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ParticipationStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ParticipationStatusEnum next : ParticipationStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ParticipationStatusEnum>());
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
	public ParticipationStatusEnum forCode(String theCode) {
		ParticipationStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ParticipationStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ParticipationStatusEnum>() {
		@Override
		public String toCodeString(ParticipationStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ParticipationStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ParticipationStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ParticipationStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ParticipationStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ParticipationStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
