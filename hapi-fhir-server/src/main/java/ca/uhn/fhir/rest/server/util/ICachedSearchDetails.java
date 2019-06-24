package ca.uhn.fhir.rest.server.util;

public interface ICachedSearchDetails {

	/**
	 * Mark the search as being non-reusable by future queries (as in, the search
	 * can serve multiple page requests for pages of the same search, but it
	 * won't be reused in the future)
	 */
	void setCannotBeReused();

}
