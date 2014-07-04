
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum GroupTypeEnum {

	/**
	 * Code Value: <b>person</b>
	 *
	 * Group contains "person" Patient resources.
	 */
	PERSON("person", "http://hl7.org/fhir/group-type"),
	
	/**
	 * Code Value: <b>animal</b>
	 *
	 * Group contains "animal" Patient resources.
	 */
	ANIMAL("animal", "http://hl7.org/fhir/group-type"),
	
	/**
	 * Code Value: <b>practitioner</b>
	 *
	 * Group contains healthcare practitioner resources.
	 */
	PRACTITIONER("practitioner", "http://hl7.org/fhir/group-type"),
	
	/**
	 * Code Value: <b>device</b>
	 *
	 * Group contains Device resources.
	 */
	DEVICE("device", "http://hl7.org/fhir/group-type"),
	
	/**
	 * Code Value: <b>medication</b>
	 *
	 * Group contains Medication resources.
	 */
	MEDICATION("medication", "http://hl7.org/fhir/group-type"),
	
	/**
	 * Code Value: <b>substance</b>
	 *
	 * Group contains Substance resources.
	 */
	SUBSTANCE("substance", "http://hl7.org/fhir/group-type"),
	
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
	private static Map<String, Map<String, GroupTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, GroupTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (GroupTypeEnum next : GroupTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, GroupTypeEnum>());
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
		public String toSystemString(GroupTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public GroupTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public GroupTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, GroupTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	GroupTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
