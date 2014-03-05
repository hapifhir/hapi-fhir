
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum UnitsOfTimeEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/units-of-time
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/units-of-time";

	/**
	 * Name for this Value Set:
	 * UnitsOfTime
	 */
	public static final String VALUESET_NAME = "UnitsOfTime";

	private static Map<String, UnitsOfTimeEnum> CODE_TO_ENUM = new HashMap<String, UnitsOfTimeEnum>();
	private String myCode;
	
	static {
		for (UnitsOfTimeEnum next : UnitsOfTimeEnum.values()) {
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
	public UnitsOfTimeEnum forCode(String theCode) {
		UnitsOfTimeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<UnitsOfTimeEnum> VALUESET_BINDER = new IValueSetEnumBinder<UnitsOfTimeEnum>() {
		@Override
		public String toCodeString(UnitsOfTimeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public UnitsOfTimeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	UnitsOfTimeEnum(String theCode) {
		myCode = theCode;
	}

	
}
