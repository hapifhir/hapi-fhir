
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

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ConditionRelationshipTypeEnum {

	/**
	 * Code Value: <b>due-to</b>
	 *
	 * this condition follows the identified condition/procedure/substance and is a consequence of it.
	 */
	DUE_TO("due-to", "http://hl7.org/fhir/condition-relationship-type"),
	
	/**
	 * Code Value: <b>following</b>
	 *
	 * this condition follows the identified condition/procedure/substance, but it is not known whether they are causually linked.
	 */
	FOLLOWING("following", "http://hl7.org/fhir/condition-relationship-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/condition-relationship-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/condition-relationship-type";

	/**
	 * Name for this Value Set:
	 * ConditionRelationshipType
	 */
	public static final String VALUESET_NAME = "ConditionRelationshipType";

	private static Map<String, ConditionRelationshipTypeEnum> CODE_TO_ENUM = new HashMap<String, ConditionRelationshipTypeEnum>();
	private static Map<String, Map<String, ConditionRelationshipTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ConditionRelationshipTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ConditionRelationshipTypeEnum next : ConditionRelationshipTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ConditionRelationshipTypeEnum>());
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
	public ConditionRelationshipTypeEnum forCode(String theCode) {
		ConditionRelationshipTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ConditionRelationshipTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<ConditionRelationshipTypeEnum>() {
		@Override
		public String toCodeString(ConditionRelationshipTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ConditionRelationshipTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ConditionRelationshipTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ConditionRelationshipTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ConditionRelationshipTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ConditionRelationshipTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
