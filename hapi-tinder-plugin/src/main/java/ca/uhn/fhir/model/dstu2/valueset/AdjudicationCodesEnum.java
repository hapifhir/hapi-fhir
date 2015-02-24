
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdjudicationCodesEnum {

	/**
	 * Code Value: <b>total</b>
	 */
	TOTAL("total", "http://hl7.org/fhir/adjudication"),
	
	/**
	 * Code Value: <b>copay</b>
	 */
	COPAY("copay", "http://hl7.org/fhir/adjudication"),
	
	/**
	 * Code Value: <b>eligible</b>
	 */
	ELIGIBLE("eligible", "http://hl7.org/fhir/adjudication"),
	
	/**
	 * Code Value: <b>deductable</b>
	 */
	DEDUCTABLE("deductable", "http://hl7.org/fhir/adjudication"),
	
	/**
	 * Code Value: <b>eligpercent</b>
	 */
	ELIGPERCENT("eligpercent", "http://hl7.org/fhir/adjudication"),
	
	/**
	 * Code Value: <b>tax</b>
	 */
	TAX("tax", "http://hl7.org/fhir/adjudication"),
	
	/**
	 * Code Value: <b>benefit</b>
	 */
	BENEFIT("benefit", "http://hl7.org/fhir/adjudication"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/adjudication
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/adjudication";

	/**
	 * Name for this Value Set:
	 * Adjudication Codes
	 */
	public static final String VALUESET_NAME = "Adjudication Codes";

	private static Map<String, AdjudicationCodesEnum> CODE_TO_ENUM = new HashMap<String, AdjudicationCodesEnum>();
	private static Map<String, Map<String, AdjudicationCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdjudicationCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdjudicationCodesEnum next : AdjudicationCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdjudicationCodesEnum>());
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
	public AdjudicationCodesEnum forCode(String theCode) {
		AdjudicationCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdjudicationCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdjudicationCodesEnum>() {
		@Override
		public String toCodeString(AdjudicationCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdjudicationCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdjudicationCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdjudicationCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdjudicationCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdjudicationCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
