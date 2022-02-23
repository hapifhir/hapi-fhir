
package ca.uhn.fhir.model.valueset;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.util.CoverageIgnore;

@CoverageIgnore
public enum BundleTypeEnum {

	TRANSACTION("transaction", "http://hl7.org/fhir/bundle-type"),
	
	DOCUMENT("document", "http://hl7.org/fhir/bundle-type"),
	
	MESSAGE("message", "http://hl7.org/fhir/bundle-type"),

	BATCH_RESPONSE("batch-response", "http://hl7.org/fhir/bundle-type"),

	TRANSACTION_RESPONSE("transaction-response", "http://hl7.org/fhir/bundle-type"),
	
	HISTORY("history", "http://hl7.org/fhir/bundle-type"),
	
	SEARCHSET("searchset", "http://hl7.org/fhir/bundle-type"),
	
	COLLECTION("collection", "http://hl7.org/fhir/bundle-type"),
	
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/address-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/bundle-type";

	/**
	 * Name for this Value Set:
	 * AddressUse
	 */
	public static final String VALUESET_NAME = "BundleType";

	private static Map<String, BundleTypeEnum> CODE_TO_ENUM = new HashMap<String, BundleTypeEnum>();
	private static Map<String, Map<String, BundleTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, BundleTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (BundleTypeEnum next : BundleTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, BundleTypeEnum>());
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
	public BundleTypeEnum forCode(String theCode) {
		BundleTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<BundleTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<BundleTypeEnum>() {

		private static final long serialVersionUID = -305725916208867517L;

		@Override
		public String toCodeString(BundleTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(BundleTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public BundleTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public BundleTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, BundleTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	BundleTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
