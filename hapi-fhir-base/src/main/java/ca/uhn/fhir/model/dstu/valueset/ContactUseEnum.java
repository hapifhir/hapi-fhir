
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ContactUseEnum {

	/**
	 * Code Value: <b>home</b>
	 *
	 * A communication contact at a home; attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.
	 */
	HOME("home"),
	
	/**
	 * Code Value: <b>work</b>
	 *
	 * An office contact. First choice for business related contacts during business hours.
	 */
	WORK("work"),
	
	/**
	 * Code Value: <b>temp</b>
	 *
	 * A temporary contact. The period can provide more detailed information.
	 */
	TEMP("temp"),
	
	/**
	 * Code Value: <b>old</b>
	 *
	 * This contact is no longer in use (or was never correct, but retained for records).
	 */
	OLD("old"),
	
	/**
	 * Code Value: <b>mobile</b>
	 *
	 * A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business.
	 */
	MOBILE("mobile"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contact-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contact-use";

	/**
	 * Name for this Value Set:
	 * ContactUse
	 */
	public static final String VALUESET_NAME = "ContactUse";

	private static Map<String, ContactUseEnum> CODE_TO_ENUM = new HashMap<String, ContactUseEnum>();
	private String myCode;
	
	static {
		for (ContactUseEnum next : ContactUseEnum.values()) {
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
	public ContactUseEnum forCode(String theCode) {
		ContactUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContactUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContactUseEnum>() {
		@Override
		public String toCodeString(ContactUseEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ContactUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ContactUseEnum(String theCode) {
		myCode = theCode;
	}

	
}
