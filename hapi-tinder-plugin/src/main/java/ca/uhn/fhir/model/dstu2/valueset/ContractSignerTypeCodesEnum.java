
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ContractSignerTypeCodesEnum {

	/**
	 * Display: <b>AuthorID</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.1</b>
	 *
	 * Author
	 */
	AUTHORID("1.2.840.10065.1.12.1.1", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Co-AuthorID</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.2</b>
	 *
	 * Co-Author
	 */
	CO_AUTHORID("1.2.840.10065.1.12.1.2", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Co-Participated</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.3</b>
	 *
	 * Co-Participated
	 */
	CO_PARTICIPATED("1.2.840.10065.1.12.1.3", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Transcriptionist</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.4</b>
	 *
	 * Transcriptionist
	 */
	TRANSCRIPTIONIST("1.2.840.10065.1.12.1.4", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Verification</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.5</b>
	 *
	 * Verification
	 */
	VERIFICATION("1.2.840.10065.1.12.1.5", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Validation</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.6</b>
	 *
	 * Validation
	 */
	VALIDATION("1.2.840.10065.1.12.1.6", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Consent</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.7</b>
	 *
	 * Consent
	 */
	CONSENT("1.2.840.10065.1.12.1.7", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Witness</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.8</b>
	 *
	 * Witness
	 */
	WITNESS("1.2.840.10065.1.12.1.8", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Event-Witness</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.9</b>
	 *
	 * Event Witness
	 */
	EVENT_WITNESS("1.2.840.10065.1.12.1.9", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Identity-Witness</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.10</b>
	 *
	 * Identity Witness
	 */
	IDENTITY_WITNESS("1.2.840.10065.1.12.1.10", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Consent-Witness</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.11</b>
	 *
	 * Consent Witness
	 */
	CONSENT_WITNESS("1.2.840.10065.1.12.1.11", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Interpreter</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.12</b>
	 *
	 * Interpreter
	 */
	INTERPRETER("1.2.840.10065.1.12.1.12", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Review</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.13</b>
	 *
	 * Review
	 */
	REVIEW("1.2.840.10065.1.12.1.13", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Source</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.14</b>
	 *
	 * Source
	 */
	SOURCE("1.2.840.10065.1.12.1.14", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Addendum</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.15</b>
	 *
	 * Addendum
	 */
	ADDENDUM("1.2.840.10065.1.12.1.15", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Administrative</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.16</b>
	 *
	 * Administrative
	 */
	ADMINISTRATIVE("1.2.840.10065.1.12.1.16", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	/**
	 * Display: <b>Timestamp</b><br>
	 * Code Value: <b>1.2.840.10065.1.12.1.17</b>
	 *
	 * Timestamp
	 */
	TIMESTAMP("1.2.840.10065.1.12.1.17", "http://www.hl7.org/fhir/contractsignertypecodes"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contract-signer-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contract-signer-type";

	/**
	 * Name for this Value Set:
	 * Contract Signer Type Codes
	 */
	public static final String VALUESET_NAME = "Contract Signer Type Codes";

	private static Map<String, ContractSignerTypeCodesEnum> CODE_TO_ENUM = new HashMap<String, ContractSignerTypeCodesEnum>();
	private static Map<String, Map<String, ContractSignerTypeCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ContractSignerTypeCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ContractSignerTypeCodesEnum next : ContractSignerTypeCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ContractSignerTypeCodesEnum>());
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
	public ContractSignerTypeCodesEnum forCode(String theCode) {
		ContractSignerTypeCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContractSignerTypeCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContractSignerTypeCodesEnum>() {
		@Override
		public String toCodeString(ContractSignerTypeCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ContractSignerTypeCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ContractSignerTypeCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ContractSignerTypeCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ContractSignerTypeCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ContractSignerTypeCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
