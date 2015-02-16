
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AllergyIntoleranceSeverityEnum {

	/**
	 * Display: <b>Mild</b><br>
	 * Code Value: <b>mild</b>
	 *
	 * Causes mild physiological effects.
	 */
	MILD("mild", "http://hl7.org/fhir/reaction-event-severity"),
	
	/**
	 * Display: <b>Moderate</b><br>
	 * Code Value: <b>moderate</b>
	 *
	 * Causes moderate physiological effects.
	 */
	MODERATE("moderate", "http://hl7.org/fhir/reaction-event-severity"),
	
	/**
	 * Display: <b>Severe</b><br>
	 * Code Value: <b>severe</b>
	 *
	 * Causes severe physiological effects.
	 */
	SEVERE("severe", "http://hl7.org/fhir/reaction-event-severity"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reaction-event-severity
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reaction-event-severity";

	/**
	 * Name for this Value Set:
	 * AllergyIntoleranceSeverity
	 */
	public static final String VALUESET_NAME = "AllergyIntoleranceSeverity";

	private static Map<String, AllergyIntoleranceSeverityEnum> CODE_TO_ENUM = new HashMap<String, AllergyIntoleranceSeverityEnum>();
	private static Map<String, Map<String, AllergyIntoleranceSeverityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AllergyIntoleranceSeverityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AllergyIntoleranceSeverityEnum next : AllergyIntoleranceSeverityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AllergyIntoleranceSeverityEnum>());
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
	public AllergyIntoleranceSeverityEnum forCode(String theCode) {
		AllergyIntoleranceSeverityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AllergyIntoleranceSeverityEnum> VALUESET_BINDER = new IValueSetEnumBinder<AllergyIntoleranceSeverityEnum>() {
		@Override
		public String toCodeString(AllergyIntoleranceSeverityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AllergyIntoleranceSeverityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AllergyIntoleranceSeverityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AllergyIntoleranceSeverityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AllergyIntoleranceSeverityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AllergyIntoleranceSeverityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
