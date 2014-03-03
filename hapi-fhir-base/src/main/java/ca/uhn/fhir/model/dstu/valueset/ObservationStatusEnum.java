
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ObservationStatusEnum {

	/**
	 * registered
	 * 
	 *
	 * The existence of the observation is registered, but there is no result yet available.
	 */
	REGISTERED("registered"),
	
	/**
	 * preliminary
	 * 
	 *
	 * This is an initial or interim observation: data may be incomplete or unverified.
	 */
	PRELIMINARY("preliminary"),
	
	/**
	 * final
	 * 
	 *
	 * The observation is complete and verified by an authorized person.
	 */
	FINAL("final"),
	
	/**
	 * amended
	 * 
	 *
	 * The observation has been modified subsequent to being Final, and is complete and verified by an authorized person.
	 */
	AMENDED("amended"),
	
	/**
	 * cancelled
	 * 
	 *
	 * The observation is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
	 */
	CANCELLED("cancelled"),
	
	/**
	 * entered in error
	 * 
	 *
	 * The observation has been withdrawn following previous Final release.
	 */
	ENTERED_IN_ERROR("entered in error"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/observation-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/observation-status";

	/**
	 * Name for this Value Set:
	 * ObservationStatus
	 */
	public static final String VALUESET_NAME = "ObservationStatus";

	private static Map<String, ObservationStatusEnum> CODE_TO_ENUM = new HashMap<String, ObservationStatusEnum>();
	private String myCode;
	
	static {
		for (ObservationStatusEnum next : ObservationStatusEnum.values()) {
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
	public ObservationStatusEnum forCode(String theCode) {
		ObservationStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ObservationStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ObservationStatusEnum>() {
		@Override
		public String toCodeString(ObservationStatusEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ObservationStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ObservationStatusEnum(String theCode) {
		myCode = theCode;
	}

	
}
