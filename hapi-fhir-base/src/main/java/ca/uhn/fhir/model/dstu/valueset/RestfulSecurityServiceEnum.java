
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum RestfulSecurityServiceEnum {

	/**
	 * Code Value: <b>OAuth</b>
	 *
	 * OAuth (see oauth.net).
	 */
	OAUTH("OAuth", "http://hl7.org/fhir/restful-security-service"),
	
	/**
	 * Code Value: <b>OAuth2</b>
	 *
	 * OAuth version 2 (see oauth.net).
	 */
	OAUTH2("OAuth2", "http://hl7.org/fhir/restful-security-service"),
	
	/**
	 * Code Value: <b>NTLM</b>
	 *
	 * Microsoft NTLM Authentication.
	 */
	NTLM("NTLM", "http://hl7.org/fhir/restful-security-service"),
	
	/**
	 * Code Value: <b>Basic</b>
	 *
	 * Basic authentication defined in HTTP specification.
	 */
	BASIC("Basic", "http://hl7.org/fhir/restful-security-service"),
	
	/**
	 * Code Value: <b>Kerberos</b>
	 *
	 * see http://www.ietf.org/rfc/rfc4120.txt.
	 */
	KERBEROS("Kerberos", "http://hl7.org/fhir/restful-security-service"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/restful-security-service
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/restful-security-service";

	/**
	 * Name for this Value Set:
	 * RestfulSecurityService
	 */
	public static final String VALUESET_NAME = "RestfulSecurityService";

	private static Map<String, RestfulSecurityServiceEnum> CODE_TO_ENUM = new HashMap<String, RestfulSecurityServiceEnum>();
	private static Map<String, Map<String, RestfulSecurityServiceEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, RestfulSecurityServiceEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (RestfulSecurityServiceEnum next : RestfulSecurityServiceEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, RestfulSecurityServiceEnum>());
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
	public RestfulSecurityServiceEnum forCode(String theCode) {
		RestfulSecurityServiceEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<RestfulSecurityServiceEnum> VALUESET_BINDER = new IValueSetEnumBinder<RestfulSecurityServiceEnum>() {
		@Override
		public String toCodeString(RestfulSecurityServiceEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(RestfulSecurityServiceEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public RestfulSecurityServiceEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public RestfulSecurityServiceEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, RestfulSecurityServiceEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	RestfulSecurityServiceEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
