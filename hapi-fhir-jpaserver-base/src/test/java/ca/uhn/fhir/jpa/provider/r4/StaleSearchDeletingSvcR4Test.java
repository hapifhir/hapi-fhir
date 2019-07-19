package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.StaleSearchDeletingSvcImpl;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.AopTestUtils;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class StaleSearchDeletingSvcR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcR4Test.class);

	@Override
	@After()
	public void after() throws Exception {
		super.after();
		StaleSearchDeletingSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(myStaleSearchDeletingSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(StaleSearchDeletingSvcImpl.DEFAULT_CUTOFF_SLACK);
		StaleSearchDeletingSvcImpl.setMaximumResultsToDeleteForUnitTest(StaleSearchDeletingSvcImpl.DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_STMT);
		StaleSearchDeletingSvcImpl.setMaximumResultsToDeleteInOnePassForUnitTest(StaleSearchDeletingSvcImpl.DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS);
	}

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		StaleSearchDeletingSvcImpl staleSearchDeletingSvc = AopTestUtils.getTargetObject(myStaleSearchDeletingSvc);
		staleSearchDeletingSvc.setCutoffSlackForUnitTest(0);
	}

	@Test
	public void testEverythingInstanceWithContentFilter() throws Exception {

		for (int i = 0; i < 20; i++) {
			Patient pt1 = new Patient();
			pt1.addName().setFamily("Everything").addGiven("Arthur");
			myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();
		}

		//@formatter:off
		IClientExecutable<IQuery<Bundle>, Bundle> search = ourClient
			.search()
			.forResource(Patient.class)
			.where(Patient.NAME.matches().value("Everything"))
			.returnBundle(Bundle.class);
		//@formatter:on

		Bundle resp1 = search.execute();

		for (int i = 0; i < 20; i++) {
			search.execute();
		}

		BundleLinkComponent nextLink = resp1.getLink("next");
		assertNotNull(nextLink);
		String nextLinkUrl = nextLink.getUrl();
		assertThat(nextLinkUrl, not(blankOrNullString()));

		Bundle resp2 = ourClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resp2));

		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		ourClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();

		Thread.sleep(20);
		myDaoConfig.setExpireSearchResultsAfterMillis(10);
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();

		try {
			ourClient.search().byUrl(nextLinkUrl).returnBundle(Bundle.class).execute();
			fail();
		} catch (ResourceGoneException e) {
			assertThat(e.getMessage(), containsString("does not exist and may have expired"));
		}
	}

	@Test
	public void testDeleteVeryLargeSearch() {
		StaleSearchDeletingSvcImpl.setMaximumResultsToDeleteForUnitTest(10);
		StaleSearchDeletingSvcImpl.setMaximumResultsToDeleteInOnePassForUnitTest(10);

		runInTransaction(() -> {
			Search search = new Search();
			search.setStatus(SearchStatusEnum.FINISHED);
			search.setUuid(UUID.randomUUID().toString());
			search.setCreated(DateUtils.addDays(new Date(), -10000));
			search.setSearchType(SearchTypeEnum.SEARCH);
			search.setResourceType("Patient");
			search.setSearchLastReturned(DateUtils.addDays(new Date(), -10000));
			search = mySearchEntityDao.save(search);

			for (int i = 0; i < 15; i++) {
				ResourceTable resource = new ResourceTable();
				resource.setPublished(new Date());
				resource.setUpdated(new Date());
				resource.setResourceType("Patient");
				resource = myResourceTableDao.saveAndFlush(resource);

				SearchResult sr = new SearchResult(search);
				sr.setOrder(i);
				sr.setResourcePid(resource.getId());
				mySearchResultDao.save(sr);
			}


			SearchInclude si = new SearchInclude(search, "Patient:name", false, false);
			mySearchIncludeDao.save(si);

		});

		// It should take two passes to delete the search fully
		assertEquals(1, mySearchEntityDao.count());
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		assertEquals(1, mySearchEntityDao.count());
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		assertEquals(0, mySearchEntityDao.count());

	}

	@Test
	public void testDeleteVerySmallSearch() {
		StaleSearchDeletingSvcImpl.setMaximumResultsToDeleteForUnitTest(10);

		runInTransaction(() -> {
			Search search = new Search();
			search.setStatus(SearchStatusEnum.FINISHED);
			search.setUuid(UUID.randomUUID().toString());
			search.setCreated(DateUtils.addDays(new Date(), -10000));
			search.setSearchType(SearchTypeEnum.SEARCH);
			search.setResourceType("Patient");
			search.setSearchLastReturned(DateUtils.addDays(new Date(), -10000));
			search = mySearchEntityDao.save(search);

		});

		// It should take one pass to delete the search fully
		assertEquals(1, mySearchEntityDao.count());
		myStaleSearchDeletingSvc.pollForStaleSearchesAndDeleteThem();
		assertEquals(0, mySearchEntityDao.count());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
