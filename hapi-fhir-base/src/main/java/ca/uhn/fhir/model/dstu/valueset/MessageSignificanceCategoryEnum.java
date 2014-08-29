
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

public enum MessageSignificanceCategoryEnum {

	/**
	 * Code Value: <b>Consequence</b>
	 *
	 * The message represents/requests a change that should not be processed more than once. E.g. Making a booking for an appointment.
	 */
	CONSEQUENCE("Consequence", "http://hl7.org/fhir/message-significance-category"),
	
	/**
	 * Code Value: <b>Currency</b>
	 *
	 * The message represents a response to query for current information. Retrospective processing is wrong and/or wasteful.
	 */
	CURRENCY("Currency", "http://hl7.org/fhir/message-significance-category"),
	
	/**
	 * Code Value: <b>Notification</b>
	 *
	 * The content is not necessarily intended to be current, and it can be reprocessed, though there may be version issues created by processing old notifications.
	 */
	NOTIFICATION("Notification", "http://hl7.org/fhir/message-significance-category"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/message-significance-category
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/message-significance-category";

	/**
	 * Name for this Value Set:
	 * MessageSignificanceCategory
	 */
	public static final String VALUESET_NAME = "MessageSignificanceCategory";

	private static Map<String, MessageSignificanceCategoryEnum> CODE_TO_ENUM = new HashMap<String, MessageSignificanceCategoryEnum>();
	private static Map<String, Map<String, MessageSignificanceCategoryEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, MessageSignificanceCategoryEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (MessageSignificanceCategoryEnum next : MessageSignificanceCategoryEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, MessageSignificanceCategoryEnum>());
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
	public MessageSignificanceCategoryEnum forCode(String theCode) {
		MessageSignificanceCategoryEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<MessageSignificanceCategoryEnum> VALUESET_BINDER = new IValueSetEnumBinder<MessageSignificanceCategoryEnum>() {
		@Override
		public String toCodeString(MessageSignificanceCategoryEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(MessageSignificanceCategoryEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public MessageSignificanceCategoryEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public MessageSignificanceCategoryEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, MessageSignificanceCategoryEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	MessageSignificanceCategoryEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
