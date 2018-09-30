package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.test.util.AopTestUtils;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("Duplicates")
public class ResourceProviderSummaryModeR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderSummaryModeR4Test.class);
	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcRaw;

	@Override
	@After
	public void after() throws Exception {
		super.after();
		myDaoConfig.setCountSearchResultsUpTo(null);
		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(SearchCoordinatorSvcImpl.DEFAULT_SYNC_SIZE);
	}

	@Override
	public void before() throws Exception {
		super.before();
		myDaoConfig.setCountSearchResultsUpTo(5);

		mySearchCoordinatorSvcRaw = AopTestUtils.getTargetObject(mySearchCoordinatorSvc);
		mySearchCoordinatorSvcRaw.setLoadingThrottleForUnitTests(250);
		mySearchCoordinatorSvcRaw.setSyncSizeForUnitTests(5);

		runInTransaction(() -> {
			for (int i = 0; i < 104; i++) {
				Patient p = new Patient();
				p.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
				p.getText().getDiv().setValue("<div>i am a div</div>");
				p.addName().setFamily("FAM" + i);
				p.setActive(true);
				myPatientDao.create(p);
			}
		});

	}

	/**
	 * Count only - Should include count but no results
	 */
	@Test
	public void testSearchWithCount() {
		Bundle outcome = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.ACTIVE.exactly().code("true"))
			.summaryMode(SummaryEnum.COUNT)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(new Integer(104), outcome.getTotalElement().getValue());
		assertEquals(0, outcome.getEntry().size());
	}

	/**
	 * Count and data - Should include both a count and the data portions of results
	 */
	@Test
	public void testSearchWithCountAndData() {
		Bundle outcome = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.ACTIVE.exactly().code("true"))
			.summaryMode(SummaryEnum.COUNT, SummaryEnum.DATA)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(new Integer(104), outcome.getTotalElement().getValue());
		assertEquals(10, outcome.getEntry().size());
	}

	/**
	 * No summary mode - Should return the first page of results but not
	 * have the total available yet
	 */
	@Test
	public void testSearchWithNoSummaryMode() {
		Bundle outcome = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.ACTIVE.exactly().code("true"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals(null, outcome.getTotalElement().getValue());
		assertEquals(10, outcome.getEntry().size());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
