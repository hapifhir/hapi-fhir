
package ca.uhn.fhir.model.dstu.valueset;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

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
	 * Notification that a patient or other administrative resource as been created or updated.
	 */
	ADMIN_NOTIFY("admin-notify", "http://hl7.org/fhir/message-events"),
	
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
	 * Code Value: <b>query</b>
	 *
	 * Request to perform a query according to the attached query resource.
	 */
	QUERY("query", "http://hl7.org/fhir/message-events"),
	
	/**
	 * Code Value: <b>query-response</b>
	 *
	 * Response with the result of processing the query.
	 */
	QUERY_RESPONSE("query-response", "http://hl7.org/fhir/message-events"),
	
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
