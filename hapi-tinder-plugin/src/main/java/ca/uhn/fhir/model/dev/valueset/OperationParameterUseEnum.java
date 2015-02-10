
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum OperationParameterUseEnum {

	/**
	 * Code Value: <b>in</b>
	 *
	 * This is an input parameter.
	 */
	IN("in", "http://hl7.org/fhir/operation-parameter-use"),
	
	/**
	 * Code Value: <b>out</b>
	 *
	 * This is an output parameter.
	 */
	OUT("out", "http://hl7.org/fhir/operation-parameter-use"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/operation-parameter-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/operation-parameter-use";

	/**
	 * Name for this Value Set:
	 * OperationParameterUse
	 */
	public static final String VALUESET_NAME = "OperationParameterUse";

	private static Map<String, OperationParameterUseEnum> CODE_TO_ENUM = new HashMap<String, OperationParameterUseEnum>();
	private static Map<String, Map<String, OperationParameterUseEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, OperationParameterUseEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (OperationParameterUseEnum next : OperationParameterUseEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, OperationParameterUseEnum>());
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
	public OperationParameterUseEnum forCode(String theCode) {
		OperationParameterUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<OperationParameterUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<OperationParameterUseEnum>() {
		@Override
		public String toCodeString(OperationParameterUseEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(OperationParameterUseEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public OperationParameterUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public OperationParameterUseEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, OperationParameterUseEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	OperationParameterUseEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
