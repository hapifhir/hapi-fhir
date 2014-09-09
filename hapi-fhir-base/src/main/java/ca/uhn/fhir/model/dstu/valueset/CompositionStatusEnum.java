
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

public enum CompositionStatusEnum {

	/**
	 * Code Value: <b>preliminary</b>
	 *
	 * This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or unverified.
	 */
	PRELIMINARY("preliminary", "http://hl7.org/fhir/composition-status"),
	
	/**
	 * Code Value: <b>final</b>
	 *
	 * The composition or document is complete and verified by an appropriate person, and no further work is planned.
	 */
	FINAL("final", "http://hl7.org/fhir/composition-status"),
	
	/**
	 * Code Value: <b>appended</b>
	 *
	 * The composition or document has been modified subsequent to being released as "final", and is complete and verified by an authorized person. The modifications added new information to the composition or document, but did not revise existing content.
	 */
	APPENDED("appended", "http://hl7.org/fhir/composition-status"),
	
	/**
	 * Code Value: <b>amended</b>
	 *
	 * The composition or document has been modified subsequent to being released as "final", and is complete and verified by an authorized person.
	 */
	AMENDED("amended", "http://hl7.org/fhir/composition-status"),
	
	/**
	 * Code Value: <b>entered in error</b>
	 *
	 * The composition or document was originally created/issued in error, and this is an amendment that marks that the entire series should not be considered as valid.
	 */
	ENTERED_IN_ERROR("entered in error", "http://hl7.org/fhir/composition-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/composition-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/composition-status";

	/**
	 * Name for this Value Set:
	 * CompositionStatus
	 */
	public static final String VALUESET_NAME = "CompositionStatus";

	private static Map<String, CompositionStatusEnum> CODE_TO_ENUM = new HashMap<String, CompositionStatusEnum>();
	private static Map<String, Map<String, CompositionStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, CompositionStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (CompositionStatusEnum next : CompositionStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, CompositionStatusEnum>());
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
	public CompositionStatusEnum forCode(String theCode) {
		CompositionStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<CompositionStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<CompositionStatusEnum>() {
		@Override
		public String toCodeString(CompositionStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(CompositionStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public CompositionStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public CompositionStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, CompositionStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	CompositionStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
