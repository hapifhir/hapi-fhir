package ca.uhn.fhir.jpa.entity;

/**
 * Possible legal Empi Target Types, stored in database as ordinals.
 */
public enum EmpiTargetType {
	/**
	 *
	 * ORDER MATTERS, IF ADDING NEW VALUES, APPEND ONLY.
	 */
	PATIENT,

	PRACTITIONER,

	PERSON;

	EmpiTargetType(){}

	public static EmpiTargetType valueOfCaseInsensitive(String theValue) {
		return valueOf(EmpiTargetType.class, theValue.toUpperCase());
	}
}
