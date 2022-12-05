package ca.uhn.fhir.rest.api.server.storage;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourcePersistentId<T> {
	IResourcePersistentId NOT_FOUND = new NotFoundPid();

	IIdType getAssociatedResourceId();

	IResourcePersistentId<T> setAssociatedResourceId(IIdType theAssociatedResourceId);

	T getId();

	Long getVersion();

	void setVersion(Long theVersion);

	String getResourceType();
}
