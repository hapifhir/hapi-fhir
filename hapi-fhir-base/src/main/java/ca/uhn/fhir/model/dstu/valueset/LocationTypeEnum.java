
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum LocationTypeEnum {

	/**
	 * Display: <b>Building</b><br/>
	 * Code Value: <b>bu</b>
	 */
	BUILDING("bu"),
	
	/**
	 * Display: <b>Wing</b><br/>
	 * Code Value: <b>wi</b>
	 */
	WING("wi"),
	
	/**
	 * Display: <b>Corridor</b><br/>
	 * Code Value: <b>co</b>
	 */
	CORRIDOR("co"),
	
	/**
	 * Display: <b>Room</b><br/>
	 * Code Value: <b>ro</b>
	 */
	ROOM("ro"),
	
	/**
	 * Display: <b>Vehicle</b><br/>
	 * Code Value: <b>ve</b>
	 */
	VEHICLE("ve"),
	
	/**
	 * Display: <b>House</b><br/>
	 * Code Value: <b>ho</b>
	 */
	HOUSE("ho"),
	
	/**
	 * Display: <b>Cabinet</b><br/>
	 * Code Value: <b>ca</b>
	 */
	CABINET("ca"),
	
	/**
	 * Display: <b>Road</b><br/>
	 * Code Value: <b>rd</b>
	 */
	ROAD("rd"),
	
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
