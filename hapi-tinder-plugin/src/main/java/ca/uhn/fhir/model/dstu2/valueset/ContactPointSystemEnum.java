
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ContactPointSystemEnum {

	/**
	 * Code Value: <b>phone</b>
	 *
	 * The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.
	 */
	PHONE("phone", "http://hl7.org/fhir/contact-point-system"),
	
	/**
	 * Code Value: <b>fax</b>
	 *
	 * The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.
	 */
	FAX("fax", "http://hl7.org/fhir/contact-point-system"),
	
	/**
	 * Code Value: <b>email</b>
	 *
	 * The value is an email address.
	 */
	EMAIL("email", "http://hl7.org/fhir/contact-point-system"),
	
	/**
	 * Code Value: <b>url</b>
	 *
	 * The value is a url. This is intended for various personal contacts including blogs, Twitter, Facebook, etc. Do not use for email addresses.
	 */
	URL("url", "http://hl7.org/fhir/contact-point-system"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contact-point-system
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contact-point-system";

	/**
	 * Name for this Value Set:
	 * ContactPointSystem
	 */
	public static final String VALUESET_NAME = "ContactPointSystem";

	private static Map<String, ContactPointSystemEnum> CODE_TO_ENUM = new HashMap<String, ContactPointSystemEnum>();
	private static Map<String, Map<String, ContactPointSystemEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ContactPointSystemEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ContactPointSystemEnum next : ContactPointSystemEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ContactPointSystemEnum>());
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
	public ContactPointSystemEnum forCode(String theCode) {
		ContactPointSystemEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContactPointSystemEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContactPointSystemEnum>() {
		@Override
		public String toCodeString(ContactPointSystemEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ContactPointSystemEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ContactPointSystemEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ContactPointSystemEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ContactPointSystemEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ContactPointSystemEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
