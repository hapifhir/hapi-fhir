
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NamespaceIdentifierTypeEnum {

	/**
	 * Code Value: <b>oid</b>
	 *
	 * An ISO object identifier.  E.g. 1.2.3.4.5.
	 */
	OID("oid", "http://hl7.org/fhir/namespace-identifier-type"),
	
	/**
	 * Code Value: <b>uuid</b>
	 *
	 * A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.
	 */
	UUID("uuid", "http://hl7.org/fhir/namespace-identifier-type"),
	
	/**
	 * Code Value: <b>uri</b>
	 *
	 * A uniform resource identifier (ideally a URL - uniform resource locator).  E.g. http://unitsofmeasure.org.
	 */
	URI("uri", "http://hl7.org/fhir/namespace-identifier-type"),
	
	/**
	 * Code Value: <b>other</b>
	 *
	 * Some other type of unique identifier.  E.g HL7-assigned reserved string such as LN for LOINC.
	 */
	OTHER("other", "http://hl7.org/fhir/namespace-identifier-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/namespace-identifier-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/namespace-identifier-type";

	/**
	 * Name for this Value Set:
	 * NamespaceIdentifierType
	 */
	public static final String VALUESET_NAME = "NamespaceIdentifierType";

	private static Map<String, NamespaceIdentifierTypeEnum> CODE_TO_ENUM = new HashMap<String, NamespaceIdentifierTypeEnum>();
	private static Map<String, Map<String, NamespaceIdentifierTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NamespaceIdentifierTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NamespaceIdentifierTypeEnum next : NamespaceIdentifierTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NamespaceIdentifierTypeEnum>());
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
	public NamespaceIdentifierTypeEnum forCode(String theCode) {
		NamespaceIdentifierTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NamespaceIdentifierTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<NamespaceIdentifierTypeEnum>() {
		@Override
		public String toCodeString(NamespaceIdentifierTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NamespaceIdentifierTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NamespaceIdentifierTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NamespaceIdentifierTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NamespaceIdentifierTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NamespaceIdentifierTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
