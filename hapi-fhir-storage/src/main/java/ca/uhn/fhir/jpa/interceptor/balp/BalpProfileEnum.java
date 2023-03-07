package ca.uhn.fhir.jpa.interceptor.balp;

public enum BalpProfileEnum {

	PATIENT_READ("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientRead");

	private final String myProfileUrl;

	BalpProfileEnum(String theProfileUrl) {
		myProfileUrl = theProfileUrl;
	}

	public String getProfileUrl() {
		return myProfileUrl;
	}
}
