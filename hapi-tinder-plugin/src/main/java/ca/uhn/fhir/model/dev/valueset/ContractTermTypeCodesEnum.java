
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ContractTermTypeCodesEnum {

	/**
	 * Code Value: <b>OralHealth-Basic</b>
	 */
	ORALHEALTH_BASIC("OralHealth-Basic", "http://www.hl7.org/fhir/contracttermsubtypecodes"),
	
	/**
	 * Code Value: <b>OralHealth-Major</b>
	 */
	ORALHEALTH_MAJOR("OralHealth-Major", "http://www.hl7.org/fhir/contracttermsubtypecodes"),
	
	/**
	 * Code Value: <b>OralHealth-Orthodontic</b>
	 */
	ORALHEALTH_ORTHODONTIC("OralHealth-Orthodontic", "http://www.hl7.org/fhir/contracttermsubtypecodes"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contract-term-subtype
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contract-term-subtype";

	/**
	 * Name for this Value Set:
	 * Contract Term Type Codes
	 */
	public static final String VALUESET_NAME = "Contract Term Type Codes";

	private static Map<String, ContractTermTypeCodesEnum> CODE_TO_ENUM = new HashMap<String, ContractTermTypeCodesEnum>();
	private static Map<String, Map<String, ContractTermTypeCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ContractTermTypeCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ContractTermTypeCodesEnum next : ContractTermTypeCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ContractTermTypeCodesEnum>());
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
	public ContractTermTypeCodesEnum forCode(String theCode) {
		ContractTermTypeCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContractTermTypeCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContractTermTypeCodesEnum>() {
		@Override
		public String toCodeString(ContractTermTypeCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ContractTermTypeCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ContractTermTypeCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ContractTermTypeCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ContractTermTypeCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ContractTermTypeCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
