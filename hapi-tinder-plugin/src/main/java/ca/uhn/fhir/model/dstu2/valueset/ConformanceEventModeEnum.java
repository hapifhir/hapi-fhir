
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ConformanceEventModeEnum {

	/**
	 * Code Value: <b>sender</b>
	 *
	 * The application sends requests and receives responses.
	 */
	SENDER("sender", "http://hl7.org/fhir/message-conformance-event-mode"),
	
	/**
	 * Code Value: <b>receiver</b>
	 *
	 * The application receives requests and sends responses.
	 */
	RECEIVER("receiver", "http://hl7.org/fhir/message-conformance-event-mode"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/message-conformance-event-mode
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/message-conformance-event-mode";

	/**
	 * Name for this Value Set:
	 * ConformanceEventMode
	 */
	public static final String VALUESET_NAME = "ConformanceEventMode";

	private static Map<String, ConformanceEventModeEnum> CODE_TO_ENUM = new HashMap<String, ConformanceEventModeEnum>();
	private static Map<String, Map<String, ConformanceEventModeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ConformanceEventModeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ConformanceEventModeEnum next : ConformanceEventModeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ConformanceEventModeEnum>());
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
	public ConformanceEventModeEnum forCode(String theCode) {
		ConformanceEventModeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ConformanceEventModeEnum> VALUESET_BINDER = new IValueSetEnumBinder<ConformanceEventModeEnum>() {
		@Override
		public String toCodeString(ConformanceEventModeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ConformanceEventModeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ConformanceEventModeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ConformanceEventModeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ConformanceEventModeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ConformanceEventModeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
