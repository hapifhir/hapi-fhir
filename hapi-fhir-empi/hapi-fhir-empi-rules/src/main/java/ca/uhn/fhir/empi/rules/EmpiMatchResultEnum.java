package ca.uhn.fhir.empi.rules;

public enum EmpiMatchResultEnum {
	/**
	 * Match weight fell below threshold for a match.
	 */
	NO_MATCH,

	/**
	 * Match weight fell below low threshold and high threshold.  Requires manual review.
	 */
	POSSIBLE_MATCH,

	/**
	 * Match weight was above high threshold for a match
	 */
	MATCH

	// Stored in database as ORDINAL.  Only add new values to bottom!
}
