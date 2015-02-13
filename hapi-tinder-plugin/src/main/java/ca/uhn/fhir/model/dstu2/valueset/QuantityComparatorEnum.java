
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum QuantityComparatorEnum {

	/**
	 * Code Value: <b>&lt;</b>
	 *
	 * The actual value is less than the given value.
	 */
	LESSTHAN("<", "http://hl7.org/fhir/quantity-comparator"),
	
	/**
	 * Code Value: <b>&lt;=</b>
	 *
	 * The actual value is less than or equal to the given value.
	 */
	LESSTHAN_OR_EQUALS("<=", "http://hl7.org/fhir/quantity-comparator"),
	
	/**
	 * Code Value: <b>&gt;=</b>
	 *
	 * The actual value is greater than or equal to the given value.
	 */
	GREATERTHAN_OR_EQUALS(">=", "http://hl7.org/fhir/quantity-comparator"),
	
	/**
	 * Code Value: <b>&gt;</b>
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
	 * QuantityComparator
	 */
	public static final String VALUESET_NAME = "QuantityComparator";

	private static Map<String, QuantityComparatorEnum> CODE_TO_ENUM = new HashMap<String, QuantityComparatorEnum>();
	private static Map<String, Map<String, QuantityComparatorEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, QuantityComparatorEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (QuantityComparatorEnum next : QuantityComparatorEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, QuantityComparatorEnum>());
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
	public QuantityComparatorEnum forCode(String theCode) {
		QuantityComparatorEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<QuantityComparatorEnum> VALUESET_BINDER = new IValueSetEnumBinder<QuantityComparatorEnum>() {
		@Override
		public String toCodeString(QuantityComparatorEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(QuantityComparatorEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public QuantityComparatorEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public QuantityComparatorEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, QuantityComparatorEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	QuantityComparatorEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
