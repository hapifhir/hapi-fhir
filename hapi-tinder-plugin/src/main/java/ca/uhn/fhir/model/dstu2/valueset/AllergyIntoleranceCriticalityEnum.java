
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AllergyIntoleranceCriticalityEnum {

	/**
	 * Display: <b>Low Risk</b><br>
	 * Code Value: <b>low</b>
	 *
	 * The potential clinical impact of a future reaction is estimated as low risk: exposure to substance is unlikely to result in a life threatening or organ system threatening outcome. Future exposure to the Substance is considered a relative contra-indication.
	 */
	LOW_RISK("low", "http://hl7.org/fhir/allergy-intolerance-criticality"),
	
	/**
	 * Display: <b>High Risk</b><br>
	 * Code Value: <b>high</b>
	 *
	 * The potential clinical impact of a future reaction is estimated as high risk: exposure to substance may result in a life threatening or organ system threatening outcome. Future exposure to the Substance may be considered an absolute contra-indication.
	 */
	HIGH_RISK("high", "http://hl7.org/fhir/allergy-intolerance-criticality"),
	
	/**
	 * Display: <b>Unable to determine</b><br>
	 * Code Value: <b>unassessible</b>
	 *
	 * Unable to assess the potential clinical impact with the information available.
	 */
	UNABLE_TO_DETERMINE("unassessible", "http://hl7.org/fhir/allergy-intolerance-criticality"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/allergy-intolerance-criticality
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/allergy-intolerance-criticality";

	/**
	 * Name for this Value Set:
	 * AllergyIntoleranceCriticality
	 */
	public static final String VALUESET_NAME = "AllergyIntoleranceCriticality";

	private static Map<String, AllergyIntoleranceCriticalityEnum> CODE_TO_ENUM = new HashMap<String, AllergyIntoleranceCriticalityEnum>();
	private static Map<String, Map<String, AllergyIntoleranceCriticalityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AllergyIntoleranceCriticalityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AllergyIntoleranceCriticalityEnum next : AllergyIntoleranceCriticalityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AllergyIntoleranceCriticalityEnum>());
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
	public AllergyIntoleranceCriticalityEnum forCode(String theCode) {
		AllergyIntoleranceCriticalityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AllergyIntoleranceCriticalityEnum> VALUESET_BINDER = new IValueSetEnumBinder<AllergyIntoleranceCriticalityEnum>() {
		@Override
		public String toCodeString(AllergyIntoleranceCriticalityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AllergyIntoleranceCriticalityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AllergyIntoleranceCriticalityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AllergyIntoleranceCriticalityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AllergyIntoleranceCriticalityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AllergyIntoleranceCriticalityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
