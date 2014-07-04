
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum MessageTransportEnum {

	/**
	 * Code Value: <b>http</b>
	 *
	 * The application sends or receives messages using HTTP POST (may be over http or https).
	 */
	HTTP("http", "http://hl7.org/fhir/message-transport"),
	
	/**
	 * Code Value: <b>ftp</b>
	 *
	 * The application sends or receives messages using File Transfer Protocol.
	 */
	FTP("ftp", "http://hl7.org/fhir/message-transport"),
	
	/**
	 * Code Value: <b>mllp</b>
	 *
	 * The application sends or receivers messages using HL7's Minimal Lower Level Protocol.
	 */
	MLLP("mllp", "http://hl7.org/fhir/message-transport"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/message-transport
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/message-transport";

	/**
	 * Name for this Value Set:
	 * MessageTransport
	 */
	public static final String VALUESET_NAME = "MessageTransport";

	private static Map<String, MessageTransportEnum> CODE_TO_ENUM = new HashMap<String, MessageTransportEnum>();
	private static Map<String, Map<String, MessageTransportEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, MessageTransportEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (MessageTransportEnum next : MessageTransportEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, MessageTransportEnum>());
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
	public MessageTransportEnum forCode(String theCode) {
		MessageTransportEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<MessageTransportEnum> VALUESET_BINDER = new IValueSetEnumBinder<MessageTransportEnum>() {
		@Override
		public String toCodeString(MessageTransportEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(MessageTransportEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public MessageTransportEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public MessageTransportEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, MessageTransportEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	MessageTransportEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
