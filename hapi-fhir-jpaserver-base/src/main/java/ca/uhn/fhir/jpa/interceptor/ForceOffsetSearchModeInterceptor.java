package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.apache.commons.lang3.Validate;

// FIXME: document
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
