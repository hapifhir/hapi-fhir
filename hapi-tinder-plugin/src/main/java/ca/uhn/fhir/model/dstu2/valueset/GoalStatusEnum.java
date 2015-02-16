
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum GoalStatusEnum {

	/**
	 * Code Value: <b>proposed</b>
	 *
	 * A goal is proposed for this patient.
	 */
	PROPOSED("proposed", "http://hl7.org/fhir/goal-status"),
	
	/**
	 * Code Value: <b>planned</b>
	 *
	 * A goal is planned for this patient.
	 */
	PLANNED("planned", "http://hl7.org/fhir/goal-status"),
	
	/**
	 * Code Value: <b>in progress</b>
	 *
	 * The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again).
	 */
	IN_PROGRESS("in progress", "http://hl7.org/fhir/goal-status"),
	
	/**
	 * Code Value: <b>achieved</b>
	 *
	 * The goal has been met and no further action is needed.
	 */
	ACHIEVED("achieved", "http://hl7.org/fhir/goal-status"),
	
	/**
	 * Code Value: <b>sustaining</b>
	 *
	 * The goal has been met, but ongoing activity is needed to sustain the goal objective.
	 */
	SUSTAINING("sustaining", "http://hl7.org/fhir/goal-status"),
	
	/**
	 * Code Value: <b>cancelled</b>
	 *
	 * The goal is no longer being sought.
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/goal-status"),
	
	/**
	 * Code Value: <b>accepted</b>
	 *
	 * A proposed goal was accepted.
	 */
	ACCEPTED("accepted", "http://hl7.org/fhir/goal-status"),
	
	/**
	 * Code Value: <b>rejected</b>
	 *
	 * A proposed goal was rejected.
	 */
	REJECTED("rejected", "http://hl7.org/fhir/goal-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/goal-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/goal-status";

	/**
	 * Name for this Value Set:
	 * GoalStatus
	 */
	public static final String VALUESET_NAME = "GoalStatus";

	private static Map<String, GoalStatusEnum> CODE_TO_ENUM = new HashMap<String, GoalStatusEnum>();
	private static Map<String, Map<String, GoalStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, GoalStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (GoalStatusEnum next : GoalStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, GoalStatusEnum>());
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
	public GoalStatusEnum forCode(String theCode) {
		GoalStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<GoalStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<GoalStatusEnum>() {
		@Override
		public String toCodeString(GoalStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(GoalStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public GoalStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public GoalStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, GoalStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	GoalStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
