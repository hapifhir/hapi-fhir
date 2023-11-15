package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ForceOffsetSearchModeInterceptorTest extends BaseResourceProviderR4Test {

	private ForceOffsetSearchModeInterceptor mySvc;
	private Integer myInitialDefaultPageSize;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		mySvc = new ForceOffsetSearchModeInterceptor();
		myInterceptorRegistry.registerInterceptor(mySvc);
		myInitialDefaultPageSize = myServer.getDefaultPageSize();
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myInterceptorRegistry.unregisterInterceptor(mySvc);
		myServer.setDefaultPageSize(myInitialDefaultPageSize);
	}

	@Test
	public void testSearch_NoExplicitCount() {
		myServer.setDefaultPageSize(5);

		for (int i = 0; i < 10; i++) {
			createPatient(withId("A" + i), withActiveTrue());
		}

		// First page
		myCaptureQueriesListener.clear();
		Bundle outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4"
		));
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
		Bundle outcome3 = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.offset(10)
			.count(5)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome3).toString(), toUnqualifiedVersionlessIdValues(outcome3), empty());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '5'"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("offset '10'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());

		assertNull(outcome3.getLink("next"), () -> outcome3.getLink("next").getUrl());

	}

	@Test
	public void testPagingNextLink_whenAllResourcesHaveBeenReturned_willNotBePresent(){

		myServer.setDefaultPageSize(5);

		for (int i = 0; i < 10; i++) {
			createPatient(withId("A" + i), withActiveTrue());
		}

		Bundle outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.returnBundle(Bundle.class)
			.execute();

		assertThat(outcome.getEntry(), hasSize(5));

		Bundle secondPageBundle = myClient.loadPage().next(outcome).execute();

		assertThat(secondPageBundle.getEntry(), hasSize(5));

		Bundle thirdPageBundle = myClient.loadPage().next(secondPageBundle).execute();

		assertThat(thirdPageBundle.getEntry(), hasSize(0));
		assertNull(thirdPageBundle.getLink("next"), () -> thirdPageBundle.getLink("next").getUrl());

	}



	@Test
	public void testSearch_WithExplicitCount() {
		myServer.setDefaultPageSize(5);

		for (int i = 0; i < 10; i++) {
			createPatient(withId("A" + i), withActiveTrue());
		}

		// First page
		myCaptureQueriesListener.clear();
		Bundle outcome = myClient
			.search()
			.forResource("Patient")
			.where(Patient.ACTIVE.exactly().code("true"))
			.count(7)
			.returnBundle(Bundle.class)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4", "Patient/A5", "Patient/A6"
		));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '7'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink(Constants.LINK_NEXT).getUrl(), containsString("Patient?_count=7&_offset=7&active=true"));
		assertNull(outcome.getLink(Constants.LINK_PREVIOUS));

		// Second page

		myCaptureQueriesListener.clear();
		outcome = myClient
			.loadPage()
			.next(outcome)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/A7", "Patient/A8", "Patient/A9"
		));
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false), containsString("limit '7'"));
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink(Constants.LINK_PREVIOUS).getUrl(), containsString("Patient?_count=7&_offset=0&active=true"));

	}


	@Test
	public void testSearch_LoadSynchronousUpToOverridesConfig() {
		myServer.setDefaultPageSize(5);
		mySvc.setDefaultCount(5);

		for (int i = 0; i < 10; i++) {
			createPatient(withId("A" + i), withActiveTrue());
		}

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(9);
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4", "Patient/A5", "Patient/A6", "Patient/A7", "Patient/A8"
		));
		assertEquals(9, outcome.size());
		assertEquals(null, outcome.getCurrentPageOffset());
		assertEquals(null, outcome.getCurrentPageSize());

	}


	@Test
	public void testSearch_NoLoadSynchronous() {
		myServer.setDefaultPageSize(5);
		mySvc.setDefaultCount(5);

		for (int i = 0; i < 10; i++) {
			createPatient(withId("A" + i), withActiveTrue());
		}

		SearchParameterMap params = new SearchParameterMap();
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		assertThat(toUnqualifiedVersionlessIdValues(outcome).toString(), toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(
			"Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4"
		));
		assertNull(outcome.size());
		assertEquals(0, outcome.getCurrentPageOffset());
		assertEquals(5, outcome.getCurrentPageSize());

	}
}
