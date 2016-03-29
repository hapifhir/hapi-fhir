package ca.uhn.fhir.rest.method;

import java.util.Collection;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IRequestOperationCallback {

	void resourceCreated(IBaseResource theResource);

	void resourceDeleted(IBaseResource theResource);

	void resourcesCreated(Collection<? extends IBaseResource> theResource);

	void resourcesDeleted(Collection<? extends IBaseResource> theResource);

	void resourcesUpdated(Collection<? extends IBaseResource> theResource);

	void resourceUpdated(IBaseResource theResource);
}
