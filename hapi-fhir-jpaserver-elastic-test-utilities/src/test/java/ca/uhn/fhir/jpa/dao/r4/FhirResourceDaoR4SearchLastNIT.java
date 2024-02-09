package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.IHSearchEventListener;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.util.TestHSearchEventDispatcher;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FhirResourceDaoR4SearchLastNIT extends BaseR4SearchLastN {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchLastNIT.class);

	@BeforeEach
	public void enableAdvancedHSearchIndexing() {
		myStorageSettings.setLastNEnabled(true);
		myStorageSettings.setAdvancedHSearchIndexing(true);
		myHSearchEventDispatcher.register(mySearchEventListener);
		ourLog.info("enableAdvancedHSearchIndexing finished.  lastn {} advancedHSearchIndexing {}", myStorageSettings.isLastNEnabled(), myStorageSettings.isAdvancedHSearchIndexing());
	}

	@AfterEach
	public void reset() {
		SearchBuilder.setMaxPageSize50ForTest(false);
		myStorageSettings.setStoreResourceInHSearchIndex(new JpaStorageSettings().isStoreResourceInHSearchIndex());
		myStorageSettings.setAdvancedHSearchIndexing(new JpaStorageSettings().isAdvancedHSearchIndexing());
	}

	@Autowired
	private TestHSearchEventDispatcher myHSearchEventDispatcher;

	@Mock
	private IHSearchEventListener mySearchEventListener;



	@Test
	public void testLastNChunking() {

		// Set up search parameters that will return 75 Observations.
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient0Id.getValue());
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", patient1Id.getValue());
		ReferenceParam subjectParam3 = new ReferenceParam("Patient", "", patient2Id.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2, subjectParam3));
		TokenParam codeParam1 = new TokenParam(codeSystem, observationCd0);
		TokenParam codeParam2 = new TokenParam(codeSystem, observationCd1);
		params.add(Observation.SP_CODE, buildTokenAndListParam(codeParam1, codeParam2));

		params.setLastN(true);
		params.setLastNMax(100);

		Map<String, String[]> requestParameters = new HashMap<>();
		when(mySrd.getParameters()).thenReturn(requestParameters);

		// Set chunk size to 50
		SearchBuilder.setMaxPageSize50ForTest(true);

		myCaptureQueriesListener.clear();
		List<String> results = toUnqualifiedVersionlessIdValues(myObservationDao.observationsLastN(params, mockSrd(), null));
		assertThat(results).hasSize(75);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		List<String> queries = myCaptureQueriesListener
			.getSelectQueriesForCurrentThread()
			.stream()
			.map(t -> t.getSql(true, false))
			.toList();

		// Two chunked queries executed by the QueryIterator (in current thread) and two chunked queries to retrieve resources by PID.
		assertThat(queries).hasSize(4);

		// The first and third chunked queries should have a full complement of PIDs
		StringBuilder firstQueryPattern = new StringBuilder(".*RES_ID IN \\('[0-9]+'");
        firstQueryPattern.append(",'[0-9]+'".repeat(49));
		firstQueryPattern.append("\\).*");
		assertThat(queries.get(0).toUpperCase().replaceAll(" , ", ",")).matches(firstQueryPattern.toString());
		assertThat(queries.get(2).toUpperCase().replaceAll(" , ", ",")).matches(firstQueryPattern.toString());

		// the second and fourth chunked queries should be padded with "-1".
		StringBuilder secondQueryPattern = new StringBuilder(".*RES_ID IN \\('[0-9]+'");
        secondQueryPattern.append(",'[0-9]+'".repeat(24));
        secondQueryPattern.append(",'-1'".repeat(25));
		secondQueryPattern.append("\\).*");
		assertThat(queries.get(1).toUpperCase().replaceAll(" , ", ",")).matches(secondQueryPattern.toString());
		assertThat(queries.get(3).toUpperCase().replaceAll(" , ", ",")).matches(secondQueryPattern.toString());

	}

	@Test
	public void testLastN_onEnablingStoringObservationWithIndexMapping_shouldSkipLoadingResourceFromDB() throws IOException {
		// Enable flag
		myStorageSettings.setStoreResourceInHSearchIndex(true);

		// Create Data
		Patient pt = new Patient();
		pt.addName().setFamily("lastn-" + UUID.randomUUID()).addGiven("LastNPat1");
		IIdType patient1 = myPatientDao.create(pt, mockSrd()).getId().toUnqualifiedVersionless();
		List<IIdType> observationIds = createFiveObservationsForPatientCodeCategory(patient1, observationCd0, categoryCd0, 15);
		myElasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		myElasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);

		// Create lastN search params

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", patient1.getValue());
		params.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1));

		params.setLastN(true);
		params.setLastNMax(100);

		Map<String, String[]> requestParameters = new HashMap<>();
		when(mySrd.getParameters()).thenReturn(requestParameters);

		List<String> results = toUnqualifiedVersionlessIdValues(myObservationDao.observationsLastN(params, mockSrd(), null));

	}

	/**
	 * We pull the resources from Hibernate Search when LastN uses Hibernate Search
	 * Override the test verification to validate only one search was performed
	 */
	void verifyResourcesLoadedFromElastic(List<IIdType> theObservationIds, List<String> theResults) {
		Mockito.verify(mySearchEventListener, Mockito.times(1))
			.hsearchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
	}
}
