
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum CommunicationRequestStatusEnum {

	/**
	 * Code Value: <b>proposed</b>
	 *
	 * The request has been proposed.
	 */
	PROPOSED("proposed", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>planned</b>
	 *
	 * The request has been planned.
	 */
	PLANNED("planned", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>requested</b>
	 *
	 * The request has been placed.
	 */
	REQUESTED("requested", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>received</b>
	 *
	 * The receiving system has received the request but not yet decided whether it will be performed.
	 */
	RECEIVED("received", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>accepted</b>
	 *
	 * The receiving system has accepted the order, but work has not yet commenced.
	 */
	ACCEPTED("accepted", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>in progress</b>
	 *
	 * The work to fulfill the order is happening.
	 */
	IN_PROGRESS("in progress", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>completed</b>
	 *
	 * The work has been complete, the report(s) released, and no further work is planned.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>suspended</b>
	 *
	 * The request has been held by originating system/user request.
	 */
	SUSPENDED("suspended", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>rejected</b>
	 *
	 * The receiving system has declined to fulfill the request.
	 */
	REJECTED("rejected", "http://hl7.org/fhir/communication-request-status"),
	
	/**
	 * Code Value: <b>failed</b>
	 *
	 * The communication was attempted, but due to some procedural error, it could not be completed.
	 */
	FAILED("failed", "http://hl7.org/fhir/communication-request-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/communication-request-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/communication-request-status";

	/**
	 * Name for this Value Set:
	 * CommunicationRequestStatus
	 */
	public static final String VALUESET_NAME = "CommunicationRequestStatus";

	private static Map<String, CommunicationRequestStatusEnum> CODE_TO_ENUM = new HashMap<String, CommunicationRequestStatusEnum>();
	private static Map<String, Map<String, CommunicationRequestStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CommunicationRequestStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CommunicationRequestStatusEnum next : CommunicationRequestStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CommunicationRequestStatusEnum>());
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
	public CommunicationRequestStatusEnum forCode(String theCode) {
		CommunicationRequestStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CommunicationRequestStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<CommunicationRequestStatusEnum>() {
		@Override
		public String toCodeString(CommunicationRequestStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CommunicationRequestStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CommunicationRequestStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CommunicationRequestStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CommunicationRequestStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CommunicationRequestStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
