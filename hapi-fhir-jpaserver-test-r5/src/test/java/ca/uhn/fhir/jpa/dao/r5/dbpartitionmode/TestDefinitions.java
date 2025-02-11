package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeJobParameters;
import ca.uhn.fhir.batch2.jobs.expunge.DeleteExpungeStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.dao.PatientEverythingParameters;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryProvenanceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.dao.JpaPidFk;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.jpa.util.TestPartitionSelectorInterceptor;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.HistorySearchDateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import jakarta.annotation.Nonnull;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.assertj.core.api.Assertions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.ConceptMap;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.QuestionnaireResponse;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.ValueSet;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.r5.dbpartitionmode.DbpmDisabledPartitioningEnabledTest.PARTITION_1;
import static ca.uhn.fhir.jpa.dao.r5.dbpartitionmode.DbpmDisabledPartitioningEnabledTest.PARTITION_2;
import static ca.uhn.fhir.rest.api.Constants.PARAM_HAS;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SOURCE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_TAG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hl7.fhir.instance.model.api.IAnyResource.SP_RES_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This class is a set of test that are run as {@literal @Nested} by several
 * test classes. It verifies that we emit appropriate SQL for various
 * scenarios including non-partitioned mode, partitioned mode, and
 * database partitioning mode.
 */
abstract class TestDefinitions implements ITestDataBuilder {

	private final TestPartitionSelectorInterceptor myPartitionSelectorInterceptor;
	private final boolean myIncludePartitionIdsInSql;
	private final BaseDbpmJpaR5Test myParentTest;
	private final boolean myIncludePartitionIdsInPks;
	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	protected ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
	@Autowired
	protected ITermReadSvc myTermSvc;
	@Autowired
	private TestDaoSearch myTestDaoSearch;
	@Autowired
	private InterceptorService myInterceptorService;
	@Autowired
	protected CircularQueueCaptureQueriesListener myCaptureQueriesListener;
	@Autowired
	private IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;
	@Autowired
	private IFhirResourceDao<ConceptMap> myConceptMapDao;
	@Autowired
	private IFhirResourceDaoObservation<Observation> myObservationDao;
	@Autowired
	private IFhirResourceDao<ValueSet> myValueSetDao;
	@Autowired
	private IFhirResourceDao<Encounter> myEncounterDao;
	@Autowired
	private IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	private IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	private IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	private IResourceHistoryProvenanceDao myResourceHistoryProvenanceTableDao;
	@Autowired
	private IResourceLinkDao myResourceLinkDao;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private MemoryCacheService myMemoryCache;
	@Autowired
	private JpaStorageSettings myStorageSettings;
	@Autowired
	private DeleteExpungeStep myDeleteExpungeStep;

	@Mock
	private IJobDataSink<VoidModel> myVoidSink;
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;

	public TestDefinitions(@Nonnull BaseDbpmJpaR5Test theParentTest, @Nonnull TestPartitionSelectorInterceptor thePartitionSelectorInterceptor, boolean theIncludePartitionIdsInSql, boolean theIncludePartitionIdsInPks) {
		myParentTest = theParentTest;
		myPartitionSelectorInterceptor = thePartitionSelectorInterceptor;
		myIncludePartitionIdsInSql = theIncludePartitionIdsInSql;
		myIncludePartitionIdsInPks = theIncludePartitionIdsInPks;
		assert myIncludePartitionIdsInSql && myIncludePartitionIdsInPks || myIncludePartitionIdsInSql || !myIncludePartitionIdsInPks;
	}

	@AfterEach
	public void after() {
		{
			JpaStorageSettings defaults = new JpaStorageSettings();
			myStorageSettings.setTagStorageMode(defaults.getTagStorageMode());
			myStorageSettings.setIndexOnContainedResources(defaults.isIndexOnContainedResources());
		}
		{
			PartitionSettings defaults = new PartitionSettings();
			myPartitionSettings.setConditionalCreateDuplicateIdentifiersEnabled(defaults.isConditionalCreateDuplicateIdentifiersEnabled());
		}
	}

	@Test
	public void testBatch_DeleteExpungeStep() {
		// Setup

		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_2);
		createPatient(withActiveFalse());
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		Long pid = createPatient(withActiveTrue()).getIdPartAsLong();

