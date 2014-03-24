
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum UnitsOfTimeEnum {

	/**
	 * Code Value: <b>s</b>
	 */
	S("s", "http://unitsofmeasure.org"),
	
	/**
	 * Code Value: <b>min</b>
	 */
	MIN("min", "http://unitsofmeasure.org"),
	
	/**
	 * Code Value: <b>h</b>
	 */
	H("h", "http://unitsofmeasure.org"),
	
	/**
	 * Code Value: <b>d</b>
	 */
	D("d", "http://unitsofmeasure.org"),
	
	/**
	 * Code Value: <b>wk</b>
	 */
	WK("wk", "http://unitsofmeasure.org"),
	
	/**
	 * Code Value: <b>mo</b>
	 */
	MO("mo", "http://unitsofmeasure.org"),
	
	/**
	 * Code Value: <b>a</b>
	 */
	A("a", "http://unitsofmeasure.org"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/units-of-time
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/units-of-time";

	/**
	 * Name for this Value Set:
	 * UnitsOfTime
	 */
	public static final String VALUESET_NAME = "UnitsOfTime";

	private static Map<String, UnitsOfTimeEnum> CODE_TO_ENUM = new HashMap<String, UnitsOfTimeEnum>();
	private static Map<String, Map<String, UnitsOfTimeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, UnitsOfTimeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (UnitsOfTimeEnum next : UnitsOfTimeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, UnitsOfTimeEnum>());
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
	public UnitsOfTimeEnum forCode(String theCode) {
		UnitsOfTimeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<UnitsOfTimeEnum> VALUESET_BINDER = new IValueSetEnumBinder<UnitsOfTimeEnum>() {
		@Override
		public String toCodeString(UnitsOfTimeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(UnitsOfTimeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public UnitsOfTimeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public UnitsOfTimeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, UnitsOfTimeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	UnitsOfTimeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
