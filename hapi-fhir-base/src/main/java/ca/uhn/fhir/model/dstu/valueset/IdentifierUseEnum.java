
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

public enum IdentifierUseEnum {

	/**
	 * Code Value: <b>usual</b>
	 *
	 * the identifier recommended for display and use in real-world interactions.
	 */
	USUAL("usual", "http://hl7.org/fhir/identifier-use"),
	
	/**
	 * Code Value: <b>official</b>
	 *
	 * the identifier considered to be most trusted for the identification of this item.
	 */
	OFFICIAL("official", "http://hl7.org/fhir/identifier-use"),
	
	/**
	 * Code Value: <b>temp</b>
	 *
	 * A temporary identifier.
	 */
	TEMP("temp", "http://hl7.org/fhir/identifier-use"),
	
	/**
	 * Code Value: <b>secondary</b>
	 *
	 * An identifier that was assigned in secondary use - it serves to identify the object in a relative context, but cannot be consistently assigned to the same object again in a different context.
	 */
	SECONDARY("secondary", "http://hl7.org/fhir/identifier-use"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/identifier-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/identifier-use";

	/**
	 * Name for this Value Set:
	 * IdentifierUse
	 */
	public static final String VALUESET_NAME = "IdentifierUse";

	private static Map<String, IdentifierUseEnum> CODE_TO_ENUM = new HashMap<String, IdentifierUseEnum>();
	private static Map<String, Map<String, IdentifierUseEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, IdentifierUseEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (IdentifierUseEnum next : IdentifierUseEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, IdentifierUseEnum>());
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
	public IdentifierUseEnum forCode(String theCode) {
		IdentifierUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<IdentifierUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<IdentifierUseEnum>() {
		@Override
		public String toCodeString(IdentifierUseEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(IdentifierUseEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public IdentifierUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public IdentifierUseEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, IdentifierUseEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	IdentifierUseEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
