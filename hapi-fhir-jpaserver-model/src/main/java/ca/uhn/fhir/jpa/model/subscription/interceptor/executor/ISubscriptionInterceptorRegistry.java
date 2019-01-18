package ca.uhn.fhir.jpa.model.subscription.interceptor.executor;

import ca.uhn.fhir.jpa.model.subscription.interceptor.api.Pointcut;

public interface ISubscriptionInterceptorRegistry {

	/**
	 * Invoke the interceptor methods
	 */
	boolean callHooks(Pointcut thePointcut, HookParams theParams);

	/**
	 * Invoke the interceptor methods
	 */
	boolean callHooks(Pointcut thePointcut, Object... theParams);

}
