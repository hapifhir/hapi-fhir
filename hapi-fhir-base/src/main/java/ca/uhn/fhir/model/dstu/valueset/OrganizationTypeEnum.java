
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum OrganizationTypeEnum {

	/**
	 * prov
	 * Healthcare Provider
	 *
	 * 
	 */
	PROV("prov"),
	
	/**
	 * dept
	 * Hospital Department
	 *
	 * 
	 */
	DEPT("dept"),
	
	/**
	 * icu
	 * Intensive Care Unit
	 *
	 * 
	 */
	ICU("icu"),
	
	/**
	 * team
	 * Organizational team
	 *
	 * 
	 */
	TEAM("team"),
	
	/**
	 * fed
	 * Federal Government
	 *
	 * 
	 */
	FED("fed"),
	
	/**
	 * ins
	 * Insurance Company
	 *
	 * 
	 */
	INS("ins"),
	
	/**
	 * edu
	 * Educational Institute
	 *
	 * 
	 */
	EDU("edu"),
	
	/**
	 * reli
	 * Religious Institution
	 *
	 * 
	 */
	RELI("reli"),
	
	/**
	 * pharm
	 * Pharmacy
	 *
	 * 
	 */
	PHARM("pharm"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/organization-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/organization-type";

	/**
	 * Name for this Value Set:
	 * OrganizationType
	 */
	public static final String VALUESET_NAME = "OrganizationType";

	private static Map<String, OrganizationTypeEnum> CODE_TO_ENUM = new HashMap<String, OrganizationTypeEnum>();
	private String myCode;
	
	static {
		for (OrganizationTypeEnum next : OrganizationTypeEnum.values()) {
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
	public OrganizationTypeEnum forCode(String theCode) {
		OrganizationTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<OrganizationTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<OrganizationTypeEnum>() {
		@Override
		public String toCodeString(OrganizationTypeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public OrganizationTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	OrganizationTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
