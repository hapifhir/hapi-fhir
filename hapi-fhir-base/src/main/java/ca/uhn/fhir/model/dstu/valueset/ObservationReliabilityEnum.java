
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ObservationReliabilityEnum {

	/**
	 * Code Value: <b>ok</b>
	 *
	 * The result has no reliability concerns.
	 */
	OK("ok", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>ongoing</b>
	 *
	 * An early estimate of value; measurement is still occurring.
	 */
	ONGOING("ongoing", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>early</b>
	 *
	 * An early estimate of value; processing is still occurring.
	 */
	EARLY("early", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>questionable</b>
	 *
	 * The observation value should be treated with care.
	 */
	QUESTIONABLE("questionable", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>calibrating</b>
	 *
	 * The result has been generated while calibration is occurring.
	 */
	CALIBRATING("calibrating", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>error</b>
	 *
	 * The observation could not be completed because of an error.
	 */
	ERROR("error", "http://hl7.org/fhir/observation-reliability"),
	
	/**
	 * Code Value: <b>unknown</b>
	 *
	 * No observation value was available.
	 */
	UNKNOWN("unknown", "http://hl7.org/fhir/observation-reliability"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/observation-reliability
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/observation-reliability";

	/**
	 * Name for this Value Set:
	 * ObservationReliability
	 */
	public static final String VALUESET_NAME = "ObservationReliability";

	private static Map<String, ObservationReliabilityEnum> CODE_TO_ENUM = new HashMap<String, ObservationReliabilityEnum>();
	private static Map<String, Map<String, ObservationReliabilityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ObservationReliabilityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ObservationReliabilityEnum next : ObservationReliabilityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ObservationReliabilityEnum>());
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
	public ObservationReliabilityEnum forCode(String theCode) {
		ObservationReliabilityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ObservationReliabilityEnum> VALUESET_BINDER = new IValueSetEnumBinder<ObservationReliabilityEnum>() {
		@Override
		public String toCodeString(ObservationReliabilityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ObservationReliabilityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ObservationReliabilityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ObservationReliabilityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ObservationReliabilityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ObservationReliabilityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
