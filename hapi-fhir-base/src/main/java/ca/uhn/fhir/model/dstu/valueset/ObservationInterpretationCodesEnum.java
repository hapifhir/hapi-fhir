
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum ObservationInterpretationCodesEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/observation-interpretation
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/observation-interpretation";

	/**
	 * Name for this Value Set:
	 * Observation Interpretation Codes
	 */
	public static final String VALUESET_NAME = "Observation Interpretation Codes";

	private static Map<String, ObservationInterpretationCodesEnum> CODE_TO_ENUM = new HashMap<String, ObservationInterpretationCodesEnum>();
	private static Map<String, Map<String, ObservationInterpretationCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ObservationInterpretationCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ObservationInterpretationCodesEnum next : ObservationInterpretationCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ObservationInterpretationCodesEnum>());
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
	public ObservationInterpretationCodesEnum forCode(String theCode) {
		ObservationInterpretationCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ObservationInterpretationCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ObservationInterpretationCodesEnum>() {
		@Override
		public String toCodeString(ObservationInterpretationCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ObservationInterpretationCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ObservationInterpretationCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ObservationInterpretationCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ObservationInterpretationCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ObservationInterpretationCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
