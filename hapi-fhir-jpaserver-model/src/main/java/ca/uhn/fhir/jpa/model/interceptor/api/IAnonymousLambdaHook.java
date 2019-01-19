package ca.uhn.fhir.jpa.model.interceptor.api;

import com.google.common.annotations.VisibleForTesting;

/**
 * This is currently only here for unit tests!
 *
 * DO NOT USE IN NON-TEST CODE. Maybe this will change in the future?
 */
@FunctionalInterface
@VisibleForTesting
public interface IAnonymousLambdaHook {

	void invoke(HookParams theArgs);

}
