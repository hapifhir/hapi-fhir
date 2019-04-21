package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.api.*;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * The JPA server has two interceptor services (aka two registries of interceptors). One that lives
 * in the JPA module and is created via Spring, and one that lives in the RestfulServer. We do this
 * so that interceptors can be registered at the JPA level via Spring (which is convenient for
 * lots of reasons) and also via the RestfulServer (which is how other interceptors work outside the
 * JPA context)
 * <p>
 *     This class is basically a composite broadcaster that broadcasts events to the internal registry but
 *     also to
 * </p>
 */
public class JpaInterceptorService implements IInterceptorService {

	private IInterceptorService myInterceptorBroadcaster = new InterceptorService("hapi-fhir-jpa");

	@Override
	public boolean callHooks(Pointcut thePointcut, HookParams theParams) {
		if (!myInterceptorBroadcaster.callHooks(thePointcut, theParams)) {
			return false;
		}
		RequestDetails requestDetails = theParams.get(RequestDetails.class);
		if (requestDetails != null) {
			requestDetails.getInterceptorBroadcaster().callHooks(thePointcut, theParams);
		}
		return true;
	}

	@Override
	public Object callHooksAndReturnObject(Pointcut thePointcut, HookParams theParams) {
		Object retVal = myInterceptorBroadcaster.callHooksAndReturnObject(thePointcut, theParams);
		if (retVal == null) {
			RequestDetails requestDetails = theParams.get(RequestDetails.class);
			if (requestDetails != null) {
				retVal = requestDetails.getInterceptorBroadcaster().callHooksAndReturnObject(thePointcut, theParams);
			}
		}
		return retVal;
	}

	@Override
	public boolean registerThreadLocalInterceptor(Object theInterceptor) {
		return myInterceptorBroadcaster.registerThreadLocalInterceptor(theInterceptor);
	}

	@Override
	public void unregisterThreadLocalInterceptor(Object theInterceptor) {
		myInterceptorBroadcaster.unregisterThreadLocalInterceptor(theInterceptor);
	}

	@Override
	public boolean registerInterceptor(Object theInterceptor) {
		return myInterceptorBroadcaster.registerInterceptor(theInterceptor);
	}

	@Override
	public void unregisterInterceptor(Object theInterceptor) {
		myInterceptorBroadcaster.unregisterInterceptor(theInterceptor);
	}

	@Override
	public void registerAnonymousInterceptor(Pointcut thePointcut, IAnonymousInterceptor theInterceptor) {
		myInterceptorBroadcaster.registerAnonymousInterceptor(thePointcut, theInterceptor);
	}

	@Override
	public void registerAnonymousInterceptor(Pointcut thePointcut, int theOrder, IAnonymousInterceptor theInterceptor) {
		myInterceptorBroadcaster.registerAnonymousInterceptor(thePointcut, theOrder, theInterceptor);
	}

	@Override
	public List<Object> getAllRegisteredInterceptors() {
		return myInterceptorBroadcaster.getAllRegisteredInterceptors();
	}

	@Override
	public void unregisterAllInterceptors() {
		myInterceptorBroadcaster.unregisterAllInterceptors();
	}

	@Override
	public void unregisterInterceptors(@Nullable Collection<?> theInterceptors) {
		myInterceptorBroadcaster.unregisterInterceptors(theInterceptors);
	}

	@Override
	public void registerInterceptors(@Nullable Collection<?> theInterceptors) {
		myInterceptorBroadcaster.registerInterceptors(theInterceptors);
	}
}
