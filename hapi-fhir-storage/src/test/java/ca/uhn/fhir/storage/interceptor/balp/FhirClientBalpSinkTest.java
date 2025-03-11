package ca.uhn.fhir.storage.interceptor.balp;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirClientBalpSinkTest {

	@RegisterExtension
	@Order(0)
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirVersionEnum.DSTU3);
	@RegisterExtension
	@Order(1)
	private HashMapResourceProviderExtension<AuditEvent> myAuditEventProvider = new HashMapResourceProviderExtension<>(myServer, AuditEvent.class);

	@Test
	public void recordAuditEvent() {
		// Setup
		FhirClientBalpSink sink = new FhirClientBalpSink(myServer.getFhirContext(), myServer.getBaseUrl());
		org.hl7.fhir.r4.model.AuditEvent auditEvent1 = new org.hl7.fhir.r4.model.AuditEvent();
		auditEvent1.addEntity().setWhat(new Reference("Patient/123"));
		org.hl7.fhir.r4.model.AuditEvent auditEvent2 = new org.hl7.fhir.r4.model.AuditEvent();
		auditEvent2.addEntity().setWhat(new Reference("Patient/456"));

		// Test
		sink.recordAuditEvent(auditEvent1);
		sink.recordAuditEvent(auditEvent2);

		// Validate
		myAuditEventProvider.waitForCreateCount(2);
		List<String> whats = myAuditEventProvider
			.getStoredResources()
			.stream()
			.map(t -> t.getEntity().get(0).getReference().getReference())
			.toList();
		assertThat(whats).containsExactlyInAnyOrder("Patient/123", "Patient/456");

	}
}
