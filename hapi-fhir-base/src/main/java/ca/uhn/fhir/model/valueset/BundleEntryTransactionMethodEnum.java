
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

/**
 * This Enum is only used to support using the DSTU1 Bundle structure (<code>ca.uhn.fhir.model.api.Bundle</code>)
 * on a DSTU2 server. It is preferably to use the new DSTU2 Bundle (<code>ca.uhn.fhir.model.dstu2.resource.Bundle</code>)
 * for this purpose.
 */
@CoverageIgnore
public enum BundleEntryTransactionMethodEnum {

	GET("GET", "http://hl7.org/fhir/http-verb"),
	POST("POST", "http://hl7.org/fhir/http-verb"),
	PUT("PUT", "http://hl7.org/fhir/http-verb"),
	DELETE("DELETE", "http://hl7.org/fhir/http-verb"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/address-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/http-verb";

	/**
	 * Name for this Value Set:
	 * AddressUse
	 */
	public static final String VALUESET_NAME = "BundleEntryStatus";

	private static Map<String, BundleEntryTransactionMethodEnum> CODE_TO_ENUM = new HashMap<String, BundleEntryTransactionMethodEnum>();
	private static Map<String, Map<String, BundleEntryTransactionMethodEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, BundleEntryTransactionMethodEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (BundleEntryTransactionMethodEnum next : BundleEntryTransactionMethodEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, BundleEntryTransactionMethodEnum>());
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
	public BundleEntryTransactionMethodEnum forCode(String theCode) {
		BundleEntryTransactionMethodEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<BundleEntryTransactionMethodEnum> VALUESET_BINDER = new IValueSetEnumBinder<BundleEntryTransactionMethodEnum>() {

		private static final long serialVersionUID = 7569681479045998433L;

		@Override
		public String toCodeString(BundleEntryTransactionMethodEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(BundleEntryTransactionMethodEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public BundleEntryTransactionMethodEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public BundleEntryTransactionMethodEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, BundleEntryTransactionMethodEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	BundleEntryTransactionMethodEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
