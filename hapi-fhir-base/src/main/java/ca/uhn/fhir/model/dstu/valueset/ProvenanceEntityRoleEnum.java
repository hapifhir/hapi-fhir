
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

public enum ProvenanceEntityRoleEnum {

	/**
	 * Code Value: <b>derivation</b>
	 *
	 * A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a new entity based on a preexisting entity.
	 */
	DERIVATION("derivation", "http://hl7.org/fhir/provenance-entity-role"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/provenance-entity-role
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/provenance-entity-role";

	/**
	 * Name for this Value Set:
	 * ProvenanceEntityRole
	 */
	public static final String VALUESET_NAME = "ProvenanceEntityRole";

	private static Map<String, ProvenanceEntityRoleEnum> CODE_TO_ENUM = new HashMap<String, ProvenanceEntityRoleEnum>();
	private static Map<String, Map<String, ProvenanceEntityRoleEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ProvenanceEntityRoleEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ProvenanceEntityRoleEnum next : ProvenanceEntityRoleEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ProvenanceEntityRoleEnum>());
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
	public ProvenanceEntityRoleEnum forCode(String theCode) {
		ProvenanceEntityRoleEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ProvenanceEntityRoleEnum> VALUESET_BINDER = new IValueSetEnumBinder<ProvenanceEntityRoleEnum>() {
		@Override
		public String toCodeString(ProvenanceEntityRoleEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ProvenanceEntityRoleEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ProvenanceEntityRoleEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ProvenanceEntityRoleEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ProvenanceEntityRoleEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ProvenanceEntityRoleEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
