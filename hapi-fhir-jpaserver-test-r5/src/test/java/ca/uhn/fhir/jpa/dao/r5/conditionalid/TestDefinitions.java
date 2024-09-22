package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import jakarta.annotation.Nonnull;
import org.assertj.core.api.Assertions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.r5.conditionalid.ConditionalIdFilteredPartitioningEnabledTest.PARTITION_1;
import static ca.uhn.fhir.jpa.dao.r5.conditionalid.ConditionalIdKeptPartitioningEnabledTest.PARTITION_2;
import static ca.uhn.fhir.rest.api.Constants.PARAM_TAG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public abstract class TestDefinitions implements ITestDataBuilder {

	private final PartitionSelectorInterceptor myPartitionSelectorInterceptor;
	private final boolean myIncludePartitionIdsInSql;
	private final BaseJpaR5Test myParentTest;
	private final boolean myIncludePartitionIdsInPks;
	@Autowired
	protected CircularQueueCaptureQueriesListener myCaptureQueriesListener;
	@Autowired
	private IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	private IFhirResourceDaoObservation<Observation> myObservationDao;
	@Autowired
	private IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	private IResourceTableDao myResourceTableDao;
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

	public TestDefinitions(@Nonnull BaseJpaR5Test theParentTest, @Nonnull PartitionSelectorInterceptor thePartitionSelectorInterceptor, boolean theIncludePartitionIdsInSql, boolean theIncludePartitionIdsInPks) {
		myParentTest = theParentTest;
		myPartitionSelectorInterceptor = thePartitionSelectorInterceptor;
		myIncludePartitionIdsInSql = theIncludePartitionIdsInSql;
		myIncludePartitionIdsInPks = theIncludePartitionIdsInPks;
		assert myIncludePartitionIdsInSql && myIncludePartitionIdsInPks || myIncludePartitionIdsInSql || !myIncludePartitionIdsInPks;
	}

	@AfterEach
	public void after() {
		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setTagStorageMode(defaults.getTagStorageMode());
		myStorageSettings.setIndexOnContainedResources(defaults.isIndexOnContainedResources());
	}

	@Test
	public void testCreate_ReferenceToResourceInWrongPartition() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_2);
		IIdType patientId = createPatient(withActiveTrue());

		// Test
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		try {
			IIdType obsId = createObservation(withSubject(patientId));
			if (myIncludePartitionIdsInSql) {
				fail();
			} else {
				assertNotNull(obsId);
			}
		} catch (InvalidRequestException e) {
			if (myIncludePartitionIdsInSql) {
				assertThat(e.getMessage()).contains("not found, specified in path: Observation.subject");
			} else {
				fail();
			}
		}
	}

	@Test
	public void testRead_DefaultPartition() {
		// Setup
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
				assertThat(getSelectSql(0)).endsWith(" where rt1_0.RES_TYPE='Organization' and rt1_0.FHIR_ID in ('O') and rt1_0.PARTITION_ID is null");
			} else {
				assertThat(getSelectSql(0)).endsWith(" where rt1_0.RES_TYPE='Organization' and rt1_0.FHIR_ID in ('O') and rt1_0.PARTITION_ID in ('0')");
			}
		} else {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.RES_TYPE='Organization' and rt1_0.FHIR_ID in ('O')");
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

	private JpaPid findId(String theResourceType, String theIdPart) {
		return myParentTest.runInTransaction(()-> myResourceTableDao
			.findAll()
			.stream()
			.filter(t->t.getResourceType().equals(theResourceType))
			.filter(t->t.getFhirId().equals(theIdPart))
			.findFirst()
			.orElseThrow()
			.getId());
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

		long id = runInTransaction(() -> myResourceTableDao.findByTypeAndFhirId("Patient", "A").orElseThrow().getId().getId());

		// Test
		myCaptureQueriesListener.clear();
		myPatientDao.read(new IdType("Patient/A"), newRequest());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID in ('A') and rt1_0.PARTITION_ID in ('1')");
			assertThat(getSelectSql(1)).endsWith(" from HFJ_RESOURCE rt1_0 where rt1_0.PARTITION_ID='1' and rt1_0.RES_ID='" + id + "'");
		} else {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID in ('A')");
			assertThat(getSelectSql(1)).endsWith(" from HFJ_RESOURCE rt1_0 where rt1_0.RES_ID='" + id + "'");
		}
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(2)).endsWith(" where (rht1_0.RES_ID,rht1_0.PARTITION_ID)=('" + id + "','1') and rht1_0.RES_VER='1'");
		} else {
			assertThat(getSelectSql(2)).endsWith(" where rht1_0.RES_ID='" + id + "' and rht1_0.RES_VER='1'");
		}
		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
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
		} else {
			assertThat(getSelectSql(0)).startsWith("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (t0.RES_ID IN (SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 ");
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
		assertThat(getSelectSql(0)).contains("SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 ");
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
			assertThat(getSelectSql(2)).contains(" where (rht1_0.PARTITION_ID,rht1_0.RES_VER_PID) in (('1','" + id + "'))");
		} else {
			assertThat(getSelectSql(2)).contains(" where (rht1_0.RES_VER_PID) in ('" + id + "')");
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
	public void testSearch_TokenParam() {
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
			assertThat(getSelectSql(0)).endsWith(" WHERE ((t0.PARTITION_ID = '1') AND (t0.HASH_VALUE = '7943378963388545453'))");
		} else {
			assertThat(getSelectSql(0)).endsWith(" WHERE (t0.HASH_VALUE = '7943378963388545453')");
		}
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(1)).endsWith(" where (rht1_0.RES_ID,rht1_0.PARTITION_ID) in (('" + id + "','1')) and mrt1_0.RES_VER=rht1_0.RES_VER");
		} else {
			assertThat(getSelectSql(1)).endsWith(" where (rht1_0.RES_ID) in ('" + id + "') and mrt1_0.RES_VER=rht1_0.RES_VER");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}


	// FIXME: add test with specific includes and add both for reverse
	// FIXME: also add version that uses client assigned IDs
	// FIXME: create another test container that uses null as the default partition ID
	@Test
	public void testSearch_IncludesStar() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		PatientWithOrganizationReferences ids = createPatientWithOrganizationReferences();

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
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = '1'))");
		} else {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL))");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.PARTITION_ID='1' and rl1_0.SRC_RESOURCE_ID in ('" + ids.patientPid() + "') fetch");
		} else {
			assertThat(sql).contains("where rl1_0.SRC_RESOURCE_ID in ('" + ids.patientPid() + "') fetch ");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(2).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.PARTITION_ID='0' and rl1_0.SRC_RESOURCE_ID in ('" + ids.childPid() + "') ");
		} else {
			assertThat(sql).contains("where rl1_0.SRC_RESOURCE_ID in ('" + ids.childPid() + "') fetch ");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(3).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("where rl1_0.PARTITION_ID='0' and rl1_0.SRC_RESOURCE_ID in ('" + ids.parentPid() + "') ");
		} else {
			assertThat(sql).contains("where rl1_0.SRC_RESOURCE_ID in ('" + ids.parentPid() + "') fetch ");
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

	@Nonnull
	private PatientWithOrganizationReferences createPatientWithOrganizationReferences() {
		IIdType parentOrgId = createOrganization(withName("PARENT"));
		IIdType childOrgId = createOrganization(withName("CHILD"), withReference("partOf", parentOrgId));
		long patientPid = createPatient(withActiveTrue(), withOrganization(childOrgId)).getIdPartAsLong();
		long childPid = childOrgId.getIdPartAsLong();
		long parentPid = parentOrgId.getIdPartAsLong();
		PatientWithOrganizationReferences result = new PatientWithOrganizationReferences(parentOrgId, childOrgId, patientPid, childPid, parentPid);
		return result;
	}

	private record PatientWithOrganizationReferences(IIdType parentOrgId, IIdType childOrgId, long patientPid, long childPid, long parentPid) {
	}

	@Test
	public void testSearch_IncludesSpecific() {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		PatientWithOrganizationReferences ids = createPatientWithOrganizationReferences();

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
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = '1'))");
		} else {
			assertThat(sql).isEqualTo("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL))");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(1).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("WHERE r.src_path = 'Organization.partOf' AND r.target_resource_id IS NOT NULL AND r.src_resource_id IN ('" + ids.patientPid + "') AND partition_id = '1' AND r.target_resource_type = 'Organization' UNION");
		} else {
			assertThat(sql).contains("WHERE r.src_path = 'Organization.partOf' AND r.target_resource_id IS NOT NULL AND r.src_resource_id IN ('" + ids.patientPid + "') AND r.target_resource_type = 'Organization' UNION");
		}

		sql = myCaptureQueriesListener.getSelectQueries().get(2).getSql(true, false);
		if (myIncludePartitionIdsInPks) {
			assertThat(sql).contains("WHERE r.src_path = 'Patient.managingOrganization' AND r.target_resource_id IS NOT NULL AND r.src_resource_id IN ('" + ids.patientPid + "') AND partition_id = '1' AND r.target_resource_type = 'Organization' UNION");
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
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID in ('O') and rt1_0.PARTITION_ID in ('1')");
			assertThat(getSelectSql(1)).endsWith(" where rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID in ('A') and rt1_0.PARTITION_ID in ('1')");
		} else {
			assertThat(getSelectSql(0)).endsWith(" where rt1_0.RES_TYPE='Observation' and rt1_0.FHIR_ID in ('O')");
			assertThat(getSelectSql(1)).endsWith(" where rt1_0.RES_TYPE='Patient' and rt1_0.FHIR_ID in ('A')");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}

	// FIXME: add a history test

	private SystemRequestDetails newRequest() {
		return new SystemRequestDetails();
	}


	@Language("SQL")
	private String getSelectSql(int theIndex) {
		return myCaptureQueriesListener.getSelectQueries().get(theIndex).getSql(true, false);
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

	public <T> T runInTransaction(Callable<T> theRunnable) {
		return myParentTest.runInTransaction(theRunnable);
	}

	private static List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound) {
		int fromIndex = 0;
		Integer toIndex = theFound.size();
		return toUnqualifiedVersionlessIdValues(theFound, fromIndex, toIndex, true);
	}

	private static List<String> toUnqualifiedVersionlessIdValues(IBundleProvider theFound, int theFromIndex, Integer theToIndex, boolean theFirstCall) {
		theToIndex = 99999;

		List<String> retVal = new ArrayList<>();

		IBundleProvider bundleProvider;
		bundleProvider = theFound;

		List<IBaseResource> resources = bundleProvider.getResources(theFromIndex, theToIndex);
		for (IBaseResource next : resources) {
			retVal.add(next.getIdElement().toUnqualifiedVersionless().getValue());
		}
		return retVal;
	}

}


