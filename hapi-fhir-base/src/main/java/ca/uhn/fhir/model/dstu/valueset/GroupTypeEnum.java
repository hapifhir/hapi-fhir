
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum GroupTypeEnum {

	/**
	 * Code Value: <b>person</b>
	 *
	 * Group contains "person" Patient resources.
	 */
	PERSON("person"),
	
	/**
	 * Code Value: <b>animal</b>
	 *
	 * Group contains "animal" Patient resources.
	 */
	ANIMAL("animal"),
	
	/**
	 * Code Value: <b>practitioner</b>
	 *
	 * Group contains healthcare practitioner resources.
	 */
	PRACTITIONER("practitioner"),
	
	/**
	 * Code Value: <b>device</b>
	 *
	 * Group contains Device resources.
	 */
	DEVICE("device"),
	
	/**
	 * Code Value: <b>medication</b>
	 *
	 * Group contains Medication resources.
	 */
	MEDICATION("medication"),
	
	/**
	 * Code Value: <b>substance</b>
	 *
	 * Group contains Substance resources.
	 */
	SUBSTANCE("substance"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/group-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/group-type";

	/**
	 * Name for this Value Set:
	 * GroupType
	 */
	public static final String VALUESET_NAME = "GroupType";

	private static Map<String, GroupTypeEnum> CODE_TO_ENUM = new HashMap<String, GroupTypeEnum>();
	private String myCode;
	
	static {
		for (GroupTypeEnum next : GroupTypeEnum.values()) {
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
	public GroupTypeEnum forCode(String theCode) {
		GroupTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<GroupTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<GroupTypeEnum>() {
		@Override
		public String toCodeString(GroupTypeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public GroupTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	GroupTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
