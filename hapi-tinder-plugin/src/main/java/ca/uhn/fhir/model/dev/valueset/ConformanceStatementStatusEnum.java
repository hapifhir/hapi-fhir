
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ConformanceStatementStatusEnum {

	/**
	 * Code Value: <b>draft</b>
	 *
	 * This conformance statement is still under development.
	 */
	DRAFT("draft", "http://hl7.org/fhir/conformance-statement-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * This conformance statement is ready for use in production systems.
	 */
	ACTIVE("active", "http://hl7.org/fhir/conformance-statement-status"),
	
	/**
	 * Code Value: <b>retired</b>
	 *
	 * This conformance statement has been withdrawn or superceded and should no longer be used.
	 */
	RETIRED("retired", "http://hl7.org/fhir/conformance-statement-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/conformance-statement-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/conformance-statement-status";

	/**
	 * Name for this Value Set:
	 * ConformanceStatementStatus
	 */
	public static final String VALUESET_NAME = "ConformanceStatementStatus";

	private static Map<String, ConformanceStatementStatusEnum> CODE_TO_ENUM = new HashMap<String, ConformanceStatementStatusEnum>();
	private static Map<String, Map<String, ConformanceStatementStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ConformanceStatementStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ConformanceStatementStatusEnum next : ConformanceStatementStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ConformanceStatementStatusEnum>());
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
	public ConformanceStatementStatusEnum forCode(String theCode) {
		ConformanceStatementStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ConformanceStatementStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ConformanceStatementStatusEnum>() {
		@Override
		public String toCodeString(ConformanceStatementStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ConformanceStatementStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ConformanceStatementStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ConformanceStatementStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ConformanceStatementStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ConformanceStatementStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
