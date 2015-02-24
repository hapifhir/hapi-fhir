
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ContactPointUseEnum {

	/**
	 * Code Value: <b>home</b>
	 *
	 * A communication contact point at a home; attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.
	 */
	HOME("home", "http://hl7.org/fhir/contact-point-use"),
	
	/**
	 * Code Value: <b>work</b>
	 *
	 * An office contact point. First choice for business related contacts during business hours.
	 */
	WORK("work", "http://hl7.org/fhir/contact-point-use"),
	
	/**
	 * Code Value: <b>temp</b>
	 *
	 * A temporary contact point. The period can provide more detailed information.
	 */
	TEMP("temp", "http://hl7.org/fhir/contact-point-use"),
	
	/**
	 * Code Value: <b>old</b>
	 *
	 * This contact point is no longer in use (or was never correct, but retained for records).
	 */
	OLD("old", "http://hl7.org/fhir/contact-point-use"),
	
	/**
	 * Code Value: <b>mobile</b>
	 *
	 * A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business.
	 */
	MOBILE("mobile", "http://hl7.org/fhir/contact-point-use"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contact-point-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contact-point-use";

	/**
	 * Name for this Value Set:
	 * ContactPointUse
	 */
	public static final String VALUESET_NAME = "ContactPointUse";

	private static Map<String, ContactPointUseEnum> CODE_TO_ENUM = new HashMap<String, ContactPointUseEnum>();
	private static Map<String, Map<String, ContactPointUseEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ContactPointUseEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ContactPointUseEnum next : ContactPointUseEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ContactPointUseEnum>());
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
	public ContactPointUseEnum forCode(String theCode) {
		ContactPointUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContactPointUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContactPointUseEnum>() {
		@Override
		public String toCodeString(ContactPointUseEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ContactPointUseEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ContactPointUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ContactPointUseEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ContactPointUseEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ContactPointUseEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
