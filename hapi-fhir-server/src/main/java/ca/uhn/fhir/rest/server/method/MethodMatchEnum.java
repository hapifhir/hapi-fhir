package ca.uhn.fhir.rest.server.method;

public enum MethodMatchEnum {

	// Order these from worst to best!

	NONE,
	APPROXIMATE,
	PERFECT;

	public MethodMatchEnum weakerOf(MethodMatchEnum theOther) {
		if (this.ordinal() < theOther.ordinal()) {
			return this;
		} else {
			return theOther;
		}
	}

}
