package ca.uhn.fhir.empi.link.entity;

public enum EmpiLinkSourceEnum {
	/**
	 * Link was created or last modified by an algorithm
	 */
	AUTO,

	/**
	 * Link was created or last modified by a person
	 */

	MANUAL

	// Stored in database as ORDINAL.  Only add new values to bottom!
}
