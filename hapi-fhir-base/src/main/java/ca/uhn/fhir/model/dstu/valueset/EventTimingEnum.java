
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum EventTimingEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/event-timing
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/event-timing";

	/**
	 * Name for this Value Set:
	 * EventTiming
	 */
	public static final String VALUESET_NAME = "EventTiming";

	private static Map<String, EventTimingEnum> CODE_TO_ENUM = new HashMap<String, EventTimingEnum>();
	private String myCode;
	
	static {
		for (EventTimingEnum next : EventTimingEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}
	
	/**
	 * Returns the enumerated value associated with this code
	 */
	public EventTimingEnum forCode(String theCode) {
		EventTimingEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EventTimingEnum> VALUESET_BINDER = new IValueSetEnumBinder<EventTimingEnum>() {
		@Override
		public String toCodeString(EventTimingEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public EventTimingEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	EventTimingEnum(String theCode) {
		myCode = theCode;
	}

	
}
