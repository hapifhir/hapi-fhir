package ca.uhn.fhir.jpa.subscription.module;

import org.hl7.fhir.exceptions.FHIRException;

public enum CanonicalSubscriptionChannelType {
	/**
	 * The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the service base, and an update (PUT) is made.
	 */
	RESTHOOK,
	/**
	 * The channel is executed by sending a packet across a web socket connection maintained by the client. The URL identifies the websocket, and the client binds to this URL.
	 */
	WEBSOCKET,
	/**
	 * The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).
	 */
	EMAIL,
	/**
	 * The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).
	 */
	SMS,
	/**
	 * The channel is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc.) to the application identified in the URI.
	 */
	MESSAGE,
	/**
	 * added to help the parsers with the generic types
	 */
	NULL;

	public static CanonicalSubscriptionChannelType fromCode(String codeString) throws FHIRException {
		if (codeString == null || "".equals(codeString))
			return null;
		if ("rest-hook".equals(codeString))
			return RESTHOOK;
		if ("websocket".equals(codeString))
			return WEBSOCKET;
		if ("email".equals(codeString))
			return EMAIL;
		if ("sms".equals(codeString))
			return SMS;
		if ("message".equals(codeString))
			return MESSAGE;
		else
			throw new FHIRException("Unknown SubscriptionChannelType code '" + codeString + "'");
	}

	public String toCode() {
		switch (this) {
			case RESTHOOK:
				return "rest-hook";
			case WEBSOCKET:
				return "websocket";
			case EMAIL:
				return "email";
			case SMS:
				return "sms";
			case MESSAGE:
				return "message";
			default:
				return "?";
		}
	}

	public String getSystem() {
		switch (this) {
			case RESTHOOK:
				return "http://hl7.org/fhir/subscription-channel-type";
			case WEBSOCKET:
				return "http://hl7.org/fhir/subscription-channel-type";
			case EMAIL:
				return "http://hl7.org/fhir/subscription-channel-type";
			case SMS:
				return "http://hl7.org/fhir/subscription-channel-type";
			case MESSAGE:
				return "http://hl7.org/fhir/subscription-channel-type";
			default:
				return "?";
		}
	}

	public String getDefinition() {
		switch (this) {
			case RESTHOOK:
				return "The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the service base, and an update (PUT) is made.";
			case WEBSOCKET:
				return "The channel is executed by sending a packet across a web socket connection maintained by the client. The URL identifies the websocket, and the client binds to this URL.";
			case EMAIL:
				return "The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).";
			case SMS:
				return "The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).";
			case MESSAGE:
				return "The channel is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc.) to the application identified in the URI.";
			default:
				return "?";
		}
	}

	public String getDisplay() {
		switch (this) {
			case RESTHOOK:
				return "Rest Hook";
			case WEBSOCKET:
				return "Websocket";
			case EMAIL:
				return "Email";
			case SMS:
				return "SMS";
			case MESSAGE:
				return "Message";
			default:
				return "?";
		}
	}
}
