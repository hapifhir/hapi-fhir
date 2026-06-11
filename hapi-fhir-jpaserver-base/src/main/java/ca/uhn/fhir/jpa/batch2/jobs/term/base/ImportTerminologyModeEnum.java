package ca.uhn.fhir.jpa.batch2.jobs.term.base;

public enum ImportTerminologyModeEnum {

	/**
	 * Specified codes are added to the existing codes.
	 */
	ADD,
	/**
	 * Specified codes are removed from the existing codes.
	 */
	REMOVE,
	/**
	 * Existing codes are removed and replaced with the new codes.
	 * This is the default mode.
	 */
	SNAPSHOT
}
