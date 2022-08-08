package ca.uhn.fhir.jpa.searchparam.submit.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

@Interceptor
public class SearchParamValidatingInterceptor {

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		validateSubmittedSearchParam(theResource, theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourcePreCreate(IBaseResource theOldResource, IBaseResource theResource, RequestDetails theRequestDetails) {
		validateSubmittedSearchParam(theResource, theRequestDetails);
	}

	protected void validateSubmittedSearchParam(IBaseResource theResource, RequestDetails theRequestDetails){

	}

}
