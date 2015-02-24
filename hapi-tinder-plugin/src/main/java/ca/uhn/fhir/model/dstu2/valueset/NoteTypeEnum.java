
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NoteTypeEnum {

	/**
	 * Code Value: <b>display</b>
	 *
	 * Display the note.
	 */
	DISPLAY("display", "http://hl7.org/fhir/NT-link"),
	
	/**
	 * Code Value: <b>print</b>
	 *
	 * Print the note on the form.
	 */
	PRINT("print", "http://hl7.org/fhir/NT-link"),
	
	/**
	 * Code Value: <b>printoper</b>
	 *
	 * Print the note for the operator.
	 */
	PRINTOPER("printoper", "http://hl7.org/fhir/NT-link"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/NT-link
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/NT-link";

	/**
	 * Name for this Value Set:
	 * NoteType
	 */
	public static final String VALUESET_NAME = "NoteType";

	private static Map<String, NoteTypeEnum> CODE_TO_ENUM = new HashMap<String, NoteTypeEnum>();
	private static Map<String, Map<String, NoteTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NoteTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NoteTypeEnum next : NoteTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NoteTypeEnum>());
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
	public NoteTypeEnum forCode(String theCode) {
		NoteTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NoteTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<NoteTypeEnum>() {
		@Override
		public String toCodeString(NoteTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NoteTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NoteTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NoteTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NoteTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NoteTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
