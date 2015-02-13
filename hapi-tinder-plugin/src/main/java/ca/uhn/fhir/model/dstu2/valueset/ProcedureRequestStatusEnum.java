
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ProcedureRequestStatusEnum {

	/**
	 * Code Value: <b>proposed</b>
	 *
	 * The request has been proposed.
	 */
	PROPOSED("proposed", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>planned</b>
	 *
	 * The request has been planned.
	 */
	PLANNED("planned", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>requested</b>
	 *
	 * The request has been placed.
	 */
	REQUESTED("requested", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>received</b>
	 *
	 * The receiving system has received the request but not yet decided whether it will be performed.
	 */
	RECEIVED("received", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>accepted</b>
	 *
	 * The receiving system has accepted the request, but work has not yet commenced.
	 */
	ACCEPTED("accepted", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>in progress</b>
	 *
	 * The work to fulfill the request is happening.
	 */
	IN_PROGRESS("in progress", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>completed</b>
	 *
	 * The work has been complete, the report(s) released, and no further work is planned.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>suspended</b>
	 *
	 * The request has been held by originating system/user request.
	 */
	SUSPENDED("suspended", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>rejected</b>
	 *
	 * The receiving system has declined to fulfill the request.
	 */
	REJECTED("rejected", "http://hl7.org/fhir/procedure-request-status"),
	
	/**
	 * Code Value: <b>aborted</b>
	 *
	 * The request was attempted, but due to some procedural error, it could not be completed.
	 */
	ABORTED("aborted", "http://hl7.org/fhir/procedure-request-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/procedure-request-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/procedure-request-status";

	/**
	 * Name for this Value Set:
	 * ProcedureRequestStatus
	 */
	public static final String VALUESET_NAME = "ProcedureRequestStatus";

	private static Map<String, ProcedureRequestStatusEnum> CODE_TO_ENUM = new HashMap<String, ProcedureRequestStatusEnum>();
	private static Map<String, Map<String, ProcedureRequestStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ProcedureRequestStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ProcedureRequestStatusEnum next : ProcedureRequestStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ProcedureRequestStatusEnum>());
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
	public ProcedureRequestStatusEnum forCode(String theCode) {
		ProcedureRequestStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ProcedureRequestStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ProcedureRequestStatusEnum>() {
		@Override
		public String toCodeString(ProcedureRequestStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ProcedureRequestStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ProcedureRequestStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ProcedureRequestStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ProcedureRequestStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ProcedureRequestStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
