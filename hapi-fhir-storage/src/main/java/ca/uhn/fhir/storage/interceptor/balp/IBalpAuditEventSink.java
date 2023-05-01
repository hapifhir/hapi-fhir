package ca.uhn.fhir.storage.interceptor.balp;

import org.hl7.fhir.r4.model.AuditEvent;

public interface IBalpAuditEventSink {

	void recordAuditEvent(AuditEvent theAuditEvent);

}
