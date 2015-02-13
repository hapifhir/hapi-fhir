
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum OperationKindEnum {

	/**
	 * Code Value: <b>operation</b>
	 *
	 * This operation is invoked as an operation.
	 */
	OPERATION("operation", "http://hl7.org/fhir/operation-kind"),
	
	/**
	 * Code Value: <b>query</b>
	 *
	 * This operation is a named query, invoked using the search mechanism.
	 */
	QUERY("query", "http://hl7.org/fhir/operation-kind"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/operation-kind
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/operation-kind";

	/**
	 * Name for this Value Set:
	 * OperationKind
	 */
	public static final String VALUESET_NAME = "OperationKind";

	private static Map<String, OperationKindEnum> CODE_TO_ENUM = new HashMap<String, OperationKindEnum>();
	private static Map<String, Map<String, OperationKindEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, OperationKindEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (OperationKindEnum next : OperationKindEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, OperationKindEnum>());
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
	public OperationKindEnum forCode(String theCode) {
		OperationKindEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<OperationKindEnum> VALUESET_BINDER = new IValueSetEnumBinder<OperationKindEnum>() {
		@Override
		public String toCodeString(OperationKindEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(OperationKindEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public OperationKindEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public OperationKindEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, OperationKindEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	OperationKindEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
