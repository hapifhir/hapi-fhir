
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NamespaceStatusEnum {

	/**
	 * Code Value: <b>proposed</b>
	 *
	 * System has been submitted but not yet approved.
	 */
	PROPOSED("proposed", "http://hl7.org/fhir/namespace-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * System is valid for use.
	 */
	ACTIVE("active", "http://hl7.org/fhir/namespace-status"),
	
	/**
	 * Code Value: <b>retired</b>
	 *
	 * System should no longer be used.
	 */
	RETIRED("retired", "http://hl7.org/fhir/namespace-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/namespace-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/namespace-status";

	/**
	 * Name for this Value Set:
	 * NamespaceStatus
	 */
	public static final String VALUESET_NAME = "NamespaceStatus";

	private static Map<String, NamespaceStatusEnum> CODE_TO_ENUM = new HashMap<String, NamespaceStatusEnum>();
	private static Map<String, Map<String, NamespaceStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NamespaceStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NamespaceStatusEnum next : NamespaceStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NamespaceStatusEnum>());
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
	public NamespaceStatusEnum forCode(String theCode) {
		NamespaceStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NamespaceStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<NamespaceStatusEnum>() {
		@Override
		public String toCodeString(NamespaceStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NamespaceStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NamespaceStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NamespaceStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NamespaceStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NamespaceStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
