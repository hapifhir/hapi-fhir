package ca.uhn.fhir.jpa.interceptor.balp;

import org.hl7.fhir.r4.model.AuditEvent;

public enum BalpProfileEnum {
	BASIC_CREATE("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Create", AuditEvent.AuditEventAction.C),
	PATIENT_CREATE("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientCreate", AuditEvent.AuditEventAction.C),

	BASIC_UPDATE("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Update", AuditEvent.AuditEventAction.U),
	PATIENT_UPDATE("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientUpdate", AuditEvent.AuditEventAction.U),

	BASIC_DELETE("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Delete", AuditEvent.AuditEventAction.D),
	PATIENT_DELETE("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientDelete", AuditEvent.AuditEventAction.D),

	BASIC_READ("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Read", AuditEvent.AuditEventAction.R),
	PATIENT_READ("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientRead", AuditEvent.AuditEventAction.R),

	BASIC_QUERY("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.Query", AuditEvent.AuditEventAction.E),
	PATIENT_QUERY("https://profiles.ihe.net/ITI/BALP/StructureDefinition/IHE.BasicAudit.PatientQuery", AuditEvent.AuditEventAction.E),

	;
	private final String myProfileUrl;
	private final AuditEvent.AuditEventAction myAction;

	BalpProfileEnum(String theProfileUrl, AuditEvent.AuditEventAction theAction) {
		myProfileUrl = theProfileUrl;
		myAction = theAction;
	}

	public String getProfileUrl() {
		return myProfileUrl;
	}

	public AuditEvent.AuditEventAction getAction() {
		return myAction;
	}
}
