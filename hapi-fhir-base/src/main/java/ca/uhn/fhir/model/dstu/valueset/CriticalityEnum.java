
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum CriticalityEnum {

	/**
	 * Code Value: <b>fatal</b>
	 *
	 * Likely to result in death if re-exposed.
	 */
	FATAL("fatal", "http://hl7.org/fhir/criticality"),
	
	/**
	 * Code Value: <b>high</b>
	 *
	 * Likely to result in reactions that will need to be treated if re-exposed.
	 */
	HIGH("high", "http://hl7.org/fhir/criticality"),
	
	/**
	 * Code Value: <b>medium</b>
	 *
	 * Likely to result in reactions that will inconvenience the subject.
	 */
	MEDIUM("medium", "http://hl7.org/fhir/criticality"),
	
	/**
	 * Code Value: <b>low</b>
	 *
	 * Not likely to result in any inconveniences for the subject.
	 */
	LOW("low", "http://hl7.org/fhir/criticality"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/criticality
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/criticality";

	/**
	 * Name for this Value Set:
	 * Criticality
	 */
	public static final String VALUESET_NAME = "Criticality";

	private static Map<String, CriticalityEnum> CODE_TO_ENUM = new HashMap<String, CriticalityEnum>();
	private static Map<String, Map<String, CriticalityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CriticalityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CriticalityEnum next : CriticalityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CriticalityEnum>());
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
	public CriticalityEnum forCode(String theCode) {
		CriticalityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CriticalityEnum> VALUESET_BINDER = new IValueSetEnumBinder<CriticalityEnum>() {
		@Override
		public String toCodeString(CriticalityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CriticalityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CriticalityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CriticalityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CriticalityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CriticalityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
