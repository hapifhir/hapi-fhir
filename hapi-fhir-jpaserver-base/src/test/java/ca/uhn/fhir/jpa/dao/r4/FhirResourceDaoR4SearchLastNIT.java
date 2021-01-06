package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FhirResourceDaoR4SearchLastNIT extends BaseR4SearchLastN {

	@AfterEach
	public void resetMaximumPageSize() {
		SearchBuilder.setMaxPageSize50ForTest(false);
	}

	@Test
	public void testLastNChunking() {

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

}
