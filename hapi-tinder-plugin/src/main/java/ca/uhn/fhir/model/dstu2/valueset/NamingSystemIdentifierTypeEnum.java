
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NamingSystemIdentifierTypeEnum {

	/**
	 * Code Value: <b>oid</b>
	 *
	 * An ISO object identifier.  E.g. 1.2.3.4.5.
	 */
	OID("oid", "http://hl7.org/fhir/namingsystem-identifier-type"),
	
	/**
	 * Code Value: <b>uuid</b>
	 *
	 * A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.
	 */
	UUID("uuid", "http://hl7.org/fhir/namingsystem-identifier-type"),
	
	/**
	 * Code Value: <b>uri</b>
	 *
	 * A uniform resource identifier (ideally a URL - uniform resource locator).  E.g. http://unitsofmeasure.org.
	 */
	URI("uri", "http://hl7.org/fhir/namingsystem-identifier-type"),
	
	/**
	 * Code Value: <b>other</b>
	 *
	 * Some other type of unique identifier.  E.g HL7-assigned reserved string such as LN for LOINC.
	 */
	OTHER("other", "http://hl7.org/fhir/namingsystem-identifier-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/namingsystem-identifier-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/namingsystem-identifier-type";

	/**
	 * Name for this Value Set:
	 * NamingSystemIdentifierType
	 */
	public static final String VALUESET_NAME = "NamingSystemIdentifierType";

	private static Map<String, NamingSystemIdentifierTypeEnum> CODE_TO_ENUM = new HashMap<String, NamingSystemIdentifierTypeEnum>();
	private static Map<String, Map<String, NamingSystemIdentifierTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NamingSystemIdentifierTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NamingSystemIdentifierTypeEnum next : NamingSystemIdentifierTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NamingSystemIdentifierTypeEnum>());
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
	public NamingSystemIdentifierTypeEnum forCode(String theCode) {
		NamingSystemIdentifierTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NamingSystemIdentifierTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<NamingSystemIdentifierTypeEnum>() {
		@Override
		public String toCodeString(NamingSystemIdentifierTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NamingSystemIdentifierTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NamingSystemIdentifierTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NamingSystemIdentifierTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NamingSystemIdentifierTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NamingSystemIdentifierTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
