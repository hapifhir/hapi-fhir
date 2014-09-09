
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

public enum PropertyRepresentationEnum {

	/**
	 * Code Value: <b>xmlAttr</b>
	 *
	 * In XML, this property is represented as an attribute not an element.
	 */
	XMLATTR("xmlAttr", "http://hl7.org/fhir/property-representation"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/property-representation
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/property-representation";

	/**
	 * Name for this Value Set:
	 * PropertyRepresentation
	 */
	public static final String VALUESET_NAME = "PropertyRepresentation";

	private static Map<String, PropertyRepresentationEnum> CODE_TO_ENUM = new HashMap<String, PropertyRepresentationEnum>();
	private static Map<String, Map<String, PropertyRepresentationEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, PropertyRepresentationEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (PropertyRepresentationEnum next : PropertyRepresentationEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, PropertyRepresentationEnum>());
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
	public PropertyRepresentationEnum forCode(String theCode) {
		PropertyRepresentationEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<PropertyRepresentationEnum> VALUESET_BINDER = new IValueSetEnumBinder<PropertyRepresentationEnum>() {
		@Override
		public String toCodeString(PropertyRepresentationEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(PropertyRepresentationEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public PropertyRepresentationEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public PropertyRepresentationEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, PropertyRepresentationEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	PropertyRepresentationEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
