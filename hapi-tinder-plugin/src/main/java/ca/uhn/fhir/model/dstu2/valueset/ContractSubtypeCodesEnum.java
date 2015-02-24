
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ContractSubtypeCodesEnum {

	/**
	 * Code Value: <b>disclosure-CA</b>
	 */
	DISCLOSURE_CA("disclosure-CA", "http://www.hl7.org/fhir/contractsubtypecodes"),
	
	/**
	 * Code Value: <b>disclosure-US</b>
	 */
	DISCLOSURE_US("disclosure-US", "http://www.hl7.org/fhir/contractsubtypecodes"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contract-subtype
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contract-subtype";

	/**
	 * Name for this Value Set:
	 * Contract Subtype Codes
	 */
	public static final String VALUESET_NAME = "Contract Subtype Codes";

	private static Map<String, ContractSubtypeCodesEnum> CODE_TO_ENUM = new HashMap<String, ContractSubtypeCodesEnum>();
	private static Map<String, Map<String, ContractSubtypeCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ContractSubtypeCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ContractSubtypeCodesEnum next : ContractSubtypeCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ContractSubtypeCodesEnum>());
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
	public ContractSubtypeCodesEnum forCode(String theCode) {
		ContractSubtypeCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContractSubtypeCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContractSubtypeCodesEnum>() {
		@Override
		public String toCodeString(ContractSubtypeCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ContractSubtypeCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ContractSubtypeCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ContractSubtypeCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ContractSubtypeCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ContractSubtypeCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
