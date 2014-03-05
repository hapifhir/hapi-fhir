
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ConstraintSeverityEnum {

	/**
	 * Code Value: <b>error</b>
	 *
	 * If the constraint is violated, the resource is not conformant.
	 */
	ERROR("error"),
	
	/**
	 * Code Value: <b>warning</b>
	 *
	 * If the constraint is violated, the resource is conformant, but it is not necessarily following best practice.
	 */
	WARNING("warning"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/constraint-severity
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/constraint-severity";

	/**
	 * Name for this Value Set:
	 * ConstraintSeverity
	 */
	public static final String VALUESET_NAME = "ConstraintSeverity";

	private static Map<String, ConstraintSeverityEnum> CODE_TO_ENUM = new HashMap<String, ConstraintSeverityEnum>();
	private String myCode;
	
	static {
		for (ConstraintSeverityEnum next : ConstraintSeverityEnum.values()) {
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
	public ConstraintSeverityEnum forCode(String theCode) {
		ConstraintSeverityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ConstraintSeverityEnum> VALUESET_BINDER = new IValueSetEnumBinder<ConstraintSeverityEnum>() {
		@Override
		public String toCodeString(ConstraintSeverityEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ConstraintSeverityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ConstraintSeverityEnum(String theCode) {
		myCode = theCode;
	}

	
}
