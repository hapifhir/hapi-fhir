
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

public enum ConceptMapEquivalenceEnum {

	/**
	 * Code Value: <b>equal</b>
	 *
	 * The definitions of the concepts are exactly the same (i.e. only grammatical differences) and structural implications of meaning are identifical or irrelevant (i.e. intensionally identical).
	 */
	EQUAL("equal", "http://hl7.org/fhir/concept-equivalence"),
	
	/**
	 * Code Value: <b>equivalent</b>
	 *
	 * The definitions of the concepts mean the same thing (including when structural implications of meaning are considered) (i.e. extensionally identical).
	 */
	EQUIVALENT("equivalent", "http://hl7.org/fhir/concept-equivalence"),
	
	/**
	 * Code Value: <b>wider</b>
	 *
	 * The target mapping is wider in meaning than the source concept.
	 */
	WIDER("wider", "http://hl7.org/fhir/concept-equivalence"),
	
	/**
	 * Code Value: <b>narrower</b>
	 *
	 * The target mapping is narrower in meaning that the source concept. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally.
	 */
	NARROWER("narrower", "http://hl7.org/fhir/concept-equivalence"),
	
	/**
	 * Code Value: <b>inexact</b>
	 *
	 * The target mapping overlaps with the source concept, but both source and target cover additional meaning. The sense in which the mapping is narrower SHALL be described in the comments in this case, and applications should be careful when atempting to use these mappings operationally.
	 */
	INEXACT("inexact", "http://hl7.org/fhir/concept-equivalence"),
	
	/**
	 * Code Value: <b>unmatched</b>
	 *
	 * There is no match for this concept in the destination concept system.
	 */
	UNMATCHED("unmatched", "http://hl7.org/fhir/concept-equivalence"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/concept-equivalence
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/concept-equivalence";

	/**
	 * Name for this Value Set:
	 * ConceptMapEquivalence
	 */
	public static final String VALUESET_NAME = "ConceptMapEquivalence";

	private static Map<String, ConceptMapEquivalenceEnum> CODE_TO_ENUM = new HashMap<String, ConceptMapEquivalenceEnum>();
	private static Map<String, Map<String, ConceptMapEquivalenceEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ConceptMapEquivalenceEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ConceptMapEquivalenceEnum next : ConceptMapEquivalenceEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ConceptMapEquivalenceEnum>());
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
	public ConceptMapEquivalenceEnum forCode(String theCode) {
		ConceptMapEquivalenceEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ConceptMapEquivalenceEnum> VALUESET_BINDER = new IValueSetEnumBinder<ConceptMapEquivalenceEnum>() {
		@Override
		public String toCodeString(ConceptMapEquivalenceEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ConceptMapEquivalenceEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ConceptMapEquivalenceEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ConceptMapEquivalenceEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ConceptMapEquivalenceEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ConceptMapEquivalenceEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
