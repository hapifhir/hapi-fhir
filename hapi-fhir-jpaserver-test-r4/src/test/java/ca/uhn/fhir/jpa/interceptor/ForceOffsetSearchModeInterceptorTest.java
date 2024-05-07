package ca.uhn.fhir.jpa.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).as(toUnqualifiedVersionlessIdValues(outcome).toString()).containsExactlyInAnyOrder("Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4");
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("fetch first '6' rows only");
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink("next").getUrl()).contains("Patient?_count=5&_offset=5&active=true");

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
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).as(toUnqualifiedVersionlessIdValues(outcome).toString()).containsExactlyInAnyOrder("Patient/A5", "Patient/A6", "Patient/A7", "Patient/A8", "Patient/A9");
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("fetch next '6' rows only");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("offset '5'");
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertNull(outcome.getLink("next"));
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

		assertThat(outcome.getEntry()).hasSize(5);

		Bundle secondPageBundle = myClient.loadPage().next(outcome).execute();

		assertThat(secondPageBundle.getEntry()).hasSize(5);

		assertNull(secondPageBundle.getLink("next"));
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
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).as(toUnqualifiedVersionlessIdValues(outcome).toString()).containsExactlyInAnyOrder("Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4", "Patient/A5", "Patient/A6");
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("fetch first '8' rows only");
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink(Constants.LINK_NEXT).getUrl()).contains("Patient?_count=7&_offset=7&active=true");
		assertNull(outcome.getLink(Constants.LINK_PREVIOUS));

		// Second page

		myCaptureQueriesListener.clear();
		outcome = myClient
			.loadPage()
			.next(outcome)
			.execute();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).as(toUnqualifiedVersionlessIdValues(outcome).toString()).containsExactlyInAnyOrder("Patient/A7", "Patient/A8", "Patient/A9");
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0");
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false)).contains("fetch next '8' rows only");
		assertEquals(0, myCaptureQueriesListener.countInsertQueries());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueries());
		assertEquals(0, myCaptureQueriesListener.countDeleteQueries());
		assertEquals(1, myCaptureQueriesListener.countCommits());
		assertEquals(0, myCaptureQueriesListener.countRollbacks());

		assertThat(outcome.getLink(Constants.LINK_PREVIOUS).getUrl()).contains("Patient?_count=7&_offset=0&active=true");

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

		assertThat(toUnqualifiedVersionlessIdValues(outcome)).as(toUnqualifiedVersionlessIdValues(outcome).toString()).containsExactlyInAnyOrder("Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4", "Patient/A5", "Patient/A6", "Patient/A7", "Patient/A8");
		assertEquals(9, outcome.size());
		assertNull(outcome.getCurrentPageOffset());
		assertNull(outcome.getCurrentPageSize());

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

		assertThat(toUnqualifiedVersionlessIdValues(outcome)).as(toUnqualifiedVersionlessIdValues(outcome).toString()).containsExactlyInAnyOrder("Patient/A0", "Patient/A1", "Patient/A2", "Patient/A3", "Patient/A4");
		assertNull(outcome.size());
		assertEquals(0, outcome.getCurrentPageOffset());
		assertEquals(5, outcome.getCurrentPageSize());

	}
}
