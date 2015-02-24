
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AddressUseEnum {

	/**
	 * Code Value: <b>home</b>
	 *
	 * A communication address at a home.
	 */
	HOME("home", "http://hl7.org/fhir/address-use"),
	
	/**
	 * Code Value: <b>work</b>
	 *
	 * An office address. First choice for business related contacts during business hours.
	 */
	WORK("work", "http://hl7.org/fhir/address-use"),
	
	/**
	 * Code Value: <b>temp</b>
	 *
	 * A temporary address. The period can provide more detailed information.
	 */
	TEMP("temp", "http://hl7.org/fhir/address-use"),
	
	/**
	 * Code Value: <b>old</b>
	 *
	 * This address is no longer in use (or was never correct, but retained for records).
	 */
	OLD("old", "http://hl7.org/fhir/address-use"),
	
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
	private static Map<String, Map<String, AddressUseEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AddressUseEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AddressUseEnum next : AddressUseEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AddressUseEnum>());
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
		public String toSystemString(AddressUseEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AddressUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AddressUseEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AddressUseEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AddressUseEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
