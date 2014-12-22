
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SubscriptionStatusEnum {

	/**
	 * Code Value: <b>requested</b>
	 *
	 * The client has requested the subscription, and the server has not yet set it up.
	 */
	REQUESTED("requested", "http://hl7.org/fhir/subscription-status"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * The subscription is active.
	 */
	ACTIVE("active", "http://hl7.org/fhir/subscription-status"),
	
	/**
	 * Code Value: <b>error</b>
	 *
	 * The server has an error executing the notification.
	 */
	ERROR("error", "http://hl7.org/fhir/subscription-status"),
	
	/**
	 * Code Value: <b>off</b>
	 *
	 * Too many errors have occurred or the subscription has expired.
	 */
	OFF("off", "http://hl7.org/fhir/subscription-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/subscription-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/subscription-status";

	/**
	 * Name for this Value Set:
	 * SubscriptionStatus
	 */
	public static final String VALUESET_NAME = "SubscriptionStatus";

	private static Map<String, SubscriptionStatusEnum> CODE_TO_ENUM = new HashMap<String, SubscriptionStatusEnum>();
	private static Map<String, Map<String, SubscriptionStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SubscriptionStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SubscriptionStatusEnum next : SubscriptionStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SubscriptionStatusEnum>());
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
	public SubscriptionStatusEnum forCode(String theCode) {
		SubscriptionStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SubscriptionStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<SubscriptionStatusEnum>() {
		@Override
		public String toCodeString(SubscriptionStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SubscriptionStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SubscriptionStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SubscriptionStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SubscriptionStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SubscriptionStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
