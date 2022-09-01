package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class FhirResourceDaoR4UpdateTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4UpdateTest.class);

	@AfterEach
	public void afterResetDao() {
		myDaoConfig.setResourceMetaCountHardLimit(new DaoConfig().getResourceMetaCountHardLimit());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
		myDaoConfig.setResourceServerIdStrategy(new DaoConfig().getResourceServerIdStrategy());
		myDaoConfig.setResourceClientIdStrategy(new DaoConfig().getResourceClientIdStrategy());
	}

	@BeforeEach
	public void before() {
		myInterceptorRegistry.registerInterceptor(myInterceptor);
	}

	@Test
	public void testCreateWithClientAssignedId_CheckDisabledMode_AlreadyExists() {
		when(mySrd.getHeader(eq(JpaConstants.HEADER_UPSERT_EXISTENCE_CHECK))).thenReturn(JpaConstants.HEADER_UPSERT_EXISTENCE_CHECK_DISABLED);

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("123");
			return myPatientDao.update(p, mySrd).getId().toUnqualified();
		});
		try {
			runInTransaction(() -> {
				Patient p = new Patient();
				p.setId("AAA");
				p.getMaritalStatus().setText("123");
				return myPatientDao.update(p, mySrd).getId().toUnqualified();
			});
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), containsString("It can also happen when a request disables the Upsert Existence Check."));
		}
	}


	@Test
	public void testCreateAndUpdateWithoutRequest() {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		IIdType id = myPatientDao.create(p).getId().toUnqualified();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		p.setActive(true);
		IIdType id2 = myPatientDao.create(p, "Patient?identifier=urn:system|" + methodName + "2").getId().toUnqualified();
		assertEquals(id.getValue(), id2.getValue());

		p = new Patient();
		p.setId(id);
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		p.setActive(false);
		myPatientDao.update(p).getId();

		p.setActive(true);
		id2 = myPatientDao.update(p, "Patient?identifier=urn:system|" + methodName + "2").getId().toUnqualified();
		assertEquals(id.getIdPart(), id2.getIdPart());
		assertEquals("3", id2.getVersionIdPart());

		Patient newPatient = myPatientDao.read(id);
		assertEquals("1", newPatient.getIdElement().getVersionIdPart());

		newPatient = myPatientDao.read(id.toVersionless());
		assertEquals("3", newPatient.getIdElement().getVersionIdPart());

		myPatientDao.delete(id.toVersionless());

		try {
			myPatientDao.read(id.toVersionless());
			fail();
		} catch (ResourceGoneException e) {
			// nothing
		}

	}

	@Test
	public void testUpdateConditionalOnEmailParameterWithPlusSymbol() {
		IBundleProvider outcome;

		myCaptureQueriesListener.clear();
		Patient p = new Patient();
		p.addTelecom()
			.setSystem(ContactPoint.ContactPointSystem.EMAIL)
			.setValue("help-im+a@bug.com");
		myPatientDao.update(p, "Patient?email=help-im+a@bug.com");
		myCaptureQueriesListener.logSelectQueries();

		outcome = myPatientDao.search(SearchParameterMap.newSynchronous());
		assertEquals(1, outcome.sizeOrThrowNpe());

		p = new Patient();
		p.addTelecom()
			.setSystem(ContactPoint.ContactPointSystem.EMAIL)
			.setValue("help-im+a@bug.com");
		myPatientDao.update(p, "Patient?email=help-im+a@bug.com");

		outcome = myPatientDao.search(SearchParameterMap.newSynchronous());
		assertEquals(1, outcome.sizeOrThrowNpe());

	}

	@Test
	public void testUpdateConditionalOnEmailParameterWithPlusSymbolCorrectlyEscaped() {
		IBundleProvider outcome;

		myCaptureQueriesListener.clear();
		Patient p = new Patient();
		p.addTelecom()
			.setSystem(ContactPoint.ContactPointSystem.EMAIL)
			.setValue("help-im+a@bug.com");
		myPatientDao.update(p, "Patient?email=help-im%2Ba@bug.com");
		myCaptureQueriesListener.logSelectQueries();

		outcome = myPatientDao.search(SearchParameterMap.newSynchronous());
		assertEquals(1, outcome.sizeOrThrowNpe());

		p = new Patient();
		p.addTelecom()
			.setSystem(ContactPoint.ContactPointSystem.EMAIL)
			.setValue("help-im+a@bug.com");
		myPatientDao.update(p, "Patient?email=help-im%2Ba@bug.com");

		outcome = myPatientDao.search(SearchParameterMap.newSynchronous());
		assertEquals(1, outcome.sizeOrThrowNpe());

	}


	/**
	 * Just in case any hash values are missing
	 */
	@Test
	public void testCreateAndUpdateStringAndTokenWhereHashesAreNull() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("sys1").setValue("val1");
		p.addName().setFamily("FAMILY1");
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			myEntityManager.createQuery("UPDATE ResourceIndexedSearchParamString s SET s.myHashIdentity = null").executeUpdate();
			myEntityManager.createQuery("UPDATE ResourceIndexedSearchParamString s SET s.myHashExact = null").executeUpdate();
			myEntityManager.createQuery("UPDATE ResourceIndexedSearchParamString s SET s.myHashNormalizedPrefix = null").executeUpdate();
			myEntityManager.createQuery("UPDATE ResourceIndexedSearchParamToken s SET s.myHashIdentity = null").executeUpdate();
			myEntityManager.createQuery("UPDATE ResourceIndexedSearchParamToken s SET s.myHashSystem = null").executeUpdate();
			myEntityManager.createQuery("UPDATE ResourceIndexedSearchParamToken s SET s.myHashValue = null").executeUpdate();
			myEntityManager.createQuery("UPDATE ResourceIndexedSearchParamToken s SET s.myHashSystemAndValue = null").executeUpdate();
		});

		p = new Patient();
		p.setId(id);
		p.addIdentifier().setSystem("sys2").setValue("val2");
		p.addName().setFamily("FAMILY2");
		myPatientDao.update(p);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_FAMILY, new StringParam("FAMILY2"));
		Patient newPatient = (Patient) myPatientDao.search(map).getResources(0, 1).get(0);
		assertEquals("FAMILY2", newPatient.getName().get(0).getFamily());
	}

	@Test
	public void testUpdateNotModifiedDoesNotAffectDates() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		String createTime = runInTransaction(() -> {
			List<ResourceTable> allResources = myResourceTableDao.findAll();
			assertEquals(1, allResources.size());
			ResourceTable resourceTable = allResources.get(0);

			List<ResourceHistoryTable> allHistory = myResourceHistoryTableDao.findAll();
			assertEquals(1, allHistory.size());
			ResourceHistoryTable historyTable = allHistory.get(0);

			assertEquals(resourceTable.getUpdated().getValueAsString(), historyTable.getUpdated().getValueAsString());
			return resourceTable.getUpdated().getValueAsString();
		});

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.update(p).getResource();
		});

		runInTransaction(() -> {
			List<ResourceTable> allResources = myResourceTableDao.findAll();
			assertEquals(1, allResources.size());
			ResourceTable resourceTable = allResources.get(0);

			List<ResourceHistoryTable> allHistory = myResourceHistoryTableDao.findAll();
			assertEquals(1, allHistory.size());
			ResourceHistoryTable historyTable = allHistory.get(0);

			assertEquals(createTime, historyTable.getUpdated().getValueAsString());
			assertEquals(createTime, resourceTable.getUpdated().getValueAsString());
		});

	}

	@Test
	public void testDuplicateProfilesIgnored() {
		String name = "testDuplicateProfilesIgnored";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);

			List<CanonicalType> tl = new ArrayList<>();
			tl.add(new CanonicalType("http://foo/bar"));
			tl.add(new CanonicalType("http://foo/bar"));
			tl.add(new CanonicalType("http://foo/bar"));
			patient.getMeta().getProfile().addAll(tl);

			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			myCaptureQueriesListener.clear();
			Patient patient = myPatientDao.read(id, mySrd);
			myCaptureQueriesListener.logAllQueriesForCurrentThread();
			List<CanonicalType> tl = patient.getMeta().getProfile();
			assertEquals(1, tl.size());
			assertEquals("http://foo/bar", tl.get(0).getValue());
		}

	}

	@Test
	public void testDuplicateTagsOnAddTagsIgnored() {
		IIdType id;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		Meta meta = new Meta();
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val1");
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val2");
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val3");
		myPatientDao.metaAddOperation(id, meta, null);

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertEquals(1, tl.size());
			assertEquals("http://foo", tl.get(0).getSystem());
			assertEquals("bar", tl.get(0).getCode());
		}

	}

	@Test
	public void testDuplicateTagsOnUpdateIgnored() {
		IIdType id;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			Patient patient = new Patient();
			patient.setId(id.getValue());
			patient.setActive(true);
			patient.getMeta().addTag().setSystem("http://foo").setCode("bar").setDisplay("Val1");
			patient.getMeta().addTag().setSystem("http://foo").setCode("bar").setDisplay("Val2");
			patient.getMeta().addTag().setSystem("http://foo").setCode("bar").setDisplay("Val3");
			myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read on second version
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertEquals(1, tl.size());
			assertEquals("http://foo", tl.get(0).getSystem());
			assertEquals("bar", tl.get(0).getCode());
		}

		// Do a read on first version
		{
			Patient patient = myPatientDao.read(id.withVersion("1"), mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertEquals(0, tl.size());
		}

		Meta meta = new Meta();
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val1");
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val2");
		meta.addTag().setSystem("http://foo").setCode("bar").setDisplay("Val3");
		myPatientDao.metaAddOperation(id.withVersion("1"), meta, null);

		// Do a read on first version
		{
			Patient patient = myPatientDao.read(id.withVersion("1"), mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertEquals(1, tl.size());
			assertEquals("http://foo", tl.get(0).getSystem());
			assertEquals("bar", tl.get(0).getCode());
		}

	}

	@Test
	public void testHardMetaCapIsEnforcedOnCreate() {
		myDaoConfig.setResourceMetaCountHardLimit(3);

		IIdType id;
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setSystem("http://foo").setCode("1");
			patient.getMeta().addTag().setSystem("http://foo").setCode("2");
			patient.getMeta().addTag().setSystem("http://foo").setCode("3");
			patient.getMeta().addTag().setSystem("http://foo").setCode("4");
			patient.setActive(true);
			try {
				id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
				fail();
			} catch (UnprocessableEntityException e) {
				assertEquals(Msg.code(932) + "Resource contains 4 meta entries (tag/profile/security label), maximum is 3", e.getMessage());
			}
		}
	}

	@Test
	public void testHardMetaCapIsEnforcedOnMetaAdd() {
		myDaoConfig.setResourceMetaCountHardLimit(3);

		IIdType id;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			Meta meta = new Meta();
			meta.addTag().setSystem("http://foo").setCode("1");
			meta.addTag().setSystem("http://foo").setCode("2");
			meta.addTag().setSystem("http://foo").setCode("3");
			meta.addTag().setSystem("http://foo").setCode("4");
			try {
				myPatientDao.metaAddOperation(id, meta, null);
				fail();
			} catch (UnprocessableEntityException e) {
				assertEquals(Msg.code(932) + "Resource contains 4 meta entries (tag/profile/security label), maximum is 3", e.getMessage());
			}

		}
	}

	@Test
	public void testMultipleUpdatesWithNoChangesDoesNotResultInAnUpdateForDiscreteUpdates() {

		// First time
		Patient p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		String id = myPatientDao.update(p).getId().getValue();
		assertThat(id, endsWith("Patient/A/_history/1"));

		// Second time should not result in an update
		p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		id = myPatientDao.update(p).getId().getValue();
		assertThat(id, endsWith("Patient/A/_history/1"));

		// And third time should not result in an update
		p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		id = myPatientDao.update(p).getId().getValue();
		assertThat(id, endsWith("Patient/A/_history/1"));

		myPatientDao.read(new IdType("Patient/A"));
		myPatientDao.read(new IdType("Patient/A/_history/1"));
		try {
			myPatientDao.read(new IdType("Patient/A/_history/2"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
		try {
			myPatientDao.read(new IdType("Patient/A/_history/3"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		// Create one more
		p = new Patient();
		p.setActive(false);
		p.setId("Patient/A");
		id = myPatientDao.update(p).getId().getValue();
		assertThat(id, endsWith("Patient/A/_history/2"));

	}

	@Test
	public void testReCreateMatchResource() {

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo");
		IIdType id = myCodeSystemDao.create(codeSystem).getId().toUnqualifiedVersionless();

		myCodeSystemDao.delete(id);

		codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo");
		myCodeSystemDao.update(codeSystem, "Patient?name=FAM").getId().toUnqualifiedVersionless();

	}

	@Test
	public void testUpdateAndGetHistoryResource() throws InterruptedException {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		assertNotNull(outcome.getId());
		assertFalse(outcome.getId().isEmpty());

		assertEquals("1", outcome.getId().getVersionIdPart());

		TestUtil.sleepOneClick();

		Date now = new Date();

		Patient retrieved = myPatientDao.read(outcome.getId(), mySrd);
		InstantType updated = TestUtil.getTimestamp(retrieved);
		assertTrue(updated.before(now));

		TestUtil.sleepOneClick();

		reset(myInterceptor);
		retrieved.getIdentifier().get(0).setValue("002");
		MethodOutcome outcome2 = myPatientDao.update(retrieved, mySrd);
		assertEquals(outcome.getId().getIdPart(), outcome2.getId().getIdPart());
		assertNotEquals(outcome.getId().getVersionIdPart(), outcome2.getId().getVersionIdPart());
		assertEquals("2", outcome2.getId().getVersionIdPart());

		TestUtil.sleepOneClick();
		Date now2 = new Date();

		Patient retrieved2 = myPatientDao.read(outcome.getId().toVersionless(), mySrd);

		assertEquals("2", retrieved2.getIdElement().getVersionIdPart());
		assertEquals("002", retrieved2.getIdentifier().get(0).getValue());
		InstantType updated2 = TestUtil.getTimestamp(retrieved2);
		assertTrue(updated2.after(now));
		assertTrue(updated2.before(now2));

		TestUtil.sleepOneClick();

		/*
		 * Get history
		 */

		IBundleProvider historyBundle = myPatientDao.history(outcome.getId(), null, null, null, mySrd);

		assertEquals(2, historyBundle.size().intValue());

		List<IBaseResource> history = historyBundle.getResources(0, 2);

		ourLog.info("updated : {}", updated.getValueAsString());
		ourLog.info("  * Exp : {}", ((Resource) history.get(1)).getMeta().getLastUpdatedElement().getValueAsString());
		ourLog.info("updated2: {}", updated2.getValueAsString());
		ourLog.info("  * Exp : {}", ((Resource) history.get(0)).getMeta().getLastUpdatedElement().getValueAsString());

		assertEquals("1", history.get(1).getIdElement().getVersionIdPart());
		assertEquals("2", history.get(0).getIdElement().getVersionIdPart());
		assertEquals(updated.getValueAsString(), ((Resource) history.get(1)).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("001", ((Patient) history.get(1)).getIdentifier().get(0).getValue());
		assertEquals(updated2.getValueAsString(), ((Resource) history.get(0)).getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("002", ((Patient) history.get(0)).getIdentifier().get(0).getValue());

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
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);

		myPatientDao.update(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertThat(p.getIdElement().toVersionless().toString(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getIdElement().toVersionless());
		assertNotEquals(id, p.getIdElement());
		assertThat(p.getIdElement().toString(), endsWith("/_history/2"));

	}

	@Test
	public void testUpdateConditionalByLastUpdated() throws Exception {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		myPatientDao.create(p, mySrd).getId();

		InstantDt start = InstantDt.withCurrentTime();
		ourLog.info("First time: {}", start.getValueAsString());
		Thread.sleep(100);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got ID: {}", id);

		Thread.sleep(100);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);

		String matchUrl = "Patient?_lastUpdated=gt" + start.getValueAsString();
		ourLog.info("URL is: {}", matchUrl);
		myPatientDao.update(p, matchUrl, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertThat(p.getIdElement().toVersionless().toString(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getIdElement().toVersionless());
		assertNotEquals(id, p.getIdElement());
		assertThat(p.getIdElement().toString(), endsWith("/_history/2"));

	}

	@Test
	public void testUpdateWithoutId() {

		Patient p = new Patient();
		try {
			myPatientDao.update(p);
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(987) + "Can not update resource of type Patient as it has no ID", e.getMessage());
		}
	}

	@Test
	public void testUpdateConditionalByLastUpdatedWithWrongTimezone() throws Exception {
		TimeZone def = TimeZone.getDefault();
		try {
			TimeZone.setDefault(TimeZone.getTimeZone("GMT-0:00"));
			String methodName = "testUpdateByUrl";

			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
			myPatientDao.create(p, mySrd).getId();

			InstantDt start = InstantDt.withCurrentTime();
			Thread.sleep(100);

			p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue(methodName);
			IIdType id = myPatientDao.create(p, mySrd).getId();
			ourLog.info("Created patient, got it: {}", id);

			Thread.sleep(100);

			p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue(methodName);
			p.addName().setFamily("Hello");
			p.setId("Patient/" + methodName);

			myPatientDao.update(p, "Patient?_lastUpdated=gt" + start.getValueAsString(), mySrd);

			p = myPatientDao.read(id.toVersionless(), mySrd);
			assertThat(p.getIdElement().toVersionless().toString(), not(containsString("test")));
			assertEquals(id.toVersionless(), p.getIdElement().toVersionless());
			assertNotEquals(id, p.getIdElement());
			assertThat(p.getIdElement().toString(), endsWith("/_history/2"));
		} finally {
			TimeZone.setDefault(def);
		}
	}

	@Test
	public void testUpdateCreatesTextualIdIfItDoesntAlreadyExist() {
		Patient p = new Patient();
		String methodName = "testUpdateCreatesTextualIdIfItDoesntAlreadyExist";
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().setFamily("Hello");
		p.setId("Patient/" + methodName);

		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertEquals("Patient/" + methodName, id.toUnqualifiedVersionless().getValue());

		p = myPatientDao.read(id, mySrd);
		assertEquals(methodName, p.getIdentifier().get(0).getValue());
	}

	@Test
	public void testUpdateDoesntFailForUnknownIdWithNumberThenText() {
		String methodName = "testUpdateFailsForUnknownIdWithNumberThenText";
		Patient p = new Patient();
		p.setId("0" + methodName);
		p.addName().setFamily(methodName);

		myPatientDao.update(p, mySrd);
	}

	@Test
	@Disabled
	public void testUpdateIgnoresIdenticalVersions() {
		String methodName = "testUpdateIgnoresIdenticalVersions";

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue(methodName);
		p1.addName().setFamily("Tester").addGiven(methodName);
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		IIdType p1id2 = myPatientDao.update(p1, mySrd).getId();
		assertEquals(p1id.getValue(), p1id2.getValue());

		p1.addName().addGiven("NewGiven");
		IIdType p1id3 = myPatientDao.update(p1, mySrd).getId();
		assertNotEquals(p1id.getValue(), p1id3.getValue());

	}

	@Test
	public void testUpdateMaintainsSearchParams() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2AAA");
		p1.addName().setFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2AAA");
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		p2.addName().setFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2BBB");
		myPatientDao.create(p2, mySrd).getId();

		List<ResourcePersistentId> ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2AAA")), null);
		assertEquals(1, ids.size());
		assertThat(ResourcePersistentId.toLongList(ids), contains(p1id.getIdPartAsLong()));

		// Update the name
		p1.getName().get(0).getGiven().get(0).setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		MethodOutcome update2 = myPatientDao.update(p1, mySrd);
		IIdType p1id2 = update2.getId();

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2AAA")), null);
		assertEquals(0, ids.size());

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2BBB")), null);
		assertEquals(2, ids.size());

		// Make sure vreads work
		p1 = myPatientDao.read(p1id, mySrd);
		assertEquals("testUpdateMaintainsSearchParamsDstu2AAA", p1.getName().get(0).getGivenAsSingleString());

		p1 = myPatientDao.read(p1id2, mySrd);
		assertEquals("testUpdateMaintainsSearchParamsDstu2BBB", p1.getName().get(0).getGivenAsSingleString());

	}

	/**
	 * Per the spec, update should preserve tags and security labels but not profiles
	 */
	@Test
	public void testUpdateMaintainsTagsAndSecurityLabels() {
		String methodName = "testUpdateMaintainsTagsAndSecurityLabels";

		IIdType p1id;
		{
			Patient p1 = new Patient();
			p1.addName().setFamily(methodName);

			p1.getMeta().addTag("tag_scheme1", "tag_term1", null);
			p1.getMeta().addSecurity("sec_scheme1", "sec_term1", null);
			p1.getMeta().addProfile("http://foo1");

			p1id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p1 = new Patient();
			p1.setId(p1id.getValue());
			p1.addName().setFamily(methodName);

			p1.getMeta().addTag("tag_scheme2", "tag_term2", null);
			p1.getMeta().addSecurity("sec_scheme2", "sec_term2", null);
			p1.getMeta().addProfile("http://foo2");

			myPatientDao.update(p1, mySrd);
		}
		{
			Patient p1 = myPatientDao.read(p1id, mySrd);
			List<Coding> tagList = p1.getMeta().getTag();
			Set<String> secListValues = new HashSet<>();
			for (Coding next : tagList) {
				secListValues.add(next.getSystemElement().getValue() + "|" + next.getCodeElement().getValue());
			}
			assertThat(secListValues, containsInAnyOrder("tag_scheme1|tag_term1", "tag_scheme2|tag_term2"));
			List<Coding> secList = p1.getMeta().getSecurity();
			secListValues = new HashSet<>();
			for (Coding next : secList) {
				secListValues.add(next.getSystemElement().getValue() + "|" + next.getCodeElement().getValue());
			}
			assertThat(secListValues, containsInAnyOrder("sec_scheme1|sec_term1", "sec_scheme2|sec_term2"));
			List<CanonicalType> profileList = p1.getMeta().getProfile();
			assertEquals(1, profileList.size());
			assertEquals("http://foo2", profileList.get(0).getValueAsString()); // no foo1
		}
	}

	@Test
	public void testUpdateModifiesProfiles() {
		String name = "testUpdateModifiesProfiles";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);

			List<CanonicalType> tl = new ArrayList<>();
			tl.add(new CanonicalType("http://foo/bar"));
			patient.getMeta().getProfile().addAll(tl);

			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<CanonicalType> tl = patient.getMeta().getProfile();
			assertEquals(1, tl.size());
			assertEquals("http://foo/bar", tl.get(0).getValue());
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id);
			patient.addName().setFamily(name);

			List<CanonicalType> tl = new ArrayList<>();
			tl.add(new CanonicalType("http://foo/baz"));
			patient.getMeta().getProfile().clear();
			patient.getMeta().getProfile().addAll(tl);

			id = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<CanonicalType> tl = patient.getMeta().getProfile();
			assertEquals(1, tl.size());
			assertEquals("http://foo/baz", tl.get(0).getValue());
		}

	}

	@Test
	public void testUpdateRejectsInvalidTypes() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().setFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		Organization p2 = new Organization();
		p2.getNameElement().setValue("testUpdateRejectsInvalidTypes");
		try {
			p2.setId(new IdType("Organization/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			p2.setId(new IdType("Patient/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.error("Good", e);
		}

	}

	@Test
	public void testUpdateUnknownNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
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
		p.addName().setFamily("Hello");
		p.setId("Patient/123:456");
		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(521) + "Can not process entity with ID[123:456], this is not a valid FHIR ID", e.getMessage());
		}
	}

	@Test
	public void testUpdateWithNoChangeDetectionDisabledUpdateUnchanged() {
		myDaoConfig.setSuppressUpdatesWithNoChange(false);

		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertNotEquals(id1.getValue(), id2.getValue());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateTagAdded() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Update
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setCode("CODE");
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertNotEquals(id1.getValue(), id2.getValue());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateTagMetaRemoved() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setCode("CODE");
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		Meta meta = new Meta();
		meta.addTag().setCode("CODE");
		myPatientDao.metaDeleteOperation(id1, meta, null);

		meta = myPatientDao.metaGetOperation(Meta.class, id1, null);
		assertEquals(0, meta.getTag().size());

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertEquals(id1.getValue(), id2.getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertEquals(0, meta.getTag().size());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateTagNoChange() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Add tag
		Meta meta = new Meta();
		meta.addTag().setCode("CODE");
		myPatientDao.metaAddOperation(id1, meta, null);

		// Update with tag
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setCode("CODE");
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertEquals(id1.getValue(), id2.getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertEquals(1, meta.getTag().size());
		assertEquals("CODE", meta.getTag().get(0).getCode());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateTagRemoved() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setCode("CODE");
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertEquals(id1.getValue(), id2.getValue());

		Meta meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertEquals(1, meta.getTag().size());
		assertEquals("CODE", meta.getTag().get(0).getCode());
	}

	@Test
	public void testUpdateWithNoChangeDetectionUpdateUnchanged() {
		String name = "testUpdateUnchanged";
		IIdType id1, id2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualified();
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertEquals(id1.getValue(), id2.getValue());
	}

	@Test
	public void testUpdateWithNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
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
		p.addName().setFamily("Hello");
		p.setId("Patient/123abc");
		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertEquals("123abc", id.getIdPart());
		assertEquals("1", id.getVersionIdPart());

		p = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);
		assertEquals("Patient/123abc", p.getIdElement().toUnqualifiedVersionless().getValue());
		assertEquals("Hello", p.getName().get(0).getFamily());

	}

	@Test
	public void testUpdateWithUuidServerResourceStrategy_ClientIdNotAllowed() {
		myDaoConfig.setResourceServerIdStrategy(DaoConfig.IdStrategyEnum.UUID);
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.NOT_ALLOWED);

		Patient p = new Patient();
		p.setId(UUID.randomUUID().toString());
		p.addName().setFamily("FAM");
		try {
			myPatientDao.update(p);
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), matchesPattern(Msg.code(959) + "No resource exists on this server resource with ID.*, and client-assigned IDs are not enabled."));
		}

	}



}
