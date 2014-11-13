
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

public enum ReactionSeverityEnum {

	/**
	 * Code Value: <b>severe</b>
	 *
	 * Severe complications arose due to the reaction.
	 */
	SEVERE("severe", "http://hl7.org/fhir/reactionSeverity"),
	
	/**
	 * Code Value: <b>serious</b>
	 *
	 * Serious inconvenience to the subject.
	 */
	SERIOUS("serious", "http://hl7.org/fhir/reactionSeverity"),
	
	/**
	 * Code Value: <b>moderate</b>
	 *
	 * Moderate inconvenience to the subject.
	 */
	MODERATE("moderate", "http://hl7.org/fhir/reactionSeverity"),
	
	/**
	 * Code Value: <b>minor</b>
	 *
	 * Minor inconvenience to the subject.
	 */
	MINOR("minor", "http://hl7.org/fhir/reactionSeverity"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reactionSeverity
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reactionSeverity";

	/**
	 * Name for this Value Set:
	 * ReactionSeverity
	 */
	public static final String VALUESET_NAME = "ReactionSeverity";

	private static Map<String, ReactionSeverityEnum> CODE_TO_ENUM = new HashMap<String, ReactionSeverityEnum>();
	private static Map<String, Map<String, ReactionSeverityEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ReactionSeverityEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ReactionSeverityEnum next : ReactionSeverityEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ReactionSeverityEnum>());
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
	public ReactionSeverityEnum forCode(String theCode) {
		ReactionSeverityEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ReactionSeverityEnum> VALUESET_BINDER = new IValueSetEnumBinder<ReactionSeverityEnum>() {
		@Override
		public String toCodeString(ReactionSeverityEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ReactionSeverityEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ReactionSeverityEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ReactionSeverityEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ReactionSeverityEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ReactionSeverityEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
