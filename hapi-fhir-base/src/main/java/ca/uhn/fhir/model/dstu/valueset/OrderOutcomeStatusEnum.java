
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

public enum OrderOutcomeStatusEnum {

	/**
	 * Code Value: <b>pending</b>
	 *
	 * The order is known, but no processing has occurred at this time.
	 */
	PENDING("pending", "http://hl7.org/fhir/order-outcome-code"),
	
	/**
	 * Code Value: <b>review</b>
	 *
	 * The order is undergoing initial processing to determine whether it will be accepted (usually this involves human review).
	 */
	REVIEW("review", "http://hl7.org/fhir/order-outcome-code"),
	
	/**
	 * Code Value: <b>rejected</b>
	 *
	 * The order was rejected because of a workflow/business logic reason.
	 */
	REJECTED("rejected", "http://hl7.org/fhir/order-outcome-code"),
	
	/**
	 * Code Value: <b>error</b>
	 *
	 * The order was unable to be processed because of a technical error (i.e. unexpected error).
	 */
	ERROR("error", "http://hl7.org/fhir/order-outcome-code"),
	
	/**
	 * Code Value: <b>accepted</b>
	 *
	 * The order has been accepted, and work is in progress.
	 */
	ACCEPTED("accepted", "http://hl7.org/fhir/order-outcome-code"),
	
	/**
	 * Code Value: <b>cancelled</b>
	 *
	 * Processing the order was halted at the initiators request.
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/order-outcome-code"),
	
	/**
	 * Code Value: <b>aborted</b>
	 *
	 * Processing the order was stopped because of some workflow/business logic reason.
	 */
	ABORTED("aborted", "http://hl7.org/fhir/order-outcome-code"),
	
	/**
	 * Code Value: <b>complete</b>
	 *
	 * The order has been completed.
	 */
	COMPLETE("complete", "http://hl7.org/fhir/order-outcome-code"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/order-outcome-code
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/order-outcome-code";

	/**
	 * Name for this Value Set:
	 * OrderOutcomeStatus
	 */
	public static final String VALUESET_NAME = "OrderOutcomeStatus";

	private static Map<String, OrderOutcomeStatusEnum> CODE_TO_ENUM = new HashMap<String, OrderOutcomeStatusEnum>();
	private static Map<String, Map<String, OrderOutcomeStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, OrderOutcomeStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (OrderOutcomeStatusEnum next : OrderOutcomeStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, OrderOutcomeStatusEnum>());
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
	public OrderOutcomeStatusEnum forCode(String theCode) {
		OrderOutcomeStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<OrderOutcomeStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<OrderOutcomeStatusEnum>() {
		@Override
		public String toCodeString(OrderOutcomeStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(OrderOutcomeStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public OrderOutcomeStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public OrderOutcomeStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, OrderOutcomeStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	OrderOutcomeStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
