
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum DiagnosticOrderPriorityEnum {

	/**
	 * Display: <b>Routine</b><br/>
	 * Code Value: <b>routine</b>
	 *
	 * The order has a normal priority.
	 */
	ROUTINE("routine", "http://hl7.org/fhir/diagnostic-order-priority"),
	
	/**
	 * Display: <b>Urgent</b><br/>
	 * Code Value: <b>urgent</b>
	 *
	 * The order should be urgently.
	 */
	URGENT("urgent", "http://hl7.org/fhir/diagnostic-order-priority"),
	
	/**
	 * Display: <b>Stat</b><br/>
	 * Code Value: <b>stat</b>
	 *
	 * The order is time-critical.
	 */
	STAT("stat", "http://hl7.org/fhir/diagnostic-order-priority"),
	
	/**
	 * Display: <b>ASAP</b><br/>
	 * Code Value: <b>asap</b>
	 *
	 * The order should be acted on as soon as possible.
	 */
	ASAP("asap", "http://hl7.org/fhir/diagnostic-order-priority"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/diagnostic-order-priority
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/diagnostic-order-priority";

	/**
	 * Name for this Value Set:
	 * DiagnosticOrderPriority
	 */
	public static final String VALUESET_NAME = "DiagnosticOrderPriority";

	private static Map<String, DiagnosticOrderPriorityEnum> CODE_TO_ENUM = new HashMap<String, DiagnosticOrderPriorityEnum>();
	private static Map<String, Map<String, DiagnosticOrderPriorityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DiagnosticOrderPriorityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DiagnosticOrderPriorityEnum next : DiagnosticOrderPriorityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DiagnosticOrderPriorityEnum>());
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
	public DiagnosticOrderPriorityEnum forCode(String theCode) {
		DiagnosticOrderPriorityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DiagnosticOrderPriorityEnum> VALUESET_BINDER = new IValueSetEnumBinder<DiagnosticOrderPriorityEnum>() {
		@Override
		public String toCodeString(DiagnosticOrderPriorityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DiagnosticOrderPriorityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DiagnosticOrderPriorityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DiagnosticOrderPriorityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DiagnosticOrderPriorityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DiagnosticOrderPriorityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
