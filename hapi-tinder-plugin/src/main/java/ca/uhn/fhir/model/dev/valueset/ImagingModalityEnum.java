
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ImagingModalityEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/imaging-modality
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/imaging-modality";

	/**
	 * Name for this Value Set:
	 * ImagingModality
	 */
	public static final String VALUESET_NAME = "ImagingModality";

	private static Map<String, ImagingModalityEnum> CODE_TO_ENUM = new HashMap<String, ImagingModalityEnum>();
	private static Map<String, Map<String, ImagingModalityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ImagingModalityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ImagingModalityEnum next : ImagingModalityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ImagingModalityEnum>());
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
	public ImagingModalityEnum forCode(String theCode) {
		ImagingModalityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ImagingModalityEnum> VALUESET_BINDER = new IValueSetEnumBinder<ImagingModalityEnum>() {
		@Override
		public String toCodeString(ImagingModalityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ImagingModalityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ImagingModalityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ImagingModalityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ImagingModalityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ImagingModalityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
