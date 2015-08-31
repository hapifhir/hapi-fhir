package ca.uhn.fhir.jpa.dao;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;

public class FhirResourceDaoDstu2UpdateTest extends BaseJpaDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2UpdateTest.class);

	@Test
	public void testUpdateAndGetHistoryResource() throws InterruptedException {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = myPatientDao.create(patient);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		assertEquals("1", outcome.getId().getVersionIdPart());

		Date now = new Date();
		Patient retrieved = myPatientDao.read(outcome.getId());
		InstantDt published = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		InstantDt updated = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertTrue(published.before(now));
		assertTrue(updated.before(now));

		Thread.sleep(1000);

		reset(myInterceptor);
		retrieved.getIdentifierFirstRep().setValue("002");
		MethodOutcome outcome2 = myPatientDao.update(retrieved);
		assertEquals(outcome.getId().getIdPart(), outcome2.getId().getIdPart());
		assertNotEquals(outcome.getId().getVersionIdPart(), outcome2.getId().getVersionIdPart());
		assertEquals("2", outcome2.getId().getVersionIdPart());

		// Verify interceptor
		ArgumentCaptor<ActionRequestDetails> detailsCapt = ArgumentCaptor.forClass(ActionRequestDetails.class);
		verify(myInterceptor).incomingRequestPreHandled(eq(RestOperationTypeEnum.UPDATE), detailsCapt.capture());
		ActionRequestDetails details = detailsCapt.getValue();
		assertNotNull(details.getId());
		assertEquals("Patient", details.getResourceType());
		assertEquals(Patient.class, details.getResource().getClass());

		Date now2 = new Date();

		Patient retrieved2 = myPatientDao.read(outcome.getId().toVersionless());

		assertEquals("2", retrieved2.getId().getVersionIdPart());
		assertEquals("002", retrieved2.getIdentifierFirstRep().getValue());
		InstantDt published2 = (InstantDt) retrieved2.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		InstantDt updated2 = (InstantDt) retrieved2.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertTrue(published2.before(now));
		assertTrue(updated2.after(now));
		assertTrue(updated2.before(now2));

		Thread.sleep(2000);

		/*
		 * Get history
		 */

		IBundleProvider historyBundle = myPatientDao.history(outcome.getId(), null);

		assertEquals(2, historyBundle.size());

		List<IBaseResource> history = historyBundle.getResources(0, 2);
		assertEquals("1", history.get(1).getIdElement().getVersionIdPart());
		assertEquals("2", history.get(0).getIdElement().getVersionIdPart());
		assertEquals(published, ResourceMetadataKeyEnum.PUBLISHED.get((IResource) history.get(1)));
		assertEquals(published, ResourceMetadataKeyEnum.PUBLISHED.get((IResource) history.get(1)));
		assertEquals(updated, ResourceMetadataKeyEnum.UPDATED.get((IResource) history.get(1)));
		assertEquals("001", ((Patient) history.get(1)).getIdentifierFirstRep().getValue());
		assertEquals(published2, ResourceMetadataKeyEnum.PUBLISHED.get((IResource) history.get(0)));
		assertEquals(updated2, ResourceMetadataKeyEnum.UPDATED.get((IResource) history.get(0)));
		assertEquals("002", ((Patient) history.get(0)).getIdentifierFirstRep().getValue());

	}

	@Test
	public void testUpdateByUrl() {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);

		myPatientDao.update(p, "Patient?identifier=urn%3Asystem%7C" + methodName);

		p = myPatientDao.read(id.toVersionless());
		assertThat(p.getId().toVersionless().toString(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getId().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId().toString(), endsWith("/_history/2"));

	}

	@Test
	public void testUpdateCreatesTextualIdIfItDoesntAlreadyExist() {
		Patient p = new Patient();
		String methodName = "testUpdateCreatesTextualIdIfItDoesntAlreadyExist";
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);

		IIdType id = myPatientDao.update(p).getId();
		assertEquals("Patient/" + methodName, id.toUnqualifiedVersionless().getValue());

		p = myPatientDao.read(id);
		assertEquals(methodName, p.getIdentifierFirstRep().getValue());
	}

	@Test
	public void testUpdateDoesntFailForUnknownIdWithNumberThenText() {
		String methodName = "testUpdateFailsForUnknownIdWithNumberThenText";
		Patient p = new Patient();
		p.setId("0" + methodName);
		p.addName().addFamily(methodName);

		myPatientDao.update(p);
	}

	@Test
	public void testUpdateMaintainsSearchParams() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2AAA");
		p1.addName().addFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2AAA");
		IIdType p1id = myPatientDao.create(p1).getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		p2.addName().addFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2BBB");
		myPatientDao.create(p2).getId();

		Set<Long> ids = myPatientDao.searchForIds(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2AAA"));
		assertEquals(1, ids.size());
		assertThat(ids, contains(p1id.getIdPartAsLong()));

		// Update the name
		p1.getNameFirstRep().getGivenFirstRep().setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		MethodOutcome update2 = myPatientDao.update(p1);
		IIdType p1id2 = update2.getId();

		ids = myPatientDao.searchForIds(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2AAA"));
		assertEquals(0, ids.size());

		ids = myPatientDao.searchForIds(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2BBB"));
		assertEquals(2, ids.size());

		// Make sure vreads work
		p1 = myPatientDao.read(p1id);
		assertEquals("testUpdateMaintainsSearchParamsDstu2AAA", p1.getNameFirstRep().getGivenAsSingleString());

		p1 = myPatientDao.read(p1id2);
		assertEquals("testUpdateMaintainsSearchParamsDstu2BBB", p1.getNameFirstRep().getGivenAsSingleString());

	}

	@Test
	public void testUpdateRejectsInvalidTypes() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IIdType p1id = myPatientDao.create(p1).getId();

		Organization p2 = new Organization();
		p2.getNameElement().setValue("testUpdateRejectsInvalidTypes");
		try {
			p2.setId(new IdDt("Organization/" + p1id.getIdPart()));
			myOrganizationDao.update(p2);
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			p2.setId(new IdDt("Patient/" + p1id.getIdPart()));
			myOrganizationDao.update(p2);
			fail();
		} catch (UnprocessableEntityException e) {
			ourLog.error("Good", e);
		}

	}

	@Test
	public void testUpdateUnknownNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/9999999999999999");
		try {
			myPatientDao.update(p);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not create resource with ID[9999999999999999], no resource with this ID exists and clients may only"));
		}
	}

	@Test
	public void testUpdateWithInvalidIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/123:456");
		try {
			myPatientDao.update(p);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Can not process entity with ID[123:456], this is not a valid FHIR ID", e.getMessage());
		}
	}

	@Test
	public void testUpdateWithNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/123");
		try {
			myPatientDao.update(p);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("clients may only assign IDs which contain at least one non-numeric"));
		}
	}

	@Test
	public void testUpdateWithNumericThenTextIdSucceeds() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/123abc");
		IIdType id = myPatientDao.update(p).getId();
		assertEquals("123abc", id.getIdPart());
		assertEquals("1", id.getVersionIdPart());

		p = myPatientDao.read(id.toUnqualifiedVersionless());
		assertEquals("Patient/123abc", p.getId().toUnqualifiedVersionless().getValue());
		assertEquals("Hello", p.getName().get(0).getFamily().get(0).getValue());

	}

}
