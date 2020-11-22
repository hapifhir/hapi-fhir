package ca.uhn.fhir.jpa.cache;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * An immutable object containing the list of resource creates, updates and deletes detected by a cache refresh operation.
 */
public class ResourceChangeResult {
	public final long created;
	public final long updated;
	public final long deleted;

	public ResourceChangeResult() {
		created = 0;
		updated = 0;
		deleted = 0;
	}

	private ResourceChangeResult(long theCreated, long theUpdated, long theDeleted) {
		created = theCreated;
		updated = theUpdated;
		deleted = theDeleted;
	}

	public static ResourceChangeResult fromCreated(int theCreated) {
		return new ResourceChangeResult(theCreated, 0, 0);
	}

	public static ResourceChangeResult fromResourceChangeEvent(IResourceChangeEvent theResourceChangeEvent) {
		return new ResourceChangeResult(theResourceChangeEvent.getCreatedResourceIds().size(), theResourceChangeEvent.getUpdatedResourceIds().size(), theResourceChangeEvent.getDeletedResourceIds().size());
	}

	public ResourceChangeResult plus(ResourceChangeResult theResult) {
		return new ResourceChangeResult(created + theResult.created, updated + theResult.updated, deleted + theResult.deleted);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("created", created)
			.append("updated", updated)
			.append("deleted", deleted)
			.toString();
	}
}
