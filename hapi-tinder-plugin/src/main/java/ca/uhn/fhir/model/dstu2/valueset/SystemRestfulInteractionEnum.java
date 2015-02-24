
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SystemRestfulInteractionEnum {

	/**
	 * Code Value: <b>transaction</b>
	 */
	TRANSACTION("transaction", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>search-system</b>
	 */
	SEARCH_SYSTEM("search-system", "http://hl7.org/fhir/restful-interaction"),
	
	/**
	 * Code Value: <b>history-system</b>
	 */
	HISTORY_SYSTEM("history-system", "http://hl7.org/fhir/restful-interaction"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/system-restful-interaction
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/system-restful-interaction";

	/**
	 * Name for this Value Set:
	 * SystemRestfulInteraction
	 */
	public static final String VALUESET_NAME = "SystemRestfulInteraction";

	private static Map<String, SystemRestfulInteractionEnum> CODE_TO_ENUM = new HashMap<String, SystemRestfulInteractionEnum>();
	private static Map<String, Map<String, SystemRestfulInteractionEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SystemRestfulInteractionEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SystemRestfulInteractionEnum next : SystemRestfulInteractionEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SystemRestfulInteractionEnum>());
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
	public SystemRestfulInteractionEnum forCode(String theCode) {
		SystemRestfulInteractionEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SystemRestfulInteractionEnum> VALUESET_BINDER = new IValueSetEnumBinder<SystemRestfulInteractionEnum>() {
		@Override
		public String toCodeString(SystemRestfulInteractionEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SystemRestfulInteractionEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SystemRestfulInteractionEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SystemRestfulInteractionEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SystemRestfulInteractionEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SystemRestfulInteractionEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
