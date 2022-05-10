package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FhirResourceDaoR4SearchLastNAsyncIT extends BaseR4SearchLastN {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchLastNAsyncIT.class);
	@Autowired
	protected DaoConfig myDaoConfig;
	private List<Integer> originalPreFetchThresholds;
	@Autowired
	private ISearchDao mySearchDao;

	@BeforeEach
	public void before() {

		RestfulServer myServer = new RestfulServer(myFhirCtx);
		myServer.setPagingProvider(myDatabaseBackedPagingProvider);

		when(mySrd.getServer()).thenReturn(myServer);

		// Set pre-fetch sizes small so that most tests are forced to do multiple fetches.
		// This will allow testing a common use case where result set is larger than first fetch size but smaller than the normal query chunk size.
		originalPreFetchThresholds = myDaoConfig.getSearchPreFetchThresholds();
		List<Integer> mySmallerPreFetchThresholds = new ArrayList<>();
		mySmallerPreFetchThresholds.add(20);
		mySmallerPreFetchThresholds.add(400);
		mySmallerPreFetchThresholds.add(-1);
		myDaoConfig.setSearchPreFetchThresholds(mySmallerPreFetchThresholds);

		SearchBuilder.setMaxPageSize50ForTest(true);

	}

	@AfterEach
	public void after() {
		myDaoConfig.setSearchPreFetchThresholds(originalPreFetchThresholds);
		SearchBuilder.setMaxPageSize50ForTest(false);
	}

	@Test
	public void testLastNChunking() {

		runInTransaction(() -> {
			for (Search search : mySearchDao.findAll()) {
				mySearchDao.updateDeleted(search.getId(), true);
			}
		});

		// Set up search parameters that will return 75 Observations.
		SearchParameterMap params = new SearchParameterMap();
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

		// Expand default fetch sizes to ensure all observations are returned in first page:
		List<Integer> myBiggerPreFetchThresholds = new ArrayList<>();
		myBiggerPreFetchThresholds.add(100);
		myBiggerPreFetchThresholds.add(1000);
		myBiggerPreFetchThresholds.add(-1);
		myDaoConfig.setSearchPreFetchThresholds(myBiggerPreFetchThresholds);

		myCaptureQueriesListener.clear();
		List<String> results = toUnqualifiedVersionlessIdValues(myObservationDao.observationsLastN(params, mockSrd(), null));
		assertEquals(75, results.size());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		List<String> queries = myCaptureQueriesListener
			.getSelectQueriesForCurrentThread()
			.stream()
			.map(t -> t.getSql(true, false))
			.collect(Collectors.toList());

		ourLog.info("Queries:\n * " + String.join("\n * ", queries));

		// 3 queries to actually perform the search
		// 1 query to lookup up Search from cache, and 2 chunked queries to retrieve resources by PID.
		assertEquals(6, queries.size());

		// The first chunked query should have a full complement of PIDs
		StringBuilder firstQueryPattern = new StringBuilder(".*RES_ID in \\('[0-9]+'");
		for (int pidIndex = 1; pidIndex < 50; pidIndex++) {
			firstQueryPattern.append(" , '[0-9]+'");
		}
		firstQueryPattern.append("\\).*");
		assertThat(queries.get(4), matchesPattern(firstQueryPattern.toString()));

		// the second chunked query should be padded with "-1".
		StringBuilder secondQueryPattern = new StringBuilder(".*RES_ID in \\('[0-9]+'");
		for (int pidIndex = 1; pidIndex < 25; pidIndex++) {
			secondQueryPattern.append(" , '[0-9]+'");
		}
		for (int pidIndex = 0; pidIndex < 25; pidIndex++) {
			secondQueryPattern.append(" , '-1'");
		}
		secondQueryPattern.append("\\).*");
		assertThat(queries.get(5), matchesPattern(secondQueryPattern.toString()));

	}

}
