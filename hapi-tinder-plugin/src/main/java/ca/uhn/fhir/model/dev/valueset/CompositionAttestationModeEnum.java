
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum CompositionAttestationModeEnum {

	/**
	 * Code Value: <b>personal</b>
	 *
	 * The person authenticated the content in their personal capacity.
	 */
	PERSONAL("personal", "http://hl7.org/fhir/composition-attestation-mode"),
	
	/**
	 * Code Value: <b>professional</b>
	 *
	 * The person authenticated the content in their professional capacity.
	 */
	PROFESSIONAL("professional", "http://hl7.org/fhir/composition-attestation-mode"),
	
	/**
	 * Code Value: <b>legal</b>
	 *
	 * The person authenticated the content and accepted legal responsibility for its content.
	 */
	LEGAL("legal", "http://hl7.org/fhir/composition-attestation-mode"),
	
	/**
	 * Code Value: <b>official</b>
	 *
	 * The organization authenticated the content as consistent with their policies and procedures.
	 */
	OFFICIAL("official", "http://hl7.org/fhir/composition-attestation-mode"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/composition-attestation-mode
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/composition-attestation-mode";

	/**
	 * Name for this Value Set:
	 * CompositionAttestationMode
	 */
	public static final String VALUESET_NAME = "CompositionAttestationMode";

	private static Map<String, CompositionAttestationModeEnum> CODE_TO_ENUM = new HashMap<String, CompositionAttestationModeEnum>();
	private static Map<String, Map<String, CompositionAttestationModeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CompositionAttestationModeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CompositionAttestationModeEnum next : CompositionAttestationModeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CompositionAttestationModeEnum>());
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
	public CompositionAttestationModeEnum forCode(String theCode) {
		CompositionAttestationModeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CompositionAttestationModeEnum> VALUESET_BINDER = new IValueSetEnumBinder<CompositionAttestationModeEnum>() {
		@Override
		public String toCodeString(CompositionAttestationModeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CompositionAttestationModeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CompositionAttestationModeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CompositionAttestationModeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CompositionAttestationModeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CompositionAttestationModeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
