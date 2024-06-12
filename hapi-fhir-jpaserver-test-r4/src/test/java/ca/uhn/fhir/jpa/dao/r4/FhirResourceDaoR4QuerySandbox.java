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
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

		assertThat(myCapturedQueries).isNotEmpty();
	}

	@Test
	void testQueryByPid() {

		// sentinel for over-match
		myDataBuilder.createPatient();

		String id = myDataBuilder.createPatient(
			myDataBuilder.withBirthdate("1971-01-01"),
			myDataBuilder.withActiveTrue(),
			myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsOnly("search by server assigned id", "Patient?_pid=" + id, id);
	}

	@Test
	void testQueryByPid_withOtherSPAvoidsResourceTable() {
		// sentinel for over-match
		myDataBuilder.createPatient();

		String id = myDataBuilder.createPatient(
			myDataBuilder.withBirthdate("1971-01-01"),
			myDataBuilder.withActiveTrue(),
			myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsOnly("search by server assigned id", "Patient?name=smith&_pid=" + id, id);
	}

	@Test
	void testSortByPid() {

		String id1 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithy")).getIdPart();
		String id2 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithwick")).getIdPart();
		String id3 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smith")).getIdPart();

		myTestDaoSearch.assertSearchFindsInOrder("sort by server assigned id", "Patient?family=smith&_sort=_pid", id1,id2,id3);
		myTestDaoSearch.assertSearchFindsInOrder("reverse sort by server assigned id", "Patient?family=smith&_sort=-_pid", id3,id2,id1);
	}

	@Test
	void testChainedSort() {
		final IIdType practitionerId = myDataBuilder.createPractitioner(myDataBuilder.withFamily("Jones"));

		final String id1 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithy")).getIdPart();
		final String id2 = myDataBuilder.createPatient(myDataBuilder.withFamily("Smithwick")).getIdPart();
		final String id3 = myDataBuilder.createPatient(
			myDataBuilder.withFamily("Smith"),
			myDataBuilder.withReference("generalPractitioner", practitionerId)).getIdPart();


		final IBundleProvider iBundleProvider = myTestDaoSearch.searchForBundleProvider("Patient?_total=ACCURATE&_sort=Practitioner:general-practitioner.family");
		assertEquals(3, iBundleProvider.size());

		final List<IBaseResource> allResources = iBundleProvider.getAllResources();
		assertEquals(3, iBundleProvider.size());
		assertEquals(3, allResources.size());

		final List<String> actualIds = allResources.stream().map(IBaseResource::getIdElement).map(IIdType::getIdPart).toList();
		assertTrue(actualIds.containsAll(List.of(id1, id2, id3)));
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
