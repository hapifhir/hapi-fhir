
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

public enum SupplyStatusEnum {

	/**
	 * Display: <b>Requested</b><br/>
	 * Code Value: <b>requested</b>
	 *
	 * Supply has been requested, but not dispensed.
	 */
	REQUESTED("requested", "http://hl7.org/fhir/valueset-supply-status"),
	
	/**
	 * Display: <b>Dispensed</b><br/>
	 * Code Value: <b>dispensed</b>
	 *
	 * Supply is part of a pharmacy order and has been dispensed.
	 */
	DISPENSED("dispensed", "http://hl7.org/fhir/valueset-supply-status"),
	
	/**
	 * Display: <b>Received</b><br/>
	 * Code Value: <b>received</b>
	 *
	 * Supply has been received by the requestor.
	 */
	RECEIVED("received", "http://hl7.org/fhir/valueset-supply-status"),
	
	/**
	 * Display: <b>Failed</b><br/>
	 * Code Value: <b>failed</b>
	 *
	 * The supply will not be completed because the supplier was unable or unwilling to supply the item.
	 */
	FAILED("failed", "http://hl7.org/fhir/valueset-supply-status"),
	
	/**
	 * Display: <b>Cancelled</b><br/>
	 * Code Value: <b>cancelled</b>
	 *
	 * The orderer of the supply cancelled the request.
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/valueset-supply-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/valueset-supply-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/valueset-supply-status";

	/**
	 * Name for this Value Set:
	 * SupplyStatus
	 */
	public static final String VALUESET_NAME = "SupplyStatus";

	private static Map<String, SupplyStatusEnum> CODE_TO_ENUM = new HashMap<String, SupplyStatusEnum>();
	private static Map<String, Map<String, SupplyStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SupplyStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SupplyStatusEnum next : SupplyStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SupplyStatusEnum>());
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
	public SupplyStatusEnum forCode(String theCode) {
		SupplyStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SupplyStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<SupplyStatusEnum>() {
		@Override
		public String toCodeString(SupplyStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SupplyStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SupplyStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SupplyStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SupplyStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SupplyStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
