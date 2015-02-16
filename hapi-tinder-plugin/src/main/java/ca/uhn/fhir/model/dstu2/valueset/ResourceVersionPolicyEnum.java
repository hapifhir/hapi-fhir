
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ResourceVersionPolicyEnum {

	/**
	 * Display: <b>No VersionId Support</b><br>
	 * Code Value: <b>no-version</b>
	 *
	 * VersionId meta-property is not suppoerted (server) or used (client).
	 */
	NO_VERSIONID_SUPPORT("no-version", "http://hl7.org/fhir/versioning-policy"),
	
	/**
	 * Display: <b>Versioned</b><br>
	 * Code Value: <b>versioned</b>
	 *
	 * VersionId meta-property is suppoerted (server) or used (client).
	 */
	VERSIONED("versioned", "http://hl7.org/fhir/versioning-policy"),
	
	/**
	 * Display: <b>VersionId tracked fully</b><br>
	 * Code Value: <b>versioned-update</b>
	 *
	 * VersionId is must be correct for updates (server) or will be specified (If-match header) for updates (client).
	 */
	VERSIONID_TRACKED_FULLY("versioned-update", "http://hl7.org/fhir/versioning-policy"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/versioning-policy
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/versioning-policy";

	/**
	 * Name for this Value Set:
	 * ResourceVersionPolicy
	 */
	public static final String VALUESET_NAME = "ResourceVersionPolicy";

	private static Map<String, ResourceVersionPolicyEnum> CODE_TO_ENUM = new HashMap<String, ResourceVersionPolicyEnum>();
	private static Map<String, Map<String, ResourceVersionPolicyEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ResourceVersionPolicyEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ResourceVersionPolicyEnum next : ResourceVersionPolicyEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ResourceVersionPolicyEnum>());
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
	public ResourceVersionPolicyEnum forCode(String theCode) {
		ResourceVersionPolicyEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ResourceVersionPolicyEnum> VALUESET_BINDER = new IValueSetEnumBinder<ResourceVersionPolicyEnum>() {
		@Override
		public String toCodeString(ResourceVersionPolicyEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ResourceVersionPolicyEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ResourceVersionPolicyEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ResourceVersionPolicyEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ResourceVersionPolicyEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ResourceVersionPolicyEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
