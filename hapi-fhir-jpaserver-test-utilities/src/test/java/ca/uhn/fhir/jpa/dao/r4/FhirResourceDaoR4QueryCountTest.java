package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.HistoryCountModeEnum;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.BaseTermReadSvcImpl;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.util.BundleBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class FhirResourceDaoR4QueryCountTest extends BaseResourceProviderR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4QueryCountTest.class);

	@AfterEach
	public void afterResetDao() {
		myDaoConfig.setResourceMetaCountHardLimit(new DaoConfig().getResourceMetaCountHardLimit());
		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
		myDaoConfig.setDeleteEnabled(new DaoConfig().isDeleteEnabled());
		myDaoConfig.setMatchUrlCacheEnabled(new DaoConfig().isMatchUrlCacheEnabled());
		myDaoConfig.setHistoryCountMode(DaoConfig.DEFAULT_HISTORY_COUNT_MODE);
		myDaoConfig.setMassIngestionMode(new DaoConfig().isMassIngestionMode());
		myModelConfig.setAutoVersionReferenceAtPaths(new ModelConfig().getAutoVersionReferenceAtPaths());
		myModelConfig.setRespectVersionsForSearchIncludes(new ModelConfig().isRespectVersionsForSearchIncludes());
		myFhirContext.getParserOptions().setStripVersionsFromReferences(true);
		myDaoConfig.setTagStorageMode(new DaoConfig().getTagStorageMode());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(new DaoConfig().isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(new DaoConfig().isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets());
		myDaoConfig.setResourceClientIdStrategy(new DaoConfig().getResourceClientIdStrategy());

		BaseTermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(false);
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myInterceptorRegistry.registerInterceptor(myInterceptor);
	}


	@Test
	public void testUpdateWithNoChanges() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("2");
			myPatientDao.update(p);
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread(), empty());
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread(), empty());
	}


	@Test
	public void testUpdateWithChanges() {
		IIdType id = runInTransaction(() -> {
			Patient p = new Patient();
			p.addIdentifier().setSystem("urn:system").setValue("2");
			return myPatientDao.create(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId(id.getIdPart());
			p.addIdentifier().setSystem("urn:system").setValue("3");
			myPatientDao.update(p).getResource();
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


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
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


	@Test
	public void testValidate() {

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("bar-1").setDisplay("Bar 1");
		cs.addConcept().setCode("bar-2").setDisplay("Bar 2");
		myCodeSystemDao.create(cs);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(cs));

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
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		// Validate once
		myCaptureQueriesListener.clear();
		myObservationDao.validate(obs, null, null, null, null, null, null);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(12, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		// Validate again (should rely only on caches)
		myCaptureQueriesListener.clear();
		myObservationDao.validate(obs, null, null, null, null, null, null);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


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
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


	@Test
	public void testCreateWithClientAssignedId() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

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
		assertEquals(1, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		runInTransaction(() -> {
			List<ResourceTable> resources = myResourceTableDao.findAll();
			assertEquals(2, resources.size());
			assertEquals(1, resources.get(0).getVersion());
			assertEquals(1, resources.get(1).getVersion());
		});

	}

	@Test
	public void testCreateWithServerAssignedId_AnyClientAssignedIdStrategy() {
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ANY);
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		myCaptureQueriesListener.clear();

		IIdType resourceId = runInTransaction(() -> {
			Patient p = new Patient();
			p.setUserData("ABAB", "ABAB");
			p.getMaritalStatus().setText("123");
			return myPatientDao.create(p).getId().toUnqualifiedVersionless();
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		runInTransaction(() -> {
			List<ForcedId> allForcedIds = myForcedIdDao.findAll();
			for (ForcedId next : allForcedIds) {
				assertNotNull(next.getResourceId());
				assertNotNull(next.getForcedId());
			}

			List<ResourceTable> resources = myResourceTableDao.findAll();
			String versions = "Resource Versions:\n * " + resources
				.stream()
				.map(t -> "Resource " + t.getIdDt() + " has version: " + t.getVersion())
				.collect(Collectors.joining("\n * "));

			for (ResourceTable next : resources) {
				assertEquals(1, next.getVersion(), versions);
			}
		});

		runInTransaction(() -> {
			Patient patient = myPatientDao.read(resourceId, mySrd);
			assertEquals(resourceId.getIdPart(), patient.getIdElement().getIdPart());
			assertEquals("123", patient.getMaritalStatus().getText());
			assertEquals("1", patient.getIdElement().getVersionIdPart());
		});

	}


	@Test
	public void testCreateWithClientAssignedId_AnyClientAssignedIdStrategy() {
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ANY);
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

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
		assertEquals(1, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		runInTransaction(() -> {
			List<ForcedId> allForcedIds = myForcedIdDao.findAll();
			for (ForcedId next : allForcedIds) {
				assertNotNull(next.getResourceId());
				assertNotNull(next.getForcedId());
			}

			List<ResourceTable> resources = myResourceTableDao.findAll();
			String versions = "Resource Versions:\n * " + resources
				.stream()
				.map(t -> "Resource " + t.getIdDt() + " has version: " + t.getVersion())
				.collect(Collectors.joining("\n * "));

			for (ResourceTable next : resources) {
				assertEquals(1, next.getVersion(), versions);
			}
		});

		runInTransaction(() -> {
			Patient patient = myPatientDao.read(new IdType("Patient/AAA"), mySrd);
			assertEquals("AAA", patient.getIdElement().getIdPart());
			assertEquals("123", patient.getMaritalStatus().getText());
			assertEquals("1", patient.getIdElement().getVersionIdPart());
		});

	}

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
		assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}

	@Test
	public void testUpdateWithClientAssignedId_DeletesDisabled() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myDaoConfig.setDeleteEnabled(false);

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
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		// Third time (caches all loaded by now)

		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			Patient p = new Patient();
			p.setId("AAA");
			p.getMaritalStatus().setText("789");
			myPatientDao.update(p).getId().toUnqualified();
		});

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


	@Test
	public void testReferenceToForcedId() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

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
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
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
		// insert to: HFJ_RESOURCE, HFJ_RES_VER, HFJ_RES_LINK
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

	}


	@Test
	public void testReferenceToForcedId_DeletesDisabled() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		myDaoConfig.setDeleteEnabled(false);

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
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
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
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());

	}

	public void assertNoPartitionSelectors() {
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueriesForCurrentThread();
		for (SqlQuery next : selectQueries) {
			assertEquals(0, StringUtils.countMatches(next.getSql(true, true).toLowerCase(), "partition_id is null"), () -> next.getSql(true, true));
			assertEquals(0, StringUtils.countMatches(next.getSql(true, true).toLowerCase(), "partition_id="), () -> next.getSql(true, true));
			assertEquals(0, StringUtils.countMatches(next.getSql(true, true).toLowerCase(), "partition_id ="), () -> next.getSql(true, true));
		}
	}

	@Test
	public void testHistory_Server() {
		myDaoConfig.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

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
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertNoPartitionSelectors();
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		// Second time should leverage forced ID cache
		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null, null);
			assertEquals(3, history.getResources(0, 99).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table
		assertEquals(2, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


	/**
	 * This could definitely stand to be optimized some, since we load tags individually
	 * for each resource
	 */
	@Test
	public void testHistory_Server_WithTags() {
		myDaoConfig.setHistoryCountMode(HistoryCountModeEnum.COUNT_ACCURATE);

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
		assertEquals(5, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());

		// Second time should leverage forced ID cache
		myCaptureQueriesListener.clear();
		runInTransaction(() -> {
			IBundleProvider history = mySystemDao.history(null, null, null, null);
			assertEquals(3, history.getResources(0, 3).size());
		});
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		// Perform count, Search history table, load tags (x3)
		assertEquals(5, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		myCaptureQueriesListener.logDeleteQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}


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
		Bundle outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.offset(0)
			.count(5)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4"
		));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '5'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink("next").getUrl(), containsString("Patient?_count=5&_offset=5&active=true"));

		// Second page
		myCaptureQueriesListener.clear();
		outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.offset(5)
			.count(5)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/A5", "Patient/A6", "Patient/A7", "Patient/A8", "Patient/A9"
		));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '5'"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("offset '5'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink("next").getUrl(), containsString("Patient?_count=5&_offset=10&active=true"));

		// Third page (no results)

		myCaptureQueriesListener.clear();
		outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.offset(10)
			.count(5)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), empty());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '5'"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("offset '10'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

	}


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


	@Test
	public void testSearchUsingForcedIdReference_DeletedDisabled() {
		myDaoConfig.setDeleteEnabled(false);

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
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder("Observation/O"));

		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true).toLowerCase();
		assertEquals(1, StringUtils.countMatches(sql, "join"), sql);
	}


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

		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.addRevInclude(CareTeam.INCLUDE_SUBJECT)
			.setSort(new SortSpec(Patient.SP_NAME));

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPatientDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/P1", "CareTeam/CT1-0", "CareTeam/CT1-1", "CareTeam/CT1-2",
			"Patient/P2", "CareTeam/CT2-0", "CareTeam/CT2-1", "CareTeam/CT2-2"
		));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getInsertQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getUpdateQueriesForCurrentThread().size());
		assertEquals(0, myCaptureQueriesListener.getDeleteQueriesForCurrentThread().size());
	}

	@Test
	public void testTransactionWithMultipleCreates() {
		myDaoConfig.setMassIngestionMode(true);
		myDaoConfig.setMatchUrlCacheEnabled(true);
		myDaoConfig.setDeleteEnabled(false);
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		// First pass

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, createTransactionWithCreatesAndOneMatchUrl());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
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

	@Test
	public void testTransactionWithMultipleCreates_PreExistingMatchUrl() {
		myDaoConfig.setMassIngestionMode(true);
		myDaoConfig.setMatchUrlCacheEnabled(true);
		myDaoConfig.setDeleteEnabled(false);
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

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
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(8, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> assertEquals(2, myResourceTableDao.count()));
	}

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
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(21, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Run a second time
		 */

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(5, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(2, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Third time with mass ingestion mode enabled
		 */
		myDaoConfig.setMassIngestionMode(true);

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(5, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(2, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

	}

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
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		// Search for IDs and Search for tag definition
		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(29, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Run a second time
		 */

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(9, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(7, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Third time with mass ingestion mode enabled
		 */
		myDaoConfig.setMassIngestionMode(true);

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(7, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(5, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

	}

	@Test
	public void testTransactionWithMultipleInlineMatchUrls() {
		myDaoConfig.setDeleteEnabled(false);
		myDaoConfig.setMassIngestionMode(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setMatchUrlCacheEnabled(true);

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

	@Test
	public void testTransactionWithMultipleInlineMatchUrlsWithAuthentication() {
		myDaoConfig.setDeleteEnabled(false);
		myDaoConfig.setMassIngestionMode(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setMatchUrlCacheEnabled(true);

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
		myInterceptorRegistry.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW));

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
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

	}


	@Test
	public void testTransactionWithMultipleForcedIdReferences() {
		myDaoConfig.setDeleteEnabled(false);
		myDaoConfig.setMassIngestionMode(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setMatchUrlCacheEnabled(true);

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


	@Test
	public void testTransactionWithMultipleNumericIdReferences() {
		myDaoConfig.setDeleteEnabled(false);
		myDaoConfig.setMassIngestionMode(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setMatchUrlCacheEnabled(true);

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
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(40, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(4, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		/*
		 * Run a second time
		 */

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
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
		myDaoConfig.setMassIngestionMode(true);
		myDaoConfig.setMatchUrlCacheEnabled(true);

		myCaptureQueriesListener.clear();
		outcome = mySystemDao.transaction(mySrd, input.get());
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
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
		ourLog.info("Resp: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(6, myCaptureQueriesListener.countSelectQueries());
		myCaptureQueriesListener.logInsertQueries();
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		myCaptureQueriesListener.logUpdateQueries();
		assertEquals(8, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
	}


	@Test
	public void testTransactionWithConditionalCreate_MatchUrlCacheEnabled() {
		myDaoConfig.setMatchUrlCacheEnabled(true);

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
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertEquals(8, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types, containsInAnyOrder("Patient", "Observation"));
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
			assertThat(types, containsInAnyOrder("Patient", "Observation", "Observation"));
		});

		// Run a third time (creates a new observation, reuses the patient, should use cache)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types, containsInAnyOrder("Patient", "Observation", "Observation", "Observation"));
		});

	}

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
		assertEquals(8, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types, containsInAnyOrder("Patient", "Observation"));
		});

		// Run a second time (creates a new observation, reuses the patient, should use cache)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		// Make sure the match URL query uses a small limit
		String matchUrlQuery = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, true);
		assertThat(matchUrlQuery, containsString("t0.HASH_SYS_AND_VALUE = '-4132452001562191669'"));
		assertThat(matchUrlQuery, containsString("limit '2'"));

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types, containsInAnyOrder("Patient", "Observation", "Observation"));
		});

		// Run a third time (creates a new observation, reuses the patient, should use cache)

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, bundleCreator.get());
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertEquals(4, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		runInTransaction(() -> {
			List<String> types = myResourceTableDao.findAll().stream().map(t -> t.getResourceType()).collect(Collectors.toList());
			assertThat(types, containsInAnyOrder("Patient", "Observation", "Observation", "Observation"));
		});

	}

	@Test
	public void testTransactionWithCreateClientAssignedIdAndReference() {
		myDaoConfig.setDeleteEnabled(false);

		Bundle input = new Bundle();

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		input.addEntry()
			.setFullUrl(patient.getId())
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("Patient/A");

		Observation observation = new Observation();
		observation.setId(IdType.newRandomUuid());
		observation.addReferenceRange().setText("A");
		input.addEntry()
			.setFullUrl(observation.getId())
			.setResource(observation)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Observation");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Pass 2

		input = new Bundle();

		patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		input.addEntry()
			.setFullUrl(patient.getId())
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("Patient/A");

		observation = new Observation();
		observation.setId(IdType.newRandomUuid());
		observation.addReferenceRange().setText("A");
		input.addEntry()
			.setFullUrl(observation.getId())
			.setResource(observation)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Observation");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());


	}


	@Test
	public void testTransactionWithMultipleReferences() {
		Bundle input = new Bundle();

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		input.addEntry()
			.setFullUrl(patient.getId())
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Patient");

		Practitioner practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry()
			.setFullUrl(practitioner.getId())
			.setResource(practitioner)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Practitioner");

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}


	@Test
	public void testTransactionWithMultiplePreExistingReferences_ForcedId() {
		myDaoConfig.setDeleteEnabled(true);

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
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testTransactionWithMultiplePreExistingReferences_Numeric() {
		myDaoConfig.setDeleteEnabled(true);

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
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testTransactionWithMultiplePreExistingReferences_ForcedId_DeletesDisabled() {
		myDaoConfig.setDeleteEnabled(false);

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
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// We do not need to resolve the target IDs a second time
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testTransactionWithMultiplePreExistingReferences_Numeric_DeletesDisabled() {
		myDaoConfig.setDeleteEnabled(false);

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
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time - Deletes are enabled so we expect to have to resolve the
		// targets again to make sure they weren't deleted

		input = new Bundle();

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReferenceElement(patientId);
		sr.addPerformer().setReferenceElement(practitionerId);
		sr.addPerformer().setReferenceElement(practitionerId);
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// We do not need to resolve the target IDs a second time
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}

	@Test
	public void testTransactionWithMultiplePreExistingReferences_IfNoneExist() {
		myDaoConfig.setDeleteEnabled(true);

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
		input.addEntry()
			.setFullUrl(patient.getId())
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Patient")
			.setIfNoneExist("Patient?active=true");

		practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry()
			.setFullUrl(practitioner.getId())
			.setResource(practitioner)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Practitioner")
			.setIfNoneExist("Practitioner?active=true");

		ServiceRequest sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(6, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Do the same a second time

		input = new Bundle();

		patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		input.addEntry()
			.setFullUrl(patient.getId())
			.setResource(patient)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Patient")
			.setIfNoneExist("Patient?active=true");

		practitioner = new Practitioner();
		practitioner.setId(IdType.newRandomUuid());
		practitioner.setActive(true);
		input.addEntry()
			.setFullUrl(practitioner.getId())
			.setResource(practitioner)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("Practitioner")
			.setIfNoneExist("Practitioner?active=true");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		sr = new ServiceRequest();
		sr.getSubject().setReference(patient.getId());
		sr.addPerformer().setReference(practitioner.getId());
		sr.addPerformer().setReference(practitioner.getId());
		input.addEntry()
			.setFullUrl(sr.getId())
			.setResource(sr)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("ServiceRequest");

		myCaptureQueriesListener.clear();
		output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		// Lookup the two existing IDs to make sure they are legit
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(4, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}


	@Test
	public void testTransactionWithMultipleProfiles() {
		myDaoConfig.setDeleteEnabled(true);
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		// Create transaction

		Bundle input = new Bundle();
		for (int i = 0; i < 5; i++) {
			Patient patient = new Patient();
			patient.getMeta().addProfile("http://example.com/profile");
			patient.getMeta().addTag().setSystem("http://example.com/tags").setCode("tag-1");
			patient.getMeta().addTag().setSystem("http://example.com/tags").setCode("tag-2");
			input.addEntry()
				.setResource(patient)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Patient");
		}

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(8, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
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
			input.addEntry()
				.setResource(patient)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.POST)
				.setUrl("Patient");
		}

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(mySrd, input);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(5, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}


	@Test
	public void testValueSetExpand_NotPreExpanded_UseHibernateSearch() {
		createLocalCsAndVs();

		logAllConcepts();
		logAllConceptDesignations();
		logAllConceptProperties();

		ValueSet valueSet = myValueSetDao.read(new IdType(MY_VALUE_SET), mySrd);

		myCaptureQueriesListener.clear();
		ValueSet expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertEquals(7, expansion.getExpansion().getContains().size());
		assertEquals(1, expansion.getExpansion().getContains().stream().filter(t->t.getCode().equals("A")).findFirst().orElseThrow(()->new IllegalArgumentException()).getDesignation().size());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(5, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		// Second time - Should reuse cache
		myCaptureQueriesListener.clear();
		expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertEquals(7, expansion.getExpansion().getContains().size());
		assertEquals(1, expansion.getExpansion().getContains().stream().filter(t->t.getCode().equals("A")).findFirst().orElseThrow(()->new IllegalArgumentException()).getDesignation().size());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
	}

	@Test
	public void testValueSetExpand_NotPreExpanded_DontUseHibernateSearch() {
		BaseTermReadSvcImpl.setForceDisableHibernateSearchForUnitTest(true);

		createLocalCsAndVs();

		logAllConcepts();
		logAllConceptDesignations();
		logAllConceptProperties();

		ValueSet valueSet = myValueSetDao.read(new IdType(MY_VALUE_SET), mySrd);

		myCaptureQueriesListener.clear();
		ValueSet expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertEquals(7, expansion.getExpansion().getContains().size());
		assertEquals(1, expansion.getExpansion().getContains().stream().filter(t->t.getCode().equals("A")).findFirst().orElseThrow(()->new IllegalArgumentException()).getDesignation().size());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(5, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		// Second time - Should reuse cache
		myCaptureQueriesListener.clear();
		expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertEquals(7, expansion.getExpansion().getContains().size());
		assertEquals(1, expansion.getExpansion().getContains().stream().filter(t->t.getCode().equals("A")).findFirst().orElseThrow(()->new IllegalArgumentException()).getDesignation().size());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
	}

	@Test
	public void testValueSetExpand_PreExpanded_UseHibernateSearch() {
		createLocalCsAndVs();

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		runInTransaction(()->{
			Slice<TermValueSet> page = myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 10), TermValueSetPreExpansionStatusEnum.EXPANDED);
			assertEquals(1, page.getContent().size());
		});

		logAllConcepts();
		logAllConceptDesignations();
		logAllConceptProperties();

		ValueSet valueSet = myValueSetDao.read(new IdType(MY_VALUE_SET), mySrd);

		myCaptureQueriesListener.clear();
		ValueSet expansion = (ValueSet) myValidationSupport.expandValueSet(new ValidationSupportContext(myValidationSupport), new ValueSetExpansionOptions(), valueSet).getValueSet();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertEquals(7, expansion.getExpansion().getContains().size());
		assertEquals(1, expansion.getExpansion().getContains().stream().filter(t->t.getCode().equals("A")).findFirst().orElseThrow(()->new IllegalArgumentException()).getDesignation().size());
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
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertEquals(7, expansion.getExpansion().getContains().size());
		assertEquals(1, expansion.getExpansion().getContains().stream().filter(t->t.getCode().equals("A")).findFirst().orElseThrow(()->new IllegalArgumentException()).getDesignation().size());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(0, myCaptureQueriesListener.countSelectQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());
	}

	@Test
	public void testMassIngestionMode_TransactionWithChanges() {
		myDaoConfig.setDeleteEnabled(false);
		myDaoConfig.setMatchUrlCacheEnabled(true);
		myDaoConfig.setMassIngestionMode(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setRespectVersionsForSearchIncludes(true);
		myModelConfig.setAutoVersionReferenceAtPaths(
			"ExplanationOfBenefit.patient",
			"ExplanationOfBenefit.insurance.coverage"
		);

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

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		assertEquals(2, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(9, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		myCaptureQueriesListener.clear();
		mySystemDao.transaction(new SystemRequestDetails(), supplier.get());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(8, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(2, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());
	}


	@Test
	public void testMassIngestionMode_TransactionWithChanges_2() throws IOException {
		myDaoConfig.setDeleteEnabled(false);
		myDaoConfig.setMatchUrlCacheEnabled(true);
		myDaoConfig.setMassIngestionMode(true);
		myFhirContext.getParserOptions().setStripVersionsFromReferences(false);
		myModelConfig.setRespectVersionsForSearchIncludes(true);
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.NON_VERSIONED);
		myModelConfig.setAutoVersionReferenceAtPaths(
			"ExplanationOfBenefit.patient",
			"ExplanationOfBenefit.insurance.coverage"
		);

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
		assertEquals(10, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		myCaptureQueriesListener.logUpdateQueriesForCurrentThread();
		assertEquals(1, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		// Now a copy that has differences in the EOB and Patient resources
		myCaptureQueriesListener.clear();
		mySystemDao.transaction(new SystemRequestDetails(), loadResourceFromClasspath(Bundle.class, "r4/transaction-perf-bundle-smallchanges.json"));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(8, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(1, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(3, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

	}


}
