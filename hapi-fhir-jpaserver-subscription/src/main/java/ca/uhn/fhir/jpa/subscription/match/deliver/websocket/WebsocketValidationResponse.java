package ca.uhn.fhir.jpa.subscription.match.deliver.websocket;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;

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
