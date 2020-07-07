package ca.uhn.fhir.jpa.entity;

public enum EmpiTargetType {
	PATIENT("patient"),
	PRACTITIONER("practitioner");

	EmpiTargetType(String thePractitioner) {}

	public static EmpiTargetType valueOfCaseInsensitive(String theValue) {
		return valueOf(EmpiTargetType.class, theValue.toUpperCase());
	}
}
