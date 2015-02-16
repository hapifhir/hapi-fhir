
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdjudicationErrorCodesEnum {

	/**
	 * Code Value: <b>A001</b>
	 */
	A001("A001", "http://hl7.org/fhir/adjudication-error"),
	
	/**
	 * Code Value: <b>A002</b>
	 */
	A002("A002", "http://hl7.org/fhir/adjudication-error"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/adjudication-error
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/adjudication-error";

	/**
	 * Name for this Value Set:
	 * Adjudication Error Codes
	 */
	public static final String VALUESET_NAME = "Adjudication Error Codes";

	private static Map<String, AdjudicationErrorCodesEnum> CODE_TO_ENUM = new HashMap<String, AdjudicationErrorCodesEnum>();
	private static Map<String, Map<String, AdjudicationErrorCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdjudicationErrorCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdjudicationErrorCodesEnum next : AdjudicationErrorCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdjudicationErrorCodesEnum>());
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
	public AdjudicationErrorCodesEnum forCode(String theCode) {
		AdjudicationErrorCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdjudicationErrorCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdjudicationErrorCodesEnum>() {
		@Override
		public String toCodeString(AdjudicationErrorCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdjudicationErrorCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdjudicationErrorCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdjudicationErrorCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdjudicationErrorCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdjudicationErrorCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
