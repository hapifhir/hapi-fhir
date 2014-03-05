
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum OrganizationTypeEnum {

	/**
	 * Display: <b>Healthcare Provider</b><br/>
	 * Code Value: <b>prov</b>
	 */
	HEALTHCARE_PROVIDER("prov"),
	
	/**
	 * Display: <b>Hospital Department</b><br/>
	 * Code Value: <b>dept</b>
	 */
	HOSPITAL_DEPARTMENT("dept"),
	
	/**
	 * Display: <b>Intensive Care Unit</b><br/>
	 * Code Value: <b>icu</b>
	 */
	INTENSIVE_CARE_UNIT("icu"),
	
	/**
	 * Display: <b>Organizational team</b><br/>
	 * Code Value: <b>team</b>
	 */
	ORGANIZATIONAL_TEAM("team"),
	
	/**
	 * Display: <b>Federal Government</b><br/>
	 * Code Value: <b>fed</b>
	 */
	FEDERAL_GOVERNMENT("fed"),
	
	/**
	 * Display: <b>Insurance Company</b><br/>
	 * Code Value: <b>ins</b>
	 */
	INSURANCE_COMPANY("ins"),
	
	/**
	 * Display: <b>Educational Institute</b><br/>
	 * Code Value: <b>edu</b>
	 */
	EDUCATIONAL_INSTITUTE("edu"),
	
	/**
	 * Display: <b>Religious Institution</b><br/>
	 * Code Value: <b>reli</b>
	 */
	RELIGIOUS_INSTITUTION("reli"),
	
	/**
	 * Display: <b>Pharmacy</b><br/>
	 * Code Value: <b>pharm</b>
	 */
	PHARMACY("pharm"),
	
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
