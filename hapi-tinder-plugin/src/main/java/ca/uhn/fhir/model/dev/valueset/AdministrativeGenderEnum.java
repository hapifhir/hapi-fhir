
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdministrativeGenderEnum {

	/**
	 * Code Value: <b>male</b>
	 *
	 * Male
	 */
	MALE("male", "http://hl7.org/fhir/administrative-gender"),
	
	/**
	 * Code Value: <b>female</b>
	 *
	 * Female
	 */
	FEMALE("female", "http://hl7.org/fhir/administrative-gender"),
	
	/**
	 * Code Value: <b>other</b>
	 *
	 * Other
	 */
	OTHER("other", "http://hl7.org/fhir/administrative-gender"),
	
	/**
	 * Code Value: <b>unknown</b>
	 *
	 * Unknown
	 */
	UNKNOWN("unknown", "http://hl7.org/fhir/administrative-gender"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/administrative-gender
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/administrative-gender";

	/**
	 * Name for this Value Set:
	 * AdministrativeGender
	 */
	public static final String VALUESET_NAME = "AdministrativeGender";

	private static Map<String, AdministrativeGenderEnum> CODE_TO_ENUM = new HashMap<String, AdministrativeGenderEnum>();
	private static Map<String, Map<String, AdministrativeGenderEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdministrativeGenderEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdministrativeGenderEnum next : AdministrativeGenderEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdministrativeGenderEnum>());
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
	public AdministrativeGenderEnum forCode(String theCode) {
		AdministrativeGenderEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdministrativeGenderEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdministrativeGenderEnum>() {
		@Override
		public String toCodeString(AdministrativeGenderEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdministrativeGenderEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdministrativeGenderEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdministrativeGenderEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdministrativeGenderEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdministrativeGenderEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
