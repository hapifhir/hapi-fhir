
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum IdentityAssuranceLevelEnum {

	/**
	 * Display: <b>Level 1</b><br>
	 * Code Value: <b>level1</b>
	 *
	 * Little or no confidence in the asserted identity's accuracy.
	 */
	LEVEL_1("level1", "http://hl7.org/fhir/identity-assuranceLevel"),
	
	/**
	 * Display: <b>Level 2</b><br>
	 * Code Value: <b>level2</b>
	 *
	 * Some confidence in the asserted identity's accuracy.
	 */
	LEVEL_2("level2", "http://hl7.org/fhir/identity-assuranceLevel"),
	
	/**
	 * Display: <b>Level 3</b><br>
	 * Code Value: <b>level3</b>
	 *
	 * High confidence in the asserted identity's accuracy.
	 */
	LEVEL_3("level3", "http://hl7.org/fhir/identity-assuranceLevel"),
	
	/**
	 * Display: <b>Level 4</b><br>
	 * Code Value: <b>level4</b>
	 *
	 * Very high confidence in the asserted identity's accuracy.
	 */
	LEVEL_4("level4", "http://hl7.org/fhir/identity-assuranceLevel"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/identity-assuranceLevel
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/identity-assuranceLevel";

	/**
	 * Name for this Value Set:
	 * IdentityAssuranceLevel
	 */
	public static final String VALUESET_NAME = "IdentityAssuranceLevel";

	private static Map<String, IdentityAssuranceLevelEnum> CODE_TO_ENUM = new HashMap<String, IdentityAssuranceLevelEnum>();
	private static Map<String, Map<String, IdentityAssuranceLevelEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, IdentityAssuranceLevelEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (IdentityAssuranceLevelEnum next : IdentityAssuranceLevelEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, IdentityAssuranceLevelEnum>());
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
	public IdentityAssuranceLevelEnum forCode(String theCode) {
		IdentityAssuranceLevelEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<IdentityAssuranceLevelEnum> VALUESET_BINDER = new IValueSetEnumBinder<IdentityAssuranceLevelEnum>() {
		@Override
		public String toCodeString(IdentityAssuranceLevelEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(IdentityAssuranceLevelEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public IdentityAssuranceLevelEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public IdentityAssuranceLevelEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, IdentityAssuranceLevelEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	IdentityAssuranceLevelEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
