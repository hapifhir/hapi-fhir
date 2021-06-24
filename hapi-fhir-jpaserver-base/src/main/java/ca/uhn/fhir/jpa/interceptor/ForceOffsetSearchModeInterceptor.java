package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.apache.commons.lang3.Validate;

/**
 * This interceptor for the HAPI FHIR JPA server forces all queries to
 * be performed as offset queries. This means that the query cache will
 * not be used and searches will never result in any writes to the
 * database.
 */
@Interceptor
public class ForceOffsetSearchModeInterceptor {

	private Integer myDefaultCount = 100;

	public void setDefaultCount(Integer theDefaultCount) {
		Validate.notNull(theDefaultCount, "theDefaultCount must not be null");
		myDefaultCount = theDefaultCount;
	}

	@Hook(Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void storagePreSearchRegistered(SearchParameterMap theMap) {
		if (theMap.getOffset() == null) {
			theMap.setOffset(0);
		}
		if (theMap.getCount() == null) {
			theMap.setCount(myDefaultCount);
		}
	}

}
