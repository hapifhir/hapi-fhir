
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

public enum CausalityExpectationEnum {

	/**
	 * Code Value: <b>likely</b>
	 *
	 * Likely that this specific exposure caused the reaction.
	 */
	LIKELY("likely", "http://hl7.org/fhir/causalityExpectation"),
	
	/**
	 * Code Value: <b>unlikely</b>
	 *
	 * Unlikely that this specific exposure caused the reaction - the exposure is being linked to for information purposes.
	 */
	UNLIKELY("unlikely", "http://hl7.org/fhir/causalityExpectation"),
	
	/**
	 * Code Value: <b>confirmed</b>
	 *
	 * It has been confirmed that this exposure was one of the causes of the reaction.
	 */
	CONFIRMED("confirmed", "http://hl7.org/fhir/causalityExpectation"),
	
	/**
	 * Code Value: <b>unknown</b>
	 *
	 * It is unknown whether this exposure had anything to do with the reaction.
	 */
	UNKNOWN("unknown", "http://hl7.org/fhir/causalityExpectation"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/causalityExpectation
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/causalityExpectation";

	/**
	 * Name for this Value Set:
	 * CausalityExpectation
	 */
	public static final String VALUESET_NAME = "CausalityExpectation";

	private static Map<String, CausalityExpectationEnum> CODE_TO_ENUM = new HashMap<String, CausalityExpectationEnum>();
	private static Map<String, Map<String, CausalityExpectationEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CausalityExpectationEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CausalityExpectationEnum next : CausalityExpectationEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CausalityExpectationEnum>());
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
	public CausalityExpectationEnum forCode(String theCode) {
		CausalityExpectationEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CausalityExpectationEnum> VALUESET_BINDER = new IValueSetEnumBinder<CausalityExpectationEnum>() {
		@Override
		public String toCodeString(CausalityExpectationEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CausalityExpectationEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CausalityExpectationEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CausalityExpectationEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CausalityExpectationEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CausalityExpectationEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
