
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum QuantityCompararatorEnum {

	/**
	 * <
	 * 
	 *
	 * The actual value is less than the given value.
	 */
	LESSTHAN("<"),
	
	/**
	 * <=
	 * 
	 *
	 * The actual value is less than or equal to the given value.
	 */
	LESSTHAN_OR_EQUALS("<="),
	
	/**
	 * >=
	 * 
	 *
	 * The actual value is greater than or equal to the given value.
	 */
	GREATERTHAN_OR_EQUALS(">="),
	
	/**
	 * >
	 * 
	 *
	 * The actual value is greater than the given value.
	 */
	GREATERTHAN(">"),
	
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
	private String myCode;
	
	static {
		for (QuantityCompararatorEnum next : QuantityCompararatorEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
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
		public QuantityCompararatorEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	QuantityCompararatorEnum(String theCode) {
		myCode = theCode;
	}

	
}
