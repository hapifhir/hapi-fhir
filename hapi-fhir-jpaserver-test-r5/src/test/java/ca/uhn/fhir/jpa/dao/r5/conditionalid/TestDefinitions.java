package ca.uhn.fhir.jpa.dao.r5.conditionalid;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static ca.uhn.fhir.jpa.dao.r5.conditionalid.ConditionalIdFilteredPartitioningEnabledTest.PARTITION_1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	private DaoRegistry myDaoRegistry;

	public TestDefinitions(@Nonnull BaseJpaR5Test theParentTest, @Nonnull PartitionSelectorInterceptor thePartitionSelectorInterceptor, boolean theIncludePartitionIdsInSql, boolean theIncludePartitionIdsInPks) {
		myParentTest = theParentTest;
		myPartitionSelectorInterceptor = thePartitionSelectorInterceptor;
		myIncludePartitionIdsInSql = theIncludePartitionIdsInSql;
		myIncludePartitionIdsInPks = theIncludePartitionIdsInPks;
		assert myIncludePartitionIdsInSql && myIncludePartitionIdsInPks || myIncludePartitionIdsInSql || !myIncludePartitionIdsInPks;
	}

	@Test
	public void testReadServerAssignedId() {
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
	public void testReadClientAssignedId() {
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


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearch_TokenParam(boolean theSynchronous) {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		long id = createPatient(withActiveTrue()).getIdPartAsLong();

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(theSynchronous);
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
			assertThat(getSelectSql(1)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		} else {
			assertThat(getSelectSql(1)).endsWith(" where rsv1_0.RES_ID in ('" + id + "')");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
	}


	// FIXME: add test with specific includes and add both for reverse
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearch_IncludesStar(boolean theSynchronous) {
		// Setup
		myPartitionSelectorInterceptor.setNextPartitionId(PARTITION_1);
		IIdType parentOrgId = createOrganization(withName("PARENT"));
		IIdType childOrgId = createOrganization(withName("CHILD"), withReference("partOf", parentOrgId));
		long id = createPatient(withActiveTrue(), withOrganization(childOrgId)).getIdPartAsLong();

		// Test
		myParentTest.logAllResources();
		myParentTest.logAllResourceLinks();
		myCaptureQueriesListener.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(theSynchronous);
		params.addInclude(new Include("*", true));
		IBundleProvider outcome = myPatientDao.search(params, newRequest());
		List<String> values = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(values).asList().containsExactlyInAnyOrder("Patient/" + id, "Organization/" + parentOrgId.getIdPart(), "Organization/" + childOrgId.getIdPart());

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		if (myIncludePartitionIdsInSql) {
			assertThat(getSelectSql(0)).endsWith(" WHERE ((t0.PARTITION_ID = '1') AND (t0.HASH_VALUE = '7943378963388545453'))");
		} else {
			assertThat(getSelectSql(0)).endsWith(" WHERE (t0.HASH_VALUE = '7943378963388545453')");
		}
		if (myIncludePartitionIdsInPks) {
			assertThat(getSelectSql(1)).endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in (('" + id + "','1'))");
		} else {
			assertThat(getSelectSql(1)).endsWith(" where rsv1_0.RES_ID in ('" + id + "')");
		}
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
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


