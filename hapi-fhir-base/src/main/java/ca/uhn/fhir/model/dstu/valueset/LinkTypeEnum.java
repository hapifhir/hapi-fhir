
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum LinkTypeEnum {

	/**
	 * replace
	 * 
	 *
	 * The patient resource containing this link must no longer be used. The link points forward to another patient resource that must be used in lieu of the patient resource that contains the link.
	 */
	REPLACE("replace"),
	
	/**
	 * refer
	 * 
	 *
	 * The patient resource containing this link is in use and valid but not considered the main source of information about a patient. The link points forward to another patient resource that should be consulted to retrieve additional patient information.
	 */
	REFER("refer"),
	
	/**
	 * seealso
	 * 
	 *
	 * The patient resource containing this link is in use and valid, but points to another patient resource that is known to contain data about the same person. Data in this resource might overlap or contradict information found in the other patient resource. This link does not indicate any relative importance of the resources concerned, and both should be regarded as equally valid.
	 */
	SEEALSO("seealso"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/link-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/link-type";

	/**
	 * Name for this Value Set:
	 * LinkType
	 */
	public static final String VALUESET_NAME = "LinkType";

	private static Map<String, LinkTypeEnum> CODE_TO_ENUM = new HashMap<String, LinkTypeEnum>();
	private String myCode;
	
	static {
		for (LinkTypeEnum next : LinkTypeEnum.values()) {
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
	public LinkTypeEnum forCode(String theCode) {
		LinkTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<LinkTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<LinkTypeEnum>() {
		@Override
		public String toCodeString(LinkTypeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public LinkTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	LinkTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
