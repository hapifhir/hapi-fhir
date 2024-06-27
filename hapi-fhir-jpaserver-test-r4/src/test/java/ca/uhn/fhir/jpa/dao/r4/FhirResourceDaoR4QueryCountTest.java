package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeStep;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexStep;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.api.model.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.HistoryCountModeEnum;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.delete.job.ReindexTestHelper;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.interceptor.ForceOffsetSearchModeInterceptor;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.triggering.ISubscriptionTriggeringSvc;
import ca.uhn.fhir.jpa.subscription.triggering.SubscriptionTriggeringSvcImpl;
import ca.uhn.fhir.jpa.term.TermReadSvcImpl;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.BundleBuilder;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.util.comparator.ComparableComparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ca.uhn.fhir.jpa.subscription.FhirR4Util.createSubscription;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Note about this test class:
 * <p>
 * This entire test class is a regression test - The aim here is to make sure that
 * changes we make don't inadvertently add additional database operations. The
 * various test perform different kinds of actions and then check the numbers of
 * SQL selects, inserts, etc. The various numbers are arbitrary, but the point of
 * this test is that if you make a change and suddenly one of these tests shows
 * that a new SQL statement has been added, it is critical that you identify why
 * that change has happened and work out if it is absolutely necessary. Every
 * single individual SQL statement adds up when we're doing operations at scale,
 * so don't ever blindly adjust numbers in this test without figuring out why.
 */
