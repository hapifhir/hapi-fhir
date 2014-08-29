
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

public enum BindingConformanceEnum {

	/**
	 * Code Value: <b>required</b>
	 *
	 * Only codes in the specified set are allowed.  If the binding is extensible, other codes may be used for concepts not covered by the bound set of codes.
	 */
	REQUIRED("required", "http://hl7.org/fhir/binding-conformance"),
	
	/**
	 * Code Value: <b>preferred</b>
	 *
	 * For greater interoperability, implementers are strongly encouraged to use the bound set of codes, however alternate codes may be used in derived profiles and implementations if necessary without being considered non-conformant.
	 */
	PREFERRED("preferred", "http://hl7.org/fhir/binding-conformance"),
	
	/**
	 * Code Value: <b>example</b>
	 *
	 * The codes in the set are an example to illustrate the meaning of the field. There is no particular preference for its use nor any assertion that the provided values are sufficient to meet implementation needs.
	 */
	EXAMPLE("example", "http://hl7.org/fhir/binding-conformance"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/binding-conformance
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/binding-conformance";

	/**
	 * Name for this Value Set:
	 * BindingConformance
	 */
	public static final String VALUESET_NAME = "BindingConformance";

	private static Map<String, BindingConformanceEnum> CODE_TO_ENUM = new HashMap<String, BindingConformanceEnum>();
	private static Map<String, Map<String, BindingConformanceEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, BindingConformanceEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (BindingConformanceEnum next : BindingConformanceEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, BindingConformanceEnum>());
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
	public BindingConformanceEnum forCode(String theCode) {
		BindingConformanceEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<BindingConformanceEnum> VALUESET_BINDER = new IValueSetEnumBinder<BindingConformanceEnum>() {
		@Override
		public String toCodeString(BindingConformanceEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(BindingConformanceEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public BindingConformanceEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public BindingConformanceEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, BindingConformanceEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	BindingConformanceEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
