package ca.uhn.fhir.rest.api.server.storage;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import com.google.common.collect.ListMultimap;

public class DeferredInterceptorBroadcasts {

	ListMultimap<Pointcut, HookParams> myDeferredInterceptorBroadcasts;

	public DeferredInterceptorBroadcasts(ListMultimap<Pointcut, HookParams> theDeferredInterceptorBroadcasts) {
		myDeferredInterceptorBroadcasts = theDeferredInterceptorBroadcasts;
	}

	public ListMultimap<Pointcut, HookParams> getDeferredInterceptorBroadcasts() {
		return myDeferredInterceptorBroadcasts;
	}
}
