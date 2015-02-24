
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum CarePlan2StatusEnum {

	/**
	 * Code Value: <b>planned</b>
	 *
	 * The plan is in development or awaiting use but is not yet intended to be acted upon.
	 */
	PLANNED("planned", "http://hl7.org/fhir/care-plan2-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * The plan is intended to be followed and used as part of patient care.
	 */
	ACTIVE("active", "http://hl7.org/fhir/care-plan2-status"),
	
	/**
	 * Code Value: <b>completed</b>
	 *
	 * The plan is no longer in use and is not expected to be followed or used in patient care.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/care-plan2-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/care-plan2-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/care-plan2-status";

	/**
	 * Name for this Value Set:
	 * CarePlan2Status
	 */
	public static final String VALUESET_NAME = "CarePlan2Status";

	private static Map<String, CarePlan2StatusEnum> CODE_TO_ENUM = new HashMap<String, CarePlan2StatusEnum>();
	private static Map<String, Map<String, CarePlan2StatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CarePlan2StatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CarePlan2StatusEnum next : CarePlan2StatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CarePlan2StatusEnum>());
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
	public CarePlan2StatusEnum forCode(String theCode) {
		CarePlan2StatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CarePlan2StatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<CarePlan2StatusEnum>() {
		@Override
		public String toCodeString(CarePlan2StatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CarePlan2StatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CarePlan2StatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CarePlan2StatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CarePlan2StatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CarePlan2StatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
