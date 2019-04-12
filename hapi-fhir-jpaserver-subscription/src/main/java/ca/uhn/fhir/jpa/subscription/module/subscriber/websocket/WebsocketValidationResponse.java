package ca.uhn.fhir.jpa.subscription.module.subscriber.websocket;

import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;

public class WebsocketValidationResponse {
	private final boolean myValid;
	private final String myMessage;
	private final ActiveSubscription myActiveSubscription;

	public static WebsocketValidationResponse INVALID_RESPONSE(String theMessage) {
		return new WebsocketValidationResponse(false, theMessage, null);
	}

	public static WebsocketValidationResponse VALID_RESPONSE(ActiveSubscription theActiveSubscription) {
		return new WebsocketValidationResponse(true, null, theActiveSubscription);
	}

	private WebsocketValidationResponse(boolean theValid, String theMessage, ActiveSubscription theActiveSubscription) {
		myValid = theValid;
		myMessage = theMessage;
		myActiveSubscription = theActiveSubscription;
	}

	public boolean isValid() {
		return myValid;
	}

	public String getMessage() {
		return myMessage;
	}

	public ActiveSubscription getActiveSubscription() {
		return myActiveSubscription;
	}
}
