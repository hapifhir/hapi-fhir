
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ReferralStatusEnum {

	/**
	 * Code Value: <b>draft</b>
	 *
	 * A draft referral that has yet to be send.
	 */
	DRAFT("draft", "http://hl7.org/fhir/referralstatus"),
	
	/**
	 * Code Value: <b>sent</b>
	 *
	 * The referral has been transmitted, but not yet acknowledged by the recipient.
	 */
	SENT("sent", "http://hl7.org/fhir/referralstatus"),
	
	/**
	 * Code Value: <b>active</b>
	 *
	 * The referral has been acknowledged by the recipient, and is in the process of being actioned.
	 */
	ACTIVE("active", "http://hl7.org/fhir/referralstatus"),
	
	/**
	 * Code Value: <b>cancelled</b>
	 *
	 * The referral has been cancelled without being completed. For example it is no longer needed.
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/referralstatus"),
	
	/**
	 * Code Value: <b>refused</b>
	 *
	 * The recipient has declined to accept the referral.
	 */
	REFUSED("refused", "http://hl7.org/fhir/referralstatus"),
	
	/**
	 * Code Value: <b>completed</b>
	 *
	 * The referral has been completely actioned.
	 */
	COMPLETED("completed", "http://hl7.org/fhir/referralstatus"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/referralstatus
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/referralstatus";

	/**
	 * Name for this Value Set:
	 * ReferralStatus
	 */
	public static final String VALUESET_NAME = "ReferralStatus";

	private static Map<String, ReferralStatusEnum> CODE_TO_ENUM = new HashMap<String, ReferralStatusEnum>();
	private static Map<String, Map<String, ReferralStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ReferralStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ReferralStatusEnum next : ReferralStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ReferralStatusEnum>());
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
	public ReferralStatusEnum forCode(String theCode) {
		ReferralStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ReferralStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<ReferralStatusEnum>() {
		@Override
		public String toCodeString(ReferralStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ReferralStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ReferralStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ReferralStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ReferralStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ReferralStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
