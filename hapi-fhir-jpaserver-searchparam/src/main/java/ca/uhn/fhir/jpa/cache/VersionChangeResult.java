package ca.uhn.fhir.jpa.cache;

import org.apache.commons.lang3.builder.ToStringBuilder;

// FIXME KHS rename all Version classes to ResourceChange
public class VersionChangeResult {
	public final long added;
	public final long updated;
	public final long removed;

	public VersionChangeResult() {
		added = 0;
		updated = 0;
		removed = 0;
	}

	private VersionChangeResult(long theAdded, long theUpdated, long theRemoved) {
		added = theAdded;
		updated = theUpdated;
		removed = theRemoved;
	}

	public static VersionChangeResult fromAddedUpdatedRemoved(long theAdded, long theUpdated, long theRemoved) {
		return new VersionChangeResult(theAdded, theUpdated, theRemoved);
	}

	public static VersionChangeResult fromAdded(int theAdded) {
		return new VersionChangeResult(theAdded, 0, 0);
	}

	public VersionChangeResult plus(VersionChangeResult theResult) {
		return new VersionChangeResult(added + theResult.added, updated + theResult.updated, removed + theResult.removed);
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
