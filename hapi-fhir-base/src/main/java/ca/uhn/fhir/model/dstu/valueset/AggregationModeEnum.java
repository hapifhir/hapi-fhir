
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

public enum AggregationModeEnum {

	/**
	 * Code Value: <b>contained</b>
	 *
	 * The reference is a local reference to a contained resource.
	 */
	CONTAINED("contained", "http://hl7.org/fhir/resource-aggregation-mode"),
	
	/**
	 * Code Value: <b>referenced</b>
	 *
	 * The reference to to a resource that has to be resolved externally to the resource that includes the reference.
	 */
	REFERENCED("referenced", "http://hl7.org/fhir/resource-aggregation-mode"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/resource-aggregation-mode
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/resource-aggregation-mode";

	/**
	 * Name for this Value Set:
	 * AggregationMode
	 */
	public static final String VALUESET_NAME = "AggregationMode";

	private static Map<String, AggregationModeEnum> CODE_TO_ENUM = new HashMap<String, AggregationModeEnum>();
	private static Map<String, Map<String, AggregationModeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AggregationModeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AggregationModeEnum next : AggregationModeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AggregationModeEnum>());
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
	public AggregationModeEnum forCode(String theCode) {
		AggregationModeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AggregationModeEnum> VALUESET_BINDER = new IValueSetEnumBinder<AggregationModeEnum>() {
		@Override
		public String toCodeString(AggregationModeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AggregationModeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AggregationModeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AggregationModeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AggregationModeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AggregationModeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
