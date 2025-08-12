package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api;

import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Implementations of bulk modification jobs will subclass {@link ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep}
 * and will receive this object for each resource candidate to be modified.
 *
 * @since 8.6.0
 * @see ResourceModificationResponse
 */
public class ResourceModificationRequest {

	@Nonnull
	private final IBaseResource myResource;

	public ResourceModificationRequest(@Nonnull IBaseResource theResource) {
		myResource = theResource;
	}

	@Nonnull
	public IBaseResource getResource() {
		return myResource;
	}
}
