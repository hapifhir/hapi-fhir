
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SpecimenCollectionMethodEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/specimen-collection-method
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/specimen-collection-method";

	/**
	 * Name for this Value Set:
	 * SpecimenCollectionMethod
	 */
	public static final String VALUESET_NAME = "SpecimenCollectionMethod";

	private static Map<String, SpecimenCollectionMethodEnum> CODE_TO_ENUM = new HashMap<String, SpecimenCollectionMethodEnum>();
	private static Map<String, Map<String, SpecimenCollectionMethodEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SpecimenCollectionMethodEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SpecimenCollectionMethodEnum next : SpecimenCollectionMethodEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SpecimenCollectionMethodEnum>());
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
	public SpecimenCollectionMethodEnum forCode(String theCode) {
		SpecimenCollectionMethodEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SpecimenCollectionMethodEnum> VALUESET_BINDER = new IValueSetEnumBinder<SpecimenCollectionMethodEnum>() {
		@Override
		public String toCodeString(SpecimenCollectionMethodEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SpecimenCollectionMethodEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SpecimenCollectionMethodEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SpecimenCollectionMethodEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SpecimenCollectionMethodEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SpecimenCollectionMethodEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
