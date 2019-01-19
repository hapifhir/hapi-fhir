package ca.uhn.fhir.jpa.model.interceptor.api;

import com.google.common.annotations.VisibleForTesting;

public interface IInterceptorRegistry {

	@VisibleForTesting
	void registerAnonymousHookForUnitTest(Pointcut thePointcut, IAnonymousLambdaHook theHook);

	@VisibleForTesting
	void clearAnonymousHookForUnitTest();

	/**
	 * Invoke the interceptor methods
	 */
	boolean callHooks(Pointcut thePointcut, HookParams theParams);

	/**
	 * Invoke the interceptor methods
	 */
	boolean callHooks(Pointcut thePointcut, Object... theParams);

}
