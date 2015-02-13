
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SubscriptionChannelTypeEnum {

	/**
	 * Code Value: <b>rest-hook</b>
	 *
	 * The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the service base, and an update (PUT) is made.
	 */
	REST_HOOK("rest-hook", "http://hl7.org/fhir/subscription-channel-type"),
	
	/**
	 * Code Value: <b>websocket</b>
	 *
	 * The channel is executed by sending a packet across a web socket connection maintained by the client. The URL identifies the websocket, and the client binds to this URL.
	 */
	WEBSOCKET("websocket", "http://hl7.org/fhir/subscription-channel-type"),
	
	/**
	 * Code Value: <b>email</b>
	 *
	 * The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).
	 */
	EMAIL("email", "http://hl7.org/fhir/subscription-channel-type"),
	
	/**
	 * Code Value: <b>sms</b>
	 *
	 * The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).
	 */
	SMS("sms", "http://hl7.org/fhir/subscription-channel-type"),
	
	/**
	 * Code Value: <b>message</b>
	 *
	 * The channel Is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc) to the application identified in the URI.
	 */
	MESSAGE("message", "http://hl7.org/fhir/subscription-channel-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/subscription-channel-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/subscription-channel-type";

	/**
	 * Name for this Value Set:
	 * SubscriptionChannelType
	 */
	public static final String VALUESET_NAME = "SubscriptionChannelType";

	private static Map<String, SubscriptionChannelTypeEnum> CODE_TO_ENUM = new HashMap<String, SubscriptionChannelTypeEnum>();
	private static Map<String, Map<String, SubscriptionChannelTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SubscriptionChannelTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SubscriptionChannelTypeEnum next : SubscriptionChannelTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SubscriptionChannelTypeEnum>());
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
	public SubscriptionChannelTypeEnum forCode(String theCode) {
		SubscriptionChannelTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SubscriptionChannelTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SubscriptionChannelTypeEnum>() {
		@Override
		public String toCodeString(SubscriptionChannelTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SubscriptionChannelTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SubscriptionChannelTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SubscriptionChannelTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SubscriptionChannelTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SubscriptionChannelTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
