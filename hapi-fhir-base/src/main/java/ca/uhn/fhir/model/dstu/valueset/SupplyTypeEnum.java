
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

public enum SupplyTypeEnum {

	/**
	 * Display: <b>Central Supply</b><br/>
	 * Code Value: <b>central</b>
	 *
	 * Supply is stored and requested from central supply
	 */
	CENTRAL_SUPPLY("central", "http://hl7.org/fhir/supply-type"),
	
	/**
	 * Display: <b>Non-Stock</b><br/>
	 * Code Value: <b>nonstock</b>
	 *
	 * Supply is not onsite and must be requested from an outside vendor using a non-stock requisition
	 */
	NON_STOCK("nonstock", "http://hl7.org/fhir/supply-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/supply-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/supply-type";

	/**
	 * Name for this Value Set:
	 * Supply Type
	 */
	public static final String VALUESET_NAME = "Supply Type";

	private static Map<String, SupplyTypeEnum> CODE_TO_ENUM = new HashMap<String, SupplyTypeEnum>();
	private static Map<String, Map<String, SupplyTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SupplyTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SupplyTypeEnum next : SupplyTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SupplyTypeEnum>());
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
	public SupplyTypeEnum forCode(String theCode) {
		SupplyTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SupplyTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SupplyTypeEnum>() {
		@Override
		public String toCodeString(SupplyTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SupplyTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SupplyTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SupplyTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SupplyTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SupplyTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
