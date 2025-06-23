package ca.uhn.fhir.storage.interceptor;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This object is used as a method parameter for interceptor hook methods implementing the
 * {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_PRE_AUTO_CREATE_PLACEHOLDER_REFERENCE}
 * pointcut.
 *
 * @since 8.4.0
 */
public class AutoCreatePlaceholderReferenceTargetRequest {

	private final IBaseResource myTargetResourceToCreate;

	/**
	 * Constructor
	 */
	public AutoCreatePlaceholderReferenceTargetRequest(IBaseResource theTargetResourceToCreate) {
		myTargetResourceToCreate = theTargetResourceToCreate;
	}

	/**
	 * Provides the resource that is going to be automatically created. Interceptors may make changes
	 * to the resource, but they must not modify its ID.
	 */
	public IBaseResource getTargetResourceToCreate() {
		return myTargetResourceToCreate;
	}
}
