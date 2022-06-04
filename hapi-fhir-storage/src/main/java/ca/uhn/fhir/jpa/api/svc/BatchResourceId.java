package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.Objects;

public class BatchResourceId {
	public final String resourceType;
	public final ResourcePersistentId id;

	public BatchResourceId(String theResourceType, ResourcePersistentId theId) {
		this.resourceType = theResourceType;
		this.id = theId;
	}

	public BatchResourceId(String theResourceType, Long theId) {
		this.resourceType = theResourceType;
		this.id = new ResourcePersistentId(theId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BatchResourceId that = (BatchResourceId) o;
		return resourceType.equals(that.resourceType) && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(resourceType, id);
	}

	@Override
	public String toString() {
		return resourceType + "[" + id + "]";
	}
}
