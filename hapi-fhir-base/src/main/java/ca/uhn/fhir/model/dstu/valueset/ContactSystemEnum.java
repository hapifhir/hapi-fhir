
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

public enum ContactSystemEnum {

	/**
	 * Code Value: <b>phone</b>
	 *
	 * The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.
	 */
	PHONE("phone", "http://hl7.org/fhir/contact-system"),
	
	/**
	 * Code Value: <b>fax</b>
	 *
	 * The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.
	 */
	FAX("fax", "http://hl7.org/fhir/contact-system"),
	
	/**
	 * Code Value: <b>email</b>
	 *
	 * The value is an email address.
	 */
	EMAIL("email", "http://hl7.org/fhir/contact-system"),
	
	/**
	 * Code Value: <b>url</b>
	 *
	 * The value is a url. This is intended for various personal contacts including blogs, Twitter, Facebook, etc. Do not use for email addresses.
	 */
	URL("url", "http://hl7.org/fhir/contact-system"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/contact-system
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/contact-system";

	/**
	 * Name for this Value Set:
	 * ContactSystem
	 */
	public static final String VALUESET_NAME = "ContactSystem";

	private static Map<String, ContactSystemEnum> CODE_TO_ENUM = new HashMap<String, ContactSystemEnum>();
	private static Map<String, Map<String, ContactSystemEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ContactSystemEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ContactSystemEnum next : ContactSystemEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ContactSystemEnum>());
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
	public ContactSystemEnum forCode(String theCode) {
		ContactSystemEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ContactSystemEnum> VALUESET_BINDER = new IValueSetEnumBinder<ContactSystemEnum>() {
		@Override
		public String toCodeString(ContactSystemEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ContactSystemEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ContactSystemEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ContactSystemEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ContactSystemEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ContactSystemEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
