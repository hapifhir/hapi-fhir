package ca.uhn.fhir.rest.server.util;

import com.google.common.annotations.VisibleForTesting;

public interface IResourceRepositoryCache {
	/**
	 * Request that the cache be refreshed at the next convenient time (possibly in a different thread) so that it will soon contain all the resources currently in the database.
	 */
	default void requestRefresh() {}

	/**
	 * Force an immediate cache refresh on the current thread so that when the method returns, the cache will contain all the resources currently in the database.
	 * ONLY USE IN TESTS.
	 */
	@VisibleForTesting
	default void forceRefresh() {
		throw new UnsupportedOperationException();
	}
}
