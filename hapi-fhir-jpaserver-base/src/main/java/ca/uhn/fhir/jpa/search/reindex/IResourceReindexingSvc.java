package ca.uhn.fhir.jpa.search.reindex;

public interface IResourceReindexingSvc {

	/**
	 * Marks all indexes as needing fresh indexing
	 */
	void markAllResourcesForReindexing();

	/**
	 * Marks all indexes of the given type as needing fresh indexing
	 */
	void markAllResourcesForReindexing(String theType);

	/**
	 * Called automatically by the job scheduler
	 *
	 * @return Returns null if the system did not attempt to perform a pass because one was
	 * already proceeding. Otherwise, returns the number of resources affected.
	 */
	Integer runReindexingPass();

	/**
	 * Does the same thing as {@link #runReindexingPass()} but makes sure to perform at
	 * least one pass even if one is half finished
	 */
	Integer forceReindexingPass();

	/**
	 * Cancels all running and future reindexing jobs. This is mainly intended
	 * to be used by unit tests.
	 */
	void cancelAndPurgeAllJobs();
}
