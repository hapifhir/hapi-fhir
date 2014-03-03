
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum LocationTypeEnum {

	/**
	 * bu
	 * Building
	 *
	 * 
	 */
	BU("bu"),
	
	/**
	 * wi
	 * Wing
	 *
	 * 
	 */
	WI("wi"),
	
	/**
	 * co
	 * Corridor
	 *
	 * 
	 */
	CO("co"),
	
	/**
	 * ro
	 * Room
	 *
	 * 
	 */
	RO("ro"),
	
	/**
	 * ve
	 * Vehicle
	 *
	 * 
	 */
	VE("ve"),
	
	/**
	 * ho
	 * House
	 *
	 * 
	 */
	HO("ho"),
	
	/**
	 * ca
	 * Cabinet
	 *
	 * 
	 */
	CA("ca"),
	
	/**
	 * rd
	 * Road
	 *
	 * 
	 */
	RD("rd"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/location-physical-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/location-physical-type";

	/**
	 * Name for this Value Set:
	 * LocationType
	 */
	public static final String VALUESET_NAME = "LocationType";

	private static Map<String, LocationTypeEnum> CODE_TO_ENUM = new HashMap<String, LocationTypeEnum>();
	private String myCode;
	
	static {
		for (LocationTypeEnum next : LocationTypeEnum.values()) {
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
	public LocationTypeEnum forCode(String theCode) {
		LocationTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<LocationTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<LocationTypeEnum>() {
		@Override
		public String toCodeString(LocationTypeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public LocationTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	LocationTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
