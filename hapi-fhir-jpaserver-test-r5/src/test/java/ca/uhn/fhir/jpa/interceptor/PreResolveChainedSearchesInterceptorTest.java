package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onCurrentThread;
import static org.assertj.core.api.Assertions.assertThat;

class PreResolveChainedSearchesInterceptorTest extends BaseJpaR5Test {

	@Autowired
	public PreResolveChainedSearchesInterceptor mySvc;

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testSearch(boolean theRegisterInterceptor) {
		createPatient(withId("P0"), withIdentifier("http://system", "0"));
		createPatient(withId("P1"), withIdentifier("http://system", "1"));
		createObservation(withId("O0-0"), withSubject("Patient/P0"), withObservationCode("http://code", "0"));
		createObservation(withId("O0-1"), withSubject("Patient/P0"), withObservationCode("http://code", "1"));
		createObservation(withId("O1"), withSubject("Patient/P1"));

		if (theRegisterInterceptor) {
			registerInterceptor(mySvc);
		}

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Observation.SP_SUBJECT, new ReferenceParam("identifier", "http://system|0"))
			.add(Observation.SP_CODE, new TokenParam("http://code", "0"));
		IBundleProvider result = myObservationDao.search(map, newSrd());

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(result)).containsExactly("Observation/O0-0");

		if (theRegisterInterceptor) {
			assertThat(myCaptureQueriesListener).has(
				onCurrentThread()
					.selectCount(3)
					.selectSqlAtIndex(0).contains("AAAAA")
					.connectionCount(1)
					.commitCount(1)
					.noOtherCounts()
			);
		} else {
			assertThat(myCaptureQueriesListener).has(
				onCurrentThread()
					.selectCount(2)
					.connectionCount(1)
					.commitCount(1)
					.noOtherCounts()
			);
		}
	}

}
