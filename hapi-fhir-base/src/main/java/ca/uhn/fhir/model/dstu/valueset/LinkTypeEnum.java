
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

public enum LinkTypeEnum {

	/**
	 * Code Value: <b>replace</b>
	 *
	 * The patient resource containing this link must no longer be used. The link points forward to another patient resource that must be used in lieu of the patient resource that contains the link.
	 */
	REPLACE("replace", "http://hl7.org/fhir/link-type"),
	
	/**
	 * Code Value: <b>refer</b>
	 *
	 * The patient resource containing this link is in use and valid but not considered the main source of information about a patient. The link points forward to another patient resource that should be consulted to retrieve additional patient information.
	 */
	REFER("refer", "http://hl7.org/fhir/link-type"),
	
	/**
	 * Code Value: <b>seealso</b>
	 *
	 * The patient resource containing this link is in use and valid, but points to another patient resource that is known to contain data about the same person. Data in this resource might overlap or contradict information found in the other patient resource. This link does not indicate any relative importance of the resources concerned, and both should be regarded as equally valid.
	 */
	SEEALSO("seealso", "http://hl7.org/fhir/link-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/link-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/link-type";

	/**
	 * Name for this Value Set:
	 * LinkType
	 */
	public static final String VALUESET_NAME = "LinkType";

	private static Map<String, LinkTypeEnum> CODE_TO_ENUM = new HashMap<String, LinkTypeEnum>();
	private static Map<String, Map<String, LinkTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, LinkTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (LinkTypeEnum next : LinkTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, LinkTypeEnum>());
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
	public LinkTypeEnum forCode(String theCode) {
		LinkTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<LinkTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<LinkTypeEnum>() {
		@Override
		public String toCodeString(LinkTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(LinkTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public LinkTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public LinkTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, LinkTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	LinkTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
