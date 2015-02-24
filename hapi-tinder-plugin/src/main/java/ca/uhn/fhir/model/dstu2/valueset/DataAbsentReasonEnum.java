
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum DataAbsentReasonEnum {

	/**
	 * Code Value: <b>unknown</b>
	 *
	 * The value is not known
	 */
	UNKNOWN("unknown", "http://hl7.org/fhir/data-absent-reason"),
	
	/**
	 * Code Value: <b>asked</b>
	 *
	 * The source human does not know the value
	 */
	ASKED("asked", "http://hl7.org/fhir/data-absent-reason"),
	
	/**
	 * Code Value: <b>temp</b>
	 *
	 * There is reason to expect (from the workflow) that the value may become known
	 */
	TEMP("temp", "http://hl7.org/fhir/data-absent-reason"),
	
	/**
	 * Code Value: <b>notasked</b>
	 *
	 * The workflow didn't lead to this value being known
	 */
	NOTASKED("notasked", "http://hl7.org/fhir/data-absent-reason"),
	
	/**
	 * Code Value: <b>masked</b>
	 *
	 * The information is not available due to security, privacy or related reasons
	 */
	MASKED("masked", "http://hl7.org/fhir/data-absent-reason"),
	
	/**
	 * Code Value: <b>unsupported</b>
	 *
	 * The source system wasn't capable of supporting this element
	 */
	UNSUPPORTED("unsupported", "http://hl7.org/fhir/data-absent-reason"),
	
	/**
	 * Code Value: <b>astext</b>
	 *
	 * The content of the data is represented in the resource narrative
	 */
	ASTEXT("astext", "http://hl7.org/fhir/data-absent-reason"),
	
	/**
	 * Code Value: <b>error</b>
	 *
	 * Some system or workflow process error means that the information is not available
	 */
	ERROR("error", "http://hl7.org/fhir/data-absent-reason"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/data-absent-reason
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/data-absent-reason";

	/**
	 * Name for this Value Set:
	 * DataAbsentReason
	 */
	public static final String VALUESET_NAME = "DataAbsentReason";

	private static Map<String, DataAbsentReasonEnum> CODE_TO_ENUM = new HashMap<String, DataAbsentReasonEnum>();
	private static Map<String, Map<String, DataAbsentReasonEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DataAbsentReasonEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DataAbsentReasonEnum next : DataAbsentReasonEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DataAbsentReasonEnum>());
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
	public DataAbsentReasonEnum forCode(String theCode) {
		DataAbsentReasonEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DataAbsentReasonEnum> VALUESET_BINDER = new IValueSetEnumBinder<DataAbsentReasonEnum>() {
		@Override
		public String toCodeString(DataAbsentReasonEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DataAbsentReasonEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DataAbsentReasonEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DataAbsentReasonEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DataAbsentReasonEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DataAbsentReasonEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
