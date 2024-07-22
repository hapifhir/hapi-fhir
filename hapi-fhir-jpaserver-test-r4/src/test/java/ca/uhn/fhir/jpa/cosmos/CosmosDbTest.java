package ca.uhn.fhir.jpa.cosmos;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Store connection properties for the Cosmos db in a file in /tmp/cosmos.properties
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	TestCosmosR4Config.class
})
public class CosmosDbTest extends BaseJpaTest {
	public static final String PATIENT_P0 = "Patient/p0";
	public static final String OBSERVATION_O0 = "Observation/O0";
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	private IFhirResourceDaoObservation<Observation> myObservationDao;
	private CosmosPartitionInterceptor myPartitionInterceptor = new CosmosPartitionInterceptor();
	@Autowired
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;
	@Autowired
	private TestDaoSearch myDaoSearch;

	@Override
	protected FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@BeforeEach
	public void beforeEach() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(-1);

		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);

		createPartitionIfItDoesntExist("P1", 1);
        createPartitionIfItDoesntExist("P2", 2);
	}

	@AfterEach
	public void afterEach() {
		purgeDatabase();

		myPartitionInterceptor.assertNoRemainingIds();
		myInterceptorRegistry.unregisterInterceptor(myPartitionInterceptor);
	}


	@Test
	public void testCreateWithClientAssignedIdAndRead() {
		// Create resource
		addCreatePartition1();
		Patient patient = new Patient();
		patient.setId(PATIENT_P0);
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.update(patient, mySrd);

		// Read back
		addReadPartition(1);
		patient = myPatientDao.read(new IdType(PATIENT_P0), mySrd);
		assertEquals("123", patient.getIdentifierFirstRep().getValue());

		// Try to read from wrong partition
		addReadPartition(2);
		try {
			myPatientDao.read(new IdType(PATIENT_P0), mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

	}

	@Test
	public void testChainedSearch() {
		// Create resource
		createPatientAndObservation();

		// Test
		addReadPartition(1);
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam("identifier", "http://foo|123"));
		IBundleProvider outcome = myObservationDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(outcome), contains(OBSERVATION_O0));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			string, Patient?_sort=name
			date, Observation?_sort=date
			_id, Patient?_sort=_id
			_lastUpdated, Patient?_sort=_lastUpdated
			_pid, Patient?_sort=_pid
			""")
	void testSort(String theComment, String theQuery) {
		// Create resource
		createPatientAndObservation();

		// when
		addReadPartition(1);
		List<String> ids = myDaoSearch.searchForIds(theQuery);

		assertEquals(1, ids.size(), theComment);
	}


	@Test
	public void testIncludes() {
		// Setup
		createPatientAndObservation();

		// Test
		addReadPartition(1);
		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.add(IAnyResource.SP_RES_ID, new TokenParam(OBSERVATION_O0))
			.addInclude(Observation.INCLUDE_PATIENT);
		IBundleProvider results = myObservationDao.search(map, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(results), contains(OBSERVATION_O0, PATIENT_P0));
	}

	@Test
	public void testRevIncludes() {
		// Setup
		createPatientAndObservation();

		// Test
		addReadPartition(1);
		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.add(IAnyResource.SP_RES_ID, new TokenParam(PATIENT_P0))
			.addRevInclude(Observation.INCLUDE_PATIENT);
		IBundleProvider results = myPatientDao.search(map, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(results), contains(PATIENT_P0, OBSERVATION_O0));
	}

	@Test
	public void testFhirTransaction() {

		addCreatePartition1();
		addCreatePartition1();
		addCreatePartition1();
		addCreatePartition1();
		Bundle requestBundle = createTransactionWithConditionalCreatePatientAndObservation();
		Bundle responseBundle = mySystemDao.transaction(mySrd, requestBundle);
		List<String> ids = toUnqualifiedVersionlessIdValues(responseBundle);

		addCreatePartition1();
		addCreatePartition1();
		addCreatePartition1();
		addCreatePartition1();
		requestBundle = createTransactionWithConditionalCreatePatientAndObservation();
		responseBundle = mySystemDao.transaction(mySrd, requestBundle);
		List<String> ids2 = toUnqualifiedVersionlessIdValues(responseBundle);

		assertEquals(ids, ids2);
	}

	private Bundle createTransactionWithConditionalCreatePatientAndObservation() {
		BundleBuilder bb = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId(IdType.newRandomUuid());
		patient.setActive(true);
		bb.addTransactionCreateEntry(patient, "?identifier=http://foo|111");

		Observation obs = new Observation();
		obs.setId(IdType.newRandomUuid());
		obs.setSubject(new Reference(patient.getIdElement()));
		obs.addIdentifier().setSystem("http://foo").setValue("222");
		bb.addTransactionCreateEntry(obs, "?identifier=http://foo|222");

		Bundle requestBundle = bb.getBundleTyped();
		return requestBundle;
	}

	private void createPatientAndObservation() {
		addCreatePartition1();
		Patient patient = new Patient();
		patient.setId(PATIENT_P0);
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		patient.setGender(Enumerations.AdministrativeGender.MALE);
		patient.addName().setFamily("Smith");
		myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();

		addCreatePartition1();
		Observation obs = new Observation();
		obs.setId(OBSERVATION_O0);
		obs.setSubject(new Reference(PATIENT_P0));
		myObservationDao.update(obs, mySrd).getId().toUnqualifiedVersionless();
	}

	private void addReadPartition(int partitionId) {
		myPartitionInterceptor.addReadPartition(RequestPartitionId.fromPartitionId(partitionId));
	}

	private void addCreatePartition1() {
		myPartitionInterceptor.addCreatePartition(RequestPartitionId.fromPartitionId(1));
	}

	private void createPartitionIfItDoesntExist(String partitionName, int partitionId) {
		PartitionEntity partition = new PartitionEntity();
		partition.setName(partitionName);
		partition.setId(partitionId);
		try {
			PartitionEntity found = myPartitionConfigSvc.getPartitionById(partition.getId());
			assertEquals(partition.getName(), found.getName());
		} catch (ResourceNotFoundException e) {
			myPartitionConfigSvc.createPartition(partition, mySrd);
		}
	}

	private void purgeDatabase() {
		myPartitionInterceptor.assertNoRemainingIds();
		myPartitionInterceptor.addReadPartition(RequestPartitionId.allPartitions());
		purgeDatabase(myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
		myPartitionInterceptor.assertNoRemainingIds();
	}


}
