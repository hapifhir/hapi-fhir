
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum UseEnum {

	/**
	 * Code Value: <b>complete</b>
	 *
	 * The treatment is complete and this represents a Claim for the services.
	 */
	COMPLETE("complete", "http://hl7.org/fhir/use-link"),
	
	/**
	 * Code Value: <b>proposed</b>
	 *
	 * The treatment is proposed and this represents a Pre-authorization for the services.
	 */
	PROPOSED("proposed", "http://hl7.org/fhir/use-link"),
	
	/**
	 * Code Value: <b>exploratory</b>
	 *
	 * The treatment is proposed and this represents a Pre-determination for the services.
	 */
	EXPLORATORY("exploratory", "http://hl7.org/fhir/use-link"),
	
	/**
	 * Code Value: <b>other</b>
	 *
	 * A locally defined or otherwise resolved status.
	 */
	OTHER("other", "http://hl7.org/fhir/use-link"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/use-link
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/use-link";

	/**
	 * Name for this Value Set:
	 * Use
	 */
	public static final String VALUESET_NAME = "Use";

	private static Map<String, UseEnum> CODE_TO_ENUM = new HashMap<String, UseEnum>();
	private static Map<String, Map<String, UseEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, UseEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (UseEnum next : UseEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, UseEnum>());
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
	public UseEnum forCode(String theCode) {
		UseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<UseEnum> VALUESET_BINDER = new IValueSetEnumBinder<UseEnum>() {
		@Override
		public String toCodeString(UseEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(UseEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public UseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public UseEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, UseEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	UseEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
