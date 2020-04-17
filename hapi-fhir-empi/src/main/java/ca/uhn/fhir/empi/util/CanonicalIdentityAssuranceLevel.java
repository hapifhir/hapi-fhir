package ca.uhn.fhir.empi.util;

import org.hl7.fhir.r4.model.Person;

public enum CanonicalIdentityAssuranceLevel {
	LEVEL1("level1"),
	LEVEL2("level2"),
	LEVEL3("level3"),
	LEVEL4("level4");

	private String myCanonicalLevel;
	private CanonicalIdentityAssuranceLevel(String theCanonicalLevel) {
		myCanonicalLevel = theCanonicalLevel;
	}

	public Person.IdentityAssuranceLevel toR4() {
		return Person.IdentityAssuranceLevel.fromCode(myCanonicalLevel);
	}

	public org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel toDstu3() {
		return org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel.fromCode(myCanonicalLevel);
	}

}
