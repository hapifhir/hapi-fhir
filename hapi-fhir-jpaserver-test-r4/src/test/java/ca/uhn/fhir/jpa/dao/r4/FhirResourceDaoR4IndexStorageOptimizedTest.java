package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.config.SearchConfig;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.util.SearchParamHash;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.reindex.ReindexStepTest;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.param.BaseParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.Substance;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * This test was added to check if changing {@link StorageSettings#isIndexStorageOptimized()} setting and performing
 * $reindex operation will correctly null/recover sp_name, res_type, sp_updated parameters
 * of ResourceIndexedSearchParam entities.
 */
public class FhirResourceDaoR4IndexStorageOptimizedTest extends BaseJpaR4Test {

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Autowired
	private SearchConfig mySearchConfig;

	@AfterEach
	void cleanUp() {
		myPartitionSettings.setIncludePartitionInSearchHashes(false);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testCoordinatesIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);
		Location loc = new Location();
		loc.getPosition().setLatitude(43.7);
		loc.getPosition().setLongitude(79.4);
		IIdType id = myLocationDao.create(loc, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myLocationDao, myResourceIndexedSearchParamCoordsDao, id,
			Location.SP_NEAR, "Location", new SpecialParam().setValue("43.7|79.4"), ResourceIndexedSearchParamCoords.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testDateIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);
		Patient p = new Patient();
		p.setBirthDateElement(new DateType("2021-02-22"));
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myPatientDao, myResourceIndexedSearchParamDateDao, id,
			Patient.SP_BIRTHDATE, "Patient", new DateParam("2021-02-22"), ResourceIndexedSearchParamDate.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testNumberIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);
		RiskAssessment riskAssessment = new RiskAssessment();
		DecimalType doseNumber = new DecimalType(15);
		riskAssessment.addPrediction(new RiskAssessment.RiskAssessmentPredictionComponent().setProbability(doseNumber));
		IIdType id = myRiskAssessmentDao.create(riskAssessment, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myRiskAssessmentDao, myResourceIndexedSearchParamNumberDao, id,
			RiskAssessment.SP_PROBABILITY, "RiskAssessment", new NumberParam(15), ResourceIndexedSearchParamNumber.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testQuantityIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);
		Observation observation = new Observation();
		observation.setValue(new Quantity(123));
		IIdType id = myObservationDao.create(observation, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myObservationDao, myResourceIndexedSearchParamQuantityDao, id,
			Observation.SP_VALUE_QUANTITY, "Observation", new QuantityParam(123), ResourceIndexedSearchParamQuantity.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testQuantityNormalizedIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
		Substance res = new Substance();
		res.addInstance().getQuantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("m").setValue(123);
		IIdType id = mySubstanceDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		QuantityParam quantityParam = new QuantityParam(null, 123, UcumServiceUtil.UCUM_CODESYSTEM_URL, "m");
		validateAndReindex(theIsIndexStorageOptimized, mySubstanceDao, myResourceIndexedSearchParamQuantityNormalizedDao,
			id, Substance.SP_QUANTITY, "Substance", quantityParam, ResourceIndexedSearchParamQuantityNormalized.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testStringIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);
		Patient p = new Patient();
		p.addAddress().addLine("123 Main Street");
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myPatientDao, myResourceIndexedSearchParamStringDao, id,
			Patient.SP_ADDRESS, "Patient", new StringParam("123 Main Street"), ResourceIndexedSearchParamString.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testTokenIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(observation, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myObservationDao, myResourceIndexedSearchParamTokenDao, id,
			Observation.SP_STATUS, "Observation", new TokenParam("final"), ResourceIndexedSearchParamToken.class);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testUriIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl("http://vs");
		IIdType id = myValueSetDao.create(valueSet, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myValueSetDao, myResourceIndexedSearchParamUriDao, id,
			ValueSet.SP_URL, "ValueSet", new UriParam("http://vs"), ResourceIndexedSearchParamUri.class);
	}

	@ParameterizedTest
	@CsvSource({
		"false, false, false",
		"false, false, true",
		"false, true, false",
		"true, false, false",
		"true, false, true",
		"true, true, false"})
	public void testValidateConfiguration_withCorrectConfiguration_doesNotThrowException(boolean thePartitioningEnabled,
																						 boolean theIsIncludePartitionInSearchHashes,
																						 boolean theIsIndexStorageOptimized) {
		myPartitionSettings.setPartitioningEnabled(thePartitioningEnabled);
		myPartitionSettings.setIncludePartitionInSearchHashes(theIsIncludePartitionInSearchHashes);
		myStorageSettings.setIndexStorageOptimized(theIsIndexStorageOptimized);

		assertDoesNotThrow(() -> mySearchConfig.validateConfiguration());
	}

	@Test
	public void testValidateConfiguration_withInCorrectConfiguration_throwsException() {
		myPartitionSettings.setIncludePartitionInSearchHashes(true);
		myPartitionSettings.setPartitioningEnabled(true);
		myStorageSettings.setIndexStorageOptimized(true);

		try {
			mySearchConfig.validateConfiguration();
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(2525) + "Incorrect configuration. "
				+ "StorageSettings#isIndexStorageOptimized and PartitionSettings.isIncludePartitionInSearchHashes "
				+ "cannot be enabled at the same time.", e.getMessage());
		}
	}

	private void validateAndReindex(boolean theIsIndexStorageOptimized, IFhirResourceDao<? extends IBaseResource> theResourceDao,
									JpaRepository<? extends BaseResourceIndexedSearchParam, Long> theIndexedSpRepository, IIdType theId,
									String theSearchParam, String theResourceType, BaseParam theParamValue,
									Class<? extends BaseResourceIndexedSearchParam> theIndexedSearchParamClass) {
		// validate
		validateSearchContainsResource(theResourceDao, theId, theSearchParam, theParamValue);
		validateSearchParams(theIndexedSpRepository, theId, theSearchParam, theResourceType, theIndexedSearchParamClass);

		// switch on/off storage optimization and run $reindex
		myStorageSettings.setIndexStorageOptimized(!theIsIndexStorageOptimized);
		executeReindex(theResourceType + "?");

		// validate again
		validateSearchContainsResource(theResourceDao, theId, theSearchParam, theParamValue);
		validateSearchParams(theIndexedSpRepository, theId, theSearchParam, theResourceType, theIndexedSearchParamClass);
	}

	private void validateSearchParams(JpaRepository<? extends BaseResourceIndexedSearchParam, Long> theIndexedSpRepository,
									  IIdType theId, String theSearchParam, String theResourceType,
									  Class<? extends BaseResourceIndexedSearchParam> theIndexedSearchParamClass) {
		List<? extends BaseResourceIndexedSearchParam> repositorySearchParams =
			getAndValidateIndexedSearchParamsRepository(theIndexedSpRepository, theId, theSearchParam, theResourceType);

		long hash = SearchParamHash.hashSearchParam(new PartitionSettings(), null, theResourceType, theSearchParam);
		if (myStorageSettings.isIndexStorageOptimized()) {
			// validated sp_name, res_type, sp_updated columns are null in DB
			runInTransaction(() -> {
				List<?> results = myEntityManager.createQuery("SELECT i FROM " + theIndexedSearchParamClass.getSimpleName() +
					" i WHERE i.myResourcePid = " + theId.getIdPartAsLong() + " AND i.myResourceType IS NULL " +
					"AND i.myParamName IS NULL AND i.myUpdated IS NULL AND i.myHashIdentity = " + hash, theIndexedSearchParamClass).getResultList();
				assertFalse(results.isEmpty());
				assertEquals(repositorySearchParams.size(), results.size());
			});
		} else {
			// validated sp_name, res_type, sp_updated columns are not null in DB
			runInTransaction(() -> {
				List<?> results = myEntityManager.createQuery("SELECT i FROM " + theIndexedSearchParamClass.getSimpleName() +
						" i WHERE i.myResourcePid = " + theId.getIdPartAsLong() + " AND i.myResourceType = '" + theResourceType +
						"' AND i.myParamName = '" + theSearchParam + "' AND i.myUpdated IS NOT NULL AND i.myHashIdentity = " + hash,
					theIndexedSearchParamClass).getResultList();
				assertFalse(results.isEmpty());
				assertEquals(repositorySearchParams.size(), results.size());
			});
		}
	}

	private List<? extends BaseResourceIndexedSearchParam> getAndValidateIndexedSearchParamsRepository(
		JpaRepository<? extends BaseResourceIndexedSearchParam, Long> theIndexedSpRepository,
		IIdType theId, String theSearchParam, String theResourceType) {

		List<? extends BaseResourceIndexedSearchParam> repositorySearchParams = theIndexedSpRepository.findAll()
			.stream()
			.filter(sp -> sp.getResourcePid().equals(theId.getIdPartAsLong()))
			.filter(sp -> theSearchParam.equals(sp.getParamName()))
			.toList();
		assertFalse(repositorySearchParams.isEmpty());

		repositorySearchParams.forEach(sp -> {
			assertEquals(theResourceType, sp.getResourceType());
			if (myStorageSettings.isIndexStorageOptimized()) {
				assertNull(sp.getUpdated());
			} else {
				assertNotNull(sp.getUpdated());
			}
		});

		return repositorySearchParams;
	}

	private void validateSearchContainsResource(IFhirResourceDao<? extends IBaseResource> theResourceDao,
												IIdType theId,
												String theSearchParam,
												BaseParam theParamValue) {
		SearchParameterMap searchParameterMap = new SearchParameterMap()
			.setLoadSynchronous(true)
			.add(theSearchParam, theParamValue);
		List<IIdType> listIds = toUnqualifiedVersionlessIds(theResourceDao.search(searchParameterMap));

		assertTrue(listIds.contains(theId));
	}

	private void executeReindex(String... theUrls) {
		ReindexJobParameters parameters = new ReindexJobParameters();
		for (String url : theUrls) {
			parameters.addUrl(url);
		}
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
		ourLog.info("Started reindex job with id {}", res.getInstanceId());
		myBatch2JobHelper.awaitJobCompletion(res);
	}

	// Additional existing tests with enabled IndexStorageOptimized
	@Nested
	public class IndexStorageOptimizedReindexStepTest extends ReindexStepTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setIndexStorageOptimized(true);
		}
	}

	@Nested
	public class IndexStorageOptimizedPartitioningSqlR4Test extends PartitioningSqlR4Test {
		@BeforeEach
		void setUp() {
			myStorageSettings.setIndexStorageOptimized(true);
		}
	}

	@Nested
	public class IndexStorageOptimizedFhirResourceDaoR4SearchMissingTest extends FhirResourceDaoR4SearchMissingTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setIndexStorageOptimized(true);
		}
	}

	@Nested
	public class IndexStorageOptimizedFhirResourceDaoR4QueryCountTest extends FhirResourceDaoR4QueryCountTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setIndexStorageOptimized(true);
		}
	}

	@Nested
	public class IndexStorageOptimizedFhirResourceDaoR4SearchNoFtTest extends FhirResourceDaoR4SearchNoFtTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setIndexStorageOptimized(true);
		}
	}
}
