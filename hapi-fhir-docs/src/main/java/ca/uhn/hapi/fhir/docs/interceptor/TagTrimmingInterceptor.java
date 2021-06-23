package ca.uhn.hapi.fhir.docs.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;

// START SNIPPET: TagTrimmingInterceptor
/**
 * This is a simple interceptor for the JPA server that trims all tags, profiles, and security labels from
 * resources before they are saved.
 */
@Interceptor
public class TagTrimmingInterceptor {

	/** Handle creates */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void insert(IBaseResource theResource) {
		theResource.getMeta().getTag().clear();
		theResource.getMeta().getProfile().clear();
		theResource.getMeta().getSecurity().clear();
	}

	/** Handle updates */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void update(IBaseResource theOldResource, IBaseResource theResource) {
		theResource.getMeta().getTag().clear();
		theResource.getMeta().getProfile().clear();
		theResource.getMeta().getSecurity().clear();
	}

}
// END SNIPPET: TagTrimmingInterceptor
