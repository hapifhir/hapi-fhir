
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum QuantityCompararatorEnum {

	/**
	 * Code Value: <b><</b>
	 *
	 * The actual value is less than the given value.
	 */
	LESSTHAN("<", "http://hl7.org/fhir/quantity-comparator"),
	
	/**
	 * Code Value: <b><=</b>
	 *
	 * The actual value is less than or equal to the given value.
	 */
	LESSTHAN_OR_EQUALS("<=", "http://hl7.org/fhir/quantity-comparator"),
	
	/**
	 * Code Value: <b>>=</b>
	 *
	 * The actual value is greater than or equal to the given value.
	 */
	GREATERTHAN_OR_EQUALS(">=", "http://hl7.org/fhir/quantity-comparator"),
	
	/**
	 * Code Value: <b>></b>
	 *
	 * The actual value is greater than the given value.
	 */
	GREATERTHAN(">", "http://hl7.org/fhir/quantity-comparator"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/quantity-comparator
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/quantity-comparator";

	/**
	 * Name for this Value Set:
	 * QuantityCompararator
	 */
	public static final String VALUESET_NAME = "QuantityCompararator";

	private static Map<String, QuantityCompararatorEnum> CODE_TO_ENUM = new HashMap<String, QuantityCompararatorEnum>();
	private static Map<String, Map<String, QuantityCompararatorEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, QuantityCompararatorEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (QuantityCompararatorEnum next : QuantityCompararatorEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, QuantityCompararatorEnum>());
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
	public QuantityCompararatorEnum forCode(String theCode) {
		QuantityCompararatorEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<QuantityCompararatorEnum> VALUESET_BINDER = new IValueSetEnumBinder<QuantityCompararatorEnum>() {
		@Override
		public String toCodeString(QuantityCompararatorEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(QuantityCompararatorEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public QuantityCompararatorEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public QuantityCompararatorEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, QuantityCompararatorEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	QuantityCompararatorEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
