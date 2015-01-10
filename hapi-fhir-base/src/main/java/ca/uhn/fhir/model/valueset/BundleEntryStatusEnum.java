
package ca.uhn.fhir.model.valueset;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

public enum BundleEntryStatusEnum {

	CREATE("create", "http://hl7.org/fhir/bundle-entry-status"),
	UPDATE("update", "http://hl7.org/fhir/bundle-entry-status"),
	MATCH("match", "http://hl7.org/fhir/bundle-entry-status"),
	INCLUDE("include", "http://hl7.org/fhir/bundle-entry-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/address-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/bundle-entry-status";

	/**
	 * Name for this Value Set:
	 * AddressUse
	 */
	public static final String VALUESET_NAME = "BundleEntryStatus";

	private static Map<String, BundleEntryStatusEnum> CODE_TO_ENUM = new HashMap<String, BundleEntryStatusEnum>();
	private static Map<String, Map<String, BundleEntryStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, BundleEntryStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (BundleEntryStatusEnum next : BundleEntryStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, BundleEntryStatusEnum>());
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
	public BundleEntryStatusEnum forCode(String theCode) {
		BundleEntryStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<BundleEntryStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<BundleEntryStatusEnum>() {
		@Override
		public String toCodeString(BundleEntryStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(BundleEntryStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public BundleEntryStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public BundleEntryStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, BundleEntryStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	BundleEntryStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
