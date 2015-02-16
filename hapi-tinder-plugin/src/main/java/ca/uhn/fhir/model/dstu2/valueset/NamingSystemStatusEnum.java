
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NamingSystemStatusEnum {

	/**
	 * Code Value: <b>proposed</b>
	 *
	 * System has been submitted but not yet approved.
	 */
	PROPOSED("proposed", "http://hl7.org/fhir/namingsystem-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * System is valid for use.
	 */
	ACTIVE("active", "http://hl7.org/fhir/namingsystem-status"),
	
	/**
	 * Code Value: <b>retired</b>
	 *
	 * System should no longer be used.
	 */
	RETIRED("retired", "http://hl7.org/fhir/namingsystem-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/namingsystem-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/namingsystem-status";

	/**
	 * Name for this Value Set:
	 * NamingSystemStatus
	 */
	public static final String VALUESET_NAME = "NamingSystemStatus";

	private static Map<String, NamingSystemStatusEnum> CODE_TO_ENUM = new HashMap<String, NamingSystemStatusEnum>();
	private static Map<String, Map<String, NamingSystemStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NamingSystemStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NamingSystemStatusEnum next : NamingSystemStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NamingSystemStatusEnum>());
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
	public NamingSystemStatusEnum forCode(String theCode) {
		NamingSystemStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NamingSystemStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<NamingSystemStatusEnum>() {
		@Override
		public String toCodeString(NamingSystemStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NamingSystemStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NamingSystemStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NamingSystemStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NamingSystemStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NamingSystemStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
