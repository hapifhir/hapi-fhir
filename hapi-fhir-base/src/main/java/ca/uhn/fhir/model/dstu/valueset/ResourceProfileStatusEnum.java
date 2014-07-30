
package ca.uhn.fhir.model.dstu.valueset;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum ResourceProfileStatusEnum {

	/**
	 * Code Value: <b>draft</b>
	 *
	 * This profile is still under development.
	 */
	DRAFT("draft", "http://hl7.org/fhir/resource-profile-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * This profile is ready for normal use.
	 */
	ACTIVE("active", "http://hl7.org/fhir/resource-profile-status"),
	
	/**
	 * Code Value: <b>retired</b>
	 *
	 * This profile has been deprecated, withdrawn or superseded and should no longer be used.
	 */
	RETIRED("retired", "http://hl7.org/fhir/resource-profile-status"),
	
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
	private static Map<String, Map<String, ResourceProfileStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ResourceProfileStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ResourceProfileStatusEnum next : ResourceProfileStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ResourceProfileStatusEnum>());
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
		public String toSystemString(ResourceProfileStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ResourceProfileStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ResourceProfileStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ResourceProfileStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ResourceProfileStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
