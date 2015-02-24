
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AllergyIntoleranceCertaintyEnum {

	/**
	 * Display: <b>Unlikely</b><br>
	 * Code Value: <b>unlikely</b>
	 *
	 * There is a low level of clinical certainty that the reaction was caused by the identified Substance.
	 */
	UNLIKELY("unlikely", "http://hl7.org/fhir/reaction-event-certainty"),
	
	/**
	 * Display: <b>Likely</b><br>
	 * Code Value: <b>likely</b>
	 *
	 * There is a high level of clinical certainty that the reaction was caused by the identified Substance.
	 */
	LIKELY("likely", "http://hl7.org/fhir/reaction-event-certainty"),
	
	/**
	 * Display: <b>Confirmed</b><br>
	 * Code Value: <b>confirmed</b>
	 *
	 * There is a very high level of clinical certainty that the reaction was due to the identified Substance, which may include clinical evidence by testing or rechallenge.
	 */
	CONFIRMED("confirmed", "http://hl7.org/fhir/reaction-event-certainty"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reaction-event-certainty
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reaction-event-certainty";

	/**
	 * Name for this Value Set:
	 * AllergyIntoleranceCertainty
	 */
	public static final String VALUESET_NAME = "AllergyIntoleranceCertainty";

	private static Map<String, AllergyIntoleranceCertaintyEnum> CODE_TO_ENUM = new HashMap<String, AllergyIntoleranceCertaintyEnum>();
	private static Map<String, Map<String, AllergyIntoleranceCertaintyEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AllergyIntoleranceCertaintyEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AllergyIntoleranceCertaintyEnum next : AllergyIntoleranceCertaintyEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AllergyIntoleranceCertaintyEnum>());
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
	public AllergyIntoleranceCertaintyEnum forCode(String theCode) {
		AllergyIntoleranceCertaintyEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AllergyIntoleranceCertaintyEnum> VALUESET_BINDER = new IValueSetEnumBinder<AllergyIntoleranceCertaintyEnum>() {
		@Override
		public String toCodeString(AllergyIntoleranceCertaintyEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AllergyIntoleranceCertaintyEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AllergyIntoleranceCertaintyEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AllergyIntoleranceCertaintyEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AllergyIntoleranceCertaintyEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AllergyIntoleranceCertaintyEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