		// Test
		myCaptureQueriesListener.clear();
		DeleteExpungeJobParameters params = new DeleteExpungeJobParameters();
		Collection<TypedPidJson> typedPids = List.of(
			new TypedPidJson("Patient", 1, pid.toString())
		);
		ResourceIdListWorkChunkJson workChunk = new ResourceIdListWorkChunkJson(typedPids, RequestPartitionId.fromPartitionId(PARTITION_1));
		JobInstance jobInstance = new JobInstance();
		String workChunkId = "AA";
		StepExecutionDetails<DeleteExpungeJobParameters, ResourceIdListWorkChunkJson> executionDetails = new StepExecutionDetails<>(params, workChunk, jobInstance, workChunkId);
		myDeleteExpungeStep.run(executionDetails, myVoidSink);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(0)).endsWith("from HFJ_RES_LINK rl1_0 where (rl1_0.TARGET_RESOURCE_ID,rl1_0.TARGET_RES_PARTITION_ID) in (('" + pid + "','1'))");
		} else {
			assertThat(getSelectSql(0)).endsWith("from HFJ_RES_LINK rl1_0 where (rl1_0.TARGET_RESOURCE_ID) in ('" + pid + "')");
		}
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());

		myCaptureQueriesListener.logDeleteQueries();
		if (myIncludePartitionIdsInPks) {
			assertThat(getDeleteSql(0)).isEqualTo("DELETE FROM HFJ_HISTORY_TAG WHERE (PARTITION_ID,RES_ID) IN ((1," + pid + "))");
		} else {
			assertThat(getDeleteSql(0)).isEqualTo("DELETE FROM HFJ_HISTORY_TAG WHERE RES_ID IN (" + pid + ")");
		}
	}

	@Test
	public void testCreate_Conditional() throws JSQLParserException {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_2);
		createPatient(withActiveTrue()); // Just to pre-fetch the partition details
		myPartitionSettings.setConditionalCreateDuplicateIdentifiersEnabled(true);

		// Test
		myCaptureQueriesListener.clear();
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://foo").setValue("bar");
		DaoMethodOutcome outcome = myPatientDao.create(patient, "Patient?identifier=http://foo|bar", new SystemRequestDetails());
		long id = outcome.getId().getIdPartAsLong();

		// Verify
		assertTrue(outcome.getCreated());

		// Verify Select Queries

		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).startsWith("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE ((t0.PARTITION_ID = '2') AND (t0.HASH_SYS_AND_VALUE = '-2780914544385068076'))");
		} else {
			assertThat(getSelectSql(0)).startsWith("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_SYS_AND_VALUE = '-2780914544385068076')");
		}
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());

		// Verify Insert Queries

		myCaptureQueriesListener.logInsertQueries();
		assertEquals(5, myCaptureQueriesListener.countInsertQueries());
		assertEquals("HFJ_RESOURCE", parseInsertStatementTableName(getInsertSql(0)));
		assertEquals("HFJ_RES_VER", parseInsertStatementTableName(getInsertSql(1)));
		for (int i = 0; i < 4; i++) {
			String insertSql = getInsertSql(i);
			Map<String, String> insertColumns = parseInsertStatementParams(insertSql);
			String tableName = parseInsertStatementTableName(getInsertSql(i));
			if (myIncludePartitionIdsInSql) {
				assertEquals("'2'", insertColumns.get("PARTITION_ID"), insertSql);
				assertEquals("'" + id + "'", insertColumns.get("RES_ID"), insertSql);
			} else {
				if ("HFJ_RES_SEARCH_URL".equals(tableName)) {
					assertEquals("'-1'", insertColumns.get("PARTITION_ID"), insertSql);
				} else {
					assertEquals("NULL", insertColumns.get("PARTITION_ID"), insertSql);
				}
				assertEquals("'" + id + "'", insertColumns.get("RES_ID"), insertSql);
			}
		}

		// Verify no other queries

		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
	}


	@Test
	public void testCreate_ConceptMap() throws JSQLParserException {
		ConceptMap cm = new ConceptMap();
		cm.setId("cm");
		cm.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cm.setUrl("http://example.com/cm");
		ConceptMap.ConceptMapGroupComponent group = cm.addGroup();
		group.setSource("http://source");
		group.setTarget("http://target");
		ConceptMap.SourceElementComponent code0 = group.addElement().setCode("code0").setDisplay("display0");
		code0.addTarget().setCode("target0").setDisplay("target0display0");

		// Test
		myCaptureQueriesListener.clear();
		myConceptMapDao.update(cm, new SystemRequestDetails());

		// Verify
		myCaptureQueriesListener.logInsertQueries();

		String expectedPartitionId = "NULL";
		if (myPartitionSettings.isPartitioningEnabled()) {
			if (myPartitionSettings.getDefaultPartitionId() != null) {
				expectedPartitionId = "'" + myPartitionSettings.getDefaultPartitionId() + "'";
			}
		}

		List<SqlQuery> insertConceptMaps = myCaptureQueriesListener.getInsertQueries(t -> t.getSql(true, false).startsWith("insert into TRM_CONCEPT_MAP "));
		assertEquals(1, insertConceptMaps.size());
		assertEquals(expectedPartitionId, parseInsertStatementParams(insertConceptMaps.get(0).getSql(true, false)).get("PARTITION_ID"));

		List<SqlQuery> insertConceptMapGroups = myCaptureQueriesListener.getInsertQueries(t -> t.getSql(true, false).startsWith("insert into TRM_CONCEPT_MAP_GROUP "));
		assertEquals(1, insertConceptMapGroups.size());
		assertEquals(expectedPartitionId, parseInsertStatementParams(insertConceptMapGroups.get(0).getSql(true, false)).get("PARTITION_ID"));

		List<SqlQuery> insertConceptMapGroupElements = myCaptureQueriesListener.getInsertQueries(t -> t.getSql(true, false).startsWith("insert into TRM_CONCEPT_MAP_GRP_ELEMENT "));
		assertEquals(1, insertConceptMapGroupElements.size());
		assertEquals(expectedPartitionId, parseInsertStatementParams(insertConceptMapGroupElements.get(0).getSql(true, false)).get("PARTITION_ID"));

		List<SqlQuery> insertConceptMapGroupElementTargets = myCaptureQueriesListener.getInsertQueries(t -> t.getSql(true, false).startsWith("insert into TRM_CONCEPT_MAP_GRP_ELM_TGT "));
		assertEquals(1, insertConceptMapGroupElementTargets.size());
		assertEquals(expectedPartitionId, parseInsertStatementParams(insertConceptMapGroupElementTargets.get(0).getSql(true, false)).get("PARTITION_ID"));
	}

	@Test
	public void testCreate_CodeSystem() throws JSQLParserException {
		CodeSystem cs = new CodeSystem();
		cs.setId("cs");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setUrl("http://example.com/cs");
		cs.addConcept().setCode("code0").setDisplay("display0");

		// Test
		myCaptureQueriesListener.clear();
		myCodeSystemDao.update(cs, new SystemRequestDetails());

		// Verify
		myCaptureQueriesListener.logInsertQueries();

		String expectedPartitionId = "NULL";
		if (myPartitionSettings.isPartitioningEnabled()) {
			if (myPartitionSettings.getDefaultPartitionId() != null) {
				expectedPartitionId = "'" + myPartitionSettings.getDefaultPartitionId() + "'";
			}
		}

		List<SqlQuery> insertTrmCodeSystem = myCaptureQueriesListener.getInsertQueries(t -> t.getSql(true, false).startsWith("insert into TRM_CODESYSTEM "));
		assertEquals(1, insertTrmCodeSystem.size());
		assertEquals(expectedPartitionId, parseInsertStatementParams(insertTrmCodeSystem.get(0).getSql(true, false)).get("PARTITION_ID"));
		assertEquals("NULL", parseInsertStatementParams(insertTrmCodeSystem.get(0).getSql(true, false)).get("CURRENT_VERSION_PID"));
		assertEquals("NULL", parseInsertStatementParams(insertTrmCodeSystem.get(0).getSql(true, false)).get("CURRENT_VERSION_PARTITION_ID"));

		List<SqlQuery> insertTrmConcept = myCaptureQueriesListener.getInsertQueries(t -> t.getSql(true, false).startsWith("insert into TRM_CONCEPT "));
		assertEquals(1, insertTrmConcept.size());
		assertEquals(expectedPartitionId, parseInsertStatementParams(insertTrmConcept.get(0).getSql(true, false)).get("PARTITION_ID"));

		myCaptureQueriesListener.logUpdateQueries();
		List<SqlQuery> updateCodeSystems = myCaptureQueriesListener.getUpdateQueries(t -> t.getSql(true, false).startsWith("update TRM_CODESYSTEM "));
		assertEquals(1, updateCodeSystems.size());
		assertEquals(expectedPartitionId, parseUpdateStatementParams(updateCodeSystems.get(0).getSql(true, false)).get("CURRENT_VERSION_PARTITION_ID"));

		List<SqlQuery> updateCodeSystemVersions = myCaptureQueriesListener.getUpdateQueries(t -> t.getSql(true, false).startsWith("update TRM_CODESYSTEM_VER "));
		assertEquals(1, updateCodeSystemVersions.size());
	}


	@ParameterizedTest
	@EnumSource(PartitionSettings.CrossPartitionReferenceMode.class)
	public void testCreate_ReferenceToResourceInOtherPartition(PartitionSettings.CrossPartitionReferenceMode theAllowReferencesToCrossPartition) {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_2);
		myPartitionSettings.setAllowReferencesAcrossPartitions(theAllowReferencesToCrossPartition);
		IIdType patientId = createPatient(withActiveTrue());

		// Test
		ourLog.info("Starting to test testCreate_ReferenceToResourceInOtherPartition");
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		try {
			IIdType obsId = createObservation(withSubject(patientId));
			if (myIncludePartitionIdsInSql && theAllowReferencesToCrossPartition == PartitionSettings.CrossPartitionReferenceMode.NOT_ALLOWED) {
				runInTransaction(()->{
					List<ResourceTable> resources = myResourceTableDao.findAll();
					String failMessage = "Resources:\n * " + resources.stream().map(ResourceTable::toString).collect(Collectors.joining("\n * "));
					List<ResourceLink> resourceLinks = myResourceLinkDao.findAll();
					failMessage += "\n\nResource Links:\n * " + resourceLinks.stream().map(ResourceLink::toString).collect(Collectors.joining("\n * "));
					failMessage += "\n\nRegistered Interceptors:\n * " + myInterceptorService.getAllRegisteredInterceptors().stream().map(Object::toString).collect(Collectors.joining("\n * "));
					fail(failMessage);
				});
			} else {
				assertNotNull(obsId);
			}
		} catch (InvalidRequestException e) {
			if (myIncludePartitionIdsInSql) {
				assertEquals(PartitionSettings.CrossPartitionReferenceMode.NOT_ALLOWED, theAllowReferencesToCrossPartition);
				assertThat(e.getMessage()).contains("not found, specified in path: Observation.subject");
			} else {
				fail();
			}
		}
	}

	@Test
	public void testDelete() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		IIdType orgId = createOrganization(withName("ORG")).toUnqualifiedVersionless();
		IIdType id = createPatient(withActiveTrue(), withFamily("HOMER"), withOrganization(orgId)).toUnqualifiedVersionless();
		long idLong = id.getIdPartAsLong();

		// Test
		myCaptureQueriesListener.clear();
		myPatientDao.delete(id, new SystemRequestDetails());

		// Verify

		// Verify Select
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(0)).endsWith(" from HFJ_RESOURCE rt1_0 where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + idLong + "','1'))");
		} else {
			assertThat(getSelectSql(0)).endsWith(" from HFJ_RESOURCE rt1_0 where rt1_0.RES_ID='" + idLong + "'");
		}
		assertEquals(4, myCaptureQueriesListener.countSelectQueries());

		// Verify Insert
		myCaptureQueriesListener.logInsertQueries();
		assertThat(getInsertSql(0)).startsWith("insert into HFJ_RES_VER ");
		assertEquals(1, myCaptureQueriesListener.countInsertQueries());

		// Verify Update
		myCaptureQueriesListener.logUpdateQueries();
		if (myIncludePartitionIdsInPks) {
			assertThat(getUpdateSql(0)).contains("where RES_ID='" + idLong + "' and PARTITION_ID='1' and RES_VER='1'");
		} else {
			assertThat(getUpdateSql(0)).contains("where RES_ID='" + idLong + "' and RES_VER='1'");
		}
		assertEquals(1, myCaptureQueriesListener.countUpdateQueries());

		// Verify Delete
		myCaptureQueriesListener.logDeleteQueries();
		String deleteWhere;
		assertEquals("delete from HFJ_RES_SEARCH_URL rsue1_0 where (rsue1_0.RES_ID='" + idLong + "')", getDeleteSql(0));
		if (myIncludePartitionIdsInPks) {
			deleteWhere = "(risps1_0.RES_ID,risps1_0.PARTITION_ID)=('" + idLong + "','1')";
		} else {
			deleteWhere = "risps1_0.RES_ID='" + idLong + "'";
		}
		assertEquals("delete from HFJ_SPIDX_STRING risps1_0 where " + deleteWhere, getDeleteSql(1));
		assertEquals("delete from HFJ_SPIDX_TOKEN rispt1_0 where " + deleteWhere.replace("risps1_0", "rispt1_0"), getDeleteSql(2));
		if (myIncludePartitionIdsInPks) {
			assertEquals("delete from HFJ_RES_LINK rl1_0 where (rl1_0.SRC_RESOURCE_ID,rl1_0.PARTITION_ID)=('" + idLong + "','1')", getDeleteSql(3));
		} else {
			assertEquals("delete from HFJ_RES_LINK rl1_0 where rl1_0.SRC_RESOURCE_ID='" + idLong + "'", getDeleteSql(3));
		}
		assertEquals(4, myCaptureQueriesListener.countDeleteQueries());
	}

	@Test
	public void testHistory_Instance() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		IIdType id = myPatientDao.create(p, newRequest()).getId();
		assertEquals("1", id.getVersionIdPart());

		p.getIdentifierFirstRep().setValue("2");
		id = myPatientDao.update(p, newRequest()).getId();
		assertEquals("2", id.getVersionIdPart());

		p.getIdentifierFirstRep().setValue("3");
		id = myPatientDao.update(p, newRequest()).getId();
		assertEquals("3", id.getVersionIdPart());
		id = id.toUnqualifiedVersionless();

		// Test
		myCaptureQueriesListener.clear();
		IBundleProvider outcome;
		outcome = myPatientDao.history(id, new HistorySearchDateRangeParam(), newRequest());

		// Verify
		List<String> actualIds = toUnqualifiedIdValues(outcome);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actualIds).asList().containsExactlyInAnyOrder("Patient/" + id.getIdPart() + "/_history/3", "Patient/" + id.getIdPart() + "/_history/2", "Patient/" + id.getIdPart() + "/_history/1");

		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith("from HFJ_RESOURCE rt1_0 where rt1_0.PARTITION_ID='1' and rt1_0.RES_ID='" + id.getIdPartAsLong() + "'");
		} else {
			assertThat(getSelectSql(0)).endsWith("from HFJ_RESOURCE rt1_0 where rt1_0.RES_ID='" + id.getIdPartAsLong() + "'");
		}

		if (myIncludePartitionIdsInSql) {
			assertEquals("select count(*) from HFJ_RES_VER rht1_0 where rht1_0.RES_ID='" + id.getIdPartAsLong() + "' and rht1_0.PARTITION_ID='1'", getSelectSql(1));
		} else {
			assertEquals("select count(*) from HFJ_RES_VER rht1_0 where rht1_0.RES_ID='" + id.getIdPartAsLong() + "'", getSelectSql(1));
		}

		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(2)).contains(" from HFJ_RES_VER rht1_0 where rht1_0.RES_ID='" + id.getIdPartAsLong() + "' and rht1_0.PARTITION_ID='1'");
		} else {
			assertThat(getSelectSql(2)).contains(" from HFJ_RES_VER rht1_0 where rht1_0.RES_ID='" + id.getIdPartAsLong() + "' ");
		}

		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testHistory_Type() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		IIdType id = myPatientDao.create(p, newRequest()).getId();
		assertEquals("1", id.getVersionIdPart());

		p.getIdentifierFirstRep().setValue("2");
		id = myPatientDao.update(p, newRequest()).getId();
		assertEquals("2", id.getVersionIdPart());

		p.getIdentifierFirstRep().setValue("3");
		id = myPatientDao.update(p, newRequest()).getId();
		assertEquals("3", id.getVersionIdPart());
		id = id.toUnqualifiedVersionless();

		// Test
		myCaptureQueriesListener.clear();
		IBundleProvider outcome;
		outcome = myPatientDao.history(null, null, null, newRequest());

		// Verify
		List<String> actualIds = toUnqualifiedIdValues(outcome);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actualIds).asList().containsExactlyInAnyOrder("Patient/" + id.getIdPart() + "/_history/3", "Patient/" + id.getIdPart() + "/_history/2", "Patient/" + id.getIdPart() + "/_history/1");

		if (myIncludePartitionIdsInSql) {
			assertEquals("select count(*) from HFJ_RES_VER rht1_0 where rht1_0.PARTITION_ID in ('1') and rht1_0.RES_TYPE='Patient'", getSelectSql(0));
		} else {
			assertEquals("select count(*) from HFJ_RES_VER rht1_0 where rht1_0.RES_TYPE='Patient'", getSelectSql(0));
		}

		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(1)).contains(" from HFJ_RES_VER rht1_0 where rht1_0.PARTITION_ID in ('1') and rht1_0.RES_TYPE='Patient' ");
		} else {
			assertThat(getSelectSql(1)).contains(" from HFJ_RES_VER rht1_0 where rht1_0.RES_TYPE='Patient' ");
		}

		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testHistory_Server() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://foo").setValue("1");
		IIdType id = myPatientDao.create(p, newRequest()).getId();
		assertEquals("1", id.getVersionIdPart());

		p.getIdentifierFirstRep().setValue("2");
		id = myPatientDao.update(p, newRequest()).getId();
		assertEquals("2", id.getVersionIdPart());

		p.getIdentifierFirstRep().setValue("3");
		id = myPatientDao.update(p, newRequest()).getId();
		assertEquals("3", id.getVersionIdPart());
		id = id.toUnqualifiedVersionless();

		// Test
		myCaptureQueriesListener.clear();
		IBundleProvider outcome;
		outcome = mySystemDao.history(null, null, null, newRequest());

		// Verify
		List<String> actualIds = toUnqualifiedIdValues(outcome);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(actualIds).asList().containsExactlyInAnyOrder("Patient/" + id.getIdPart() + "/_history/3", "Patient/" + id.getIdPart() + "/_history/2", "Patient/" + id.getIdPart() + "/_history/1");

		if (myIncludePartitionIdsInSql) {
			assertEquals("select count(*) from HFJ_RES_VER rht1_0 where rht1_0.PARTITION_ID in ('1')", getSelectSql(0));
		} else {
			assertEquals("select count(*) from HFJ_RES_VER rht1_0", getSelectSql(0));
		}

		assertThat(getSelectSql(1)).contains(" from HFJ_RES_VER rht1_0 ");
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(1)).contains(" where rht1_0.PARTITION_ID in ('1') ");
		} else {
			assertThat(getSelectSql(1)).doesNotContain(" where ");
		}

		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}


	@Test
	public void testOperation_Everything() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		CreatedResourceIds ids = createPatientWithOrganizationAndEncounterReferences();
		myParentTest.logAllResources();

		// Test
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPatientDao.patientInstanceEverything(null, new SystemRequestDetails(), new PatientEverythingParameters(), ids.patientId);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		List<String> actualIds = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(actualIds).asList().containsExactlyInAnyOrder(ids.allIdValues().toArray(new String[0]));

		assertEquals(6, myCaptureQueriesListener.countSelectQueries());
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.PARTITION_ID='1' and (rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='" + ids.patientPid + "')");
		} else {
			assertThat(getSelectSql(0)).endsWith(" where (rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='" + ids.patientPid + "')");
		}

		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(1)).startsWith("SELECT DISTINCT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK");
		} else {
			assertThat(getSelectSql(1)).startsWith("SELECT DISTINCT t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK");
		}
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(1)).contains("WHERE ((t0.TARGET_RES_PARTITION_ID,t0.TARGET_RESOURCE_ID) IN (('1','" + ids.patientPid + "')) )");
			assertThat(getSelectSql(1)).contains("GROUP BY t0.PARTITION_ID,t0.SRC_RESOURCE_ID ");
			assertThat(getSelectSql(1)).contains("ORDER BY t0.PARTITION_ID,t0.SRC_RESOURCE_ID ");
			assertThat(getSelectSql(2)).containsAnyOf(
				"from HFJ_RES_LINK rl1_0 where rl1_0.PARTITION_ID='1' and rl1_0.SRC_RESOURCE_ID in ('" + ids.patientPid() + "','" + ids.encounterPid() + "') ",
				"from HFJ_RES_LINK rl1_0 where rl1_0.PARTITION_ID='1' and rl1_0.SRC_RESOURCE_ID in ('" + ids.encounterPid() + "','" + ids.patientPid() + "') "
			);
			assertThat(getSelectSql(3)).contains("from HFJ_RES_LINK rl1_0 where rl1_0.PARTITION_ID='1' and rl1_0.SRC_RESOURCE_ID in ('" + ids.childOrgPid() + "') ");
			assertThat(getSelectSql(4)).contains("from HFJ_RES_LINK rl1_0 where rl1_0.PARTITION_ID='1' and rl1_0.SRC_RESOURCE_ID in ('" + ids.parentOrgPid() + "') ");
		} else {
			assertThat(getSelectSql(1)).contains("WHERE (t0.TARGET_RESOURCE_ID = '" + ids.patientPid() + "') ");
			if (myIncludePartitionIdsInSql) {
				assertThat(getSelectSql(1)).contains(" GROUP BY t0.PARTITION_ID,t0.SRC_RESOURCE_ID ");
			} else {
				assertThat(getSelectSql(1)).contains(" GROUP BY t0.SRC_RESOURCE_ID ");
			}
			assertThat(getSelectSql(1)).endsWith(" ORDER BY t0.SRC_RESOURCE_ID fetch first '10000' rows only");
			assertThat(getSelectSql(2)).containsAnyOf(
				"from HFJ_RES_LINK rl1_0 where rl1_0.SRC_RESOURCE_ID in ('" + ids.patientPid() + "','" + ids.encounterPid() + "') ",
				"from HFJ_RES_LINK rl1_0 where rl1_0.SRC_RESOURCE_ID in ('" + ids.encounterPid() + "','" + ids.patientPid() + "') "
			);
			assertThat(getSelectSql(3)).contains("from HFJ_RES_LINK rl1_0 where rl1_0.SRC_RESOURCE_ID in ('" + ids.childOrgPid() + "') ");
			assertThat(getSelectSql(4)).contains("from HFJ_RES_LINK rl1_0 where rl1_0.SRC_RESOURCE_ID in ('" + ids.parentOrgPid() + "') ");
		}

	}

	@Test
	public void testOperation_ExpungeEverything() {
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		createPatient(withActiveTrue());
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_2);
		createPatient(withFamily("SIMPSON"), withBirthdate("2024-01-01"));

		// Test
		myCaptureQueriesListener.clear();
		myExpungeEverythingService.expungeEverything(new SystemRequestDetails());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		String sql;

		// Select HFJ_SPIDX_TOKEN
		List<SqlQuery> selectTokenQueries = getSqlSelectQueriesWithString(" HFJ_SPIDX_TOKEN ");
		if (myIncludePartitionIdsInPks) {
			sql = "select rispt1_0.SP_ID,rispt1_0.PARTITION_ID from HFJ_SPIDX_TOKEN rispt1_0 fetch first '400' rows only";
		} else {
			sql = "select rispt1_0.SP_ID from HFJ_SPIDX_TOKEN rispt1_0 fetch first '400' rows only";
		}
		assertThat(selectTokenQueries.get(0).getSql(true, false)).isEqualTo(sql);
		assertThat(selectTokenQueries.get(1).getSql(true, false)).isEqualTo(sql);
		assertEquals(2, selectTokenQueries.size());

		// Delete HFJ_SPIDX_TOKEN
		List<SqlQuery> deleteTokenQueries = getSqlDeleteQueriesWithString(" HFJ_SPIDX_TOKEN ");
		if (myIncludePartitionIdsInPks) {
			assertThat(deleteTokenQueries.get(0).getSql(true, false)).startsWith("delete from HFJ_SPIDX_TOKEN rispt1_0 where (rispt1_0.SP_ID,rispt1_0.PARTITION_ID) in ");
		} else {
			assertThat(deleteTokenQueries.get(0).getSql(true, false)).startsWith("delete from HFJ_SPIDX_TOKEN rispt1_0 where rispt1_0.SP_ID in ");
		}
		assertEquals(1, deleteTokenQueries.size(), ()-> "SQL:\n * " + deleteTokenQueries.stream().map(t->t.getSql(true, false)).collect(Collectors.joining("\n * ")));

		// Select HFJ_RES_VER
		List<SqlQuery> selectResVerQueries = getSqlSelectQueriesWithString(" HFJ_RES_VER ");
		if (myIncludePartitionIdsInPks) {
			sql = "select rht1_0.PARTITION_ID,rht1_0.PID from HFJ_RES_VER rht1_0 fetch first '400' rows only";
		} else {
			sql = "select rht1_0.PID from HFJ_RES_VER rht1_0 fetch first '400' rows only";
		}
		assertThat(selectResVerQueries.get(0).getSql(true, false)).isEqualTo(sql);
		assertThat(selectResVerQueries.get(1).getSql(true, false)).isEqualTo(sql);
		assertEquals(2, selectResVerQueries.size());

		// Select HFJ_RES_VER
		List<SqlQuery> deleteResVerQueries = getSqlDeleteQueriesWithString(" HFJ_RES_VER ");
		if (myIncludePartitionIdsInPks) {
			assertThat(deleteResVerQueries.get(0).getSql(true, false)).startsWith("delete from HFJ_RES_VER rht1_0 where (rht1_0.PARTITION_ID,rht1_0.PID) in ");
		} else {
			assertThat(deleteResVerQueries.get(0).getSql(true, false)).startsWith("delete from HFJ_RES_VER rht1_0 where (rht1_0.PID) in ");
		}
		assertEquals(1, deleteResVerQueries.size());
	}

	@Test
	public void testRead_DefaultPartition() {
		// Setup
		myPartitionSelectorInterceptor.addNonPartitionableResource("Organization");
		IIdType id = createOrganization(withId("O"), withName("PARENT"));
		long pid = findId("Organization", "O").getId();

		// Test
		myCaptureQueriesListener.clear();
		myMemoryCache.invalidateAllCaches();
		Organization actual = myOrganizationDao.read(id, new SystemRequestDetails());

		// Verify
		assertEquals("PARENT", actual.getName());
		myCaptureQueriesListener.logSelectQueries();

		if (myIncludePartitionIdsInSql) {
			if (myPartitionSettings.getDefaultPartitionId() == null) {
				assertThat(getSelectSql(0)).endsWith(" where rt1_0.PARTITION_ID is null and (rt1_0.RES_TYPE='Organization' and rt1_0.FHIR_ID='O')");
			} else {
				assertThat(getSelectSql(0)).endsWith(" where rt1_0.PARTITION_ID='0' and (rt1_0.RES_TYPE='Organization' and rt1_0.FHIR_ID='O')");
			}
		} else {
			assertThat(getSelectSql(0)).endsWith(" where (rt1_0.RES_TYPE='Organization' and rt1_0.FHIR_ID='O')");
		}

		if (myIncludePartitionIdsInSql) {
			if (myPartitionSettings.getDefaultPartitionId() == null) {
				assertThat(getSelectSql(1)).endsWith(" from HFJ_RESOURCE rt1_0 where rt1_0.PARTITION_ID is null and rt1_0.RES_ID='" + pid + "'");
			} else {
				assertThat(getSelectSql(1)).endsWith(" from HFJ_RESOURCE rt1_0 where rt1_0.PARTITION_ID='0' and rt1_0.RES_ID='" + pid + "'");
			}
		} else {
			assertThat(getSelectSql(1)).endsWith(" from HFJ_RESOURCE rt1_0 where rt1_0.RES_ID='" + pid + "'");
		}

		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(2)).endsWith(" from HFJ_RES_VER rht1_0 where (rht1_0.RES_ID,rht1_0.PARTITION_ID)=('" + pid + "','0') and rht1_0.RES_VER='1'");
		} else {
			assertThat(getSelectSql(2)).endsWith(" from HFJ_RES_VER rht1_0 where rht1_0.RES_ID='" + pid + "' and rht1_0.RES_VER='1'");
		}

		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testRead_ServerAssignedId() {
		// Setup
		myCaptureQueriesListener.clear();
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		long id = createPatient(withActiveTrue()).getIdPartAsLong();
		myParentTest.logAllResources();
		myCaptureQueriesListener.logInsertQueries();

		// Test
		myCaptureQueriesListener.clear();
		myPatientDao.read(new IdType("Patient/" + id), newRequest());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.PARTITION_ID='1' and rt1_0.RES_ID='" + id + "'");
		} else {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.RES_ID='" + id + "'");
		}
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(1)).endsWith("where (rht1_0.RES_ID,rht1_0.PARTITION_ID)=('" + id + "','1') and rht1_0.RES_VER='1'");
		} else {
			assertThat(getSelectSql(1)).endsWith(" where rht1_0.RES_ID='" + id + "' and rht1_0.RES_VER='1'");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testRead_ClientAssignedId() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		createPatient(withId("A"), withActiveTrue());

		long id = runInTransaction(() -> myResourceTableDao.findByTypeAndFhirId("Patient", "A").orElseThrow().getPersistentId().getId());

		// Test
		myCaptureQueriesListener.clear();
		myPatientDao.read(new IdType("Patient/A"), newRequest());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith(" from HFJ_RESOURCE rt1_0 where rt1_0.PARTITION_ID='1' and rt1_0.RES_ID='" + id + "'");
		} else {
			assertThat(getSelectSql(0)).endsWith(" from HFJ_RESOURCE rt1_0 where rt1_0.RES_ID='" + id + "'");
		}
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(1)).endsWith(" where (rht1_0.RES_ID,rht1_0.PARTITION_ID)=('" + id + "','1') and rht1_0.RES_VER='1'");
		} else {
			assertThat(getSelectSql(1)).endsWith(" where rht1_0.RES_ID='" + id + "' and rht1_0.RES_VER='1'");
		}
	}

	@Test
	public void testSearch_Contained() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		myStorageSettings.setIndexOnContainedResources(true);
		Patient p = new Patient();
		p.addName().setFamily("Smith");
		Observation obs = new Observation();
		obs.setSubject(new Reference(p));
		IIdType id = myObservationDao.create(obs, new SystemRequestDetails()).getId().toUnqualifiedVersionless();

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add("subject", new ReferenceParam("name", "Smith"));
		IBundleProvider outcome = myObservationDao.search(map, new SystemRequestDetails());
		List<String> results = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		Assertions.assertThat(results).containsExactlyInAnyOrder(id.getValue());
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(0)).startsWith("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.PARTITION_ID,t0.RES_ID) IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_STRING t0 ");
		} else if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).startsWith("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (t0.RES_ID IN (SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 ");
		} else {
			assertThat(getSelectSql(0)).startsWith("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (t0.RES_ID IN (SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 ");
		}

		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).contains("t0.PARTITION_ID = '1'");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());

	}

	@Test
	public void testSearch_Chained() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		IIdType patientId = createPatient(withFamily("NAME")).toUnqualifiedVersionless();
		IIdType observationId = createObservation(withSubject(patientId)).toUnqualifiedVersionless();

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous(Observation.SP_PATIENT, new ReferenceParam("family", "NAME"));
		IBundleProvider outcome = myObservationDao.search(params, new SystemRequestDetails());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactly(observationId.getValue());
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).contains("SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 ");
		} else {
			assertThat(getSelectSql(0)).contains("SELECT t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 ");
		}
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(0)).contains("INNER JOIN HFJ_SPIDX_STRING t1 ON ((t0.TARGET_RES_PARTITION_ID = t1.PARTITION_ID) AND (t0.TARGET_RESOURCE_ID = t1.RES_ID))");
		} else {
			assertThat(getSelectSql(0)).contains("INNER JOIN HFJ_SPIDX_STRING t1 ON (t0.TARGET_RESOURCE_ID = t1.RES_ID)");
		}
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).contains("t1.PARTITION_ID = '1'");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());

	}

	@Test
	public void testSearch_Has() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);

		IIdType patientId = createPatient(withActiveTrue()).toUnqualifiedVersionless();
		IIdType observationId = createObservation(withSubject(patientId)).toUnqualifiedVersionless();
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(PARAM_HAS, new HasParam("Observation", "patient", "_id", observationId.getValue()));
		IBundleProvider outcome = myPatientDao.search(params, new SystemRequestDetails());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactly(patientId.getValue());
	}

	@ParameterizedTest
	@ValueSource(booleans = {false}) // TODO: True will be added in the next PR
	public void testSearch_IdParam(boolean theIncludeOtherParameter) {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);

		IIdType id0 = createPatient(withActiveTrue()).toUnqualifiedVersionless();
		IIdType id1 = createPatient(withId("A"), withActiveTrue()).toUnqualifiedVersionless();

		myMemoryCache.invalidateAllCaches();
		myParentTest.preFetchPartitionsIntoCache();

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		if (theIncludeOtherParameter) {
			params.add(Patient.SP_ACTIVE, new TokenParam("true"));
		}
		params.add(SP_RES_ID, new TokenOrListParam().add(id0.getValue()).add(id1.getValue()));
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).asList().containsExactlyInAnyOrder(id0.getValue(), id1.getValue());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.PARTITION_ID='1' and (rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='" + id0.getIdPart() + "' or rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='A')");
		} else {
			assertThat(getSelectSql(0)).endsWith(" where (rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='" + id0.getIdPart() + "' or rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='A')");
		}
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(1)).contains(" WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID = '1') AND (t0.RES_ID IN ");
		} else {
			assertThat(getSelectSql(1)).contains(" WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID IN ");
		}
		assertEquals(3, myCaptureQueriesListener.countSelectQueries());

	}

	@Test
	public void testSearch_ListParam() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		IIdType patId0 = createPatient(withActiveTrue()).toUnqualifiedVersionless();
		IIdType patId1 = createPatient(withActiveTrue()).toUnqualifiedVersionless();
		IIdType listId = createList(withListItem(patId0), withListItem(patId1)).toUnqualifiedVersionless();
		Long listIdLong = listId.getIdPartAsLong();

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Constants.PARAM_LIST, new TokenParam(listId.getValue()));
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).asList().containsExactlyInAnyOrder(patId0.getValue(), patId1.getValue());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(getSelectSql(0)).contains(" FROM HFJ_RESOURCE t1 ");
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_RES_LINK t0 ON ((t1.PARTITION_ID = t0.PARTITION_ID) AND (t1.RES_ID = t0.TARGET_RESOURCE_ID)) ");
			assertThat(getSelectSql(0)).endsWith(" WHERE ((t0.SRC_PATH = 'List.entry.item') AND (t0.TARGET_RESOURCE_TYPE = 'Patient') AND ((t0.PARTITION_ID,t0.SRC_RESOURCE_ID) IN (('1','" + listIdLong + "')) )) fetch first '10000' rows only");
		} else {
			assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_RES_LINK t0 ON (t1.RES_ID = t0.TARGET_RESOURCE_ID) ");
			if (myIncludePartitionIdsInSql) {
				assertThat(getSelectSql(0)).endsWith(" WHERE ((t0.PARTITION_ID = '1') AND (t0.SRC_PATH = 'List.entry.item') AND (t0.TARGET_RESOURCE_TYPE = 'Patient') AND (t0.SRC_RESOURCE_ID = '" + listIdLong + "')) fetch first '10000' rows only");
			} else {
				assertThat(getSelectSql(0)).endsWith(" WHERE ((t0.SRC_PATH = 'List.entry.item') AND (t0.TARGET_RESOURCE_TYPE = 'Patient') AND (t0.SRC_RESOURCE_ID = '" + listIdLong + "')) fetch first '10000' rows only");
			}
		}

		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

	/**
	 * Perform a search where the request partition ID includes multiple partitions
	 */
	@Test
	public void testSearch_MultiPartition() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		IIdType id0 = createPatient(withActiveTrue(), withFamily("A")).toUnqualifiedVersionless();
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_2);
		IIdType id1 = createPatient(withActiveFalse(), withFamily("B")).toUnqualifiedVersionless();

		// Test
		myPartitionSelectorInterceptor.setNextPartition(RequestPartitionId.fromPartitionIds(PARTITION_1, PARTITION_2));
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous()
			.setSort(new SortSpec(Patient.SP_FAMILY));
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).asList().containsExactlyInAnyOrder(id0.getValue(), id1.getValue());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInPks) {
			assertEquals("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_SPIDX_STRING t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.RES_ID) AND (t1.HASH_IDENTITY = '-9208284524139093953')) WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID IN ('1','2') )) ORDER BY t1.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first '10000' rows only", getSelectSql(0));
			assertThat(getSelectSql(1)).contains(" where (rht1_0.RES_ID,rht1_0.PARTITION_ID) in (('" + id0.getIdPartAsLong() + "','1'),('" + id1.getIdPartAsLong() + "','2'),('-1',NULL),('-1',NULL),('-1',NULL),('-1',NULL),('-1',NULL),('-1',NULL),('-1',NULL),('-1',NULL)) and mrt1_0.RES_VER=rht1_0.RES_VER");
		} else if (myIncludePartitionIdsInSql) {
			assertEquals("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_SPIDX_STRING t1 ON ((t0.RES_ID = t1.RES_ID) AND (t1.HASH_IDENTITY = '-9208284524139093953')) WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID IN ('1','2') )) ORDER BY t1.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first '10000' rows only", getSelectSql(0));
			assertThat(getSelectSql(1)).contains(" where (rht1_0.RES_ID) in ('" + id0.getIdPartAsLong() + "','" + id1.getIdPartAsLong() + "','-1','-1','-1','-1','-1','-1','-1','-1') and mrt1_0.RES_VER=rht1_0.RES_VER");
		} else {
			assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_SPIDX_STRING t1 ON ((t0.RES_ID = t1.RES_ID) AND (t1.HASH_IDENTITY = '-9208284524139093953')) WHERE ((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) ORDER BY t1.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first '10000' rows only", getSelectSql(0));
			assertThat(getSelectSql(1)).contains(" where (rht1_0.RES_ID) in ('" + id0.getIdPartAsLong() + "','" + id1.getIdPartAsLong() + "','-1','-1','-1','-1','-1','-1','-1','-1') and mrt1_0.RES_VER=rht1_0.RES_VER");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearch_Source(boolean theAccessMetaSourceInformationFromProvenanceTable) {
		// Setup
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(theAccessMetaSourceInformationFromProvenanceTable);
		myStorageSettings.setStoreMetaSourceInformation(JpaStorageSettings.StoreMetaSourceInformationEnum.SOURCE_URI_AND_REQUEST_ID);
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		long idFoo = createPatient(withActiveTrue(), withSource("http://foo")).getIdPartAsLong();
		long idBar = createPatient(withActiveTrue(), withSource("http://bar")).getIdPartAsLong();

		runInTransaction(()->{
			ResourceTable table = myResourceTableDao.getReferenceById(JpaPid.fromId(idFoo, 1));
			ResourceHistoryProvenanceEntity prov = new ResourceHistoryProvenanceEntity();
			prov.setResourceTable(table);
			prov.setResourceHistoryTable(myResourceHistoryTableDao.findForIdAndVersion(table.getResourceId().toFk(), 1));
			prov.setSourceUri("http://foo");
			myResourceHistoryProvenanceTableDao.save(prov);

			table = myResourceTableDao.getReferenceById(JpaPid.fromId(idBar, 1));
			prov = new ResourceHistoryProvenanceEntity();
			prov.setResourceTable(table);
			prov.setResourceHistoryTable(myResourceHistoryTableDao.findForIdAndVersion(table.getResourceId().toFk(), 1));
			prov.setSourceUri("http://bar");
			myResourceHistoryProvenanceTableDao.save(prov);
		});

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.add(PARAM_SOURCE, new TokenParam("http://foo"));
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactly("Patient/" + idFoo);

		if (myIncludePartitionIdsInPks) {
			if (theAccessMetaSourceInformationFromProvenanceTable) {
				assertEquals("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_VER_PROV t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.RES_PID)) WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t1.SOURCE_URI = 'http://foo')) fetch first '10000' rows only", getSelectSql(0));
			} else {
				assertEquals("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_VER t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.RES_ID)) WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t1.SOURCE_URI = 'http://foo')) fetch first '10000' rows only", getSelectSql(0));
			}
		} else if (myIncludePartitionIdsInSql) {
			if (theAccessMetaSourceInformationFromProvenanceTable) {
				assertEquals("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_VER_PROV t1 ON (t0.RES_ID = t1.RES_PID) WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t1.SOURCE_URI = 'http://foo')) fetch first '10000' rows only", getSelectSql(0));
			} else {
				assertEquals("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_VER t1 ON (t0.RES_ID = t1.RES_ID) WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t1.SOURCE_URI = 'http://foo')) fetch first '10000' rows only", getSelectSql(0));
			}
		} else {
			if (theAccessMetaSourceInformationFromProvenanceTable) {
				assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_VER_PROV t1 ON (t0.RES_ID = t1.RES_PID) WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t1.SOURCE_URI = 'http://foo')) fetch first '10000' rows only", getSelectSql(0));
			} else {
				assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_VER t1 ON (t0.RES_ID = t1.RES_ID) WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t1.SOURCE_URI = 'http://foo')) fetch first '10000' rows only", getSelectSql(0));
			}
		}

		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

		@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearch_Tags_Versioned(boolean theNegate) {
		// Setup
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.VERSIONED);
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		long idBar = createPatient(withActiveTrue(), withTag("http://foo", "bar")).getIdPartAsLong();
		long idBaz = createPatient(withActiveTrue(), withTag("http://foo", "baz")).getIdPartAsLong();
		long id = theNegate ? idBaz : idBar;

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		TokenParam bar = new TokenParam("http://foo", "bar");
		if (theNegate) {
			bar.setModifier(TokenParamModifier.NOT);
		}
		params.add(PARAM_TAG, bar);
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactly("Patient/" + id);

		if (theNegate) {
			if (myIncludePartitionIdsInPks) {
				assertThat(getSelectSql(0)).contains("((t0.PARTITION_ID,t0.RES_ID) NOT IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RES_TAG t0");
			} else {
				assertThat(getSelectSql(0)).contains("t0.RES_ID NOT IN (SELECT t0.RES_ID FROM HFJ_RES_TAG t0 ");
			}
			assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_TAG_DEF t1 ON (t0.TAG_ID = t1.TAG_ID) ");
		} else {
			if (myIncludePartitionIdsInPks) {
				assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_RES_TAG t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.RES_ID)) INNER");
			} else {
				assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) INNER");
			}
			assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_TAG_DEF t2 ON (t1.TAG_ID = t2.TAG_ID) ");
		}

		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).contains("PARTITION_ID = '1')");
		}

		// Query 1 is the HFJ_RES_VER fetch
		assertThat(getSelectSql(1)).contains(" from HFJ_RES_VER ");

		assertThat(getSelectSql(2)).contains(" from HFJ_HISTORY_TAG rht1_0 ");
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(2)).contains(" where (rht1_0.PARTITION_ID,rht1_0.RES_VER_PID) in (('1',");
		} else {
			assertThat(getSelectSql(2)).contains(" where (rht1_0.RES_VER_PID) in ('");
		}

		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testSearch_Tags_Unversioned() {
		// Setup
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		long id = createPatient(withActiveTrue(), withTag("http://foo", "bar")).getIdPartAsLong();

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(PARAM_TAG, new TokenParam("http://foo", "bar"));
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactly("Patient/" + id);

		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_RES_TAG t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.RES_ID)) INNER");
		} else {
			assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) INNER");
		}
		assertThat(getSelectSql(0)).contains(" INNER JOIN HFJ_TAG_DEF t2 ON (t1.TAG_ID = t2.TAG_ID) ");
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).contains("(t1.PARTITION_ID = '1')");
		}

		// Query 1 is the HFJ_RES_VER fetch
		assertThat(getSelectSql(1)).contains(" from HFJ_RES_VER ");

		assertThat(getSelectSql(2)).contains(" from HFJ_RES_TAG rt1_0 ");
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(2)).contains(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		} else {
			assertThat(getSelectSql(2)).contains(" where (rt1_0.RES_ID) in ('" + id + "')");
		}

		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testSearch_Token() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		long id = createPatient(withActiveTrue()).getIdPartAsLong();

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_ACTIVE, new TokenParam().setValue("true"));
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).asList().containsExactly("Patient/" + id);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith(" WHERE ((t0.PARTITION_ID = '1') AND (t0.HASH_VALUE = '7943378963388545453')) fetch first '10000' rows only");
		} else {
			assertThat(getSelectSql(0)).endsWith(" WHERE (t0.HASH_VALUE = '7943378963388545453') fetch first '10000' rows only");
		}
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(1)).endsWith(" where (rht1_0.RES_ID,rht1_0.PARTITION_ID) in (('" + id + "','1')) and mrt1_0.RES_VER=rht1_0.RES_VER");
		} else {
			assertThat(getSelectSql(1)).endsWith(" where (rht1_0.RES_ID) in ('" + id + "') and mrt1_0.RES_VER=rht1_0.RES_VER");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testSearch_Token_Not() {
		// Setup

		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		createObservation(withId("A"), withObservationCode("http://foo", "A"));
		createObservation(withId("B"), withObservationCode("http://foo", "B"));

		// Test
		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Observation.SP_CODE, new TokenParam("http://foo", "B").setModifier(TokenParamModifier.NOT));
		IBundleProvider outcome = myObservationDao.search(params, newRequest());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).asList().containsExactly("Observation/A");
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(0)).contains("((t0.PARTITION_ID,t0.RES_ID) NOT IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_TOKEN");
		} else {
			assertThat(getSelectSql(0)).contains("((t0.RES_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testSearch_Includes_Forward_Star() {
		// Setup
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		myPartitionSelectorInterceptor.addNonPartitionableResource("Organization");
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		CreatedResourceIds ids = createPatientWithOrganizationReferences();

		// Test
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.addInclude(IBaseResource.INCLUDE_ALL.asRecursive());
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(values).asList().containsExactlyInAnyOrder("Patient/" + ids.patientPid(), "Organization/" + ids.parentOrgId().getIdPart(), "Organization/" + ids.childOrgId().getIdPart());

		// Verify
		myCaptureQueriesListener.logSelectQueries();

		String sql;

		sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		if (myIncludePartitionIdsInSql) {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = '1')) fetch first '10000' rows only");
		} else {
			assertThat(sql).isEqualTo("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) fetch first '10000' rows only");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.PARTITION_ID='1' and rl1_0.SRC_RESOURCE_ID in ('" + ids.patientPid() + "') fetch");
		} else {
			assertThat(sql).contains("where rl1_0.SRC_RESOURCE_ID in ('" + ids.patientPid() + "') fetch ");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(2).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.PARTITION_ID='0' and rl1_0.SRC_RESOURCE_ID in ('" + ids.childOrgPid() + "') ");
		} else {
			assertThat(sql).contains("where rl1_0.SRC_RESOURCE_ID in ('" + ids.childOrgPid() + "') fetch ");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(3).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.PARTITION_ID='0' and rl1_0.SRC_RESOURCE_ID in ('" + ids.parentOrgPid() + "') ");
		} else {
			assertThat(sql).contains("where rl1_0.SRC_RESOURCE_ID in ('" + ids.parentOrgPid() + "') fetch ");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(4).getSql(true, false);
		assertThat(sql).contains("from HFJ_RES_VER rht1_0");
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("join HFJ_RESOURCE mrt1_0 on mrt1_0.RES_ID=rht1_0.RES_ID and mrt1_0.PARTITION_ID=rht1_0.PARTITION_ID where");
			assertThat(sql).contains("where (rht1_0.RES_ID,rht1_0.PARTITION_ID) in");
		} else {
			assertThat(sql).contains("join HFJ_RESOURCE mrt1_0 on mrt1_0.RES_ID=rht1_0.RES_ID where");
			assertThat(sql).contains("where (rht1_0.RES_ID) in");
		}

		assertEquals(5, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testSearch_Includes_Forward_Star_UsingCanonicalUrl() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		CreatedQuestionnaireAndResponseIds ids = createQuestionnaireAndQuestionnaireResponseWithCanonicalUrlLink();

		// Test
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();
		myParentTest.logAllUriIndexes();
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.addInclude(IBaseResource.INCLUDE_ALL.asRecursive());
		IBundleProvider outcome = myQuestionnaireResponseDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactlyInAnyOrder(ids.qId().getValue(), ids.qrId().getValue());

		String sql;

		sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		if (myIncludePartitionIdsInSql) {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'QuestionnaireResponse') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = '1')) fetch first '10000' rows only");
		} else {
			assertThat(sql).isEqualTo("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.RES_TYPE = 'QuestionnaireResponse') AND (t0.RES_DELETED_AT IS NULL)) fetch first '10000' rows only");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).isEqualTo("select rl1_0.TARGET_RESOURCE_ID,rl1_0.TARGET_RESOURCE_TYPE,rl1_0.TARGET_RESOURCE_URL,rl1_0.TARGET_RES_PARTITION_ID from HFJ_RES_LINK rl1_0 where rl1_0.PARTITION_ID='1' and rl1_0.SRC_RESOURCE_ID in ('" + ids.qrId.getIdPart() + "') fetch first '1000' rows only");
		} else {
			assertThat(sql).isEqualTo("select rl1_0.TARGET_RESOURCE_ID,rl1_0.TARGET_RESOURCE_TYPE,rl1_0.TARGET_RESOURCE_URL from HFJ_RES_LINK rl1_0 where rl1_0.SRC_RESOURCE_ID in ('" + ids.qrId().getIdPart() + "') fetch first '1000' rows only");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(2).getSql(true, false);
		if (myIncludePartitionIdsInSql) {
			assertThat(sql).startsWith("select rispu1_0.PARTITION_ID,rispu1_0.RES_ID from HFJ_SPIDX_URI rispu1_0 where rispu1_0.HASH_IDENTITY in (");
		} else {
			assertThat(sql).startsWith("select rispu1_0.RES_ID from HFJ_SPIDX_URI rispu1_0 where rispu1_0.HASH_IDENTITY in (");
		}

		assertEquals(5, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testSearch_Includes_Forward_Specific() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		CreatedResourceIds ids = createPatientWithOrganizationReferences();

		// Test
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.addInclude(Patient.INCLUDE_ORGANIZATION.asRecursive());
		params.addInclude(Organization.INCLUDE_PARTOF.asRecursive());
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(values).asList().containsExactlyInAnyOrder("Patient/" + ids.patientPid(), "Organization/" + ids.parentOrgId.getIdPart(), "Organization/" + ids.childOrgId.getIdPart());

		// Verify
		myCaptureQueriesListener.logSelectQueries();

		String sql;

		sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		if (myIncludePartitionIdsInSql) {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = '1')) fetch first '10000' rows only");
		} else {
			assertThat(sql).isEqualTo("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) fetch first '10000' rows only");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("WHERE r.src_path = 'Organization.partOf' AND r.target_resource_id IS NOT NULL AND r.src_resource_id IN ('" + ids.patientPid + "') AND r.partition_id = '1' AND r.target_resource_type = 'Organization' UNION");
		} else {
			assertThat(sql).contains("WHERE r.src_path = 'Organization.partOf' AND r.target_resource_id IS NOT NULL AND r.src_resource_id IN ('" + ids.patientPid + "') AND r.target_resource_type = 'Organization' UNION");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(2).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("WHERE r.src_path = 'Patient.managingOrganization' AND r.target_resource_id IS NOT NULL AND r.src_resource_id IN ('" + ids.patientPid + "') AND r.partition_id = '1' AND r.target_resource_type = 'Organization' UNION");
		} else {
			assertThat(sql).contains("WHERE r.src_path = 'Patient.managingOrganization' AND r.target_resource_id IS NOT NULL AND r.src_resource_id IN ('" + ids.patientPid + "') AND r.target_resource_type = 'Organization' UNION");
		}

		// Index 3-6 are just more includes loading
		assertThat(myCaptureQueriesListener.getSelectQueries().get(3).getSql(true, false)).contains(" FROM hfj_res_link r ");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(4).getSql(true, false)).contains(" FROM hfj_res_link r ");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(5).getSql(true, false)).contains(" FROM hfj_res_link r ");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(6).getSql(true, false)).contains(" FROM hfj_res_link r ");

		sql = myCaptureQueriesListener.getSelectQueries().get(7).getSql(true, false);
		assertThat(sql).contains("from HFJ_RES_VER rht1_0");
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("join HFJ_RESOURCE mrt1_0 on mrt1_0.RES_ID=rht1_0.RES_ID and mrt1_0.PARTITION_ID=rht1_0.PARTITION_ID where");
			assertThat(sql).contains("where (rht1_0.RES_ID,rht1_0.PARTITION_ID) in");
		} else {
			assertThat(sql).contains("join HFJ_RESOURCE mrt1_0 on mrt1_0.RES_ID=rht1_0.RES_ID where");
			assertThat(sql).contains("where (rht1_0.RES_ID) in");
		}

		assertEquals(8, myCaptureQueriesListener.countSelectQueries());
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearch_Includes_Forward_Specific_UsingCanonicalUrl(boolean theIncludePartitionInSearchHashes) {
		// Setup
		myPartitionSettings.setIncludePartitionInSearchHashes(theIncludePartitionInSearchHashes);
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		CreatedQuestionnaireAndResponseIds result = createQuestionnaireAndQuestionnaireResponseWithCanonicalUrlLink();

		// Test
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();
		myParentTest.logAllUriIndexes();
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.addInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
		IBundleProvider outcome = myQuestionnaireResponseDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactlyInAnyOrder(result.qrId().getValue(), result.qId().getValue());

		String sql;

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		sql = sql.substring(sql.indexOf("UNION"));
		long expectedHash;
		if (theIncludePartitionInSearchHashes && myIncludePartitionIdsInSql && myPartitionSettings.getDefaultPartitionId() != null) {
			expectedHash = -2559752747310040606L;
		} else {
			expectedHash = -600769180185160063L;
		}
		if (myIncludePartitionIdsInPks) {
			assertEquals("UNION SELECT rUri.res_id, rUri.partition_id as partition_id FROM hfj_res_link r JOIN hfj_spidx_uri rUri ON (rUri.partition_id IN ('0') AND rUri.hash_identity = '" + expectedHash + "' AND r.target_resource_url = rUri.sp_uri) WHERE r.src_path = 'QuestionnaireResponse.questionnaire' AND r.target_resource_id IS NULL AND r.partition_id = '1' AND r.src_resource_id IN ('" + result.qrId.getIdPart() + "') fetch first '1000' rows only", sql);
		} else {
			assertEquals("UNION SELECT rUri.res_id FROM hfj_res_link r JOIN hfj_spidx_uri rUri ON (rUri.hash_identity = '" + expectedHash + "' AND r.target_resource_url = rUri.sp_uri) WHERE r.src_path = 'QuestionnaireResponse.questionnaire' AND r.target_resource_id IS NULL AND r.src_resource_id IN ('" + result.qrId().getIdPart() + "') fetch first '1000' rows only", sql);
		}

		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
	}

	@Test
	public void testSearch_Includes_Reverse_Specific_UsingCanonicalUrl() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		CreatedQuestionnaireAndResponseIds result = createQuestionnaireAndQuestionnaireResponseWithCanonicalUrlLink();

		// Test
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();
		myParentTest.logAllUriIndexes();
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		params.addRevInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
		IBundleProvider outcome = myQuestionnaireDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactlyInAnyOrder(result.qrId().getValue(), result.qId().getValue());

		String sql;

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		sql = sql.substring(sql.indexOf("UNION"));
		if (myIncludePartitionIdsInPks) {
			assertEquals("UNION SELECT r.src_resource_id, r.partition_id as partition_id FROM hfj_res_link r JOIN hfj_spidx_uri rUri ON (rUri.partition_id IN ('0') AND rUri.hash_identity = '-600769180185160063' AND r.target_resource_url = rUri.sp_uri) WHERE r.src_path = 'QuestionnaireResponse.questionnaire' AND r.target_resource_id IS NULL AND rUri.partition_id = '0' AND rUri.res_id IN ('" + result.qId.getIdPart() + "') fetch first '1000' rows only", sql);
		} else {
			assertEquals("UNION SELECT r.src_resource_id FROM hfj_res_link r JOIN hfj_spidx_uri rUri ON (rUri.hash_identity = '-600769180185160063' AND r.target_resource_url = rUri.sp_uri) WHERE r.src_path = 'QuestionnaireResponse.questionnaire' AND r.target_resource_id IS NULL AND rUri.res_id IN ('" + result.qId().getIdPart() + "') fetch first '1000' rows only", sql);
		}

		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
	}

	@Nonnull
	private CreatedQuestionnaireAndResponseIds createQuestionnaireAndQuestionnaireResponseWithCanonicalUrlLink() {
		Questionnaire q = new Questionnaire();
		q.setUrl("http://foo");
		IIdType qId = myQuestionnaireDao.create(q, newRequest()).getId().toUnqualifiedVersionless();

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setQuestionnaire("http://foo");
		IIdType qrId = myQuestionnaireResponseDao.create(qr, newRequest()).getId().toUnqualifiedVersionless();
		CreatedQuestionnaireAndResponseIds result = new CreatedQuestionnaireAndResponseIds(qId, qrId);
		return result;
	}

	private record CreatedQuestionnaireAndResponseIds(IIdType qId, IIdType qrId) {
	}

	@Test
	public void testSearch_Includes_Reverse_Star() {
		// Setup
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		myPartitionSelectorInterceptor.addNonPartitionableResource("Organization");
		CreatedResourceIds ids = createPatientWithOrganizationReferences();

		// Test
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.add(SP_RES_ID, new TokenParam("Organization/" + ids.parentOrgPid()));
		params.setLoadSynchronous(true);
		params.addRevInclude(IBaseResource.INCLUDE_ALL.asRecursive());
		IBundleProvider outcome = myOrganizationDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(values).asList().containsExactlyInAnyOrder("Patient/" + ids.patientPid(), "Organization/" + ids.parentOrgId().getIdPart(), "Organization/" + ids.childOrgId().getIdPart());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(5, myCaptureQueriesListener.countSelectQueries());

		String sql;

		sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		if (myIncludePartitionIdsInSql && myPartitionSettings.getDefaultPartitionId() == null) {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Organization') AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID IS NULL) AND (t0.RES_ID = '" + ids.parentOrgPid() + "'))) fetch first '10000' rows only");
		} else if (myIncludePartitionIdsInSql) {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Organization') AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID = '0') AND (t0.RES_ID = '" + ids.parentOrgPid() + "'))) fetch first '10000' rows only");
		} else {
			assertThat(sql).isEqualTo("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Organization') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID = '" + ids.parentOrgPid() + "')) fetch first '10000' rows only");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.TARGET_RES_PARTITION_ID='0' and rl1_0.TARGET_RESOURCE_ID in ('" + ids.parentOrgPid() + "') fetch");
		} else {
			assertThat(sql).contains("where rl1_0.TARGET_RESOURCE_ID in ('" + ids.parentOrgPid() + "') fetch");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(2).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.TARGET_RES_PARTITION_ID='0' and rl1_0.TARGET_RESOURCE_ID in ('" + ids.childOrgPid() + "') ");
		} else {
			assertThat(sql).contains("where rl1_0.TARGET_RESOURCE_ID in ('" + ids.childOrgPid() + "') ");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(3).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.TARGET_RES_PARTITION_ID='1' and rl1_0.TARGET_RESOURCE_ID in ('" + ids.patientPid() + "') fetch");
		} else {
			assertThat(sql).contains("where rl1_0.TARGET_RESOURCE_ID in ('" + ids.patientPid() + "') fetch");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(4).getSql(true, false);
		assertThat(sql).contains("from HFJ_RES_VER rht1_0");
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("join HFJ_RESOURCE mrt1_0 on mrt1_0.RES_ID=rht1_0.RES_ID and mrt1_0.PARTITION_ID=rht1_0.PARTITION_ID where");
			assertThat(sql).contains("where (rht1_0.RES_ID,rht1_0.PARTITION_ID) in");
		} else {
			assertThat(sql).contains("join HFJ_RESOURCE mrt1_0 on mrt1_0.RES_ID=rht1_0.RES_ID where");
			assertThat(sql).contains("where (rht1_0.RES_ID) in");
		}
	}

	@Test
	public void testSearch_Includes_Reverse_Specific() {
		// Setup
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		myPartitionSelectorInterceptor.addNonPartitionableResource("Organization");
		CreatedResourceIds ids = createPatientWithOrganizationReferences();

		// Test
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.add(SP_RES_ID, new TokenParam("Organization/" + ids.parentOrgPid()));
		params.setLoadSynchronous(true);
		params.addRevInclude(Patient.INCLUDE_ORGANIZATION.asRecursive());
		params.addRevInclude(Organization.INCLUDE_PARTOF.asRecursive());
		IBundleProvider outcome = myOrganizationDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(values).asList().containsExactlyInAnyOrder("Patient/" + ids.patientPid(), "Organization/" + ids.parentOrgId.getIdPart(), "Organization/" + ids.childOrgId.getIdPart());

		String sql;

		sql = myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false);
		if (myIncludePartitionIdsInSql && myPartitionSettings.getDefaultPartitionId() == null) {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Organization') AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID IS NULL) AND (t0.RES_ID = '" + ids.parentOrgPid() + "'))) fetch first '10000' rows only");
		} else if (myIncludePartitionIdsInSql) {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Organization') AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID = '0') AND (t0.RES_ID = '" + ids.parentOrgPid() + "'))) fetch first '10000' rows only");
		} else {
			assertThat(sql).isEqualTo("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Organization') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.RES_ID = '" + ids.parentOrgPid() + "')) fetch first '10000' rows only");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("WHERE r.src_path = 'Organization.partOf' AND r.target_resource_id IN ('" + ids.parentOrgPid() + "') AND r.target_res_partition_id = '0' AND r.target_resource_type = 'Organization' ");
		} else {
			assertThat(sql).contains("WHERE r.src_path = 'Organization.partOf' AND r.target_resource_id IN ('" + ids.parentOrgPid() + "') AND r.target_resource_type = 'Organization' ");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(2).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("WHERE r.src_path = 'Patient.managingOrganization' AND r.target_resource_id IN ('" + ids.parentOrgPid + "') AND r.target_res_partition_id = '0' AND r.target_resource_type = 'Organization' UNION");
		} else {
			assertThat(sql).contains("WHERE r.src_path = 'Patient.managingOrganization' AND r.target_resource_id IN ('" + ids.parentOrgPid + "') AND r.target_resource_type = 'Organization' UNION");
		}

		// Index 3-6 are just more includes loading
		assertThat(myCaptureQueriesListener.getSelectQueries().get(3).getSql(true, false)).contains(" FROM hfj_res_link r ");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(4).getSql(true, false)).contains(" FROM hfj_res_link r ");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(5).getSql(true, false)).contains(" FROM hfj_res_link r ");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(6).getSql(true, false)).contains(" FROM hfj_res_link r ");

		sql = myCaptureQueriesListener.getSelectQueries().get(7).getSql(true, false);
		assertThat(sql).contains("from HFJ_RES_VER rht1_0");
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("join HFJ_RESOURCE mrt1_0 on mrt1_0.RES_ID=rht1_0.RES_ID and mrt1_0.PARTITION_ID=rht1_0.PARTITION_ID where");
			assertThat(sql).contains("where (rht1_0.RES_ID,rht1_0.PARTITION_ID) in");
		} else {
			assertThat(sql).contains("join HFJ_RESOURCE mrt1_0 on mrt1_0.RES_ID=rht1_0.RES_ID where");
			assertThat(sql).contains("where (rht1_0.RES_ID) in");
		}

		assertEquals(8, myCaptureQueriesListener.countSelectQueries());
	}


	/**
	 * Searching for all partitions or multiple partitions
	 */
	@ParameterizedTest(name = "[{index}] -  {0}")
	@MethodSource("searchMultiPartitionTestCases")
	public void testSearch_MultiplePartitions(SearchMultiPartitionTestCase theTestCase) {
		myPartitionSelectorInterceptor.setNextPartition(theTestCase.requestPartitionId);
		String sql = getSqlForRestQuery(theTestCase.restQuery);

		if (myIncludePartitionIdsInPks) {
			assertEquals(theTestCase.expectedPartitionedPksSql, sql, theTestCase.comment);
		} else if (myIncludePartitionIdsInSql) {
			assertEquals(theTestCase.expectedPartitionedSql, sql, theTestCase.comment);
		} else {
			assertEquals(theTestCase.expectedSql, sql, theTestCase.comment);
		}
	}

	/**
	 * Make sure _sort incorporates the partition ID on joins
	 */
	@ParameterizedTest(name = "[{index}] -  {0}")
	@MethodSource("searchSortTestCases")
	public void testSearch_Sort(SqlGenerationTestCase theTestCase) {
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		String sql = getSqlForRestQuery(theTestCase.restQuery);

		if (myIncludePartitionIdsInPks) {
			assertEquals(theTestCase.expectedPartitionedPksSql, sql, theTestCase.comment);
		} else if (myIncludePartitionIdsInSql) {
			assertEquals(theTestCase.expectedPartitionedSql, sql, theTestCase.comment);
		} else {
			assertEquals(theTestCase.expectedSql, sql, theTestCase.comment);
		}
	}


	@Test
	public void testValuesetExpansion_IncludePreExpandedVsWithFilter() {
		// Setup
		myStorageSettings.setPreExpandValueSets(true);

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://cs");
		cs.setContent(Enumerations.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs, newRequest());

		CustomTerminologySet additions = new CustomTerminologySet();
		additions.addRootConcept("A", "HELLO");
		additions.addRootConcept("B", "HELLO");
		additions.addRootConcept("C", "GOODBYE");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://cs", additions);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl("http://vs");
		valueSet
			.getCompose()
				.addInclude().setSystem("http://cs");
		myValueSetDao.create(valueSet, newRequest());

		myCaptureQueriesListener.clear();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myParentTest.logAllCodeSystemsAndVersionsCodeSystemsAndVersions();
		myParentTest.logAllConcepts();
		myParentTest.logAllValueSetConcepts();

		// Test
		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.addValueSet("http://vs");

		ValueSetExpansionOptions expansionOptions = new ValueSetExpansionOptions();
		expansionOptions.setFilter("HELLO");
		myCaptureQueriesListener.clear();
		ValueSet outcome = (ValueSet) myTermSvc.expandValueSet(expansionOptions, valueSet);

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		assertThat(outcome.getExpansion().getContains().stream().map(ValueSet.ValueSetExpansionContainsComponent::getCode).toList()).asList().containsExactly("A", "B");
	}


	@Test
	public void testUpdateAsCreate() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		createPatient(withId("A"), withActiveTrue());

		// Test
		myCaptureQueriesListener.clear();

		Observation obs = new Observation();
		obs.setId("Observation/O");
		obs.setSubject(new Reference("Patient/A"));
		obs.setEffective(new DateTimeType("2022"));
		myObservationDao.update(obs, newRequest());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.PARTITION_ID='1' and (rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID='O')");
			assertThat(getSelectSql(1)).endsWith(" where rt1_0.PARTITION_ID='1' and (rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='A')");
		} else {
			assertThat(getSelectSql(0)).endsWith(" where (rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID='O')");
			assertThat(getSelectSql(1)).endsWith(" where (rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID='A')");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

	private SystemRequestDetails newRequest() {
		return new SystemRequestDetails();
	}

	private JpaPid findId(String theResourceType, String theIdPart) {
		return myParentTest.runInTransaction(() -> myResourceTableDao
			.findAll()
			.stream()
			.filter(t -> t.getResourceType().equals(theResourceType))
			.filter(t -> t.getFhirId().equals(theIdPart))
			.findFirst()
			.orElseThrow()
			.getPersistentId());
	}

	@Language("SQL")
	private String getSelectSql(int theIndex) {
		return myCaptureQueriesListener.getSelectQueries().get(theIndex).getSql(true, false);
	}

	@Language("SQL")
	private String getDeleteSql(int theIndex) {
		return myCaptureQueriesListener.getDeleteQueries().get(theIndex).getSql(true, false);
	}

	@Language("SQL")
	private String getUpdateSql(int theIndex) {
		return myCaptureQueriesListener.getUpdateQueries().get(theIndex).getSql(true, false);
	}

	@Language("SQL")
	private String getInsertSql(int theIndex) {
		return myCaptureQueriesListener.getInsertQueries().get(theIndex).getSql(true, false);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public IIdType doCreateResource(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.create(theResource, newRequest()).getId().toUnqualifiedVersionless();
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public IIdType doUpdateResource(IBaseResource theResource) {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		return dao.update(theResource, newRequest()).getId().toUnqualifiedVersionless();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirCtx;
	}

	public void runInTransaction(Runnable theRunnable) {
		myParentTest.runInTransaction(theRunnable);
	}
	public <T> T runInTransaction(Callable<T> theRunnable) {
		return myParentTest.runInTransaction(theRunnable);
	}

	@Nonnull
	private CreatedResourceIds createPatientWithOrganizationReferences() {
		IIdType parentOrgId = createOrganization(withName("PARENT")).toUnqualifiedVersionless();
		IIdType childOrgId = createOrganization(withName("CHILD"), withReference("partOf", parentOrgId)).toUnqualifiedVersionless();
		IIdType patientId = createPatient(withActiveTrue(), withOrganization(childOrgId)).toUnqualifiedVersionless();
		long patientPid = patientId.getIdPartAsLong();
		long childPid = childOrgId.getIdPartAsLong();
		long parentPid = parentOrgId.getIdPartAsLong();
		CreatedResourceIds result = new CreatedResourceIds(parentOrgId, childOrgId, patientId, null, patientPid, childPid, parentPid, null);
		return result;
	}

	@Nonnull
	private CreatedResourceIds createPatientWithOrganizationAndEncounterReferences() {
		CreatedResourceIds createdResourceIds = createPatientWithOrganizationReferences();

		Encounter encounter = new Encounter();
		encounter.setSubject(new Reference(createdResourceIds.patientId));
		IIdType encounterId = myEncounterDao.create(encounter).getId().toUnqualifiedVersionless();
		Long encounterPid = encounterId.getIdPartAsLong();

		return new CreatedResourceIds(
			createdResourceIds.parentOrgId,
			createdResourceIds.childOrgId,
			createdResourceIds.patientId,
			encounterId,
			createdResourceIds.patientPid,
			createdResourceIds.childOrgPid,
			createdResourceIds.parentOrgPid,
			encounterPid
		);
	}

	@Nonnull
	private List<SqlQuery> getSqlSelectQueriesWithString(String tableName) {
		List<SqlQuery> selectTokenQueries = myCaptureQueriesListener.getSelectQueries()
			.stream()
			.filter(t -> t.getSql(false, false).contains(tableName))
			.toList();
		return selectTokenQueries;
	}

	@Nonnull
	private List<SqlQuery> getSqlDeleteQueriesWithString(String tableName) {
		List<SqlQuery> selectTokenQueries = myCaptureQueriesListener.getDeleteQueries()
			.stream()
			.filter(t -> t.getSql(false, false).contains(tableName))
			.toList();
		return selectTokenQueries;
	}

	private static Map<String, String> parseInsertStatementParams(String theInsertSql) throws JSQLParserException {
		Insert parsedStatement = (Insert) CCJSqlParserUtil.parse(theInsertSql);

		Map<String, String> retVal = new HashMap<>();

		for (int i = 0; i < parsedStatement.getColumns().size(); i++) {
			String columnName = parsedStatement.getColumns().get(i).getColumnName();
			String columnValue = parsedStatement.getValues().getExpressions().get(i).toString();
			assertFalse(retVal.containsKey(columnName), ()->"Duplicate column in insert statement: " + columnName);
			retVal.put(columnName, columnValue);
		}

		return retVal;
	}

	private static Map<String, String> parseUpdateStatementParams(String theUpdateSql) throws JSQLParserException {
		Update parsedStatement = (Update) CCJSqlParserUtil.parse(theUpdateSql);

		Map<String, String> retVal = new HashMap<>();

		for (UpdateSet updateSet : parsedStatement.getUpdateSets()) {
			for (int i = 0; i < updateSet.getColumns().size(); i++) {
				String columnName = updateSet.getColumns().get(i).getColumnName();
				String columnValue = updateSet.getValues().getExpressions().get(i).toString();
				assertFalse(retVal.containsKey(columnName), ()->"Duplicate column in insert statement: " + columnName);
				retVal.put(columnName, columnValue);
			}
		}

		return retVal;
	}

	private static String parseInsertStatementTableName(String theInsertSql) throws JSQLParserException {
		Insert parsedStatement = (Insert) CCJSqlParserUtil.parse(theInsertSql);
		return parsedStatement.getTable().getName();
	}

	private static List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound) {
		int fromIndex = 0;
		Integer toIndex = theFound.size();
		return toUnqualifiedVersionlessIdValues(theFound, fromIndex, toIndex, true);
	}

	private static List<String> toUnqualifiedIdValues(IBundleProvider theFound) {
		return toIdValues(theFound, false);
	}

	private static List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound, int theFromIndex, Integer theToIndex, boolean theFirstCall) {
		return toIdValues(theFound, true);
	}

	@Nonnull
	private static List<String> toIdValues(IBundleProvider theFound, boolean theVersionless) {
		List<String> retVal = new ArrayList<>();

		IBundleProvider bundleProvider;
		bundleProvider = theFound;

		List<IBaseResource> resources = bundleProvider.getResources(0, 99999);
		for (IBaseResource next : resources) {
			IIdType id = next.getIdElement();
			if (theVersionless) {
				id = id.toUnqualifiedVersionless();
			} else {
				id = id.toUnqualified();
			}
			retVal.add(id.getValue());
		}
		return retVal;
	}

	private record CreatedResourceIds(IIdType parentOrgId, IIdType childOrgId, IIdType patientId, IIdType encounterId,
									  Long patientPid, Long childOrgPid, Long parentOrgPid, Long encounterPid) {

		public Set<String> allIdValues() {
			Set<String> retVal = new HashSet<>();
			addIfNotNull(retVal, parentOrgId);
			addIfNotNull(retVal, childOrgId);
			addIfNotNull(retVal, patientId);
			addIfNotNull(retVal, encounterId);
			return retVal;
		}

		private static void addIfNotNull(Set<String> theList, IIdType theObject) {
			if (theObject != null) {
				theList.add(theObject.getValue());
			}
		}
	}

	public record SearchMultiPartitionTestCase(String comment, RequestPartitionId requestPartitionId, String restQuery, String expectedSql, String expectedPartitionedSql, String expectedPartitionedPksSql) {
		@Override
		public String toString() {
			return comment;
		}

		public static void add(List<SearchMultiPartitionTestCase> theTarget, RequestPartitionId theRequestPartitionId, String theName, String theRestQuery, String theExpectedSql, String theExpectedPartitionedSql, String theExpectedPartitionedPksSql) {
			theTarget.add(new SearchMultiPartitionTestCase(
				theName,
				theRequestPartitionId,
				theRestQuery,
				theExpectedSql,
				theExpectedPartitionedSql,
				theExpectedPartitionedPksSql));
		}
	}

	static List<SearchMultiPartitionTestCase> searchMultiPartitionTestCases() {
		List<SearchMultiPartitionTestCase> retVal = new ArrayList<>();

		SearchMultiPartitionTestCase.add(
			retVal,
			RequestPartitionId.allPartitions(),
			"single string - no hfj_resource root - all partitions",
				"Patient?name=FOO",
				"SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) fetch first ? rows only"
		);
		SearchMultiPartitionTestCase.add(
			retVal,
			RequestPartitionId.fromPartitionIds(PARTITION_1, PARTITION_2),
			"single string - no hfj_resource root - multiple partitions",
				"Patient?name=FOO",
				"SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.PARTITION_ID IN (?,?) ) AND ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.PARTITION_ID IN (?,?) ) AND ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))) fetch first ? rows only"
		);

		SearchMultiPartitionTestCase.add(
			retVal,
			RequestPartitionId.allPartitions(),
			"two regular params - should use hfj_resource as root - all partitions",
			"Patient?name=smith&active=true",
			"SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_TOKEN t2 ON (t1.RES_ID = t2.RES_ID) WHERE (((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) AND (t2.HASH_VALUE = ?)) fetch first ? rows only",
			"SELECT t1.PARTITION_ID,t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_TOKEN t2 ON (t1.RES_ID = t2.RES_ID) WHERE (((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) AND (t2.HASH_VALUE = ?)) fetch first ? rows only",
			"SELECT t1.PARTITION_ID,t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON ((t1.PARTITION_ID = t0.PARTITION_ID) AND (t1.RES_ID = t0.RES_ID)) INNER JOIN HFJ_SPIDX_TOKEN t2 ON ((t1.PARTITION_ID = t2.PARTITION_ID) AND (t1.RES_ID = t2.RES_ID)) WHERE (((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) AND (t2.HASH_VALUE = ?)) fetch first ? rows only"
		);
		SearchMultiPartitionTestCase.add(
			retVal,
			RequestPartitionId.fromPartitionIds(PARTITION_1, PARTITION_2),
			"two regular params - should use hfj_resource as root - multiple partitions",
			"Patient?name=smith&active=true",
			"SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_TOKEN t2 ON (t1.RES_ID = t2.RES_ID) WHERE (((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) AND (t2.HASH_VALUE = ?)) fetch first ? rows only",
			"SELECT t1.PARTITION_ID,t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_TOKEN t2 ON (t1.RES_ID = t2.RES_ID) WHERE (((t0.PARTITION_ID IN (?,?) ) AND ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))) AND ((t2.PARTITION_ID IN (?,?) ) AND (t2.HASH_VALUE = ?))) fetch first ? rows only",
			"SELECT t1.PARTITION_ID,t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON ((t1.PARTITION_ID = t0.PARTITION_ID) AND (t1.RES_ID = t0.RES_ID)) INNER JOIN HFJ_SPIDX_TOKEN t2 ON ((t1.PARTITION_ID = t2.PARTITION_ID) AND (t1.RES_ID = t2.RES_ID)) WHERE (((t0.PARTITION_ID IN (?,?) ) AND ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))) AND ((t2.PARTITION_ID IN (?,?) ) AND (t2.HASH_VALUE = ?))) fetch first ? rows only"
		);

		SearchMultiPartitionTestCase.add(
			retVal,
			RequestPartitionId.allPartitions(),
			"token not as a NOT IN subselect - all partitions",
			"Encounter?class:not=not-there",
			"SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.RES_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
			"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.RES_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
			"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID,t0.RES_ID) NOT IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only"
		);
		SearchMultiPartitionTestCase.add(
			retVal,
			RequestPartitionId.fromPartitionIds(PARTITION_1, PARTITION_2),
			"token not as a NOT IN subselect - multiple partitions",
			"Encounter?class:not=not-there",
			"SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.RES_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
			"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID IN (?,?) ) AND ((t0.RES_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) ))) fetch first ? rows only",
			"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID IN (?,?) ) AND ((t0.PARTITION_ID,t0.RES_ID) NOT IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) ))) fetch first ? rows only"
		);

		SearchMultiPartitionTestCase.add(
			retVal,
			RequestPartitionId.allPartitions(),
			"token not on chain join - NOT IN from hfj_res_link target columns - all partitions",
			"Observation?encounter.class:not=not-there",
			"SELECT t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
			"SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
			"SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.TARGET_RES_PARTITION_ID,t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only"
		);
		SearchMultiPartitionTestCase.add(
			retVal,
			RequestPartitionId.fromPartitionIds(PARTITION_1, PARTITION_2),
			"token not on chain join - NOT IN from hfj_res_link target columns - multiple partitions",
			"Observation?encounter.class:not=not-there",
			"SELECT t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
			"SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.PARTITION_ID IN (?,?) ) AND ((t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) ))) fetch first ? rows only",
			"SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.PARTITION_ID IN (?,?) ) AND ((t0.TARGET_RES_PARTITION_ID,t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) ))) fetch first ? rows only"
		);

		return retVal;
	}

	public record SqlGenerationTestCase(String comment, String restQuery, String expectedSql, String expectedPartitionedSql, String expectedPartitionedPksSql) {
		@Override
		public String toString() {
			return comment;
		}
	}

	static List<SqlGenerationTestCase> searchSortTestCases() {
		return List.of(
			new SqlGenerationTestCase(
				"bare sort",
				"Patient?_sort=name",
				"SELECT t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_SPIDX_STRING t1 ON ((t0.RES_ID = t1.RES_ID) AND (t1.HASH_IDENTITY = ?)) WHERE ((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) ORDER BY t1.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_SPIDX_STRING t1 ON ((t0.RES_ID = t1.RES_ID) AND (t1.HASH_IDENTITY = ?)) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = ?)) ORDER BY t1.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_SPIDX_STRING t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.RES_ID) AND (t1.HASH_IDENTITY = ?)) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = ?)) ORDER BY t1.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"sort with predicate",
				"Patient?active=true&_sort=name",
				"SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_TOKEN t0 ON (t1.RES_ID = t0.RES_ID) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.RES_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE (t0.HASH_VALUE = ?) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t1.PARTITION_ID,t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_TOKEN t0 ON (t1.RES_ID = t0.RES_ID) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.RES_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE ((t0.PARTITION_ID = ?) AND (t0.HASH_VALUE = ?)) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t1.PARTITION_ID,t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_TOKEN t0 ON ((t1.PARTITION_ID = t0.PARTITION_ID) AND (t1.RES_ID = t0.RES_ID)) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.PARTITION_ID = t2.PARTITION_ID) AND (t1.RES_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE ((t0.PARTITION_ID = ?) AND (t0.HASH_VALUE = ?)) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"chained sort",
				"Patient?_sort=Practitioner:general-practitioner.name",
				"SELECT t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_RES_LINK t1 ON ((t0.RES_ID = t1.SRC_RESOURCE_ID) AND (t1.SRC_PATH = ?)) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.TARGET_RESOURCE_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE ((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_RES_LINK t1 ON ((t0.RES_ID = t1.SRC_RESOURCE_ID) AND (t1.SRC_PATH = ?)) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.TARGET_RESOURCE_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = ?)) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_RES_LINK t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.SRC_RESOURCE_ID) AND (t1.SRC_PATH = ?)) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.TARGET_RES_PARTITION_ID = t2.PARTITION_ID) AND (t1.TARGET_RESOURCE_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = ?)) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only"
			)
		);
	}

	private String getSqlForRestQuery(String theFhirRestQuery) {
		myCaptureQueriesListener.clear();
		myTestDaoSearch.searchForIds(theFhirRestQuery);
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		return myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
	}

}


