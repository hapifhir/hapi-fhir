package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.springframework.messaging.MessageHandler;

import java.util.Optional;

public interface IDeliveryHandlerCreator {
	/**
	 * Returns an empty handler if the interceptor will manually handle registration and unregistration
	 */

	Optional<MessageHandler> createDeliveryHandler(CanonicalSubscription theCanonicalized);
}
