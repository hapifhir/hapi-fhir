
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

public enum ObservationRelationshipTypeEnum {

	/**
	 * Code Value: <b>has-component</b>
	 *
	 * The target observation is a component of this observation (e.g. Systolic and Diastolic Blood Pressure).
	 */
	HAS_COMPONENT("has-component", "http://hl7.org/fhir/observation-relationshiptypes"),
	
	/**
	 * Code Value: <b>has-member</b>
	 *
	 * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.
	 */
	HAS_MEMBER("has-member", "http://hl7.org/fhir/observation-relationshiptypes"),
	
	/**
	 * Code Value: <b>derived-from</b>
	 *
	 * The target observation is part of the information from which this observation value is derived (e.g. calculated anion gap, Apgar score).
	 */
	DERIVED_FROM("derived-from", "http://hl7.org/fhir/observation-relationshiptypes"),
	
	/**
	 * Code Value: <b>sequel-to</b>
	 *
	 * This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).
	 */
	SEQUEL_TO("sequel-to", "http://hl7.org/fhir/observation-relationshiptypes"),
	
	/**
	 * Code Value: <b>replaces</b>
	 *
	 * This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.
	 */
	REPLACES("replaces", "http://hl7.org/fhir/observation-relationshiptypes"),
	
	/**
	 * Code Value: <b>qualified-by</b>
	 *
	 * The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipaemia measure target from a plasma measure).
	 */
	QUALIFIED_BY("qualified-by", "http://hl7.org/fhir/observation-relationshiptypes"),
	
	/**
	 * Code Value: <b>interfered-by</b>
	 *
	 * The value of the target observation interferes (degardes quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure which has no value).
	 */
	INTERFERED_BY("interfered-by", "http://hl7.org/fhir/observation-relationshiptypes"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/observation-relationshiptypes
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/observation-relationshiptypes";

	/**
	 * Name for this Value Set:
	 * ObservationRelationshipType
	 */
	public static final String VALUESET_NAME = "ObservationRelationshipType";

	private static Map<String, ObservationRelationshipTypeEnum> CODE_TO_ENUM = new HashMap<String, ObservationRelationshipTypeEnum>();
	private static Map<String, Map<String, ObservationRelationshipTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ObservationRelationshipTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ObservationRelationshipTypeEnum next : ObservationRelationshipTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ObservationRelationshipTypeEnum>());
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
	public ObservationRelationshipTypeEnum forCode(String theCode) {
		ObservationRelationshipTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ObservationRelationshipTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<ObservationRelationshipTypeEnum>() {
		@Override
		public String toCodeString(ObservationRelationshipTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ObservationRelationshipTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ObservationRelationshipTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ObservationRelationshipTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ObservationRelationshipTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ObservationRelationshipTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
