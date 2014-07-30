
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

public enum ContactUseEnum {

	/**
	 * Code Value: <b>home</b>
	 *
	 * A communication contact at a home; attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.
	 */
	HOME("home", "http://hl7.org/fhir/contact-use"),
	
	/**
	 * Code Value: <b>work</b>
	 *
	 * An office contact. First choice for business related contacts during business hours.
	 */
	WORK("work", "http://hl7.org/fhir/contact-use"),
	
	/**
	 * Code Value: <b>temp</b>
	 *
	 * A temporary contact. The period can provide more detailed information.
	 */
	TEMP("temp", "http://hl7.org/fhir/contact-use"),
	
	/**
	 * Code Value: <b>old</b>
	 *
	 * This contact is no longer in use (or was never correct, but retained for records).
	 */
	OLD("old", "http://hl7.org/fhir/contact-use"),
	
	/**
	 * Code Value: <b>mobile</b>
	 *
	 * A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business.
	 */
	MOBILE("mobile", "http://hl7.org/fhir/contact-use"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contact-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contact-use";

	/**
	 * Name for this Value Set:
	 * ContactUse
	 */
	public static final String VALUESET_NAME = "ContactUse";

	private static Map<String, ContactUseEnum> CODE_TO_ENUM = new HashMap<String, ContactUseEnum>();
	private static Map<String, Map<String, ContactUseEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ContactUseEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ContactUseEnum next : ContactUseEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ContactUseEnum>());
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
	public ContactUseEnum forCode(String theCode) {
		ContactUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContactUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContactUseEnum>() {
		@Override
		public String toCodeString(ContactUseEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ContactUseEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ContactUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ContactUseEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ContactUseEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ContactUseEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
