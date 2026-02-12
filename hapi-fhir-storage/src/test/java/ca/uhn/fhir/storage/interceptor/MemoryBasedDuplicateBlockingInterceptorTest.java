package ca.uhn.fhir.storage.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MemoryBasedDuplicateBlockingInterceptorTest implements ITestDataBuilder {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final MemoryBasedDuplicateBlockingInterceptor mySvc = new MemoryBasedDuplicateBlockingInterceptor(myFhirContext);

	@RegisterExtension
	private LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(MemoryBasedDuplicateBlockingInterceptor.class);

	@Test
	void testBlockDuplicate() {

		// Setup
		Patient p0 = (Patient) buildPatient(withId("A"), withActiveTrue(), withBirthdate("2020-01-01"));
		addMetaToResource(p0);
		mySvc.onCreate(p0);

		// Test
		Patient p1 = (Patient) buildPatient(withId("B"), withActiveTrue(), withBirthdate("2020-01-01"));
		addMetaToResource(p1);
		assertThatThrownBy(()->mySvc.onCreate(p1))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("HAPI-2840: Can not create resource duplicating existing resource: Patient/A");

	}

	@Test
	void testBlockDuplicate_Log() {

		// Setup
		Patient p0 = (Patient) buildPatient(withId("A"), withActiveTrue(), withBirthdate("2020-01-01"));
		mySvc.onCreate(p0);

		// Test
		Patient p1 = (Patient) buildPatient(withId("B"), withActiveTrue(), withBirthdate("2020-01-01"));
		for (int i = 0; i < 30; i++) {
			assertThatThrownBy(() -> mySvc.onCreate(p1));
		}

		List<ILoggingEvent> logEvents = myLogbackTestExtension.getLogEvents();
		ILoggingEvent lastEvent = logEvents.get(logEvents.size() - 1);
		assertThat(lastEvent.getFormattedMessage())
			.contains("Blocked creating a duplicate resource to Patient/B existing: Patient/A (30/hr)");
	}

	@Test
	void testAllowNonDuplicate() {

		// Setup
		Patient p0 = (Patient) buildPatient(withId("A"), withActiveTrue(), withBirthdate("2020-01-01"));
		addMetaToResource(p0);
		mySvc.onCreate(p0);

		// Test
		Patient p1 = (Patient) buildPatient(withId("B"), withActiveFalse(), withBirthdate("2020-01-01"));
		addMetaToResource(p1);
		assertDoesNotThrow(()->mySvc.onCreate(p1));

	}

	private void addMetaToResource(Patient thePatient) {
		thePatient.getMeta().addTag().setSystem("http://foo").setCode(UUID.randomUUID().toString());
		thePatient.getText().getDiv().setValueAsString("<div>" + UUID.randomUUID() + "</div>");
	}

	@Test
	void testExpireOldCacheEntries() {
		for (int i = 0; i < MemoryBasedDuplicateBlockingInterceptor.ENTRY_COUNT; i++) {
			Patient p = (Patient) buildPatient(withId("P" + i), withIdentifier("http://foo", Integer.toString(i)));
			mySvc.onCreate(p);
		}

		// The first entry in the cache still matches
		Patient p0 = (Patient) buildPatient(withId("P0"), withIdentifier("http://foo", "0"));
		assertThatThrownBy(()->mySvc.onCreate(p0))
			.isInstanceOf(PreconditionFailedException.class)
			.hasMessageContaining("HAPI-2840: Can not create resource duplicating existing resource: Patient/P0" );

		// Add another new entry
		Patient pa = (Patient) buildPatient(withId("PA"), withIdentifier("http://foo", "PA"));
		assertDoesNotThrow(()->mySvc.onCreate(pa));

		// The first entry in the cache no longer matches
		Patient p0b = (Patient) buildPatient(withId("P0"), withIdentifier("http://foo", "0"));
		assertDoesNotThrow(()->mySvc.onCreate(p0b));
	}



	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}
}
