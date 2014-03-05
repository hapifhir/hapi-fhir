
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum LocationModeEnum {

	/**
	 * Code Value: <b>instance</b>
	 *
	 * The Location resource represents a specific instance of a Location.
	 */
	INSTANCE("instance"),
	
	/**
	 * Code Value: <b>kind</b>
	 *
	 * The Location represents a class of Locations.
	 */
	KIND("kind"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/location-mode
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/location-mode";

	/**
	 * Name for this Value Set:
	 * LocationMode
	 */
	public static final String VALUESET_NAME = "LocationMode";

	private static Map<String, LocationModeEnum> CODE_TO_ENUM = new HashMap<String, LocationModeEnum>();
	private String myCode;
	
	static {
		for (LocationModeEnum next : LocationModeEnum.values()) {
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
	public LocationModeEnum forCode(String theCode) {
		LocationModeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<LocationModeEnum> VALUESET_BINDER = new IValueSetEnumBinder<LocationModeEnum>() {
		@Override
		public String toCodeString(LocationModeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public LocationModeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	LocationModeEnum(String theCode) {
		myCode = theCode;
	}

	
}
