package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;

/**
 * facade over raw hook intererface
 */
public class StorageInterceptorHooksFacade {
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	public StorageInterceptorHooksFacade(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	/**
	 * Interceptor call: STORAGE_PRESEARCH_REGISTERED
	 *
	 * @param theRequestDetails
	 * @param theParams
	 * @param search
	 */
	public void callStoragePresearchRegistered(RequestDetails theRequestDetails, SearchParameterMap theParams, Search search) {
		HookParams params = new HookParams()
			.add(ICachedSearchDetails.class, search)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
			.add(SearchParameterMap.class, theParams);
		CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRESEARCH_REGISTERED, params);
	}
	//private IInterceptorBroadcaster myInterceptorBroadcaster;
}
