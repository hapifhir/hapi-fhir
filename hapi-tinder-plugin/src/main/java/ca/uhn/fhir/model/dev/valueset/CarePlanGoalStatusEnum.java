
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum CarePlanGoalStatusEnum {

	/**
	 * Code Value: <b>in progress</b>
	 *
	 * The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again).
	 */
	IN_PROGRESS("in progress", "http://hl7.org/fhir/care-plan-goal-status"),
	
	/**
	 * Code Value: <b>achieved</b>
	 *
	 * The goal has been met and no further action is needed.
	 */
	ACHIEVED("achieved", "http://hl7.org/fhir/care-plan-goal-status"),
	
	/**
	 * Code Value: <b>sustaining</b>
	 *
	 * The goal has been met, but ongoing activity is needed to sustain the goal objective.
	 */
	SUSTAINING("sustaining", "http://hl7.org/fhir/care-plan-goal-status"),
	
	/**
	 * Code Value: <b>cancelled</b>
	 *
	 * The goal is no longer being sought.
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/care-plan-goal-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/care-plan-goal-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/care-plan-goal-status";

	/**
	 * Name for this Value Set:
	 * CarePlanGoalStatus
	 */
	public static final String VALUESET_NAME = "CarePlanGoalStatus";

	private static Map<String, CarePlanGoalStatusEnum> CODE_TO_ENUM = new HashMap<String, CarePlanGoalStatusEnum>();
	private static Map<String, Map<String, CarePlanGoalStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CarePlanGoalStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CarePlanGoalStatusEnum next : CarePlanGoalStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CarePlanGoalStatusEnum>());
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
	public CarePlanGoalStatusEnum forCode(String theCode) {
		CarePlanGoalStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CarePlanGoalStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<CarePlanGoalStatusEnum>() {
		@Override
		public String toCodeString(CarePlanGoalStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CarePlanGoalStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CarePlanGoalStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CarePlanGoalStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CarePlanGoalStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CarePlanGoalStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
