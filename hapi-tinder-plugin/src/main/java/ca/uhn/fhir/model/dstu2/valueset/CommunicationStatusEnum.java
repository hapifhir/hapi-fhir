
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum CommunicationStatusEnum {

	/**
	 * Code Value: <b>in progress</b>
	 *
	 * The communication transmission is ongoing.
	 */
	IN_PROGRESS("in progress", "http://hl7.org/fhir/communication-status"),
	
	/**
	 * Code Value: <b>completed</b>
	 *
	 * The message transmission is complete, i.e., delivered to the recipient's destination.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/communication-status"),
	
	/**
	 * Code Value: <b>suspended</b>
	 *
	 * The communication transmission has been held by originating system/user request.
	 */
	SUSPENDED("suspended", "http://hl7.org/fhir/communication-status"),
	
	/**
	 * Code Value: <b>rejected</b>
	 *
	 * The receiving system has declined to accept the message.
	 */
	REJECTED("rejected", "http://hl7.org/fhir/communication-status"),
	
	/**
	 * Code Value: <b>failed</b>
	 *
	 * There was a failure in transmitting the message out.
	 */
	FAILED("failed", "http://hl7.org/fhir/communication-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/communication-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/communication-status";

	/**
	 * Name for this Value Set:
	 * CommunicationStatus
	 */
	public static final String VALUESET_NAME = "CommunicationStatus";

	private static Map<String, CommunicationStatusEnum> CODE_TO_ENUM = new HashMap<String, CommunicationStatusEnum>();
	private static Map<String, Map<String, CommunicationStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CommunicationStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CommunicationStatusEnum next : CommunicationStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CommunicationStatusEnum>());
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
	public CommunicationStatusEnum forCode(String theCode) {
		CommunicationStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CommunicationStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<CommunicationStatusEnum>() {
		@Override
		public String toCodeString(CommunicationStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CommunicationStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CommunicationStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CommunicationStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CommunicationStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CommunicationStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
