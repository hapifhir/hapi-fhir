package ca.uhn.fhir.jpa.interceptor.balp;

public enum BalpProfileEnum {
	BASIC_CREATE("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Create"),
	PATIENT_CREATE("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientCreate"),

	BASIC_READ("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Read"),
	PATIENT_READ("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientRead"),

	BASIC_QUERY("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Query"),
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
