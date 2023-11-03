package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

/**
 * Sandbox for implementing queries.
 * This will NOT run during the build - use this class as a convenient
 * place to explore, debug, profile, and optimize.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	TestR4Config.class,
	TestHSearchAddInConfig.NoFT.class,
	DaoTestDataBuilder.Config.class,
	TestDaoSearch.Config.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestExecutionListeners(listeners = {
	DependencyInjectionTestExecutionListener.class
	, FhirResourceDaoR4QuerySandbox.TestDirtiesContextTestExecutionListener.class
})
public class FhirResourceDaoR4QuerySandbox extends BaseJpaTest {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4QuerySandbox.class);

	@Autowired
	PlatformTransactionManager myTxManager;
	@Autowired
	FhirContext myFhirCtx;
	@RegisterExtension
	@Autowired
	DaoTestDataBuilder myDataBuilder;
	@Autowired
	TestDaoSearch myTestDaoSearch;

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	List<String> myCapturedQueries = new ArrayList<>();
	@BeforeEach
	void registerLoggingInterceptor() {
		registerInterceptor(new Object(){
			@Hook(Pointcut.JPA_PERFTRACE_RAW_SQL)
			public void captureSql(RequestDetails theRequestDetails, SqlQueryList theQueries) {
				for (SqlQuery next : theQueries) {
					String output = next.getSql(true, true, true);
					ourLog.info("Query: {}", output);
					myCapturedQueries.add(output);
				}
			}
		});

	}

	@Test
	public void testSearches_logQueries() {
		myDataBuilder.createPatient();

		myTestDaoSearch.searchForIds("Patient?name=smith");

		assertThat(myCapturedQueries, not(empty()));
	}

	@Test
	void testQueryByPid() {

		// sentinel for over-match
		myDataBuilder.createPatient();

		String id = myDataBuilder.createPatient(
			myDataBuilder.withBirthdate("1971-01-01"),
			myDataBuilder.withActiveTrue(),
			myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsOnly("search by server assigned id", "Patient?__pid=" + id, id);
	}

	@Test
	void testQueryByPid_withOtherSPAvoidsResourceTable() {
		// sentinel for over-match
		myDataBuilder.createPatient();

		String id = myDataBuilder.createPatient(
			myDataBuilder.withBirthdate("1971-01-01"),
			myDataBuilder.withActiveTrue(),
			myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsOnly("search by server assigned id", "Patient?name=smith&__pid=" + id, id);
	}

	@Test
	void testSortByPid() {

		String id1 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithy")).getIdPart();
		String id2 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithwick")).getIdPart();
		String id3 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsInOrder("sort by server assigned id", "Patient?family=smith&_sort=__pid", id1,id2,id3);
		myTestDaoSearch.assertSearchFindsInOrder("reverse sort by server assigned id", "Patient?family=smith&_sort=-__pid", id3,id2,id1);
	}

	public static final class TestDirtiesContextTestExecutionListener extends DirtiesContextTestExecutionListener {

		@Override
		protected void beforeOrAfterTestClass(TestContext testContext, DirtiesContext.ClassMode requiredClassMode) throws Exception {
			if (!testContext.getTestClass().getName().contains("$")) {
				super.beforeOrAfterTestClass(testContext, requiredClassMode);
			}
		}
	}

}
