
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NamespaceTypeEnum {

	/**
	 * Code Value: <b>codesystem</b>
	 *
	 * The namespace is used to define concepts and symbols to represent those concepts.  E.g. UCUM, LOINC, NDC code, local lab codes, etc.
	 */
	CODESYSTEM("codesystem", "http://hl7.org/fhir/namespace-type"),
	
	/**
	 * Code Value: <b>identifier</b>
	 *
	 * The namespace is used to manage identifiers (e.g. license numbers, order numbers, etc.).
	 */
	IDENTIFIER("identifier", "http://hl7.org/fhir/namespace-type"),
	
	/**
	 * Code Value: <b>root</b>
	 *
	 * The namespace is used as the root for other identifiers and namespaces.
	 */
	ROOT("root", "http://hl7.org/fhir/namespace-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/namespace-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/namespace-type";

	/**
	 * Name for this Value Set:
	 * NamespaceType
	 */
	public static final String VALUESET_NAME = "NamespaceType";

	private static Map<String, NamespaceTypeEnum> CODE_TO_ENUM = new HashMap<String, NamespaceTypeEnum>();
	private static Map<String, Map<String, NamespaceTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NamespaceTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NamespaceTypeEnum next : NamespaceTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NamespaceTypeEnum>());
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
	public NamespaceTypeEnum forCode(String theCode) {
		NamespaceTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NamespaceTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<NamespaceTypeEnum>() {
		@Override
		public String toCodeString(NamespaceTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NamespaceTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NamespaceTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NamespaceTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NamespaceTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NamespaceTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
