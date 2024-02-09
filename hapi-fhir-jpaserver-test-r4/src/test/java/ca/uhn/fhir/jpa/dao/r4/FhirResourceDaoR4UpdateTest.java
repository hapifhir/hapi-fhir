package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IPointcut;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.fail;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class FhirResourceDaoR4UpdateTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4UpdateTest.class);

	@AfterEach
	public void afterResetDao() {
		myStorageSettings.setResourceMetaCountHardLimit(new JpaStorageSettings().getResourceMetaCountHardLimit());
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
		myStorageSettings.setResourceServerIdStrategy(new JpaStorageSettings().getResourceServerIdStrategy());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.SEQUENTIAL_NUMERIC);
		myInterceptorRegistry.unregisterAllAnonymousInterceptors();
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
			fail("");
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("It can also happen when a request disables the Upsert Existence Check.");
		}
	}


	@Test
	public void testTagCollision() {
		TagDefinition def = new TagDefinition();
		def.setTagType(TagTypeEnum.TAG);
		def.setSystem("system");
		def.setCode("coding");
		def.setDisplay("display");
		def.setUserSelected(null);


		Patient p = new Patient();
		p.getMeta().addTag("system", "coding", "display");

		myMemoryCacheService.invalidateAllCaches();
		myPatientDao.create(p, mySrd);
		//inject conflicting.
		myTagDefinitionDao.saveAndFlush(def);
		myMemoryCacheService.invalidateAllCaches();

		myPatientDao.create(p, mySrd);
		myMemoryCacheService.invalidateAllCaches();

		myPatientDao.create(p, mySrd);

	}


	@Test
	public void testCreateAndUpdateWithoutRequest() {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualified();

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		p.setActive(true);
		IIdType id2 = myPatientDao.create(p, "Patient?identifier=urn:system|" + methodName + "2", mySrd).getId().toUnqualified();
		assertThat(id2.getValue()).isEqualTo(id.getValue());

		p = new Patient();
		p.setId(id);
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		p.setActive(false);
		myPatientDao.update(p, mySrd);

		p.setActive(true);
		id2 = myPatientDao.update(p, "Patient?identifier=urn:system|" + methodName + "2", mySrd).getId().toUnqualified();
		assertThat(id2.getIdPart()).isEqualTo(id.getIdPart());
		assertThat(id2.getVersionIdPart()).isEqualTo("3");

		Patient newPatient = myPatientDao.read(id, mySrd);
		assertThat(newPatient.getIdElement().getVersionIdPart()).isEqualTo("1");

		newPatient = myPatientDao.read(id.toVersionless(), mySrd);
		assertThat(newPatient.getIdElement().getVersionIdPart()).isEqualTo("3");

		myPatientDao.delete(id.toVersionless(), mySrd);

		try {
			myPatientDao.read(id.toVersionless(), mySrd);
			fail("");
		} catch (ResourceGoneException e) {
			// nothing
		}

	}

	@Nested
	public class TestConditionalResourceMustMatchConditionForUpdate {

		private final Patient myPatient = new Patient();

		@BeforeEach
		void setUp() {
			myPatient.setId("existing-patient");
			myPatient.addIdentifier().setSystem("http://kookaburra.text/id").setValue("kookaburra1");
			myPatientDao.update(myPatient, mySrd);
		}

		@AfterEach
		void tearDown() {
			myStorageSettings.setPreventInvalidatingConditionalMatchCriteria(false);
		}

		@Nested
		public class ForFirstVersion {

			// For first version must fail validation no matter the state of PreventInvalidatingConditionalMatchCriteria

			@Test
			public void withPreventInvalidatingConditionalMatchCriteria_true_mustThrow() {
				//Note this should always default to false to preserve existing behaviour
				myStorageSettings.setPreventInvalidatingConditionalMatchCriteria(true);

				Patient p2 = new Patient();
				p2.addIdentifier().setSystem("http://kookaburra.text/id").setValue("kookaburra1");

				InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
					() -> myPatientDao.update(p2,
						"Patient?identifier=http://kookaburra.text/id|kookaburra2", mySrd));
				assertThat(thrown.getMessage()).endsWith("Failed to process conditional create. The supplied resource did not satisfy the conditional URL.");
			}

			@Test
			public void withPreventInvalidatingConditionalMatchCriteria_false_mustThrow() {
				//Note this should always default to false to preserve existing behaviour
				assertThat(myStorageSettings.isPreventInvalidatingConditionalMatchCriteria()).isFalse();

				Patient p2 = new Patient();
				p2.addIdentifier().setSystem("http://kookaburra.text/id").setValue("kookaburra1");

				InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
					() -> myPatientDao.update(p2,
						"Patient?identifier=http://kookaburra.text/id|kookaburra2", mySrd));
				assertThat(thrown.getMessage()).endsWith("Failed to process conditional create. The supplied resource did not satisfy the conditional URL.");
			}

		}

		@Nested
		public class ForOtherThanFirstVersion {

			// For other than first version must fail validation only when PreventInvalidatingConditionalMatchCriteria is true

			@Test
			public void withPreventInvalidatingConditionalMatchCriteria_false_mustWork() {
				//Note this should always default to false to preserve existing behaviour
				assertThat(myStorageSettings.isPreventInvalidatingConditionalMatchCriteria()).isFalse();

				Patient p2 = new Patient();
				p2.addIdentifier().setSystem("http://kookaburra.text/id").setValue("kookaburra2");

				myPatientDao.update(p2, "Patient?identifier=http://kookaburra.text/id|kookaburra1", mySrd);
			}

			@Test
			public void withPreventInvalidatingConditionalMatchCriteria_true_mustThrow() {
				myStorageSettings.setPreventInvalidatingConditionalMatchCriteria(true); //Note this should always default to false to preserve existing behaviour

				Patient p2 = new Patient();
				p2.addIdentifier().setSystem("http://kookaburra.text/id").setValue("kookaburra2");

				InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
					() -> myPatientDao.update(p2,
						"Patient?identifier=http://kookaburra.text/id|kookaburra1", mySrd));
				assertThat(thrown.getMessage()).endsWith("Failed to process conditional update. The supplied resource did not satisfy the conditional URL.");
			}
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
		myPatientDao.update(p, "Patient?email=help-im+a@bug.com", mySrd);
		myCaptureQueriesListener.logSelectQueries();

		outcome = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);
		assertThat(outcome.sizeOrThrowNpe()).isEqualTo(1);

		p = new Patient();
		p.addTelecom()
			.setSystem(ContactPoint.ContactPointSystem.EMAIL)
			.setValue("help-im+a@bug.com");
		myPatientDao.update(p, "Patient?email=help-im+a@bug.com", mySrd);

		outcome = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);
		assertThat(outcome.sizeOrThrowNpe()).isEqualTo(1);
	}

	@Test
	public void testUpdateConditionalOnEmailParameterWithPlusSymbolCorrectlyEscaped() {
		IBundleProvider outcome;

		myCaptureQueriesListener.clear();
		Patient p = new Patient();
		p.addTelecom()
			.setSystem(ContactPoint.ContactPointSystem.EMAIL)
			.setValue("help-im+a@bug.com");
		myPatientDao.update(p, "Patient?email=help-im%2Ba@bug.com", mySrd);
		myCaptureQueriesListener.logSelectQueries();

		outcome = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);
		assertThat(outcome.sizeOrThrowNpe()).isEqualTo(1);

		p = new Patient();
		p.addTelecom()
			.setSystem(ContactPoint.ContactPointSystem.EMAIL)
			.setValue("help-im+a@bug.com");
		myPatientDao.update(p, "Patient?email=help-im%2Ba@bug.com", mySrd);

		outcome = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);
		assertThat(outcome.sizeOrThrowNpe()).isEqualTo(1);

	}


	/**
	 * Just in case any hash values are missing
	 */
	@Test
	public void testCreateAndUpdateStringAndTokenWhereHashesAreNull() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("sys1").setValue("val1");
		p.addName().setFamily("FAMILY1");
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

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
		myPatientDao.update(p, mySrd);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_FAMILY, new StringParam("FAMILY2"));
		Patient newPatient = (Patient) myPatientDao.search(map, mySrd).getResources(0, 1).get(0);
		assertThat(newPatient.getName().get(0).getFamily()).isEqualTo("FAMILY2");
	}

	@Test
	public void testUpdateNotModifiedDoesNotAffectDates() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p, mySrd).getId().toUnqualified();
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
			myPatientDao.update(p, mySrd);
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
			assertThat(tl).hasSize(1);
			assertThat(tl.get(0).getValue()).isEqualTo("http://foo/bar");
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
			assertThat(tl).hasSize(1);
			assertThat(tl.get(0).getSystem()).isEqualTo("http://foo");
			assertThat(tl.get(0).getCode()).isEqualTo("bar");
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
			assertThat(tl).hasSize(1);
			assertThat(tl.get(0).getSystem()).isEqualTo("http://foo");
			assertThat(tl.get(0).getCode()).isEqualTo("bar");
		}

		// Do a read on first version
		{
			Patient patient = myPatientDao.read(id.withVersion("1"), mySrd);
			List<Coding> tl = patient.getMeta().getTag();
			assertThat(tl).isEmpty();
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
			assertThat(tl).hasSize(1);
			assertThat(tl.get(0).getSystem()).isEqualTo("http://foo");
			assertThat(tl.get(0).getCode()).isEqualTo("bar");
		}

	}

	@Test
	public void testHardMetaCapIsEnforcedOnCreate() {
		myStorageSettings.setResourceMetaCountHardLimit(3);

		{
			Patient patient = new Patient();
			patient.getMeta().addTag().setSystem("http://foo").setCode("1");
			patient.getMeta().addTag().setSystem("http://foo").setCode("2");
			patient.getMeta().addTag().setSystem("http://foo").setCode("3");
			patient.getMeta().addTag().setSystem("http://foo").setCode("4");
			patient.setActive(true);
			try {
				myPatientDao.create(patient, mySrd);
				fail("");
			} catch (UnprocessableEntityException e) {
				assertThat(e.getMessage()).isEqualTo(Msg.code(932) + "Resource contains 4 meta entries (tag/profile/security label), maximum is 3");
			}
		}
	}

	@Test
	public void testHardMetaCapIsEnforcedOnMetaAdd() {
		myStorageSettings.setResourceMetaCountHardLimit(3);

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
				fail("");
			} catch (UnprocessableEntityException e) {
				assertThat(e.getMessage()).isEqualTo(Msg.code(932) + "Resource contains 4 meta entries (tag/profile/security label), maximum is 3");
			}

		}
	}

	@Test
	public void testMultipleUpdatesWithNoChangesDoesNotResultInAnUpdateForDiscreteUpdates() {

		// First time
		Patient p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		String id = myPatientDao.update(p, mySrd).getId().getValue();
		assertThat(id).endsWith("Patient/A/_history/1");

		// Second time should not result in an update
		p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		id = myPatientDao.update(p, mySrd).getId().getValue();
		assertThat(id).endsWith("Patient/A/_history/1");

		// And third time should not result in an update
		p = new Patient();
		p.setActive(true);
		p.setId("Patient/A");
		id = myPatientDao.update(p, mySrd).getId().getValue();
		assertThat(id).endsWith("Patient/A/_history/1");

		myPatientDao.read(new IdType("Patient/A"), mySrd);
		myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		try {
			myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
			fail("");
		} catch (ResourceNotFoundException e) {
			// good
		}
		try {
			myPatientDao.read(new IdType("Patient/A/_history/3"), mySrd);
			fail("");
		} catch (ResourceNotFoundException e) {
			// good
		}

		// Create one more
		p = new Patient();
		p.setActive(false);
		p.setId("Patient/A");
		id = myPatientDao.update(p, mySrd).getId().getValue();
		assertThat(id).endsWith("Patient/A/_history/2");

	}

	@Test
	public void testUpdateAndGetHistoryResource() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("001");
		patient.addName().setFamily("Tester").addGiven("Joe");

		MethodOutcome outcome = myPatientDao.create(patient, mySrd);
		assertThat(outcome.getId()).isNotNull();
		assertThat(outcome.getId().isEmpty()).isFalse();

		assertThat(outcome.getId().getVersionIdPart()).isEqualTo("1");

		TestUtil.sleepOneClick();

		Date now = new Date();

		Patient retrieved = myPatientDao.read(outcome.getId(), mySrd);
		InstantType updated = TestUtil.getTimestamp(retrieved);
		assertThat(updated.before(now)).isTrue();

		TestUtil.sleepOneClick();

		retrieved.getIdentifier().get(0).setValue("002");
		MethodOutcome outcome2 = myPatientDao.update(retrieved, mySrd);
		assertThat(outcome2.getId().getIdPart()).isEqualTo(outcome.getId().getIdPart());
		assertThat(outcome2.getId().getVersionIdPart()).isNotEqualTo(outcome.getId().getVersionIdPart());
		assertThat(outcome2.getId().getVersionIdPart()).isEqualTo("2");

		TestUtil.sleepOneClick();
		Date now2 = new Date();

		Patient retrieved2 = myPatientDao.read(outcome.getId().toVersionless(), mySrd);

		assertThat(retrieved2.getIdElement().getVersionIdPart()).isEqualTo("2");
		assertThat(retrieved2.getIdentifier().get(0).getValue()).isEqualTo("002");
		InstantType updated2 = TestUtil.getTimestamp(retrieved2);
		assertThat(updated2.after(now)).isTrue();
		assertThat(updated2.before(now2)).isTrue();

		TestUtil.sleepOneClick();

		/*
		 * Get history
		 */

		IBundleProvider historyBundle = myPatientDao.history(outcome.getId(), null, null, null, mySrd);

		assertThat(historyBundle).isNotNull();
		assertThat(Objects.requireNonNull(historyBundle.size()).intValue()).isEqualTo(2);

		List<IBaseResource> history = historyBundle.getResources(0, 2);

		ourLog.info("updated : {}", updated.getValueAsString());
		ourLog.info("  * Exp : {}", ((Resource) history.get(1)).getMeta().getLastUpdatedElement().getValueAsString());
		ourLog.info("updated2: {}", updated2.getValueAsString());
		ourLog.info("  * Exp : {}", ((Resource) history.get(0)).getMeta().getLastUpdatedElement().getValueAsString());

		assertThat(history.get(1).getIdElement().getVersionIdPart()).isEqualTo("1");
		assertThat(history.get(0).getIdElement().getVersionIdPart()).isEqualTo("2");
		assertThat(((Resource) history.get(1)).getMeta().getLastUpdatedElement().getValueAsString()).isEqualTo(updated.getValueAsString());
		assertThat(((Patient) history.get(1)).getIdentifier().get(0).getValue()).isEqualTo("001");
		assertThat(((Resource) history.get(0)).getMeta().getLastUpdatedElement().getValueAsString()).isEqualTo(updated2.getValueAsString());
		assertThat(((Patient) history.get(0)).getIdentifier().get(0).getValue()).isEqualTo("002");

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

		myPatientDao.update(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertThat(p.getIdElement().toVersionless()).isEqualTo(id.toVersionless());
		assertThat(p.getIdElement()).isNotEqualTo(id);
		assertThat(p.getIdElement().toString()).endsWith("/_history/2");

	}

	@Test
	public void testUpdateConditionalByLastUpdated() throws Exception {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		myPatientDao.create(p, mySrd);

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

		String matchUrl = "Patient?_lastUpdated=gt" + start.getValueAsString();
		ourLog.info("URL is: {}", matchUrl);
		myPatientDao.update(p, matchUrl, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertThat(p.getIdElement().toVersionless()).isEqualTo(id.toVersionless());
		assertThat(p.getIdElement()).isNotEqualTo(id);
		assertThat(p.getIdElement().toString()).endsWith("/_history/2");

	}

	@Test
	public void testUpdateConditionalByLastUpdatedWithId() throws Exception {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName + "2");
		myPatientDao.create(p, mySrd);

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

		try {
			myPatientDao.update(p, matchUrl, mySrd);
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("2279");
		}
	}

	@Test
	public void testUpdateResourceCreatedWithConditionalUrl_willRemoveEntryInSearchUrlTable(){
		String identifierCode = "20210427133226.4440+800";
		String matchUrl = "identifier=20210427133226.4440%2B800";
		Observation obs = new Observation();
		obs.addIdentifier().setValue(identifierCode);
		myObservationDao.create(obs, matchUrl, new SystemRequestDetails());
		assertThat(myResourceSearchUrlDao.findAll()).hasSize(1);

		// when
		obs.setStatus(Observation.ObservationStatus.CORRECTED);
		myObservationDao.update(obs, mySrd);

		// then
		assertThat(myResourceSearchUrlDao.findAll()).hasSize(0);

	}

	@Test
	public void testUpdateWithoutId() {

		Patient p = new Patient();
		try {
			myPatientDao.update(p, mySrd);
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(987) + "Can not update resource of type Patient as it has no ID");
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
			myPatientDao.create(p, mySrd);

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

			myPatientDao.update(p, "Patient?_lastUpdated=gt" + start.getValueAsString(), mySrd);

			p = myPatientDao.read(id.toVersionless(), mySrd);
			assertThat(p.getIdElement().toVersionless()).isEqualTo(id.toVersionless());
			assertThat(p.getIdElement()).isNotEqualTo(id);
			assertThat(p.getIdElement().toString()).endsWith("/_history/2");
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
		assertThat(id.toUnqualifiedVersionless().getValue()).isEqualTo("Patient/" + methodName);

		p = myPatientDao.read(id, mySrd);
		assertThat(p.getIdentifier().get(0).getValue()).isEqualTo(methodName);
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
		assertThat(p1id2.getValue()).isEqualTo(p1id.getValue());

		p1.addName().addGiven("NewGiven");
		IIdType p1id3 = myPatientDao.update(p1, mySrd).getId();
		assertThat(p1id3.getValue()).isNotEqualTo(p1id.getValue());

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
		myPatientDao.create(p2, mySrd);

		List<JpaPid> ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2AAA")), null);
		assertThat(ids).hasSize(1);
		assertThat(JpaPid.toLongList(ids)).containsExactly(p1id.getIdPartAsLong());

		// Update the name
		p1.getName().get(0).getGiven().get(0).setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		MethodOutcome update2 = myPatientDao.update(p1, mySrd);
		IIdType p1id2 = update2.getId();

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2AAA")), null);
		assertThat(ids).isEmpty();

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringParam("testUpdateMaintainsSearchParamsDstu2BBB")), null);
		assertThat(ids).hasSize(2);

		// Make sure vreads work
		p1 = myPatientDao.read(p1id, mySrd);
		assertThat(p1.getName().get(0).getGivenAsSingleString()).isEqualTo("testUpdateMaintainsSearchParamsDstu2AAA");

		p1 = myPatientDao.read(p1id2, mySrd);
		assertThat(p1.getName().get(0).getGivenAsSingleString()).isEqualTo("testUpdateMaintainsSearchParamsDstu2BBB");

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
			assertThat(secListValues).containsExactlyInAnyOrder("tag_scheme1|tag_term1", "tag_scheme2|tag_term2");
			List<Coding> secList = p1.getMeta().getSecurity();
			secListValues = new HashSet<>();
			for (Coding next : secList) {
				secListValues.add(next.getSystemElement().getValue() + "|" + next.getCodeElement().getValue());
			}
			assertThat(secListValues).containsExactlyInAnyOrder("sec_scheme1|sec_term1", "sec_scheme2|sec_term2");
			List<CanonicalType> profileList = p1.getMeta().getProfile();
			assertThat(profileList).hasSize(1);
			assertThat(profileList.get(0).getValueAsString()).isEqualTo("http://foo2"); // no foo1
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
			assertThat(tl).hasSize(1);
			assertThat(tl.get(0).getValue()).isEqualTo("http://foo/bar");
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
			assertThat(tl).hasSize(1);
			assertThat(tl.get(0).getValue()).isEqualTo("http://foo/baz");
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
			fail("");
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			p2.setId(new IdType("Patient/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail("");
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("Can not create resource with ID[9999999999999999], no resource with this ID exists and clients may only");
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(521) + "Can not process entity with ID[123:456], this is not a valid FHIR ID");
		}
	}

	@Test
	public void testUpdateWithNoChangeDetectionDisabledUpdateUnchanged() {
		myStorageSettings.setSuppressUpdatesWithNoChange(false);

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

		assertThat(id2.getValue()).isNotEqualTo(id1.getValue());
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

		assertThat(id2.getValue()).isNotEqualTo(id1.getValue());
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
		assertThat(meta.getTag()).isEmpty();

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id1);
			patient.addName().setFamily(name);
			id2 = myPatientDao.update(patient, mySrd).getId().toUnqualified();
		}

		assertThat(id2.getValue()).isEqualTo(id1.getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertThat(meta.getTag()).isEmpty();
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

		assertThat(id2.getValue()).isEqualTo(id1.getValue());

		meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertThat(meta.getTag()).hasSize(1);
		assertThat(meta.getTag().get(0).getCode()).isEqualTo("CODE");
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

		assertThat(id2.getValue()).isEqualTo(id1.getValue());

		Meta meta = myPatientDao.metaGetOperation(Meta.class, id2, null);
		assertThat(meta.getTag()).hasSize(1);
		assertThat(meta.getTag().get(0).getCode()).isEqualTo("CODE");
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

		assertThat(id2.getValue()).isEqualTo(id1.getValue());
	}

	@Test
	public void testUpdateWithNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
		p.setId("Patient/123");
		try {
			myPatientDao.update(p, mySrd);
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).contains("clients may only assign IDs which contain at least one non-numeric");
		}
	}

	@Test
	public void testUpdateWithNumericThenTextIdSucceeds() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().setFamily("Hello");
		p.setId("Patient/123abc");
		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertThat(id.getIdPart()).isEqualTo("123abc");
		assertThat(id.getVersionIdPart()).isEqualTo("1");

		p = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);
		assertThat(p.getIdElement().toUnqualifiedVersionless().getValue()).isEqualTo("Patient/123abc");
		assertThat(p.getName().get(0).getFamily()).isEqualTo("Hello");

	}

	@Test
	public void testUpdateWithUuidServerResourceStrategy_ClientIdNotAllowed() {
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.NOT_ALLOWED);

		Patient p = new Patient();
		p.setId(UUID.randomUUID().toString());
		p.addName().setFamily("FAM");
		try {
			myPatientDao.update(p, mySrd);
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).matches(Msg.code(959) + "No resource exists on this server resource with ID.*, and client-assigned IDs are not enabled.");
		}

	}

	@Test
	void testCreateWithConditionalUpdate_withUuidAsServerResourceStrategyAndNoIdProvided_uuidAssignedAsResourceId() {
		// setup
		myStorageSettings.setResourceServerIdStrategy(JpaStorageSettings.IdStrategyEnum.UUID);
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://my-lab-system").setValue("123");
		p.addName().setFamily("FAM");
		String theMatchUrl = "Patient?identifier=http://my-lab-system|123";

		// execute
		String result = myPatientDao.update(p, theMatchUrl, mySrd).getId().getIdPart();

		// verify
		try {
			UUID.fromString(result);
		} catch (IllegalArgumentException exception){
			fail("", "Result id is not a UUID. Instead, it was: " + result);
		}
	}

	@Test
	public void testUpdateNoChange_ChangeForcedInPreStorageInterceptor() {
		// Add interceptor which forces a change
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, new IAnonymousInterceptor() {
			@Override
			public void invoke(IPointcut thePointcut, HookParams theArgs) {
				Patient newResource = (Patient) theArgs.get(IBaseResource.class, 1);
				newResource.addIdentifier().setValue("foo");
			}
		});

		Patient p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		assertThat(myPatientDao.update(p, mySrd).getId().getVersionIdPart()).isEqualTo("1");

		p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		assertThat(myPatientDao.update(p, mySrd).getId().getVersionIdPart()).isEqualTo("2");

		p = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(p.getActive()).isTrue();
		assertThat(p.getIdentifierFirstRep().getValue()).isEqualTo("foo");
	}

	/**
	 * The precommit interceptor should not actually be invoked since this is a NO-OP
	 * (only prestorage is)
	 */
	@Test
	public void testUpdateNoChange_ChangeForcedInPreCommitInterceptor() {
		// Add interceptor which forces a change
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, new IAnonymousInterceptor() {
			@Override
			public void invoke(IPointcut thePointcut, HookParams theArgs) {
				throw new InternalErrorException("failed intentionally");
			}
		});

		Patient p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		assertThat(myPatientDao.update(p, mySrd).getId().getVersionIdPart()).isEqualTo("1");

		p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		assertThat(myPatientDao.update(p, mySrd).getId().getVersionIdPart()).isEqualTo("1");

		p = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(p.getActive()).isTrue();
		assertThat(p.getIdentifier()).isEmpty();
	}

	@Test
	public void testUpdateNoChange_ChangeForcedInPreStorageInterceptor_InTransaction() {
		// Add interceptor which forces a change
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, new IAnonymousInterceptor() {
			@Override
			public void invoke(IPointcut thePointcut, HookParams theArgs) {
				Patient newResource = (Patient) theArgs.get(IBaseResource.class, 1);
				newResource.addIdentifier().setValue("foo");
			}
		});

		Patient p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(p);
		assertThat(mySystemDao.transaction(mySrd, bb.getBundleTyped()).getEntryFirstRep().getResponse().getLocation()).endsWith("/_history/1");

		p = new Patient();
		p.setId("Patient/A");
		p.setActive(true);
		bb = new BundleBuilder(myFhirContext);
		bb.addTransactionUpdateEntry(p);
		assertThat(mySystemDao.transaction(mySrd, bb.getBundleTyped()).getEntryFirstRep().getResponse().getLocation()).endsWith("/_history/2");

		p = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(p.getActive()).isTrue();
		assertThat(p.getIdentifierFirstRep().getValue()).isEqualTo("foo");
	}
}
