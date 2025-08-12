package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api;

import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Implementations of bulk modification jobs will subclass {@link ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep}
 * and will return this object for each resource candidate to be modified.
 *
 * @since 8.6.0
 * @see ResourceModificationRequest
 */
public class ResourceModificationResponse {

	@Nonnull
	private final IBaseResource myResource;

	/**
	 * Use static factory methods to instantiate this class
	 */
	private ResourceModificationResponse(@Nonnull IBaseResource theResource) {
		myResource = theResource;
	}

	@Nonnull
	public IBaseResource getResource() {
		return myResource;
	}

	public static ResourceModificationResponse updateResource(@Nonnull IBaseResource theResource) {
		Validate.notNull(theResource, "theResource must not be null");
		return new ResourceModificationResponse(theResource);
	}

}
