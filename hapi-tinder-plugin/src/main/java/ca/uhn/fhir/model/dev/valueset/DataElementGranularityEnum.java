
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum DataElementGranularityEnum {

	/**
	 * Code Value: <b>comparable</b>
	 *
	 * The data element is sufficiently well-constrained that multiple pieces of data captured according to the constraints of the data element will be comparable (though in some cases, a degree of automated conversion/normalization may be required).
	 */
	COMPARABLE("comparable", "http://hl7.org/fhir/dataelement-granularity"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/dataelement-granularity
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/dataelement-granularity";

	/**
	 * Name for this Value Set:
	 * DataElementGranularity
	 */
	public static final String VALUESET_NAME = "DataElementGranularity";

	private static Map<String, DataElementGranularityEnum> CODE_TO_ENUM = new HashMap<String, DataElementGranularityEnum>();
	private static Map<String, Map<String, DataElementGranularityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DataElementGranularityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DataElementGranularityEnum next : DataElementGranularityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DataElementGranularityEnum>());
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
	public DataElementGranularityEnum forCode(String theCode) {
		DataElementGranularityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DataElementGranularityEnum> VALUESET_BINDER = new IValueSetEnumBinder<DataElementGranularityEnum>() {
		@Override
		public String toCodeString(DataElementGranularityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DataElementGranularityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DataElementGranularityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DataElementGranularityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DataElementGranularityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DataElementGranularityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
