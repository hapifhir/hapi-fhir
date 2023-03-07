package ca.uhn.fhir.jpa.interceptor.balp;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IAuditEventSink {
	void recordAuditEvent(IBaseResource theAuditEvent);

}
