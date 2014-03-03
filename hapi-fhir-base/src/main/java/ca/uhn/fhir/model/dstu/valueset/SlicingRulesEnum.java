
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SlicingRulesEnum {

	/**
	 * closed
	 * 
	 *
	 * No additional content is allowed other than that described by the slices in this profile.
	 */
	CLOSED("closed"),
	
	/**
	 * open
	 * 
	 *
	 * Additional content is allowed anywhere in the list.
	 */
	OPEN("open"),
	
	/**
	 * openAtEnd
	 * 
	 *
	 * Additional content is allowed, but only at the end of the list.
	 */
	OPENATEND("openAtEnd"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/resource-slicing-rules
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/resource-slicing-rules";

	/**
	 * Name for this Value Set:
	 * SlicingRules
	 */
	public static final String VALUESET_NAME = "SlicingRules";

	private static Map<String, SlicingRulesEnum> CODE_TO_ENUM = new HashMap<String, SlicingRulesEnum>();
	private String myCode;
	
	static {
		for (SlicingRulesEnum next : SlicingRulesEnum.values()) {
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
	public SlicingRulesEnum forCode(String theCode) {
		SlicingRulesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SlicingRulesEnum> VALUESET_BINDER = new IValueSetEnumBinder<SlicingRulesEnum>() {
		@Override
		public String toCodeString(SlicingRulesEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public SlicingRulesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	SlicingRulesEnum(String theCode) {
		myCode = theCode;
	}

	
}
