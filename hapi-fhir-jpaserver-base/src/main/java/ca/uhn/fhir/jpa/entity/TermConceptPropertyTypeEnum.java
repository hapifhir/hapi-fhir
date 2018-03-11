package ca.uhn.fhir.jpa.entity;

/**
 * @see TermConceptProperty#getType()
 * @see TermConceptProperty#MAX_PROPTYPE_ENUM_LENGTH
 */
public enum TermConceptPropertyTypeEnum {
	/*
	 * VALUES SHOULD BE <= 6 CHARS LONG!
	 *
	 * We store this in a DB column of that length
	 */

	/**
	 * String
	 */
	STRING,
	/**
	 * Coding
	 */
	CODING
}
