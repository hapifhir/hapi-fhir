
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ResourceDataElementStatusEnum {

	/**
	 * Code Value: <b>draft</b>
	 *
	 * This data element is still under development.
	 */
	DRAFT("draft", "http://hl7.org/fhir/resource-observation-def-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * This data element is ready for normal use.
	 */
	ACTIVE("active", "http://hl7.org/fhir/resource-observation-def-status"),
	
	/**
	 * Code Value: <b>retired</b>
	 *
	 * This data element has been deprecated, withdrawn or superseded and should no longer be used.
	 */
	RETIRED("retired", "http://hl7.org/fhir/resource-observation-def-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/resource-observation-def-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/resource-observation-def-status";

	/**
	 * Name for this Value Set:
	 * ResourceDataElementStatus
	 */
	public static final String VALUESET_NAME = "ResourceDataElementStatus";

	private static Map<String, ResourceDataElementStatusEnum> CODE_TO_ENUM = new HashMap<String, ResourceDataElementStatusEnum>();
	private static Map<String, Map<String, ResourceDataElementStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ResourceDataElementStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ResourceDataElementStatusEnum next : ResourceDataElementStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ResourceDataElementStatusEnum>());
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
	public ResourceDataElementStatusEnum forCode(String theCode) {
		ResourceDataElementStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ResourceDataElementStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ResourceDataElementStatusEnum>() {
		@Override
		public String toCodeString(ResourceDataElementStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ResourceDataElementStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ResourceDataElementStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ResourceDataElementStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ResourceDataElementStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ResourceDataElementStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
