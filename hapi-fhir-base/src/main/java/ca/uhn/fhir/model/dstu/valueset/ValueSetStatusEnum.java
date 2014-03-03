
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ValueSetStatusEnum {

	/**
	 * draft
	 * 
	 *
	 * This valueset is still under development.
	 */
	DRAFT("draft"),
	
	/**
	 * active
	 * 
	 *
	 * This valueset is ready for normal use.
	 */
	ACTIVE("active"),
	
	/**
	 * retired
	 * 
	 *
	 * This valueset has been withdrawn or superceded and should no longer be used.
	 */
	RETIRED("retired"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/valueset-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/valueset-status";

	/**
	 * Name for this Value Set:
	 * ValueSetStatus
	 */
	public static final String VALUESET_NAME = "ValueSetStatus";

	private static Map<String, ValueSetStatusEnum> CODE_TO_ENUM = new HashMap<String, ValueSetStatusEnum>();
	private String myCode;
	
	static {
		for (ValueSetStatusEnum next : ValueSetStatusEnum.values()) {
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
	public ValueSetStatusEnum forCode(String theCode) {
		ValueSetStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ValueSetStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ValueSetStatusEnum>() {
		@Override
		public String toCodeString(ValueSetStatusEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ValueSetStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ValueSetStatusEnum(String theCode) {
		myCode = theCode;
	}

	
}
