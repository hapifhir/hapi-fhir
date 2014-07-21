
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

public enum HierarchicalRelationshipTypeEnum {

	/**
	 * Display: <b>Parent</b><br/>
	 * Code Value: <b>parent</b>
	 *
	 * The target resource is the parent of the focal specimen resource.
	 */
	PARENT("parent", "http://hl7.org/fhir/hierarchical-relationship-type"),
	
	/**
	 * Display: <b>Child</b><br/>
	 * Code Value: <b>child</b>
	 *
	 * The target resource is the child of the focal specimen resource.
	 */
	CHILD("child", "http://hl7.org/fhir/hierarchical-relationship-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/hierarchical-relationship-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/hierarchical-relationship-type";

	/**
	 * Name for this Value Set:
	 * HierarchicalRelationshipType
	 */
	public static final String VALUESET_NAME = "HierarchicalRelationshipType";

	private static Map<String, HierarchicalRelationshipTypeEnum> CODE_TO_ENUM = new HashMap<String, HierarchicalRelationshipTypeEnum>();
	private static Map<String, Map<String, HierarchicalRelationshipTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, HierarchicalRelationshipTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (HierarchicalRelationshipTypeEnum next : HierarchicalRelationshipTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, HierarchicalRelationshipTypeEnum>());
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
	public HierarchicalRelationshipTypeEnum forCode(String theCode) {
		HierarchicalRelationshipTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<HierarchicalRelationshipTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<HierarchicalRelationshipTypeEnum>() {
		@Override
		public String toCodeString(HierarchicalRelationshipTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(HierarchicalRelationshipTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public HierarchicalRelationshipTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public HierarchicalRelationshipTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, HierarchicalRelationshipTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	HierarchicalRelationshipTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
