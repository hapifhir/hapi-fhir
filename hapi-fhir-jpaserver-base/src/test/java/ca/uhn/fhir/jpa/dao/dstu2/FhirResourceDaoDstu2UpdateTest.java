package ca.uhn.fhir.jpa.dao.dstu2;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
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
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoDstu2UpdateTest extends BaseJpaDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2UpdateTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testUpdateAndGetHistoryResource() throws InterruptedException {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().addFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		assertEquals("1", outcome.getId().getVersionIdPart());

		Date now = new Date();
		Patient retrieved = myPatientDao.read(outcome.getId(), mySrd);
		InstantDt published = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.PUBLISHED);
		InstantDt updated = (InstantDt) retrieved.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		assertTrue(published.before(now));
		assertTrue(updated.before(now));

		Thread.sleep(1000);

		reset(myInterceptor);
		retrieved.getIdentifierFirstRep().setValue("002");
		MethodOutcome outcome2 = myPatientDao.update(retrieved, mySrd);
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

		Patient retrieved2 = myPatientDao.read(outcome.getId().toVersionless(), mySrd);

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

		IBundleProvider historyBundle = myPatientDao.history(outcome.getId(), null, null, mySrd);

		assertEquals(2, historyBundle.size().intValue());

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
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);

		myPatientDao.update(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
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

		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertEquals("Patient/" + methodName, id.toUnqualifiedVersionless().getValue());

		p = myPatientDao.read(id, mySrd);
		assertEquals(methodName, p.getIdentifierFirstRep().getValue());
	}

	@Test
	public void testUpdateDoesntFailForUnknownIdWithNumberThenText() {
		String methodName = "testUpdateFailsForUnknownIdWithNumberThenText";
		Patient p = new Patient();
		p.setId("0" + methodName);
		p.addName().addFamily(methodName);

		myPatientDao.update(p, mySrd);
	}

	/**
	 * Per the spec, update should preserve tags and security labels but not profiles
	 */
	@Test
	public void testUpdateMaintainsTagsAndSecurityLabels() throws InterruptedException {
		String methodName = "testUpdateMaintainsTagsAndSecurityLabels";

		IIdType p1id;
		{
			Patient p1 = new Patient();
			p1.addName().addFamily(methodName);

			TagList tagList = new TagList();
			tagList.addTag("tag_scheme1", "tag_term1");
			ResourceMetadataKeyEnum.TAG_LIST.put(p1, tagList);
			List<BaseCodingDt> secList = new ArrayList<BaseCodingDt>();
			secList.add(new CodingDt("sec_scheme1", "sec_term1"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(p1, secList);
			List<IdDt> profileList = new ArrayList<IdDt>();
			profileList.add(new IdDt("http://foo1"));
			ResourceMetadataKeyEnum.PROFILES.put(p1, profileList);

			p1id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p1 = new Patient();
			p1.setId(p1id);
			p1.addName().addFamily(methodName);

			TagList tagList = new TagList();
			tagList.addTag("tag_scheme2", "tag_term2");
			ResourceMetadataKeyEnum.TAG_LIST.put(p1, tagList);
			List<BaseCodingDt> secList = new ArrayList<BaseCodingDt>();
			secList.add(new CodingDt("sec_scheme2", "sec_term2"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(p1, secList);
			List<IdDt> profileList = new ArrayList<IdDt>();
			profileList.add(new IdDt("http://foo2"));
			ResourceMetadataKeyEnum.PROFILES.put(p1, profileList);

			myPatientDao.update(p1, mySrd);
		}
		{
			Patient p1 = myPatientDao.read(p1id, mySrd);
			TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(p1);
			assertThat(tagList, containsInAnyOrder(new Tag("tag_scheme1", "tag_term1"), new Tag("tag_scheme2", "tag_term2")));
			List<BaseCodingDt> secList = ResourceMetadataKeyEnum.SECURITY_LABELS.get(p1);
			Set<String> secListValues = new HashSet<String>();
			for (BaseCodingDt next : secList) {
				secListValues.add(next.getSystemElement().getValue() + "|" + next.getCodeElement().getValue());
			}
			assertThat(secListValues, containsInAnyOrder("sec_scheme1|sec_term1", "sec_scheme2|sec_term2"));
			List<IdDt> profileList = ResourceMetadataKeyEnum.PROFILES.get(p1);
			assertThat(profileList, contains(new IdDt("http://foo2"))); // no foo1
		}
	}

	@Test
	public void testUpdateMaintainsSearchParams() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2AAA");
		p1.addName().addFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2AAA");
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		p2.addName().addFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2BBB");
		myPatientDao.create(p2, mySrd).getId();

		Set<Long> ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2AAA")));
		assertEquals(1, ids.size());
		assertThat(ids, contains(p1id.getIdPartAsLong()));

		// Update the name
		p1.getNameFirstRep().getGivenFirstRep().setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		MethodOutcome update2 = myPatientDao.update(p1, mySrd);
		IIdType p1id2 = update2.getId();

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2AAA")));
		assertEquals(0, ids.size());

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2BBB")));
		assertEquals(2, ids.size());

		// Make sure vreads work
		p1 = myPatientDao.read(p1id, mySrd);
		assertEquals("testUpdateMaintainsSearchParamsDstu2AAA", p1.getNameFirstRep().getGivenAsSingleString());

		p1 = myPatientDao.read(p1id2, mySrd);
		assertEquals("testUpdateMaintainsSearchParamsDstu2BBB", p1.getNameFirstRep().getGivenAsSingleString());

	}

	@Test
	public void testUpdateRejectsInvalidTypes() throws InterruptedException {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		Organization p2 = new Organization();
		p2.getNameElement().setValue("testUpdateRejectsInvalidTypes");
		try {
			p2.setId(new IdDt("Organization/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			p2.setId(new IdDt("Patient/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			ourLog.error("Good", e);
		}

	}

	@Test
	public void testDuplicateProfilesIgnored() {
		String name = "testDuplicateProfilesIgnored";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().addFamily(name);

			List<IdDt> tl = new ArrayList<IdDt>();
			tl.add(new IdDt("http://foo/bar"));
			tl.add(new IdDt("http://foo/bar"));
			tl.add(new IdDt("http://foo/bar"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, tl);

			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<IdDt> tl = ResourceMetadataKeyEnum.PROFILES.get(patient);
			assertEquals(1, tl.size());
			assertEquals("http://foo/bar", tl.get(0).getValue());
		}

	}

	@Test
	public void testUpdateModifiesProfiles() {
		String name = "testUpdateModifiesProfiles";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().addFamily(name);

			List<IdDt> tl = new ArrayList<IdDt>();
			tl.add(new IdDt("http://foo/bar"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, tl);

			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<IdDt> tl = ResourceMetadataKeyEnum.PROFILES.get(patient);
			assertEquals(1, tl.size());
			assertEquals("http://foo/bar", tl.get(0).getValue());
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id);
			patient.addName().addFamily(name);

			List<IdDt> tl = new ArrayList<IdDt>();
			tl.add(new IdDt("http://foo/baz"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, tl);

			id = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<IdDt> tl = ResourceMetadataKeyEnum.PROFILES.get(patient);
			assertEquals(1, tl.size());
			assertEquals("http://foo/baz", tl.get(0).getValue());
		}

	}

	@Test
	public void testUpdateUnknownNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/9999999999999999");
		try {
			myPatientDao.update(p, mySrd);
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
			myPatientDao.update(p, mySrd);
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
			myPatientDao.update(p, mySrd);
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
		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertEquals("123abc", id.getIdPart());
		assertEquals("1", id.getVersionIdPart());

		p = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);
		assertEquals("Patient/123abc", p.getId().toUnqualifiedVersionless().getValue());
		assertEquals("Hello", p.getName().get(0).getFamily().get(0).getValue());

	}

}
