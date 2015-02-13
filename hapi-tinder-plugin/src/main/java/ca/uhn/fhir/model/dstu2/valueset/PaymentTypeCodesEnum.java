
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum PaymentTypeCodesEnum {

	/**
	 * Code Value: <b>payment</b>
	 */
	PAYMENT("payment", "http://www.hl7.org/fhir/contracttypecodes"),
	
	/**
	 * Code Value: <b>adjustment</b>
	 */
	ADJUSTMENT("adjustment", "http://www.hl7.org/fhir/contracttypecodes"),
	
	/**
	 * Code Value: <b>advance</b>
	 */
	ADVANCE("advance", "http://www.hl7.org/fhir/contracttypecodes"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/payment-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/payment-type";

	/**
	 * Name for this Value Set:
	 * Payment Type Codes
	 */
	public static final String VALUESET_NAME = "Payment Type Codes";

	private static Map<String, PaymentTypeCodesEnum> CODE_TO_ENUM = new HashMap<String, PaymentTypeCodesEnum>();
	private static Map<String, Map<String, PaymentTypeCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, PaymentTypeCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (PaymentTypeCodesEnum next : PaymentTypeCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, PaymentTypeCodesEnum>());
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
	public PaymentTypeCodesEnum forCode(String theCode) {
		PaymentTypeCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<PaymentTypeCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<PaymentTypeCodesEnum>() {
		@Override
		public String toCodeString(PaymentTypeCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(PaymentTypeCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public PaymentTypeCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public PaymentTypeCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, PaymentTypeCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	PaymentTypeCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
