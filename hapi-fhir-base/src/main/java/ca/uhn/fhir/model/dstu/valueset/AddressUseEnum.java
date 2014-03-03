
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AddressUseEnum {

	/**
	 * home
	 * 
	 *
	 * A communication address at a home.
	 */
	HOME("home"),
	
	/**
	 * work
	 * 
	 *
	 * An office address. First choice for business related contacts during business hours.
	 */
	WORK("work"),
	
	/**
	 * temp
	 * 
	 *
	 * A temporary address. The period can provide more detailed information.
	 */
	TEMP("temp"),
	
	/**
	 * old
	 * 
	 *
	 * This address is no longer in use (or was never correct, but retained for records).
	 */
	OLD("old"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/address-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/address-use";

	/**
	 * Name for this Value Set:
	 * AddressUse
	 */
	public static final String VALUESET_NAME = "AddressUse";

	private static Map<String, AddressUseEnum> CODE_TO_ENUM = new HashMap<String, AddressUseEnum>();
	private String myCode;
	
	static {
		for (AddressUseEnum next : AddressUseEnum.values()) {
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
	public AddressUseEnum forCode(String theCode) {
		AddressUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AddressUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<AddressUseEnum>() {
		@Override
		public String toCodeString(AddressUseEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public AddressUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	AddressUseEnum(String theCode) {
		myCode = theCode;
	}

	
}
