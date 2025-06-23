package ca.uhn.fhir.storage.interceptor;

import org.hl7.fhir.instance.model.api.IBaseResource;

// FIXME: document both
public class AutoCreatePlaceholderReferenceTargetRequest {

	private final IBaseResource myTargetResourceToCreate;

	public AutoCreatePlaceholderReferenceTargetRequest(IBaseResource theTargetResourceToCreate) {
		myTargetResourceToCreate = theTargetResourceToCreate;
	}

	public IBaseResource getTargetResourceToCreate() {
		return myTargetResourceToCreate;
	}
}
