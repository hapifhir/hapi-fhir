package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

public class ForceSynchronousSearchInterceptor {

	@Hook(Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void storagePreSearchRegistered(SearchParameterMap theMap) {
		theMap.setLoadSynchronous(true);
	}

}
