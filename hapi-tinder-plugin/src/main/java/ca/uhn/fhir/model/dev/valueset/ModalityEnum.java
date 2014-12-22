
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ModalityEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/modality
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/modality";

	/**
	 * Name for this Value Set:
	 * Modality
	 */
	public static final String VALUESET_NAME = "Modality";

	private static Map<String, ModalityEnum> CODE_TO_ENUM = new HashMap<String, ModalityEnum>();
	private static Map<String, Map<String, ModalityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ModalityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ModalityEnum next : ModalityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ModalityEnum>());
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
	public ModalityEnum forCode(String theCode) {
		ModalityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ModalityEnum> VALUESET_BINDER = new IValueSetEnumBinder<ModalityEnum>() {
		@Override
		public String toCodeString(ModalityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ModalityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ModalityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ModalityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ModalityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ModalityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
