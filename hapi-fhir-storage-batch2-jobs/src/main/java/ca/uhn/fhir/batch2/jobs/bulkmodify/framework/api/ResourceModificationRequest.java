package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api;

import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

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
