package ca.uhn.fhir.storage.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;

import java.util.Set;

/**
 * This interceptor can be used on systems with
 * {@link ca.uhn.fhir.jpa.api.config.JpaStorageSettings#setAutoCreatePlaceholderReferenceTargets(boolean) Auto-Create Placeholder Reference Targets}
 * enabled in order to cause these placeholder resources to only be created for specific resource types.
 *
 * @since 8.4.0
 */
@Interceptor
public class AutoCreatePlaceholderReferenceEnabledByTypeInterceptor {

	private final Set<String> myResourceTypesToAllow;

	/**
	 * Constructor
	 *
	 * @param theResourceTypesToAllow The resource types to allow. For example, if you pass in the strings "Patient"
	 *                                and "Observation", then placeholder reference target resources will be created
	 *                                with these resource types but not with any other resource types.
	 */
	public AutoCreatePlaceholderReferenceEnabledByTypeInterceptor(String... theResourceTypesToAllow) {
		myResourceTypesToAllow = Set.of(theResourceTypesToAllow);
	}

	/**
	 * This method will be called automatically before each auto-created placeholder
	 * reference target resource.
	 */
	@Hook(Pointcut.STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE)
	public AutoCreatePlaceholderReferenceTargetResponse autoCreatePlaceholderReferenceTarget(AutoCreatePlaceholderReferenceTargetRequest theRequest) {

		String resourceType = theRequest.getTargetResourceToCreate().getIdElement().getResourceType();
		if (!myResourceTypesToAllow.contains(resourceType)) {
			return AutoCreatePlaceholderReferenceTargetResponse.doNotCreateTarget();
		}

		return AutoCreatePlaceholderReferenceTargetResponse.proceed();
	}


}
