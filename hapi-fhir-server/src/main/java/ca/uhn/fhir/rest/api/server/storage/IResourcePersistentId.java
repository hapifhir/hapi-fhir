package ca.uhn.fhir.rest.api.server.storage;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourcePersistentId<T> {
	IResourcePersistentId NOT_FOUND = new NotFoundPid();

	IIdType getAssociatedResourceId();

	IResourcePersistentId<T> setAssociatedResourceId(IIdType theAssociatedResourceId);

	T getId();

	Long getVersion();
	/**
	 * @param theVersion This should only be populated if a specific version is needed. If you want the current version,
	 *                   leave this as <code>null</code>
	 */

	void setVersion(Long theVersion);

	/**
	 * Note that, like Version, ResourceType is usually not populated.  It is only populated in scenarios where it
	 * is needed downstream.
	 */
	String getResourceType();
}
