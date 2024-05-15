package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FhirResourceDaoR4IndexStorageOptimizedTest extends BaseJpaR4Test {

	@Autowired
	private IJobCoordinator myJobCoordinator;

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
			Location.SP_NEAR, "Location", new SpecialParam().setValue("43.7|79.4"));
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
			Patient.SP_BIRTHDATE, "Patient", new DateParam("2021-02-22"));
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
			RiskAssessment.SP_PROBABILITY, "RiskAssessment", new NumberParam(15));
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
			Observation.SP_VALUE_QUANTITY, "Observation", new QuantityParam(123));
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
		validateAndReindex(theIsIndexStorageOptimized, mySubstanceDao,
			myResourceIndexedSearchParamQuantityNormalizedDao, id, Substance.SP_QUANTITY, "Substance", quantityParam);
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
			Patient.SP_ADDRESS, "Patient", new StringParam("123 Main Street"));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testTokenIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(true);
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(observation, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myObservationDao, myResourceIndexedSearchParamTokenDao, id,
			Observation.SP_STATUS, "Observation", new TokenParam("final"));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testUriIndexedSearchParam_searchAndReindex_searchParamUpdatedCorrectly(boolean theIsIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexStorageOptimized(true);
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl("http://vs");
		IIdType id = myValueSetDao.create(valueSet, mySrd).getId().toUnqualifiedVersionless();

		validateAndReindex(theIsIndexStorageOptimized, myValueSetDao, myResourceIndexedSearchParamUriDao, id,
			ValueSet.SP_URL, "ValueSet", new UriParam("http://vs"));
	}

	private void validateAndReindex(boolean theIsIndexStorageOptimized, IFhirResourceDao<? extends IBaseResource> theResourceDao,
									JpaRepository<? extends BaseResourceIndexedSearchParam, Long> theIndexedSpRepository, IIdType theId,
									String theSearchParam, String theResourceType, BaseParam theParamValue) {
		// validate
		validateSearchContainsResource(theResourceDao, theId, theSearchParam, theParamValue);
		validateSearchParams(theIndexedSpRepository, theId, theSearchParam, theResourceType);

		// switch on/off storage optimization and run $reindex
		myStorageSettings.setIndexStorageOptimized(!theIsIndexStorageOptimized);
		executeReindex(theResourceType + "?");

		// validate again
		validateSearchContainsResource(theResourceDao, theId, theSearchParam, theParamValue);
		validateSearchParams(theIndexedSpRepository, theId, theSearchParam, theResourceType);
	}

	private void validateSearchParams(JpaRepository<? extends BaseResourceIndexedSearchParam, Long> theIndexedSpRepository,
									  IIdType theId, String theSearchParam, String theResourceType) {
		if (myStorageSettings.isIndexStorageOptimized()) {
			List<? extends BaseResourceIndexedSearchParam> list = theIndexedSpRepository.findAll().stream()
				.filter(sp -> sp.getResourcePid().equals(theId.getIdPartAsLong()))
				.toList();
			assertFalse(list.isEmpty());
			list.forEach(sp -> {
				assertNull(sp.getParamName());
				assertNull(sp.getResourceType());
				assertNull(sp.getUpdated());
			});
		} else {
			List<? extends BaseResourceIndexedSearchParam> list = theIndexedSpRepository.findAll().stream()
				.filter(sp -> sp.getResourcePid().equals(theId.getIdPartAsLong()))
				.filter(sp -> theSearchParam.equals(sp.getParamName()))
				.toList();
			assertFalse(list.isEmpty());
			list.forEach(sp -> {
				assertEquals(theResourceType, sp.getResourceType());
				assertNotNull(sp.getUpdated());
			});
		}
	}

	private void validateSearchContainsResource(IFhirResourceDao<? extends IBaseResource> theResourceDao,
												IIdType theId,
												String theSearchParam,
												BaseParam theParamValue) {
		SearchParameterMap searchParameterMap = new SearchParameterMap()
			.setLoadSynchronous(true)
			.add(theSearchParam, theParamValue);
		List<IIdType> listIds = toUnqualifiedVersionlessIds(theResourceDao.search(searchParameterMap));

		assertThat(listIds, containsInAnyOrder(theId));
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
}
