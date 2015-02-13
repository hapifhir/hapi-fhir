
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum RemittanceOutcomeEnum {

	/**
	 * Code Value: <b>complete</b>
	 *
	 * The processing completed without errors.
	 */
	COMPLETE("complete", "http://hl7.org/fhir/RS-link"),
	
	/**
	 * Code Value: <b>error</b>
	 *
	 * The processing identified with errors.
	 */
	ERROR("error", "http://hl7.org/fhir/RS-link"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/RS-link
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/RS-link";

	/**
	 * Name for this Value Set:
	 * RemittanceOutcome
	 */
	public static final String VALUESET_NAME = "RemittanceOutcome";

	private static Map<String, RemittanceOutcomeEnum> CODE_TO_ENUM = new HashMap<String, RemittanceOutcomeEnum>();
	private static Map<String, Map<String, RemittanceOutcomeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, RemittanceOutcomeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (RemittanceOutcomeEnum next : RemittanceOutcomeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, RemittanceOutcomeEnum>());
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
	public RemittanceOutcomeEnum forCode(String theCode) {
		RemittanceOutcomeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<RemittanceOutcomeEnum> VALUESET_BINDER = new IValueSetEnumBinder<RemittanceOutcomeEnum>() {
		@Override
		public String toCodeString(RemittanceOutcomeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(RemittanceOutcomeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public RemittanceOutcomeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public RemittanceOutcomeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, RemittanceOutcomeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	RemittanceOutcomeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
