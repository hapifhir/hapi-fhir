package ca.uhn.fhir.jpa.model.interceptor.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Value for {@link Hook#value()}
 */
public enum Pointcut {

	/**
	 * Invoked immediately after the delivery of a REST HOOK subscription.
	 * <p>
	 * When this hook is called, all processing is complete so this hook should not
	 * make any changes to the parameters.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 */
	SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY("CanonicalSubscription", "ResourceDeliveryMessage"),

	/**
	 * Invoked immediately before the delivery of a REST HOOK subscription.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 */
	SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY("CanonicalSubscription", "ResourceDeliveryMessage"),

	/**
	 * Invoked whenever a persisted resource (a resource that has just been stored in the
	 * database via a create/update/patch/etc.) has been checked for whether any subscriptions
	 * were triggered as a result of the operation
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage</li>
	 * </ul>
	 */
	SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED("ResourceModifiedMessage")

	;

	private final List<String> myParameterTypes;

	Pointcut(String... theParameterTypes) {
		myParameterTypes = Collections.unmodifiableList(Arrays.asList(theParameterTypes));
	}

	public List<String> getParameterTypes() {
		return myParameterTypes;
	}
}