@SuppressWarnings("JavadocBlankLines")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class FhirResourceDaoR4QueryCountTest extends BaseResourceProviderR4Test {

	@RegisterExtension
	@Order(0)
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(FhirContext.forR4Cached())
		.keepAliveBetweenTests();
	@RegisterExtension
	@Order(1)
	public static final HashMapResourceProviderExtension<Patient> ourPatientProvider = new HashMapResourceProviderExtension<>(ourServer, Patient.class);
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4QueryCountTest.class);
	@Autowired
	private ISearchParamPresentDao mySearchParamPresentDao;
	@Autowired
	private ISubscriptionTriggeringSvc mySubscriptionTriggeringSvc;
	@Autowired
	private ReindexStep myReindexStep;
	@Autowired
	private DeleteExpungeStep myDeleteExpungeStep;
	@Autowired
	protected SubscriptionTestUtil mySubscriptionTestUtil;
	private ReindexTestHelper myReindexTestHelper;

	@AfterEach
	public void afterResetDao() {
		mySubscriptionSettings.clearSupportedSubscriptionTypesForUnitTest();
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(new JpaStorageSettings().isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setAutoVersionReferenceAtPaths(new JpaStorageSettings().getAutoVersionReferenceAtPaths());
		myStorageSettings.setDeleteEnabled(new JpaStorageSettings().isDeleteEnabled());
		myStorageSettings.setHistoryCountMode(JpaStorageSettings.DEFAULT_HISTORY_COUNT_MODE);
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
		myStorageSettings.setMassIngestionMode(new JpaStorageSettings().isMassIngestionMode());
		myStorageSettings.setMatchUrlCacheEnabled(new JpaStorageSettings().isMatchUrlCacheEnabled());
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(new JpaStorageSettings().isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets());
		myStorageSettings.setResourceClientIdStrategy(new JpaStorageSettings().getResourceClientIdStrategy());
		myStorageSettings.setResourceMetaCountHardLimit(new JpaStorageSettings().getResourceMetaCountHardLimit());
		myStorageSettings.setRespectVersionsForSearchIncludes(new JpaStorageSettings().isRespectVersionsForSearchIncludes());
		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
		myStorageSettings.setExpungeEnabled(false);
		myStorageSettings.setUniqueIndexesEnabled(new JpaStorageSettings().isUniqueIndexesEnabled());
		myStorageSettings.setUniqueIndexesCheckedBeforeSave(new JpaStorageSettings().isUniqueIndexesCheckedBeforeSave());

		myFhirContext.getParserOptions().setStripVersionsFromReferences(true);
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(false);

		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		// Pre-cache all StructureDefinitions so that query doesn't affect other counts
		myValidationSupport.invalidateCaches();
		myValidationSupport.fetchAllStructureDefinitions();

		myReindexTestHelper = new ReindexTestHelper(myFhirContext, myDaoRegistry, mySearchParamRegistry);
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testExpungeAllVersionsWithTagsDeletesRow() {
		// Setup
		// Create then delete
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("TEST" + i);
			p.getMeta().addTag().setSystem("http://foo").setCode("bar");
			p.setActive(true);
			p.addName().setFamily("FOO");
			myPatientDao.update(p).getId();

			for (int j = 0; j < 5; j++) {
				p.setActive(!p.getActive());
				myPatientDao.update(p);
			}

			myPatientDao.delete(new IdType("Patient/TEST" + i));
		}

		myStorageSettings.setExpungeEnabled(true);

		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isNotEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isNotEmpty());

		logAllResources();

		// Test
		myCaptureQueriesListener.clear();
		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true), null);

		// Verify
		/*
		 * Note: $expunge is still pretty inefficient. We load all the HFJ_RESOURCE entities
		 * in one shot, but we then load HFJ_RES_VER entities one by one and delete the FK
		 * constraints on both HFJ_RESOURCE and HFJ_RES_VER one by one. This could definitely
		 * stand to be optimized. The one gotcha is that we call an interceptor for each
		 * version being deleted (I think so that MDM can do cleanup?) so we need to be careful
		 * about any batch deletes.
		 */
		assertEquals(47, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(80, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isEmpty());

	}

	/*
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdateWithNoChanges() {
		IIdType orgId = createOrganization(withName("MY ORG"));

		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			p.setManagingOrganization(new Reference(orgId));
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("2");
			p.setManagingOrganization(new Reference(orgId));
			myPatientDao.update(p);
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(5);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdateWithChanges() {
		IIdType orgId = createOrganization(withName("MY ORG"));
		IIdType orgId2 = createOrganization(withName("MY ORG 2"));

		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			p.setManagingOrganization(new Reference(orgId));
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("3");
			p.setManagingOrganization(new Reference(orgId2));
			myPatientDao.update(p).getResource();
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(6);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(3);
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(1);
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdateGroup_withAddedReferences_willSucceed() {
		int initialPatientsCount = 30;
		int newPatientsCount = 5;
		int allPatientsCount = initialPatientsCount + newPatientsCount;

		List<IIdType> patientList = createPatients(allPatientsCount);

		myCaptureQueriesListener.clear();
		Group group = createGroup(patientList.subList(0, initialPatientsCount));

		assertQueryCount(31, 0, 3, 0);

		myCaptureQueriesListener.clear();
		group = updateGroup(group, patientList.subList(initialPatientsCount, allPatientsCount));

		assertQueryCount(10, 1, 2, 0);

		assertThat(group.getMember()).hasSize(allPatientsCount);


	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdateGroup_NoChangesToReferences() {
		List<IIdType> patientList = createPatients(30);

		myCaptureQueriesListener.clear();
		Group group = createGroup(patientList);

		assertQueryCount(31, 0, 3, 0);

		// Make a change to the group, but don't touch any references in it
		myCaptureQueriesListener.clear();
		group.addIdentifier().setValue("foo");
		group = updateGroup(group, Collections.emptyList());

		myCaptureQueriesListener.logSelectQueries();
		assertQueryCount(5, 1, 2, 0);

		assertThat(group.getMember()).hasSize(30);


	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdateWithChangesAndTags() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);

		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.getMeta().addTag("http://system", "foo", "display");
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		runInTransaction(() -> {
			assertEquals(1, myResourceTagDao.count());
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("3");
			IBaseResource newRes = myPatientDao.update(p).getResource();
			assertEquals(1, newRes.getMeta().getTag().size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(4);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(2);
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(1);
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdateWithIndexMissingFieldsEnabled() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			p.addName().setFamily("FAMILY");
			myCaptureQueriesListener.clear();
			return myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		});
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(6);
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		runInTransaction(() -> {
			assertEquals(9, myResourceIndexedSearchParamStringDao.count());
			assertEquals(9, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(3, mySearchParamPresentDao.count());
		});

		// Now update with one additional string index

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id);
			p.addIdentifier().setSystem("urn:system").setValue("2");
			p.addName().setFamily("FAMILY").addGiven("GIVEN");
			myCaptureQueriesListener.clear();
			myPatientDao.update(p, mySrd);
		});
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(6);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(2);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(2);
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		runInTransaction(() -> {
			assertEquals(11, myResourceIndexedSearchParamStringDao.count());
			assertEquals(9, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(3, mySearchParamPresentDao.count());
		});

		// Now update with no changes

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id);
			p.addIdentifier().setSystem("urn:system").setValue("2");
			p.addName().setFamily("FAMILY").addGiven("GIVEN");
			myCaptureQueriesListener.clear();
			myPatientDao.update(p, mySrd);
		});
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(5);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		runInTransaction(() -> {
			assertEquals(11, myResourceIndexedSearchParamStringDao.count());
			assertEquals(9, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(3, mySearchParamPresentDao.count());
		});
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdate_DeletesSearchUrlOnlyWhenPresent() {

		Patient p = new Patient();
		p.setActive(false);
		p.addIdentifier().setSystem("http://foo").setValue("123");

		myCaptureQueriesListener.clear();
		IIdType id = myPatientDao.create(p, "Patient?identifier=http://foo|123", mySrd).getId();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1L, id.getVersionIdPartAsLong());

		// Update 1 - Should delete search URL

		p.setActive(true);
		myCaptureQueriesListener.clear();
		id = myPatientDao.update(p, "Patient?identifier=http://foo|123", mySrd).getId();
		assertEquals(1, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(2L, id.getVersionIdPartAsLong());

		// Update 2 - Should not try to delete search URL

		p.setActive(false);
		myCaptureQueriesListener.clear();
		id = myPatientDao.update(p, "Patient?identifier=http://foo|123", mySrd).getId();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(3L, id.getVersionIdPartAsLong());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdate_DeletesSearchUrlOnlyWhenPresent_NonConditional() {

		Patient p = new Patient();
		p.setActive(false);
		p.addIdentifier().setSystem("http://foo").setValue("123");

		myCaptureQueriesListener.clear();
		IIdType id = myPatientDao.create(p, mySrd).getId();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1L, id.getVersionIdPartAsLong());

		// Update 1 - Should not try to delete search URL since none should exist

		p.setActive(true);
		myCaptureQueriesListener.clear();
		id = myPatientDao.update(p, "Patient?identifier=http://foo|123", mySrd).getId();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(2L, id.getVersionIdPartAsLong());

		// Update 2 - Should not try to delete search URL

		p.setActive(false);
		myCaptureQueriesListener.clear();
		id = myPatientDao.update(p, "Patient?identifier=http://foo|123", mySrd).getId();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(3L, id.getVersionIdPartAsLong());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testRead() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			myPatientDao.read(id.toVersionless());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testValidate() {

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("bar-1").setDisplay("Bar 1");
		cs.addConcept().setCode("bar-2").setDisplay("Bar 2");
		myCodeSystemDao.create(cs);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(cs));

		Observation obs = new Observation();
//		obs.getMeta().addProfile("http://example.com/fhir/StructureDefinition/vitalsigns-2");
		obs.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div>Hello</div>");
		obs.getCategoryFirstRep().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category").setCode("vital-signs");
		obs.setSubject(new Reference("Patient/123"));
		obs.addPerformer(new Reference("Practitioner/123"));
		obs.setEffective(DateTimeType.now());
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.setValue(new StringType("This is the value"));
		obs.getCode().addCoding().setSystem("http://foo/cs").setCode("bar-1");
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		// Validate once
		myCaptureQueriesListener.clear();
		try {
			myObservationDao.validate(obs, null, null, null, null, null, null);
		} catch (PreconditionFailedException e) {
			fail(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(8, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
		assertEquals(6, myCaptureQueriesListener.getCommitCount());

		// Validate again (should rely only on caches)
		myCaptureQueriesListener.clear();
		myObservationDao.validate(obs, null, null, null, null, null, null);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getCommitCount());
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testVRead() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			myPatientDao.read(id.withVersion("1"));
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testCreateWithClientAssignedId() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		runInTransaction(() -> {
			Patient p = new Patient();
			p.getMaritalStatus().setText("123");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("123");
			return myPatientDao.update(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(1);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();

		runInTransaction(() -> {
			List<ResourceTable> resources = myResourceTableDao.findAll();
			assertEquals(2, resources.size());
			assertEquals(1, resources.get(0).getVersion());
			assertEquals(1, resources.get(1).getVersion());
		});

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testCreateWithServerAssignedId_AnyClientAssignedIdStrategy() {
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		myCaptureQueriesListener.clear();

		IIdType resourceId = runInTransaction(() -> {
			Patient p = new Patient();
			p.setUserData("ABAB", "ABAB");
			p.getMaritalStatus().setText("123");
			return myPatientDao.create(p).getId().toUnqualifiedVersionless();
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();

		runInTransaction(() -> {
			List<ResourceTable> resources = myResourceTableDao.findAll();
			String versions = "Resource Versions:\n * " + resources.stream().map(t -> "Resource " + t.getIdDt() + " has version: " + t.getVersion()).collect(Collectors.joining("\n * "));

			for (ResourceTable next : resources) {
				assertEquals(1, next.getVersion(), versions);
				assertNotNull(next.getResourceId());
				assertNotNull(next.getFhirId());
			}
		});

		runInTransaction(() -> {
			Patient patient = myPatientDao.read(resourceId, mySrd);
			assertEquals(resourceId.getIdPart(), patient.getIdElement().getIdPart());
			assertEquals("123", patient.getMaritalStatus().getText());
			assertEquals("1", patient.getIdElement().getVersionIdPart());
		});

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testCreateWithClientAssignedId_AnyClientAssignedIdStrategy() {
		myStorageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setUserData("ABAB", "ABAB");
			p.getMaritalStatus().setText("123");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("BBB");
			p.getMaritalStatus().setText("123");
			myPatientDao.update(p);
		});

		myCaptureQueriesListener.clear();

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("123");
			myPatientDao.update(p);
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(1);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();

		runInTransaction(() -> {
			List<ResourceTable> resources = myResourceTableDao.findAll();
			String versions = "Resource Versions:\n * " + resources.stream().map(t -> "Resource " + t.getIdDt() + " has version: " + t.getVersion()).collect(Collectors.joining("\n * "));

			for (ResourceTable next : resources) {
				assertEquals(1, next.getVersion(), versions);
				assertNotNull(next.getResourceId());
				assertNotNull(next.getFhirId());
			}
		});

		runInTransaction(() -> {
			Patient patient = myPatientDao.read(new IdType("Patient/AAA"), mySrd);
			assertEquals("AAA", patient.getIdElement().getIdPart());
			assertEquals("123", patient.getMaritalStatus().getText());
			assertEquals("1", patient.getIdElement().getVersionIdPart());
		});

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testCreateWithClientAssignedId_CheckDisabledMode() {
		when(mySrd.getHeader(eq(JpaConstants.HEADER_UPSERT_EXISTENCE_CHECK))).thenReturn(JpaConstants.HEADER_UPSERT_EXISTENCE_CHECK_DISABLED);

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("123");
			return myPatientDao.update(p, mySrd).getId().toUnqualified();
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}

	@Test
	public void testDeleteMultiple() {
		for (int i = 0; i < 10; i++) {
			createPatient(withId("PT" + i), withActiveTrue(), withIdentifier("http://foo", "id" + i), withFamily("Family" + i));
		}

		myStorageSettings.setAllowMultipleDelete(true);

		// Test

		myCaptureQueriesListener.clear();
		DeleteMethodOutcome outcome = myPatientDao.deleteByUrl("Patient?active=true", new SystemRequestDetails());

		// Validate
		assertEquals(12, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(30, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(10, outcome.getDeletedEntities().size());
	}

	@Test
	public void testDeleteExpungeStep() {
		// Setup
		for (int i = 0; i < 10; i++) {
			createPatient(
				withId("PT" + i),
				withActiveTrue(),
				withIdentifier("http://foo", "id" + i),
				withFamily("Family" + i),
				withTag("http://foo", "blah"));
		}
		List<TypedPidJson> pids = runInTransaction(() -> myResourceTableDao
			.findAll()
			.stream()
			.map(t -> new TypedPidJson(t.getResourceType(), Long.toString(t.getResourceId())))
			.collect(Collectors.toList()));

		runInTransaction(()-> assertEquals(10, myResourceTableDao.count()));

		IJobDataSink<VoidModel> sink = mock(IJobDataSink.class);

		// Test
		myCaptureQueriesListener.clear();
		RunOutcome outcome = myDeleteExpungeStep.doDeleteExpunge(new ResourceIdListWorkChunkJson(pids, null), sink, "instance-id", "chunk-id", false, null);

		// Verify
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(29, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(10, outcome.getRecordsProcessed());
		runInTransaction(()-> assertEquals(0, myResourceTableDao.count()));
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testUpdateWithClientAssignedId_DeletesDisabled() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		myStorageSettings.setDeleteEnabled(false);

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("123");
			myPatientDao.update(p).getId().toUnqualified();
		});


		// Second time

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("456");
			myPatientDao.update(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(3);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(1);
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(1);
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();

		// Third time (caches all loaded by now)

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("789");
			myPatientDao.update(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(3);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(1);
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(1);
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testReferenceToForcedId() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);

		myCaptureQueriesListener.clear();
		myPatientDao.update(patient);

		/*
		 * Add a resource with a forced ID target link
		 */

		myCaptureQueriesListener.clear();
		Observation observation = new Observation();
		observation.getSubject().setReference("Patient/P");
		myObservationDao.create(observation);
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// select: lookup forced ID
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK (subject/patient)
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

		/*
		 * Add another
		 */

		myCaptureQueriesListener.clear();
		observation = new Observation();
		observation.getSubject().setReference("Patient/P");
		myObservationDao.create(observation);
		// select: lookup forced ID
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK (subject/patient)
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testReferenceToForcedId_DeletesDisabled() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		myStorageSettings.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);

		myCaptureQueriesListener.clear();
		myPatientDao.update(patient);

		/*
		 * Add a resource with a forced ID target link
		 */

		myCaptureQueriesListener.clear();
		Observation observation = new Observation();
		observation.getSubject().setReference("Patient/P");
		myObservationDao.create(observation);
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// select: lookup forced ID
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertNoPartitionSelectors();
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

		/*
		 * Add another
		 */

		myCaptureQueriesListener.clear();
		observation = new Observation();
		observation.getSubject().setReference("Patient/P");
		myObservationDao.create(observation);
		// select: no lookups needed because of cache
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

	}

	@ParameterizedTest
	@CsvSource({
		// OptimisticLock  OptimizeMode      ExpectedSelect  ExpectedUpdate
		"  false,          CURRENT_VERSION,  1,              0",
		"  true,           CURRENT_VERSION,  11,             0",
		"  false,          ALL_VERSIONS,     11,             0",
		"  true,           ALL_VERSIONS,     21,             0",
	})
	public void testReindexJob_OptimizeStorage(boolean theOptimisticLock, ReindexParameters.OptimizeStorageModeEnum theOptimizeStorageModeEnum, int theExpectedSelectCount, int theExpectedUpdateCount) {
		// Setup

		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson();
		IIdType patientId = createPatient(withActiveTrue());
		IIdType orgId = createOrganization(withName("MY ORG"));
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId(patientId.toUnqualifiedVersionless());
			p.setActive(true);
			p.addIdentifier().setValue("" + i);
			p.setManagingOrganization(new Reference(orgId));
			myPatientDao.update(p, mySrd);
		}
		data.addTypedPid("Patient", patientId.getIdPartAsLong());
		for (int i = 0; i < 9; i++) {
			IIdType nextPatientId = createPatient(withActiveTrue());
			data.addTypedPid("Patient", nextPatientId.getIdPartAsLong());
		}

		ReindexJobParameters params = new ReindexJobParameters()
			.setOptimizeStorage(theOptimizeStorageModeEnum)
			.setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.NONE)
			.setOptimisticLock(theOptimisticLock);

		// execute
		myCaptureQueriesListener.clear();
		RunOutcome outcome = myReindexStep.doReindex(data, mock(IJobDataSink.class), "123", "456", params);

		// validate
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(theExpectedSelectCount);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(theExpectedUpdateCount);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
		assertEquals(10, outcome.getRecordsProcessed());

	}

	@Test
	public void testReindexJob_ComboParamIndexesInUse() {
        myStorageSettings.setUniqueIndexesEnabled(true);
		myReindexTestHelper.createUniqueCodeSearchParameter();
		myReindexTestHelper.createNonUniqueStatusAndCodeSearchParameter();

		Bundle inputBundle = myReindexTestHelper.createTransactionBundleWith20Observation(false);
		Bundle transactionResonse = mySystemDao.transaction(mySrd, inputBundle);
		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson();
		transactionResonse
			.getEntry()
			.stream()
			.map(t->new IdType(t.getResponse().getLocation()))
			.forEach(t->data.addTypedPid("Observation", t.getIdPartAsLong()));

        runInTransaction(() -> {
            assertEquals(24L, myResourceTableDao.count());
            assertEquals(20L, myResourceIndexedComboStringUniqueDao.count());
            assertEquals(20L, myResourceIndexedComboTokensNonUniqueDao.count());
        });

        ReindexJobParameters params = new ReindexJobParameters()
                .setOptimizeStorage(ReindexParameters.OptimizeStorageModeEnum.NONE)
                .setReindexSearchParameters(ReindexParameters.ReindexSearchParametersEnum.ALL)
                .setOptimisticLock(false);

        // execute
        myCaptureQueriesListener.clear();
		RunOutcome outcome = myReindexStep.doReindex(data, mock(IJobDataSink.class), "123", "456", params);
		assertEquals(20, outcome.getRecordsProcessed());

        // validate
        assertEquals(4, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
        assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
        assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
        assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

    }


	public void assertNoPartitionSelectors() {
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueriesForCurrentThread();
		for (SqlQuery next : selectQueries) {
			assertThat(countMatches(next.getSql(true, true).toLowerCase(), "partition_id is null")).as(() -> next.getSql(true, true)).isEqualTo(0);
			assertThat(countMatches(next.getSql(true, true).toLowerCase(), "partition_id=")).as(() -> next.getSql(true, true)).isEqualTo(0);
			assertThat(countMatches(next.getSql(true, true).toLowerCase(), "partition_id =")).as(() -> next.getSql(true, true)).isEqualTo(0);
		}
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testHistory_Server() {
		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("A");
			p.addIdentifier().setSystem("urn:system").setValue("1");
			myPatientDao.update(p).getId().toUnqualified();

			p = new Patient();
			p.setId("B");
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.update(p).getId().toUnqualified();

			p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null, null);
			assertEquals(3, history.getResources(0, 99).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table, resolve forced IDs
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		assertNoPartitionSelectors();
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();

		// Second time should leverage forced ID cache
		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null, null);
			assertEquals(3, history.getResources(0, 99).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(2);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}


	/**
	 * This could definitely stand to be optimized some, since we load tags individually
	 * for each resource
	 */
	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testHistory_Server_WithTags() {
		myStorageSettings.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

		runInTransaction(() -> {
			Patient p = new Patient();
			p.getMeta().addTag("system", "code1", "displaY1");
			p.getMeta().addTag("system", "code2", "displaY2");
			p.setId("A");
			p.addIdentifier().setSystem("urn:system").setValue("1");
			myPatientDao.update(p).getId().toUnqualified();

			p = new Patient();
			p.getMeta().addTag("system", "code1", "displaY1");
			p.getMeta().addTag("system", "code2", "displaY2");
			p.setId("B");
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.update(p).getId().toUnqualified();

			p = new Patient();
			p.getMeta().addTag("system", "code1", "displaY1");
			p.getMeta().addTag("system", "code2", "displaY2");
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null, null);
			assertEquals(3, history.getResources(0, 3).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table, resolve forced IDs, load tags (x3)
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(5);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();

		// Second time should leverage forced ID cache
		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null, null);
			assertEquals(3, history.getResources(0, 3).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table, load tags (x3)
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(5);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchAndPageThroughResults_SmallChunksOnSameBundleProvider() {
		List<String> ids = create150Patients();

		myCaptureQueriesListener.clear();
		IBundleProvider search = myPatientDao.search(new SearchParameterMap(), mySrd);
		List<String> foundIds = new ArrayList<>();
		for (int i = 0; i < 170; i += 10) {
			List<IBaseResource> nextChunk = search.getResources(i, i + 10);
			nextChunk.forEach(t -> foundIds.add(t.getIdElement().toUnqualifiedVersionless().getValue()));
		}

		assertThat(foundIds).hasSize(ids.size());
		ids.sort(new ComparableComparator<>());
		foundIds.sort(new ComparableComparator<>());
		assertEquals(ids, foundIds);

		// This really generates a surprising number of selects and commits. We
		// could stand to reduce this!
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(56, myCaptureQueriesListener.countSelectQueries());
		assertEquals(71, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchAndPageThroughResults_LargeChunksOnIndependentBundleProvider() {
		List<String> ids = create150Patients();

		myCaptureQueriesListener.clear();
		IBundleProvider search = myPatientDao.search(new SearchParameterMap(), mySrd);
		List<String> foundIds = new ArrayList<>();
		for (int i = 0; i < 170; i += 60) {
			List<IBaseResource> nextChunk = search.getResources(i, i + 60);
			nextChunk.forEach(t -> foundIds.add(t.getIdElement().toUnqualifiedVersionless().getValue()));
			search = myPagingProvider.retrieveResultList(mySrd, search.getUuid());
		}

		ids.sort(new ComparableComparator<>());
		foundIds.sort(new ComparableComparator<>());
		assertEquals(ids, foundIds);

		assertEquals(22, myCaptureQueriesListener.countSelectQueries());
		assertEquals(21, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchAndPageThroughResults_LargeChunksOnSameBundleProvider_Synchronous() {
		List<String> ids = create150Patients();

		myCaptureQueriesListener.clear();
		IBundleProvider search = myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd);
		List<String> foundIds = new ArrayList<>();
		for (int i = 0; i < 170; i += 60) {
			List<IBaseResource> nextChunk = search.getResources(i, i + 60);
			nextChunk.forEach(t -> foundIds.add(t.getIdElement().toUnqualifiedVersionless().getValue()));
		}

		ids.sort(new ComparableComparator<>());
		foundIds.sort(new ComparableComparator<>());
		assertEquals(ids, foundIds);

		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertEquals(1, myCaptureQueriesListener.getCommitCount());
		assertEquals(0, myCaptureQueriesListener.getRollbackCount());
	}

	@Nonnull
	private List<String> create150Patients() {
		BundleBuilder b = new BundleBuilder(myFhirContext);
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < 150; i++) {
			Patient p = new Patient();
			String nextId = "Patient/A" + i;
			ids.add(nextId);
			p.setId(nextId);
			b.addTransactionUpdateEntry(p);
		}
		mySystemDao.transaction(mySrd, b.getBundleTyped());
		return ids;
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchUsingOffsetMode_Explicit() {
		for (int i = 0; i < 10; i++) {
			createPatient(withId("A" + i), withActiveTrue());
		}

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronousUpTo(5);
		map.setOffset(0);
		map.add("active", new TokenParam("true"));

		// First page
		myCaptureQueriesListener.clear();
		Bundle outcome = myClient.search().forResource("Patient").where(Patient.ACTIVE.exactly().code("true")).offset(0).count(5).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).as(toUnqualifiedVersionlessIdValues(outcome).toString()).containsExactlyInAnyOrder("Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4");
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("fetch first '6'");
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink("next").getUrl()).contains("Patient?_count=5&_offset=5&active=true");

		// Second page
		myCaptureQueriesListener.clear();
		outcome = myClient.search().forResource("Patient").where(Patient.ACTIVE.exactly().code("true")).offset(5).count(5).returnBundle(Bundle.class).execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).as(toUnqualifiedVersionlessIdValues(outcome).toString()).containsExactlyInAnyOrder("Patient/A5", "Patient/A6", "Patient/A7", "Patient/A8", "Patient/A9");
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("fetch next '6'");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("offset '5'");
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertNull(outcome.getLink("next"));
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchUsingForcedIdReference() {

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);
		myPatientDao.update(patient);

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/P");
		myObservationDao.create(obs);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("subject", new ReferenceParam("Patient/P"));

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(map).size().intValue());
		// (not resolve forced ID), Perform search, load result
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertNoPartitionSelectors();
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		/*
		 * Again
		 */

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(map).size().intValue());
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// (not resolve forced ID), Perform search, load result (this time we reuse the cached forced-id resolution)
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchUsingForcedIdReference_DeletedDisabled() {
		myStorageSettings.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setId("P");
		patient.setActive(true);
		myPatientDao.update(patient);

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/P");
		myObservationDao.create(obs);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("subject", new ReferenceParam("Patient/P"));

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(map).size().intValue());
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// (not Resolve forced ID), Perform search, load result
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		/*
		 * Again
		 */

		myCaptureQueriesListener.clear();
		assertEquals(1, myObservationDao.search(map).size().intValue());
		myCaptureQueriesListener.logAllQueriesForCurrentThread();
		// (NO resolve forced ID), Perform search, load result
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchOnChainedToken() {
		Patient patient = new Patient();
		patient.setId("P");
		patient.addIdentifier().setSystem("sys").setValue("val");
		myPatientDao.update(patient);

		Observation obs = new Observation();
		obs.setId("O");
		obs.getSubject().setReference("Patient/P");
		myObservationDao.update(obs);

		SearchParameterMap map = SearchParameterMap.newSynchronous(Observation.SP_SUBJECT, new ReferenceParam("identifier", "sys|val"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder("Observation/O");

		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true).toLowerCase();
		assertThat(countMatches(sql, "join")).as(sql).isEqualTo(1);
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchOnReverseInclude() {
		Patient patient = new Patient();
		patient.getMeta().addTag("http://system", "value1", "display");
		patient.setId("P1");
		patient.getNameFirstRep().setFamily("FAM1");
		myPatientDao.update(patient);

		patient = new Patient();
		patient.setId("P2");
		patient.getMeta().addTag("http://system", "value1", "display");
		patient.getNameFirstRep().setFamily("FAM2");
		myPatientDao.update(patient);

		for (int i = 0; i < 3; i++) {
			CareTeam ct = new CareTeam();
			ct.setId("CT1-" + i);
			ct.getMeta().addTag("http://system", "value11", "display");
			ct.getSubject().setReference("Patient/P1");
			myCareTeamDao.update(ct);

			ct = new CareTeam();
			ct.setId("CT2-" + i);
			ct.getMeta().addTag("http://system", "value22", "display");
			ct.getSubject().setReference("Patient/P2");
			myCareTeamDao.update(ct);
		}

		SearchParameterMap map = SearchParameterMap.newSynchronous().addRevInclude(CareTeam.INCLUDE_SUBJECT).setSort(new SortSpec(Patient.SP_NAME));

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPatientDao.search(map);
		assertEquals(SimpleBundleProvider.class, outcome.getClass());
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder("Patient/P1", "CareTeam/CT1-0", "CareTeam/CT1-1", "CareTeam/CT1-2", "Patient/P2", "CareTeam/CT2-0", "CareTeam/CT2-1", "CareTeam/CT2-2");

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(4);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}


	@Test
	public void testSearchWithRevInclude() {
		Questionnaire q = new Questionnaire();
		q.setId("q");
		q.setUrl("http://foo");
		q.setVersion("1.0");
		myQuestionnaireDao.update(q, mySrd);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setId("qr");
		qr.setQuestionnaire("http://foo");
		myQuestionnaireResponseDao.update(qr, mySrd);

		logAllResourceLinks();

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("_id", new ReferenceParam("Questionnaire/q"));
		map.addRevInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
		IFhirResourceDao<?> dao = myQuestionnaireDao;
		dao.search(map, mySrd);

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = dao.search(map, mySrd);
		toUnqualifiedVersionlessIdValues(outcome);
		assertEquals(4, myCaptureQueriesListener.countSelectQueries());

		myCaptureQueriesListener.clear();
		outcome = dao.search(map, mySrd);
		toUnqualifiedVersionlessIdValues(outcome);
		assertEquals(4, myCaptureQueriesListener.countSelectQueries());
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchWithMultipleIncludes_Async() {
		// Setup
		createPatient(withId("A"), withFamily("Hello"));
		createEncounter(withId("E"), withIdentifier("http://foo", "bar"));
		createObservation(withId("O"), withSubject("Patient/A"), withEncounter("Encounter/E"));
		List<String> ids;

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(Observation.INCLUDE_ENCOUNTER);
		map.addInclude(Observation.INCLUDE_PATIENT);
		map.addInclude(Observation.INCLUDE_SUBJECT);
		IBundleProvider results = myObservationDao.search(map, mySrd);
		assertEquals(PersistedJpaSearchFirstPageBundleProvider.class, results.getClass());
		ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).containsExactlyInAnyOrder("Patient/A", "Encounter/E", "Observation/O");

		// Verify
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(7);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(3);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(1);
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
		runInTransaction(() -> {
			assertEquals(1, mySearchEntityDao.count());
			assertEquals(3, mySearchIncludeEntityDao.count());
		});
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchWithMultipleIncludesRecurse_Async() {
		// Setup
		createPatient(withId("A"), withFamily("Hello"));
		createEncounter(withId("E"), withIdentifier("http://foo", "bar"));
		createObservation(withId("O"), withSubject("Patient/A"), withEncounter("Encounter/E"));
		List<String> ids;

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.addInclude(Observation.INCLUDE_ENCOUNTER.asRecursive());
		map.addInclude(Observation.INCLUDE_PATIENT.asRecursive());
		map.addInclude(Observation.INCLUDE_SUBJECT.asRecursive());
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map, mySrd));
		assertThat(ids).containsExactlyInAnyOrder("Patient/A", "Encounter/E", "Observation/O");

		// Verify
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(10);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(3);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(1);
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchWithMultipleIncludes_Sync() {
		// Setup
		createPatient(withId("A"), withFamily("Hello"));
		createEncounter(withId("E"), withIdentifier("http://foo", "bar"));
		createObservation(withId("O"), withSubject("Patient/A"), withEncounter("Encounter/E"));
		List<String> ids;

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.addInclude(Observation.INCLUDE_ENCOUNTER);
		map.addInclude(Observation.INCLUDE_PATIENT);
		map.addInclude(Observation.INCLUDE_SUBJECT);
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map, mySrd));
		assertThat(ids).containsExactlyInAnyOrder("Patient/A", "Encounter/E", "Observation/O");

		// Verify
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(5);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testSearchWithMultipleIncludesRecurse_Sync() {
		// Setup
		createPatient(withId("A"), withFamily("Hello"));
		createEncounter(withId("E"), withIdentifier("http://foo", "bar"));
		createObservation(withId("O"), withSubject("Patient/A"), withEncounter("Encounter/E"));
		List<String> ids;

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.addInclude(Observation.INCLUDE_ENCOUNTER.asRecursive());
		map.addInclude(Observation.INCLUDE_PATIENT.asRecursive());
		map.addInclude(Observation.INCLUDE_SUBJECT.asRecursive());
		ids = toUnqualifiedVersionlessIdValues(myObservationDao.search(map, mySrd));
		assertThat(ids).containsExactlyInAnyOrder("Patient/A", "Encounter/E", "Observation/O");

		// Verify
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(8);
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleCreates() {
		myStorageSettings.setMassIngestionMode(true);
		myStorageSettings.setMatchUrlCacheEnabled(true);
		myStorageSettings.setDeleteEnabled(false);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		// First pass

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, createTransactionWithCreatesAndOneMatchUrl());
		// 1 lookup for the match URL only
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertEquals(19, myCaptureQueriesListener.countInsertQueries());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		runInTransaction(() -> assertEquals(4, myResourceTableDao.count()));
		logAllResources();

		// Run it again - This time even the match URL should be cached

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, createTransactionWithCreatesAndOneMatchUrl());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(16, myCaptureQueriesListener.countInsertQueries());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		runInTransaction(() -> assertEquals(7, myResourceTableDao.count()));

		// Once more for good measure

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, createTransactionWithCreatesAndOneMatchUrl());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(16, myCaptureQueriesListener.countInsertQueries());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		runInTransaction(() -> assertEquals(10, myResourceTableDao.count()));

	}

	@Nonnull
	private Bundle createTransactionWithCreatesAndOneMatchUrl() {
		BundleBuilder bb = new BundleBuilder(myFhirContext);

		Patient p = new Patient();
		p.setId(IdType.newRandomUuid());
		p.setActive(true);
		bb.addTransactionCreateEntry(p);

		Encounter enc = new Encounter();
		enc.setSubject(new Reference(p.getId()));
		enc.addParticipant().setIndividual(new Reference("Practitioner?identifier=foo|bar"));
		bb.addTransactionCreateEntry(enc);

		enc = new Encounter();
		enc.setSubject(new Reference(p.getId()));
		enc.addParticipant().setIndividual(new Reference("Practitioner?identifier=foo|bar"));
		bb.addTransactionCreateEntry(enc);

		return (Bundle) bb.getBundle();
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleCreates_PreExistingMatchUrl() {
		myStorageSettings.setMassIngestionMode(true);
		myStorageSettings.setMatchUrlCacheEnabled(true);
		myStorageSettings.setDeleteEnabled(false);
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		Practitioner pract = new Practitioner();
		pract.addIdentifier().setSystem("foo").setValue("bar");
		myPractitionerDao.create(pract);
		runInTransaction(() -> assertEquals(1, myResourceTableDao.count(), () -> myResourceTableDao.findAll().stream().map(t -> t.getIdDt().toUnqualifiedVersionless().getValue()).collect(Collectors.joining(","))));

		// First pass

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, createTransactionWithCreatesAndOneMatchUrl());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// 1 lookup for the match URL only
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertEquals(16, myCaptureQueriesListener.countInsertQueries());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		runInTransaction(() -> assertEquals(4, myResourceTableDao.count(), () -> myResourceTableDao.findAll().stream().map(t -> t.getIdDt().toUnqualifiedVersionless().getValue()).collect(Collectors.joining(","))));

		// Run it again - This time even the match URL should be cached

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, createTransactionWithCreatesAndOneMatchUrl());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(16, myCaptureQueriesListener.countInsertQueries());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		runInTransaction(() -> assertEquals(7, myResourceTableDao.count()));

		// Once more for good measure

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, createTransactionWithCreatesAndOneMatchUrl());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(16, myCaptureQueriesListener.countInsertQueries());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		runInTransaction(() -> assertEquals(10, myResourceTableDao.count()));

	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithTwoCreates() {

		BundleBuilder bb = new BundleBuilder(myFhirContext);

		Patient pt = new Patient();
		pt.setId(IdType.newRandomUuid());
		pt.addIdentifier().setSystem("http://foo").setValue("123");
		bb.addTransactionCreateEntry(pt);

		Patient pt2 = new Patient();
		pt2.setId(IdType.newRandomUuid());
		pt2.addIdentifier().setSystem("http://foo").setValue("456");
		bb.addTransactionCreateEntry(pt2);

		runInTransaction(() -> assertEquals(0, myResourceTableDao.count()));

		ourLog.info("About to start transaction");

		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) bb.getBundle());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(8, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> assertEquals(2, myResourceTableDao.count()));
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleUpdates() {

		AtomicInteger counter = new AtomicInteger(0);
		Supplier<Bundle> input = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Patient pt = new Patient();
			pt.setId("Patient/A");
			pt.addIdentifier().setSystem("http://foo").setValue("123");
			bb.addTransactionUpdateEntry(pt);

			Observation obsA = new Observation();
			obsA.setId("Observation/A");
			obsA.getCode().addCoding().setSystem("http://foo").setCode("bar");
			obsA.setValue(new Quantity(null, 1, "http://unitsofmeasure.org", "kg", "kg"));
			obsA.setEffective(new DateTimeType(new Date()));
			obsA.addNote().setText("Foo " + counter.incrementAndGet()); // changes every time
			bb.addTransactionUpdateEntry(obsA);

			Observation obsB = new Observation();
			obsB.setId("Observation/B");
			obsB.getCode().addCoding().setSystem("http://foo").setCode("bar");
			obsB.setValue(new Quantity(null, 1, "http://unitsofmeasure.org", "kg", "kg"));
			obsB.setEffective(new DateTimeType(new Date()));
			obsB.addNote().setText("Foo " + counter.incrementAndGet()); // changes every time
			bb.addTransactionUpdateEntry(obsB);

			return (Bundle) bb.getBundle();
		};

		ourLog.info("About to start transaction");

		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(18, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Run a second time
		 */

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(4, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(2, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Third time with mass ingestion mode enabled
		 */
		myStorageSettings.setMassIngestionMode(true);

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(4, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(2, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleUpdates_ResourcesHaveTags() {

		AtomicInteger counter = new AtomicInteger(0);
		Supplier<Bundle> input = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Patient pt = new Patient();
			pt.setId("Patient/A");
			pt.getMeta().addTag("http://foo", "bar", "baz");
			pt.addIdentifier().setSystem("http://foo").setValue("123");
			bb.addTransactionUpdateEntry(pt);

			int i = counter.incrementAndGet();

			Observation obsA = new Observation();
			obsA.getMeta().addTag("http://foo", "bar" + i, "baz"); // changes every time
			obsA.setId("Observation/A");
			obsA.getCode().addCoding().setSystem("http://foo").setCode("bar");
			obsA.setValue(new Quantity(null, 1, "http://unitsofmeasure.org", "kg", "kg"));
			obsA.setEffective(new DateTimeType(new Date()));
			obsA.addNote().setText("Foo " + i); // changes every time
			bb.addTransactionUpdateEntry(obsA);

			Observation obsB = new Observation();
			obsB.getMeta().addTag("http://foo", "bar", "baz" + i); // changes every time
			obsB.setId("Observation/B");
			obsB.getCode().addCoding().setSystem("http://foo").setCode("bar");
			obsB.setValue(new Quantity(null, 1, "http://unitsofmeasure.org", "kg", "kg"));
			obsB.setEffective(new DateTimeType(new Date()));
			obsB.addNote().setText("Foo " + i); // changes every time
			bb.addTransactionUpdateEntry(obsB);

			return (Bundle) bb.getBundle();
		};

		ourLog.info("About to start transaction");

		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		// Search for IDs and Search for tag definition
		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(26, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Run a second time
		 */

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(7, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(7, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Third time with mass ingestion mode enabled
		 */
		myStorageSettings.setMassIngestionMode(true);

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(5, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(5, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleInlineMatchUrls() {
		myStorageSettings.setDeleteEnabled(false);
		myStorageSettings.setMassIngestionMode(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setMatchUrlCacheEnabled(true);

		Location loc = new Location();
		loc.setId("LOC");
		loc.addIdentifier().setSystem("http://foo").setValue("123");
		myLocationDao.update(loc, mySrd);

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Encounter enc = new Encounter();
			enc.addLocation().setLocation(new Reference("Location?identifier=http://foo|123"));
			bb.addTransactionCreateEntry(enc);
		}
		Bundle input = (Bundle) bb.getBundle();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(1, countMatches(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), "'6445233466262474106'"));
		assertEquals(1, countMatches(myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false), "'LOC'"));
		assertEquals(6, runInTransaction(() -> myResourceTableDao.count()));

		// Second identical pass

		bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Encounter enc = new Encounter();
			enc.addLocation().setLocation(new Reference("Location?identifier=http://foo|123"));
			bb.addTransactionCreateEntry(enc);
		}
		input = (Bundle) bb.getBundle();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(11, runInTransaction(() -> myResourceTableDao.count()));

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleInlineMatchUrlsWithAuthentication() {
		myStorageSettings.setDeleteEnabled(false);
		myStorageSettings.setMassIngestionMode(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setMatchUrlCacheEnabled(true);

		Location loc = new Location();
		loc.setId("LOC");
		loc.addIdentifier().setSystem("http://foo").setValue("123");
		myLocationDao.update(loc, mySrd);

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Encounter enc = new Encounter();
			enc.addLocation().setLocation(new Reference("Location?identifier=http://foo|123"));
			bb.addTransactionCreateEntry(enc);
		}
		Bundle input = (Bundle) bb.getBundle();

		when(mySrd.getRestOperationType()).thenReturn(RestOperationTypeEnum.TRANSACTION);
		AuthorizationInterceptor authorizationInterceptor = new AuthorizationInterceptor(PolicyEnum.ALLOW);
		myInterceptorRegistry.registerInterceptor(authorizationInterceptor);
		try {
			myCaptureQueriesListener.clear();
			mySystemDao.transaction(mySrd, input);
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();
			assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
			assertEquals(6, runInTransaction(() -> myResourceTableDao.count()));

			// Second identical pass

			bb = new BundleBuilder(myFhirContext);
			for (int i = 0; i < 5; i++) {
				Encounter enc = new Encounter();
				enc.addLocation().setLocation(new Reference("Location?identifier=http://foo|123"));
				bb.addTransactionCreateEntry(enc);
			}
			input = (Bundle) bb.getBundle();

			myCaptureQueriesListener.clear();
			mySystemDao.transaction(mySrd, input);
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();
			assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
			assertEquals(11, runInTransaction(() -> myResourceTableDao.count()));
		} finally {
			myInterceptorRegistry.unregisterInterceptor(authorizationInterceptor);
		}
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleForcedIdReferences() {
		myStorageSettings.setDeleteEnabled(false);
		myStorageSettings.setMassIngestionMode(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setMatchUrlCacheEnabled(true);

		Patient pt = new Patient();
		pt.setId("ABC");
		pt.setActive(true);
		myPatientDao.update(pt);

		Location loc = new Location();
		loc.setId("LOC");
		loc.addIdentifier().setSystem("http://foo").setValue("123");
		myLocationDao.update(loc, mySrd);

		myMemoryCacheService.invalidateAllCaches();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Encounter enc = new Encounter();
			enc.setSubject(new Reference(pt.getId()));
			enc.addLocation().setLocation(new Reference(loc.getId()));
			bb.addTransactionCreateEntry(enc);
		}
		Bundle input = (Bundle) bb.getBundle();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(7, runInTransaction(() -> myResourceTableDao.count()));

		// Second identical pass

		bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Encounter enc = new Encounter();
			enc.setSubject(new Reference(pt.getId()));
			enc.addLocation().setLocation(new Reference(loc.getId()));
			bb.addTransactionCreateEntry(enc);
		}
		input = (Bundle) bb.getBundle();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(12, runInTransaction(() -> myResourceTableDao.count()));

	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleNumericIdReferences() {
		myStorageSettings.setDeleteEnabled(false);
		myStorageSettings.setMassIngestionMode(true);
		myStorageSettings.setAllowInlineMatchUrlReferences(true);
		myStorageSettings.setMatchUrlCacheEnabled(true);

		Patient pt = new Patient();
		pt.setActive(true);
		myPatientDao.create(pt, mySrd);

		Location loc = new Location();
		loc.addIdentifier().setSystem("http://foo").setValue("123");
		myLocationDao.create(loc, mySrd);

		myMemoryCacheService.invalidateAllCaches();

		BundleBuilder bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Encounter enc = new Encounter();
			enc.setSubject(new Reference(pt.getId()));
			enc.addLocation().setLocation(new Reference(loc.getId()));
			bb.addTransactionCreateEntry(enc);
		}
		Bundle input = (Bundle) bb.getBundle();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(7, runInTransaction(() -> myResourceTableDao.count()));

		// Second identical pass

		bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Encounter enc = new Encounter();
			enc.setSubject(new Reference(pt.getId()));
			enc.addLocation().setLocation(new Reference(loc.getId()));
			bb.addTransactionCreateEntry(enc);
		}
		input = (Bundle) bb.getBundle();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(12, runInTransaction(() -> myResourceTableDao.count()));

	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleConditionalUpdates() {

		AtomicInteger counter = new AtomicInteger(0);
		Supplier<Bundle> input = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Patient pt = new Patient();
			pt.setId(IdType.newRandomUuid());
			pt.addIdentifier().setSystem("http://foo").setValue("123");
			bb.addTransactionCreateEntry(pt).conditional("Patient?identifier=http://foo|123");

			Observation obsA = new Observation();
			obsA.getSubject().setReference(pt.getId());
			obsA.getCode().addCoding().setSystem("http://foo").setCode("bar1");
			obsA.setValue(new Quantity(null, 1, "http://unitsofmeasure.org", "kg", "kg"));
			obsA.setEffective(new DateTimeType(new Date()));
			obsA.addNote().setText("Foo " + counter.incrementAndGet()); // changes every time
			bb.addTransactionUpdateEntry(obsA).conditional("Observation?code=http://foo|bar1");

			Observation obsB = new Observation();
			obsB.getSubject().setReference(pt.getId());
			obsB.getCode().addCoding().setSystem("http://foo").setCode("bar2");
			obsB.setValue(new Quantity(null, 1, "http://unitsofmeasure.org", "kg", "kg"));
			obsB.setEffective(new DateTimeType(new Date()));
			obsB.addNote().setText("Foo " + counter.incrementAndGet()); // changes every time
			bb.addTransactionUpdateEntry(obsB).conditional("Observation?code=http://foo|bar2");

			Observation obsC = new Observation();
			obsC.getSubject().setReference(pt.getId());
			obsC.getCode().addCoding().setSystem("http://foo").setCode("bar3");
			obsC.setValue(new Quantity(null, 1, "http://unitsofmeasure.org", "kg", "kg"));
			obsC.setEffective(new DateTimeType(new Date()));
			obsC.addNote().setText("Foo " + counter.incrementAndGet()); // changes every time
			bb.addTransactionUpdateEntry(obsC).conditional("Observation?code=bar3");

			Observation obsD = new Observation();
			obsD.getSubject().setReference(pt.getId());
			obsD.getCode().addCoding().setSystem("http://foo").setCode("bar4");
			obsD.setValue(new Quantity(null, 1, "http://unitsofmeasure.org", "kg", "kg"));
			obsD.setEffective(new DateTimeType(new Date()));
			obsD.addNote().setText("Foo " + counter.incrementAndGet()); // changes every time
			bb.addTransactionUpdateEntry(obsD).conditional("Observation?code=bar4");

			return (Bundle) bb.getBundle();
		};

		ourLog.info("About to start transaction");

		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		// One to prefetch sys+val, one to prefetch val
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(45, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Run a second time
		 */

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(8, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(8, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Third time with mass ingestion mode enabled
		 */
		myStorageSettings.setMassIngestionMode(true);
		myStorageSettings.setMatchUrlCacheEnabled(true);

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(7, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(8, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Fourth time with mass ingestion mode enabled
		 */

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.debug("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(5, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(8, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithConditionalCreate_MatchUrlCacheEnabled() {
		myStorageSettings.setMatchUrlCacheEnabled(true);

		Supplier<Bundle> bundleCreator = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Patient pt = new Patient();
			pt.setId(IdType.newRandomUuid());
			pt.addIdentifier().setSystem("http://foo").setValue("123");
			bb.addTransactionCreateEntry(pt).conditional("Patient?identifier=http://foo|123");

			Observation obs = new Observation();
			obs.setId(IdType.newRandomUuid());
			obs.setSubject(new Reference(pt.getId()));
			bb.addTransactionCreateEntry(obs);

			return (Bundle) bb.getBundle();
		};

		// Run once (creates both)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertEquals(9, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types).containsExactlyInAnyOrder("Patient", "Observation");
		});

		// Run a second time (creates a new observation, reuses the patient, should use cache)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types).containsExactlyInAnyOrder("Patient", "Observation", "Observation");
		});

		// Run a third time (creates a new observation, reuses the patient, should use cache)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types).containsExactlyInAnyOrder("Patient", "Observation", "Observation", "Observation");
		});

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithConditionalCreate_MatchUrlCacheNotEnabled() {

		Supplier<Bundle> bundleCreator = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Patient pt = new Patient();
			pt.setId(IdType.newRandomUuid());
			pt.addIdentifier().setSystem("http://foo").setValue("123");
			bb.addTransactionCreateEntry(pt).conditional("Patient?identifier=http://foo|123");

			Observation obs = new Observation();
			obs.setId(IdType.newRandomUuid());
			obs.setSubject(new Reference(pt.getId()));
			bb.addTransactionCreateEntry(obs);

			return (Bundle) bb.getBundle();
		};

		// Run once (creates both)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertEquals(9, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types).containsExactlyInAnyOrder("Patient", "Observation");
		});

		// Run a second time (creates a new observation, reuses the patient, should use cache)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		// Make sure the match URL query uses a small limit
		String matchUrlQuery = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		assertThat(matchUrlQuery).contains("rispt1_0.HASH_SYS_AND_VALUE='-4132452001562191669'");
		assertThat(matchUrlQuery).contains("fetch first '2'");

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types).containsExactlyInAnyOrder("Patient", "Observation", "Observation");
		});

		// Run a third time (creates a new observation, reuses the patient, should use cache)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types).containsExactlyInAnyOrder("Patient", "Observation", "Observation", "Observation");
		});

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithCreateClientAssignedIdAndReference() {
		myStorageSettings.setDeleteEnabled(false);

		Bundle input = new Bundle();

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		input.addEntry().setFullUrl(patient.getId()).setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl("Patient/A");

		Observation observation = new Observation();
		observation.setId(IdType.newRandomUuid());
		observation.addReferenceRange().setText("A");
		input.addEntry().setFullUrl(observation.getId()).setResource(observation).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(6, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Pass 2

		input = new Bundle();

		patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		input.addEntry().setFullUrl(patient.getId()).setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.PUT).setUrl("Patient/A");

		observation = new Observation();
		observation.setId(IdType.newRandomUuid());
		observation.addReferenceRange().setText("A");
		input.addEntry().setFullUrl(observation.getId()).setResource(observation).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Observation");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());


	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleReferences() {
		Bundle input = new Bundle();

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		input.addEntry().setFullUrl(patient.getId()).setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");

		Practitioner practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry().setFullUrl(practitioner.getId()).setResource(practitioner).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Practitioner");

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(17, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultiplePreExistingReferences_ForcedId() {
		myStorageSettings.setDeleteEnabled(true);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("Practitioner/B");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		// Create transaction

		Bundle input = new Bundle();

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultiplePreExistingReferences_Numeric() {
		myStorageSettings.setDeleteEnabled(true);

		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		Practitioner practitioner = new Practitioner();
		practitioner.setActive(true);
		IIdType practitionerId = myPractitionerDao.create(practitioner).getId().toUnqualifiedVersionless();

		// Create transaction
		Bundle input = new Bundle();

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultiplePreExistingReferences_ForcedId_DeletesDisabled() {
		myStorageSettings.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("Practitioner/B");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		// Create transaction

		Bundle input = new Bundle();

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// We do not need to resolve the target IDs a second time
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultiplePreExistingReferences_Numeric_DeletesDisabled() {
		myStorageSettings.setDeleteEnabled(false);

		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		Practitioner practitioner = new Practitioner();
		practitioner.setActive(true);
		IIdType practitionerId = myPractitionerDao.create(practitioner).getId().toUnqualifiedVersionless();

		// Create transaction
		Bundle input = new Bundle();

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// We do not need to resolve the target IDs a second time
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultiplePreExistingReferences_IfNoneExist() {
		myStorageSettings.setDeleteEnabled(true);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		myPatientDao.update(patient);

		Practitioner practitioner = new Practitioner();
		practitioner.setId("Practitioner/B");
		practitioner.setActive(true);
		myPractitionerDao.update(practitioner);

		// Create transaction

		Bundle input = new Bundle();

		patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		input.addEntry().setFullUrl(patient.getId()).setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient").setIfNoneExist("Patient?active=true");

		practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry().setFullUrl(practitioner.getId()).setResource(practitioner).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Practitioner").setIfNoneExist("Practitioner?active=true");

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time

		input = new Bundle();

		patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		input.addEntry().setFullUrl(patient.getId()).setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient").setIfNoneExist("Patient?active=true");

		practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry().setFullUrl(practitioner.getId()).setResource(practitioner).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Practitioner").setIfNoneExist("Practitioner?active=true");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry().setFullUrl(sr.getId()).setResource(sr).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithMultipleProfiles() {
		myStorageSettings.setDeleteEnabled(true);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

		// Create transaction

		Bundle input = new Bundle();
		for (int i = 0; i < 5; i++) {
			Patient patient = new Patient();
			patient.getMeta().addProfile("http://example.com/profile");
			patient.getMeta().addTag().setSystem("http://example.com/tags").setCode("tag-1");
			patient.getMeta().addTag().setSystem("http://example.com/tags").setCode("tag-2");
			input.addEntry().setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");
		}

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(48, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time

		input = new Bundle();
		for (int i = 0; i < 5; i++) {
			Patient patient = new Patient();
			patient.getMeta().addProfile("http://example.com/profile");
			patient.getMeta().addTag().setSystem("http://example.com/tags").setCode("tag-1");
			patient.getMeta().addTag().setSystem("http://example.com/tags").setCode("tag-2");
			input.addEntry().setResource(patient).getRequest().setMethod(Bundle.HTTPVerb.POST).setUrl("Patient");
		}

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(45, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(output.getEntry()).hasSize(input.getEntry().size());

		runInTransaction(() -> {
			assertEquals(10, myResourceTableDao.count());
			assertEquals(10, myResourceHistoryTableDao.count());
		});

	}


	/**
	 * This test runs a transaction bundle that has a large number of inline match URLs,
	 * as well as a large number of updates (PUT). This means that a lot of URLs and resources
	 * need to be resolved (ie SQL SELECT) in order to proceed with the transaction. Prior
	 * to the optimization that introduced this test, we had 140 SELECTs, now it's 17.
	 * <p>
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithManyInlineMatchUrls() throws IOException {
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);

		Bundle input = loadResource(myFhirContext, Bundle.class, "/r4/test-patient-bundle.json");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueries();

		assertEquals(17, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(6189, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(418, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(output.getEntry()).hasSize(input.getEntry().size());

		runInTransaction(() -> {
			assertEquals(437, myResourceTableDao.count());
			assertEquals(437, myResourceHistoryTableDao.count());
		});
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithConditionalCreateAndConditionalPatchOnSameUrl() {
		// Setup
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		Patient patient = new Patient();
		patient.setActive(false);
		patient.addIdentifier().setSystem("http://system").setValue("value");
		bb.addTransactionCreateEntry(patient).conditional("Patient?identifier=http://system|value");

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(true));
		bb.addTransactionFhirPatchEntry(patch).conditional("Patient?identifier=http://system|value");

		Bundle input = bb.getBundleTyped();

		// Test
		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);

		// Verify
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(6, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(output.getEntry()).hasSize(input.getEntry().size());

		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myResourceHistoryTableDao.count());
		});

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testTransactionWithClientAssignedId() {
		BundleBuilder bb = new BundleBuilder(myFhirContext);

		for (int i = 0; i < 5; i++) {
			Provenance prov = new Provenance();
			prov.setId(IdType.newRandomUuid());
			prov.setOccurred(new DateTimeType("2022"));
			bb.addTransactionUpdateEntry(prov).conditional("Provenance/Patient-0d3b0c98-048e-4111-b804-d1c6c7816d5e-" + i);
		}

		Bundle input = bb.getBundleTyped();

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());

	}


	@Test
	public void testTransaction_ComboParamIndexesInUse() {
		myStorageSettings.setUniqueIndexesEnabled(true);
		myReindexTestHelper.createUniqueCodeSearchParameter();
		myReindexTestHelper.createNonUniqueStatusAndCodeSearchParameter();

		// Create resources for the first time
		myCaptureQueriesListener.clear();
		Bundle inputBundle = myReindexTestHelper.createTransactionBundleWith20Observation(true);
		mySystemDao.transaction(mySrd, inputBundle);
		assertEquals(21, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertEquals(78, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		// Now run the transaction again - It should not need too many SELECTs
		myCaptureQueriesListener.clear();
		inputBundle = myReindexTestHelper.createTransactionBundleWith20Observation(true);
		mySystemDao.transaction(mySrd, inputBundle);
		assertEquals(4, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());


	}

	@Test
	public void testTransaction_ComboParamIndexesInUse_NoPreCheck() {
		myStorageSettings.setUniqueIndexesEnabled(true);
		myStorageSettings.setUniqueIndexesCheckedBeforeSave(false);

		myReindexTestHelper.createUniqueCodeSearchParameter();
		myReindexTestHelper.createNonUniqueStatusAndCodeSearchParameter();

		// Create resources for the first time
		myCaptureQueriesListener.clear();
		Bundle inputBundle = myReindexTestHelper.createTransactionBundleWith20Observation(true);
		mySystemDao.transaction(mySrd, inputBundle);
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertEquals(7, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		// Now run the transaction again - It should not need too many SELECTs
		myCaptureQueriesListener.clear();
		inputBundle = myReindexTestHelper.createTransactionBundleWith20Observation(true);
		mySystemDao.transaction(mySrd, inputBundle);
		assertEquals(4, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());


	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testTriggerSubscription_Sync() throws Exception {
		// Setup
		IntStream.range(0, 200).forEach(i->createAPatient());

		mySubscriptionTestUtil.registerRestHookInterceptor();
		ForceOffsetSearchModeInterceptor interceptor = new ForceOffsetSearchModeInterceptor();
		myInterceptorRegistry.registerInterceptor(interceptor);
		try {
			String payload = "application/fhir+json";
			Subscription subscription = createSubscription("Patient?", payload, ourServer.getBaseUrl(), null);
			IIdType subscriptionId = mySubscriptionDao.create(subscription, mySrd).getId();

			waitForActivatedSubscriptionCount(1);

			mySubscriptionTriggeringSvc.triggerSubscription(null, List.of(new StringType("Patient?")),  subscriptionId, mySrd);

			// Test
			myCaptureQueriesListener.clear();
			mySubscriptionTriggeringSvc.runDeliveryPass();
			mySubscriptionTriggeringSvc.runDeliveryPass();
			mySubscriptionTriggeringSvc.runDeliveryPass();
			mySubscriptionTriggeringSvc.runDeliveryPass();
			mySubscriptionTriggeringSvc.runDeliveryPass();
			myCaptureQueriesListener.logSelectQueries();
			ourPatientProvider.waitForUpdateCount(200);

			// Validate
			assertEquals(7, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
			assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
			assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
			assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}


	@Test
	public void testTriggerSubscription_Async() throws Exception {
		// Setup
		IntStream.range(0, 200).forEach(i->createAPatient());

		mySubscriptionTestUtil.registerRestHookInterceptor();

		String payload = "application/fhir+json";
		Subscription subscription = createSubscription("Patient?", payload, ourServer.getBaseUrl(), null);
		IIdType subId = mySubscriptionDao.create(subscription, mySrd).getId();

		waitForActivatedSubscriptionCount(1);

		// Test
		myCaptureQueriesListener.clear();
		Parameters response = myClient
			.operation()
			.onInstance(subId)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Patient?"))
			.execute();
		String responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue).contains("Subscription triggering job submitted as JOB ID");

		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		myCaptureQueriesListener.clear();
		mySubscriptionTriggeringSvc.runDeliveryPass();

		myCaptureQueriesListener.logInsertQueries();
		assertEquals(15, myCaptureQueriesListener.countSelectQueries());
		assertEquals(201, myCaptureQueriesListener.countInsertQueries());
		assertEquals(3, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		myCaptureQueriesListener.clear();
		mySubscriptionTriggeringSvc.runDeliveryPass();

		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		myCaptureQueriesListener.clear();
		mySubscriptionTriggeringSvc.runDeliveryPass();

		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		SubscriptionTriggeringSvcImpl svc = ProxyUtil.getSingletonTarget(mySubscriptionTriggeringSvc, SubscriptionTriggeringSvcImpl.class);
		assertEquals(0, svc.getActiveJobCount());

		assertEquals(0, ourPatientProvider.getCountCreate());
		await().until(() -> ourPatientProvider.getCountUpdate() == 200);

	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testValueSetExpand_NotPreExpanded_UseHibernateSearch() {
		createLocalCsAndVs();

		logAllConcepts();
		logAllConceptDesignations();
		logAllConceptProperties();

		ValueSet valueSet = myValueSetDao.read(new IdType(MY_VALUE_SET), mySrd);

		myCaptureQueriesListener.clear();
		ValueSet expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(expansion.getExpansion().getContains()).hasSize(7);
		assertThat(expansion.getExpansion().getContains().stream().filter(t -> t.getCode().equals("A")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getDesignation()).hasSize(1);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.countSelectQueries()).as(() -> "\n *" + myCaptureQueriesListener.getSelectQueries().stream().map(t -> t.getSql(true, false)).collect(Collectors.joining("\n * "))).isEqualTo(6);
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		// Second time - Should reuse cache
		myCaptureQueriesListener.clear();
		expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(expansion.getExpansion().getContains()).hasSize(7);
		assertThat(expansion.getExpansion().getContains().stream().filter(t -> t.getCode().equals("A")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getDesignation()).hasSize(1);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testValueSetExpand_NotPreExpanded_DontUseHibernateSearch() {
		TermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(true);

		createLocalCsAndVs();

		logAllConcepts();
		logAllConceptDesignations();
		logAllConceptProperties();

		ValueSet valueSet = myValueSetDao.read(new IdType(MY_VALUE_SET), mySrd);

		myCaptureQueriesListener.clear();
		ValueSet expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(expansion.getExpansion().getContains()).hasSize(7);
		assertThat(expansion.getExpansion().getContains().stream().filter(t -> t.getCode().equals("A")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getDesignation()).hasSize(1);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(6, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		// Second time - Should reuse cache
		myCaptureQueriesListener.clear();
		expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(expansion.getExpansion().getContains()).hasSize(7);
		assertThat(expansion.getExpansion().getContains().stream().filter(t -> t.getCode().equals("A")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getDesignation()).hasSize(1);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testValueSetExpand_PreExpanded_UseHibernateSearch() {
		createLocalCsAndVs();

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		runInTransaction(() -> {
			Slice<TermValueSet> page = myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 10), TermValueSetPreExpansionStatusEnum.EXPANDED);
			assertEquals(1, page.getContent().size());
		});

		logAllConcepts();
		logAllConceptDesignations();
		logAllConceptProperties();

		ValueSet valueSet = myValueSetDao.read(new IdType(MY_VALUE_SET), mySrd);

		myCaptureQueriesListener.clear();
		ValueSet expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(expansion.getExpansion().getContains()).hasSize(7);
		assertThat(expansion.getExpansion().getContains().stream().filter(t -> t.getCode().equals("A")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getDesignation()).hasSize(1);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		// Second time - Should reuse cache
		myCaptureQueriesListener.clear();
		expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(expansion.getExpansion().getContains()).hasSize(7);
		assertThat(expansion.getExpansion().getContains().stream().filter(t -> t.getCode().equals("A")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getDesignation()).hasSize(1);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testMassIngestionMode_TransactionWithChanges() {
		myStorageSettings.setDeleteEnabled(false);
		myStorageSettings.setMatchUrlCacheEnabled(true);
		myStorageSettings.setMassIngestionMode(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);
		myStorageSettings.setAutoVersionReferenceAtPaths("ExplanationOfBenefit.patient", "ExplanationOfBenefit.insurance.coverage");

		Patient warmUpPt = new Patient();
		warmUpPt.getMeta().addProfile("http://foo");
		warmUpPt.setActive(true);
		myPatientDao.create(warmUpPt);

		AtomicInteger ai = new AtomicInteger(0);
		Supplier<Bundle> supplier = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirContext);

			Coverage coverage = new Coverage();
			coverage.getMeta().addProfile("http://foo");
			coverage.setId(IdType.newRandomUuid());
			coverage.addIdentifier().setSystem("http://coverage").setValue("12345");
			coverage.setStatus(Coverage.CoverageStatus.ACTIVE);
			coverage.setType(new CodeableConcept().addCoding(new Coding("http://coverage-type", "12345", null)));
			bb.addTransactionUpdateEntry(coverage).conditional("Coverage?identifier=http://coverage|12345");

			Patient patient = new Patient();
			patient.getMeta().addProfile("http://foo");
			patient.setId("Patient/PATIENT-A");
			patient.setActive(true);
			patient.addName().setFamily("SMITH").addGiven("JAMES" + ai.incrementAndGet());
			bb.addTransactionUpdateEntry(patient);

			ExplanationOfBenefit eob = new ExplanationOfBenefit();
			eob.getMeta().addProfile("http://foo");
			eob.addIdentifier().setSystem("http://eob").setValue("12345");
			eob.addInsurance().setCoverage(new Reference(coverage.getId()));
			eob.getPatient().setReference(patient.getId());
			eob.setCreatedElement(new DateTimeType("2021-01-01T12:12:12Z"));
			bb.addTransactionUpdateEntry(eob).conditional("ExplanationOfBenefit?identifier=http://eob|12345");

			return (Bundle) bb.getBundle();
		};

		// Pass 1

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(29, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Pass 2

		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals(6, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(6, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		IdType patientId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());
		assertEquals("2", patientId.getVersionIdPart());

		Patient patient = myPatientDao.read(patientId, mySrd);
		assertThat(patient.getMeta().getProfile()).hasSize(1);
		assertEquals("http://foo", patient.getMeta().getProfile().get(0).getValue());
		assertEquals("SMITH", patient.getNameFirstRep().getFamily());
		patient = myPatientDao.read(patientId.withVersion("1"), mySrd);
		assertThat(patient.getMeta().getProfile()).hasSize(1);
		assertEquals("http://foo", patient.getMeta().getProfile().get(0).getValue());
		assertEquals("SMITH", patient.getNameFirstRep().getFamily());

		// Pass 3

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals(6, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(6, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		patientId = new IdType(outcome.getEntry().get(1).getResponse().getLocation());
		assertEquals("3", patientId.getVersionIdPart());

		patient = myPatientDao.read(patientId, mySrd);
		assertThat(patient.getMeta().getProfile()).hasSize(1);
		assertEquals("http://foo", patient.getMeta().getProfile().get(0).getValue());
		assertEquals("SMITH", patient.getNameFirstRep().getFamily());
		patient = myPatientDao.read(patientId.withVersion("2"), mySrd);
		assertThat(patient.getMeta().getProfile()).hasSize(1);
		assertEquals("http://foo", patient.getMeta().getProfile().get(0).getValue());
		assertEquals("SMITH", patient.getNameFirstRep().getFamily());
		patient = myPatientDao.read(patientId.withVersion("1"), mySrd);
		assertThat(patient.getMeta().getProfile()).hasSize(1);
		assertEquals("http://foo", patient.getMeta().getProfile().get(0).getValue());
		assertEquals("SMITH", patient.getNameFirstRep().getFamily());
	}


	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testMassIngestionMode_TransactionWithChanges_NonVersionedTags() throws IOException {
		myStorageSettings.setDeleteEnabled(false);
		myStorageSettings.setMatchUrlCacheEnabled(true);
		myStorageSettings.setMassIngestionMode(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myStorageSettings.setRespectVersionsForSearchIncludes(true);
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
		myStorageSettings.setAutoVersionReferenceAtPaths("ExplanationOfBenefit.patient", "ExplanationOfBenefit.insurance.coverage");

		// Pre-cache tag definitions
		Patient patient = new Patient();
		patient.getMeta().addProfile("http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient");
		patient.getMeta().addProfile("http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Organization");
		patient.getMeta().addProfile("http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitioner");
		patient.getMeta().addProfile("http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-ExplanationOfBenefit-Professional-NonClinician");
		patient.getMeta().addProfile("http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Coverage");
		patient.setActive(true);
		myPatientDao.create(patient);

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(new SystemRequestDetails(), loadResourceFromClasspath(Bundle.class, "r4/transaction-perf-bundle.json"));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(120, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Now a copy that has differences in the EOB and Patient resources
		myCaptureQueriesListener.clear();
		mySystemDao.transaction(new SystemRequestDetails(), loadResourceFromClasspath(Bundle.class, "r4/transaction-perf-bundle-smallchanges.json"));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(6, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(5, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testDeleteResource_WithOutgoingReference() {
		// Setup
		createOrganization(withId("A"));
		IIdType patientId = createPatient(withOrganization(new IdType("Organization/A")), withActiveTrue());

		// Test
		myCaptureQueriesListener.clear();
		myPatientDao.delete(patientId, mySrd);

		// Verify
		assertEquals(4, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
		runInTransaction(()->{
			ResourceTable version = myResourceTableDao.findById(patientId.getIdPartAsLong()).orElseThrow();
			assertFalse(version.isParamsTokenPopulated());
			assertFalse(version.isHasLinks());
			assertEquals(0, myResourceIndexedSearchParamTokenDao.count());
			assertEquals(0, myResourceLinkDao.count());
		});

	}

	/**
	 * See the class javadoc before changing the counts in this test!
	 */
	@Test
	public void testDeleteResource_WithMassIngestionMode_enabled() {
		myStorageSettings.setMassIngestionMode(true);

		// given
		Observation observation = new Observation().setStatus(Observation.ObservationStatus.FINAL).addCategory(new CodeableConcept().addCoding(new Coding("http://category-type", "12345", null))).setCode(new CodeableConcept().addCoding(new Coding("http://coverage-type", "12345", null)));

		IIdType idDt = myObservationDao.create(observation, mySrd).getEntity().getIdDt();
		runInTransaction(()->{
			assertEquals(4, myResourceIndexedSearchParamTokenDao.count());
			ResourceTable version = myResourceTableDao.findById(idDt.getIdPartAsLong()).orElseThrow();
			assertTrue(version.isParamsTokenPopulated());
		});

		// when
		myCaptureQueriesListener.clear();
		myObservationDao.delete(idDt, mySrd);

		// then
		assertQueryCount(3, 1, 1, 2);
		runInTransaction(()->{
			assertEquals(0, myResourceIndexedSearchParamTokenDao.count());
			ResourceTable version = myResourceTableDao.findById(idDt.getIdPartAsLong()).orElseThrow();
			assertFalse(version.isParamsTokenPopulated());
		});
	}

	private void assertQueryCount(int theExpectedSelectCount, int theExpectedUpdateCount, int theExpectedInsertCount, int theExpectedDeleteCount) {

		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(theExpectedSelectCount);
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).hasSize(theExpectedUpdateCount);
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).hasSize(theExpectedInsertCount);
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).hasSize(theExpectedDeleteCount);
	}

	private Group createGroup(List<IIdType> theIIdTypeList) {
		Group aGroup = new Group();
		aGroup.setId("Group/someGroupId");

		return updateGroup(aGroup, theIIdTypeList);
	}

	private Group updateGroup(Group theGroup, List<IIdType> theIIdTypeList) {

		for (IIdType idType : theIIdTypeList) {
			Group.GroupMemberComponent aGroupMemberComponent = new Group.GroupMemberComponent(new Reference(idType));
			theGroup.addMember(aGroupMemberComponent);
		}

		return runInTransaction(() -> (Group) myGroupDao.update(theGroup, mySrd).getResource());

	}

	private List<IIdType> createPatients(int theCount) {
		List<IIdType> reVal = new ArrayList<>(theCount);
		for (int i = 0; i < theCount; i++) {
			reVal.add(createAPatient());
		}

		return reVal;
	}

	private IIdType createAPatient() {

		return runInTransaction(() -> {
			Patient p = new Patient();
			p.getMeta().addTag("http://system", "foo", "display");
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});
	}

}
