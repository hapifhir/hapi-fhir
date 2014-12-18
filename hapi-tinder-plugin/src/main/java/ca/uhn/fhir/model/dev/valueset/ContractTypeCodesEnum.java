
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ContractTypeCodesEnum {

	/**
	 * Code Value: <b>privacy</b>
	 */
	PRIVACY("privacy", "http://www.hl7.org/fhir/contracttypecodes"),
	
	/**
	 * Code Value: <b>disclosure</b>
	 */
	DISCLOSURE("disclosure", "http://www.hl7.org/fhir/contracttypecodes"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contract-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contract-type";

	/**
	 * Name for this Value Set:
	 * Contract Type Codes
	 */
	public static final String VALUESET_NAME = "Contract Type Codes";

	private static Map<String, ContractTypeCodesEnum> CODE_TO_ENUM = new HashMap<String, ContractTypeCodesEnum>();
	private static Map<String, Map<String, ContractTypeCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ContractTypeCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ContractTypeCodesEnum next : ContractTypeCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ContractTypeCodesEnum>());
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
	public ContractTypeCodesEnum forCode(String theCode) {
		ContractTypeCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContractTypeCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContractTypeCodesEnum>() {
		@Override
		public String toCodeString(ContractTypeCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ContractTypeCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ContractTypeCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ContractTypeCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ContractTypeCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ContractTypeCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
