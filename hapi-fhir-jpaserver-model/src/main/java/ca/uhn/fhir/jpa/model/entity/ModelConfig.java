package ca.uhn.fhir.jpa.model.entity;

public class ModelConfig {
	/**
	 * update setter javadoc if default changes
	 */
	private boolean myAllowContainsSearches = false;

	/**
	 * If enabled, the server will support the use of :contains searches,
	 * which are helpful but can have adverse effects on performance.
	 * <p>
	 * Default is <code>false</code> (Note that prior to HAPI FHIR
	 * 3.5.0 the default was <code>true</code>)
	 * </p>
	 * <p>
	 * Note: If you change this value after data already has
	 * already been stored in the database, you must for a reindexing
	 * of all data in the database or resources may not be
	 * searchable.
	 * </p>
	 */
	public boolean isAllowContainsSearches() {
		return myAllowContainsSearches;
	}

	/**
	 * If enabled, the server will support the use of :contains searches,
	 * which are helpful but can have adverse effects on performance.
	 * <p>
	 * Default is <code>false</code> (Note that prior to HAPI FHIR
	 * 3.5.0 the default was <code>true</code>)
	 * </p>
	 * <p>
	 * Note: If you change this value after data already has
	 * already been stored in the database, you must for a reindexing
	 * of all data in the database or resources may not be
	 * searchable.
	 * </p>
	 */
	public void setAllowContainsSearches(boolean theAllowContainsSearches) {
		this.myAllowContainsSearches = theAllowContainsSearches;
	}

}
