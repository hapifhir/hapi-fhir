
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

public enum SecurityEventParticipantNetworkTypeEnum {

	/**
	 * Code Value: <b>1</b>
	 *
	 * Machine Name, including DNS name.
	 */
	MACHINE_NAME("1", "http://hl7.org/fhir/network-type"),
	
	/**
	 * Code Value: <b>2</b>
	 *
	 * IP Address.
	 */
	IP_ADDRESS("2", "http://hl7.org/fhir/network-type"),
	
	/**
	 * Code Value: <b>3</b>
	 *
	 * Telephone Number.
	 */
	TELEPHONE_NUMBER("3", "http://hl7.org/fhir/network-type"),
	
	/**
	 * Code Value: <b>4</b>
	 *
	 * Email address.
	 */
	EMAIL_ADDRESS("4", "http://hl7.org/fhir/network-type"),
	
	/**
	 * Code Value: <b>5</b>
	 *
	 * URI (User directory, HTTP-PUT, ftp, etc.).
	 */
	URI("5", "http://hl7.org/fhir/network-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/network-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/network-type";

	/**
	 * Name for this Value Set:
	 * SecurityEventParticipantNetworkType
	 */
	public static final String VALUESET_NAME = "SecurityEventParticipantNetworkType";

	private static Map<String, SecurityEventParticipantNetworkTypeEnum> CODE_TO_ENUM = new HashMap<String, SecurityEventParticipantNetworkTypeEnum>();
	private static Map<String, Map<String, SecurityEventParticipantNetworkTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SecurityEventParticipantNetworkTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SecurityEventParticipantNetworkTypeEnum next : SecurityEventParticipantNetworkTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SecurityEventParticipantNetworkTypeEnum>());
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
	public SecurityEventParticipantNetworkTypeEnum forCode(String theCode) {
		SecurityEventParticipantNetworkTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SecurityEventParticipantNetworkTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SecurityEventParticipantNetworkTypeEnum>() {
		@Override
		public String toCodeString(SecurityEventParticipantNetworkTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SecurityEventParticipantNetworkTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SecurityEventParticipantNetworkTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SecurityEventParticipantNetworkTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SecurityEventParticipantNetworkTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SecurityEventParticipantNetworkTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
