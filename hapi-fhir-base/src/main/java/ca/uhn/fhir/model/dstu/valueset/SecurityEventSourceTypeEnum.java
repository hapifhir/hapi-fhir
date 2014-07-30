
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

public enum SecurityEventSourceTypeEnum {

	/**
	 * Display: <b>User Device</b><br/>
	 * Code Value: <b>1</b>
	 *
	 * End-user display device, diagnostic device
	 */
	USER_DEVICE("1", "http://hl7.org/fhir/security-source-type"),
	
	/**
	 * Display: <b>Data Interface</b><br/>
	 * Code Value: <b>2</b>
	 *
	 * Data acquisition device or instrument
	 */
	DATA_INTERFACE("2", "http://hl7.org/fhir/security-source-type"),
	
	/**
	 * Display: <b>Web Server</b><br/>
	 * Code Value: <b>3</b>
	 *
	 * Web Server process or thread
	 */
	WEB_SERVER("3", "http://hl7.org/fhir/security-source-type"),
	
	/**
	 * Display: <b>Application Server</b><br/>
	 * Code Value: <b>4</b>
	 *
	 * Application Server process or thread
	 */
	APPLICATION_SERVER("4", "http://hl7.org/fhir/security-source-type"),
	
	/**
	 * Display: <b>Database Server</b><br/>
	 * Code Value: <b>5</b>
	 *
	 * Database Server process or thread
	 */
	DATABASE_SERVER("5", "http://hl7.org/fhir/security-source-type"),
	
	/**
	 * Display: <b>Security Server</b><br/>
	 * Code Value: <b>6</b>
	 *
	 * Security server, e.g., a domain controller
	 */
	SECURITY_SERVER("6", "http://hl7.org/fhir/security-source-type"),
	
	/**
	 * Display: <b>Network Device</b><br/>
	 * Code Value: <b>7</b>
	 *
	 * ISO level 1-3 network component
	 */
	NETWORK_DEVICE("7", "http://hl7.org/fhir/security-source-type"),
	
	/**
	 * Display: <b>Network Router</b><br/>
	 * Code Value: <b>8</b>
	 *
	 * ISO level 4-6 operating software
	 */
	NETWORK_ROUTER("8", "http://hl7.org/fhir/security-source-type"),
	
	/**
	 * Display: <b>Other</b><br/>
	 * Code Value: <b>9</b>
	 *
	 * other kind of device (defined by DICOM, but some other code/system can be used)
	 */
	OTHER("9", "http://hl7.org/fhir/security-source-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/security-source-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/security-source-type";

	/**
	 * Name for this Value Set:
	 * Security Event Source Type
	 */
	public static final String VALUESET_NAME = "Security Event Source Type";

	private static Map<String, SecurityEventSourceTypeEnum> CODE_TO_ENUM = new HashMap<String, SecurityEventSourceTypeEnum>();
	private static Map<String, Map<String, SecurityEventSourceTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SecurityEventSourceTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SecurityEventSourceTypeEnum next : SecurityEventSourceTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SecurityEventSourceTypeEnum>());
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
	public SecurityEventSourceTypeEnum forCode(String theCode) {
		SecurityEventSourceTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SecurityEventSourceTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SecurityEventSourceTypeEnum>() {
		@Override
		public String toCodeString(SecurityEventSourceTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SecurityEventSourceTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SecurityEventSourceTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SecurityEventSourceTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SecurityEventSourceTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SecurityEventSourceTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
