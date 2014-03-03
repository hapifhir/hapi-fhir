
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum PractitionerRoleEnum {

	/**
	 * doctor
	 * 
	 *
	 * 
	 */
	DOCTOR("doctor"),
	
	/**
	 * nurse
	 * 
	 *
	 * 
	 */
	NURSE("nurse"),
	
	/**
	 * pharmacist
	 * 
	 *
	 * 
	 */
	PHARMACIST("pharmacist"),
	
	/**
	 * researcher
	 * 
	 *
	 * 
	 */
	RESEARCHER("researcher"),
	
	/**
	 * teacher
	 * Teacher/educator
	 *
	 * 
	 */
	TEACHER("teacher"),
	
	/**
	 * ict
	 * ICT professional
	 *
	 * 
	 */
	ICT("ict"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/practitioner-role
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/practitioner-role";

	/**
	 * Name for this Value Set:
	 * PractitionerRole
	 */
	public static final String VALUESET_NAME = "PractitionerRole";

	private static Map<String, PractitionerRoleEnum> CODE_TO_ENUM = new HashMap<String, PractitionerRoleEnum>();
	private String myCode;
	
	static {
		for (PractitionerRoleEnum next : PractitionerRoleEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}
	
	/**
	 * Returns the enumerated value associated with this code
	 */
	public PractitionerRoleEnum forCode(String theCode) {
		PractitionerRoleEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<PractitionerRoleEnum> VALUESET_BINDER = new IValueSetEnumBinder<PractitionerRoleEnum>() {
		@Override
		public String toCodeString(PractitionerRoleEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public PractitionerRoleEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	PractitionerRoleEnum(String theCode) {
		myCode = theCode;
	}

	
}
