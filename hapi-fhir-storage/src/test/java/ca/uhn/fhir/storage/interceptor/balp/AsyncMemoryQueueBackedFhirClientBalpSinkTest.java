package ca.uhn.fhir.storage.interceptor.balp;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.storage.interceptor.balp.AsyncMemoryQueueBackedFhirClientBalpSink;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class AsyncMemoryQueueBackedFhirClientBalpSinkTest {

	@RegisterExtension
	@Order(0)
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirVersionEnum.DSTU3);
	@RegisterExtension
	@Order(1)
	private HashMapResourceProviderExtension<AuditEvent> myAuditEventProvider = new HashMapResourceProviderExtension<>(myServer, AuditEvent.class);


	@Test
	public void testStressTest() {
		AsyncMemoryQueueBackedFhirClientBalpSink sink = new AsyncMemoryQueueBackedFhirClientBalpSink(myServer.getFhirContext(), myServer.getBaseUrl());
		sink.start();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 100; j++) {
				sink.recordAuditEvent(new AuditEvent());
			}
		}

		await().until(()->myAuditEventProvider.getStoredResources().size(), equalTo(1000));
	}


	@Test
	public void recordAuditEvent() {
		// Setup
		AsyncMemoryQueueBackedFhirClientBalpSink sink = new AsyncMemoryQueueBackedFhirClientBalpSink(myServer.getFhirContext(), myServer.getBaseUrl());
		sink.start();
		try {
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
			assertThat(whats, containsInAnyOrder("Patient/123", "Patient/456"));
		} finally {
			sink.stop();
			await().until(sink::isRunning, equalTo(false));
		}
	}

	@Test
	public void recordAuditEvent_AutoRetry() {
		// Setup
		AsyncMemoryQueueBackedFhirClientBalpSink sink = new AsyncMemoryQueueBackedFhirClientBalpSink(myServer.getFhirContext(), myServer.getBaseUrl());
		sink.start();
		try {
			org.hl7.fhir.r4.model.AuditEvent auditEvent1 = new org.hl7.fhir.r4.model.AuditEvent();
			auditEvent1.addEntity().setWhat(new Reference("Patient/123"));
			org.hl7.fhir.r4.model.AuditEvent auditEvent2 = new org.hl7.fhir.r4.model.AuditEvent();
			auditEvent2.addEntity().setWhat(new Reference("Patient/456"));

			AtomicInteger counter = new AtomicInteger(10);
			myServer.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, new IAnonymousInterceptor() {
				@Override
				public void invoke(IPointcut thePointcut, HookParams theArgs) {
					if (counter.decrementAndGet() > 0) {
						throw new InternalErrorException("Intentional error for unit test");
					}
				}
			});

			// Test
			sink.recordAuditEvent(auditEvent1);
			sink.recordAuditEvent(auditEvent2);

			// Validate
			myAuditEventProvider.waitForCreateCount(2);
			assertThat(counter.get(), lessThan(1));
			List<String> whats = myAuditEventProvider
				.getStoredResources()
				.stream()
				.map(t -> t.getEntity().get(0).getReference().getReference())
				.toList();
			assertThat(whats.toString(), whats, containsInAnyOrder("Patient/123", "Patient/456"));
		} finally {
			sink.stop();
			await().until(sink::isRunning, equalTo(false));
		}
	}


}
