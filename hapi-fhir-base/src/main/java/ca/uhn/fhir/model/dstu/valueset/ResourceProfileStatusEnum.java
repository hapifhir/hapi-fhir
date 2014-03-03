
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ResourceProfileStatusEnum {

	/**
	 * draft
	 * 
	 *
	 * This profile is still under development.
	 */
	DRAFT("draft"),
	
	/**
	 * active
	 * 
	 *
	 * This profile is ready for normal use.
	 */
	ACTIVE("active"),
	
	/**
	 * retired
	 * 
	 *
	 * This profile has been deprecated, withdrawn or superseded and should no longer be used.
	 */
	RETIRED("retired"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/resource-profile-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/resource-profile-status";

	/**
	 * Name for this Value Set:
	 * ResourceProfileStatus
	 */
	public static final String VALUESET_NAME = "ResourceProfileStatus";

	private static Map<String, ResourceProfileStatusEnum> CODE_TO_ENUM = new HashMap<String, ResourceProfileStatusEnum>();
	private String myCode;
	
	static {
		for (ResourceProfileStatusEnum next : ResourceProfileStatusEnum.values()) {
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
	public ResourceProfileStatusEnum forCode(String theCode) {
		ResourceProfileStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ResourceProfileStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ResourceProfileStatusEnum>() {
		@Override
		public String toCodeString(ResourceProfileStatusEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ResourceProfileStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ResourceProfileStatusEnum(String theCode) {
		myCode = theCode;
	}

	
}
