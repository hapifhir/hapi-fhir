package ca.uhn.fhir.jpa.model.subscription.interceptor.api;

/**
 * Value for {@link SubscriptionHook#value()}
 */
public enum Pointcut {

	/**
	 * Invoked immediately after the delivery of a REST HOOK subscription.
	 * <p>
	 * When this hook is called, all processing is complete so this hook should not
	 * make any changes to the parameters.
	 * </p>
	 */
	AFTER_REST_HOOK_DELIVERY,

	/**
	 * Invoked immediately before the delivery of a REST HOOK subscription.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 */
	BEFORE_REST_HOOK_DELIVERY;


}
