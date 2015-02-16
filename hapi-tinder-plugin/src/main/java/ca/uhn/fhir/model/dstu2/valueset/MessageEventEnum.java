
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum MessageEventEnum {

	/**
	 * Code Value: <b>MedicationAdministration-Complete</b>
	 *
	 * Change the status of a Medication Administration to show that it is complete.
	 */
	MEDICATIONADMINISTRATION_COMPLETE("MedicationAdministration-Complete", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>MedicationAdministration-Nullification</b>
	 *
	 * Someone wishes to record that the record of administration of a medication is in error and should be ignored.
	 */
	MEDICATIONADMINISTRATION_NULLIFICATION("MedicationAdministration-Nullification", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>MedicationAdministration-Recording</b>
	 *
	 * Indicates that a medication has been recorded against the patient's record.
	 */
	MEDICATIONADMINISTRATION_RECORDING("MedicationAdministration-Recording", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>MedicationAdministration-Update</b>
	 *
	 * Update a Medication Administration record.
	 */
	MEDICATIONADMINISTRATION_UPDATE("MedicationAdministration-Update", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>admin-notify</b>
	 *
	 * Notification of a change to an administrative resource (either create or update). Note that there is no delete, though some administrative resources have status or period elements for this use.
	 */
	ADMIN_NOTIFY("admin-notify", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>conceptmap-translate</b>
	 *
	 * Translate a code from one value set to another, based on the existing value set and concept maps resources, and/or other additional knowledge available to the server. The coded value is carried as an extension in the message header.  The outcome is an operationOutcome with hints, warnings, or errors, or the translated code in an extension.
	 */
	CONCEPTMAP_TRANSLATE("conceptmap-translate", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>diagnosticreport-provide</b>
	 *
	 * Provide a diagnostic report, or update a previously provided diagnostic report.
	 */
	DIAGNOSTICREPORT_PROVIDE("diagnosticreport-provide", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>observation-provide</b>
	 *
	 * Provide a simple observation or update a previously provided simple observation.
	 */
	OBSERVATION_PROVIDE("observation-provide", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>patient-link</b>
	 *
	 * Notification that two patient records actually identify the same patient.
	 */
	PATIENT_LINK("patient-link", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>patient-unlink</b>
	 *
	 * Notification that previous advice that two patient records concern the same patient is now considered incorrect.
	 */
	PATIENT_UNLINK("patient-unlink", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>valueset-expand</b>
	 *
	 * The definition of a value set is used to create a simple collection of codes suitable for use for data entry or validation. An expanded value set will be returned, or an error message.
	 */
	VALUESET_EXPAND("valueset-expand", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>valueset-validate</b>
	 *
	 * Validate that a coded value is in the set of codes allowed by a value set. The coded value is carried as an extension in the message header. The outcome is an OperationOutcome with hints, warnings, or errors.
	 */
	VALUESET_VALIDATE("valueset-validate", "http://hl7.org/fhir/message-events"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/message-events
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/message-events";

	/**
	 * Name for this Value Set:
	 * MessageEvent
	 */
	public static final String VALUESET_NAME = "MessageEvent";

	private static Map<String, MessageEventEnum> CODE_TO_ENUM = new HashMap<String, MessageEventEnum>();
	private static Map<String, Map<String, MessageEventEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, MessageEventEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (MessageEventEnum next : MessageEventEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, MessageEventEnum>());
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
	public MessageEventEnum forCode(String theCode) {
		MessageEventEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<MessageEventEnum> VALUESET_BINDER = new IValueSetEnumBinder<MessageEventEnum>() {
		@Override
		public String toCodeString(MessageEventEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(MessageEventEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public MessageEventEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public MessageEventEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, MessageEventEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	MessageEventEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
