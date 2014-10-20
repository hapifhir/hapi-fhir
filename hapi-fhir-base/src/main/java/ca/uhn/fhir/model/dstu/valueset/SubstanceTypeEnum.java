
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SubstanceTypeEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/substance-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/substance-type";

	/**
	 * Name for this Value Set:
	 * Substance Type
	 */
	public static final String VALUESET_NAME = "Substance Type";

	private static Map<String, SubstanceTypeEnum> CODE_TO_ENUM = new HashMap<String, SubstanceTypeEnum>();
	private static Map<String, Map<String, SubstanceTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SubstanceTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SubstanceTypeEnum next : SubstanceTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SubstanceTypeEnum>());
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
	public SubstanceTypeEnum forCode(String theCode) {
		SubstanceTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SubstanceTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SubstanceTypeEnum>() {
		@Override
		public String toCodeString(SubstanceTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SubstanceTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SubstanceTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SubstanceTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SubstanceTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SubstanceTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
