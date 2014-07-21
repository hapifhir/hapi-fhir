
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

public enum QueryOutcomeEnum {

	/**
	 * Code Value: <b>ok</b>
	 *
	 * The query was processed successfully.
	 */
	OK("ok", "http://hl7.org/fhir/query-outcome"),
	
	/**
	 * Code Value: <b>limited</b>
	 *
	 * The query was processed successfully, but some additional limitations were added.
	 */
	LIMITED("limited", "http://hl7.org/fhir/query-outcome"),
	
	/**
	 * Code Value: <b>refused</b>
	 *
	 * The server refused to process the query.
	 */
	REFUSED("refused", "http://hl7.org/fhir/query-outcome"),
	
	/**
	 * Code Value: <b>error</b>
	 *
	 * The server tried to process the query, but some error occurred.
	 */
	ERROR("error", "http://hl7.org/fhir/query-outcome"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/query-outcome
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/query-outcome";

	/**
	 * Name for this Value Set:
	 * QueryOutcome
	 */
	public static final String VALUESET_NAME = "QueryOutcome";

	private static Map<String, QueryOutcomeEnum> CODE_TO_ENUM = new HashMap<String, QueryOutcomeEnum>();
	private static Map<String, Map<String, QueryOutcomeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, QueryOutcomeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (QueryOutcomeEnum next : QueryOutcomeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, QueryOutcomeEnum>());
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
	public QueryOutcomeEnum forCode(String theCode) {
		QueryOutcomeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<QueryOutcomeEnum> VALUESET_BINDER = new IValueSetEnumBinder<QueryOutcomeEnum>() {
		@Override
		public String toCodeString(QueryOutcomeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(QueryOutcomeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public QueryOutcomeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public QueryOutcomeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, QueryOutcomeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	QueryOutcomeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
