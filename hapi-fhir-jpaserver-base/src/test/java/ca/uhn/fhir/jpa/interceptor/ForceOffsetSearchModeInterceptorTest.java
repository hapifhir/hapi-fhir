package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

public class ForceOffsetSearchModeInterceptorTest extends BaseResourceProviderR4Test {

	private ForceOffsetSearchModeInterceptor mySvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		mySvc = new ForceOffsetSearchModeInterceptor();
		ourRestServer.registerInterceptor(mySvc);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		ourRestServer.unregisterInterceptor(mySvc);
	}

	@Test
	public void testSearchUsingOffsetMode_UsingForceOffsetModeInterceptor() {
		mySvc.setDefaultCount(5);
		for (int i = 0; i < 10; i++) {
			createPatient(withId("A" + i), withActiveTrue());
		}

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronousUpTo(5);
		map.setOffset(0);
		map.add("active", new TokenParam("true"));

		// First page
		myCaptureQueriesListener.clear();
		Bundle outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.returnBundle(Bundle.class)
			.execute();
//		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
//			"Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4"
//		));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '5'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink("next").getUrl(), containsString("Patient?_count=5&_offset=5&active=true"));

		// Second page
		myCaptureQueriesListener.clear();
		outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.offset(5)
			.count(5)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/A5", "Patient/A6", "Patient/A7", "Patient/A8", "Patient/A9"
		));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '5'"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("offset '5'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink("next").getUrl(), containsString("Patient?_count=5&_offset=10&active=true"));

		// Third page (no results)

		myCaptureQueriesListener.clear();
		outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.offset(10)
			.count(5)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), empty());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '5'"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("offset '10'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

	}


}
