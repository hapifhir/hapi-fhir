package ca.uhn.fhir.storage.interceptor.balp;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsyncMemoryQueueBackedFhirClientBalpSinkTest {

	@RegisterExtension
	@Order(0)
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirVersionEnum.DSTU3)
		.withServer(t->t.registerProvider(new MySystemProvider()));
	@RegisterExtension
	@Order(1)
	private HashMapResourceProviderExtension<AuditEvent> myAuditEventProvider = new HashMapResourceProviderExtension<>(myServer, AuditEvent.class);


	@Test
	public void testStressTest() {
		AsyncMemoryQueueBackedFhirClientBalpSink sink = new AsyncMemoryQueueBackedFhirClientBalpSink(myServer.getFhirContext(), myServer.getBaseUrl());
		try {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 100; j++) {
					AuditEvent auditEvent = new AuditEvent();
					auditEvent.setAction(AuditEvent.AuditEventAction.C);
					sink.recordAuditEvent(auditEvent);
				}
			}

			await().until(() -> myAuditEventProvider.getStoredResources().size() == 1000);

		} finally {
			sink.stop();
		}
	}


	@Test
	public void recordAuditEvent() {
		// Setup
		AsyncMemoryQueueBackedFhirClientBalpSink sink = new AsyncMemoryQueueBackedFhirClientBalpSink(myServer.getFhirContext(), myServer.getBaseUrl());
		try {
			org.hl7.fhir.r4.model.AuditEvent auditEvent1 = new org.hl7.fhir.r4.model.AuditEvent();
			auditEvent1.addEntity().setWhat(new org.hl7.fhir.r4.model.Reference("Patient/123"));
			org.hl7.fhir.r4.model.AuditEvent auditEvent2 = new org.hl7.fhir.r4.model.AuditEvent();
			auditEvent2.addEntity().setWhat(new org.hl7.fhir.r4.model.Reference("Patient/456"));

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
		} finally {
			sink.stop();
		}
	}

	@Test
	public void recordAuditEvent_AutoRetry() {
		// Setup
		AsyncMemoryQueueBackedFhirClientBalpSink sink = new AsyncMemoryQueueBackedFhirClientBalpSink(myServer.getFhirContext(), myServer.getBaseUrl());
		try {
			org.hl7.fhir.r4.model.AuditEvent auditEvent1 = new org.hl7.fhir.r4.model.AuditEvent();
			auditEvent1.addEntity().setWhat(new org.hl7.fhir.r4.model.Reference("Patient/123"));
			org.hl7.fhir.r4.model.AuditEvent auditEvent2 = new org.hl7.fhir.r4.model.AuditEvent();
			auditEvent2.addEntity().setWhat(new org.hl7.fhir.r4.model.Reference("Patient/456"));

			AtomicInteger counter = new AtomicInteger(1);
			myServer.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, new IAnonymousInterceptor() {
				@Override
				public void invoke(IPointcut thePointcut, HookParams theArgs) {
					if (counter.getAndDecrement() > 0) {
						throw new InternalErrorException("Intentional error for unit test");
					}
				}
			});

			// Test
			sink.recordAuditEvent(auditEvent1);
			sink.recordAuditEvent(auditEvent2);

			// Validate
			myAuditEventProvider.waitForCreateCount(2);
			assertThat(counter.get()).isLessThan(1);
			List<String> whats = myAuditEventProvider
				.getStoredResources()
				.stream()
				.map(t -> t.getEntity().get(0).getReference().getReference())
				.toList();
			assertThat(whats).as(whats.toString()).containsExactlyInAnyOrder("Patient/123", "Patient/456");
		} finally {
			sink.stop();
		}
	}


	public class MySystemProvider {

		@Transaction
		public Bundle transaction(@TransactionParam Bundle theInput, RequestDetails theRequestDetails) {
			Bundle retVal = new Bundle();
			retVal.setType(Bundle.BundleType.TRANSACTIONRESPONSE);

			for (Bundle.BundleEntryComponent next : theInput.getEntry()) {
				assertEquals(Bundle.HTTPVerb.POST, next.getRequest().getMethod());
				assertEquals("AuditEvent", next.getRequest().getUrl());
				AuditEvent resource = (AuditEvent) next.getResource();
				IIdType outcome = myAuditEventProvider.create(resource, theRequestDetails).getId();

				Bundle.BundleEntryComponent respEntry = retVal.addEntry();
				respEntry.getResponse().setStatus("201 Created");
				respEntry.getResponse().setLocation(outcome.getValue());
			}

			return retVal;
		}

	}
}
