
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

public enum ResponseTypeEnum {

	/**
	 * Code Value: <b>ok</b>
	 *
	 * The message was accepted and processed without error.
	 */
	OK("ok", "http://hl7.org/fhir/response-code"),
	
	/**
	 * Code Value: <b>transient-error</b>
	 *
	 * Some internal unexpected error occurred - wait and try again. Note - this is usually used for things like database unavailable, which may be expected to resolve, though human intervention may be required.
	 */
	TRANSIENT_ERROR("transient-error", "http://hl7.org/fhir/response-code"),
	
	/**
	 * Code Value: <b>fatal-error</b>
	 *
	 * The message was rejected because of some content in it. There is no point in re-sending without change. The response narrative SHALL describe what the issue is.
	 */
	FATAL_ERROR("fatal-error", "http://hl7.org/fhir/response-code"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/response-code
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/response-code";

	/**
	 * Name for this Value Set:
	 * ResponseType
	 */
	public static final String VALUESET_NAME = "ResponseType";

	private static Map<String, ResponseTypeEnum> CODE_TO_ENUM = new HashMap<String, ResponseTypeEnum>();
	private static Map<String, Map<String, ResponseTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ResponseTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ResponseTypeEnum next : ResponseTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ResponseTypeEnum>());
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
	public ResponseTypeEnum forCode(String theCode) {
		ResponseTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ResponseTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<ResponseTypeEnum>() {
		@Override
		public String toCodeString(ResponseTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ResponseTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ResponseTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ResponseTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ResponseTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ResponseTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
