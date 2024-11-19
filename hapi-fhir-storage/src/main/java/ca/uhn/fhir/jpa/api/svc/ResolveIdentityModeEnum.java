package ca.uhn.fhir.jpa.api.svc;

public class ResolveIdentityModeEnum {

	private final boolean myIncludeDeleted;
	private final boolean myUseCache;
	private final boolean myFailOnDeleted;

	private ResolveIdentityModeEnum(boolean theIncludeDeleted, boolean theFailOnDeleted, boolean theUseCache) {
		myIncludeDeleted = theIncludeDeleted;
		myUseCache = theUseCache;
		myFailOnDeleted = theFailOnDeleted;
	}

	public boolean isUseCache(boolean theDeleteEnabled) {
		if (myUseCache) {
			return true;
		} else {
			return !theDeleteEnabled;
		}
	}

	public boolean isIncludeDeleted() {
		return myIncludeDeleted;
	}

	public boolean isFailOnDeleted() {
		return myFailOnDeleted;
	}

	/**
	 * Deleted resource identities can be included in the results
	 */
	public static Builder includeDeleted() {
		return new Builder(true, false);
	}

	/**
	 * Deleted resource identities should be exluded from the results
	 */
	public static Builder excludeDeleted() {
		return new Builder(false, false);
	}

	/**
	 * Throw a {@link ca.uhn.fhir.rest.server.exceptions.ResourceGoneException} if
	 * any of the supplied IDs corresponds to a deleted resource
	 */
	public static Builder failOnDeleted() {
		return new Builder(false, true);
	}

	public static class Builder {

		private final boolean myIncludeDeleted;
		private final boolean myFailOnDeleted;

		private Builder(boolean theIncludeDeleted, boolean theFailOnDeleted) {
			myIncludeDeleted = theIncludeDeleted;
			myFailOnDeleted = theFailOnDeleted;
		}

		// FIXME: rename to cacheOk
		public ResolveIdentityModeEnum useCache() {
			return new ResolveIdentityModeEnum(myIncludeDeleted, myFailOnDeleted, true);
		}

		// FIXME: rename to noCacheUnlessDeletesDisabled (here and in the rest of the class)
		public ResolveIdentityModeEnum noCache() {
			return new ResolveIdentityModeEnum(myIncludeDeleted, myFailOnDeleted, false);
		}
	}
}
