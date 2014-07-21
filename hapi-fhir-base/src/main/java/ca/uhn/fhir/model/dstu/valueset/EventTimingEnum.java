
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

public enum EventTimingEnum {

	/**
	 * Code Value: <b>HS</b>
	 */
	HS("HS", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>WAKE</b>
	 */
	WAKE("WAKE", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>AC</b>
	 */
	AC("AC", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>ACM</b>
	 */
	ACM("ACM", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>ACD</b>
	 */
	ACD("ACD", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>ACV</b>
	 */
	ACV("ACV", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>PC</b>
	 */
	PC("PC", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>PCM</b>
	 */
	PCM("PCM", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>PCD</b>
	 */
	PCD("PCD", "http://hl7.org/fhir/v3/TimingEvent"),
	
	/**
	 * Code Value: <b>PCV</b>
	 */
	PCV("PCV", "http://hl7.org/fhir/v3/TimingEvent"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/event-timing
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/event-timing";

	/**
	 * Name for this Value Set:
	 * EventTiming
	 */
	public static final String VALUESET_NAME = "EventTiming";

	private static Map<String, EventTimingEnum> CODE_TO_ENUM = new HashMap<String, EventTimingEnum>();
	private static Map<String, Map<String, EventTimingEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, EventTimingEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (EventTimingEnum next : EventTimingEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, EventTimingEnum>());
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
	public EventTimingEnum forCode(String theCode) {
		EventTimingEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EventTimingEnum> VALUESET_BINDER = new IValueSetEnumBinder<EventTimingEnum>() {
		@Override
		public String toCodeString(EventTimingEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(EventTimingEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public EventTimingEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public EventTimingEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, EventTimingEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	EventTimingEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
