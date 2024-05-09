package ca.uhn.fhir.rest.server.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HashMapResourceProviderTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	@Order(0)
	private static final RestfulServerExtension ourRestServer = new RestfulServerExtension(ourCtx);
	@RegisterExtension
	@Order(1)
	private static final HashMapResourceProviderExtension<Patient> myPatientResourceProvider = new HashMapResourceProviderExtension<>(ourRestServer, Patient.class);
	@RegisterExtension
	@Order(2)
	private static final HashMapResourceProviderExtension<Observation> myObservationResourceProvider = new HashMapResourceProviderExtension<>(ourRestServer, Observation.class);

	private static final Logger ourLog = LoggerFactory.getLogger(HashMapResourceProviderTest.class);

	@Mock
	private IAnonymousInterceptor myAnonymousInterceptor;

	@Test
	public void testCreateAndRead() {
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, myAnonymousInterceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, myAnonymousInterceptor);

		// Create
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourRestServer.getFhirClient().create().resource(p).execute().getId();
		assertThat(id.getIdPart()).matches("[0-9]+");
		assertEquals("1", id.getVersionIdPart());

		verify(myAnonymousInterceptor, Mockito.times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED), any());
		verify(myAnonymousInterceptor, Mockito.times(1)).invoke(eq(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED), any());

		// Read
		p = (Patient) ourRestServer.getFhirClient().read().resource("Patient").withId(id).execute();
		assertTrue(p.getActive());

		assertEquals(1, myPatientResourceProvider.getCountRead());
	}

	@Test
	public void testCreateWithClientAssignedIdAndRead() {
		// Create
		Patient p = new Patient();
		p.setId("ABC");
		p.setActive(true);
		IIdType id = ourRestServer.getFhirClient().update().resource(p).execute().getId();
		assertEquals("ABC", id.getIdPart());
		assertEquals("1", id.getVersionIdPart());

		// Read
		p = (Patient) ourRestServer.getFhirClient().read().resource("Patient").withId(id).execute();
		assertTrue(p.getActive());
	}

	@Test
	public void testDelete() {
		// Create
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourRestServer.getFhirClient().create().resource(p).execute().getId().toUnqualified();
		assertThat(id.getIdPart()).matches("[0-9]+");
		assertEquals("1", id.getVersionIdPart());

		assertEquals(0, myPatientResourceProvider.getCountDelete());

		ourRestServer.getFhirClient().delete().resourceById(id.toUnqualifiedVersionless()).execute();
		ourLog.info("About to execute");

		assertEquals(1, myPatientResourceProvider.getCountDelete());

		// VRead original version
		ourRestServer.getFhirClient().read().resource("Patient").withId(id.withVersion("1")).execute();

		// Vread gone version
		try {
			ourRestServer.getFhirClient().read().resource("Patient").withId(id.withVersion("2")).execute();
			fail();		} catch (ResourceGoneException e) {
			// good
		}

		// Read (non vread) gone version
		try {
			ourRestServer.getFhirClient().read().resource("Patient").withId(id.toUnqualifiedVersionless()).execute();
			fail();		} catch (ResourceGoneException e) {
			// good
		}

		// History should include deleted entry
		Bundle history = ourRestServer.getFhirClient().history().onType(Patient.class).returnBundle(Bundle.class).execute();
		ourLog.info("History:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(history));
		assertEquals(id.withVersion("2").getValue(), history.getEntry().get(0).getRequest().getUrl());
		assertEquals("DELETE", history.getEntry().get(0).getRequest().getMethod().toCode());
		assertEquals(id.withVersion("1").getValue(), history.getEntry().get(1).getRequest().getUrl());
		assertEquals("POST", history.getEntry().get(1).getRequest().getMethod().toCode());

		// Search should not include deleted entry
		Bundle search = ourRestServer.getFhirClient().search().forResource("Patient").returnBundle(Bundle.class).execute();
		ourLog.info("Search:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(search));
		assertThat(search.getEntry()).isEmpty();

	}

	@Test
	public void testHistoryInstance() {
		// Create Res 1
		Patient p = new Patient();
		p.setActive(true);
		IIdType id1 = ourRestServer.getFhirClient().create().resource(p).execute().getId();
		assertThat(id1.getIdPart()).matches("[0-9]+");
		assertEquals("1", id1.getVersionIdPart());

		// Create Res 2
		p = new Patient();
		p.setActive(true);
		IIdType id2 = ourRestServer.getFhirClient().create().resource(p).execute().getId();
		assertThat(id2.getIdPart()).matches("[0-9]+");
		assertEquals("1", id2.getVersionIdPart());

		// Update Res 2
		p = new Patient();
		p.setId(id2);
		p.setActive(false);
		id2 = ourRestServer.getFhirClient().update().resource(p).execute().getId();
		assertThat(id2.getIdPart()).matches("[0-9]+");
		assertEquals("2", id2.getVersionIdPart());

		Bundle history = ourRestServer.getFhirClient()
			.history()
			.onInstance(id2.toUnqualifiedVersionless())
			.andReturnBundle(Bundle.class)
			.encodedJson()
			.prettyPrint()
			.execute();
		ourLog.debug(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(history));
		List<String> ids = history
			.getEntry()
			.stream()
			.map(t -> t.getResource().getIdElement().toUnqualified().getValue())
			.collect(Collectors.toList());
		assertThat(ids).containsExactly(id2.toUnqualified().withVersion("2").getValue(), id2.toUnqualified().withVersion("1").getValue());

	}

	@Test
	public void testHistoryType() {
		// Create Res 1
		Patient p = new Patient();
		p.setActive(true);
		IIdType id1 = ourRestServer.getFhirClient().create().resource(p).execute().getId();
		assertThat(id1.getIdPart()).matches("[0-9]+");
		assertEquals("1", id1.getVersionIdPart());

		// Create Res 2
		p = new Patient();
		p.setActive(true);
		IIdType id2 = ourRestServer.getFhirClient().create().resource(p).execute().getId();
		assertThat(id2.getIdPart()).matches("[0-9]+");
		assertEquals("1", id2.getVersionIdPart());

		// Update Res 2
		p = new Patient();
		p.setId(id2);
		p.setActive(false);
		id2 = ourRestServer.getFhirClient().update().resource(p).execute().getId();
		assertThat(id2.getIdPart()).matches("[0-9]+");
		assertEquals("2", id2.getVersionIdPart());

		Bundle history = ourRestServer.getFhirClient()
			.history()
			.onType(Patient.class)
			.andReturnBundle(Bundle.class)
			.execute();
		List<String> ids = history
			.getEntry()
			.stream()
			.map(t -> t.getResource().getIdElement().toUnqualified().getValue())
			.collect(Collectors.toList());
		ourLog.info("Received IDs: {}", ids);
		assertThat(ids).containsExactly(id2.toUnqualified().withVersion("2").getValue(), id2.toUnqualified().withVersion("1").getValue(), id1.toUnqualified().withVersion("1").getValue());

	}

	@Test
	public void testSearchAll() {
		// Create
		for (int i = 0; i < 100; i++) {
			Patient p = new Patient();
			p.addName().setFamily("FAM" + i);
			ourRestServer.getFhirClient().registerInterceptor(new LoggingInterceptor(true));
			IIdType id = ourRestServer.getFhirClient().create().resource(p).execute().getId();
			assertThat(id.getIdPart()).matches("[0-9]+");
			assertEquals("1", id.getVersionIdPart());
		}

		// Search
		Bundle resp = ourRestServer.getFhirClient()
			.search()
			.forResource("Patient")
			.returnBundle(Bundle.class)
			.execute();
		ourLog.info("Search:\n{}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));
		assertEquals(100, resp.getTotal());
		assertThat(resp.getEntry()).hasSize(100);
		assertFalse(resp.getEntry().get(0).hasRequest());
		assertFalse(resp.getEntry().get(1).hasRequest());

		assertEquals(1, myPatientResourceProvider.getCountSearch());

	}

	@Test
	public void testSearchById() {
		// Create
		for (int i = 0; i < 100; i++) {
			Patient p = new Patient();
			p.addName().setFamily("FAM" + i);
			IIdType id = ourRestServer.getFhirClient().create().resource(p).execute().getId();
			assertThat(id.getIdPart()).matches("[0-9]+");
			assertEquals("1", id.getVersionIdPart());
		}

		// Search
		Bundle resp = ourRestServer.getFhirClient()
			.search()
			.forResource("Patient")
			.where(IAnyResource.RES_ID.exactly().codes("2", "3"))
			.returnBundle(Bundle.class).execute();
		assertEquals(2, resp.getTotal());
		assertThat(resp.getEntry()).hasSize(2);
		List<String> respIds = resp.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(respIds).containsExactlyInAnyOrder("Patient/2", "Patient/3");

		// Search
		resp = ourRestServer.getFhirClient()
			.search()
			.forResource("Patient")
			.where(IAnyResource.RES_ID.exactly().codes("2", "3"))
			.where(IAnyResource.RES_ID.exactly().codes("2", "3"))
			.returnBundle(Bundle.class).execute();
		assertEquals(2, resp.getTotal());
		assertThat(resp.getEntry()).hasSize(2);
		respIds = resp.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(respIds).containsExactlyInAnyOrder("Patient/2", "Patient/3");

		resp = ourRestServer.getFhirClient()
			.search()
			.forResource("Patient")
			.where(IAnyResource.RES_ID.exactly().codes("2", "3"))
			.where(IAnyResource.RES_ID.exactly().codes("4", "3"))
			.returnBundle(Bundle.class).execute();
		respIds = resp.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(respIds).containsExactlyInAnyOrder("Patient/3");
		assertEquals(1, resp.getTotal());
		assertThat(resp.getEntry()).hasSize(1);

	}

	@Test
	public void testUpdate() {
		// Create
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = ourRestServer.getFhirClient().create().resource(p).execute().getId();
		assertThat(id.getIdPart()).matches("[0-9]+");
		assertEquals("1", id.getVersionIdPart());

		// Update
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, myAnonymousInterceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, myAnonymousInterceptor);

		p = new Patient();
		p.setId(id);
		p.setActive(false);
		id = ourRestServer.getFhirClient().update().resource(p).execute().getId();
		assertThat(id.getIdPart()).matches("[0-9]+");
		assertEquals("2", id.getVersionIdPart());

		verify(myAnonymousInterceptor, Mockito.times(1)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), any());
		verify(myAnonymousInterceptor, Mockito.times(1)).invoke(eq(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED), any());

		assertEquals(1, myPatientResourceProvider.getCountCreate());
		assertEquals(1, myPatientResourceProvider.getCountUpdate());

		// Read
		p = (Patient) ourRestServer.getFhirClient().read().resource("Patient").withId(id.withVersion("1")).execute();
		assertTrue(p.getActive());
		p = (Patient) ourRestServer.getFhirClient().read().resource("Patient").withId(id.withVersion("2")).execute();
		assertFalse(p.getActive());
		try {
			ourRestServer.getFhirClient().read().resource("Patient").withId(id.withVersion("3")).execute();
			fail();		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	@Test
	void testReadDeletedOk() {
		Patient patient = new Patient();
		patient.setActive(true);
		SystemRequestDetails srd = new SystemRequestDetails();

		IIdType patientId = myPatientResourceProvider.create(patient, srd).getId().toVersionless();
		Patient readPatient = myPatientResourceProvider.read(patientId, srd, true);
		assertFalse(readPatient.isDeleted());
	}

	@Test
	void testReadDeletedDeletedOk() {
		Patient patient = new Patient();
		patient.setActive(true);
		SystemRequestDetails srd = new SystemRequestDetails();

		IIdType patientId = myPatientResourceProvider.create(patient, srd).getId().toVersionless();
		myPatientResourceProvider.delete(patientId, srd);
		Patient readPatient = myPatientResourceProvider.read(patientId, srd, true);
		assertTrue(readPatient.isDeleted());
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
