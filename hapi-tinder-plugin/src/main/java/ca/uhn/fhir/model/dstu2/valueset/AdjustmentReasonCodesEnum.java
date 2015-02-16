
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdjustmentReasonCodesEnum {

	/**
	 * Code Value: <b>A001</b>
	 */
	A001("A001", "http://hl7.org/fhir/adjustment-reason"),
	
	/**
	 * Code Value: <b>A002</b>
	 */
	A002("A002", "http://hl7.org/fhir/adjustment-reason"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/adjustment-reason
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/adjustment-reason";

	/**
	 * Name for this Value Set:
	 * Adjustment Reason Codes
	 */
	public static final String VALUESET_NAME = "Adjustment Reason Codes";

	private static Map<String, AdjustmentReasonCodesEnum> CODE_TO_ENUM = new HashMap<String, AdjustmentReasonCodesEnum>();
	private static Map<String, Map<String, AdjustmentReasonCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdjustmentReasonCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdjustmentReasonCodesEnum next : AdjustmentReasonCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdjustmentReasonCodesEnum>());
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
	public AdjustmentReasonCodesEnum forCode(String theCode) {
		AdjustmentReasonCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdjustmentReasonCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdjustmentReasonCodesEnum>() {
		@Override
		public String toCodeString(AdjustmentReasonCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdjustmentReasonCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdjustmentReasonCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdjustmentReasonCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdjustmentReasonCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdjustmentReasonCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
