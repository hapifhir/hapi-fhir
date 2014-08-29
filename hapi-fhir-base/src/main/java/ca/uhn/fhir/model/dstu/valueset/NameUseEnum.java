
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

public enum NameUseEnum {

	/**
	 * Code Value: <b>usual</b>
	 *
	 * Known as/conventional/the one you normally use.
	 */
	USUAL("usual", "http://hl7.org/fhir/name-use"),
	
	/**
	 * Code Value: <b>official</b>
	 *
	 * The formal name as registered in an official (government) registry, but which name might not be commonly used. May be called "legal name".
	 */
	OFFICIAL("official", "http://hl7.org/fhir/name-use"),
	
	/**
	 * Code Value: <b>temp</b>
	 *
	 * A temporary name. Name.period can provide more detailed information. This may also be used for temporary names assigned at birth or in emergency situations.
	 */
	TEMP("temp", "http://hl7.org/fhir/name-use"),
	
	/**
	 * Code Value: <b>nickname</b>
	 *
	 * A name that is used to address the person in an informal manner, but is not part of their formal or usual name.
	 */
	NICKNAME("nickname", "http://hl7.org/fhir/name-use"),
	
	/**
	 * Code Value: <b>anonymous</b>
	 *
	 * Anonymous assigned name, alias, or pseudonym (used to protect a person's identity for privacy reasons).
	 */
	ANONYMOUS("anonymous", "http://hl7.org/fhir/name-use"),
	
	/**
	 * Code Value: <b>old</b>
	 *
	 * This name is no longer in use (or was never correct, but retained for records).
	 */
	OLD("old", "http://hl7.org/fhir/name-use"),
	
	/**
	 * Code Value: <b>maiden</b>
	 *
	 * A name used prior to marriage. Marriage naming customs vary greatly around the world. This name use is for use by applications that collect and store "maiden" names. Though the concept of maiden name is often gender specific, the use of this term is not gender specific. The use of this term does not imply any particular history for a person's name, nor should the maiden name be determined algorithmically.
	 */
	MAIDEN("maiden", "http://hl7.org/fhir/name-use"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/name-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/name-use";

	/**
	 * Name for this Value Set:
	 * NameUse
	 */
	public static final String VALUESET_NAME = "NameUse";

	private static Map<String, NameUseEnum> CODE_TO_ENUM = new HashMap<String, NameUseEnum>();
	private static Map<String, Map<String, NameUseEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, NameUseEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (NameUseEnum next : NameUseEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, NameUseEnum>());
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
	public NameUseEnum forCode(String theCode) {
		NameUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NameUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<NameUseEnum>() {
		@Override
		public String toCodeString(NameUseEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(NameUseEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public NameUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public NameUseEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, NameUseEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	NameUseEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
