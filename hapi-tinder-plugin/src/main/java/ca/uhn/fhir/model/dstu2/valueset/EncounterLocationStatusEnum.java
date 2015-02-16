
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum EncounterLocationStatusEnum {

	/**
	 * Display: <b>Planned</b><br>
	 * Code Value: <b>planned</b>
	 *
	 * The patient is planned to be moved to this location at some point in the future.
	 */
	PLANNED("planned", "http://hl7.org/fhir/encounter-location-status"),
	
	/**
	 * Display: <b>Present</b><br>
	 * Code Value: <b>present</b>
	 *
	 * The patient is currently at this location, or was between the period specified.
	 */
	PRESENT("present", "http://hl7.org/fhir/encounter-location-status"),
	
	/**
	 * Display: <b>Reserved</b><br>
	 * Code Value: <b>reserved</b>
	 *
	 * This location is held empty for this patient.
	 */
	RESERVED("reserved", "http://hl7.org/fhir/encounter-location-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/encounter-location-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/encounter-location-status";

	/**
	 * Name for this Value Set:
	 * EncounterLocationStatus
	 */
	public static final String VALUESET_NAME = "EncounterLocationStatus";

	private static Map<String, EncounterLocationStatusEnum> CODE_TO_ENUM = new HashMap<String, EncounterLocationStatusEnum>();
	private static Map<String, Map<String, EncounterLocationStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, EncounterLocationStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (EncounterLocationStatusEnum next : EncounterLocationStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, EncounterLocationStatusEnum>());
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
	public EncounterLocationStatusEnum forCode(String theCode) {
		EncounterLocationStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EncounterLocationStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<EncounterLocationStatusEnum>() {
		@Override
		public String toCodeString(EncounterLocationStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(EncounterLocationStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public EncounterLocationStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public EncounterLocationStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, EncounterLocationStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	EncounterLocationStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
