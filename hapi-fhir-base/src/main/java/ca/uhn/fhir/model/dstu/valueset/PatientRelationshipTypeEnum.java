
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum PatientRelationshipTypeEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/relatedperson-relationshiptype
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/relatedperson-relationshiptype";

	/**
	 * Name for this Value Set:
	 * PatientRelationshipType
	 */
	public static final String VALUESET_NAME = "PatientRelationshipType";

	private static Map<String, PatientRelationshipTypeEnum> CODE_TO_ENUM = new HashMap<String, PatientRelationshipTypeEnum>();
	private static Map<String, Map<String, PatientRelationshipTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, PatientRelationshipTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (PatientRelationshipTypeEnum next : PatientRelationshipTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, PatientRelationshipTypeEnum>());
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
	public PatientRelationshipTypeEnum forCode(String theCode) {
		PatientRelationshipTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<PatientRelationshipTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<PatientRelationshipTypeEnum>() {
		@Override
		public String toCodeString(PatientRelationshipTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(PatientRelationshipTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public PatientRelationshipTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public PatientRelationshipTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, PatientRelationshipTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	PatientRelationshipTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
