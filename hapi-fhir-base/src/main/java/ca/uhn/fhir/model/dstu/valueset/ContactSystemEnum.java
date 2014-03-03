
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ContactSystemEnum {

	/**
	 * phone
	 * 
	 *
	 * The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.
	 */
	PHONE("phone"),
	
	/**
	 * fax
	 * 
	 *
	 * The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.
	 */
	FAX("fax"),
	
	/**
	 * email
	 * 
	 *
	 * The value is an email address.
	 */
	EMAIL("email"),
	
	/**
	 * url
	 * 
	 *
	 * The value is a url. This is intended for various personal contacts including blogs, Twitter, Facebook, etc. Do not use for email addresses.
	 */
	URL("url"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contact-system
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contact-system";

	/**
	 * Name for this Value Set:
	 * ContactSystem
	 */
	public static final String VALUESET_NAME = "ContactSystem";

	private static Map<String, ContactSystemEnum> CODE_TO_ENUM = new HashMap<String, ContactSystemEnum>();
	private String myCode;
	
	static {
		for (ContactSystemEnum next : ContactSystemEnum.values()) {
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
	public ContactSystemEnum forCode(String theCode) {
		ContactSystemEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContactSystemEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContactSystemEnum>() {
		@Override
		public String toCodeString(ContactSystemEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ContactSystemEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ContactSystemEnum(String theCode) {
		myCode = theCode;
	}

	
}
