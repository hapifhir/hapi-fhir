package ca.uhn.fhir.jpa.subscription.module.subscriber.websocket;

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import com.sun.istack.NotNull;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebsocketConnectionValidator {
	private static Logger ourLog = LoggerFactory.getLogger(WebsocketConnectionValidator.class);

	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;


	public WebsocketValidationResponse validate(@NotNull IdType id) {
		if (!id.hasIdPart() || !id.isIdPartValid()) {
			return WebsocketValidationResponse.INVALID_RESPONSE("Invalid bind request - No ID included: " + id.getValue());
		}

		if (!id.hasResourceType()) {
			id = id.withResourceType("Subscription");
		}

		ActiveSubscription activeSubscription = mySubscriptionRegistry.get(id.getIdPart());

		if (activeSubscription == null) {
			return WebsocketValidationResponse.INVALID_RESPONSE("Invalid bind request - Unknown subscription: " + id.getValue());
		}

		if (activeSubscription.getSubscription().getChannelType() != CanonicalSubscriptionChannelType.WEBSOCKET) {
			return WebsocketValidationResponse.INVALID_RESPONSE("Subscription " + id.getValue() + " is not a " + CanonicalSubscriptionChannelType.WEBSOCKET + " subscription");
		}

		return WebsocketValidationResponse.VALID_RESPONSE(activeSubscription);
	}
}
