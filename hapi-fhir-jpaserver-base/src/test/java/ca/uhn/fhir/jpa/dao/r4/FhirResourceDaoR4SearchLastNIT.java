package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchSvcImpl;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FhirResourceDaoR4SearchLastNIT extends BaseR4SearchLastN {
	@AfterEach
	public void reset() {
		SearchBuilder.setMaxPageSize50ForTest(false);
		myDaoConfig.setStoreResourceInLuceneIndex(new DaoConfig().isStoreResourceInLuceneIndex());
	}

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
		assertEquals(75, results.size());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		List<String> queries = myCaptureQueriesListener
			.getSelectQueriesForCurrentThread()
			.stream()
			.map(t -> t.getSql(true, false))
			.collect(Collectors.toList());

		// Two chunked queries executed by the QueryIterator (in current thread) and two chunked queries to retrieve resources by PID.
		assertEquals(4, queries.size());

		// The first and third chunked queries should have a full complement of PIDs
		StringBuilder firstQueryPattern = new StringBuilder(".*RES_ID IN \\('[0-9]+'");
		for (int pidIndex = 1; pidIndex < 50; pidIndex++) {
			firstQueryPattern.append(",'[0-9]+'");
		}
		firstQueryPattern.append("\\).*");
		assertThat(queries.get(0).toUpperCase().replaceAll(" , ", ","), matchesPattern(firstQueryPattern.toString()));
		assertThat(queries.get(2).toUpperCase().replaceAll(" , ", ","), matchesPattern(firstQueryPattern.toString()));

		// the second and fourth chunked queries should be padded with "-1".
		StringBuilder secondQueryPattern = new StringBuilder(".*RES_ID IN \\('[0-9]+'");
		for (int pidIndex = 1; pidIndex < 25; pidIndex++) {
			secondQueryPattern.append(",'[0-9]+'");
		}
		for (int pidIndex = 0; pidIndex < 25; pidIndex++) {
			secondQueryPattern.append(",'-1'");
		}
		secondQueryPattern.append("\\).*");
		assertThat(queries.get(1).toUpperCase().replaceAll(" , ", ","), matchesPattern(secondQueryPattern.toString()));
		assertThat(queries.get(3).toUpperCase().replaceAll(" , ", ","), matchesPattern(secondQueryPattern.toString()));

	}

	@Test
	public void testLastN_onEnablingStoringObservationWithIndexMapping_shouldSkipLoadingResourceFromDB() throws IOException {
		// Enable flag
		myDaoConfig.setStoreResourceInLuceneIndex(true);

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
		verifyResourcesLoadedFromElastic(observationIds, results);

	}

	void verifyResourcesLoadedFromElastic(List<IIdType> theObservationIds, List<String> theResults) {
		List<ResourcePersistentId> expectedArgumentPids = ResourcePersistentId.fromLongList(
			theObservationIds.stream().map(IIdType::getIdPartAsLong).collect(Collectors.toList())
		);
		ArgumentCaptor<List<ResourcePersistentId>> actualPids = ArgumentCaptor.forClass(List.class);
		verify(myElasticsearchSvc, times(1)).getObservationResources(actualPids.capture());
		assertThat(actualPids.getValue(), is(expectedArgumentPids));

		List<String> expectedObservationList = theObservationIds.stream()
			.map(id -> id.toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertEquals(expectedObservationList, theResults);

	}

}
