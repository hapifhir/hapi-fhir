package ca.uhn.fhir.jpa.entity;

public enum EmpiTargetType {
	PATIENT,
	PRACTITIONER,
	PERSON;

	EmpiTargetType(){}

	public static EmpiTargetType valueOfCaseInsensitive(String theValue) {
		return valueOf(EmpiTargetType.class, theValue.toUpperCase());
	}
}
