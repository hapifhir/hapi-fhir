package ca.uhn.fhir.jpa.cache;

import org.apache.commons.lang3.builder.ToStringBuilder;

// FIXME KHS rename all Version classes to ResourceChange
public class ResourceChangeResult {
	public final long added;
	public final long updated;
	public final long removed;

	public ResourceChangeResult() {
		added = 0;
		updated = 0;
		removed = 0;
	}

	private ResourceChangeResult(long theAdded, long theUpdated, long theRemoved) {
		added = theAdded;
		updated = theUpdated;
		removed = theRemoved;
	}

	public static ResourceChangeResult fromAddedUpdatedRemoved(long theAdded, long theUpdated, long theRemoved) {
		return new ResourceChangeResult(theAdded, theUpdated, theRemoved);
	}

	public static ResourceChangeResult fromAdded(int theAdded) {
		return new ResourceChangeResult(theAdded, 0, 0);
	}

	public ResourceChangeResult plus(ResourceChangeResult theResult) {
		return new ResourceChangeResult(added + theResult.added, updated + theResult.updated, removed + theResult.removed);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("added", added)
			.append("updated", updated)
			.append("removed", removed)
			.toString();
	}
}
