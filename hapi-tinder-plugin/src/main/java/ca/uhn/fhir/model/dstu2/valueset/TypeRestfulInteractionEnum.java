
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum TypeRestfulInteractionEnum {

	/**
	 * Code Value: <b>read</b>
	 */
	READ("read", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>vread</b>
	 */
	VREAD("vread", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>update</b>
	 */
	UPDATE("update", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>delete</b>
	 */
	DELETE("delete", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>history-instance</b>
	 */
	HISTORY_INSTANCE("history-instance", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>validate</b>
	 */
	VALIDATE("validate", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>history-type</b>
	 */
	HISTORY_TYPE("history-type", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>create</b>
	 */
	CREATE("create", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>search-type</b>
	 */
	SEARCH_TYPE("search-type", "http://hl7.org/fhir/restful-interaction"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/type-restful-interaction
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/type-restful-interaction";

	/**
	 * Name for this Value Set:
	 * TypeRestfulInteraction
	 */
	public static final String VALUESET_NAME = "TypeRestfulInteraction";

	private static Map<String, TypeRestfulInteractionEnum> CODE_TO_ENUM = new HashMap<String, TypeRestfulInteractionEnum>();
	private static Map<String, Map<String, TypeRestfulInteractionEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, TypeRestfulInteractionEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (TypeRestfulInteractionEnum next : TypeRestfulInteractionEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, TypeRestfulInteractionEnum>());
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
	public TypeRestfulInteractionEnum forCode(String theCode) {
		TypeRestfulInteractionEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<TypeRestfulInteractionEnum> VALUESET_BINDER = new IValueSetEnumBinder<TypeRestfulInteractionEnum>() {
		@Override
		public String toCodeString(TypeRestfulInteractionEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(TypeRestfulInteractionEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public TypeRestfulInteractionEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public TypeRestfulInteractionEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, TypeRestfulInteractionEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	TypeRestfulInteractionEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
