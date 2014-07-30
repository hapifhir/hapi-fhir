
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

public enum ExtensionContextEnum {

	/**
	 * Code Value: <b>resource</b>
	 *
	 * The context is all elements matching a particular resource element path.
	 */
	RESOURCE("resource", "http://hl7.org/fhir/extension-context"),
	
	/**
	 * Code Value: <b>datatype</b>
	 *
	 * The context is all nodes matching a particular data type element path (root or repeating element) or all elements referencing a particular primitive data type (expressed as the datatype name).
	 */
	DATATYPE("datatype", "http://hl7.org/fhir/extension-context"),
	
	/**
	 * Code Value: <b>mapping</b>
	 *
	 * The context is all nodes whose mapping to a specified reference model corresponds to a particular mapping structure.  The context identifies the mapping target. The mapping should clearly identify where such an extension could be used.
	 */
	MAPPING("mapping", "http://hl7.org/fhir/extension-context"),
	
	/**
	 * Code Value: <b>extension</b>
	 *
	 * The context is a particular extension from a particular profile.  Expressed as uri#name, where uri identifies the profile and #name identifies the extension code.
	 */
	EXTENSION("extension", "http://hl7.org/fhir/extension-context"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/extension-context
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/extension-context";

	/**
	 * Name for this Value Set:
	 * ExtensionContext
	 */
	public static final String VALUESET_NAME = "ExtensionContext";

	private static Map<String, ExtensionContextEnum> CODE_TO_ENUM = new HashMap<String, ExtensionContextEnum>();
	private static Map<String, Map<String, ExtensionContextEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ExtensionContextEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ExtensionContextEnum next : ExtensionContextEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ExtensionContextEnum>());
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
	public ExtensionContextEnum forCode(String theCode) {
		ExtensionContextEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ExtensionContextEnum> VALUESET_BINDER = new IValueSetEnumBinder<ExtensionContextEnum>() {
		@Override
		public String toCodeString(ExtensionContextEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ExtensionContextEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ExtensionContextEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ExtensionContextEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ExtensionContextEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ExtensionContextEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
