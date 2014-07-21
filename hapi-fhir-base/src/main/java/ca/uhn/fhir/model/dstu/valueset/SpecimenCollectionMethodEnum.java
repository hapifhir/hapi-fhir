
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

public enum SpecimenCollectionMethodEnum {

	/**
	 * Code Value: <b>119295008</b>
	 */
	_119295008("119295008", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>413651001</b>
	 */
	_413651001("413651001", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>360020006</b>
	 */
	_360020006("360020006", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>430823004</b>
	 */
	_430823004("430823004", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>16404004</b>
	 */
	_16404004("16404004", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>67889009</b>
	 */
	_67889009("67889009", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>29240004</b>
	 */
	_29240004("29240004", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>45710003</b>
	 */
	_45710003("45710003", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>7800008</b>
	 */
	_7800008("7800008", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>258431006</b>
	 */
	_258431006("258431006", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>20255002</b>
	 */
	_20255002("20255002", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>386147002</b>
	 */
	_386147002("386147002", "http://snomed.info/sct"),
	
	/**
	 * Code Value: <b>278450005</b>
	 */
	_278450005("278450005", "http://snomed.info/sct"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/specimen-collection-method
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/specimen-collection-method";

	/**
	 * Name for this Value Set:
	 * SpecimenCollectionMethod
	 */
	public static final String VALUESET_NAME = "SpecimenCollectionMethod";

	private static Map<String, SpecimenCollectionMethodEnum> CODE_TO_ENUM = new HashMap<String, SpecimenCollectionMethodEnum>();
	private static Map<String, Map<String, SpecimenCollectionMethodEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SpecimenCollectionMethodEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SpecimenCollectionMethodEnum next : SpecimenCollectionMethodEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SpecimenCollectionMethodEnum>());
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
	public SpecimenCollectionMethodEnum forCode(String theCode) {
		SpecimenCollectionMethodEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SpecimenCollectionMethodEnum> VALUESET_BINDER = new IValueSetEnumBinder<SpecimenCollectionMethodEnum>() {
		@Override
		public String toCodeString(SpecimenCollectionMethodEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SpecimenCollectionMethodEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SpecimenCollectionMethodEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SpecimenCollectionMethodEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SpecimenCollectionMethodEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SpecimenCollectionMethodEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
