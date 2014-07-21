
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

public enum AlertStatusEnum {

	/**
	 * Code Value: <b>active</b>
	 *
	 * A current alert that should be displayed to a user. A system may use the category to determine which roles should view the alert.
	 */
	ACTIVE("active", "http://hl7.org/fhir/alert-status"),
	
	/**
	 * Code Value: <b>inactive</b>
	 *
	 * The alert does not need to be displayed any more.
	 */
	INACTIVE("inactive", "http://hl7.org/fhir/alert-status"),
	
	/**
	 * Code Value: <b>entered in error</b>
	 *
	 * The alert was added in error, and should no longer be displayed.
	 */
	ENTERED_IN_ERROR("entered in error", "http://hl7.org/fhir/alert-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/alert-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/alert-status";

	/**
	 * Name for this Value Set:
	 * AlertStatus
	 */
	public static final String VALUESET_NAME = "AlertStatus";

	private static Map<String, AlertStatusEnum> CODE_TO_ENUM = new HashMap<String, AlertStatusEnum>();
	private static Map<String, Map<String, AlertStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AlertStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AlertStatusEnum next : AlertStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AlertStatusEnum>());
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
	public AlertStatusEnum forCode(String theCode) {
		AlertStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AlertStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<AlertStatusEnum>() {
		@Override
		public String toCodeString(AlertStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AlertStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AlertStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AlertStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AlertStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AlertStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
