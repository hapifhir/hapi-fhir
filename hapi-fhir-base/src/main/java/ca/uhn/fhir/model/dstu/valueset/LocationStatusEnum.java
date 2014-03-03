
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum LocationStatusEnum {

	/**
	 * active
	 * 
	 *
	 * The location is operational.
	 */
	ACTIVE("active"),
	
	/**
	 * suspended
	 * 
	 *
	 * The location is temporarily closed.
	 */
	SUSPENDED("suspended"),
	
	/**
	 * inactive
	 * 
	 *
	 * The location is no longer used.
	 */
	INACTIVE("inactive"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/location-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/location-status";

	/**
	 * Name for this Value Set:
	 * LocationStatus
	 */
	public static final String VALUESET_NAME = "LocationStatus";

	private static Map<String, LocationStatusEnum> CODE_TO_ENUM = new HashMap<String, LocationStatusEnum>();
	private String myCode;
	
	static {
		for (LocationStatusEnum next : LocationStatusEnum.values()) {
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
	public LocationStatusEnum forCode(String theCode) {
		LocationStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<LocationStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<LocationStatusEnum>() {
		@Override
		public String toCodeString(LocationStatusEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public LocationStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	LocationStatusEnum(String theCode) {
		myCode = theCode;
	}

	
}
