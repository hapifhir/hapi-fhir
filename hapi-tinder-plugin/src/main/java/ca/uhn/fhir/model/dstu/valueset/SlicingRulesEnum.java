
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SlicingRulesEnum {

	/**
	 * Code Value: <b>closed</b>
	 *
	 * No additional content is allowed other than that described by the slices in this profile.
	 */
	CLOSED("closed", "http://hl7.org/fhir/resource-slicing-rules"),
	
	/**
	 * Code Value: <b>open</b>
	 *
	 * Additional content is allowed anywhere in the list.
	 */
	OPEN("open", "http://hl7.org/fhir/resource-slicing-rules"),
	
	/**
	 * Code Value: <b>openAtEnd</b>
	 *
	 * Additional content is allowed, but only at the end of the list.
	 */
	OPENATEND("openAtEnd", "http://hl7.org/fhir/resource-slicing-rules"),
	
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
	private static Map<String, Map<String, SlicingRulesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SlicingRulesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SlicingRulesEnum next : SlicingRulesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SlicingRulesEnum>());
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
		public String toSystemString(SlicingRulesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SlicingRulesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SlicingRulesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SlicingRulesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SlicingRulesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
