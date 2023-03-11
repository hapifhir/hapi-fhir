package ca.uhn.fhir.jpa.interceptor.balp;

import org.hl7.fhir.r4.model.AuditEvent;

public interface IAuditEventSink {
	void recordAuditEvent(AuditEvent theAuditEvent);

}
