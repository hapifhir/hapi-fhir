package ca.uhn.fhir.jpa.interceptor.balp;

public enum BalpProfileEnum {

	BASIC_READ("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Read"),
	PATIENT_READ("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientRead"),

	PATIENT_QUERY("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientQuery"),

	;
	private final String myProfileUrl;

	BalpProfileEnum(String theProfileUrl) {
		myProfileUrl = theProfileUrl;
	}

	public String getProfileUrl() {
		return myProfileUrl;
	}
}
